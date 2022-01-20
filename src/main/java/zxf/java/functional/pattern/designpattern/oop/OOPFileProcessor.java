package zxf.java.functional.pattern.designpattern.oop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class OOPFileProcessor {
    public void process(Path folder) throws IOException {
        Files.newDirectoryStream(folder).forEach(path -> {
            if (shouldHandle(path)) {
                handle(path);
            }
        });
    }

    protected abstract boolean shouldHandle(Path file);

    protected abstract void handle(Path file);
}
