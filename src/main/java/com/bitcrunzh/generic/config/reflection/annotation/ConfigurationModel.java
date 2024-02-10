package com.bitcrunzh.generic.config.reflection.annotation;

import com.bitcrunzh.generic.config.validation.NoObjectValidation;
import com.bitcrunzh.generic.config.validation.ObjectValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigurationModel {
    String version() default "1.0";
    Class<? extends ObjectValidator> validator() default NoObjectValidation.class;
}
