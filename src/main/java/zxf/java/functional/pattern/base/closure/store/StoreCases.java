package zxf.java.functional.pattern.base.closure.store;

import zxf.java.functional.pattern.base.TriFunction;

import java.util.HashMap;
import java.util.Map;

public class StoreCases {
    public static void main(String[] args) {
        store_case();
    }

    public static void store_case(){
        TriFunction<String, String, Integer, Integer> store = store();
        Integer v1 = store.apply("put", "a", 123);
        Integer v2 = store.apply("put", "b", 234);
        Integer v3 = store.apply("put", "b", 432);

        Integer v4 = store.apply("get", "a", 888);
        Integer v5 = store.apply("get", "b", 888);
        Integer v6 = store.apply("get", "c", 888);
    }



    public static <T, U> TriFunction<String, T, U, U> store() {
        Map<T, U> store = new HashMap<>();
        return (action, t, u) -> {
            if (action.equalsIgnoreCase("get")) {
                return store.getOrDefault(t, u);
            }
            return store.put(t, u);
        };
    }
}
