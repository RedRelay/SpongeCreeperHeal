package fr.redrelay.spongecreeperheal.healable;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;

import java.util.Set;

public interface Healable {
    void restore();
    int getRemainingTime();
    void setRemainingTime(int remainingTime);
    Set<Vector3i> getChunks();
    default void decreaseRemainingTime() {
        final int remainingTime = getRemainingTime();
        if(remainingTime <= 0) {
            SpongeCreeperHeal.getLogger().error("Cannot decrease remaining time because it is already 0 ... skipping");
            return;
        }
        setRemainingTime(remainingTime-1);
    }
}
