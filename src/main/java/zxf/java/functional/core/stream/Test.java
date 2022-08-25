package zxf.java.functional.core.stream;

import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        Stream<Integer> ints = Stream.list(Arrays.asList(1,2,3,4));
        List<String> result = ints.filter(i->i%2==0).map(String::valueOf).collect(Collector.toList());
        System.out.println(result.size());


        Stream<Integer> numbers = Stream.list(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
        String numberResult = numbers.filter(i->i%2==0).map(String::valueOf).collect(Collector.joining());
        System.out.println(numberResult);
    }
}
