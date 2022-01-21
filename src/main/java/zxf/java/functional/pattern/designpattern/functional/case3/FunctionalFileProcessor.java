package zxf.java.functional.pattern.designpattern.functional.case3;

import java.io.IOException;
import java.nio.file.Files;
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

    public void process(Path folder) throws IOException {
        Files.newDirectoryStream(folder).forEach(path -> {
            if (shouldHandle.test(path)) {
                handler.accept(path);
            }
        });
    }

    public static FunctionalFileProcessor pdf() {
        return new FunctionalFileProcessor(PDF::shouldHandle, PDF::handle);
    }

    public static FunctionalFileProcessor word() {
        return new FunctionalFileProcessor(Word::shouldHandle, Word::handle);
    }

    public static class PDF {
        static boolean shouldHandle(Path file) {
            return file.getFileName().endsWith(".pdf");
        }

        static void handle(Path file) {
            System.out.println("Processing pdf file " + file.getFileName());
        }
    }

    public static class Word {
        public static boolean shouldHandle(Path file) {
            return file.getFileName().endsWith(".docx");
        }

        public static void handle(Path file) {
            System.out.println("Processing word file" + file.getFileName());
        }
    }
}
