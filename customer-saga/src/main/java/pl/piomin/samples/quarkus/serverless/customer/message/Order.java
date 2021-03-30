package pl.piomin.samples.quarkus.serverless.customer.message;

import lombok.Data;

@Data
public class Order {
    private Long id;
    private Long customerId;
    private int amount;
    private OrderStatus status;
}
