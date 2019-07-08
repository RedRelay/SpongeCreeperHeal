package fr.redrelay.spongecreeperheal.healable.atom.block;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.healable.Healable;

import java.util.Set;

public interface HealableBlock extends Healable {
    Set<Vector3i> getBlockPositions();
}
