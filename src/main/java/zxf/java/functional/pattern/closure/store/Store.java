package zxf.java.functional.pattern.closure.store;

import zxf.java.functional.core.function.TriFunction;

import java.util.HashMap;
import java.util.Map;

public class Store {
    public static <T, U> TriFunction<String, T, U, U> store() {
        Map<T, U> store = new HashMap<>();
        PutCommand putCommand = new PutCommand(store);
        GetCommand getCommand = new GetCommand(store);
        return (action, t, u) -> {
            if (action.equalsIgnoreCase("get")) {

                return store.getOrDefault(t, u);
            }
            return store.put(t, u);
        };
    }

    public static <T, U> TriFunction<String, T, U, U> storeByCmd() {
        Map<T, U> store = new HashMap<>();
        PutCommand<T, U> putCommand = new PutCommand<>(store);
        GetCommand<T, U> getCommand = new GetCommand<>(store);
        return (action, t, u) -> {
            if (action.equalsIgnoreCase("get")) {
                return getCommand.execute(t, u);
            }
            return putCommand.execute(t, u);
        };
    }

    public static class PutCommand<T, U> {
        private Map<T, U> store;

        private PutCommand(Map<T, U> store) {
            this.store = store;
        }

        public U execute(T t, U u) {
            return store.put(t, u);
        }
    }

    public static class GetCommand<T, U> {
        private Map<T, U> store;

        private GetCommand(Map<T, U> store) {
            this.store = store;
        }

        public U execute(T t, U u) {
            return store.getOrDefault(t, u);
        }
    }
}
