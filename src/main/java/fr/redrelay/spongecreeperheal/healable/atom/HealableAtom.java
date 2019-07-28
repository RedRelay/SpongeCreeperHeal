package fr.redrelay.spongecreeperheal.healable.atom;

import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.healable.AbstractHealable;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

public abstract class HealableAtom extends AbstractHealable {

    private static class Keys {
        final static DataQuery REMAINING_TIME = DataQuery.of("remainingTime");
    }

    private int remainingTime = 1;

    protected HealableAtom() {}

    protected HealableAtom(DataView data) throws InvalidDataException {
        final int remainingTime = data.getInt(Keys.REMAINING_TIME).orElseGet(() -> {
            SpongeCreeperHeal.getLogger().error("Missing \"{}\" : set default to {}", Keys.REMAINING_TIME.toString(), 1);
            return 1;
        });

        try {
            this.setRemainingTime(remainingTime);
        }catch(IllegalArgumentException err) {
            SpongeCreeperHeal.getLogger().error("Error occured while deserialize {} : {} ... set default to {}", this.getClass().getSimpleName(), err.getMessage(), 1);
            this.setRemainingTime(1);
        }
    }

    @Override
    public int getRemainingTime() {
        return remainingTime;
    }

    @Override
    public void setRemainingTime(int remainingTime) {
        if(remainingTime < 1) {
            throw new IllegalArgumentException("Unable to set a remaining time < 0 ... was "+remainingTime);
        }
        this.remainingTime = remainingTime;
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer data = super.toContainer();
        data.set(Keys.REMAINING_TIME, remainingTime);
        return data;
    }



}
