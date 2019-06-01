package fr.redrelay.spongecreeperheal.chunk.component;

import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import org.slf4j.Logger;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.*;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class HealableEntry implements DataSerializable {

    private static class Keys {
        final static DataQuery BLOCK_SNAPSHOT = DataQuery.of("blockSnapshot");
        final static DataQuery REMAINING_TIME = DataQuery.of("remainingTime");
    }

    private static Logger logger = SpongeCreeperHeal.getLogger();

    private final BlockSnapshot blockSnapshot;
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
            logger.error("Cannot decrease remaining time because it is already 0 ... skipping");
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

    public static class HealableEntryBuilder extends AbstractDataBuilder<HealableEntry> {

        public HealableEntryBuilder() {
            super(HealableEntry.class, 0);
        }

        @Override
        protected Optional<HealableEntry> buildContent(DataView data) throws InvalidDataException {
            final Optional<BlockSnapshot> optBlockSnapshot = data.getSerializable(Keys.BLOCK_SNAPSHOT, BlockSnapshot.class);
            if(!optBlockSnapshot.isPresent()) {
                logger.error("Found HealableEntry data without BlockSnapshot ... skipping.");
                return Optional.empty();
            }
            final int remainingTime = data.getInt(Keys.REMAINING_TIME).orElseGet(() -> {
                logger.error("Missing \""+Keys.REMAINING_TIME.toString()+"\" data : set default to 1");
                return 1;
            });

            final HealableEntry entry = new HealableEntry(optBlockSnapshot.get());
            try {
                entry.setRemainingTime(remainingTime);
            }catch(IllegalArgumentException err) {
                logger.error("Error occured while deserialize HealableEntry : "+err.getMessage()+" ... set default to 1");
                entry.setRemainingTime(1);
            }


            return Optional.of(entry);
        }
    }
}
