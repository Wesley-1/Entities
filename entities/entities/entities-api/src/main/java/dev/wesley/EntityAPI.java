package dev.wesley;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import dev.wesley.component.Testing;
import dev.wesley.component.enums.ComponentType;
import dev.wesley.nms.injector.PlayerPacketInjector;
import dev.wesley.nms.packet.types.PacketPlayInBlockDigImpl;
import dev.wesley.system.events.subscription.EventSubscriptions;
import dev.wesley.world.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.TestOnly;

public class EntityAPI {
    private final JavaPlugin plugin;
    private final World world;
    private Injector injector;
    private final PlayerPacketInjector packetInjector;
    private final EventSubscriptions eventSubscriptions;

    public EntityAPI(final JavaPlugin plugin, final World world) {
        this.plugin = plugin;
        this.world = world;
        this.injector = Guice.createInjector(new EntityModule(this.plugin, this.world));
        this.eventSubscriptions = new EventSubscriptions(this.plugin);
        this.packetInjector = new PlayerPacketInjector();
    }

    public PlayerPacketInjector getPacketInjector() {
        return packetInjector;
    }

    public EventSubscriptions getEventSubscriptions() {
        return eventSubscriptions;
    }

    public Injector getInjector() {
        return injector;
    }
}

class EntityModule extends AbstractModule {

    private final JavaPlugin plugin;
    private final World world;

    public EntityModule(final JavaPlugin plugin, final World world) {
        this.plugin = plugin;
        this.world = world;
    }

    @Override
    protected void configure() {
        this.bind(JavaPlugin.class).toInstance(plugin);
        this.bind(World.class).toInstance(world);
    }
}
