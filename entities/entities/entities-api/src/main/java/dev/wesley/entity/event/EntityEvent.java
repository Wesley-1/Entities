package dev.wesley.entity.event;

import dev.wesley.nms.packet.PacketEvent;
import dev.wesley.system.events.subscription.EventSubscription;
import dev.wesley.system.events.subscription.EventSubscriptions;

import java.util.function.Consumer;

public class EntityEvent<T extends PacketEvent<?>> {

    private final Consumer<T> packetConsumer;

    public EntityEvent(final Consumer<T> packetConsumer) {
        this.packetConsumer = packetConsumer;
        EventSubscriptions.instance.subscribe(this, getClass());
    }

    @EventSubscription
    private void handle(final T event) {
        this.packetConsumer.accept(event);
    }
}
