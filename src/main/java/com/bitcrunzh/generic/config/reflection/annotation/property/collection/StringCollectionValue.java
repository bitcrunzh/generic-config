package com.bitcrunzh.generic.config.reflection.annotation.property.collection;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(StringCollectionValues.class)
public @interface StringCollectionValue {
    /**
     * Type of the context this collection value should be used in.
     * VALUE applies to Arrays, List, Set and Map values.
     * KEY only applies to Map keys.
     */
    ValueType valueType() default ValueType.VALUE;

    /**
     * @return the minimum (inclusive) length of the String.
     */
    int minLength() default 0;
    /**
     * @return the maximum (inclusive) length of the String.
     */
    int maxLength() default Integer.MAX_VALUE;

    /**
     * White list of valid values.
     * An empty array means all values.
     */
    String[] validValues() default {};

    /**
     * Default recommended value for this property.
     */
    String defaultValue() default "";

    /**
     * Regular Expression Pattern, used for validation values.
     * Empty means no RegEx validation.
     */
    String regExValidationPattern() default "";

    /**
     * Name of the value.
     */
    String name() default "";

    /**
     * Description of this value
     */
    String description() default "";
}
