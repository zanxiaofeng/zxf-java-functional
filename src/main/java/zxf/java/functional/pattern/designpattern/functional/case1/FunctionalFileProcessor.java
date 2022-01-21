package zxf.java.functional.pattern.designpattern.functional.case1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FunctionalFileProcessor {
    public void process(Path folder, Predicate<Path> shouldHandle, Consumer<Path> handler) throws IOException {
        Files.newDirectoryStream(folder).forEach(path -> {
            if (shouldHandle.test(path)) {
                handler.accept(path);
            }
        });
    }

    public interface PDF {
        static boolean shouldHandle(Path file) {
            return file.getFileName().toString().endsWith(".pdf");
        }

        static void handle(Path file) {
            System.out.println("Processing pdf file: " + file.getFileName());
        }
    }

    public static class Word {
        public static boolean shouldHandle(Path file) {
            return file.getFileName().toString().endsWith(".docx");
        }

        public static void handle(Path file) {
            System.out.println("Processing word file: " + file.getFileName());
        }
    }
}
