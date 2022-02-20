package zxf.java.functional.function;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FunctionalUsage {
    public static void main(String[] args) {
        //Throws Exceptions会导致函数契约不匹配，从而影响Ｊava中函数的相互转换，
        //比如函数"public static void log(Path logFile, String message) throws Exception"不能转换成BiConsumer<Path, String>
        //BiConsumer<Path, String> fileLogger = FunctionalUsage::log;
    }

    public static void log(Path logFile, String message) throws Exception {
        Files.write(logFile, (message + "\n").getBytes(StandardCharsets.UTF_8), CREATE, APPEND);
    }
}
