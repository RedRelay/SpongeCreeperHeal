package fr.redrelay.spongecreeperheal.healable;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.data.DataSerializable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public interface ChunkedHealable extends Healable {
    Vector3i getChunkPosition();

    default Map<Vector3i, Optional<? extends Healable>> splitByChunk() {
        return Collections.singletonMap(getChunkPosition(), Optional.of(this));
    }
}
