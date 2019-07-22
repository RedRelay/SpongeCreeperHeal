package fr.redrelay.spongecreeperheal.registry;

import fr.redrelay.spongecreeperheal.accessor.impl.BlockSnapshotAccessor;
import fr.redrelay.spongecreeperheal.factory.BlockProvider;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import org.spongepowered.api.block.BlockType;

public class MultiBlockRegistry extends Registry<BlockType, BlockProvider<BlockSnapshotAccessor, HealableBlock>> {

    private final static MultiBlockRegistry INSTANCE = new MultiBlockRegistry();

    private MultiBlockRegistry() {};

    public static MultiBlockRegistry getInstance() {
        return INSTANCE;
    }
}
