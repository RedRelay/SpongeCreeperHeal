import fr.redrelay.spongecreeperheal.SpongeCreeperHeal;
import fr.redrelay.spongecreeperheal.explosion.ExplosionEventHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.game.state.GameInitializationEvent;

import static org.mockito.Mockito.*;

class PluginMainTest {

    @Spy private SpongeCreeperHeal plugin;
    @Mock private GameInitializationEvent event;
    @Mock private EventManager eventManager;

    PluginMainTest() {
        MockitoAnnotations.initMocks(this);
        doReturn(eventManager).when(plugin).getEventManager();
    }

    @Test
    void ShouldListenExplosionEvent() {
        plugin.onGameInit(event);
        verify(eventManager, times(1)).registerListeners(same(plugin), any(ExplosionEventHandler.class));
    }

}
