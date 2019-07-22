package fr.redrelay.spongecreeperheal.factory.healable.block;

import fr.redrelay.spongecreeperheal.registry.accessor.BlockStateAccessor;
import org.spongepowered.api.block.BlockSnapshot;

public interface BlockProvider<T> {
    T provide(BlockSnapshot block, BlockStateAccessor accessor);
}
