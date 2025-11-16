package com.example.backend.shared.utils;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Consumer;

public class ObjectUtils {

    public static <T> void updateIfPresent(Consumer<T> consumer, T value) {
        if (!Objects.isNull(value)) {
            consumer.accept(value);
        }
    }
}
