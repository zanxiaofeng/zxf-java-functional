package zxf.java.functional.stream;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class StreamUsageCases {
    private static Set<String> stopWords = Set.of("is", "a", "an", "the", "am", "are", "be", "to", "for",
            "of", "and", "in", "or", "on", "by", "it", "as", "s", "any");
    private static Pattern splitter = Pattern.compile("[\\s,.;:\\)\\(’]+");

    public static void main(String[] args) throws IOException, URISyntaxException {
        use_case1();
    }

    public static void use_case1() throws IOException, URISyntaxException {
        String fileContent = Files.readString(Paths.get(StreamCreationCases.class.getResource("/files/article.txt").toURI()));
        Map<String, Integer> wordCount = splitter.splitAsStream(fileContent)
                .map(String::toLowerCase)
                .filter(Predicate.not(String::isBlank))
                .filter(Predicate.not(stopWords::contains))
                .reduce(new HashMap<>(), (r, w) -> {
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
