import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DIContainer {
    private static DIContainer instance;
    private final Map<Class<?>, Object> singletons = new HashMap<>();
    private final Map<Class<?>, Class<?>> implementations = new HashMap<>();

    public static synchronized DIContainer getInstance() {
        if (instance == null) {
            instance = new DIContainer();
        }
        return instance;
    }

    public static <T> T resolve(Class<T> clazz) {
        return getInstance().getInstance(clazz);
    }

    public static void injectDependencies(Object instance) {
        getInstance().injectIntoInstance(instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> clazz) {
        try {
            if (clazz == DIContainer.class) {
                return (T) this;
            }

            Class<?> implementationClass = implementations.get(clazz);
            if (implementationClass != null) {
                return (T) createInstance(implementationClass);
            }

            return createInstance(clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get instance of " + clazz.getName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createInstance(Class<T> clazz) {
        if (clazz.isAnnotationPresent(Singleton.class)) {
            if (singletons.containsKey(clazz)) {
                return (T) singletons.get(clazz);
            }
        }

        try {
            T instance;
            instance = clazz.getDeclaredConstructor().newInstance();


            injectIntoInstance(instance);

            if (clazz.isAnnotationPresent(Singleton.class)) {
                singletons.put(clazz, instance);
            }

            return instance;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of " + clazz.getName(), e);
        }
    }

    // Внедрение зависимостей в существующий объект
    public void injectIntoInstance(Object instance) {
        Class<?> clazz = instance.getClass();

        // Обрабатка всей иерархии наследования
        while (clazz != null && clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Inject.class)) {
                    try {
                        Object dependency = getInstance(field.getType());
                        field.setAccessible(true);
                        field.set(instance, dependency);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to inject dependency " +
                                field.getType().getSimpleName() + " into " +
                                instance.getClass().getSimpleName() + "." + field.getName(), e);
                    }
                }
            }

            // Переходим к родительскому классу
            clazz = clazz.getSuperclass();
        }
    }

}