package fr.redrelay.spongecreeperheal.chunk;

import fr.redrelay.spongecreeperheal.data.chunk.ChunkContainerBuilder;
import fr.redrelay.spongecreeperheal.healable.Healable;
import fr.redrelay.spongecreeperheal.healable.explosion.ChunkedHealableExplosion;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.Queries;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ChunkContainer contains a list of HealableExplosion to be restored
 * It represents all explosions for a chunk
 */
public class ChunkContainer implements DataSerializable {



    private static class EmptyChunkContainerException extends RuntimeException {
        public EmptyChunkContainerException() {
            super(ChunkContainer.class.getSimpleName()+" must contains at least one explosion to heal");
        }
    }

    private final World world;
    private final Chunk chunk;
    private final List<ChunkedHealableExplosion> explosions = new LinkedList<>();

    public ChunkContainer(World world, Chunk chunk, Collection<? extends ChunkedHealableExplosion> chunkedHealableExplosions) {
        this.world = world;
        this.chunk = chunk;

        if(chunkedHealableExplosions.isEmpty()) {
            throw new EmptyChunkContainerException();
        }

        this.explosions.addAll(chunkedHealableExplosions);
    }

    public ChunkContainer(World world, Chunk chunk, ChunkedHealableExplosion explosion) {
       this(world, chunk, Collections.singleton(explosion));
    }


    public World getWorld() {
        return world;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void addExplosion(ChunkedHealableExplosion explosion) {
        this.explosions.add(explosion);
    }

    public boolean isExplosionQueueEmpty() {
        return explosions.isEmpty();
    }


    public void tick() {
        explosions.forEach(Healable::decreaseRemainingTime);
        explosions.removeAll(explosions.parallelStream().filter(ChunkedHealableExplosion::isHealableQueueEmpty).collect(Collectors.toList()));
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer data = DataContainer.createNew();
        data.set(Queries.CONTENT_VERSION, getContentVersion());
        data.set(ChunkContainerBuilder.Keys.EXPLOSIONS, explosions);
        return data;
    }
}
