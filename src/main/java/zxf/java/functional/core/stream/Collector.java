package zxf.java.functional.core.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Collector<T, A, R> {
    Supplier<A> supplier();

    BiConsumer<A, T> accumulator();

    Function<A, R> finisher();

    static <T> Collector<T, ArrayList<T>, List<T>> toList() {
        return new Collector<T, ArrayList<T>, List<T>>() {
            @Override
            public Supplier<ArrayList<T>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<ArrayList<T>, T> accumulator() {
                return ArrayList::add;
            }

            @Override
            public Function<ArrayList<T>, List<T>> finisher() {
                return (list -> list);
            }
        };
    }

    static Collector<String, StringBuilder, String> joining() {
        return new Collector<String, StringBuilder, String>() {
            @Override
            public Supplier<StringBuilder> supplier() {
                return StringBuilder::new;
            }

            @Override
            public BiConsumer<StringBuilder, String> accumulator() {
                return StringBuilder::append;
            }

            @Override
            public Function<StringBuilder, String> finisher() {
                return StringBuilder::toString;
            }
        };
    }
}
