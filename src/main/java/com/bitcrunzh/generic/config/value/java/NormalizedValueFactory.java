package com.bitcrunzh.generic.config.value.java;

import com.bitcrunzh.generic.config.description.java.ClassDescription;
import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;

public class NormalizedValueFactory {
    public static <V> Value<V> convertToNormalizedValue(V value, ClassDescriptionCache classDescriptionCache) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot create a specialized value type from null.");
        }
        if (isSimpleType(value.getClass())) {
            return new SimpleValue<>(value);
        }
        @SuppressWarnings("unchecked") ClassDescription<V> classDescription = (ClassDescription<V>) classDescriptionCache.getClassDescription(value.getClass());
        if (classDescription == null) {
            throw new IllegalArgumentException(String.format("Could not create normalized value for type '%s', as it was neither primitive or existed in the ClassDescriptionCache", value.getClass().getSimpleName()));
        }
        NormalizedObject<V> normalizedObject = classDescription.normalize(value, classDescriptionCache);
        return new ObjectValue<>(normalizedObject);
    }

    public static boolean isSimpleType(Class<?> type) {
        if (type.isPrimitive() || type == Boolean.class || type == Byte.class || type == Short.class || type == Integer.class || type == Long.class || type == Float.class || type == Double.class || type == String.class) {
            return true;
        }
        return false;
    }

    public static <V> V convertToValue(Value<V> normalizedValue, ClassDescriptionCache classDescriptionCache) {
        if(normalizedValue instanceof SimpleValue) {
            return ((SimpleValue<V>) normalizedValue).getValue();
        }
        if(!(normalizedValue instanceof ObjectValue)) {
            throw new IllegalArgumentException(String.format("Cannot create value from normalized value type '%s'. It must be either a SimpleValue or ObjectValue.", normalizedValue.getClass().getSimpleName()));
        }
        ObjectValue<V> objectValue = (ObjectValue<V>) normalizedValue;
        ClassDescription<V> classDescription = classDescriptionCache.getClassDescription(objectValue.getValue().getType());
        if (classDescription == null) {
            throw new IllegalArgumentException(String.format("Could not create value from normalized value for type '%s' modelVersion '%s', as it was neither primitive or existed in the ClassDescriptionCache", objectValue.getValue().getType().getSimpleName(), objectValue.getValue().getModelVersion()));
        }
        return classDescription.denormalize(objectValue.getValue(), classDescriptionCache);
    }
}
