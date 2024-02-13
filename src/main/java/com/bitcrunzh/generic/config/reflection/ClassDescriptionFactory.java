package com.bitcrunzh.generic.config.reflection;

import com.bitcrunzh.generic.config.description.java.ClassDescription;
import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.PropertyDescription;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.reflection.annotation.ConfigurationModel;
import com.bitcrunzh.generic.config.reflection.annotation.util.ConstructParamFieldMapping;
import com.bitcrunzh.generic.config.validation.ObjectValidator;
import com.bitcrunzh.generic.config.value.java.NormalizedObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;

public class ClassDescriptionFactory {
    public static <T> ClassDescription<T> create(Class<T> type) {
        ConfigurationModel annotation = type.getAnnotation(ConfigurationModel.class);
        Version modeVersion = getModelVersion(annotation);
        ClassDescriptionCache classDescriptionCache = new ClassDescriptionCache();
        List<PropertyDescription<T, ?>> propertyDescriptions = createPropertyDescriptions(type, classDescriptionCache);
        Function<NormalizedObject<T>, T> constructorFunction = createConstructorFunction(propertyDescriptions, type);
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

    private static <T> Function<NormalizedObject<T>, T> createConstructorFunction(List<PropertyDescription<T, ?>> propertyDescriptions, Class<T> type) {
        Set<String> propertiesWithNoSetter = Collections.unmodifiableSet(filterPropertiesWithNoSetter(propertyDescriptions));
        for (Constructor<?> constructor : type.getConstructors()) {
            Set<String> propertiesRequiredInConstructor = new HashSet<>(propertiesWithNoSetter);
            Set<String> propertiesToUseSetter = getPropertyNames(propertyDescriptions);
            List<String> propertiesToUseConstructor = new ArrayList<>();
            boolean isPublic = Modifier.isPublic(constructor.getModifiers());
            boolean isEmptyConstructor = constructor.getParameters() == null || constructor.getParameters().length == 0;
            if (
                    !isPublic ||
                            (isEmptyConstructor && !propertiesRequiredInConstructor.isEmpty())

            ) {
                //Not usable constructor
                continue;
            }
            if(isEmptyConstructor && propertiesRequiredInConstructor.isEmpty()) {
                return createConstructorFunction(propertiesToUseSetter, propertiesToUseConstructor, propertyDescriptions, constructor);
            }
                ConstructParamFieldMapping paramFieldMapping = constructor.getAnnotation(ConstructParamFieldMapping.class);
        }
        return null;
    }

    private static <T> Set<String> getPropertyNames(List<PropertyDescription<T,?>> propertyDescriptions) {
        return null;
    }

    private static <T> Set<String> filterPropertiesWithNoSetter(List<PropertyDescription<T,?>> propertyDescriptions) {
        return null;
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
