package fr.redrelay.spongecreeperheal.chunk.component;

import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.*;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

/**
 * HealableEntry is a wrapper for a BlockSnapshot and a timer
 * The timer should decrement over time, when it reaches 0, then
 * BlockSnapshot need to be restored
 */
public class HealableEntry implements DataSerializable {

    private static class Keys {
        final static DataQuery BLOCK_SNAPSHOT = DataQuery.of("blockSnapshot");
        final static DataQuery REMAINING_TIME = DataQuery.of("remainingTime");
    }

    private final BlockSnapshot blockSnapshot;
    /**
     * Number of time the task must be called before restore BlockSnapshot
     */
    private int remainingTime = 1;

    public HealableEntry(BlockSnapshot blockSnapshot) {
        this.blockSnapshot = blockSnapshot;
    }

    public BlockSnapshot getBlockSnapshot() {
        return blockSnapshot;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        if(remainingTime < 1) {
            throw new IllegalArgumentException("Unable to set a remaining time < 0 ... was "+remainingTime);
        }
        this.remainingTime = remainingTime;
    }

    public void decreaseRemainingTime() {
        if(this.remainingTime <= 0) {
            SpongeCreeperHeal.getLogger().error("Cannot decrease remaining time because it is already 0 ... skipping");
            return;
        }
        this.remainingTime--;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer data = DataContainer.createNew();
        data.set(Queries.CONTENT_VERSION, getContentVersion());
        data.set(Keys.BLOCK_SNAPSHOT, blockSnapshot);
        data.set(Keys.REMAINING_TIME, remainingTime);
        return data;
    }

    /**
     * Class used to build HealableEntry
     */
    public static class HealableEntryBuilder extends AbstractDataBuilder<HealableEntry> {

        public HealableEntryBuilder() {
            super(HealableEntry.class, 0);
        }

        @Override
        protected Optional<HealableEntry> buildContent(DataView data) throws InvalidDataException {
            final Optional<BlockSnapshot> optBlockSnapshot = data.getSerializable(Keys.BLOCK_SNAPSHOT, BlockSnapshot.class);
            if(!optBlockSnapshot.isPresent()) {
                SpongeCreeperHeal.getLogger().error("Found {} data without {} ... skipping.", HealableEntry.class.getSimpleName(), BlockSnapshot.class.getSimpleName());
                return Optional.empty();
            }
            final int remainingTime = data.getInt(Keys.REMAINING_TIME).orElseGet(() -> {
                SpongeCreeperHeal.getLogger().error("Missing \"{}\" data : set default to {}", Keys.REMAINING_TIME.toString(), 1);
                return 1;
            });

            final HealableEntry entry = new HealableEntry(optBlockSnapshot.get());
            try {
                entry.setRemainingTime(remainingTime);
            }catch(IllegalArgumentException err) {
                SpongeCreeperHeal.getLogger().error("Error occured while deserialize {} : {} ... set default to {}", HealableEntry.class.getSimpleName(), err.getMessage(), 1);
                entry.setRemainingTime(1);
            }


            return Optional.of(entry);
        }
    }
}
