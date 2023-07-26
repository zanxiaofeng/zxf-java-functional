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

    public int typeOrder() {
        if (this.type == null) {
            return -1;
        }
        return "OFZH".indexOf(this.type.toUpperCase(Locale.ROOT));
    }


    public static Comparator<MyBean> myBeanComparator() {
        // number can not be null
        return Comparator.comparingInt(MyBean::typeOrder).thenComparing(MyBean::getNumber, String.CASE_INSENSITIVE_ORDER);
    }
}
