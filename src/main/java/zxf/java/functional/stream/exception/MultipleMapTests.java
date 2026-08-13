package zxf.java.functional.stream.exception;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Stream 多 map 链中的异常处理演示。
 *
 * <p>问题背景：在 {@code list.stream().map(step1).map(step2).map(step3).collect(...)}
 * 的调用链中，任意一个 map 抛出异常 → <b>整个 Stream 管道立即中断</b>：后续元素不被处理，
 * 已处理的部分结果被丢弃，且无法定位是哪个元素 / 哪一步出的错。</p>
 *
 * <p>本类演示四种处理方式的对比，核心是 {@link Outcome}（Either / Try Monad）模式 —
 * 把异常从「中断信号」变为「数据流中的值」，实现错误隔离。
 * 另外也演示了 JDK 原生 {@link java.util.Optional} + flatMap 的轻量方案。</p>
 */
public class MultipleMapTests {

    // ==================== 演示数据 ====================

    /**
     * 输入：字符串列表，经过三步转换：
     * <ol>
     *   <li>parseToInt：String → Integer（"abc" 抛 NumberFormatException）</li>
     *   <li>divide1000：Integer → Double（0 抛 ArithmeticException）</li>
     *   <li>formatResult：Double → String</li>
     * </ol>
     */
    private static final List<String> INPUTS = List.of("100", "0", "abc", "50", "200");

    public static void main(String[] args) {
        use_case1_exception_kills_entire_stream();
        use_case2_try_catch_in_each_map();
        use_case3_outcome_pattern();
        use_case4_partition_success_and_failure();
        use_case5_optional_pattern();
    }

    // ==================== use_case1：展示问题 ====================

