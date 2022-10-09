package dev.wesley.component.enums;

import dev.wesley.component.Component;
import dev.wesley.component.Testing;

import java.util.function.Supplier;

public enum ComponentType {
    TESTING(Testing::new),
    TESTING2(Testing::new);

    private final Supplier<Component> componentSupplier;

    ComponentType(Supplier<Component> componentSupplier) {
        this.componentSupplier = componentSupplier;
    }

    public Supplier<Component> getComponentSupplier() {
        return componentSupplier;
    }

    public Component getComponent() {
        return componentSupplier.get();
    }
}
