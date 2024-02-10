package com.bitcrunzh.generic.config.reflection.annotation.property;

import com.bitcrunzh.generic.config.reflection.annotation.property.initializer.MapDefaultValueInitializer;
import com.bitcrunzh.generic.config.reflection.annotation.property.initializer.NoMapDefaultValue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The CollectionProperty can be used for properties of type: Array, List, Set.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MapProperty {
    /**
     * @return the minimum (inclusive) valid value.
     */
    int minSize() default 0;

    /**
     * @return the maximum (inclusive) valid value.
     */
    int maxSize() default Integer.MAX_VALUE;

    Class<? extends MapDefaultValueInitializer> defaultValue() default NoMapDefaultValue.class;

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
