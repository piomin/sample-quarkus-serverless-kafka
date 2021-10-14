package pl.piomin.samples.quarkus.serverless.order.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {

    @Id
    private Long id;
    private Integer customerId;
    private Integer productId;
    private int amount;
    private int productCount;
    private String rejectedService;
    @Enumerated
    private OrderStatus status = OrderStatus.NEW;
    @Transient
    private String source;
}
