package com.bitcrunzh.generic.config.reflection.annotation;

import com.bitcrunzh.generic.config.validation.NoValidation;
import com.bitcrunzh.generic.config.validation.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigurationModel {
    String version() default "1.0";
    Class<? extends Validator> validator() default NoValidation.class;
}
