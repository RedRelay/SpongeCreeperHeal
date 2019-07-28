package fr.redrelay.spongecreeperheal.data;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;

public class DelegatedDataSerializable<T extends DataSerializable> implements DataSerializable {
    protected final T data;

    protected DelegatedDataSerializable(T data) {
        this.data = data;
    }

    @Override
    public final int getContentVersion() {
        return data.getContentVersion();
    }

    @Override
    public final DataContainer toContainer() {
        return data.toContainer();
    }
}
