package com.bitcrunzh.generic.config.reflection.annotation.property.collection;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(ByteCollectionValues.class)
public @interface ByteCollectionValue {
    /**
     * Type of the context this collection value should be used in.
     * VALUE applies to Arrays, List, Set and Map values.
     * KEY only applies to Map keys.
     */
    ValueType valueType() default ValueType.VALUE;

    /**
     * @return the minimum (inclusive) valid value.
     */
    byte minValue() default Byte.MIN_VALUE;
    /**
     * @return the maximum (inclusive) valid value.
     */
    byte maxValue() default Byte.MAX_VALUE;

    /**
     * White list of valid values.
     * An empty array means all values.
     */
    byte[] validValues() default {};

    /**
     * Default recommended value for this property.
     */
    byte defaultValue() default 0;

    /**
     * Name of the value.
     */
    String name() default "";

    /**
     * Description of this value
     */
    String description() default "";
}
