package zxf.java.functional.core.function;

import java.util.function.Function;

@FunctionalInterface
public interface ThrowingFunction<T, R> extends Function<T, R> {
    @Override
    default R apply(T t) {
        try {
            return applyThrows(t);
        } catch (final Exception e) {
            // Implement your own exception handling logic here.
            // For example:
            System.out.println("handling an exception...");
            // Or ...
            throw new RuntimeException(e);
        }
    }

    R applyThrows(T t) throws Exception;
}
