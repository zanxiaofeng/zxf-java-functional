package zxf.java.functional.pattern.closure.store;

import zxf.java.functional.pattern.core.TriFunction;

public class StoreCases {
    public static void main(String[] args) {
        store_case_1();
        store_case_2();
    }

    public static void store_case_1() {
        TriFunction<String, String, Integer, Integer> store = Store.store();
        Integer v1 = store.apply("put", "a", 123);
        Integer v2 = store.apply("put", "b", 234);
        Integer v3 = store.apply("put", "b", 432);

        Integer v4 = store.apply("get", "a", 888);
        Integer v5 = store.apply("get", "b", 888);
        Integer v6 = store.apply("get", "c", 888);
    }


    public static void store_case_2() {
        TriFunction<String, String, Integer, Integer> store = Store.storeByCmd();
        Integer v1 = store.apply("put", "a", 123);
        Integer v2 = store.apply("put", "b", 234);
        Integer v3 = store.apply("put", "b", 432);

        Integer v4 = store.apply("get", "a", 888);
        Integer v5 = store.apply("get", "b", 888);
        Integer v6 = store.apply("get", "c", 888);
    }
}
