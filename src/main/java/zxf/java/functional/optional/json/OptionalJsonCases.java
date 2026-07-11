package zxf.java.functional.optional.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import zxf.java.functional.optional.model.Book;

import java.io.IOException;
import java.util.Optional;

/**
 * Optional 与 JSON 序列化/反序列化的交互演示。
 *
 * <p>Jackson 注册 {@link Jdk8Module} 后能把 Optional 字段正确序列化为
 * 原始值或缺失字段；但反序列化「缺失字段」时拿到的是 null 而非 Optional.empty()，
 * 揭示了 Optional 作字段的争议性。</p>
 */
public class OptionalJsonCases {
    public static void main(String[] args) throws IOException {
        use_case1();
        use_case2();
        use_case3();
    }

    // 基础：Optional 非空字段的序列化/反序列化
    public static void use_case1() throws IOException {
        System.out.println("use_case1 Optional 非空字段序列化");
        Book book = new Book();
        book.setTitle("Test book");
        book.setSubTitle(Optional.of("this is sub title"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        String json = mapper.writeValueAsString(book);
        System.out.println("  序列化 JSON = " + json);
        Book newBook = mapper.readValue(json, Book.class);
        System.out.println("  反序列化 subTitle = " + newBook.getSubTitle());
    }

    // 关键对比：Optional.empty() vs null 作为字段的序列化差异
    public static void use_case2() throws IOException {
        System.out.println("use_case2 Optional.empty() vs null 字段序列化对比");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());

        // 情形 A：setSubTitle(Optional.empty()) —— Jdk8Module 把 empty 序列化为「字段缺失」
        Book bookEmpty = new Book();
        bookEmpty.setTitle("Book A");
        bookEmpty.setSubTitle(Optional.empty());
        String jsonEmpty = mapper.writeValueAsString(bookEmpty);
        System.out.println("  setSubTitle(Optional.empty()) JSON = " + jsonEmpty);

        // 情形 B：setSubTitle(null) —— 字段本身为 null，同样缺失
        Book bookNull = new Book();
        bookNull.setTitle("Book B");
        bookNull.setSubTitle(null);
        String jsonNull = mapper.writeValueAsString(bookNull);
        System.out.println("  setSubTitle(null)            JSON = " + jsonNull);

        System.out.println("  结论: 序列化层面 empty 与 null 输出一致（都为缺失字段）");
    }

    // 反序列化后的状态：空字段反序列化可能是 null 而非 empty（Optional 作字段的争议性）
    public static void use_case3() throws IOException {
        System.out.println("use_case3 反序列化空字段后 getSubTitle() 的实际状态");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());

        // 只设 title，subTitle 字段在 JSON 中完全缺失
        String jsonMissingField = "{\"title\":\"Book C\"}";
        Book newBook = mapper.readValue(jsonMissingField, Book.class);
        Optional<String> subTitle = newBook.getSubTitle();
        System.out.println("  JSON 缺失 subTitle 字段, getSubTitle() = " + subTitle);
        System.out.println("  getSubTitle() == null? " + (subTitle == null)
                + "  (而非 Optional.empty!)");
        System.out.println("  结论: 反序列化缺失字段得到 null 而非 Optional.empty —— "
                + "Optional 作字段既不能保证非 null，又增加复杂度，故生产环境不推荐作字段类型（参考 Book.java 注释）");
    }
}
