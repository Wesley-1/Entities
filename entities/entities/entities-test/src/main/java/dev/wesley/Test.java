package dev.wesley;

import com.google.inject.Injector;
import dev.wesley.component.Testing;
import dev.wesley.component.enums.ComponentType;
import dev.wesley.component.types.PerPlayerComponent;
import dev.wesley.entity.models.Entity;
import dev.wesley.nms.packet.types.PacketPlayInBlockDigImpl;
import dev.wesley.world.World;
import net.minecraft.network.protocol.game.PacketPlayInBlockDig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.TestOnly;

public class Test extends JavaPlugin {

    private final EntityAPI entityAPI = new EntityAPI(this, new World());
    @Override
    public void onEnable() {
        entityAPI.getInjector().getInstance(World.class).newEntity(entity -> {

            entity.add(PerPlayerComponent.class, new PerPlayerComponent(
                    Zombie.class,
                    Bukkit.getWorld("world"),
                    new Location(Bukkit.getWorld("world"), 0, 0, 0))
            );

            entity.create().spawn();

            entity.mapEvent(PacketPlayInBlockDigImpl.class, event -> {
                PacketPlayInBlockDig packet = event.getPacket();
                if (entity.hasComponent(ComponentType.PER_PLAYER_COMPONENT)) {
                    System.out.println("Digging");
                }
            });
        });
    }

    @TestOnly
    void testWorld(final World world) {
        world.newEntity(entity -> {

            entity.add(Testing::new);

            entity.mapEvent(PacketPlayInBlockDigImpl.class, packetPlayInBlockDig -> {
                if (entity.hasComponent(ComponentType.TESTING.getComponent())) {
                    // DO STUFFS
                }
            });

        });
    }
}
