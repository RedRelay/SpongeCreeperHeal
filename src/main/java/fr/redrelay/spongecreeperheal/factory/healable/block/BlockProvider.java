package fr.redrelay.spongecreeperheal.factory.healable.block;

import org.spongepowered.api.block.BlockSnapshot;

public interface BlockProvider<T> {
    T provide(BlockSnapshot block);
}
