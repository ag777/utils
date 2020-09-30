package com.ag777.util.lang.function;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<T, U, W> {
    void accept(T t, U u, W w);
    default TriConsumer<T, U, W> andThen(TriConsumer<? super T, ? super U, ? super W> after) {
        Objects.requireNonNull(after);
        return (l, r, w) -> {
            accept(l, r, w);
            after.accept(l, r, w);
        };
    }
}
