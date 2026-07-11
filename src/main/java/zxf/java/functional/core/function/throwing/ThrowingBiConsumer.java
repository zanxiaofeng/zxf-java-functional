package zxf.java.functional.core.function.throwing;

import java.util.function.BiConsumer;

/**
 * 把「抛受检异常的 {@code BiConsumer}」适配成 JDK {@link BiConsumer}。
 *
 * <p>关于 {@code checked/}（声明 throws、显式传播）与 {@code throwing/}
 * （适配为 unchecked、接入标准 API）的区别，详见
 * {@link ThrowingFunction} 的类级 Javadoc。</p>
 */
@FunctionalInterface
public interface ThrowingBiConsumer<T, U> extends BiConsumer<T, U> {
    @Override
    default void accept(T t, U u) {
        try {
            acceptThrows(t, u);
        } catch (final Exception e) {
            throw new RuntimeException(
                    "ThrowingBiConsumer 执行失败 [" + e.getClass().getSimpleName() + "]: " + e.getMessage(), e);
        }
    }

    void acceptThrows(T t, U u) throws Exception;
}
