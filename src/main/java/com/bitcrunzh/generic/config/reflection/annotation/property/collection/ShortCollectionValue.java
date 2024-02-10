package com.bitcrunzh.generic.config.reflection.annotation.property.collection;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(ShortCollectionValues.class)
public @interface ShortCollectionValue {
    /**
     * Type of the context this collection value should be used in.
     * VALUE applies to Arrays, List, Set and Map values.
     * KEY only applies to Map keys.
     */
    ValueType valueType() default ValueType.VALUE;

    /**
     * @return the minimum (inclusive) valid value.
     */
    short minValue() default Short.MIN_VALUE;
    /**
     * @return the maximum (inclusive) valid value.
     */
    short maxValue() default Short.MAX_VALUE;

    /**
     * White list of valid values.
     * An empty array means all values.
     */
    short[] validValues() default {};

    /**
     * Default recommended value for this property.
     */
    short defaultValue() default 0;

    /**
     * Name of the value. If empty, the field name will be used.
     */
    String name() default "";

    /**
     * Description of this value
     */
    String description() default "";
}
