package fr.redrelay.spongecreeperheal.adapter;

import net.minecraft.block.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Helper Class used to interface with Minecraft properties which could not be find by looking for attributes
 * If alternative existe, we must use it.
 * It represents almost hardcoded values
 */
public final class MinecraftAdapter {

    private static final MinecraftAdapter INSTANCE = new MinecraftAdapter();

    /**
     * List of block class which stand over an other block, without its bottom block, it breaks
     */
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

    /**
     * List of blocks class which stand on a side block, if this standing block is broken, its break
     * Opposite make difference when you have to use internal Facing property inverted to get the support block
     */
    private final Set<Class<? extends Block>> oppositeFacingLayedBlocks = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            BlockLever.class,
            BlockButton.class,
            BlockTorch.class,
            BlockLadder.class,
            BlockWallSign.class,
            BlockBanner.BlockBannerHanging.class,
            BlockTripWireHook.class
    )));

    /**
     * List of blocks class which stand on a side block, if this standing block is broken, its break
     */
    private final Set<Class<? extends Block>> facingLayedBlocks = Collections.unmodifiableSet(new HashSet<>(Collections.singletonList(
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
