package zxf.java.functional.pattern.designpattern.oop;

import java.nio.file.Path;

public class PDFOOPFileProcessor extends OOPFileProcessor {
    @Override
    protected boolean shouldHandle(Path file) {
        return file.getFileName().endsWith(".pdf");
    }

    @Override
    protected void handle(Path file) {
        System.out.println("Processing pdf file " + file.getFileName());
    }
}
