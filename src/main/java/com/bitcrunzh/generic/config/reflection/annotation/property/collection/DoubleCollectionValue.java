package com.bitcrunzh.generic.config.reflection.annotation.property.collection;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(DoubleCollectionValues.class)
public @interface DoubleCollectionValue {
    /**
     * Type of the context this collection value should be used in.
     * VALUE applies to Arrays, List, Set and Map values.
     * KEY only applies to Map keys.
     */
    ValueType valueType() default ValueType.VALUE;

    /**
     * @return the minimum (inclusive) valid value.
     */
    double minValue() default Double.MIN_VALUE;
    /**
     * @return the maximum (inclusive) valid value.
     */
    double maxValue() default Double.MAX_VALUE;

    /**
     * White list of valid values.
     * An empty array means all values.
     */
    double[] validValues() default {};

    /**
     * Default recommended value for this property.
     */
    double defaultValue() default 0.0;

    /**
     * Name of the value.
     */
    String name() default "";

    /**
     * Description of this value
     */
    String description() default "";
}
