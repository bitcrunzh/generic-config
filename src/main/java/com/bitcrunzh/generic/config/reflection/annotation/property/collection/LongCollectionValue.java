package com.bitcrunzh.generic.config.reflection.annotation.property.collection;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(LongCollectionValues.class)
public @interface LongCollectionValue {
    /**
     * Type of the context this collection value should be used in.
     * VALUE applies to Arrays, List, Set and Map values.
     * KEY only applies to Map keys.
     */
    ValueType valueType() default ValueType.VALUE;

    /**
     * @return the minimum (inclusive) valid value.
     */
    long minValue() default Long.MIN_VALUE;
    /**
     * @return the maximum (inclusive) valid value.
     */
    long maxValue() default Long.MAX_VALUE;

    /**
     * White list of valid values.
     * An empty array means all values.
     */
    long[] validValues() default {};
    /**
     * Default recommended value for this property.
     */
    long defaultValue() default 0;

    /**
     * Name of the value. If empty, the field name will be used.
     */
    String name() default "";

    /**
     * Description of this value
     */
    String description() default "";

}
