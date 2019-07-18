package fr.redrelay.spongecreeperheal.healable.factory;

import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import fr.redrelay.spongecreeperheal.healable.factory.provider.BlockProvider;
import fr.redrelay.spongecreeperheal.healable.factory.provider.impl.SimpleHealableBlockProvider;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;

public class HealableBlockFactory extends Registry<BlockType, BlockProvider<?>> implements BlockProvider<HealableBlock> {

    private static final HealableBlockFactory INSTANCE = new HealableBlockFactory();

    private final BlockProvider<?> DEFAULT_PROVIDER = new SimpleHealableBlockProvider();

    private HealableBlockFactory() {}

    @Override
    public HealableBlock provide(BlockSnapshot block, BlockStateAccessor accessor) {
        final BlockProvider<?> provider = this.getRegistred(block.getState().getType()).orElse(DEFAULT_PROVIDER);
        return provider.provide(block, accessor);
    }

    public static HealableBlockFactory getInstance() { return INSTANCE; }


}
