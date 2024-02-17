package com.bitcrunzh.generic.config.reflection;

import com.bitcrunzh.generic.config.description.java.ClassDescription;
import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.PropertyDescription;
import com.bitcrunzh.generic.config.description.java.property.ClassPropertyDescription;
import com.bitcrunzh.generic.config.description.java.property.SimplePropertyDescription;
import com.bitcrunzh.generic.config.reflection.annotation.property.BooleanProperty;
import com.bitcrunzh.generic.config.reflection.annotation.property.ClassProperty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class PropertyDescriptionFactory {
    private static final Map<Class<?>, PropertyDescriptionCreateFunction<?, ?>> typePropertyDescriptionFunction = new HashMap<>();

    static {
        addPropertyDescriptionCreateFunction(boolean.class, PropertyDescriptionFactory::createBooleanDescription);
        addPropertyDescriptionCreateFunction(Boolean.class, PropertyDescriptionFactory::createBooleanDescription);
    }

    private PropertyDescriptionFactory() {
    }

    public static <P> void addPropertyDescriptionCreateFunction(Class<P> propertyType, PropertyDescriptionCreateFunction<?, P> function) {
        if(typePropertyDescriptionFunction.containsKey(propertyType)) {
            throw new IllegalStateException("Factory already contains a create function for property type "+propertyType.getSimpleName());
        }
        typePropertyDescriptionFunction.put(propertyType, function);
    }
    public static <T> PropertyDescription<T, ?> create(Class<T> parentType, Field field, ClassDescriptionCache classDescriptionCache) {
        @SuppressWarnings("unchecked") PropertyDescriptionCreateFunction<T, ?> propertyDescriptionCreateFunction = (PropertyDescriptionCreateFunction<T, ?>) typePropertyDescriptionFunction.get(field.getType());
        if(propertyDescriptionCreateFunction != null) {
            return propertyDescriptionCreateFunction.createPropertyDescription(parentType, field, classDescriptionCache);
        }
        if(field.getType().getName().startsWith("java.")) {
            //All other java framework types with no annotation support, we just wrap as simple types.
            return new SimplePropertyDescriptionBuilder<>(parentType, field).build();
        }
        ensureClassDescriptionExists(parentType, field, classDescriptionCache);
        return createComplexClassPropertyDescription(parentType, field, classDescriptionCache);
    }

    private static <T> void ensureClassDescriptionExists(Class<T> parentType, Field field, ClassDescriptionCache classDescriptionCache) {
        List<Class<?>> classesToCreateDescriptionFor = new ArrayList<>();
        if(!ReflectionUtil.isAbstractClassOrInterface(field.getType())) {
            classesToCreateDescriptionFor.add(field.getType());
        }
        ClassProperty classPropertyAnnotation = field.getAnnotation(ClassProperty.class);
        if(classPropertyAnnotation != null) {
            classesToCreateDescriptionFor.addAll(Arrays.asList(classPropertyAnnotation.subClasses()));
        }
        if(classesToCreateDescriptionFor.isEmpty()) {
            throw new IllegalArgumentException(String.format("Property '%s.%s:%s' is abstract or an interface, while no sub-classes is specified.", parentType.getSimpleName(), field.getName(), field.getType().getSimpleName()));
        }
        ClassDescription<?> fieldTypeClassDescription = classDescriptionCache.getClassDescription(field.getType());
        if(fieldTypeClassDescription != null) {
            return;
        }
        ClassDescriptionFactory.create(field.getType(), classDescriptionCache);
    }

    public interface PropertyDescriptionCreateFunction<T, P> {
        PropertyDescription<T, P> createPropertyDescription(Class<T> type, Field field, ClassDescriptionCache classDescriptionCache);
    }

    private static <T> PropertyDescription<T, Boolean> createBooleanDescription(Class<T> type, Field field, ClassDescriptionCache classDescriptionCache) {
        SimplePropertyDescriptionBuilder<T, Boolean> builder = new SimplePropertyDescriptionBuilder<>(type, field, false);
        BooleanProperty booleanAnnotation = tryGetAnnotationAndAssertNoOtherPropertyAnnotations(BooleanProperty.class, type, field);
        if (booleanAnnotation != null) {
            builder.setNameIfNotEmpty(booleanAnnotation.name());
            builder.setDescriptionIfNotEmpty(booleanAnnotation.description());
            builder.setDefaultValue(booleanAnnotation.defaultValue());
            builder.setVersion(booleanAnnotation.introducedInModelVersion());
            builder.setOptional(booleanAnnotation.isOptional());
        }
        return builder.build();
    }

    private static <T extends Annotation> T tryGetAnnotationAndAssertNoOtherPropertyAnnotations(Class<T> annotationType, Class<?> parent, Field field) {
        T annotationMatch = null;
        List<Annotation> invalidPropertyAnnotations = new ArrayList<>();
        for (Annotation annotation : field.getAnnotations()) {
            if (annotationType.isInstance(annotation)) {
                //noinspection unchecked
                annotationMatch = (T) annotation;
            }
            if (annotation.getClass().getPackage().getName().equals(annotationType.getPackage().getName())) {
                //Only match for property annotations in same package.
                invalidPropertyAnnotations.add(annotation);
            }
        }
        if (!invalidPropertyAnnotations.isEmpty()) {
            throw new IllegalArgumentException(String.format("Invalid annotation(s) used for field '%s.%s:%s'. Expected only property annotation '@%s' but was '%s'", parent.getSimpleName(), field.getName(), field.getType().getSimpleName(), annotationType.getSimpleName(), printInvalidAnnotations(invalidPropertyAnnotations)));
        }
        return annotationMatch;
    }

    private static String printInvalidAnnotations(List<Annotation> invalidPropertyAnnotations) {
        if (invalidPropertyAnnotations.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Annotation annotation : invalidPropertyAnnotations) {
            stringBuilder.append("@").append(annotation.getClass().getSimpleName()).append(", ");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 2);
    }
}
