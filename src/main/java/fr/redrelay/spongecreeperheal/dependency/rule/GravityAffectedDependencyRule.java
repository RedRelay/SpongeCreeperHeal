package fr.redrelay.spongecreeperheal.dependency.rule;

import fr.redrelay.spongecreeperheal.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.dependency.factory.DirectionalDependencyFactory;
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
    protected DependencyFactory getFactory(BlockType block) {
        return new DirectionalDependencyFactory.Static(this, Direction.DOWN);
    }
}