package zxf.java.functional.core.function;

import java.util.Objects;

@FunctionalInterface
public interface TriPredicate<T, U, P> {
    boolean test(T t, U u, P p);

    default TriPredicate<T, U, P> and(TriPredicate<? super T, ? super U, ? super P> other) {
        Objects.requireNonNull(other);
        return (T t, U u, P p) -> {
            return test(t, u, p) && other.test(t, u, p);
        };
    }

    default TriPredicate<T, U, P> negate() {
        return (T t, U u, P p) -> {
            return !test(t, u, p);
        };
    }

    default TriPredicate<T, U, P> or(TriPredicate<? super T, ? super U, ? super P> other) {
        Objects.requireNonNull(other);
        return (T t, U u, P p) -> {
            return test(t, u, p) || other.test(t, u, p);
        };
    }
}
