package zxf.java.functional.pattern.closure.cache;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class UIdGenerator {
    public static void main(String[] args) {
        Supplier<String> idGenerator = uniqueIdGenerator();

        System.out.println(idGenerator.get());
        System.out.println(idGenerator.get());
        System.out.println(idGenerator.get());
    }

    private static Supplier<String> uniqueIdGenerator() {
        final Set<String> ids = new HashSet<>();
        return ()-> {
            String batchNumber = null;
            do {
                batchNumber = LocalDateTime.now().toString();
            } while (ids.contains(batchNumber));
            ids.add(batchNumber);
            return batchNumber;
        };
    }
}
