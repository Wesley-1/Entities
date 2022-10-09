package dev.wesley.component.enums;

import dev.wesley.component.Component;
import dev.wesley.component.Testing;
import dev.wesley.component.types.PerPlayerComponent;

import java.util.function.Supplier;

public enum ComponentType {

    PER_PLAYER_COMPONENT("PerPlayerComponent");

    private final String componentName;

    ComponentType(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentName() {
        return componentName;
    }
}
