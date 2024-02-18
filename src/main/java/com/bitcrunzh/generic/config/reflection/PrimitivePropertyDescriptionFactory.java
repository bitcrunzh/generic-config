package com.bitcrunzh.generic.config.reflection;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.PropertyDescription;
import com.bitcrunzh.generic.config.reflection.annotation.property.*;
import com.bitcrunzh.generic.config.validation.NumberValidationProblem;
import com.bitcrunzh.generic.config.validation.PropertyValidator;
import com.bitcrunzh.generic.config.validation.ValidationResult;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class PrimitivePropertyDescriptionFactory {
    static <T> PropertyDescription<T, Boolean> createBooleanDescription(Class<T> type, Field field, ClassDescriptionCache classDescriptionCache) {
        PropertyDescriptionBuilder<T, Boolean> builder = new PropertyDescriptionBuilder<>(type, field, classDescriptionCache, false);
        BooleanProperty booleanAnnotation = PropertyDescriptionFactory.tryGetAnnotationAndAssertNoOtherPropertyAnnotations(BooleanProperty.class, field.getType(), field);
        if (booleanAnnotation != null) {
            builder.setNameIfNotEmpty(booleanAnnotation.name());
            builder.setDescriptionIfNotEmpty(booleanAnnotation.description());
            builder.setDefaultValue(booleanAnnotation.defaultValue());
            builder.setVersion(booleanAnnotation.introducedInModelVersion());
            builder.setOptional(booleanAnnotation.isOptional());
        }
        return builder.buildSimpleProperty();
    }

    static <T> PropertyDescription<T, Byte> createByteDescription(Class<T> type, Field field, ClassDescriptionCache classDescriptionCache) {
        PropertyDescriptionBuilder<T, Byte> builder = new PropertyDescriptionBuilder<>(type, field, classDescriptionCache, (byte) 0);
        ByteProperty byteAnnotation = PropertyDescriptionFactory.tryGetAnnotationAndAssertNoOtherPropertyAnnotations(ByteProperty.class, field.getType(), field);
        if (byteAnnotation != null) {
            builder.setNameIfNotEmpty(byteAnnotation.name());
            builder.setDescriptionIfNotEmpty(byteAnnotation.description());
            builder.setDefaultValue(byteAnnotation.defaultValue());
            builder.setVersion(byteAnnotation.introducedInModelVersion());
            builder.setOptional(byteAnnotation.isOptional());
            builder.setValidator(createNumberValidator(type, field, byteAnnotation.minValue(), byteAnnotation.maxValue(), convertToSet(byteAnnotation.validValues()), Byte::compare));
        }
        return builder.buildSimpleProperty();
    }

    static <T> PropertyDescription<T, Short> createShortDescription(Class<T> type, Field field, ClassDescriptionCache classDescriptionCache) {
        PropertyDescriptionBuilder<T, Short> builder = new PropertyDescriptionBuilder<>(type, field, classDescriptionCache, (short) 0);
        ShortProperty shortAnnotation = PropertyDescriptionFactory.tryGetAnnotationAndAssertNoOtherPropertyAnnotations(ShortProperty.class, field.getType(), field);
        if (shortAnnotation != null) {
            builder.setNameIfNotEmpty(shortAnnotation.name());
            builder.setDescriptionIfNotEmpty(shortAnnotation.description());
            builder.setDefaultValue(shortAnnotation.defaultValue());
            builder.setVersion(shortAnnotation.introducedInModelVersion());
            builder.setOptional(shortAnnotation.isOptional());
            builder.setValidator(createNumberValidator(type, field, shortAnnotation.minValue(), shortAnnotation.maxValue(), convertToSet(shortAnnotation.validValues()), Short::compare));
        }
        return builder.buildSimpleProperty();
    }

    static <T> PropertyDescription<T, Integer> createIntDescription(Class<T> type, Field field, ClassDescriptionCache classDescriptionCache) {
        PropertyDescriptionBuilder<T, Integer> builder = new PropertyDescriptionBuilder<>(type, field, classDescriptionCache, 0);
        IntProperty intAnnotation = PropertyDescriptionFactory.tryGetAnnotationAndAssertNoOtherPropertyAnnotations(IntProperty.class, field.getType(), field);
        if (intAnnotation != null) {
            builder.setNameIfNotEmpty(intAnnotation.name());
            builder.setDescriptionIfNotEmpty(intAnnotation.description());
            builder.setDefaultValue(intAnnotation.defaultValue());
            builder.setVersion(intAnnotation.introducedInModelVersion());
            builder.setOptional(intAnnotation.isOptional());
            builder.setValidator(createNumberValidator(type, field, intAnnotation.minValue(), intAnnotation.maxValue(), convertToSet(intAnnotation.validValues()), Integer::compare));
        }
        return builder.buildSimpleProperty();
    }

    static <T> PropertyDescription<T, Long> createLongDescription(Class<T> type, Field field, ClassDescriptionCache classDescriptionCache) {
        PropertyDescriptionBuilder<T, Long> builder = new PropertyDescriptionBuilder<>(type, field, classDescriptionCache, 0L);
        LongProperty annotation = PropertyDescriptionFactory.tryGetAnnotationAndAssertNoOtherPropertyAnnotations(LongProperty.class, field.getType(), field);
        if (annotation != null) {
            builder.setNameIfNotEmpty(annotation.name());
            builder.setDescriptionIfNotEmpty(annotation.description());
            builder.setDefaultValue(annotation.defaultValue());
            builder.setVersion(annotation.introducedInModelVersion());
            builder.setOptional(annotation.isOptional());
            builder.setValidator(createNumberValidator(type, field, annotation.minValue(), annotation.maxValue(), convertToSet(annotation.validValues()), Long::compare));
        }
        return builder.buildSimpleProperty();
    }

    static <T> PropertyDescription<T, Float> createFloatDescription(Class<T> type, Field field, ClassDescriptionCache classDescriptionCache) {
        PropertyDescriptionBuilder<T, Float> builder = new PropertyDescriptionBuilder<>(type, field, classDescriptionCache, 0.0f);
        FloatProperty annotation = PropertyDescriptionFactory.tryGetAnnotationAndAssertNoOtherPropertyAnnotations(FloatProperty.class, field.getType(), field);
        if (annotation != null) {
            builder.setNameIfNotEmpty(annotation.name());
            builder.setDescriptionIfNotEmpty(annotation.description());
            builder.setDefaultValue(annotation.defaultValue());
            builder.setVersion(annotation.introducedInModelVersion());
            builder.setOptional(annotation.isOptional());
            builder.setValidator(createNumberValidator(type, field, annotation.minValue(), annotation.maxValue(), convertToSet(annotation.validValues()), Float::compare));
        }
        return builder.buildSimpleProperty();
    }

    static <T> PropertyDescription<T, Double> createDoubleDescription(Class<T> type, Field field, ClassDescriptionCache classDescriptionCache) {
        PropertyDescriptionBuilder<T, Double> builder = new PropertyDescriptionBuilder<>(type, field, classDescriptionCache, 0.0);
        DoubleProperty annotation = PropertyDescriptionFactory.tryGetAnnotationAndAssertNoOtherPropertyAnnotations(DoubleProperty.class, field.getType(), field);
        if (annotation != null) {
            builder.setNameIfNotEmpty(annotation.name());
            builder.setDescriptionIfNotEmpty(annotation.description());
            builder.setDefaultValue(annotation.defaultValue());
            builder.setVersion(annotation.introducedInModelVersion());
            builder.setOptional(annotation.isOptional());
            builder.setValidator(createNumberValidator(type, field, annotation.minValue(), annotation.maxValue(), convertToSet(annotation.validValues()), Double::compare));
        }
        return builder.buildSimpleProperty();
    }

    static <T> PropertyDescription<T, String> createStringDescription(Class<T> type, Field field, ClassDescriptionCache classDescriptionCache) {
        return null;
        //TODO implement;
    }

    private static Set<Byte> convertToSet(byte[] bytes) {
        Set<Byte> byteSet = new HashSet<>();
        for (byte arrayValue : bytes) {
            byteSet.add(arrayValue);
        }
        return byteSet;
    }

    private static Set<Short> convertToSet(short[] shorts) {
        Set<Short> shortSet = new HashSet<>();
        for (short arrayValue : shorts) {
            shortSet.add(arrayValue);
        }
        return shortSet;
    }

    private static Set<Integer> convertToSet(int[] integers) {
        Set<Integer> integerSet = new HashSet<>();
        for (int arrayValue : integers) {
            integerSet.add(arrayValue);
        }
        return integerSet;
    }

    private static Set<Long> convertToSet(long[] longs) {
        Set<Long> longSet = new HashSet<>();
        for (long arrayValue : longs) {
            longSet.add(arrayValue);
        }
        return longSet;
    }

    private static Set<Float> convertToSet(float[] bytes) {
        Set<Float> floatSet = new HashSet<>();
        for (float arrayValue : bytes) {
            floatSet.add(arrayValue);
        }
        return floatSet;
    }

    private static Set<Double> convertToSet(double[] bytes) {
        Set<Double> doubleSet = new HashSet<>();
        for (double arrayValue : bytes) {
            doubleSet.add(arrayValue);
        }
        return doubleSet;
    }

    private static <T extends Number> PropertyValidator<T> createNumberValidator(Class<?> parentType, Field field, T minValue, T maxValue, Set<T> validValues, Comparator<T> comparator) {
        return propertyValue -> {
            if(comparator.compare(propertyValue, minValue) < 0) {
                return new ValidationResult<>(propertyValue, NumberValidationProblem.numberTooSmall(parentType, field.getName(), field.getType(), propertyValue, minValue));
            }
            if(comparator.compare(propertyValue, maxValue) > 0) {
                return new ValidationResult<>(propertyValue, NumberValidationProblem.numberTooLarge(parentType, field.getName(), field.getType(), propertyValue, maxValue));
            }
            if(!validValues.isEmpty() && !validValues.contains(propertyValue)) {
                return new ValidationResult<>(propertyValue, NumberValidationProblem.numberNotValid(parentType, field.getName(), field.getType(), propertyValue, validValues));
            }
            return ValidationResult.empty();
        };
    }
}
