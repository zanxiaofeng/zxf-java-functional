package zxf.java.functional.pattern.functional;

import java.io.IOException;
import java.nio.file.Paths;

public class FunctionalFileProcessorCases {
    public static void main(String[] args) throws IOException {
        use_case1();
        use_case2();
    }

    public static void use_case1() throws IOException {
        FunctionalFileProcessor functionalFileProcessor = new FunctionalFileProcessor();
        functionalFileProcessor.process(
                Paths.get(""),
                FunctionalFileProcessor.PDF::shouldHandle,
                FunctionalFileProcessor.PDF::handle);
    }

    public static void use_case2() throws IOException {
        FunctionalFileProcessor functionalFileProcessor = new FunctionalFileProcessor();
        functionalFileProcessor.process(
                Paths.get(""),
                FunctionalFileProcessor.Word::shouldHandle,
                FunctionalFileProcessor.Word::handle);
    }
}
