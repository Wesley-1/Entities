package dev.wesley.entity.models;

import dev.wesley.EntityAPI;
import dev.wesley.component.Component;
import dev.wesley.entity.event.EntityEvent;
import dev.wesley.nms.packet.PacketEvent;
import dev.wesley.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Entity {
    private final UUID id;
    private final Set<Component> entityComponents;
    private final World world;

    public Entity(final World world, final UUID id) {
        this.id = id;
        this.entityComponents = new HashSet<>();
        this.world = world;

        world.getWorldEntities().add(this);
    }

    public <T extends Component> void add(final Supplier<T> component) {
        if (component.get() == null) return;
        final T cmp = component.get();

        if (!world.getWorldEntities().contains(this)) try {
            throw new Exception("Entity doesn't exist in the world.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (entityComponents.contains(cmp)) try {
            throw new Exception("Component already added!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!world.isRegisteredComponent(component.get().getClass())) try {
            throw new Exception("Component not registered!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        entityComponents.add(cmp);
    }

    public <T extends PacketEvent<?>> EntityEvent<T> mapEvent(Class<T> packetEvent, Consumer<T> packetConsumer) {
        return new EntityEvent<>(packetConsumer);
    }

    public <T extends Component> void remove(final Supplier<T> component) {
        if (component.get() == null) return;

        final T cmp = component.get();

        if (!world.getWorldEntities().contains(this)) try {
            throw new Exception("Entity doesn't exist in the world.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!entityComponents.contains(cmp)) try {
            throw new Exception("Component not added to entity!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!world.isRegisteredComponent(component.get().getClass())) try {
            throw new Exception("Component not registered!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        entityComponents.remove(cmp);
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

    public <T extends Component> boolean hasComponent(final Component component) {
        if (entityComponents.isEmpty()) return false;
        return entityComponents.contains(component);
    }
}
