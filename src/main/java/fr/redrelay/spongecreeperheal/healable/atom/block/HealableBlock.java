package fr.redrelay.spongecreeperheal.healable.atom.block;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.healable.atom.HealableAtom;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Map;

public abstract class HealableBlock extends HealableAtom {

    protected HealableBlock() {}

    protected HealableBlock(DataView data) throws InvalidDataException {
        super(data);
    }
    public abstract Map<Vector3i, BlockSnapshot> getBlockSnapshots();
}
