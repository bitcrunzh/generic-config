package com.bitcrunzh.generic.config.reflection;

import com.bitcrunzh.generic.config.description.java.ClassDescription;
import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.PropertyDescription;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.reflection.annotation.ConfigurationModel;
import com.bitcrunzh.generic.config.validation.ObjectValidator;
import com.bitcrunzh.generic.config.value.java.NormalizedObject;
import com.bitcrunzh.generic.config.value.java.NormalizedProperty;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;

public class ClassDescriptionFactory {
    public static <T> ClassDescription<T> create(Class<T> type) {
        ConfigurationModel annotation = type.getAnnotation(ConfigurationModel.class);
        Version modeVersion = getModelVersion(annotation);
        ClassDescriptionCache classDescriptionCache = new ClassDescriptionCache();
        List<PropertyDescription<T, ?>> propertyDescriptions = createPropertyDescriptions(type, classDescriptionCache);
        Function<NormalizedObject<T>, T> constructorFunction = createConstructorFunction(propertyDescriptions, type, classDescriptionCache);
        ObjectValidator<T> validator = createValidator(type, annotation);
        return new ClassDescription<>(type, modeVersion, propertyDescriptions, constructorFunction, validator);
    }

    private static <T> ObjectValidator<T> createValidator(Class<T> type, ConfigurationModel annotation) {
        try {
            //noinspection unchecked
            return (ObjectValidator<T>) annotation.validator().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("@ConfigurationModel(validator = %s.class) - validator '%s' cannot be instantiated, reason: '%s'", type.getSimpleName(), type.getSimpleName(), e.getMessage()));
        }
    }

    private static <T> Function<NormalizedObject<T>, T> createConstructorFunction(List<PropertyDescription<T, ?>> propertyDescriptions, Class<T> type, ClassDescriptionCache classDescriptionCache) {
        ReflectionUtil.ConstructorMatch<T> constructorMatch = ReflectionUtil.findconstructor(propertyDescriptions, type);
        if (!constructorMatch.isConstructorFound()) {
            throw new IllegalArgumentException(String.format("Unable to find appropriate constructor for instantiating all properties for type '%s'. Constructors investigated:", type.getSimpleName(), printConstructors(constructorMatch.getNotMatchingConstructors())));
        }
        return createConstructorFunctionFromMatch(constructorMatch, propertyDescriptions, type, classDescriptionCache);
    }

    private static String printConstructors(List<String> notMatchingConstructors) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String notMatchingConstructor : notMatchingConstructors) {
            stringBuilder.append("%n").append(notMatchingConstructor);
        }
        return stringBuilder.toString();
    }

    private static <T> Function<NormalizedObject<T>, T> createConstructorFunctionFromMatch(ReflectionUtil.ConstructorMatch<T> constructorMatch, List<PropertyDescription<T, ?>> propertyDescriptions, Class<T> type, ClassDescriptionCache classDescriptionCache) {
        Map<String, PropertyDescription<T, ?>> propertyDescriptionMap = new HashMap<>();
        for (PropertyDescription<T, ?> propertyDescription : propertyDescriptions) {
            propertyDescriptionMap.put(propertyDescription.getPropertyName(), propertyDescription);
        }
        return normalizedObject -> {
            Object[] constructorArgs = getConstructorArgs(normalizedObject, constructorMatch, classDescriptionCache);
            try {
                T object = constructorMatch.getConstructor().newInstance(constructorArgs);
                for (PropertyDescription<T, ?> propertyDescription : constructorMatch.getSetterProperties().values()) {
                    NormalizedProperty<?> normalizedPropertyValue = normalizedObject.getProperty(propertyDescription.getPropertyName());
                    //noinspection unchecked,CastCanBeRemovedNarrowingVariableType
                    setProperty(object, (NormalizedProperty<Object>) normalizedPropertyValue, (PropertyDescription<T, Object>) propertyDescription, classDescriptionCache);
                }
                return object;
            } catch (InstantiationException e) {
                throw new IllegalStateException("Failed to instantiate object.", e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Failed to instantiate object. Constructor must be public.", e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException("Failed to instantiate object. Wrong invocation target.", e);
            }
        };
    }

    private static <T, V> Object[] getConstructorArgs(NormalizedObject<T> normalizedObject, ReflectionUtil.ConstructorMatch<T> constructorMatch, ClassDescriptionCache classDescriptionCache) {
        Object[] constructorParameters = new Object[constructorMatch.getConstructorParameters().size()];
        for (int i = 0; i < constructorMatch.getConstructorParameters().size(); i++) {
            @SuppressWarnings("unchecked") PropertyDescription<T, V> propertyDescription = (PropertyDescription<T, V>) constructorMatch.getConstructorParameters().get(i);
            NormalizedProperty<V> normalizedProperty = normalizedObject.getProperty(propertyDescription.getPropertyName());
            V property = getProperty(normalizedProperty, propertyDescription, classDescriptionCache);
            constructorParameters[i] = property;
        }
        return constructorParameters;
    }

    private static <T, V> V getProperty(NormalizedProperty<V> normalizedPropertyValue, PropertyDescription<T, V> propertyDescription, ClassDescriptionCache classDescriptionCache) {
        return propertyDescription.createProperty(normalizedPropertyValue, classDescriptionCache).orElse(null);
    }

    private static <T, V> void setProperty(T object, NormalizedProperty<V> normalizedPropertyValue, PropertyDescription<T, V> propertyDescription, ClassDescriptionCache classDescriptionCache) {
        V value = getProperty(normalizedPropertyValue, propertyDescription, classDescriptionCache);
        propertyDescription.getSetterFunction().orElseThrow(() -> new IllegalStateException("No setter function for property " + normalizedPropertyValue.getPropertyName())).accept(object, value);
    }

    private static <T> Set<String> getPropertyNames(List<PropertyDescription<T, ?>> propertyDescriptions) {
        Set<String> propertyNames = new HashSet<>();
        for (PropertyDescription<T, ?> propertyDescription : propertyDescriptions) {
            propertyNames.add(propertyDescription.getPropertyName());
        }
        return propertyNames;
    }

    private static <T> Set<String> filterPropertiesWithNoSetter(List<PropertyDescription<T, ?>> propertyDescriptions) {
        Set<String> propertyNames = new HashSet<>();
        for (PropertyDescription<T, ?> propertyDescription : propertyDescriptions) {
            if (!propertyDescription.getSetterFunction().isPresent()) {
                propertyNames.add(propertyDescription.getPropertyName());
            }
        }
        return propertyNames;
    }

    private static <T> Function<NormalizedObject<T>, T> createConstructorFunction(Set<String> propertiesToUseSetter, List<String> propertiesToUseConstructor, List<PropertyDescription<T, ?>> propertyDescriptions, Constructor<?> constructor) {
        return null;
    }

    private static <T> List<Field> getAllFields(Class<T> type) {
        List<Field> foundFields = Arrays.asList(type.getDeclaredFields());
        if (type.getSuperclass() != Object.class) {
            foundFields.addAll(getAllFields(type.getSuperclass()));
        }
        return foundFields;
    }

    private static <T> List<PropertyDescription<T, ?>> createPropertyDescriptions(Class<T> type, ClassDescriptionCache classDescriptionCache) {
        List<PropertyDescription<T, ?>> propertyDescriptions = new ArrayList<>();
        for (Field field : getAllFields(type)) {
            propertyDescriptions.add(PropertyDescriptionFactory.create(type, field, classDescriptionCache));
        }
        return propertyDescriptions;
    }

    private static Version getModelVersion(ConfigurationModel annotation) {
        if (annotation == null) {
            return Version.DEFAULT;
        } else {
            return Version.of(annotation.version());
        }
    }
}
