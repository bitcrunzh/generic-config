package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.validation.PropertyValidator;
import com.bitcrunzh.generic.config.value.java.MapValue;
import com.bitcrunzh.generic.config.value.java.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

//TODO support map
public class MapPropertyDescription<C, K, V> extends PropertyDescriptionBase<C, Map<K, V>> {
    private final CollectionValueDescription<K> mapKeyDescription;
    private final CollectionValueDescription<V> mapValueDescription;
    private final ClassDescriptionCache classDescriptionCache;

    public MapPropertyDescription(String propertyName, String description, Map<K, V> defaultValue, Class<C> parentType, Class<Map<K, V>> type, PropertyValidator<Map<K, V>>validator, CollectionValueDescription<K> mapKeyDescription, CollectionValueDescription<V> mapValueDescription, boolean isOptional, Version introducedInVersion, Function<C, Map<K, V>> getterFunction, ClassDescriptionCache classDescriptionCache) {
        this(propertyName, description, defaultValue, parentType, type, validator, mapKeyDescription, mapValueDescription, isOptional, introducedInVersion, getterFunction, null, classDescriptionCache);
    }

    public MapPropertyDescription(String propertyName, String description, Map<K, V> defaultValue, Class<C> parentType, Class<Map<K, V>> type, PropertyValidator<Map<K, V>> validator, CollectionValueDescription<K> mapKeyDescription, CollectionValueDescription<V> mapValueDescription, boolean isOptional, Version introducedInVersion, Function<C, Map<K, V>> getterFunction, BiConsumer<C, Map<K, V>> setterFunction, ClassDescriptionCache classDescriptionCache) {
        super(propertyName, description, defaultValue, parentType, type, validator, isOptional, introducedInVersion, getterFunction, setterFunction);
        this.mapKeyDescription = mapKeyDescription;
        this.mapValueDescription = mapValueDescription;
        this.classDescriptionCache = classDescriptionCache;
    }

    @Override
    protected Map<K, V> createPropertyNoValidation(Value normalizedProperty) {
        if (normalizedProperty == null) {
            return null;
        }
        Map<K, V> map = new HashMap<>();
        MapValue<K, V> normalizedMap = getValueAsType(normalizedProperty, MapValue.class);
        for(Map.Entry<Value, Value> normalizedKeyValue : normalizedMap.getValue().entrySet()) {
            K key = mapKeyDescription.convertToValue(normalizedKeyValue.getKey(), classDescriptionCache);
            V value = mapValueDescription.convertToValue(normalizedKeyValue.getValue(), classDescriptionCache);
            map.put(key, value);
        }
        return map;
    }

    @Override
    protected Value createNormalizedPropertyNoValidation(Map<K, V> property) {
        if(property == null)  {
            return null;
        }
        Map<Value, Value> normalizedMap = new HashMap<>();
        for(Map.Entry<K, V> entry : property.entrySet()) {
            Value key = mapKeyDescription.convertToNormalizedValue(entry.getKey(), classDescriptionCache);
            Value value = mapValueDescription.convertToNormalizedValue(entry.getValue(), classDescriptionCache);
            normalizedMap.put(key, value);
        }
        return new MapValue<>(normalizedMap);
    }
}
