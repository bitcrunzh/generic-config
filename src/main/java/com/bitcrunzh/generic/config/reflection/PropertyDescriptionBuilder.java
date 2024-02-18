package com.bitcrunzh.generic.config.reflection;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.PropertyDescription;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.description.java.property.ClassPropertyDescription;
import com.bitcrunzh.generic.config.description.java.property.ListPropertyDescription;
import com.bitcrunzh.generic.config.description.java.property.SimplePropertyDescription;
import com.bitcrunzh.generic.config.reflection.annotation.property.initializer.ClassDefaultValueInitializer;
import com.bitcrunzh.generic.config.validation.PropertyValidator;
import com.bitcrunzh.generic.config.validation.ValidationResult;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PropertyDescriptionBuilder<C, T> {
    private final Class<C> parentType;
    private final Class<T> propertyType;
    private final Function<C, T> getterFunction;
    private final BiConsumer<C, T> setterFunction;
    private final ClassDescriptionCache classDescriptionCache;
    private String name;
    private String fieldName;
    private String description;
    private T defaultValue;
    private List<T> listDefaultValue = null;
    private PropertyValidator<T> validator;
    private boolean isOptional;
    private Version version;
    private Set<Class<? extends T>> subTypes;

    public PropertyDescriptionBuilder(Class<C> parentType, Field field, ClassDescriptionCache classDescriptionCache) {
        this(parentType, field, classDescriptionCache, null);
    }

    public PropertyDescriptionBuilder(Class<C> parentType, Field field, ClassDescriptionCache classDescriptionCache, T defaultValue) {
        this.parentType = parentType;
        //noinspection unchecked
        this.propertyType = (Class<T>) field.getType();
        name = field.getName();
        fieldName = field.getName();
        description = field.getName();
        this.classDescriptionCache = classDescriptionCache;
        this.defaultValue = field.getType().isPrimitive() ? defaultValue : null;
        validator = propertyValue -> ValidationResult.empty();
        version = Version.DEFAULT;
        isOptional = true;
        Method getterMethod = ReflectionUtil.findGetterMethod(propertyType, field);
        Method setterMethod = ReflectionUtil.findSetterMethod(propertyType, field);
        getterFunction = createGetterFunction(getterMethod);
        setterFunction = createSetterFunction(setterMethod);
    }

    public void setNameIfNotEmpty(String name) {
        if (name.isEmpty()) {
            //Ignore empty strings which is the annotation default value.
            return;
        }
        this.name = name;
    }

    public void setDescriptionIfNotEmpty(String description) {
        if (description.isEmpty()) {
            //Ignore empty strings which is the annotation default value.
            return;
        }
        this.description = description;
    }

    public void setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setValidator(PropertyValidator<T> validator) {
        this.validator = validator;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    public void setVersion(String version) {
        this.version = Version.of(version);
    }

    public void setSubTypes(Class<?>[] subTypesToSet) {
        HashSet<Class<? extends T>> newSubTypes = new HashSet<>();
        for (Class<?> subType : subTypesToSet) {
            if (!propertyType.isAssignableFrom(subType)) {
                throw new IllegalArgumentException(String.format("Property '%s.%s:%s' has subtype '%s' annotated as part of its sub types, but is not a polymorphic subtype of the property type.", parentType.getSimpleName(), fieldName, propertyType, subType.getSimpleName()));
            }
            //noinspection unchecked
            newSubTypes.add((Class<? extends T>) subType);
        }
        this.subTypes = newSubTypes;
    }

    public SimplePropertyDescription<C, T> buildSimpleProperty() {
        return new SimplePropertyDescription<>(name, fieldName, description, defaultValue, parentType, propertyType, validator, isOptional, version, getterFunction, setterFunction);
    }

    public ClassPropertyDescription<C, T> buildClassProperty() {
        Set<Class<? extends T>> subTypesNotNull = Collections.emptySet();
        if (subTypes != null) {
            subTypesNotNull = subTypes;
        }
        return new ClassPropertyDescription<>(name, fieldName, description, defaultValue, parentType, propertyType, isOptional, version, getterFunction, setterFunction, subTypesNotNull, classDescriptionCache);
    }

    public ListPropertyDescription<C, T> buildListProperty() {
        return new ListPropertyDescription<C, T>(name, fieldName, description, listDefaultValue, );
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
