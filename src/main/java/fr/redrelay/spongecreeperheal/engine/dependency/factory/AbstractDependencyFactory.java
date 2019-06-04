package fr.redrelay.spongecreeperheal.engine.dependency.factory;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.engine.dependency.DependencyFactory;
import fr.redrelay.spongecreeperheal.engine.dependency.rule.DependencyRule;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;

import java.util.Map;

public abstract class AbstractDependencyFactory implements DependencyFactory<BlockSnapshot, Vector3i, Map<Vector3i, BlockState>>{

    final DependencyRule sourceRule;

    public AbstractDependencyFactory(DependencyRule rule) {
        this.sourceRule = rule;
    }

    @Override
    public DependencyRule getSource() {
        return sourceRule;
    }
}
