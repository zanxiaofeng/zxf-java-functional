package zxf.java.functional.core.function.throwing;

import zxf.java.functional.core.function.TriConsumer;

/**
 * 把「抛受检异常的三元消费者」适配成自实现的 {@link TriConsumer}。
 *
 * <p>关于 {@code checked/}（声明 throws、显式传播）与 {@code throwing/}
 * （适配为 unchecked、接入标准 API）的区别，详见
 * {@link ThrowingFunction} 的类级 Javadoc。</p>
 */
@FunctionalInterface
public interface ThrowingTriConsumer<T, U, P> extends TriConsumer<T, U, P> {
    @Override
    default void accept(T t, U u, P p) {
        try {
            acceptThrows(t, u, p);
        } catch (final Exception e) {
            throw new RuntimeException(
                    "ThrowingTriConsumer 执行失败 [" + e.getClass().getSimpleName() + "]: " + e.getMessage(), e);
        }
    }

    void acceptThrows(T t, U u, P p) throws Exception;
}
