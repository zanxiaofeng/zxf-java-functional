package zxf.java.functional.pattern.designpattern.functional.case3;

import java.io.IOException;
import java.nio.file.Paths;

public class FunctionalFileProcessorCases {
    public static void main(String[] args) throws IOException {
        use_case1();
        use_case2();
    }

    public static void use_case1() throws IOException {
        FunctionalFileProcessor functionalFileProcessor = FunctionalFileProcessor.pdf();
        functionalFileProcessor.process(Paths.get(""));
    }

    public static void use_case2() throws IOException {
        FunctionalFileProcessor functionalFileProcessor = FunctionalFileProcessor.word();
        functionalFileProcessor.process(Paths.get(""));
    }
}
