package zxf.java.functional.core.function;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ThrowingBiFunction<T, U, R> extends BiFunction<T, U, R> {
    @Override
    default R apply(T t, U u) {
        try {
            return applyThrows(t, u);
        } catch (final Exception e) {
            // Implement your own exception handling logic here.
            // For example:
            System.out.println("handling an exception...");
            // Or ...
            throw new RuntimeException(e);
        }
    }

    R applyThrows(T t, U u) throws Exception;
}
