package zxf.java.functional.stream;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class StreamUsageCases {
    private static Set<String> stopWords = new HashSet<String>() {{
        add("is");
        add("a");
        add("an");
        add("the");
        add("of");
        add("to");
        add("in");
        add("as");
        add("be");
    }};

    public static void main(String[] args) {
        use_case1();
    }

    public static void use_case1() {
        Map<String, Integer> wordCount = Pattern.compile("\\s").splitAsStream("he is a child and he like play basket ball")
                .filter(x-> !stopWords.contains(x)).reduce(new HashMap<String, Integer>(), (r, w) -> {
                    r.compute(w, (k, v) -> v == null ? 1 : v + 1);
                    return r;
                }, (r1, r2) -> {
                    r1.putAll(r2);
                    return r1;
                });

    }
}
