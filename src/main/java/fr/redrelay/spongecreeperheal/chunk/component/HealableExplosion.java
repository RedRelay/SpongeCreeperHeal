package fr.redrelay.spongecreeperheal.chunk.component;

import org.spongepowered.api.data.*;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.world.BlockChangeFlags;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * HealableExplosion represent a memorized list of HealableEntry after an Explosion.
 * All HealableEntry of a HealableExplosion should be on the same chunk
 * It means a multi chunk explosion create multiple HealableExplosion for each chunk
 * Each HealTask ticks decrements first HealableEntry of HealableExplosion
 * When HealableEntry timer reach 0, it is restored and dropped from the list
 * Empty HealableExplosion must not be keep in memory to avoid leak
 */
public class HealableExplosion implements DataSerializable {
    private final LinkedList<HealableEntry> entries = new LinkedList<>();

    public HealableExplosion(Collection<HealableEntry> entries){
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
     * Used to build HealableExplosion
     */
    public static class HealableExplosionBuilder extends AbstractDataBuilder<HealableExplosion> {

        public HealableExplosionBuilder() {
            super(HealableExplosion.class, 0);
        }

        @Override
        protected Optional<HealableExplosion> buildContent(DataView data) throws InvalidDataException {
            final List<HealableEntry> entries = data.getSerializableList(DataQuery.of("entries"), HealableEntry.class)
                    .orElseThrow(() -> new InvalidDataException(HealableExplosion.class.getSimpleName()+" : Missing \"entries\" data"));
            return Optional.of(new HealableExplosion(entries));
        }
    }
}
