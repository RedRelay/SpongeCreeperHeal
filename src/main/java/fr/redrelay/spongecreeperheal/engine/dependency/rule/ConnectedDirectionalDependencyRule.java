package fr.redrelay.spongecreeperheal.engine.dependency.rule;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.dependency.model.OrDependencyModel;
import fr.redrelay.spongecreeperheal.engine.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.engine.dependency.factory.ConnectedDirectionDependencyFactory;
import net.minecraft.block.Block;
import org.spongepowered.api.block.BlockType;

public class ConnectedDirectionalDependencyRule extends BlockClassFilteredRule {


    public ConnectedDirectionalDependencyRule(Class<? extends Block> blockClass) {
        super(blockClass);
    }

    @Override
    protected DependencyFactory getFactory(BlockType block) {
        return new ConnectedDirectionDependencyFactory(this) {
            @Override
            protected DependencyModel<Vector3i> merge(DependencyModel<Vector3i>... dependencies) {
                return new OrDependencyModel<>(dependencies);
            }
        };
    }
}
