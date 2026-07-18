package zxf.java.functional.pattern.closure.store;

import zxf.java.functional.core.function.TriFunction;

public class StoreCases {
    public static void main(String[] args) {
        store_case_1();
        store_case_2();
    }

    public static void store_case_1() {
        System.out.println("#case 1: store() —— 闭包直接捕获 HashMap");
        TriFunction<String, String, Integer, Integer> store = Store.store();
        System.out.println("put a=123 返回旧值: " + store.apply("put", "a", 123)); // null（首次放入）
        System.out.println("put b=234 返回旧值: " + store.apply("put", "b", 234)); // null
        System.out.println("put b=432 返回旧值: " + store.apply("put", "b", 432)); // 234（覆盖时返回旧值）

        System.out.println("get a（缺省 888）: " + store.apply("get", "a", 888));        // 123
        System.out.println("get b（缺省 888）: " + store.apply("get", "b", 888));        // 432（拿到覆盖后的值）
        System.out.println("get 不存在的 c（缺省 888）: " + store.apply("get", "c", 888)); // 888
    }


    public static void store_case_2() {
        System.out.println("\n#case 2: storeByCmd() —— 闭包捕获 Command 对象");
        TriFunction<String, String, Integer, Integer> store = Store.storeByCmd();
        System.out.println("put a=123 返回旧值: " + store.apply("put", "a", 123)); // null
        System.out.println("put b=234 返回旧值: " + store.apply("put", "b", 234)); // null
        System.out.println("put b=432 返回旧值: " + store.apply("put", "b", 432)); // 234

        System.out.println("get a（缺省 888）: " + store.apply("get", "a", 888));        // 123
        System.out.println("get b（缺省 888）: " + store.apply("get", "b", 888));        // 432
        System.out.println("get 不存在的 c（缺省 888）: " + store.apply("get", "c", 888)); // 888
    }
}
