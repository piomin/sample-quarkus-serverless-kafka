package pl.piomin.samples.quarkus.serverless.order.model;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Order() {
    }

    public Order(Long id, Integer customerId, Integer productId, int amount, int productCount, OrderStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.amount = amount;
        this.productCount = productCount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public String getRejectedService() {
        return rejectedService;
    }

    public void setRejectedService(String rejectedService) {
        this.rejectedService = rejectedService;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", productId=" + productId +
                ", amount=" + amount +
                ", productCount=" + productCount +
                ", rejectedService='" + rejectedService + '\'' +
                ", status=" + status +
                ", source='" + source + '\'' +
                '}';
    }
}
