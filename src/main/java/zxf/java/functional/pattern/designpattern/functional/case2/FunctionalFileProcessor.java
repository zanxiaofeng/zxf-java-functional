package zxf.java.functional.pattern.designpattern.functional.case2;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FunctionalFileProcessor {
    public void process(Path folder, Handler handler) {
        File[] files = folder.toFile().listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                process(file.toPath(), handler);
                continue;
            }
            if (handler.shouldHandle(file.toPath())) {
                handler.handle(file.toPath());
            }
        }
    }

    public interface Handler {
        boolean shouldHandle(Path file);

        void handle(Path file);

        static Handler pdf() {
            return new HandlerImpl(PDF::shouldHandle, PDF::handle);
        }

        static Handler word() {
            return new HandlerImpl(Word::shouldHandle, Word::handle);
        }
    }

    //Should be protected for unit test
    private static class HandlerImpl implements Handler {
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
