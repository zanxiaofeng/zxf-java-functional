package zxf.java.functional.core.function;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {
    @Override
    default void accept(T t) {
        try {
            acceptThrows(t);
        } catch (final Exception e) {
            // Implement your own exception handling logic here.
            // For example:
            System.out.println("handling an exception...");
            // Or ...
            throw new RuntimeException(e);
        }
    }

    void acceptThrows(T t) throws Exception;
}
