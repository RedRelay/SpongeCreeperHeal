package fr.redrelay.spongecreeperheal.adapter;

import net.minecraft.block.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class MinecraftAdapter {

    private static final MinecraftAdapter INSTANCE = new MinecraftAdapter();

    private final Set<Class<? extends Block>> downLayedBlocks = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
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
    )));

    private final Set<Class<? extends Block>> oppositeFacingLayedBlocks = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            BlockLever.class,
            BlockButton.class,
            BlockTorch.class,
            BlockLadder.class,
            BlockWallSign.class,
            BlockBanner.BlockBannerHanging.class,
            BlockTripWireHook.class
    )));

    private final Set<Class<? extends Block>> facingLayedBlocks = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            BlockCocoa.class
    )));

    private MinecraftAdapter() {
    }

    public static MinecraftAdapter getInstance() {
        return INSTANCE;
    }

    public Set<Class<? extends Block>> getDownLayedBlocks() {
        return downLayedBlocks;
    }

    public Set<Class<? extends Block>> getOppositeFacingLayedBlocks() {
        return oppositeFacingLayedBlocks;
    }

    public Set<Class<? extends Block>> getFacingLayedBlocks() {
        return facingLayedBlocks;
    }
}
