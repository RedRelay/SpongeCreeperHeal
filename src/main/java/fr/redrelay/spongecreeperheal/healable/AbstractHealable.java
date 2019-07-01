package fr.redrelay.spongecreeperheal.healable;

import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.*;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public abstract class AbstractHealable implements DataSerializable, Healable {
    private static class Keys {
        final static DataQuery IMPL = DataQuery.of("class");
    }

    protected AbstractHealable() {}

    @Override
    public DataContainer toContainer() {
        final DataContainer data = DataContainer.createNew();
        data.set(Queries.CONTENT_VERSION, getContentVersion());
        data.set(Keys.IMPL, this.getClass().getName());
        return data;
    }

    public static class DataBuilder extends AbstractDataBuilder<AbstractHealable> {

        public DataBuilder() {
            super(AbstractHealable.class, 0);
        }

        @Override
        protected Optional<AbstractHealable> buildContent(DataView data) throws InvalidDataException {
            final Class<? extends AbstractHealable> impl = lookForImpl(data);
            final Optional<? extends org.spongepowered.api.data.persistence.DataBuilder<? extends AbstractHealable>> optBuilder = Sponge.getDataManager().getBuilder(impl);

            if(!optBuilder.isPresent()) {
                throw new InvalidDataException(impl.getName()+" is not registered to Sponge data manager");
            }

            final Optional<? extends AbstractHealable> obj = optBuilder.get().build(data);

            if(!obj.isPresent()) {
                return Optional.empty();
            }

            return Optional.of(obj.get());
        }

        private Class<? extends AbstractHealable> lookForImpl(DataView data) throws InvalidDataException{
            final Optional<String> optImpl = data.getString(AbstractHealable.Keys.IMPL);

            if(!optImpl.isPresent()) {
                throw new InvalidDataException("Unable to find implementation of "+ AbstractHealable.class.getName());
            }

            try {
                return (Class<? extends AbstractHealable>) Class.forName(optImpl.get());
            }catch(ClassNotFoundException | ClassCastException e) {
                throw new InvalidDataException("Invalid implementation : "+optImpl.get());
            }
        }
    }
}
