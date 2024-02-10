package com.bitcrunzh.generic.config.reflection.annotation;

import com.bitcrunzh.generic.config.description.java.Version;
import com.bitcrunzh.generic.config.validation.NoObjectValidation;
import com.bitcrunzh.generic.config.validation.ObjectValidator;

@SuppressWarnings("rawtypes")
public @interface ConfigurationModel {
    String version() default "1.0";
    Class<? extends ObjectValidator> validator() default NoObjectValidation.class;
}
