package zxf.java.functional.stream;

import zxf.java.functional.stream.account.MyBean;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamUsageCases {
    private static Set<String> stopWords = Set.of("is", "a", "an", "the", "am", "are", "be", "to", "for", "of", "and", "in", "or", "on", "by", "it", "as", "s", "any");
    private static Pattern splitter = Pattern.compile("[\\s,.;:\\)\\(’]+");

    public static void main(String[] args) throws IOException, URISyntaxException {
        use_case1();
        use_case2();
    }

    public static void use_case1() throws IOException, URISyntaxException {
        // Please note "Files.readString(Paths.get(StreamCreationCases.class.getResource("/files/article.txt").toURI()))" will fail when run this program by jar
        String fileContent = Files.readString(Paths.get(StreamCreationCases.class.getResource("/files/article.txt").toURI()));
        Map<String, Integer> wordCount = splitter.splitAsStream(fileContent).map(String::toLowerCase).filter(Predicate.not(String::isBlank)).filter(Predicate.not(stopWords::contains)).reduce(new HashMap<>(), (r, w) -> {
            r.compute(w, (k, v) -> v == null ? 1 : v + 1);
            return r;
        }, (r1, r2) -> {
            r1.putAll(r2);
            return r1;
        });
        wordCount.forEach((k, v) -> {
            System.out.println("Word: " + k + ", " + v);
        });
    }

    public static void use_case2() {
        MyBean bean1 = new MyBean();
        bean1.setType("f");
        bean1.setNumber("20230706");

        MyBean bean2 = new MyBean();
        bean2.setType("F");
        bean2.setNumber("20230703");

        MyBean bean3 = new MyBean();
        bean3.setType("H");
        bean3.setNumber("20230501");

        MyBean bean4 = new MyBean();
        bean4.setType("Z");
        bean4.setNumber("20230601");

        MyBean bean5 = new MyBean();
        bean5.setType("H");
        bean5.setNumber("20230301");

        MyBean bean6 = new MyBean();
        bean6.setNumber("20230301");

        MyBean bean7 = new MyBean();
        bean7.setType("Y");
        bean7.setNumber("20230301");


        List<MyBean> sortedMyBeans = Stream.of(bean1, bean2, bean3, bean4, bean5, bean6, bean7).sorted(MyBean.myBeanComparator()).collect(Collectors.toList());

        for (int i = 0; i < sortedMyBeans.size(); i++) {
            MyBean bean = sortedMyBeans.get(i);
            System.out.println("MyBean: type " + bean.getType() + ", " + bean.getNumber());
        }

    }
}
