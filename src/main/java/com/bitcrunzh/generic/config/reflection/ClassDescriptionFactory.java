package com.bitcrunzh.generic.config.reflection;

import com.bitcrunzh.generic.config.description.java.ClassDescription;
import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.PropertyDescription;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.reflection.annotation.ConfigurationModel;
import com.bitcrunzh.generic.config.validation.Validator;
import com.bitcrunzh.generic.config.value.java.NormalizedObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ClassDescriptionFactory {
    public static <T> ClassDescription<T> create(Class<T> type) {
        return create(type, new ClassDescriptionCache());
    }

    public static <T> ClassDescription<T> create(Class<T> type, ClassDescriptionCache classDescriptionCache) {
        ConfigurationModel annotation = type.getAnnotation(ConfigurationModel.class);
        Version modeVersion = getModelVersion(annotation);
        List<PropertyDescription<T, ?>> propertyDescriptions = createPropertyDescriptions(type, classDescriptionCache);
        Function<NormalizedObject<T>, T> constructorFunction = createConstructorFunction(propertyDescriptions, type, classDescriptionCache);
        Validator<T> validator = createValidator(type, annotation);
        ClassDescription<T> classDescription = new ClassDescription<>(type, modeVersion, propertyDescriptions, constructorFunction, validator);
        classDescriptionCache.addClassDescription(classDescription);
        return classDescription;
    }

    private static <T> Validator<T> createValidator(Class<T> type, ConfigurationModel annotation) {
        try {
            //noinspection unchecked
            return (Validator<T>) annotation.validator().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("@ConfigurationModel(validator = %s.class) - validator '%s' cannot be instantiated, reason: '%s'", type.getSimpleName(), type.getSimpleName(), e.getMessage()));
        }
    }

    private static <T> Function<NormalizedObject<T>, T> createConstructorFunction(List<PropertyDescription<T, ?>> propertyDescriptions, Class<T> type, ClassDescriptionCache classDescriptionCache) {
        return ReflectionUtil.createConstructorFunction(type, propertyDescriptions, classDescriptionCache);
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
