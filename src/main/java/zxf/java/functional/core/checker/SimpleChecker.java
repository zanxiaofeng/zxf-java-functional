package zxf.java.functional.core.checker;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class SimpleChecker<T> {
    private final Predicate<T> check;
    private final T checkObject;

    protected SimpleChecker(Predicate<T> check, T checkObject) {
        this.check = check;
        this.checkObject = checkObject;
    }

    public void ifTrue(Consumer<T> trueAction){
        if (check.test(checkObject)){
            trueAction.accept(checkObject);
        }
    }

    public void ifFalse(Consumer<T> falseAction){
        if (!check.test(checkObject)){
            falseAction.accept(checkObject);
        }
    }

    public void ifTrueOrFalse(Consumer<T> trueAction, Consumer<T> falseAction){
        if (check.test(checkObject)){
            trueAction.accept(checkObject);
            return;
        }
        falseAction.accept(checkObject);
    }
}
