package fr.redrelay.storage.databuilder;

import fr.redrelay.storage.data.view.ChunkDataView;
import fr.redrelay.storage.data.view.WorldDataView;
import fr.redrelay.storage.world.WorldStorage;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public abstract class AbstractChunkDataBuilder<T extends DataSerializable> extends AbstractWorldDataBuilder<T> {

    protected AbstractChunkDataBuilder(Class<T> requiredClass, int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<T> buildContent(WorldDataView container) throws InvalidDataException {
        if(!(container instanceof ChunkDataView)) {
            throw new InvalidDataException(this.getClass()+" could not buildContent from data wich do not come from a "+WorldStorage.class.getSimpleName(),
                    new ClassCastException("required "+ ChunkDataView.class+" but got "+container.getClass()));
        }
        return buildContent((ChunkDataView) container);
    }

    protected abstract Optional<T> buildContent(ChunkDataView container) throws InvalidDataException;
}
