package zxf.java.functional.pattern.designpattern.oop;

import java.nio.file.Paths;

public class OOPFileProcessorCases {
    public static void main(String[] args) {
        use_case1();
        use_case2();
    }

    public static void use_case1() {
        System.out.println("use-case1: pdf-process");
        OOPFileProcessor fileProcessor = new PDFOOPFileProcessor();
        fileProcessor.process(Paths.get("./files"));
    }

    public static void use_case2() {
        System.out.println("\nuse-case1: word-process");
        OOPFileProcessor fileProcessor = new WordOOPFileProcessor();
        fileProcessor.process(Paths.get("./files"));
    }
}
