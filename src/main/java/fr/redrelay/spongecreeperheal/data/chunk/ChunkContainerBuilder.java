package fr.redrelay.spongecreeperheal.data.chunk;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.chunk.ChunkContainer;
import fr.redrelay.spongecreeperheal.data.explosion.HealableExplosionData;
import fr.redrelay.spongecreeperheal.healable.explosion.ChunkedHealableExplosion;
import org.spongepowered.api.data.*;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChunkContainerBuilder implements DataSerializable {

    public final static int CONTENT_VERSION = 0;

    public static class Keys {
        public final static DataQuery EXPLOSIONS = DataQuery.of("explosions");
    }

    private final List<HealableExplosionData> explosions;

    private ChunkContainerBuilder(List<HealableExplosionData> data) {
        this.explosions = data;
    }

    @Override
    public int getContentVersion() {
        return CONTENT_VERSION;
    }

    @Override
    public DataContainer toContainer() {
        //Should not be used
        return null;
    }

    public ChunkContainer build(World world, Chunk chunk)  {
        return new ChunkContainer(world, chunk, explosions.parallelStream().map(e -> new ChunkedHealableExplosion(chunk.getPosition(), e)).collect(Collectors.toList()));
    }

    public static class DataBuilder extends AbstractDataBuilder<ChunkContainerBuilder> {

        public DataBuilder() {
            super(ChunkContainerBuilder.class, 0);
        }

        @Override
        protected Optional<ChunkContainerBuilder> buildContent(DataView container) throws InvalidDataException {
            final Optional<List<HealableExplosionData>> opt = container.getSerializableList(Keys.EXPLOSIONS, HealableExplosionData.class);
            if(!opt.isPresent()) {
                throw new InvalidDataException("Missing "+Keys.EXPLOSIONS);
            }

            return Optional.of(new ChunkContainerBuilder(opt.get()));
        }

    }
}
