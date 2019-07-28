package fr.redrelay.spongecreeperheal.healable.atom.block;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.healable.Healable;
import org.spongepowered.api.block.BlockSnapshot;

import java.util.Map;

public interface HealableBlock extends Healable {
    Map<Vector3i, BlockSnapshot> getBlockSnapshots();
}
