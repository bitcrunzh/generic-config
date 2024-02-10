package com.bitcrunzh.generic.config.util;

import java.util.Collection;

public class PrintUtil {
    public static String printSimpleClassNames(Collection<Class<?>> subTypes) {
        if (subTypes == null || subTypes.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Class<?> type : subTypes) {
            stringBuilder.append(type.getSimpleName()).append(", ");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 2);
    }
}
