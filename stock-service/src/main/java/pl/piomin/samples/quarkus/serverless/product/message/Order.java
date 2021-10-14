package pl.piomin.samples.quarkus.serverless.product.message;

import lombok.Data;

@Data
public class Order {
    private Long id;
    private Long productId;
    private int productsCount;
    private OrderStatus status;
    private String source;
    private String rejectedService;
}
