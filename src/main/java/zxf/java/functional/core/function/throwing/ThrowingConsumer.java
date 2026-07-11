package zxf.java.functional.core.function.throwing;

import java.util.function.Consumer;

/**
 * 把「抛受检异常的 {@code Consumer}」适配成 JDK {@link Consumer}，以便接入
 * {@code java.util.stream.Stream.forEach} 等标准 API。
 *
 * <p>关于 {@code checked/}（声明 throws、显式传播）与 {@code throwing/}
 * （适配为 unchecked、接入标准 API）的区别，详见
 * {@link ThrowingFunction} 的类级 Javadoc。</p>
 */
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {
    @Override
    default void accept(T t) {
        try {
            acceptThrows(t);
        } catch (final Exception e) {
            // 不硬编码日志/打印：把原异常作为 cause 重新抛出，交给调用方兜底。
            throw new RuntimeException(
                    "ThrowingConsumer 执行失败 [" + e.getClass().getSimpleName() + "]: " + e.getMessage(), e);
        }
    }

    void acceptThrows(T t) throws Exception;
}
