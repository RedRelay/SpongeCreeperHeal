package fr.redrelay.spongecreeperheal.healable.explosion;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.data.DelegatedDataSerializable;
import fr.redrelay.spongecreeperheal.data.explosion.HealableExplosionData;
import fr.redrelay.spongecreeperheal.healable.ChunkedHealable;
import fr.redrelay.spongecreeperheal.healable.Healable;
import fr.redrelay.spongecreeperheal.healable.atom.HealableAtom;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * HealableExplosion represent a memorized list of Healable after an Explosion.
 * All Healable of a HealableExplosion should be on the same chunk
 * It means a multi chunk explosion create multiple HealableExplosion for each chunk
 * Each HealTask ticks decrements first Healable of HealableExplosion
 * When Healable timer reach 0, it is restored and dropped from the list
 * Empty HealableExplosion must not be keep in memory to avoid leak
 */
public class ChunkedHealableExplosion extends DelegatedDataSerializable<HealableExplosionData> implements Healable, ChunkedHealable {

    private final Vector3i chunkPos;

    public ChunkedHealableExplosion(Vector3i chunkPos, HealableExplosionData data) {
        super(data);
        this.chunkPos = chunkPos;
    }

    public ChunkedHealableExplosion(Vector3i chunkPos, Collection<? extends HealableAtom> healableAtoms) throws HealableExplosionData.EmptyHealableExplosionDataException {
        this(chunkPos, new HealableExplosionData(getFilteredHealableAtoms(chunkPos,healableAtoms)));
    }

    public boolean isHealableQueueEmpty() {
        return data.getHealableAtoms().isEmpty();
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
    public Vector3i getChunkPosition() {
        return chunkPos;
    }

    private static Collection<? extends HealableAtom> getFilteredHealableAtoms(Vector3i chunkPos, Collection<? extends HealableAtom> healableAtoms) {
        final Collection<? extends HealableAtom> chunkedHealableAtoms = healableAtoms.parallelStream().filter(atom -> atom.getChunks().parallelStream().anyMatch((atomChunkPos) -> atomChunkPos.equals(chunkPos))).collect(Collectors.toList());
        if(chunkedHealableAtoms.size() != healableAtoms.size()) {
            final Collection<HealableAtom> unchunkedHealableAtom = new LinkedList<>(healableAtoms);
            unchunkedHealableAtom.removeAll(chunkedHealableAtoms);
            SpongeCreeperHeal.getLogger().warn("Unable to add {} in {} {} because chunk does not match", unchunkedHealableAtom, ChunkedHealableExplosion.class.getSimpleName(), chunkPos);
        }
        return chunkedHealableAtoms;
    }

}
