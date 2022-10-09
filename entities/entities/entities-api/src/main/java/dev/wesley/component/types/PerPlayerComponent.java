package dev.wesley.component.types;

import dev.wesley.component.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class PerPlayerComponent implements Component {

    private final Class<? extends Entity> entityType;
    private final World bukkitWorld;
    private final Location location;

    public PerPlayerComponent(final Class<? extends Entity> entityType, final World bukkitWorld, Location location) {
        this.entityType = entityType;
        this.bukkitWorld = bukkitWorld;
        this.location = location;
    }

    @Override
    public String getString() {
        return "PerPlayerComponent";
    }

    @Override
    public void handle() {
        if (bukkitWorld == null) try {
            throw new Exception("BukkitWorld is null when creating " + getString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        final CraftEntity entity = ((CraftWorld)bukkitWorld).createEntity(location, entityType).getBukkitEntity();
        ((CraftWorld)bukkitWorld).addEntity(entity.getHandle(), CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

}
