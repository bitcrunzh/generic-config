package com.bitcrunzh.generic.config.reflection.annotation.property;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IntProperty {
    /**
     * @return the minimum (inclusive) valid value.
     */
    int minValue() default Integer.MIN_VALUE;
    /**
     * @return the maximum (inclusive) valid value.
     */
    int maxValue() default Integer.MAX_VALUE;

    /**
     * White list of valid values.
     * An empty array means all values.
     */
    int[] validValues() default {};

    /**
     * Default recommended value for this property.
     */
    int defaultValue() default 0;

    /**
     * Name of the property. If empty, the field name will be used.
     */
    String name() default "";

    /**
     * Description of this property
     */
    String description() default "";

    /**
     * Model version of the class with this property, where this property was introduced.
     */
    String introducedInModelVersion() default "1.0";

    /**
     * Whether specifying this property as part of the object is mandatory.
     * If optional, the default value will be used when not specified.
     */
    boolean isOptional() default true;
}
