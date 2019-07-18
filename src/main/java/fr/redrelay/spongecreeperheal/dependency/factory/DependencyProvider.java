package fr.redrelay.spongecreeperheal.dependency.factory;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.dependency.model.DependencyModel;
import fr.redrelay.spongecreeperheal.dependency.rule.DependencyRule;
import fr.redrelay.spongecreeperheal.healable.factory.provider.BlockProvider;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;

import java.util.Map;
import java.util.Optional;

public interface DependencyProvider extends BlockProvider<Optional<DependencyModel<Vector3i>>> {
     DependencyRule getSource();
}
