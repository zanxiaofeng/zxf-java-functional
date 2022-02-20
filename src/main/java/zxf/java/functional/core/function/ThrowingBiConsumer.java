package zxf.java.functional.core.function;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ThrowingBiConsumer<T, U> extends BiConsumer<T, U> {
    @Override
    default void accept(T t, U u) {
        try {
            acceptThrows(t, u);
        } catch (final Exception e) {
            // Implement your own exception handling logic here.
            // For example:
            System.out.println("handling an exception...");
            // Or ...
            throw new RuntimeException(e);
        }
    }

    void acceptThrows(T t, U u) throws Exception;
}
