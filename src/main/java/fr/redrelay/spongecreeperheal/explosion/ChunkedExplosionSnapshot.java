package fr.redrelay.spongecreeperheal.explosion;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.healable.ChunkedHealable;
import fr.redrelay.spongecreeperheal.healable.Healable;
import fr.redrelay.spongecreeperheal.healable.atom.HealableAtom;
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
public class ChunkedExplosionSnapshot implements Healable, ChunkedHealable, DataSerializable {

    private static class Keys {
        final static DataQuery HEALABLES = DataQuery.of("entries");
    }

    private final LinkedList<HealableAtom> entries = new LinkedList<>();

    public ChunkedExplosionSnapshot(Collection<? extends HealableAtom> entries){
        this.entries.addAll(entries);
    }

    @Override
    public void restore() {
        //TODO
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public int getRemainingTime() {
        //TODO
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void setRemainingTime(int remainingTime) {
        //TODO
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void decreaseRemainingTime() {
        entries.getFirst().decreaseRemainingTime();
        if(entries.getFirst().getRemainingTime() == 0) {
            entries.removeFirst().restore();
        }
    }

    public LinkedList<HealableAtom> getEntries() {
        return entries;
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

    public Vector3i getChunkPosition() {
        return null;
    }

    /**
     * Used to provide ExplosionSnapshot
     */
    public static class DataBuilder extends AbstractDataBuilder<ChunkedExplosionSnapshot> {

        public DataBuilder() {
            super(ChunkedExplosionSnapshot.class, 0);
        }

        @Override
        protected Optional<ChunkedExplosionSnapshot> buildContent(DataView data) throws InvalidDataException {
            final List<? extends HealableAtom> entries = data.getSerializableList(Keys.HEALABLES, HealableAtom.class)
                    .orElseThrow(() -> new InvalidDataException(ChunkedExplosionSnapshot.class.getName()+" : Missing \"entries\" data"));
            return Optional.of(new ChunkedExplosionSnapshot(entries));
        }
    }
}
