package fr.redrelay.spongecreeperheal.task;

import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.chunk.ChunkContainerRegistry;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.scheduler.Task;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class HealTask implements Consumer<Task> {

    private static final HealTask INSTANCE = new HealTask();

    private Logger logger = SpongeCreeperHeal.getLogger();

    private Optional<Task> task = Optional.empty();

    private HealTask() {};

    @Override
    public void accept(Task task) {
        ChunkContainerRegistry.getInstance().tick();
    }

    @Listener
    public void onServerStart(GameStartingServerEvent e) {
        SpongeCreeperHeal.getInstance().ifPresent(plugin -> {
            logger.info("Starting healing task.");
            task = Optional.ofNullable(Task.builder().execute(this)
                    .interval(500, TimeUnit.MILLISECONDS)
                    .name("HealTask").submit(plugin));
        });
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent e) {
        task.ifPresent(healTask -> {
            logger.info("Stopping healing task");
            healTask.cancel();
        });
        task = Optional.empty();
    }

    public static HealTask getInstance() { return INSTANCE ; }
}