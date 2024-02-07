package zxf.java.functional.core.function;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

@FunctionalInterface
public interface TriPredicate<T, U, P> {
    boolean test(T t, U u, P p);

    default BiPredicate<U, P> apply(final T x) {
        return (y, z) -> test(x, y, z);
    }

    default Predicate<P> apply(final T x, final U y) {
        return (z) -> test(x, y, z);
    }

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
