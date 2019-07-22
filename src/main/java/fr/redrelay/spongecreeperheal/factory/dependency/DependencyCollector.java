package fr.redrelay.spongecreeperheal.factory.dependency;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.DependencyNode;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.healable.atom.HealableAtom;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import fr.redrelay.spongecreeperheal.registry.DependencyRegistry;
import fr.redrelay.spongecreeperheal.registry.accessor.BlockStateAccessor;
import org.spongepowered.api.block.BlockSnapshot;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class DependencyCollector implements Collector<BlockSnapshot, Collection<DependencyModel<Vector3i>>, DependencyNode<HealableBlock>> {

    private final BlockStateAccessor accessor;

    public DependencyCollector(BlockStateAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public Supplier<Collection<DependencyModel<Vector3i>>> supplier() {
        return LinkedList<DependencyModel<Vector3i>>::new;
    }

    @Override
    public BiConsumer<Collection<DependencyModel<Vector3i>>, BlockSnapshot> accumulator() {
        return (dependencyModels, block) -> {
            DependencyRegistry.getInstance().getRegistred(block.getState().getType())
                    .ifPresent(provider -> provider.provide(block, accessor).ifPresent(dependencyModels::add));
        };
    }

    @Override
    public BinaryOperator<Collection<DependencyModel<Vector3i>>> combiner() {
        return (left,right) -> {
            left.addAll(right);
            return left;
        };
    }

    @Override
    public Function<Collection<DependencyModel<Vector3i>>, DependencyNode<HealableBlock>> finisher() {
        return dependencyModels -> {
            if(dependencyModels.isEmpty()) {
                return new DependencyNode<>(block, DEFAULT_DEPENDENCY_MODEL);
            }
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }
}
