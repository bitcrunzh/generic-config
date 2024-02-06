package com.bitcrunzh.generic.config.value.java;

import java.util.Optional;

public interface PropertyValue<T> {
    String getPropertyName();
    Optional<T> getValue();
}
