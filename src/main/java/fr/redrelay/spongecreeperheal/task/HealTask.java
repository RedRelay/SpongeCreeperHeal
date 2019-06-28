package fr.redrelay.spongecreeperheal.task;

import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.chunk.ChunkContainerRegistry;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class HealTask implements Consumer<Task> {

    private static final HealTask INSTANCE = new HealTask();

    private Task task;

    private HealTask() {}

    @Override
    public void accept(Task task) {
        ChunkContainerRegistry.getInstance().tick();
    }

    @Listener
    public void onServerStart(GameStartingServerEvent e) {
        SpongeCreeperHeal.getInstance().ifPresent(plugin -> {
            SpongeCreeperHeal.getLogger().info("Starting healing task.");
            task = Task.builder().execute(this)
                    .interval(500, TimeUnit.MILLISECONDS)
                    .name("HealTask").submit(plugin);
        });
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent e) {
        if(task != null) {
            SpongeCreeperHeal.getLogger().info("Stopping healing task");
            task.cancel();
        }
        task = null;
    }

    public static HealTask getInstance() { return INSTANCE ; }
}