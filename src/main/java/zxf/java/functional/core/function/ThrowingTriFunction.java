package zxf.java.functional.core.function;

@FunctionalInterface
public interface ThrowingTriFunction<T, U, P, R> extends TriFunction<T, U, P, R> {
    @Override
    default R apply(T t, U u, P p) {
        try {
            return applyThrows(t, u, p);
        } catch (final Exception e) {
            // Implement your own exception handling logic here.
            // For example:
            System.out.println("handling an exception...");
            // Or ...
            throw new RuntimeException(e);
        }
    }

    R applyThrows(T t, U u, P p) throws Exception;
}
