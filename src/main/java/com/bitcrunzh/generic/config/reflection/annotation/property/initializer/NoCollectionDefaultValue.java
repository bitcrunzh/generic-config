package com.bitcrunzh.generic.config.reflection.annotation.property.initializer;

import java.util.Collection;
import java.util.Collections;

public class NoCollectionDefaultValue<T> implements CollectionDefaultValueInitializer<T> {
    @Override
    public Collection<T> getDefaultValues() {
        return Collections.emptyList();
    }
}
