package zxf.java.functional.pattern.oop;

import java.nio.file.Path;

public class WordOOPFileProcessor extends OOPFileProcessor {
    @Override
    protected boolean shouldHandle(Path file) {
        return file.getFileName().endsWith(".docx");
    }

    @Override
    protected void handle(Path file) {
        System.out.println("Processing word file " + file.getFileName());
    }
}
