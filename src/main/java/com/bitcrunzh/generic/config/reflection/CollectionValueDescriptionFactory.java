package com.bitcrunzh.generic.config.reflection;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.property.collection.CollectionValueDescription;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CollectionValueDescriptionFactory {
    private static final Map<Class<?>, CollectionValueDescription<?>> typeCollectionDescriptionFunction = new HashMap<>();

    static {
        addPropertyDescriptionCreateFunction(boolean.class, PrimitiveDescriptionFactory::createBooleanPropertyDescription);
        addPropertyDescriptionCreateFunction(Boolean.class, PrimitiveDescriptionFactory::createBooleanPropertyDescription);
        addPropertyDescriptionCreateFunction(byte.class, PrimitiveDescriptionFactory::createBytePropertyDescription);
        addPropertyDescriptionCreateFunction(Byte.class, PrimitiveDescriptionFactory::createBytePropertyDescription);
        addPropertyDescriptionCreateFunction(short.class, PrimitiveDescriptionFactory::createShortPropertyDescription);
        addPropertyDescriptionCreateFunction(Short.class, PrimitiveDescriptionFactory::createShortPropertyDescription);
        addPropertyDescriptionCreateFunction(int.class, PrimitiveDescriptionFactory::createIntPropertyDescription);
        addPropertyDescriptionCreateFunction(Integer.class, PrimitiveDescriptionFactory::createIntPropertyDescription);
        addPropertyDescriptionCreateFunction(long.class, PrimitiveDescriptionFactory::createLongPropertyDescription);
        addPropertyDescriptionCreateFunction(Long.class, PrimitiveDescriptionFactory::createLongPropertyDescription);
        addPropertyDescriptionCreateFunction(float.class, PrimitiveDescriptionFactory::createFloatPropertyDescription);
        addPropertyDescriptionCreateFunction(Float.class, PrimitiveDescriptionFactory::createFloatPropertyDescription);
        addPropertyDescriptionCreateFunction(double.class, PrimitiveDescriptionFactory::createDoublePropertyDescription);
        addPropertyDescriptionCreateFunction(Double.class, PrimitiveDescriptionFactory::createDoublePropertyDescription);
        addPropertyDescriptionCreateFunction(String.class, PrimitiveDescriptionFactory::createStringPropertyDescription);
    }

    private CollectionValueDescriptionFactory() {
    }

    public static <P> void addPropertyDescriptionCreateFunction(Class<P> propertyType, CollectionValueDescription<P> function) {
        if (typeCollectionDescriptionFunction.containsKey(propertyType)) {
            throw new IllegalStateException("Factory already contains a create function for collection value type " + propertyType.getSimpleName());
        }
        typeCollectionDescriptionFunction.put(propertyType, function);
    }

    public interface CollectionValueDescriptionCreateFunction {
        <T, V> CollectionValueDescription<V> createPropertyDescription(Class<T> parentType, Field field, ClassDescriptionCache classDescriptionCache);
    }
}
