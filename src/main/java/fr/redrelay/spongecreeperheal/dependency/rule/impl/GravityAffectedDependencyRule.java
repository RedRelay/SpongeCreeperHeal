package fr.redrelay.spongecreeperheal.dependency.rule.impl;

import fr.redrelay.spongecreeperheal.dependency.factory.DependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.factory.impl.DirectionalDependencyProvider;
import fr.redrelay.spongecreeperheal.dependency.rule.BlockTypeFilteredRule;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.property.block.GravityAffectedProperty;
import org.spongepowered.api.util.Direction;

import java.util.Optional;

public class GravityAffectedDependencyRule extends BlockTypeFilteredRule {

    @Override
    public boolean matches(BlockType block) {
        final Optional<GravityAffectedProperty> opt = block.getProperty(GravityAffectedProperty.class);
        return opt.isPresent() && opt.get().getValue();
    }

    @Override
    protected DependencyProvider getFactory(BlockType block) {
        return new DirectionalDependencyProvider.Static(this, Direction.DOWN);
    }
}
