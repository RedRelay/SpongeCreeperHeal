package fr.redrelay.spongecreeperheal.healable;

import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.Queries;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public abstract class AbstractHealable implements Healable {
    private static class Keys {
        final static DataQuery REMAINING_TIME = DataQuery.of("remainingTime");
        final static DataQuery IMPL = DataQuery.of("class");
    }

    protected AbstractHealable() {}

    protected AbstractHealable(DataView data) throws InvalidDataException {
        final int remainingTime = data.getInt(Keys.REMAINING_TIME).orElseGet(() -> {
            SpongeCreeperHeal.getLogger().error("Missing \"{}\" data : set default to {}", Keys.REMAINING_TIME.toString(), 1);
            return 1;
        });

        try {
            this.setRemainingTime(remainingTime);
        }catch(IllegalArgumentException err) {
            SpongeCreeperHeal.getLogger().error("Error occured while deserialize {} : {} ... set default to {}", this.getClass().getSimpleName(), err.getMessage(), 1);
            this.setRemainingTime(1);
        }
    }

    private int remainingTime = 1;

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
    public void decreaseRemainingTime() {
        if(this.remainingTime <= 0) {
            SpongeCreeperHeal.getLogger().error("Cannot decrease remaining time because it is already 0 ... skipping");
            return;
        }
        this.remainingTime--;
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer data = DataContainer.createNew();
        data.set(Queries.CONTENT_VERSION, getContentVersion());
        data.set(Keys.IMPL, this.getClass().getName());
        data.set(Keys.REMAINING_TIME, remainingTime);
        return data;
    }

    public static class ChunkedHealableBuilder extends AbstractDataBuilder<ChunkedHealable> {

        public ChunkedHealableBuilder() {
            super(ChunkedHealable.class, 0);
        }

        @Override
        protected Optional<ChunkedHealable> buildContent(DataView data) throws InvalidDataException {
            final Class<? extends ChunkedHealable> impl = lookForImpl(data);
            final Optional<? extends DataBuilder<? extends ChunkedHealable>> optBuilder = Sponge.getDataManager().getBuilder(impl);

            if(!optBuilder.isPresent()) {
                throw new InvalidDataException(impl.getName()+" is not registered to Sponge data manager");
            }

            final Optional<? extends ChunkedHealable> obj = optBuilder.get().build(data);

            if(!obj.isPresent()) {
                return Optional.empty();
            }

            return Optional.of(obj.get());
        }

        private Class<? extends ChunkedHealable> lookForImpl(DataView data) throws InvalidDataException{
            final Optional<String> optImpl = data.getString(AbstractHealable.Keys.IMPL);

            if(!optImpl.isPresent()) {
                throw new InvalidDataException("Unable to find implementation of "+AbstractHealable.class.getName());
            }

            try {
                return (Class<? extends ChunkedHealable>) Class.forName(optImpl.get());
            }catch(ClassNotFoundException | ClassCastException e) {
                throw new InvalidDataException("Invalid implementation : "+optImpl.get());
            }
        }
    }
}
