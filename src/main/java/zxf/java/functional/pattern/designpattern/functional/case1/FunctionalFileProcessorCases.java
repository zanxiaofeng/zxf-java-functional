package zxf.java.functional.pattern.designpattern.functional.case1;

import java.io.IOException;
import java.nio.file.Paths;

public class FunctionalFileProcessorCases {
    public static void main(String[] args) throws IOException {
        use_case1();
        use_case2();
    }

    public static void use_case1() throws IOException {
        System.out.println("use-case1: pdf-process");
        FunctionalFileProcessor functionalFileProcessor = new FunctionalFileProcessor();
        functionalFileProcessor.process(Paths.get("./files"),
                FunctionalFileProcessor.PDF::shouldHandle, FunctionalFileProcessor.PDF::handle);
    }

    public static void use_case2() throws IOException {
        System.out.println("\nuse-case1: word-process");
        FunctionalFileProcessor functionalFileProcessor = new FunctionalFileProcessor();
        functionalFileProcessor.process(Paths.get("./files"),
                FunctionalFileProcessor.Word::shouldHandle, FunctionalFileProcessor.Word::handle);
    }
}