    /**
     * 原生 map().map().map() 链：遇到第一个异常即中断整个流。
     *
     * <p>"100" 处理成功 → "0" 处理成功（divide1000 抛异常）→ 异常传播，"abc"/"50"/"200" 不被处理。
     * 注意：实际运行时 "abc" 在 Step1 就会先失败（因为它在 "0" 之后，
     * 但 Stream 是逐元素走完全部管道再处理下一个元素——实际是 "0" 在 Step2 先失败）。</p>
     */
    public static void use_case1_exception_kills_entire_stream() {
        System.out.println("=== use_case1: 原生 map 链 — 一个异常杀死整个流 ===");
        System.out.println("输入: " + INPUTS);
        try {
            List<String> results = INPUTS.stream()
                    .map(MultipleMapTests::parseToInt)      // "abc" → NumberFormatException
                    .map(MultipleMapTests::divide1000)      // 0 → ArithmeticException
                    .map(MultipleMapTests::formatResult)
                    .collect(Collectors.toList());
            System.out.println("结果: " + results);
        } catch (Exception e) {
            System.out.println("异常中断: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            System.out.println("→ 后续元素未被处理，已处理的部分结果也丢失了");
        }
        System.out.println();
    }

    // ==================== use_case2：朴素 try-catch ====================

    /**
     * 朴素方案：每个 map 内部 try-catch，失败时返回 null。
     *
     * <p>所有元素都能跑完，但丢失了错误信息（只知道是 null，不知道失败原因），
     * 且成功结果与 null 混在一起，调用方需要逐个判空。</p>
     */
    public static void use_case2_try_catch_in_each_map() {
        System.out.println("=== use_case2: 朴素 try-catch — 能跑完但丢失错误信息 ===");
        System.out.println("输入: " + INPUTS);
        List<String> results = INPUTS.stream()
                .map(MultipleMapTests::parseToIntSafe)
                .map(MultipleMapTests::divide1000Safe)
                .map(MultipleMapTests::formatResultSafe)
                .collect(Collectors.toList());
        System.out.println("结果: " + results);
        System.out.println("→ 成功与 null 混在一起，无法区分「值为 null」与「处理失败」，且丢失异常原因");
        System.out.println();
    }

    // ==================== use_case3：Outcome 模式 ====================

    /**
     * Outcome 方案：异常变成值在管道中流动，所有元素都处理完。
     *
     * <p>{@code Outcome.lift(step1)} 把第一步 lift 进 Outcome 上下文，
     * 后续每步用 {@code outcome.map(stepN)} —— map 内部自动 catch 异常转为 Failure，
     * Failure 短路跳过后续步骤但<b>不中断 Stream</b>。</p>
     */
    public static void use_case3_outcome_pattern() {
        System.out.println("=== use_case3: Outcome 模式 — 异常变为值，所有元素都处理完 ===");
        System.out.println("输入: " + INPUTS);
        List<Outcome<String>> results = INPUTS.stream()
                .map(Outcome.lift(MultipleMapTests::parseToInt))          // String      → Outcome<Integer>
                .map(outcome -> outcome.map(MultipleMapTests::divide1000)) // Outcome<Integer> → Outcome<Double>
                .map(outcome -> outcome.map(MultipleMapTests::formatResult)) // Outcome<Double>  → Outcome<String>
                .collect(Collectors.toList());
        results.forEach(o -> System.out.println("  " + o));
        System.out.println("→ 每个元素都有明确结果：Success 携带值，Failure 携带异常类型和原因");
        System.out.println();
    }

    // ==================== use_case4：分离成功与失败 ====================

    /**
     * Outcome 进阶：collect 后用 partitioningBy 分离成功结果与失败记录。
     *
     * <p>成功的元素提取值，失败的元素提取异常信息，两个列表各自独立、结构清晰。</p>
     */
    public static void use_case4_partition_success_and_failure() {
        System.out.println("=== use_case4: Outcome 进阶 — 分离成功结果与失败记录 ===");
        System.out.println("输入: " + INPUTS);
        Map<Boolean, List<Outcome<String>>> partitioned = INPUTS.stream()
                .map(Outcome.lift(MultipleMapTests::parseToInt))
                .map(outcome -> outcome.map(MultipleMapTests::divide1000))
                .map(outcome -> outcome.map(MultipleMapTests::formatResult))
                .collect(Collectors.partitioningBy(Outcome::isSuccess));

        List<String> successes = partitioned.get(true).stream()
                .map(o -> o.getOrElse(""))
                .toList();
        List<String> failures = partitioned.get(false).stream()
                .map(o -> {
                    Exception cause = o.getCauseOrNull();
                    return cause.getClass().getSimpleName() + ": " + cause.getMessage();
                })
                .toList();

        System.out.println("成功 (" + successes.size() + "): " + successes);
        System.out.println("失败 (" + failures.size() + "): " + failures);
        System.out.println();
    }

    // ==================== use_case5：Optional 模式 ====================

    /**
     * Optional 方案：用 {@code Optional<T>} 替代裸值，失败时返回 {@code Optional.empty()}。
     *
     * <p>与朴素 try-catch（use_case2）的区别在于 <b>flatMap 短路</b>：一旦某步返回 empty，
     * 后续 flatMap 自动跳过（不再调用函数），比每步手动判 null 更优雅。
     * 与 Outcome（use_case3）的区别在于 <b>不携带错误信息</b>：Optional 只有 present/empty，
     * 无法区分「解析失败」还是「除以零」。</p>
     */
    public static void use_case5_optional_pattern() {
        System.out.println("=== use_case5: Optional + flatMap — 轻量方案，短路但不保留错误信息 ===");
        System.out.println("输入: " + INPUTS);
        List<Optional<String>> results = INPUTS.stream()
                .map(MultipleMapTests::parseToIntOptional)                      // String  → Optional<Integer>
                .map(opt -> opt.flatMap(MultipleMapTests::divide1000Optional))  //          → Optional<Double>
                .map(opt -> opt.flatMap(MultipleMapTests::formatResultOptional))//          → Optional<String>
                .collect(Collectors.toList());
        results.forEach(o -> System.out.println("  " + (o.isPresent()
                ? "Present(" + o.get() + ")"
                : "Empty")));
        System.out.println("→ flatMap 短路：empty 后自动跳过后续步骤；但无法区分失败原因");
        System.out.println();
    }

    // ==================== 三步转换函数（会抛异常的原版） ====================

    /** Step 1: String → Integer */
    private static Integer parseToInt(String input) {
        return Integer.parseInt(input);
    }

    /** Step 2: Integer → Double（1000 / input，0 抛 ArithmeticException） */
    private static Double divide1000(Integer input) {
        if (input == 0) {
            throw new ArithmeticException("/ by zero");
        }
        return 1000.0 / input;
    }

    /** Step 3: Double → String */
    private static String formatResult(Double input) {
        return String.format("result = %.2f", input);
    }

    // ==================== 朴素 try-catch 版本（use_case2 用） ====================

    private static Integer parseToIntSafe(String input) {
        try {
            return Integer.parseInt(input);
        } catch (Exception e) {
            return null;
        }
    }

    private static Double divide1000Safe(Integer input) {
        if (input == null || input == 0) {
            return null;
        }
        return 1000.0 / input;
    }

    private static String formatResultSafe(Double input) {
        if (input == null) {
            return null;
        }
        return String.format("result = %.2f", input);
    }

    // ==================== Optional 版本（use_case5 用） ====================

    private static Optional<Integer> parseToIntOptional(String input) {
        try {
            return Optional.of(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private static Optional<Double> divide1000Optional(Integer input) {
        if (input == 0) {
            return Optional.empty();
        }
        return Optional.of(1000.0 / input);
    }

    private static Optional<String> formatResultOptional(Double input) {
        return Optional.of(String.format("result = %.2f", input));
    }
}
