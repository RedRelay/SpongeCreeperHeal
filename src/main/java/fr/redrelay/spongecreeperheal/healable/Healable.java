package fr.redrelay.spongecreeperheal.healable;

import com.flowpowered.math.vector.Vector3i;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface Healable {
    void restore();
    int getRemainingTime();
    void setRemainingTime(int remainingTime);
    void decreaseRemainingTime();
    Set<Vector3i> getChunks();
}
