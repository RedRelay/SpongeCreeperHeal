package fr.redrelay.adapter;

import net.minecraft.block.BlockDoor;
import org.spongepowered.api.block.BlockType;

public final class MinecraftAdapter {

    private static final MinecraftAdapter INSTANCE = new MinecraftAdapter();

    private MinecraftAdapter() {}

    public boolean isDoor(BlockType block) {
        return BlockDoor.class.isAssignableFrom(block.getClass());
    }

    public static MinecraftAdapter getInstance() {
        return INSTANCE;
    }
}
