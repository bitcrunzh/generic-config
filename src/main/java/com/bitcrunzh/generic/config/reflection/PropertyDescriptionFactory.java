package com.bitcrunzh.generic.config.reflection;

import com.bitcrunzh.generic.config.description.java.ClassDescriptionCache;
import com.bitcrunzh.generic.config.description.java.PropertyDescription;

import java.lang.reflect.Field;

public class PropertyDescriptionFactory {
    public static <T> PropertyDescription<T, ?> create(Class<T> type, Field field, ClassDescriptionCache classDescriptionCache) {
        return null;
    }
}
