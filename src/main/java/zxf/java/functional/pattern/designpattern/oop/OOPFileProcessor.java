package zxf.java.functional.pattern.designpattern.oop;

import java.io.File;
import java.nio.file.Path;

public abstract class OOPFileProcessor {
    public void process(Path folder) {
        File[] files = folder.toFile().listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                process(file.toPath());
                continue;
            }
            if (shouldHandle(file.toPath())) {
                handle(file.toPath());
            }
        }
    }

    protected abstract boolean shouldHandle(Path file);

    protected abstract void handle(Path file);
}
