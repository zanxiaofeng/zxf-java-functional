package zxf.java.functional.core.function;

@FunctionalInterface
public interface ThrowingTriConsumer<T, U, P> extends TriConsumer<T, U, P> {
    @Override
    default void accept(T t, U u, P p) {
        try {
            acceptThrows(t, u, p);
        } catch (final Exception e) {
            // Implement your own exception handling logic here.
            // For example:
            System.out.println("handling an exception...");
            // Or ...
            throw new RuntimeException(e);
        }
    }

    void acceptThrows(T t, U u, P p) throws Exception;
}
