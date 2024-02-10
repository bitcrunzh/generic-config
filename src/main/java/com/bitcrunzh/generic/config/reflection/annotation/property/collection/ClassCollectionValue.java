package com.bitcrunzh.generic.config.reflection.annotation.property.collection;

import com.bitcrunzh.generic.config.reflection.annotation.property.initializer.ClassDefaultValueInitializer;
import com.bitcrunzh.generic.config.reflection.annotation.property.initializer.NoClassDefaultValue;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(ClassCollectionValues.class)
public @interface ClassCollectionValue {
    /**
     * Type of the context this collection value should be used in.
     * VALUE applies to Arrays, List, Set and Map values.
     * KEY only applies to Map keys.
     */
    ValueType valueType() default ValueType.VALUE;

    /**
     * Default recommended value for this value.
     */
    Class<? extends ClassDefaultValueInitializer> defaultValue() default NoClassDefaultValue.class;

    /**
     * The polymorphic subclasses which may be assigned to this property.
     */
    Class<?> topType();

    /**
     * The polymorphic subclasses which may be assigned to this property.
     */
    Class<?>[] subClasses() default {};

    /**
     * Name of the value.
     */
    String name() default "";

    /**
     * Description of this value
     */
    String description() default "";
}
