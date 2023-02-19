package pl.piomin.samples.quarkus.serverless.product;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.piomin.samples.quarkus.serverless.product.message.Order;
import pl.piomin.samples.quarkus.serverless.product.message.OrderStatus;
import pl.piomin.samples.quarkus.serverless.product.model.Product;
import pl.piomin.samples.quarkus.serverless.product.repository.ProductRepository;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderReserveFunctionTests {

    private static int reserved;
    private ProductRepository repository;

    public OrderReserveFunctionTests(ProductRepository repository) {
        this.repository = repository;
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void reserve() {
        Product p = repository.findById(1L);
        reserved = p.getReservedItems();
        given().contentType("application/json").body(createTestOrder(OrderStatus.NEW)).post("/reserve")
                .then()
                .statusCode(204);
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void confirm() {
        given().contentType("application/json").body(createTestOrder(OrderStatus.IN_PROGRESS)).post("/reserve")
                .then()
                .statusCode(204);
        Product p = repository.findById(1L);
//        assertEquals(reserved - 5, p.getReservedItems());
    }

    private Order createTestOrder(OrderStatus status) {
        Order o = new Order();
        o.setId(1L);
        o.setSource("test");
        o.setStatus(status);
        o.setProductId(1L);
        o.setProductsCount(5);
        return o;
    }
}
