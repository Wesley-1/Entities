package dev.wesley.system.events.subscription;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class ObjectSubscriptions<A extends Annotation> {

    private class ClassSchema {
        final Map<Class<?>, Set<MethodWrapper>> methods = new HashMap<>();
    }

    public class MethodWrapper {
        public Method method;
        public A annotation;
    }

    private static class Subscription {
        WeakReference<?> reference;
        final Set<Class<?>> subscribedClasses = new HashSet<>();
    }

    private final Map<Class<?>, List<Subscription>> eventToObjects = new HashMap<>();
    private final Map<Class<?>, ClassSchema> classSchema = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    private final Class<A> annotation;

    public ObjectSubscriptions(Class<A> annotation) {
        this.annotation = annotation;
    }

    public void publish(Object event) {

        for (Map.Entry<Object, List<MethodWrapper>> entry : getCalls(event).entrySet()) {
            for (MethodWrapper methodWrapper : entry.getValue()) {
                try {
                    methodWrapper.method.setAccessible(true);
                    methodWrapper.method.invoke(entry.getKey(), event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    new RuntimeException("Error while handling event " + event.getClass().getSimpleName() + "!", e).printStackTrace();
                }
            }
        }
    }

    public Map<Object, List<MethodWrapper>> getCalls(Object event) {
        Map<Object, List<MethodWrapper>> toCall = new HashMap<>();

        lock.lock();
        try {

            Class<?> eventClass = event.getClass();

            for (Class<?> superClass : getSuperClasses(eventClass)) {
                List<Subscription> subs = eventToObjects.get(superClass);
                if (subs == null)
                    continue;
                subs.removeIf(subscription -> {
                    Object o = subscription.reference.get();
                    if (o == null)
                        return true;
                    List<MethodWrapper> methodWrappers = toCall.computeIfAbsent(o, (key) -> new ArrayList<>());
                    for (Class<?> subscribedClass : subscription.subscribedClasses) {
                        methodWrappers.addAll(classSchema.get(subscribedClass).methods.get(superClass));
                    }
                    return false;
                });
            }
        } finally {
            lock.unlock();
        }
        return toCall;
    }

    @Deprecated
    public <T> void subscribe(T object) {
        subscribe(object, object.getClass());
    }

    public <T> void subscribe(T object, Class<?> clazz) {
        lock.lock();
        try {
            if (!classSchema.containsKey(clazz)) {
                map(clazz);
            }

            ClassSchema schema = classSchema.get(clazz);
            for (Class<?> key : schema.methods.keySet()) {
                List<Subscription> subscribed = eventToObjects.computeIfAbsent(key, (k) -> new ArrayList<>());

                boolean contains = false;
                for (Subscription subscription : subscribed) {
                    Object r = subscription.reference.get();
                    if (r != null) {
                        if (Objects.equals(r, object)) {
                            subscription.subscribedClasses.add(clazz);
                            contains = true;
                            break;
                        }
                    }
                }

                Subscription subscription = new Subscription();
                subscription.reference = new WeakReference<>(object);
                subscription.subscribedClasses.add(clazz);

                if (!contains) subscribed.add(subscription);
            }
        } finally {
            lock.unlock();
        }
    }

    public void unSubscribe(Object o, Class<?> clazz) {
        lock.lock();
        try {
            ClassSchema schema = classSchema.get(clazz);
            if (schema == null)
                return;

            for (Class<?> key : schema.methods.keySet()) {
                eventToObjects.computeIfAbsent(key, (k) -> new ArrayList<>()).removeIf(s -> {
                    Object r = s.reference.get();
                    if (r == null) return true;
                    if (Objects.equals(o, r)) {
                        s.subscribedClasses.remove(clazz);
                        return s.subscribedClasses.isEmpty();
                    }
                    return false;
                });
            }
        } finally {
            lock.unlock();
        }
    }

    public void unSubscribeAll(Object o) {
        lock.lock();
        try {
            for (Class<?> superClass : getSuperClasses(o.getClass())) {
                unSubscribe(o, superClass);
            }
        } finally {
            lock.unlock();
        }
    }

    private void map(Class<?> c) {
        ClassSchema schema = new ClassSchema();
        for (Method declaredMethod : c.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(annotation)) {
                Class<?> param = declaredMethod.getParameterTypes()[0];
                MethodWrapper wrapper = new MethodWrapper();
                wrapper.method = declaredMethod;
                wrapper.annotation = declaredMethod.getAnnotation(annotation);
                schema.methods.computeIfAbsent(param, (k) -> new HashSet<>()).add(wrapper);
                onMap(param);
            }
        }
        classSchema.put(c, schema);
    }

    protected void onMap(Class<?> paramClass) {
    }

    private Class<?>[] getSuperClasses(Class<?> c) {
        List<Class<?>> l = new ArrayList<>();
        Class<?> next = c;
        while (next != null) {
            l.add(next);
            next = next.getSuperclass();
        }
        return l.toArray(new Class<?>[0]);
    }
}

