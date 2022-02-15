package zxf.java.functional.pattern.designpattern.functional.case3;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FunctionalFileProcessor {
    private Predicate<Path> shouldHandle;
    private Consumer<Path> handler;

    private FunctionalFileProcessor(Predicate<Path> shouldHandle, Consumer<Path> handler) {
        this.shouldHandle = shouldHandle;
        this.handler = handler;
    }

    public void process(Path folder) {
        File[] files = folder.toFile().listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                process(file.toPath());
                continue;
            }
            if (shouldHandle.test(file.toPath())) {
                handler.accept(file.toPath());
            }
        }
    }

    public static FunctionalFileProcessor pdf() {
        return new FunctionalFileProcessor(PDF::shouldHandle, PDF::handle);
    }

    public static FunctionalFileProcessor word() {
        return new FunctionalFileProcessor(Word::shouldHandle, Word::handle);
    }

    //Should be protected for unit test
    private static class PDF {
        static boolean shouldHandle(Path file) {
            return file.getFileName().toString().endsWith(".pdf");
        }

        static void handle(Path file) {
            System.out.println("Processing pdf file: " + file.toString());
        }
    }

    //Should be protected for unit test
    private static class Word {
        public static boolean shouldHandle(Path file) {
            return file.getFileName().toString().endsWith(".docx");
        }

        public static void handle(Path file) {
            System.out.println("Processing word file: " + file.toString());
        }
    }
}
