package dev.wesley.system.events.subscription;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class EventSubscriptions extends ObjectSubscriptions<EventSubscription> implements Listener {

    private final RegisteredListener registeredListenerLowest;
    private final RegisteredListener registeredListenerLow;
    private final RegisteredListener registeredListenerNormal;
    private final RegisteredListener registeredListenerHigh;
    private final RegisteredListener registeredListenerHighest;
    private final RegisteredListener registeredListenerMonitor;

    public Plugin plugin;
    public static EventSubscriptions instance;

    public EventSubscriptions(Plugin plugin) {
        super(EventSubscription.class);

        instance = this;
        this.plugin = plugin;

        registeredListenerLowest = listener(EventPriority.LOWEST);
        registeredListenerLow = listener(EventPriority.LOW);
        registeredListenerNormal = listener(EventPriority.NORMAL);
        registeredListenerHigh = listener(EventPriority.HIGH);
        registeredListenerHighest = listener(EventPriority.HIGHEST);
        registeredListenerMonitor = listener(EventPriority.MONITOR);
    }

    @Override
    public void publish(Object event) {
        Map<Object, List<MethodWrapper>> calls = getCalls(event);
        for (EventPriority value : EventPriority.values()) {
            for (Map.Entry<Object, List<MethodWrapper>> entry : calls.entrySet()) {
                for (MethodWrapper methodWrapper : entry.getValue()) {
                    try {
                        if (methodWrapper.annotation.priority() == value) {
                            methodWrapper.method.setAccessible(true);
                            methodWrapper.method.invoke(entry.getKey(), event);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        new RuntimeException("Error while handling event " + event.getClass().getSimpleName() + "!", e).printStackTrace();
                    }
                }
            }
        }
    }

    public void callMethods(Object event, EventPriority priority) {
        for (Map.Entry<Object, List<MethodWrapper>> entry : getCalls(event).entrySet()) {
            for (MethodWrapper methodWrapper : entry.getValue()) {
                try {
                    if (methodWrapper.annotation.priority() == priority) {
                        methodWrapper.method.setAccessible(true);
                        methodWrapper.method.invoke(entry.getKey(), event);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private RegisteredListener listener(EventPriority priority) {
        return new RegisteredListener(this, (listener, event) -> callMethods(event, priority), priority, plugin, false);
    }

    private final Set<Class<?>> processedEvents = new HashSet<>();
    private final ReentrantLock processedEventsLock = new ReentrantLock();

    @Override
    protected void onMap(Class<?> paramClass) {
        if (Event.class.isAssignableFrom(paramClass)) {
            processedEventsLock.lock();
            try {
                if (!processedEvents.add(paramClass))
                    return;
            } finally {
                processedEventsLock.unlock();
            }

            try {
                Method method = paramClass.getMethod("getHandlerList");
                synchronized (this) {
                    HandlerList list = (HandlerList) method.invoke(null);
                    List<RegisteredListener> listeners = Arrays.asList(list.getRegisteredListeners());
                    if (!listeners.contains(registeredListenerLowest)) {
                        list.register(registeredListenerLowest);
                        list.register(registeredListenerLow);
                        list.register(registeredListenerNormal);
                        list.register(registeredListenerHigh);
                        list.register(registeredListenerHighest);
                        list.register(registeredListenerMonitor);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}