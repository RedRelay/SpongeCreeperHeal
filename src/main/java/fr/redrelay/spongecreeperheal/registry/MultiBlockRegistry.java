package fr.redrelay.spongecreeperheal.registry;

import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import fr.redrelay.spongecreeperheal.factory.healable.block.BlockProvider;
import org.spongepowered.api.block.BlockType;

public class MultiBlockRegistry extends Registry<BlockType, BlockProvider<? extends HealableBlock>> {

    private final static MultiBlockRegistry INSTANCE = new MultiBlockRegistry();

    private MultiBlockRegistry() {};

    public static MultiBlockRegistry getInstance() {
        return INSTANCE;
    }
}
