package zxf.java.functional.core.list;

import zxf.java.functional.core.function.checked.CheckedFunction;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;

public class List<T> {
    private ArrayList<T> contents;

    @SafeVarargs
    public List(T... contents) {
        Objects.requireNonNull(contents);
        this.contents = new ArrayList<>();
        for (T content : contents) {
            this.contents.add(content);
        }
    }

    /**
     * 用已有 {@link ArrayList} 构造 List。
     * 做防御性拷贝，避免外部后续修改传入集合导致内部状态被意外变更
     * （符合不可变/隔离原则）。
     */
    public List(ArrayList<T> contents) {
        Objects.requireNonNull(contents);
        this.contents = new ArrayList<>(contents);
    }

    /**
     * 返回内容的不可变拷贝（{@code List.copyOf}，拒绝 null 元素），
     * 防止外部通过返回值修改内部状态——与构造器的防御性拷贝共同保证不可变/隔离。
     */
    public java.util.List<T> getContents() {
        return java.util.List.copyOf(contents);
    }

    //Functor
    public <R> List<R> map(Function<T, R> mapper) {
        ArrayList<R> result = new ArrayList<R>();
        for (int i = 0; i < this.contents.size(); i++) {
            result.add(mapper.apply(this.contents.get(i)));
        }
        return new List<R>(result);
    }

    //Functor
    public <R> List<R> mapChecked(CheckedFunction<T, R> mapper) throws Exception {
        ArrayList<R> result = new ArrayList<R>();
        for (int i = 0; i < this.contents.size(); i++) {
            result.add(mapper.apply(this.contents.get(i)));
        }
        return new List<R>(result);
    }

    //Monad
    public <R> List<R> flatMap(Function<T, List<R>> mapper) {
        ArrayList<R> result = new ArrayList<R>();
        for (int i = 0; i < this.contents.size(); i++) {
            result.addAll(mapper.apply(this.contents.get(i)).contents);
        }
        return new List<>(result);
    }

    //Monad
    public <R> List<R> flatMapChecked(CheckedFunction<T, List<R>> mapper) throws Exception {
        ArrayList<R> result = new ArrayList<R>();
        for (int i = 0; i < this.contents.size(); i++) {
            result.addAll(mapper.apply(this.contents.get(i)).contents);
        }
        return new List<>(result);
    }

    //Applicative
    public <R> List<R> apply(List<Function<T, R>> appliers) {
        ArrayList<R> result = new ArrayList<>();
        for (int i = 0; i < this.contents.size(); i++) {
            for (Function<T, R> applier : appliers.getContents()) {
                result.add(applier.apply(this.contents.get(i)));
            }
        }
        return new List<>(result);
    }

    //Applicative
    public <R> List<R> applyChecked(List<CheckedFunction<T, R>> appliers) throws Exception {
        ArrayList<R> result = new ArrayList<>();
        for (int i = 0; i < this.contents.size(); i++) {
            for (CheckedFunction<T, R> applier : appliers.getContents()) {
                result.add(applier.apply(this.contents.get(i)));
            }
        }
        return new List<>(result);
    }
}
