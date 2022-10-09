package dev.wesley.entity.models;

import dev.wesley.EntityAPI;
import dev.wesley.component.Component;
import dev.wesley.component.enums.ComponentType;
import dev.wesley.entity.event.EntityEvent;
import dev.wesley.nms.packet.PacketEvent;
import dev.wesley.world.World;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Entity {
    private final UUID id;
    private final HashMap<String, Component> entityComponents;
    private final World world;

    public Entity(final World world, final UUID id) {
        this.id = id;
        this.entityComponents = new HashMap<>();
        this.world = world;

        world.getWorldEntities().add(this);
    }

    public <T extends Component> void add(final Class<T> componentClass, final T component) {
        if (component == null) return;

        if (!world.getWorldEntities().contains(this)) try {
            throw new Exception("Entity doesn't exist in the world.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (entityComponents.containsKey(component.getString())) try {
            throw new Exception("Component already added!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!world.isRegisteredComponent(component.getClass())) try {
            throw new Exception("Component not registered!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        entityComponents.put(component.getString(), component);
    }

    public <T extends PacketEvent<?>> EntityEvent<T> mapEvent(final Class<T> packetEvent, final Consumer<T> packetConsumer) {
        return new EntityEvent<>(packetConsumer);
    }

    public EntityCreator create() {
        return new EntityCreator(this);
    }

    public <T extends Component> void remove(final Class<T> componentClass, final T component) {
        if (component == null) return;

        if (!world.getWorldEntities().contains(this)) try {
            throw new Exception("Entity doesn't exist in the world.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!entityComponents.containsKey(component.getString())) try {
            throw new Exception("Component not added to entity!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!world.isRegisteredComponent(component.getClass())) try {
            throw new Exception("Component not registered!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        entityComponents.remove(component.getString());
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(final Class<T> entityComponentClass, final ComponentType componentType) {
        if (entityComponents.containsKey(componentType.getComponentName())) {
            return (T) entityComponents.get(componentType.getComponentName());
        }
        return null;
    }

    public void destroy() {
        CompletableFuture.runAsync(() -> {
            if (!world.getWorldEntities().contains(this)) try {
                throw new Exception("Entity doesn't exist!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            /**
             * Move to database storage not Map. (Otherwise remove future)
             */
            world.getWorldEntities().remove(this);
        });
    }

    public boolean hasComponent(final ComponentType component) {
        if (entityComponents.isEmpty()) return false;
        return entityComponents.containsKey(component.getComponentName());
    }
}
