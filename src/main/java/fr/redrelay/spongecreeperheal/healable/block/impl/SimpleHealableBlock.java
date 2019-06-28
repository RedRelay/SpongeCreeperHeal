package fr.redrelay.spongecreeperheal.healable.block.impl;

import com.flowpowered.math.vector.Vector3i;
import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.healable.AbstractHealable;
import fr.redrelay.spongecreeperheal.healable.ChunkedHealable;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.world.BlockChangeFlags;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class SimpleHealableBlock extends AbstractHealable implements ChunkedHealable {

    private static class Keys {
        final static DataQuery BLOCK_SNAPSHOT = DataQuery.of("blockSnapshot");
    }

    private final BlockSnapshot blockSnapshot;

    public SimpleHealableBlock(BlockSnapshot blockSnapshot) {
        this.blockSnapshot = blockSnapshot;
    }

    protected SimpleHealableBlock(DataView data) throws InvalidDataException{
        super(data);
        final Optional<BlockSnapshot> optBlockSnapshot = data.getSerializable(Keys.BLOCK_SNAPSHOT, BlockSnapshot.class);
        if(!optBlockSnapshot.isPresent()) {
            SpongeCreeperHeal.getLogger().error("Found {} data without {} ... skipping.", SimpleHealableBlock.class.getSimpleName(), BlockSnapshot.class.getSimpleName());
            throw new InvalidDataException(Keys.BLOCK_SNAPSHOT.toString() + "is missing");
        }

        this.blockSnapshot = optBlockSnapshot.get();
    }

    @Override
    public void restore() {
        this.blockSnapshot.restore(true, BlockChangeFlags.NEIGHBOR_PHYSICS_OBSERVER);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer data = super.toContainer();
        data.set(Keys.BLOCK_SNAPSHOT, blockSnapshot);
        return data;
    }

    @Override
    public Vector3i getChunkPosition() {
        if(!this.blockSnapshot.getLocation().isPresent()) {
            final String errorMsg = "No location defined for a " + SimpleHealableBlock.class.getSimpleName();
            SpongeCreeperHeal.getLogger().error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        return this.blockSnapshot.getLocation().get().getChunkPosition();
    }

    @Override
    public Collection<ChunkedHealable> split() {
        return Collections.singletonList(this);
    }

    public static class Builder extends AbstractDataBuilder<SimpleHealableBlock> {

        public Builder() {
            super(SimpleHealableBlock.class, 0);
        }

        @Override
        protected Optional<SimpleHealableBlock> buildContent(DataView data) throws InvalidDataException {
            return Optional.of(new SimpleHealableBlock(data));
        }
    }


}
