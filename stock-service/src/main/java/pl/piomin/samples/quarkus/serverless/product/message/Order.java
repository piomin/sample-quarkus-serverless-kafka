package pl.piomin.samples.quarkus.serverless.product.message;

public class Order {
    private Long id;
    private Long productId;
    private int productsCount;
    private OrderStatus status;
    private String source;
    private String rejectedService;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(int productsCount) {
        this.productsCount = productsCount;
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
                ", productId=" + productId +
                ", productsCount=" + productsCount +
                ", status=" + status +
                ", source='" + source + '\'' +
                ", rejectedService='" + rejectedService + '\'' +
                '}';
    }
}
