package fr.redrelay.storage.data.container;

import fr.redrelay.storage.data.view.WorldDataView;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.World;

public class WorldDataContainer extends DelegatedDataContainer implements WorldDataView  {

    private final World world;

    public WorldDataContainer(World world, DataContainer dataContainer) {
        super(dataContainer);
        this.world = world;
    }

    public World getWorld() {
        return world;
    }
}
