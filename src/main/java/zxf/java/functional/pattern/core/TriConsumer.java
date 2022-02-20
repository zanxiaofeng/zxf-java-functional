package zxf.java.functional.pattern.core;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<T, U, P> {
    void accept(T x, U y, P z);

    default TriConsumer<T, U, P> andThen(TriConsumer<? super T, ? super U, ? super P> after) {
        Objects.requireNonNull(after);

        return (t, u, p) -> {
            accept(t, u, p);
            after.accept(t, u, p);
        };
    }
}