package fr.redrelay.spongecreeperheal.explosion;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.healable.ChunkedHealable;
import fr.redrelay.spongecreeperheal.healable.Healable;
import fr.redrelay.spongecreeperheal.healable.atom.HealableAtom;
import org.spongepowered.api.data.*;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ExplosionSnapshot represent a memorized list of Healable after an Explosion.
 * All Healable of a ExplosionSnapshot should be on the same chunk
 * It means a multi chunk explosion create multiple ExplosionSnapshot for each chunk
 * Each HealTask ticks decrements first Healable of ExplosionSnapshot
 * When Healable timer reach 0, it is restored and dropped from the list
 * Empty ExplosionSnapshot must not be keep in memory to avoid leak
 */
public class ChunkedExplosionSnapshot implements Healable, ChunkedHealable, DataSerializable {

    public static class EmptyChunkedExplosionSnapshotException extends RuntimeException {

        private final Collection<? extends HealableAtom> healableAtoms;

        private EmptyChunkedExplosionSnapshotException(Vector3i chunkPos, Collection<? extends HealableAtom> healableAtoms) {
            super("Unable to create an "+ChunkedExplosionSnapshot.class.getSimpleName()+" without at least one "+HealableAtom.class.getSimpleName()+" located in its chunk "+chunkPos);
            this.healableAtoms = healableAtoms;
        }

        public Collection<? extends HealableAtom> getEntries() {
            return Collections.unmodifiableCollection(healableAtoms);
        }
    }

    private static class Keys {
        final static DataQuery HEALABLES = DataQuery.of("healableAtoms");
    }

    private final LinkedList<HealableAtom> healableAtoms = new LinkedList<>();
    private final Vector3i chunkPos;

    public ChunkedExplosionSnapshot(Vector3i chunkPos, Collection<? extends HealableAtom> healableAtoms) throws EmptyChunkedExplosionSnapshotException {
        this.chunkPos = chunkPos;
        final Collection<? extends HealableAtom> chunkedHealableAtoms = healableAtoms.parallelStream().filter(atom -> atom.getChunks().parallelStream().anyMatch((atomChunkPos) -> atomChunkPos.equals(chunkPos))).collect(Collectors.toList());
        if(chunkedHealableAtoms.isEmpty()) {
            throw new EmptyChunkedExplosionSnapshotException(chunkPos, healableAtoms);
        }
        if(chunkedHealableAtoms.size() != healableAtoms.size()) {
            final Collection<HealableAtom> unchunkedHealableAtom = new LinkedList<>(healableAtoms);
            unchunkedHealableAtom.removeAll(chunkedHealableAtoms);
            SpongeCreeperHeal.getLogger().warn("Unable to add {} in {} {} because chunk does not match", unchunkedHealableAtom, ChunkedExplosionSnapshot.class.getSimpleName(), chunkPos);
        }
        this.healableAtoms.addAll(chunkedHealableAtoms);

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
        healableAtoms.getFirst().decreaseRemainingTime();
        if(healableAtoms.getFirst().getRemainingTime() == 0) {
            healableAtoms.removeFirst().restore();
        }
    }

    public LinkedList<HealableAtom> getHealableAtoms() {
        return healableAtoms;
    }


    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer data = DataContainer.createNew();
        data.set(Queries.CONTENT_VERSION, getContentVersion());
        data.set(Keys.HEALABLES, healableAtoms);
        return data;
    }

    @Override
    public Vector3i getChunkPosition() {
        return chunkPos;
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
                    .orElseThrow(() -> new InvalidDataException(ChunkedExplosionSnapshot.class.getName()+" : Missing \"healableAtoms\" data"));
            return Optional.of(new ChunkedExplosionSnapshot(entries));
        }
    }
}
