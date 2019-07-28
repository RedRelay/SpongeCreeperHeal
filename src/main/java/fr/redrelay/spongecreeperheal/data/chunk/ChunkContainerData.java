package fr.redrelay.spongecreeperheal.data.chunk;

import fr.redrelay.spongecreeperheal.healable.explosion.ChunkedHealableExplosion;
import org.spongepowered.api.data.*;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ChunkContainerData implements DataSerializable {

    private static class EmptyChunkContainerData extends RuntimeException {
        public EmptyChunkContainerData() {
            super(Keys.EXPLOSIONS+" is empty, "+ChunkContainerData.class.getSimpleName()+" must contains at least one explosion to heal");
        }
    }

    private static class Keys {
        final static DataQuery EXPLOSIONS = DataQuery.of("explosions");
    }

    private final List<ChunkedHealableExplosion> explosions = new LinkedList<>();

    public ChunkContainerData(ChunkedHealableExplosion explosion) {
        this.explosions.add(explosion);
    }

    private ChunkContainerData(Collection<? extends ChunkedHealableExplosion> explosions) throws EmptyChunkContainerData{
        if(explosions.isEmpty()) {
            throw new EmptyChunkContainerData();
        }
        this.explosions.addAll(explosions);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer data = DataContainer.createNew();
        data.set(Queries.CONTENT_VERSION, getContentVersion());
        data.set(Keys.EXPLOSIONS, explosions);
        return data;
    }

    public List<ChunkedHealableExplosion> getExplosions() {
        return explosions;
    }

    public static class DataBuilder extends AbstractDataBuilder<ChunkContainerData> {

        public DataBuilder() {
            super(ChunkContainerData.class, 0);
        }

        @Override
        protected Optional<ChunkContainerData> buildContent(DataView container) throws InvalidDataException {
            final Optional<List<ChunkedHealableExplosion>> opt = container.getSerializableList(Keys.EXPLOSIONS, ChunkedHealableExplosion.class);
            if(!opt.isPresent()) {
                throw new InvalidDataException("Missing "+Keys.EXPLOSIONS);
            }
            try {
                return Optional.of(new ChunkContainerData(opt.get()));
            }catch(EmptyChunkContainerData e) {
                throw new InvalidDataException(e);
            }
        }

    }
}
