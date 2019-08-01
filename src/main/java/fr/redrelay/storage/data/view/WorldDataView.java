package fr.redrelay.storage.data.view;

import org.spongepowered.api.data.DataView;
import org.spongepowered.api.world.World;

public interface WorldDataView extends DataView {
    World getWorld();
}
