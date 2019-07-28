package fr.redrelay.spongecreeperheal.chunk;

import fr.redrelay.spongecreeperheal.data.DelegatedDataSerializable;
import fr.redrelay.spongecreeperheal.data.chunk.ChunkContainerData;
import fr.redrelay.spongecreeperheal.healable.Healable;
import fr.redrelay.spongecreeperheal.healable.explosion.ChunkedHealableExplosion;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.World;

import java.util.stream.Collectors;

/**
 * ChunkContainer contains a list of HealableExplosion to be restored
 * It represents all explosions for a chunk
 */
public class ChunkContainer extends DelegatedDataSerializable<ChunkContainerData> {

    private final World world;
    private final Chunk chunk;

    public ChunkContainer(World world, Chunk chunk, ChunkContainerData data) {
        super(data);
        this.world = world;
        this.chunk = chunk;
    }

    public ChunkContainer(World world, Chunk chunk, ChunkedHealableExplosion explosion) {
       this(world, chunk, new ChunkContainerData(explosion));
    }


    public World getWorld() {
        return world;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void addExplosion(ChunkedHealableExplosion explosion) {
        this.data.getExplosions().add(explosion);
    }

    public boolean isExplosionQueueEmpty() {
        return data.getExplosions().isEmpty();
    }


    public void tick() {
        data.getExplosions().forEach(Healable::decreaseRemainingTime);
        data.getExplosions().removeAll(data.getExplosions().parallelStream().filter(ChunkedHealableExplosion::isHealableQueueEmpty).collect(Collectors.toList()));
    }
}
