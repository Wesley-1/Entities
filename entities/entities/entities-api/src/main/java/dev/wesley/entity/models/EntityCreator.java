package dev.wesley.entity.models;

import dev.wesley.component.enums.ComponentType;
import dev.wesley.component.types.PerPlayerComponent;
import org.bukkit.Location;

public class EntityCreator {

    private final dev.wesley.entity.models.Entity customEntity;

    public EntityCreator(final Entity customEntity) {
        this.customEntity = customEntity;
    }

    public void spawn() {
        if (customEntity.hasComponent(ComponentType.PER_PLAYER_COMPONENT)) {
            customEntity.getComponent(PerPlayerComponent.class, ComponentType.PER_PLAYER_COMPONENT).handle();
        }
    }

}
