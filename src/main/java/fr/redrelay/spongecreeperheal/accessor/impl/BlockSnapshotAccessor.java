package fr.redrelay.spongecreeperheal.accessor.impl;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.accessor.Accessor;
import fr.redrelay.spongecreeperheal.registry.Registry;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.LocatableSnapshot;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.world.Location;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BlockSnapshotAccessor extends Registry<Vector3i, BlockSnapshot> implements Accessor<Vector3i, BlockSnapshot> {

    public BlockSnapshotAccessor(ExplosionEvent.Detonate e) {
        final Map<Vector3i, BlockSnapshot> blockSnapshots = e.getAffectedLocations().stream()
                .filter(worldLocation -> worldLocation.getBlockType() != BlockTypes.AIR)
                .map(Location::createSnapshot)
                .collect(Collectors.toMap(LocatableSnapshot::getPosition, Function.identity()));
        this.registry.putAll(blockSnapshots);
    }

}
