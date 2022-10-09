package dev.wesley;

import dev.wesley.component.Testing;
import dev.wesley.component.enums.ComponentType;
import dev.wesley.nms.packet.types.PacketPlayInBlockDigImpl;
import dev.wesley.world.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.TestOnly;

public class Test extends JavaPlugin {

    @Override
    public void onEnable() {
        new EntityAPI(this, new World());
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
