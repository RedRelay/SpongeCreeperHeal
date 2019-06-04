package fr.redrelay.spongecreeperheal.engine.dependency.rule;

import fr.redrelay.spongecreeperheal.engine.dependency.factory.DownLayedDependencyFactory;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.property.block.GravityAffectedProperty;

import java.util.Optional;

public class GravityAffectedDependencyRule extends BlockTypeFilteredRule {

    public GravityAffectedDependencyRule() {
        super();
        this.dependencyFactory = new DownLayedDependencyFactory(this);
    }

    @Override
    public boolean matches(BlockType block) {
        final Optional<GravityAffectedProperty> opt = block.getProperty(GravityAffectedProperty.class);
        return opt.isPresent() && opt.get().getValue();
    }
}
