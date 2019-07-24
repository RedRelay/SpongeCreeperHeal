package fr.redrelay.spongecreeperheal.explosion;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.healable.Healable;

import java.util.*;
import java.util.stream.Collectors;

//TODO : Must be serialisable, because it will be saved in world save (levelDB)
//TODO : Unique indentifier my be human readable like "TNT-Player(RedRelay)-20190701:16:12:53:2351-1" with the last number the identifier of the current thread
public class ExplosionSnapshot implements Healable {

    //Null values are allowed, meaning chuck is not loaded
    private final Map<Vector3i, Optional<ChunkedExplosionSnapshot>> chunkedExplosions = new HashMap<>();

    public ExplosionSnapshot(Set<ChunkedExplosionSnapshot> chunkedExplosions){
        this.chunkedExplosions.putAll(chunkedExplosions.stream().collect(Collectors.toMap(ChunkedExplosionSnapshot::getChunkPosition, Optional::of)));
    }


    @Override
    public void restore() {
        //TODO
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public int getRemainingTime() {
        //TODO
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void setRemainingTime(int remainingTime) {
        //TODO
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void decreaseRemainingTime() {
        //TODO
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Set<Vector3i> getChunks() {
        return Collections.unmodifiableSet(this.chunkedExplosions.keySet());
    }

    public Optional<ChunkedExplosionSnapshot> getChunkedExplosion(Vector3i chunkPos) {
        final Optional<ChunkedExplosionSnapshot> chunkedExplosionSnapshot = chunkedExplosions.get(chunkPos);
        if(chunkedExplosionSnapshot == null) return Optional.empty();
        return chunkedExplosionSnapshot;
    }


}
