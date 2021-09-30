package pl.piomin.samples.quarkus.serverless.customer.message;

public class Order {
    private Long id;
    private Long customerId;
    private int amount;
    private OrderStatus status;
    private String source;
    private String rejectedService;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public String getRejectedService() {
        return rejectedService;
    }

    public void setRejectedService(String rejectedService) {
        this.rejectedService = rejectedService;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", amount=" + amount +
                ", status=" + status +
                ", source='" + source + '\'' +
                ", rejectedService='" + rejectedService + '\'' +
                '}';
    }
}
