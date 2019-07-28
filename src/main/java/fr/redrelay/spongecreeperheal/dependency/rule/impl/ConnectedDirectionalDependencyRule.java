package fr.redrelay.spongecreeperheal.dependency.rule.impl;

import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.dependency.model.OrDependencyModel;
import fr.redrelay.spongecreeperheal.dependency.provider.ConnectedDirectionDependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.provider.DependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.rule.BlockClassFilteredRule;
import fr.redrelay.spongecreeperheal.healable.atom.block.HealableBlock;
import net.minecraft.block.Block;
import org.spongepowered.api.block.BlockType;

public class ConnectedDirectionalDependencyRule extends BlockClassFilteredRule {


    public ConnectedDirectionalDependencyRule(Class<? extends Block> blockClass) {
        super(blockClass);
    }

    @Override
    protected DependencyProvider getFactory(BlockType block) {
        return new ConnectedDirectionDependencyProvider(this) {
            @Override
            protected DependencyModel<HealableBlock> merge(DependencyModel<HealableBlock>... dependencies) {
                return new OrDependencyModel<>(dependencies);
            }
        };
    }
}
