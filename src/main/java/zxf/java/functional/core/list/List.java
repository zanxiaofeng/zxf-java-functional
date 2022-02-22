package zxf.java.functional.core.list;

import zxf.java.functional.core.function.CheckedFunction;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;

public class List<T> {
    private ArrayList<T> contents;

    public List(T... contents) {
        Objects.requireNonNull(contents);
        this.contents = new ArrayList<>();
        for (T content : contents) {
            this.contents.add(content);
        }
    }

    public List(ArrayList<T> contents) {
        Objects.requireNonNull(contents);
        this.contents = contents;
    }

    public ArrayList<T> getContents() {
        return contents;
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
