package zxf.java.functional.core.function.throwing;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * throwing/ 演示：把抛受检异常的 lambda 通过 {@link ThrowingFunction} 等适配器
 * 包装成 JDK 标准函数式接口，从而直接塞进 {@code java.util.stream.Stream} 的
 * map / forEach 等需要 {@code Function}/{@code Consumer} 的标准 API。
 *
 * <p><b>使用场景（对应 Readme 函数式接口体系）：</b>
 * 当业务 lambda 必须声明 {@code throws}（如读文件、解析、反射）时，
 * JDK {@code Stream.map} 等接口的方法签名「不接受受检异常」，编译会失败。
 * throwing/ 系列接口在 {@code default} 方法中把受检异常包装成 {@link RuntimeException}，
 * 绕开编译期检查，让流式调用得以连续书写。</p>
 *
 * <p><b>对比 checked/：</b>{@code checked/} 系列（如 {@code CheckedFunction}）
 * 只声明 {@code throws Exception}、把异常显式留给调用方处理——更「诚实」但无法
 * 直接接入 {@code Stream}；{@code throwing/} 牺牲编译期强制处理换取 API 兼容性。</p>
 */
public class ThrowingCases {
    public static void main(String[] args) {
        use_case1_adapt_to_jdk_stream_map();
        use_case2_adapt_consumer_to_forEach();
        use_case3_exception_propagated_as_runtime();
    }

    // use_case1：抛 IOException 的 lambda 经 ThrowingFunction 适配，接入 java.util.stream.Stream.map
    public static void use_case1_adapt_to_jdk_stream_map() {
        System.out.println("use_case1 用 ThrowingFunction 把受检异常 lambda 接入 JDK Stream.map");
        List<String> paths = Arrays.asList("/etc/hostname", "/etc/issue");
        // readAll 抛 IOException（受检）；ThrowingFunction 把它适配成 java.util.function.Function，
        // 于是可以直接传给 JDK Stream.map。受检异常被包装成 RuntimeException 在运行时抛出。
        List<String> firstLines = paths.stream()
                .map((ThrowingFunction<String, String>) ThrowingCases::readFirstLine)
                .collect(Collectors.toList());
        firstLines.forEach(line -> System.out.println("  首行长度 = " + line.length()));
    }

    // use_case2：ThrowingConsumer 适配 java.util.stream.Stream.forEach
    public static void use_case2_adapt_consumer_to_forEach() {
        System.out.println("use_case2 用 ThrowingConsumer 接入 JDK Stream.forEach");
        List<String> paths = Arrays.asList("/etc/hostname");
        paths.stream()
                .forEach((ThrowingConsumer<String>) p -> {
                    String line = readFirstLine(p);
                    System.out.println("  读到: " + line);
                });
    }

    // use_case3：内部抛出的受检异常最终以 RuntimeException 形式向上传播，可被统一兜底
    public static void use_case3_exception_propagated_as_runtime() {
        System.out.println("use_case3 异常以 RuntimeException 形式传播");
        List<String> paths = Arrays.asList("/a/path/that/does/not/exist");
        try {
            paths.stream()
                    .map((ThrowingFunction<String, String>) ThrowingCases::readFirstLine)
                    .collect(Collectors.toList());
            System.out.println("ERROR: 应当抛异常");
        } catch (RuntimeException e) {
            // 原始受检异常作为 cause 保留，可在此统一兜底
            System.out.println("正确捕获 RuntimeException, 原因 = " + e.getCause().getClass().getSimpleName());
        }
    }

    /** 读取文件首行；抛出受检 IOException。 */
    private static String readFirstLine(String path) throws java.io.IOException {
        return Files.readString(Path.of(path)).trim();
    }
}
