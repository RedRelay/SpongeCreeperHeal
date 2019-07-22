package fr.redrelay.spongecreeperheal.dependency.provider;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import fr.redrelay.spongecreeperheal.factory.healable.block.BlockProvider;

import java.util.Optional;

public interface DependencyProvider extends BlockProvider<Optional<DependencyModel<Vector3i>>> {
     DependencyRule getSource();
}
