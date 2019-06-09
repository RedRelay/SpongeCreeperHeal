package fr.redrelay.spongecreeperheal.adapter;

import net.minecraft.block.BlockShulkerBox;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class VineAdapter {

    private static final VineAdapter INSTANCE = new VineAdapter();

    //see BlockVine::isExceptBlockForAttaching to get list of hardcoded blocks
    private final Set<BlockType> inacceptableSideBlocks = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            BlockTypes.CAULDRON,
            BlockTypes.BEACON,
            BlockTypes.GLASS,
            BlockTypes.STAINED_GLASS,
            BlockTypes.PISTON,
            BlockTypes.STICKY_PISTON,
            BlockTypes.PISTON_HEAD,
            BlockTypes.TRAPDOOR
    )));

    private VineAdapter() {}

    public boolean isExceptBlockForAttaching(BlockType block) {
        return block.getClass().isAssignableFrom(BlockShulkerBox.class) || inacceptableSideBlocks.contains(block);
    }

    public static VineAdapter getInstance() {
        return INSTANCE;
    }
}
