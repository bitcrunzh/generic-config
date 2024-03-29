package com.bitcrunzh.generic.config.reflection.annotation.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface ConstructParamFieldMapping {
    /**
     *
     * @return
     */
    String[] paramFieldNames();
}
