package zxf.java.functional.optional;

import zxf.java.functional.optional.model.Address;
import zxf.java.functional.optional.model.Customer;
import zxf.java.functional.optional.model.Order;

import java.util.Optional;

public class OptionalCases {

    public static void use_case1() {
        //Original
        Integer int1 = getInteger();
        if (int1 == null) {
            int1 = 23;
        }

        //New
        Integer int2 = getOptInteger().orElse(23);
    }

    public static void use_case2() throws Exception {
        //Original
        Integer int1 = getInteger();
        if (int1 == null) {
            throw new Exception("******");
        }

        //New
        Integer int2 = getOptInteger().orElseThrow(() -> new Exception("******"));
    }

    public static void use_case3() throws Exception {
        //Original
        Order order = Order.getOrder();
        if (order != null) {
            Customer customer = order.getCustomer();
            if (customer != null) {
                Address address = customer.getAddress();
                if (address != null) {
                    //Do something with address
                }
            }
        }

        //New
        Optional<Address> optionalAddress = Optional.ofNullable(Order.getOrder())
                .map(Order::getCustomer)
                .map(Customer::getAddress);
        //Do something with optionalAddress
    }

    public static void use_case4_1() throws Exception {
        //Original
        Integer int1 = getInteger();
        if (int1 == null) {
            //Do something with int1
        }

        //New
        getOptInteger().ifPresent((int2) -> {
            //Do something with int2
        });
    }

    public static void use_case4_2() throws Exception {
        //Original
        Integer int1 = getInteger();
        if (int1 == null) {
            //Do something with int1
        } else {
            //Do something else
        }

        //New
        getOptInteger().ifPresentOrElse((int2) -> {
            //Do something with int2
        }, () -> {
            //Do something else
        });
    }

    private static Integer getInteger() {
        return 232;
    }

    private static Optional<Integer> getOptInteger() {
        return Optional.of(232);
    }
}
