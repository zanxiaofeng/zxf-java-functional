package zxf.java.functional.core.function.throwing;

import java.util.function.BiFunction;

/**
 * 把「抛受检异常的 {@code BiFunction}」适配成 JDK {@link BiFunction}。
 *
 * <p>关于 {@code checked/}（声明 throws、显式传播）与 {@code throwing/}
 * （适配为 unchecked、接入标准 API）的区别，详见
 * {@link ThrowingFunction} 的类级 Javadoc。</p>
 */
@FunctionalInterface
public interface ThrowingBiFunction<T, U, R> extends BiFunction<T, U, R> {
    @Override
    default R apply(T t, U u) {
        try {
            return applyThrows(t, u);
        } catch (final Exception e) {
            throw new RuntimeException(
                    "ThrowingBiFunction 执行失败 [" + e.getClass().getSimpleName() + "]: " + e.getMessage(), e);
        }
    }

    R applyThrows(T t, U u) throws Exception;
}
