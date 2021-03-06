package fr.redrelay.spongecreeperheal.dependency.rule.impl;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.dependency.model.OrDependencyModel;
import fr.redrelay.spongecreeperheal.dependency.factory.ConnectedDirectionDependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.factory.DependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.rule.BlockClassFilteredRule;
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
