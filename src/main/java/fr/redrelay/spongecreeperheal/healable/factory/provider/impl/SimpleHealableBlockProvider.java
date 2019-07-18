package fr.redrelay.spongecreeperheal.healable.factory.provider.impl;

import fr.redrelay.spongecreeperheal.healable.atom.block.impl.SimpleHealableBlock;
import fr.redrelay.spongecreeperheal.healable.factory.BlockStateAccessor;
import fr.redrelay.spongecreeperheal.healable.factory.provider.BlockProvider;
import org.spongepowered.api.block.BlockSnapshot;

public class SimpleHealableBlockProvider implements BlockProvider<SimpleHealableBlock> {
    @Override
    public SimpleHealableBlock provide(BlockSnapshot block, BlockStateAccessor accessor) {
        return new SimpleHealableBlock(block);
    }
}
