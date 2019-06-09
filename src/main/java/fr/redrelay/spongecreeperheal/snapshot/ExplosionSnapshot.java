package fr.redrelay.spongecreeperheal.snapshot;

import fr.redrelay.spongecreeperheal.block.HealableEntry;
import org.spongepowered.api.data.*;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.world.BlockChangeFlags;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * ExplosionSnapshot represent a memorized list of HealableEntry after an Explosion.
 * All HealableEntry of a ExplosionSnapshot should be on the same chunk
 * It means a multi chunk explosion create multiple ExplosionSnapshot for each chunk
 * Each HealTask ticks decrements first HealableEntry of ExplosionSnapshot
 * When HealableEntry timer reach 0, it is restored and dropped from the list
 * Empty ExplosionSnapshot must not be keep in memory to avoid leak
 */
public class ExplosionSnapshot implements DataSerializable {
    private final LinkedList<HealableEntry> entries = new LinkedList<>();

    public ExplosionSnapshot(Collection<HealableEntry> entries){
        this.entries.addAll(entries);
    }

    public LinkedList<HealableEntry> getEntries() {
        return entries;
    }

    public void tick() {
        entries.getFirst().decreaseRemainingTime();
        if(entries.getFirst().getRemainingTime() == 0) {
            entries.removeFirst().getBlockSnapshot().restore(true, BlockChangeFlags.NEIGHBOR_PHYSICS_OBSERVER);
        }
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer data = DataContainer.createNew();
        data.set(Queries.CONTENT_VERSION, getContentVersion());
        data.set(DataQuery.of("entries"), entries);
        return data;
    }

    /**
     * Used to build ExplosionSnapshot
     */
    public static class HealableExplosionBuilder extends AbstractDataBuilder<ExplosionSnapshot> {

        public HealableExplosionBuilder() {
            super(ExplosionSnapshot.class, 0);
        }

        @Override
        protected Optional<ExplosionSnapshot> buildContent(DataView data) throws InvalidDataException {
            final List<HealableEntry> entries = data.getSerializableList(DataQuery.of("entries"), HealableEntry.class)
                    .orElseThrow(() -> new InvalidDataException(ExplosionSnapshot.class.getSimpleName()+" : Missing \"entries\" data"));
            return Optional.of(new ExplosionSnapshot(entries));
        }
    }
}
