package pl.piomin.samples.quarkus.serverless.order;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.piomin.samples.quarkus.serverless.order.client.OrderSender;
import pl.piomin.samples.quarkus.serverless.order.model.Order;
import pl.piomin.samples.quarkus.serverless.order.model.OrderStatus;
import pl.piomin.samples.quarkus.serverless.order.repository.OrderRepository;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderConfirmFunctionTests {

    private OrderRepository repository;
    @RestClient
    @InjectMock
    OrderSender sender;

    public OrderConfirmFunctionTests(OrderRepository repository) {
        this.repository = repository;
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void confirm() {

        // first response -> IN_PROGRESS
        given().contentType("application/json").body(createTestOrder(2L, OrderStatus.IN_PROGRESS)).post("/confirm")
                .then()
                .statusCode(204);

        // second response -> CONFIRMED
        given().contentType("application/json").body(createTestOrder(2L, OrderStatus.IN_PROGRESS)).post("/confirm")
                .then()
                .statusCode(204);

        Order o = repository.findById(2L);
        assertEquals(OrderStatus.CONFIRMED, o.getStatus());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void reject() {
        given().contentType("application/json").body(createTestOrder(3L, OrderStatus.REJECTED)).post("/confirm")
                .then()
                .statusCode(204);

        Order o = repository.findById(3L);
        assertEquals(OrderStatus.REJECTED, o.getStatus());
        assertEquals("test", o.getRejectedService());
    }

    private Order createTestOrder(Long id, OrderStatus status) {
        Order o = new Order();
        o.setId(id);
        o.setSource("test");
        o.setStatus(status);
        o.setAmount(100);
        return o;
    }

}
