package fr.redrelay.spongecreeperheal.factory.healable.block;

import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import fr.redrelay.spongecreeperheal.healable.atom.block.impl.SimpleHealableBlock;
import fr.redrelay.spongecreeperheal.registry.MultiBlockRegistry;
import org.spongepowered.api.block.BlockSnapshot;

public class HealableBlockFactory implements BlockProvider<HealableBlock> {

    private static final HealableBlockFactory INSTANCE = new HealableBlockFactory();

    private final BlockProvider<HealableBlock> DEFAULT_PROVIDER = new SimpleHealableBlock.Provider();

    private HealableBlockFactory() {}

    @Override
    public HealableBlock provide(BlockSnapshot block) {
        final BlockProvider<HealableBlock> provider = MultiBlockRegistry.getInstance().get(block.getState().getType()).orElse(DEFAULT_PROVIDER);
        return provider.provide(block);
    }

    public static HealableBlockFactory getInstance() { return INSTANCE; }


}
