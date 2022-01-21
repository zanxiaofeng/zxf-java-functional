package zxf.java.functional.optional.model;

public class Order {
    private String id;
    private Double amount;
    private Customer customer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public static Order getOrder(){
        Address address = new Address();
        address.setId("a-1");
        address.setCity("city");
        address.setState("state");
        address.setZip("zip");
        Customer customer = new Customer();
        customer.setId("c-1");
        customer.setName("customer");
        customer.setAddress(address);
        Order order = new Order();
        order.setId("o-1");
        order.setAmount(9.9);
        order.setCustomer(customer);
        return order;
    }
}
