package zxf.java.functional.core;

import zxf.java.functional.core.function.*;
import zxf.java.functional.core.optional.Optional;


import java.util.function.*;

public class Currying {
    public static <T, U> CheckedConsumer<U> curryingConsumer(T t, CheckedBiConsumer<T, U> consumer) {
        return (u) -> consumer.accept(t, u);
    }

    public static <T, U, P> CheckedBiConsumer<U, P> curryingConsumer(T t, CheckedTriConsumer<T, U, P> consumer) {
        return (u, p) -> consumer.accept(t, u, p);
    }

    public static <T, U, P> CheckedConsumer<P> curryingConsumer(T t, U u, CheckedTriConsumer<T, U, P> consumer) {
        return (p) -> consumer.accept(t, u, p);
    }

    public static <T, U, R> CheckedFunction<U, R> curryingFunction(T t, CheckedBiFunction<T, U, R> function) {
        return (u) -> {
            return function.apply(t, u);
        };
    }

    public static <T, U, P, R> CheckedBiFunction<U, P, R> curryingFunction(T t, CheckedTriFunction<T, U, P, R> function) {
        return (u, p) -> {
            return function.apply(t, u, p);
        };
    }

    public static <T, U, P, R> CheckedFunction<P, R> curryingFunction(T t, U u, CheckedTriFunction<T, U, P, R> function) {
        return (p) -> {
            return function.apply(t, u, p);
        };
    }

    public static <T, U> Predicate<U> curryingPredicate(T t, BiPredicate<T, U> predicate) {
        return (u) -> {
            return predicate.test(t, u);
        };
    }

    public static <T, U, P> BiPredicate<U, P> curryingPredicate(T t, TriPredicate<T, U, P> predicate) {
        return (u, p) -> {
            return predicate.test(t, u, p);
        };
    }

    public static <T, U, P> Predicate<P> curryingPredicate(T t, U u, TriPredicate<T, U, P> predicate) {
        return (p) -> {
            return predicate.test(t, u, p);
        };
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

    public static class BiCurryingFunction<T, U, R> {
        private Function<T, CheckedFunction<U, R>> curried;

        public BiCurryingFunction(Function<T, CheckedFunction<U, R>> curried) {
            this.curried = curried;
        }

        public CheckedFunction<U, R> apply(T t) {
            return curried.apply(t);
        }

        public Optional<CheckedFunction<U, R>> apply(Optional<T> t) {
            if (t.isPresent()) {
                return new Optional<>(apply(t.get()));
            }
            return new Optional();
        }
    }

    public static class TriCurryingFunction<T, U, P, R> {
        private Function<T, Function<U, CheckedFunction<P, R>>> curried;

        public TriCurryingFunction(Function<T, Function<U, CheckedFunction<P, R>>> curried) {
            this.curried = curried;
        }

        public BiCurryingFunction<U, P, R> apply(T t) {
            return new BiCurryingFunction(curried.apply(t));
        }

        public Optional<BiCurryingFunction<U, P, R>> apply(Optional<T> t) {
            if (t.isPresent()) {
                return new Optional<>(apply(t.get()));
            }
            return new Optional();
        }
    }

    public interface Common {
        static <T, U> BiCurryingConsumer<T, U> curryingConsumer(CheckedBiConsumer<T, U> consumer) {
            return new BiCurryingConsumer<>((t) -> {
                return (u) -> {
                    consumer.accept(t, u);
                };
            });
        }

        static <T, U, P> TriCurryingConsumer<T, U, P> curryingConsumer(CheckedTriConsumer<T, U, P> consumer) {
            return new TriCurryingConsumer<>((t) -> {
                return (u) -> {
                    return (p) -> {
                        consumer.accept(t, u, p);
                    };
                };
            });
        }

        static <T, U, R> BiCurryingFunction<T, U, R> curryingFunction(CheckedBiFunction<T, U, R> function) {
            return new BiCurryingFunction<>((t) -> {
                return (u) -> {
                    return function.apply(t, u);
                };
            });
        }

        static <T, U, P, R> TriCurryingFunction<T, U, P, R> curryingFunction(CheckedTriFunction<T, U, P, R> function) {
            Function<T, Function<U, CheckedFunction<P, R>>> curried = (t) -> {
                return (u) -> {
                    return (p) -> {
                        return function.apply(t, u, p);
                    };
                };
            };
            return new TriCurryingFunction<>(curried);
        }
    }
}
