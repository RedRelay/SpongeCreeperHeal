package fr.redrelay.spongecreeperheal.accessor.impl;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.accessor.Accessor;
import fr.redrelay.spongecreeperheal.factory.healable.block.HealableBlockFactory;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import fr.redrelay.spongecreeperheal.registry.Registry;
import org.spongepowered.api.event.world.ExplosionEvent;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HealableBlockAccessor extends Registry<Vector3i, HealableBlock> implements Accessor<Vector3i, HealableBlock> {

    final Set<HealableBlock> healableBlocks = new HashSet<>();

    public HealableBlockAccessor(ExplosionEvent.Detonate e) {
        final BlockSnapshotAccessor blockSnapshotAccessor = new BlockSnapshotAccessor(e);
        final Map<Vector3i, HealableBlock> healableBlocks = blockSnapshotAccessor.getRegistry().values().stream()
            .collect(HashMap<Vector3i, HealableBlock>::new, (healableBlockIndex, blockSnapshot) -> {
                if(healableBlockIndex.containsKey(blockSnapshot.getPosition())) return;
                final HealableBlock healableBlock = HealableBlockFactory.getInstance().provide(blockSnapshot, blockSnapshotAccessor);
                healableBlockIndex.putAll(healableBlock.getBlockSnapshots().keySet().stream().collect(Collectors.toMap(Function.identity(), (pos) -> healableBlock)));
            }, Map::putAll);

        this.healableBlocks.addAll(healableBlocks.values());

        this.registry.putAll(healableBlocks);
    }

    public Set<HealableBlock> getHealableBlocks() {
        return Collections.unmodifiableSet(healableBlocks);
    }
}
