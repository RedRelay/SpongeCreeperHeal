package fr.redrelay.spongecreeperheal.healable.factory.provider;

import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import fr.redrelay.spongecreeperheal.healable.factory.BlockStateAccessor;
import org.spongepowered.api.block.BlockSnapshot;

public interface BlockProvider<T> {
    T provide(BlockSnapshot block, BlockStateAccessor accessor);
}
