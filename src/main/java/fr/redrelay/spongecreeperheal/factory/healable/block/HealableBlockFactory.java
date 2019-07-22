package fr.redrelay.spongecreeperheal.factory.healable.block;

import fr.redrelay.spongecreeperheal.accessor.impl.BlockSnapshotAccessor;
import fr.redrelay.spongecreeperheal.factory.BlockProvider;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import fr.redrelay.spongecreeperheal.healable.atom.block.impl.SimpleHealableBlock;
import fr.redrelay.spongecreeperheal.registry.MultiBlockRegistry;
import org.spongepowered.api.block.BlockSnapshot;

public class HealableBlockFactory implements BlockProvider<BlockSnapshotAccessor, HealableBlock> {

    private static final HealableBlockFactory INSTANCE = new HealableBlockFactory();

    private final BlockProvider<BlockSnapshotAccessor, HealableBlock> DEFAULT_PROVIDER = new SimpleHealableBlock.Provider();

    private HealableBlockFactory() {}

    @Override
    public HealableBlock provide(BlockSnapshot block, BlockSnapshotAccessor accessor) {
        final BlockProvider<BlockSnapshotAccessor, HealableBlock> provider = MultiBlockRegistry.getInstance().get(block.getState().getType()).orElse(DEFAULT_PROVIDER);
        return provider.provide(block, accessor);
    }

    public static HealableBlockFactory getInstance() { return INSTANCE; }


}
