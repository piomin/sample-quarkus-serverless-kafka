package pl.piomin.samples.quarkus.serverless.product;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.piomin.samples.quarkus.serverless.product.client.OrderSender;
import pl.piomin.samples.quarkus.serverless.product.message.Order;
import pl.piomin.samples.quarkus.serverless.product.message.OrderStatus;
import pl.piomin.samples.quarkus.serverless.product.model.Product;
import pl.piomin.samples.quarkus.serverless.product.repository.ProductRepository;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderReserveFunctionTests {

    private static int items;
    private ProductRepository repository;
    @InjectMock
    @RestClient
    OrderSender sender;

    public OrderReserveFunctionTests(ProductRepository repository) {
        this.repository = repository;
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void reserve() {
        given().contentType("application/json").body(createTestOrder(OrderStatus.NEW)).post("/reserve")
                .then()
                .statusCode(204);

        Product p = repository.findById(1L);
        items = p.getAvailableItems();
        assertEquals(5, p.getReservedItems());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void confirm() {
        given().contentType("application/json").body(createTestOrder(OrderStatus.CONFIRMED)).post("/reserve")
                .then()
                .statusCode(204);

        Product p = repository.findById(1L);
        assertEquals(0, p.getReservedItems());
        assertEquals(items, p.getAvailableItems());
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
