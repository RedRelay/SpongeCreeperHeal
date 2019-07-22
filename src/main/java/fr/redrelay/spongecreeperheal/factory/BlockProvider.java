package fr.redrelay.spongecreeperheal.factory;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.accessor.Accessor;
import org.spongepowered.api.block.BlockSnapshot;

public interface BlockProvider<T extends Accessor<Vector3i, ?>, R> {
    R provide(BlockSnapshot block, T accessor);
}
