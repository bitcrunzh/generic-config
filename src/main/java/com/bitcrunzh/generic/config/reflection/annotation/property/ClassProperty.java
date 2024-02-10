package com.bitcrunzh.generic.config.reflection.annotation.property;

import com.bitcrunzh.generic.config.reflection.annotation.property.initializer.ClassDefaultValueInitializer;
import com.bitcrunzh.generic.config.reflection.annotation.property.initializer.NoClassDefaultValue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ClassProperty {

    /**
     * Default recommended value for this property.
     */
    Class<? extends ClassDefaultValueInitializer> defaultValue() default NoClassDefaultValue.class;

    /**
     * The polymorphic subclasses which may be assigned to this property.
     */
    Class<?>[] subClasses() default {};

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
