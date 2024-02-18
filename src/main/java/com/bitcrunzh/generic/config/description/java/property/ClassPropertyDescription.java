package com.bitcrunzh.generic.config.description.java.property;


import com.bitcrunzh.generic.config.description.java.ClassDescription;
import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.util.PrintUtil;
import com.bitcrunzh.generic.config.validation.*;
import com.bitcrunzh.generic.config.value.java.*;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ClassPropertyDescription<C, T> extends PropertyDescriptionBase<C, T> {
    private final Set<Class<?>> allTypes;
    private final ClassDescriptionCache classDescriptionCache;

    public ClassPropertyDescription(String propertyName, String fieldName, String description, T defaultValue, Class<C> parentType, Class<T> type, boolean isOptional, Version introducedInVersion, Function<C, T> getterFunction, Set<Class<? extends T>> subTypes, ClassDescriptionCache classDescriptionCache) {
        this(
                propertyName,
                fieldName,
                description,
                defaultValue,
                parentType,
                type,
                isOptional,
                introducedInVersion,
                getterFunction,
                null,
                subTypes,
                classDescriptionCache);
    }

    public ClassPropertyDescription(String propertyName, String fieldName, String description, T defaultValue, Class<C> parentType, Class<T> type, boolean isOptional, Version introducedInVersion, Function<C, T> getterFunction, BiConsumer<C, T> setterFunction, Set<Class<? extends T>> subTypes, ClassDescriptionCache classDescriptionCache) {
        super(
                propertyName,
                fieldName,
                description,
                defaultValue,
                parentType,
                type,
                propertyValue -> {
                    @SuppressWarnings("unchecked") ClassDescription<T> classDescription = (ClassDescription<T>) classDescriptionCache.getClassDescription(propertyValue.getClass());
                    if (classDescription == null) {
                        return ValidationResult.of(new UnknownClassDescriptionProblem(parentType, type, propertyName));
                    }
                    Set<Class<?>> allTypes = new HashSet<>(subTypes);
                    allTypes.add(type);
                    if (!allTypes.contains(propertyValue.getClass())) {
                        return ValidationResult.of(new PropertyTypeProblem(propertyName, allTypes, propertyValue.getClass()));
                    }
                    return classDescription.validate(propertyValue);
                },
                isOptional,
                introducedInVersion,
                getterFunction,
                setterFunction);
        this.allTypes = new HashSet<>(subTypes);
        this.classDescriptionCache = classDescriptionCache;
        this.allTypes.add(type);
    }

    @Override
    protected T createPropertyNoValidation(Value normalizedValue) {
        if (normalizedValue == null) {
            return null;
        }
        NormalizedObject<T> objectValue = getValueAsType(normalizedValue, NormalizedObject.class);
        ClassDescription<T> classDescription = classDescriptionCache.getClassDescription(objectValue.getType());

        return classDescription.denormalize(objectValue);
    }

    @Override
    protected Value createNormalizedPropertyNoValidation(T property) {
        @SuppressWarnings("unchecked") ClassDescription<T> classDescription = (ClassDescription<T>) classDescriptionCache.getClassDescription(property.getClass());
        if (classDescription == null) {
            throw new IllegalArgumentException(String.format("ClassPropertyDescription for topType '%s', subTypes '%s', cannot be used for type '%s' as no ClassDescription could be found in the ClassDescriptionCache.", getType().getSimpleName(), PrintUtil.printSimpleClassNames(allTypes), property.getClass().getSimpleName()));
        }
        return classDescription.normalize(property);
    }

}
