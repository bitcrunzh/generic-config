package com.bitcrunzh.generic.config.reflection.annotation.property.collection;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(BooleanCollectionValues.class)
public @interface BooleanCollectionValue {
    /**
     * Type of the context this collection value should be used in.
     * VALUE applies to Arrays, List, Set and Map values.
     * KEY only applies to Map keys.
     */
    ValueType valueType() default ValueType.VALUE;

    /**
     * Default recommended value for this property.
     */
    boolean defaultValue() default false;

    /**
     * Name of the value.
     */
    String name() default "";

    /**
     * Description of this value
     */
    String description() default "";
}
