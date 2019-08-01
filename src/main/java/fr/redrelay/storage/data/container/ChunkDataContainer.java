package fr.redrelay.storage.data.container;

import fr.redrelay.storage.data.view.ChunkDataView;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.World;

public class ChunkDataContainer extends WorldDataContainer implements ChunkDataView {

    private final Chunk chunk;

    public ChunkDataContainer(World world, Chunk chunk, DataContainer dataContainer) {
        super(world, dataContainer);
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }
}
