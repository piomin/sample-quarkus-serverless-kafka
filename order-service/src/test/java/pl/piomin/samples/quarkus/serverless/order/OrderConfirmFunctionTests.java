package pl.piomin.samples.quarkus.serverless.order;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import pl.piomin.samples.quarkus.serverless.order.client.OrderSender;
import pl.piomin.samples.quarkus.serverless.order.model.Order;
import pl.piomin.samples.quarkus.serverless.order.model.OrderStatus;
import pl.piomin.samples.quarkus.serverless.order.repository.OrderRepository;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class OrderConfirmFunctionTests {

    private OrderRepository repository;
    @RestClient
    @InjectMock
    OrderSender sender;

    public OrderConfirmFunctionTests(OrderRepository repository) {
        this.repository = repository;
    }

    @Test
    void confirm() {
        // first response -> IN_PROGRESS
        given().contentType("application/json").body(createTestOrder(1L, OrderStatus.IN_PROGRESS)).post("/confirm")
                .then()
                .statusCode(204);

        // second response -> CONFIRMED
        given().contentType("application/json").body(createTestOrder(1L, OrderStatus.IN_PROGRESS)).post("/confirm")
                .then()
                .statusCode(204);

        Order o = repository.findById(1L);
        assertEquals(OrderStatus.CONFIRMED, o.getStatus());
    }

    @Test
    void reject() {
        given().contentType("application/json").body(createTestOrder(2L, OrderStatus.REJECTED)).post("/confirm")
                .then()
                .statusCode(204);

        Order o = repository.findById(2L);
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
