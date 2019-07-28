package fr.redrelay.spongecreeperheal.healable;

import com.flowpowered.math.vector.Vector3i;

import java.util.Collections;
import java.util.Set;

public interface ChunkedHealable extends Healable {
    Vector3i getChunkPosition();
    default Set<Vector3i> getChunks() {
        return Collections.singleton(getChunkPosition());
    }
}
