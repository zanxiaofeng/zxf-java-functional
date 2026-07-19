package zxf.java.functional.stream.account;

import java.util.Comparator;
import java.util.Locale;

public class MyBean {
    private String type;
    private String number;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    // 教学简化：假设 type 为单字符（"O"/"F"/"Z"/"H"）。多字符时 indexOf 返回子串位置，语义不适用；
    // null type 与未知 type 均返回 -1（排在最前），不做区分。
    public int typeOrder() {
        if (this.type == null) {
            return -1;
        }
        return "OFZH".indexOf(this.type.toUpperCase(Locale.ROOT));
    }


    public static Comparator<MyBean> myBeanComparator() {
        // number can not be null（教学简化：未做 null 防御，number 为 null 时比较器会抛 NPE）
        return Comparator.comparingInt(MyBean::typeOrder).thenComparing(MyBean::getNumber, String.CASE_INSENSITIVE_ORDER);
    }
}
