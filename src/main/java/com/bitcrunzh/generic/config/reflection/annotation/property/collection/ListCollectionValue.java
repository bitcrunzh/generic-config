package com.bitcrunzh.generic.config.reflection.annotation.property.collection;

import com.bitcrunzh.generic.config.reflection.annotation.property.initializer.CollectionDefaultValueInitializer;
import com.bitcrunzh.generic.config.reflection.annotation.property.initializer.NoCollectionDefaultValue;

import java.lang.annotation.*;

/**
 * The CollectionProperty can be used for properties of type: Array, List, Set.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ListCollectionValue {
    /**
     * Type of the context this collection value should be used in.
     * VALUE applies to Arrays, List, Set and Map values.
     * KEY only applies to Map keys.
     */
    ValueType valueType() default ValueType.VALUE;

    /**
     * @return the minimum (inclusive) valid value.
     */
    int minSize() default 0;

    /**
     * @return the maximum (inclusive) valid value.
     */
    int maxSize() default Integer.MAX_VALUE;

    Class<? extends CollectionDefaultValueInitializer> defaultValue() default NoCollectionDefaultValue.class;

    /**
     * Name of the value.
     */
    String name() default "";

    /**
     * Description of this value
     */
    String description() default "";
}
