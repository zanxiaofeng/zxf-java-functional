package zxf.java.functional.function;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FunctionalUsage {
    public static void main(String[] args) {
        //一个函数值是否可以赋值给一个函数类型，首先要看函数值的参数类型、参数顺序、返回值类型是否与函数类型声明的形式兼容一致，
        //其次要看函数值声明的Exceptions(Throws子句)能否被函数类型声明的Exceptions(Throws子句)覆盖包含，
        //立即函数-Lambda虽然没有显式的Throws子句，但编译器可以依据其代码中调用的函数的签名汇总出其可能Throw的Ｅxceptions，
        //因此也要符合这个约束条件
        //BiConsumer<Path, String> fileLogger = FunctionalUsage::log;
    }

    public static void log(Path logFile, String message) throws Exception {
        Files.write(logFile, (message + "\n").getBytes(StandardCharsets.UTF_8), CREATE, APPEND);
    }
}
