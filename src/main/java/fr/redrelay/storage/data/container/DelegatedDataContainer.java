package fr.redrelay.storage.data.container;

import org.spongepowered.api.CatalogType;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DelegatedDataContainer implements DataContainer {

    private final DataContainer dataContainer;

    protected DelegatedDataContainer(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
    }

    @Override
    public DataContainer set(DataQuery path, Object value) {
        return dataContainer.set(path, value);
    }

    @Override
    public <E> DataContainer set(Key<? extends BaseValue<E>> key, E value) {
        return dataContainer.set(key, value);
    }

    @Override
    public DataContainer remove(DataQuery path) {
        return dataContainer.remove(path);
    }

    @Override
    public DataContainer getContainer() {
        return dataContainer.getContainer();
    }

    @Override
    public DataQuery getCurrentPath() {
        return dataContainer.getCurrentPath();
    }

    @Override
    public String getName() {
        return dataContainer.getName();
    }

    @Override
    public Optional<DataView> getParent() {
        return dataContainer.getParent();
    }

    @Override
    public Set<DataQuery> getKeys(boolean deep) {
        return dataContainer.getKeys(deep);
    }

    @Override
    public Map<DataQuery, Object> getValues(boolean deep) {
        return dataContainer.getValues(deep);
    }

    @Override
    public boolean contains(DataQuery path) {
        return dataContainer.contains(path);
    }

    @Override
    public boolean contains(DataQuery path, DataQuery... paths) {
        return dataContainer.contains(path, paths);
    }

    @Override
    public boolean contains(Key<?> key) {
        return dataContainer.contains(key);
    }

    @Override
    public boolean contains(Key<?> key, Key<?>... keys) {
        return dataContainer.contains(key, keys);
    }

    @Override
    public Optional<Object> get(DataQuery path) {
        return dataContainer.get(path);
    }

    @Override
    public DataView createView(DataQuery path) {
        return dataContainer.createView(path);
    }

    @Override
    public DataView createView(DataQuery path, Map<?, ?> map) {
        return dataContainer.createView(path, map);
    }

    @Override
    public Optional<DataView> getView(DataQuery path) {
        return dataContainer.getView(path);
    }

    @Override
    public Optional<? extends Map<?, ?>> getMap(DataQuery path) {
        return dataContainer.getMap(path);
    }

    @Override
    public Optional<Boolean> getBoolean(DataQuery path) {
        return dataContainer.getBoolean(path);
    }

    @Override
    public Optional<Short> getShort(DataQuery path) {
        return dataContainer.getShort(path);
    }

    @Override
    public Optional<Byte> getByte(DataQuery path) {
        return dataContainer.getByte(path);
    }

    @Override
    public Optional<Integer> getInt(DataQuery path) {
        return dataContainer.getInt(path);
    }

    @Override
    public Optional<Long> getLong(DataQuery path) {
        return dataContainer.getLong(path);
    }

    @Override
    public Optional<Float> getFloat(DataQuery path) {
        return dataContainer.getFloat(path);
    }

    @Override
    public Optional<Double> getDouble(DataQuery path) {
        return dataContainer.getDouble(path);
    }

    @Override
    public Optional<String> getString(DataQuery path) {
        return dataContainer.getString(path);
    }

    @Override
    public Optional<List<?>> getList(DataQuery path) {
        return dataContainer.getList(path);
    }

    @Override
    public Optional<List<String>> getStringList(DataQuery path) {
        return dataContainer.getStringList(path);
    }

    @Override
    public Optional<List<Character>> getCharacterList(DataQuery path) {
        return dataContainer.getCharacterList(path);
    }

    @Override
    public Optional<List<Boolean>> getBooleanList(DataQuery path) {
        return dataContainer.getBooleanList(path);
    }

    @Override
    public Optional<List<Byte>> getByteList(DataQuery path) {
        return dataContainer.getByteList(path);
    }

    @Override
    public Optional<List<Short>> getShortList(DataQuery path) {
        return dataContainer.getShortList(path);
    }

    @Override
    public Optional<List<Integer>> getIntegerList(DataQuery path) {
        return dataContainer.getIntegerList(path);
    }

    @Override
    public Optional<List<Long>> getLongList(DataQuery path) {
        return dataContainer.getLongList(path);
    }

    @Override
    public Optional<List<Float>> getFloatList(DataQuery path) {
        return dataContainer.getFloatList(path);
    }

    @Override
    public Optional<List<Double>> getDoubleList(DataQuery path) {
        return dataContainer.getDoubleList(path);
    }

    @Override
    public Optional<List<Map<?, ?>>> getMapList(DataQuery path) {
        return dataContainer.getMapList(path);
    }

    @Override
    public Optional<List<DataView>> getViewList(DataQuery path) {
        return dataContainer.getViewList(path);
    }

    @Override
    public <T extends DataSerializable> Optional<T> getSerializable(DataQuery path, Class<T> clazz) {
        return dataContainer.getSerializable(path, clazz);
    }

    @Override
    public <T extends DataSerializable> Optional<List<T>> getSerializableList(DataQuery path, Class<T> clazz) {
        return dataContainer.getSerializableList(path, clazz);
    }

    @Override
    public <T> Optional<T> getObject(DataQuery path, Class<T> objectClass) {
        return dataContainer.getObject(path, objectClass);
    }

    @Override
    public <T> Optional<List<T>> getObjectList(DataQuery path, Class<T> objectClass) {
        return dataContainer.getObjectList(path, objectClass);
    }

    @Override
    public <T extends CatalogType> Optional<T> getCatalogType(DataQuery path, Class<T> catalogType) {
        return dataContainer.getCatalogType(path, catalogType);
    }

    @Override
    public <T extends CatalogType> Optional<List<T>> getCatalogTypeList(DataQuery path, Class<T> catalogType) {
        return dataContainer.getCatalogTypeList(path, catalogType);
    }

    @Override
    public DataContainer copy() {
        return dataContainer.copy();
    }

    @Override
    public DataContainer copy(DataView.SafetyMode safety) {
        return dataContainer.copy(safety);
    }

    @Override
    public boolean isEmpty() {
        return dataContainer.isEmpty();
    }

    @Override
    public DataView.SafetyMode getSafetyMode() {
        return dataContainer.getSafetyMode();
    }
}
