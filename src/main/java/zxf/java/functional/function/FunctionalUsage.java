package zxf.java.functional.function;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FunctionalUsage {
    public static void main(String[] args) {
        //关于函数的Throws子句，没有Throws子句的函数可以赋值给有Ｔhrows子句的函数只要参数和返回类型匹配即可；
        //有Ｔhrows子句的函数之间，要看Ｔhrows子句定义的Ｅxception之间的覆盖关系，
        //如果Ａ函数签名定义的Ｔhrow　Ｅxceptions能覆盖Ｂ函数签名定义的Ｔhrow　Ｅxceptions，
        //那么拥有Ｂ函数签名的函数值可以赋值给拥有Ａ函数签名类型的入参，返回值，变量
        //BiConsumer<Path, String> fileLogger = FunctionalUsage::log;
    }

    public static void log(Path logFile, String message) throws Exception {
        Files.write(logFile, (message + "\n").getBytes(StandardCharsets.UTF_8), CREATE, APPEND);
    }
}
