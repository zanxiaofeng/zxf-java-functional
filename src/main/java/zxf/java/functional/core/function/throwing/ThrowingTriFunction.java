package zxf.java.functional.core.function.throwing;

import zxf.java.functional.core.function.TriFunction;

/**
 * 把「抛受检异常的三元函数」适配成自实现的 {@link TriFunction}。
 *
 * <p>关于 {@code checked/}（声明 throws、显式传播）与 {@code throwing/}
 * （适配为 unchecked、接入标准 API）的区别，详见
 * {@link ThrowingFunction} 的类级 Javadoc。</p>
 */
@FunctionalInterface
public interface ThrowingTriFunction<T, U, P, R> extends TriFunction<T, U, P, R> {
    @Override
    default R apply(T t, U u, P p) {
        try {
            return applyThrows(t, u, p);
        } catch (final Exception e) {
            throw new RuntimeException(
                    "ThrowingTriFunction 执行失败 [" + e.getClass().getSimpleName() + "]: " + e.getMessage(), e);
        }
    }

    R applyThrows(T t, U u, P p) throws Exception;
}
