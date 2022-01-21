package zxf.java.functional.pattern.designpattern.functional.case2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FunctionalFileProcessor {
    public void process(Path folder, Handler handler) throws IOException {
        Files.newDirectoryStream(folder).forEach(path -> {
            if (handler.shouldHandle(path)) {
                handler.handle(path);
            }
        });
    }

    public interface Handler {
        boolean shouldHandle(Path file);

        void handle(Path file);
    }

    public static class HandlerImpl implements Handler {
        private Predicate<Path> shouldHandle;
        private Consumer<Path> handler;

        public HandlerImpl(Predicate<Path> shouldHandle, Consumer<Path> handler) {
            this.shouldHandle = shouldHandle;
            this.handler = handler;
        }

        @Override
        public boolean shouldHandle(Path file) {
            return shouldHandle.test(file);
        }

        @Override
        public void handle(Path file) {
            handler.accept(file);
        }
    }

    public interface Handlers {
        static Handler pdf() {
            return new HandlerImpl(PDF::shouldHandle, PDF::handle);
        }

        static Handler word() {
            return new HandlerImpl(Word::shouldHandle, Word::handle);
        }
    }

    public static class PDF {
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
