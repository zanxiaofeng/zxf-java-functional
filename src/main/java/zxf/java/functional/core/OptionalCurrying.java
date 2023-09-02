package zxf.java.functional.core;

import zxf.java.functional.core.function.checked.*;
import zxf.java.functional.core.optional.Optional;

import java.util.function.Function;

public class OptionalCurrying {
    public static <T, U, P> TriCurryingConsumer<T, U, P> curryingConsumer(CheckedTriConsumer<T, U, P> consumer) {
        return new TriCurryingConsumer<>((t) -> {
            return (u) -> {
                return (p) -> {
                    consumer.accept(t, u, p);
                };
            };
        });
    }

    public static <T, U> BiCurryingConsumer<T, U> curryingConsumer(CheckedBiConsumer<T, U> consumer) {
        return new BiCurryingConsumer<>((t) -> {
            return (u) -> {
                consumer.accept(t, u);
            };
        });
    }

    public static <T, U, P, R> TriCurryingFunction<T, U, P, R> curryingFunction(CheckedTriFunction<T, U, P, R> function) {
        Function<T, Function<U, CheckedFunction<P, R>>> curried = (t) -> {
            return (u) -> {
                return (p) -> {
                    return function.apply(t, u, p);
                };
            };
        };
        return new TriCurryingFunction<>(curried);
    }

    public static <T, U, R> BiCurryingFunction<T, U, R> curryingFunction(CheckedBiFunction<T, U, R> function) {
        return new BiCurryingFunction<>((t) -> {
            return (u) -> {
                return function.apply(t, u);
            };
        });
    }

    public static class TriCurryingConsumer<T, U, P> {
        private Function<T, Function<U, CheckedConsumer<P>>> curried;

        public TriCurryingConsumer(Function<T, Function<U, CheckedConsumer<P>>> curried) {
            this.curried = curried;
        }

        public BiCurryingConsumer<U, P> apply(T t) {
            return new BiCurryingConsumer(curried.apply(t));
        }

        public Optional<BiCurryingConsumer<U, P>> apply(Optional<T> t) {
            if (t.isPresent()) {
                return new Optional<>(apply(t.get()));
            }
            return new Optional();
        }
    }

    public static class BiCurryingConsumer<T, U> {
        private Function<T, CheckedConsumer<U>> curried;

        public BiCurryingConsumer(Function<T, CheckedConsumer<U>> curried) {
            this.curried = curried;
        }

        public CheckedConsumer<U> apply(T t) {
            return curried.apply(t);
        }

        public Optional<CheckedConsumer<U>> apply(Optional<T> t) {
            if (t.isPresent()) {
                return new Optional<>(apply(t.get()));
            }
            return new Optional();
        }
    }

    public static class TriCurryingFunction<T, U, P, R> {
        private Optional<Function<T, Function<U, CheckedFunction<P, R>>>> curried;

        public TriCurryingFunction(Function<T, Function<U, CheckedFunction<P, R>>> curried) {
            this.curried = new Optional<>(curried);
        }

        public BiCurryingFunction<U, P, R> apply(T t) {
            return apply(new Optional<>(t));
        }

        public BiCurryingFunction<U, P, R> apply(Optional<T> t) {
            if (curried.isPresent() && t.isPresent()) {
                return new BiCurryingFunction<>(curried.get().apply(t.get()));
            }
            return new BiCurryingFunction<>(null);
        }
    }

    public static class BiCurryingFunction<T, U, R> {
        private Optional<Function<T, CheckedFunction<U, R>>> curried;

        public BiCurryingFunction(Function<T, CheckedFunction<U, R>> curried) {
            this.curried = new Optional<>(curried);
        }

        public CurryingFunction<U, R> apply(T t) {
            return apply(new Optional<>(t));
        }

        public CurryingFunction<U, R> apply(Optional<T> t) {
            if (curried.isPresent() && t.isPresent()) {
                return new CurryingFunction<>(curried.get().apply(t.get()));
            }
            return new CurryingFunction<>(null);
        }
    }

    public static class CurryingFunction<U, R> {
        private Optional<CheckedFunction<U, R>> curried;

        public CurryingFunction(CheckedFunction<U, R> curried) {
            this.curried = new Optional<>(curried);
        }

        public Optional<R> apply(U u) throws Exception {
            return apply(new Optional<U>(u));
        }

        public Optional<R> apply(Optional<U> u) throws Exception {
            if (curried.isPresent() && u.isPresent()) {
                return new Optional<>(curried.get().apply(u.get()));
            }
            return new Optional();
        }
    }
}
