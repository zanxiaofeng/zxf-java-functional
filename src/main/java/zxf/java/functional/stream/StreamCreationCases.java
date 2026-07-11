package zxf.java.functional.stream;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

// Stream 创建方式全集：演示 Stream API 提供的各种创建入口
public class StreamCreationCases {
    private static Pattern splitter = Pattern.compile("[\\s,.;:\\)\\(’]+");

    public static void main(String[] args) throws IOException, URISyntaxException {
        use_case1_stream_of();
        use_case2_arrays_stream();
        use_case3_collection_stream();
        use_case4_stream_generate();
        use_case5_stream_iterate();
        use_case6_stream_empty();
        use_case7_stream_concat();
        use_case8_stream_builder();
        use_case9_pattern_split_as_stream();
        use_case10_files_lines();
    }

    // Stream.of：从显式列出的元素创建有限流
    public static void use_case1_stream_of() {
        System.out.println("=== use_case1: Stream.of 从固定元素创建流 ===");
        Stream<String> stream = Stream.of("Java", "Kotlin", "Scala");
        stream.forEach(s -> System.out.println("元素: " + s));
    }

    // Arrays.stream：从数组创建流，支持指定区间 [start, end)
    public static void use_case2_arrays_stream() {
        System.out.println("\n=== use_case2: Arrays.stream 从数组创建流 ===");
        int[] numbers = {10, 20, 30, 40, 50};
        int sum = Arrays.stream(numbers).sum();
        System.out.println("数组总和: " + sum);
        // 区间 [1, 4) -> 20, 30, 40
        System.out.println("区间 [1,4) 元素:");
        Arrays.stream(numbers, 1, 4).forEach(n -> System.out.println("  " + n));
    }

    // Collection.stream()：从任意集合（List/Set 等）创建串行流
    public static void use_case3_collection_stream() {
        System.out.println("\n=== use_case3: Collection.stream() 从集合创建流 ===");
        List<String> list = List.of("A", "B", "C");
        list.stream().forEach(s -> System.out.println("元素: " + s));
    }

    // Stream.generate：基于 Supplier 无限生成，必须配合 limit 截断
    public static void use_case4_stream_generate() {
        System.out.println("\n=== use_case4: Stream.generate 无限生成（需 limit 限制）===");
        Stream.generate(() -> "Hi")
                .limit(3)
                .forEach(s -> System.out.println("生成: " + s));
    }

    // Stream.iterate：基于种子和一元函数迭代生成，无限流需 limit 截断
    public static void use_case5_stream_iterate() {
        System.out.println("\n=== use_case5: Stream.iterate 迭代生成 ===");
        // 经典双参 iterate：seed=1，每次 +2
        Stream.iterate(1, n -> n + 2)
                .limit(5)
                .forEach(n -> System.out.println("奇数: " + n));
        // JDK 9 三参 iterate：带终止条件 hasNext
        System.out.println("三参 iterate (n < 10):");
        Stream.iterate(1, n -> n < 10, n -> n + 2)
                .forEach(n -> System.out.println("  " + n));
    }

    // Stream.empty：创建一个空的、不产生任何元素的流
    public static void use_case6_stream_empty() {
        System.out.println("\n=== use_case6: Stream.empty 创建空流 ===");
        long count = Stream.<String>empty().count();
        System.out.println("空流元素个数: " + count);
    }

    // Stream.concat：将两个流首尾连接成一个新流
    public static void use_case7_stream_concat() {
        System.out.println("\n=== use_case7: Stream.concat 连接两个流 ===");
        Stream<String> first = Stream.of("A", "B");
        Stream<String> second = Stream.of("C", "D");
        Stream.concat(first, second)
                .forEach(s -> System.out.println("合并后: " + s));
    }

    // Stream.builder：使用 builder 模式按需 append 元素再 build 成流
    public static void use_case8_stream_builder() {
        System.out.println("\n=== use_case8: Stream.builder 构建器模式 ===");
        Stream.Builder<String> builder = Stream.builder();
        builder.add("X");
        builder.add("Y");
        builder.add("Z");
        builder.build().forEach(s -> System.out.println("builder 元素: " + s));
    }

    // Pattern.splitAsStream：将正则匹配切分结果作为流（惰性处理大文本）
    public static void use_case9_pattern_split_as_stream() {
        System.out.println("\n=== use_case9: Pattern.splitAsStream 正则切分流 ===");
        splitter.splitAsStream("Java, Kotlin; Scala (Groovy)")
                .filter(s -> !s.isBlank())
                .forEach(s -> System.out.println("切分词: " + s));
    }

    // Files.lines：按行读取文件为流，必须 try-with-resources 关闭底层文件资源
    public static void use_case10_files_lines() throws IOException, URISyntaxException {
        System.out.println("\n=== use_case10: Files.lines 按行读取文件（try-with-resources）===");
        Path path = Paths.get(StreamCreationCases.class.getResource("/files/article.txt").toURI());
        // Files.lines 打开了操作系统文件句柄，必须用 try-with-resources 确保关闭
        try (Stream<String> lines = Files.lines(path)) {
            long lineCount = lines.count();
            System.out.println("文件总行数: " + lineCount);
        }
    }
}
