package fr.redrelay.spongecreeperheal.block;

import org.spongepowered.api.data.DataSerializable;

import java.util.Collection;

public interface Healable extends DataSerializable {
    void restore();
    int getRemainingTime();
    void setRemainingTime(int remainingTime);
    void decreaseRemainingTime();
    Collection<ChunkedHealable> split();
}
