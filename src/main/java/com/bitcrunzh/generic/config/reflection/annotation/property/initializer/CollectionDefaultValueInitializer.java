package com.bitcrunzh.generic.config.reflection.annotation.property.initializer;

import java.util.Collection;

public interface CollectionDefaultValueInitializer<T> {
    Collection<T> getDefaultValues();
}
