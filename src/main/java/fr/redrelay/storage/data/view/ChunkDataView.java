package fr.redrelay.storage.data.view;

import org.spongepowered.api.world.Chunk;

public interface ChunkDataView extends WorldDataView {
    Chunk getChunk();
}
