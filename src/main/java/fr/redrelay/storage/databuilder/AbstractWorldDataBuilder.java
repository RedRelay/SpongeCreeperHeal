package fr.redrelay.storage.databuilder;

import fr.redrelay.storage.data.view.WorldDataView;
import fr.redrelay.storage.world.WorldStorage;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public abstract class AbstractWorldDataBuilder<T extends DataSerializable> extends AbstractDataBuilder<T> {

    protected AbstractWorldDataBuilder(Class<T> requiredClass, int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<T> buildContent(DataView container) throws InvalidDataException {
        if(!(container instanceof WorldDataView)) {
            throw new InvalidDataException(this.getClass()+" could not buildContent from data wich do not come from a "+WorldStorage.class.getSimpleName(),
                    new ClassCastException("required "+ WorldDataView.class+" but got "+container.getClass()));
        }
        return buildContent((WorldDataView) container);
    }

    protected abstract Optional<T> buildContent(WorldDataView container) throws InvalidDataException;
}
