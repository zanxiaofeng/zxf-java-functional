package zxf.java.functional.pattern.designpattern.functional.case2;

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
                Paths.get(""), FunctionalFileProcessor.Handlers.pdf());
    }

    public static void use_case2() throws IOException {
        FunctionalFileProcessor functionalFileProcessor = new FunctionalFileProcessor();
        functionalFileProcessor.process(
                Paths.get(""),
                FunctionalFileProcessor.Handlers.pdf());
    }
}
