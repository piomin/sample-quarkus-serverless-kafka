package pl.piomin.samples.quarkus.serverless.order;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import pl.piomin.samples.quarkus.serverless.order.model.Order;
import pl.piomin.samples.quarkus.serverless.order.model.OrderStatus;
import pl.piomin.samples.quarkus.serverless.order.repository.OrderRepository;

import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class OrderConfirmFunctionTests {

    private OrderRepository repository;

    public OrderConfirmFunctionTests(OrderRepository repository) {
        this.repository = repository;
    }

    @Test
//    @Transactional
    void confirm() {
        // first response -> IN_PROGRESS
        Order o = given().contentType("application/json").body(createTestOrder(1L, OrderStatus.IN_PROGRESS)).post("/confirm")
                .then()
                .statusCode(200)
                .extract().body().as(Order.class);
        assertEquals(OrderStatus.IN_PROGRESS, o.getStatus());

        // second response -> CONFIRMED
        o = given().contentType("application/json").body(createTestOrder(1L, OrderStatus.IN_PROGRESS)).post("/confirm")
                .then()
                .statusCode(200)
                .extract().body().as(Order.class);
        assertEquals(OrderStatus.CONFIRMED, o.getStatus());

        o = repository.findById(1L);
        assertEquals(OrderStatus.CONFIRMED, o.getStatus());
    }

//    @Test
//    @Transactional
    void reject() {
        Order o = given().contentType("application/json").body(createTestOrder(2L, OrderStatus.REJECTED)).post("/confirm")
                .then()
                .statusCode(200)
                .extract().body().as(Order.class);
        assertEquals(OrderStatus.REJECTED, o.getStatus());

        o = repository.findById(1L);
        assertEquals(OrderStatus.REJECTED, o.getStatus());
        assertEquals("test", o.getRejectedService());
    }

    private Order createTestOrder(Long id, OrderStatus status) {
        Order o = new Order();
        // TODO - change to auto-increment
        o.setId(id);
        o.setSource("test");
        o.setStatus(status);
        o.setAmount(100);
//        repository.persist(o);
        return o;
    }
}
