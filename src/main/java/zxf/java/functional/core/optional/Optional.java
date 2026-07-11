package zxf.java.functional.core.optional;

import zxf.java.functional.core.function.checked.CheckedFunction;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 自实现的 Optional，对应 Readme 中「Optional / 幽灵类型」概念。
 *
 * <p>这是一个容器：要么装着一个非 null 的值，要么为空（empty）。
 * 与 JDK {@code java.util.Optional} 语义一致：empty 的 {@link #get()}
 * 抛 {@link NoSuchElementException}，{@code of} 拒绝 null，
 * {@code ofNullable} 同时接受 null 与非 null。</p>
 *
 * <p>不可变：{@code value} 为 final，所有变换都返回新实例。</p>
 */
public class Optional<T> {
    /** 持有值；null 表示 empty。final 保证不可变。 */
    private final T value;

    /** 构造一个 empty 实例（value 为 null）。保留以兼容旧调用方。 */
    public Optional() {
        this.value = null;
    }

    /** 直接持有传入值（可为 null，等价于 ofNullable）。保留以兼容旧调用方。 */
    public Optional(T value) {
        this.value = value;
    }

    // ============ 工厂方法（推荐使用，与 JDK 一致） ============

    /** 返回一个 empty 实例。 */
    public static <T> Optional<T> empty() {
        return new Optional<T>(null);
    }

    /** 构造持有非 null 值的实例；传入 null 抛 {@link NullPointerException}，与 JDK 一致。 */
    public static <T> Optional<T> of(T value) {
        Objects.requireNonNull(value, "Optional.of 不接受 null，如需允许 null 请用 ofNullable");
        return new Optional<T>(value);
    }

    /** 值非 null 则包装，为 null 则返回 empty。 */
    public static <T> Optional<T> ofNullable(T value) {
        return new Optional<T>(value);
    }

    // ============ 查询与取值 ============

    public boolean isPresent() {
        return value != null;
    }

    /**
     * 取出值；empty 时抛 {@link NoSuchElementException}（与 JDK 一致，
     * 不再返回 null，以避免「空指针被静默传播」的反模式）。
     */
    public T get() {
        if (!isPresent()) {
            throw new NoSuchElementException("Optional 为空，没有值可取。请先用 isPresent() 判空或用 orElse/orElseGet 提供默认值。");
        }
        return value;
    }

    /** 值存在且满足 predicate 时返回当前实例，否则返回 empty。对应 Readme 中 filter。 */
    public Optional<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate 不能为空");
        if (isPresent() && predicate.test(value)) {
            return this;
        }
        return empty();
    }

    /** 值存在则返回，否则返回 other。对应 Readme 中 orElse。 */
    public T orElse(T other) {
        return isPresent() ? value : other;
    }

    /** 值存在则返回，否则调用 supplier 取默认值（惰性求值）。对应 Readme 中 orElseGet。 */
    public T orElseGet(Supplier<T> supplier) {
        Objects.requireNonNull(supplier, "supplier 不能为空");
        return isPresent() ? value : supplier.get();
    }

    /** 值存在则对其执行 action。对应 Readme 中 ifPresent。 */
    public void ifPresent(Consumer<T> action) {
        Objects.requireNonNull(action, "action 不能为空");
        if (isPresent()) {
            action.accept(value);
        }
    }

    /** 值存在返回当前实例，否则返回 supplier 产出的 Optional。对应 JDK 9+ 中 or。 */
    public Optional<T> or(Supplier<Optional<T>> supplier) {
        Objects.requireNonNull(supplier, "supplier 不能为空");
        if (isPresent()) {
            return this;
        }
        return supplier.get();
    }

    // ============ Functor / Monad / Applicative（函数式变换） ============

    //Functor
    public <R> Optional<R> map(Function<T, R> mapper) {
        Objects.requireNonNull(mapper, "mapper 不能为空");
        if (isPresent()) {
            return new Optional<R>(mapper.apply(value));
        }
        return empty();
    }

    //Functor
    public <R> Optional<R> mapChecked(CheckedFunction<T, R> mapper) throws Exception {
        Objects.requireNonNull(mapper, "mapper 不能为空");
        if (isPresent()) {
            return new Optional<R>(mapper.apply(value));
        }
        return empty();
    }

    //Monad
    public <R> Optional<R> flatMap(Function<T, Optional<R>> mapper) {
        Objects.requireNonNull(mapper, "mapper 不能为空");
        if (isPresent()) {
            return Objects.requireNonNull(mapper.apply(value), "flatMap 的 mapper 不能返回 null Optional");
        }
        return empty();
    }

    //Monad
    public <R> Optional<R> flatMapChecked(CheckedFunction<T, Optional<R>> mapper) throws Exception {
        Objects.requireNonNull(mapper, "mapper 不能为空");
        if (isPresent()) {
            return mapper.apply(value);
        }
        return empty();
    }

    //Applicative
    public <R> Optional<R> apply(Optional<Function<T, R>> applier) {
        Objects.requireNonNull(applier, "applier 不能为空");
        if (isPresent() && applier.isPresent()) {
            return this.map(applier.get());
        }
        return empty();
    }

    //Applicative
    public <R> Optional<R> applyChecked(Optional<CheckedFunction<T, R>> applier) throws Exception {
        Objects.requireNonNull(applier, "applier 不能为空");
        if (isPresent() && applier.isPresent()) {
            return new Optional<>(applier.get().apply(value));
        }
        return empty();
    }

    @Override
    public String toString() {
        return isPresent() ? "Optional[" + value + "]" : "Optional.empty";
    }
}
