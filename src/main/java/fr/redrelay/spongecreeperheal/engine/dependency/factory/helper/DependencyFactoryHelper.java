package fr.redrelay.spongecreeperheal.engine.dependency.factory.helper;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.adapter.DirectionAdapter;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class DependencyFactoryHelper {

    public static Stream<Vector3i> sideBlocks(BlockSnapshot blockSnapshot, Map<Vector3i, BlockState> index) {
        return Arrays.asList(DirectionAdapter.HORIZONTAL).stream()
                .map(direction -> blockSnapshot.getPosition().add(direction.asBlockOffset()))
                .filter(pos -> index.containsKey(pos));
    }

}
