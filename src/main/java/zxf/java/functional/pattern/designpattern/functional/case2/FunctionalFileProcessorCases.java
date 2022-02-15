package zxf.java.functional.pattern.designpattern.functional.case2;

import java.nio.file.Paths;

public class FunctionalFileProcessorCases {
    public static void main(String[] args) {
        use_case1();
        use_case2();
    }

    public static void use_case1() {
        System.out.println("use-case1: pdf-process");
        FunctionalFileProcessor functionalFileProcessor = new FunctionalFileProcessor();
        functionalFileProcessor.process(Paths.get("./files"), FunctionalFileProcessor.Handler.pdf());
    }

    public static void use_case2() {
        System.out.println("\nuse-case1: word-process");
        FunctionalFileProcessor functionalFileProcessor = new FunctionalFileProcessor();
        functionalFileProcessor.process(Paths.get("./files"), FunctionalFileProcessor.Handler.word());
    }
}
