package zxf.java.functional.pattern.oop;

import java.io.IOException;
import java.nio.file.Paths;

public class OOPFileProcessorCases {
    public static void main(String[] args) throws IOException {
        use_case1();
        use_case2();
    }

    public static void use_case1() throws IOException {
        OOPFileProcessor fileProcessor = new PDFOOPFileProcessor();
        fileProcessor.process(Paths.get(""));
    }

    public static void use_case2() throws IOException {
        OOPFileProcessor fileProcessor = new WordOOPFileProcessor();
        fileProcessor.process(Paths.get(""));
    }
}
