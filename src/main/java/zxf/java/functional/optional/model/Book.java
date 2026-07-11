package zxf.java.functional.optional.model;

import java.util.Optional;

public class Book {
    private String title;
    /**
     * 注意：此 Optional<String> 字段【仅为教学/演示反模式用途】。
     * 生产环境 Optional 不应作为字段类型，原因：
     *   1. Optional 设计初衷是作为方法返回类型，表示「可能无值」的显式契约；
     *   2. 序列化框架（Jackson/JDK 序列化）对 Optional 字段支持不一致，反序列化缺失字段常得到 null 而非 empty；
     *   3. Optional 不可序列化（不实现 Serializable），作 DTO/Entity 字段会破坏序列化；
     *   4. 占用更多内存（Optional 包装对象开销）。
     * 正确用法：字段用原生类型 + nullable，仅在方法返回值用 Optional。
     * 参见 {@link zxf.java.functional.optional.json.OptionalJsonCases} 的反序列化演示。
     */
    private Optional<String> subTitle;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Optional<String> getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(Optional<String> subTitle) {
        this.subTitle = subTitle;
    }
}
