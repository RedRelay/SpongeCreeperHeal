package fr.redrelay.spongecreeperheal.factory.dependency;

import fr.redrelay.dependency.DependencyNode;
import fr.redrelay.dependency.model.AndDependencyModel;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.dependency.model.NoDependencyModel;
import fr.redrelay.spongecreeperheal.accessor.impl.HealableBlockAccessor;
import fr.redrelay.spongecreeperheal.dependency.provider.DependencyProvider;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import fr.redrelay.spongecreeperheal.registry.DependencyRegistry;
import org.spongepowered.api.block.BlockSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DependencyFactory {
    private final static DependencyFactory INSTANCE = new DependencyFactory();

    private final DependencyModel<HealableBlock> DEFAULT_DEPENDENCY_MODEL = NoDependencyModel.getInstance();

    private DependencyFactory() {}

    public DependencyNode<HealableBlock> createDependencyNode(HealableBlock block, HealableBlockAccessor accessor) {

        final List<DependencyModel<HealableBlock>> dependencies = block.getBlockSnapshots().values().stream().collect(ArrayList<DependencyModel<HealableBlock>>::new, (list, blockSnapshot) -> {
            createDependencyNode(blockSnapshot, accessor).ifPresent(list::add);
        }, ArrayList::addAll);

        if(dependencies.isEmpty()) {
            return new DependencyNode<>(block, DEFAULT_DEPENDENCY_MODEL);
        }

        if(dependencies.size() == 1) {
            return new DependencyNode<HealableBlock>(block, dependencies.get(0));
        }

       return new DependencyNode<>(block, new AndDependencyModel<>(dependencies.toArray(new DependencyModel[dependencies.size()])));

    }

    private Optional<DependencyModel<HealableBlock>> createDependencyNode(BlockSnapshot block, HealableBlockAccessor accessor) {
        final Optional<DependencyProvider> optProvider = DependencyRegistry.getInstance().get(block.getState().getType());
        if(optProvider.isPresent()) {
            return optProvider.get().provide(block, accessor);
        }

        return Optional.empty();
    }

    public static DependencyFactory getInstance() {
        return INSTANCE;
    }
}
