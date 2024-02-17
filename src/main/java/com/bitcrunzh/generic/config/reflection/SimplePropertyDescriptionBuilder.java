package com.bitcrunzh.generic.config.reflection;

import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.description.java.property.SimplePropertyDescription;
import com.bitcrunzh.generic.config.validation.PropertyValidator;
import com.bitcrunzh.generic.config.validation.ValidationResult;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SimplePropertyDescriptionBuilder<C, T> {
    private final Class<C> parentType;
    private final Class<T> propertyType;
    private final Function<C,T> getterFunction;
    private final BiConsumer<C,T> setterFunction;
    private String name;
    private String description;
    private T defaultValue;
    private PropertyValidator<T> validator;
    private boolean isOptional;
    private Version version;

    public SimplePropertyDescriptionBuilder(Class<C> parentType, Field field) {
        this(parentType, field, null);
    }

    public SimplePropertyDescriptionBuilder(Class<C> parentType, Field field, T defaultValue) {
        this.parentType = parentType;
        //noinspection unchecked
        this.propertyType = (Class<T>) field.getType();
        name = field.getName();
        description = field.getName();
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
        if(name.isEmpty()) {
            //Ignore empty strings which is the annotation default value.
            return;
        }
        this.name = name;
    }

    public void setDescriptionIfNotEmpty(String description) {
        if(description.isEmpty()) {
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

    public SimplePropertyDescription<C, T> build() {
        return new SimplePropertyDescription<>(name, description, defaultValue, parentType, propertyType, validator, isOptional, version, getterFunction, setterFunction);
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
}
