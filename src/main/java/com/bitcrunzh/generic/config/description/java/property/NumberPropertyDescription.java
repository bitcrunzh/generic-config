package com.bitcrunzh.generic.config.description.java.property;

import com.bitcrunzh.generic.config.description.java.PropertyDescription;

import java.util.Optional;
import java.util.Set;

public interface NumberPropertyDescription<T extends Number> extends PropertyDescription<T> {
    T getMinimumValue();
    T getMaximumValue();
    Optional<Set<T>> getValidValues();
}
