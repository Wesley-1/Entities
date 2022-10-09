package dev.wesley.world;

import dev.wesley.EntityAPI;
import dev.wesley.component.Component;
import dev.wesley.entity.models.Entity;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class World {
    private final Set<Entity> worldEntities;
    private final Map<Class<? extends Component>, Component> registeredComponents;

    public World() {
        this.worldEntities = new HashSet<>();
        this.registeredComponents = new IdentityHashMap<>();
    }

    public World newEntity(final Consumer<Entity> entityConsumer) {
        entityConsumer.accept(new Entity(this, UUID.randomUUID()));
        return this;
    }

    public World newComponent(final Supplier<Component> componentSupplier) {
        registeredComponents.put(componentSupplier.get().getClass(), componentSupplier.get());
        return this;
    }

    public boolean isRegisteredComponent(final Class<? extends Component> clazz) {
        if (registeredComponents.isEmpty()) return false;
        return registeredComponents.containsKey(clazz);
    }

    public Set<Entity> getWorldEntities() {
        return worldEntities;
    }

    public Map<Class<? extends Component>, Component> getRegisteredComponents() {
        return registeredComponents;
    }
}