package fr.redrelay.spongecreeperheal.registry.accessor;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.registry.Registry;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.world.Location;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockStateAccessor extends Registry<Vector3i, BlockState> {

    public BlockStateAccessor(ExplosionEvent.Detonate e) {
        this.registerAll(e.getAffectedLocations().stream()
                .filter(worldLocation -> worldLocation.getBlockType() != BlockTypes.AIR)
                .collect(Collectors.toMap(Location::getBlockPosition, Location::getBlock))
        );
    }

    public Optional<BlockState> get(Vector3i blockPosition) { return this.getRegistred(blockPosition); }
}
