package fr.redrelay.spongecreeperheal.healable;

import com.flowpowered.math.vector.Vector3i;

public interface ChunkedHealable extends  Healable{
    Vector3i getChunkPosition();
}
