package fr.redrelay.adapter;

import net.minecraft.block.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class MinecraftAdapter {

    private static final MinecraftAdapter INSTANCE = new MinecraftAdapter();

    private final Set<Class<? extends Block>> downLayedBlocks = new HashSet<>(Arrays.asList(
            BlockDoor.class,
            BlockBasePressurePlate.class,
            BlockBanner.BlockBannerStanding.class,
            BlockRedstoneDiode.class,
            BlockRedstoneWire.class,
            BlockStandingSign.class,
            BlockCrops.class,
            BlockCactus.class,
            BlockRailBase.class,
            BlockReed.class,
            BlockSnow.class,
            BlockCake.class,
            BlockCarpet.class,
            BlockDragonEgg.class,
            BlockFlowerPot.class,
            BlockBush.class
    ));

    private MinecraftAdapter() {
    }

    public static MinecraftAdapter getInstance() {
        return INSTANCE;
    }

    public Set<Class<? extends Block>> getDownLayedBlocks() {
        return downLayedBlocks;
    }
}
