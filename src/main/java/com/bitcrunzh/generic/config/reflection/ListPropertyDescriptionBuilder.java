package com.bitcrunzh.generic.config.reflection;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.property.collection.CollectionValueDescription;
import com.bitcrunzh.generic.config.description.java.property.ListPropertyDescription;
import com.bitcrunzh.generic.config.reflection.annotation.property.initializer.ClassDefaultValueInitializer;
import com.bitcrunzh.generic.config.validation.ValidationResult;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ListPropertyDescriptionBuilder<C, T> extends PropertyDescriptionBuilder<C, T> {
    private final CollectionValueDescription<T> collectionValueDescription;
    private final PropertyValidator<List<T>> validator;
    private List<T> collectionDefaultValue = null;
    private Class<T> topType = null;

    public ListPropertyDescriptionBuilder(Class<C> parentType, Field field, ClassDescriptionCache classDescriptionCache) {
        super(parentType, field, classDescriptionCache);
        validator = propertyValue -> ValidationResult.empty();
        CollectionValueDescription<T> collectionValueDescription = CollectionValueDescriptionFactory.create(parentType, fieldName, topType, subTypes);
    }

    public void setCollectionDefaultValue(List<T> collectionDefaultValue) {
        this.collectionDefaultValue = collectionDefaultValue;
    }

    public void setCollectionValidator(PropertyValidator<List<T>> validator) {
        this.validator = validator;
    }

    public void setTopType(Class<T> topType) {
        this.topType = topType;
        if(subTypes.isEmpty()) {
            return;
        }
        for (Class<?> subType : subTypes) {
            if (topType != null && !topType.isAssignableFrom(subType)) {
                throw new IllegalArgumentException(String.format("Property '%s.%s:%s' has subtype '%s' annotated as part of its sub types, but is not a polymorphic subtype of the property type.", parentType.getSimpleName(), fieldName, propertyType, subType.getSimpleName()));
            }
        }
    }

    public void setSubTypes(Class<?>[] subTypesToSet) {
        HashSet<Class<? extends T>> newSubTypes = new HashSet<>();
        for (Class<?> subType : subTypesToSet) {
            if (topType != null && !topType.isAssignableFrom(subType)) {
                throw new IllegalArgumentException(String.format("Property '%s.%s:%s' has subtype '%s' annotated as part of its sub types, but is not a polymorphic subtype of the property type.", parentType.getSimpleName(), fieldName, propertyType, subType.getSimpleName()));
            }
            //noinspection unchecked
            newSubTypes.add((Class<? extends T>) subType);
        }
        this.subTypes = newSubTypes;
    }

    public ListPropertyDescription<C, T> buildListProperty() {

        return new ListPropertyDescription<C, T>(name, fieldName, description, collectionDefaultValue, parentType, validator, , isOptional, version, getterFunction, setterFunction);
    }

    private static <T, V> BiConsumer<T, V> createSetterFunction(Method setterMethod) {
        if (setterMethod == null) {
            return null;
        }
        return (parentObj, value) -> {
            try {
                setterMethod.invoke(parentObj, value);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(String.format("Setter method '%s.%s:%s' can not be invoked as it is not accessible.", parentObj.getClass().getSimpleName(), setterMethod.getName(), setterMethod.getReturnType().getSimpleName()), e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException(String.format("Setter method '%s(%s param)' can not be invoked on class '%s'.", setterMethod.getName(), setterMethod.getParameterTypes()[0].getSimpleName(), parentObj.getClass().getSimpleName()), e);
            }
        };
    }

    private static <T, V> Function<T, V> createGetterFunction(Method getterMethod) {
        return (parentObj) -> {
            try {
                //noinspection unchecked
                return (V) getterMethod.invoke(parentObj);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(String.format("Getter method '%s.%s:%s' can not be invoked as it is not accessible.", parentObj.getClass().getSimpleName(), getterMethod.getName(), getterMethod.getReturnType().getSimpleName()), e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException(String.format("Getter method '%s:%s' can not be invoked on class '%s'.", getterMethod.getName(), getterMethod.getReturnType().getSimpleName(), parentObj.getClass().getSimpleName()), e);
            }
        };
    }

    public void setDefaultValueFromDefaultInitializer(@SuppressWarnings("rawtypes") Class<? extends ClassDefaultValueInitializer> initializer) {
        try {
            @SuppressWarnings("unchecked") ClassDefaultValueInitializer<T> instance = initializer.newInstance();
            defaultValue = instance.getDefaultValue();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(String.format("ClassDefaultValueIntializer '%s' annotated on property '%s.%s:%s could not be instantiated using the default constructor.'", initializer.getSimpleName(), parentType.getSimpleName(), fieldName, propertyType.getSimpleName()), e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("ClassDefaultValueIntializer '%s' annotated on property '%s.%s:%s is either no public or has a non-public default constructor.'", initializer.getSimpleName(), parentType.getSimpleName(), fieldName, propertyType.getSimpleName()), e);
        }
    }
}
