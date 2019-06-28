package fr.redrelay.spongecreeperheal.explosion;

import fr.redrelay.spongecreeperheal.healable.ChunkedHealable;
import org.spongepowered.api.data.*;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * ExplosionSnapshot represent a memorized list of Healable after an Explosion.
 * All Healable of a ExplosionSnapshot should be on the same chunk
 * It means a multi chunk explosion create multiple ExplosionSnapshot for each chunk
 * Each HealTask ticks decrements first Healable of ExplosionSnapshot
 * When Healable timer reach 0, it is restored and dropped from the list
 * Empty ExplosionSnapshot must not be keep in memory to avoid leak
 */
public class ExplosionSnapshot implements DataSerializable {

    private static class Keys {
        final static DataQuery HEALABLES = DataQuery.of("entries");
    }

    private final LinkedList<ChunkedHealable> entries = new LinkedList<>();

    public ExplosionSnapshot(Collection<ChunkedHealable> entries){
        this.entries.addAll(entries);
    }

    public LinkedList<ChunkedHealable> getEntries() {
        return entries;
    }

    public void tick() {
        entries.getFirst().decreaseRemainingTime();
        if(entries.getFirst().getRemainingTime() == 0) {
            entries.removeFirst().restore();
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
        data.set(Keys.HEALABLES, entries);
        return data;
    }

    /**
     * Used to build ExplosionSnapshot
     */
    public static class Builder extends AbstractDataBuilder<ExplosionSnapshot> {

        public Builder() {
            super(ExplosionSnapshot.class, 0);
        }

        @Override
        protected Optional<ExplosionSnapshot> buildContent(DataView data) throws InvalidDataException {
            final List<ChunkedHealable> entries = data.getSerializableList(Keys.HEALABLES, ChunkedHealable.class)
                    .orElseThrow(() -> new InvalidDataException(ExplosionSnapshot.class.getName()+" : Missing \"entries\" data"));
            return Optional.of(new ExplosionSnapshot(entries));
        }
    }
}
