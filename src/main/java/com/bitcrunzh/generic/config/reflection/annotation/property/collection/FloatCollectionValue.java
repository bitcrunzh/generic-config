package com.bitcrunzh.generic.config.reflection.annotation.property.collection;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(FloatCollectionValues.class)
public @interface FloatCollectionValue {
    /**
     * Type of the context this collection value should be used in.
     * VALUE applies to Arrays, List, Set and Map values.
     * KEY only applies to Map keys.
     */
    ValueType valueType() default ValueType.VALUE;

    /**
     * @return the minimum (inclusive) valid value.
     */
    float minValue() default Float.MIN_VALUE;
    /**
     * @return the maximum (inclusive) valid value.
     */
    float maxValue() default Float.MAX_VALUE;

    /**
     * White list of valid values.
     * An empty array means all values.
     */
    float[] validValues() default {};

    /**
     * Default recommended value for this property.
     */
    float defaultValue() default 0;

    /**
     * Name of the value.
     */
    String name() default "";

    /**
     * Description of this value
     */
    String description() default "";
}
