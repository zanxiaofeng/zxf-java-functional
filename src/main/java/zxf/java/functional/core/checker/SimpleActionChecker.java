package zxf.java.functional.core.checker;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class SimpleActionChecker<T> {
    private final Predicate<T> check;
    private final Consumer<T> trueAction;
    private final Consumer<T> falseAction;

    public SimpleActionChecker(Predicate<T> check, Consumer<T> trueAction, Consumer<T> falseAction) {
        this.check = check;
        this.trueAction = trueAction;
        this.falseAction = falseAction;
    }

    public void checkAndAction(T checkObject) {
        if (check.test(checkObject)) {
            if (trueAction != null) {
                trueAction.accept(checkObject);
            }
            return;
        }
        if (falseAction != null) {
            falseAction.accept(checkObject);
        }
    }
}
