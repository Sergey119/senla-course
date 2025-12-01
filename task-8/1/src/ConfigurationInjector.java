import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

public class ConfigurationInjector {

    public static void inject(Object configObject) {
        Class<?> clazz = configObject.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(ConfigProperty.class)) {
                injectField(configObject, field);
            }
        }
    }

    private static void injectField(Object configObject, Field field) {
        ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);

        try {
            String configFileName = annotation.configFileName();
            String propertyName = getPropertyName(annotation, field);
            Class<?> targetType = getTargetType(annotation, field);

            Properties properties = loadProperties(configFileName);
            String propertyValue = properties.getProperty(propertyName);

            if (propertyValue != null) {
                Object convertedValue = convertSingleValue(propertyValue, targetType);
                field.setAccessible(true);
                field.set(configObject, convertedValue);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to inject configuration for field: " + field.getName(), e);
        }
    }

    private static String getPropertyName(ConfigProperty annotation, Field field) {
        String propertyName = annotation.propertyName();
        if (propertyName.isEmpty()) {
            return field.getDeclaringClass().getSimpleName() + "." + field.getName();
        }
        return propertyName;
    }

    private static Class<?> getTargetType(ConfigProperty annotation, Field field) {
        Class<?> annotationType = annotation.type();
        if (annotationType != String.class) {
            return annotationType;
        }
        return field.getType();
    }

    private static Properties loadProperties(String configFileName) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = ConfigurationInjector.class.getClassLoader()
                .getResourceAsStream(configFileName)) {
            if (input == null) {
                throw new IOException("Configuration file not found: " + configFileName);
            }
            properties.load(input);
        }
        return properties;
    }

    private static Object convertSingleValue(String value, Class<?> targetType) {
        Map<Class<?>, Function<String, Object>> converters = new HashMap<>();

        converters.put(boolean.class, Boolean::parseBoolean);
        converters.put(Boolean.class, Boolean::parseBoolean);
        converters.put(String.class, v -> v);
        converters.put(int.class, Integer::parseInt);
        converters.put(Integer.class, Integer::parseInt);


        Function<String, Object> converter = converters.get(targetType);
        if (converter != null) {
            return converter.apply(value);
        }

        throw new IllegalArgumentException("Unsupported type: " + targetType);
    }
}