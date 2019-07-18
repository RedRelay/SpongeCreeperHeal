package fr.redrelay.spongecreeperheal.healable;

import com.flowpowered.math.vector.Vector3i;

import java.util.Map;
import java.util.Optional;

public interface Healable {
    void restore();
    int getRemainingTime();
    void setRemainingTime(int remainingTime);
    void decreaseRemainingTime();
    Map<Vector3i, Optional<? extends Healable>> splitByChunk();
}
