package zxf.java.functional.stream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class StreamUsageCases {
    private static Set<String> stopWords = Set.of("is", "a", "an", "the", "am", "are", "be", "to", "for",
            "of", "and", "in", "or", "on", "by", "it", "as", "s", "any");
    private static Pattern splitter = Pattern.compile("[\\s,.;:\\)\\(’]+");

    public static void main(String[] args) throws IOException {
        use_case1();
    }

    public static void use_case1() throws IOException {
        Map<String, Integer> wordCount = splitter.splitAsStream(Files.readString(Paths.get("./article.txt")))
                .filter(x -> !stopWords.contains(x.toLowerCase(Locale.ROOT))).reduce(new HashMap<String, Integer>(), (r, w) -> {
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
}
