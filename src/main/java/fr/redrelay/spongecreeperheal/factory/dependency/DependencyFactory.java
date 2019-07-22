package fr.redrelay.spongecreeperheal.factory.dependency;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.DependencyNode;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.dependency.model.NoDependencyModel;
import fr.redrelay.spongecreeperheal.healable.atom.HealableAtom;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import fr.redrelay.spongecreeperheal.registry.accessor.BlockStateAccessor;
import fr.redrelay.spongecreeperheal.dependency.provider.DependencyProvider;
import fr.redrelay.spongecreeperheal.registry.DependencyRegistry;
import org.spongepowered.api.block.BlockSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DependencyFactory {
    private final static DependencyFactory INSTANCE = new DependencyFactory();

    private final DependencyModel<HealableBlock> DEFAULT_DEPENDENCY_MODEL = NoDependencyModel.getInstance();

    private DependencyFactory() {}

    public DependencyNode<HealableBlock> createDependencyNode(HealableBlock block, BlockStateAccessor accessor) {

        final List<DependencyModel<Vector3i>> dependencies = block.getBlockSnapshots().stream().collect(ArrayList<DependencyModel<Vector3i>>::new, (list, blockSnapshot) -> {
            createDependencyNode(blockSnapshot, accessor).ifPresent(list::add);
        }, ArrayList::addAll);

        if(dependencies.isEmpty()) {
            return new DependencyNode<>(block, DEFAULT_DEPENDENCY_MODEL);
        }

        if(dependencies.size() == 1) {
            return new DependencyNode<HealableBlock>(block, dependencies.get(0));
        }


    }

    private Optional<DependencyModel<Vector3i>> createDependencyNode(BlockSnapshot block, BlockStateAccessor accessor) {
        final Optional<DependencyProvider> optProvider = DependencyRegistry.getInstance().getRegistred(block.getState().getType());
        if(optProvider.isPresent()) {
            return optProvider.get().provide(block, accessor);
        }

        return Optional.empty();
    }

    public static DependencyFactory getInstance() {
        return INSTANCE;
    }
}
