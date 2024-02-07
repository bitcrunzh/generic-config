package com.bitcrunzh.generic.config.value.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NormalizedValueFactoryTest {

    @Test
    void isSimpleType() {
        //Act
        assertTrue(NormalizedValueFactory.isSimpleType(boolean.class));
        assertTrue(NormalizedValueFactory.isSimpleType(Boolean.class));
        assertTrue(NormalizedValueFactory.isSimpleType(byte.class));
        assertTrue(NormalizedValueFactory.isSimpleType(Byte.class));
        assertTrue(NormalizedValueFactory.isSimpleType(short.class));
        assertTrue(NormalizedValueFactory.isSimpleType(Short.class));
        assertTrue(NormalizedValueFactory.isSimpleType(int.class));
        assertTrue(NormalizedValueFactory.isSimpleType(Integer.class));
        assertTrue(NormalizedValueFactory.isSimpleType(long.class));
        assertTrue(NormalizedValueFactory.isSimpleType(Long.class));
        assertTrue(NormalizedValueFactory.isSimpleType(float.class));
        assertTrue(NormalizedValueFactory.isSimpleType(Float.class));
        assertTrue(NormalizedValueFactory.isSimpleType(double.class));
        assertTrue(NormalizedValueFactory.isSimpleType(Double.class));
        assertTrue(NormalizedValueFactory.isSimpleType(String.class));
    }
}