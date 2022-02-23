package zxf.java.functional.core.optional;

import zxf.java.functional.core.function.checked.CheckedFunction;

import java.util.function.Function;

public class Optional<T> {
    private T value;

    public Optional() {
    }

    public Optional(T value) {
        this.value = value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public T get() {
        return value;
    }

    //Functor
    public <R> Optional<R> map(Function<T, R> mapper) {
        if (isPresent()) {
            return new Optional<R>(mapper.apply(value));
        }
        return new Optional(null);
    }

    //Functor
    public <R> Optional<R> mapChecked(CheckedFunction<T, R> mapper) throws Exception {
        if (isPresent()) {
            return new Optional<R>(mapper.apply(value));
        }
        return new Optional(null);
    }

    //Monad
    public <R> Optional<R> flatMap(Function<T, Optional<R>> mapper) {
        if (isPresent()) {
            return mapper.apply(value);
        }
        return new Optional(null);
    }

    //Monad
    public <R> Optional<R> flatMapChecked(CheckedFunction<T, Optional<R>> mapper) throws Exception {
        if (isPresent()) {
            return mapper.apply(value);
        }
        return new Optional(null);
    }

    //Applicative
    public <R> Optional<R> apply(Optional<Function<T, R>> applier) {
        if (isPresent() && applier.isPresent()) {
            return this.map(applier.get());
        }
        return new Optional(null);
    }

    //Applicative
    public <R> Optional<R> applyChecked(Optional<CheckedFunction<T, R>> applier) throws Exception {
        if (isPresent() && applier.isPresent()) {
            return new Optional<>(applier.get().apply(value));
        }
        return new Optional(null);
    }
}
