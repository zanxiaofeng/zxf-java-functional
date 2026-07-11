package zxf.java.functional.core.function.throwing;

import java.util.function.Function;

/**
 * 把「抛受检异常的 lambda」适配成 JDK 标准函数式接口，以便塞进
 * {@code java.util.stream.Stream} 等标准 API。
 *
 * <p><b>checked/ vs throwing/ 的区别（对应 Readme 函数式接口体系）：</b>
 * <ul>
 *   <li>{@code checked/}（如 {@code CheckedFunction}）：只声明 {@code throws Exception}，
 *       把异常处理责任显式留给调用方——更「诚实」，但无法直接用于
 *       {@code Stream.map} 等「不接受受检异常」的标准 API。</li>
 *   <li>{@code throwing/}（本类）：继承 JDK 函数式接口，在 {@code default} 方法里
 *       把受检异常包装成 {@link RuntimeException}（unchecked），从而可以无缝接入
 *       {@code Stream} 等标准 API；代价是丢失编译期强制处理。</li>
 * </ul>
 * 选择哪种，取决于你更想「编译期显式处理」还是「运行时统一兜底」。</p>
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> extends Function<T, R> {
    @Override
    default R apply(T t) {
        try {
            return applyThrows(t);
        } catch (final Exception e) {
            // 不在此处硬编码日志/打印：把原异常作为 cause 重新抛出，由调用方统一兜底处理。
            throw new RuntimeException(
                    "ThrowingFunction 执行失败 [" + e.getClass().getSimpleName() + "]: " + e.getMessage(), e);
        }
    }

    R applyThrows(T t) throws Exception;
}
