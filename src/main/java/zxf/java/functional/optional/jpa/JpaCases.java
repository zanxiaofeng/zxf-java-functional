package zxf.java.functional.optional.jpa;

import zxf.java.functional.optional.model.Address;
import zxf.java.functional.optional.model.Customer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 模拟 Spring Data JPA 的 Optional 消费场景（纯 JDK，不引入 Spring/JPA 依赖）。
 *
 * <p>Spring Data JPA 的 {@code JpaRepository} 接口方法签名形如
 * {@code Optional<T> findById(ID id)}，返回 Optional 强制调用方显式处理「不存在」。
 * 本类用纯 JDK 定义 {@link CustomerRepo} 接口 + 内存实现演示该消费链。</p>
 */
public class JpaCases {

    public static void main(String[] args) {
        use_case1();
        use_case2();
        use_case3();
    }

    // 模拟 Spring Data JPA 的 Repository 接口：findById 返回 Optional<Customer>
    interface CustomerRepo {
        Optional<Customer> findById(String id);
    }

    // 内存实现：把 Optional 作为「值存在与否」的显式契约，不再用 null 表示「查不到」
    static class InMemoryCustomerRepo implements CustomerRepo {
        private final Map<String, Customer> store = new HashMap<>();

        InMemoryCustomerRepo() {
            Customer c1 = new Customer();
            c1.setId("c-1");
            c1.setName("Alice");
            Address a1 = new Address();
            a1.setId("a-1");
            a1.setCity("Beijing");
            a1.setState("BJ");
            a1.setZip("100000");
            c1.setAddress(a1);
            store.put("c-1", c1);
        }

        @Override
        public Optional<Customer> findById(String id) {
            // 查不到返回 empty，而非 null —— 调用方绝不会拿到 null
            return Optional.ofNullable(store.get(id));
        }
    }

    // ===== 场景一：findById + map + orElseThrow（最常见服务层消费链） =====

    // 模拟 Spring Data JPA findById(id).map(...).orElseThrow(...) 消费链
    public static void use_case1() {
        System.out.println("use_case1 findById + map + orElseThrow（服务层消费链）");
        CustomerRepo repo = new InMemoryCustomerRepo();

        String city = repo.findById("c-1")
                .map(Customer::getAddress)
                .map(Address::getCity)
                .orElseThrow(() -> new RuntimeException("customer not found: c-1"));
        System.out.println("  findById(\"c-1\").map(...).city = " + city);

        // 查不存在的 id：抛业务异常（替代: 手动 if null throw）
        try {
            repo.findById("c-x").orElseThrow(() -> new RuntimeException("customer not found: c-x"));
            System.out.println("  ERROR: 应当抛异常");
        } catch (RuntimeException e) {
            System.out.println("  查不到 c-x 抛出: " + e.getMessage());
        }
    }

    // ===== 场景二：findById + ifPresentOrElse（有/无记录分支处理） =====

    // 演示「存在」与「不存在」两条分支的处理
    public static void use_case2() {
        System.out.println("use_case2 findById + ifPresentOrElse");
        CustomerRepo repo = new InMemoryCustomerRepo();

        repo.findById("c-1").ifPresentOrElse(
                c -> System.out.println("  命中记录: " + c.getName()),
                () -> System.out.println("  无记录 c-1"));
        repo.findById("c-x").ifPresentOrElse(
                c -> System.out.println("  命中记录: " + c.getName()),
                () -> System.out.println("  无记录 c-x"));
    }

    // ===== 场景三：findById + or + orElseGet（多源回退链） =====

    // or：主源查不到则回退到备用源；收尾按契约二选一：orElseThrow（要求必有值）或 orElseGet（给默认值）
    public static void use_case3() {
        System.out.println("use_case3 findById + or（多源回退链）");
        CustomerRepo primary = new InMemoryCustomerRepo();     // 主源
        // 备用源：查任意 id 返回一个具名 customer（模拟缓存/降级数据源）
        CustomerRepo backup = id -> {
            Customer c = new Customer();
            c.setId(id);
            c.setName("Backup-Customer");
            return Optional.of(c);
        };

        // 主源 miss -> or 回退到备用源命中
        Customer fromBackup = primary.findById("c-x")
                .or(() -> backup.findById("c-x"))
                .orElseThrow(() -> new RuntimeException("all sources empty"));
        System.out.println("  主源 miss -> 备用源命中: name = " + fromBackup.getName());

        // 两源都 miss（primary 与 emptyRepo 均 empty）走 orElseGet 默认值
        CustomerRepo emptyRepo = id -> Optional.empty();
        Customer fallback = primary.findById("c-y")
                .or(() -> emptyRepo.findById("c-y"))
                .orElseGet(() -> {
                    Customer c = new Customer();
                    c.setName("(anonymous)");
                    return c;
                });
        System.out.println("  两源都 miss 时默认值: name = " + fallback.getName());
    }
}
