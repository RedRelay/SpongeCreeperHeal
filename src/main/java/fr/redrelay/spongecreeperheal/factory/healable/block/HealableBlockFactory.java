package fr.redrelay.spongecreeperheal.factory.healable.block;

import fr.redrelay.spongecreeperheal.healable.atom.block.impl.SimpleHealableBlock;
import fr.redrelay.spongecreeperheal.registry.accessor.BlockStateAccessor;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import fr.redrelay.spongecreeperheal.registry.MultiBlockRegistry;
import org.spongepowered.api.block.BlockSnapshot;

public class HealableBlockFactory implements BlockProvider<HealableBlock> {

    private static final HealableBlockFactory INSTANCE = new HealableBlockFactory();

    private final BlockProvider<? extends HealableBlock> DEFAULT_PROVIDER = new SimpleHealableBlock.Provider();

    private HealableBlockFactory() {}

    @Override
    public HealableBlock provide(BlockSnapshot block, BlockStateAccessor accessor) {
        final BlockProvider<? extends HealableBlock> provider = MultiBlockRegistry.getInstance().getRegistred(block.getState().getType()).orElse(DEFAULT_PROVIDER);
        return provider.provide(block, accessor);
    }

    public static HealableBlockFactory getInstance() { return INSTANCE; }


}
