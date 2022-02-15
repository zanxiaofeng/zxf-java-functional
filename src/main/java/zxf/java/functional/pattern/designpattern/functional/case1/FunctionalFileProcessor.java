package zxf.java.functional.pattern.designpattern.functional.case1;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FunctionalFileProcessor {
    public void process(Path folder, Predicate<Path> shouldHandle, Consumer<Path> handler) {
        File[] files = folder.toFile().listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                process(file.toPath(), shouldHandle, handler);
                continue;
            }
            if (shouldHandle.test(file.toPath())) {
                handler.accept(file.toPath());
            }
        }
    }

    public interface PDF {
        static boolean shouldHandle(Path file) {
            return file.getFileName().toString().endsWith(".pdf");
        }

        static void handle(Path file) {
            System.out.println("Processing pdf file: " + file.toString());
        }
    }

    public static class Word {
        public static boolean shouldHandle(Path file) {
            return file.getFileName().toString().endsWith(".docx");
        }

        public static void handle(Path file) {
            System.out.println("Processing word file: " + file.toString());
        }
    }
}
