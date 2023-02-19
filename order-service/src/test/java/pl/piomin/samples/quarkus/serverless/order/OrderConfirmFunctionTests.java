package pl.piomin.samples.quarkus.serverless.order;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.piomin.samples.quarkus.serverless.order.model.Order;
import pl.piomin.samples.quarkus.serverless.order.model.OrderStatus;
import pl.piomin.samples.quarkus.serverless.order.repository.OrderRepository;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestTransaction
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderConfirmFunctionTests {

    private OrderRepository repository;

    public OrderConfirmFunctionTests(OrderRepository repository) {
        this.repository = repository;
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void reserve() {
        Order c = repository.findById(1L);
        given().contentType("application/json").body(createTestOrder(201L, OrderStatus.NEW)).post("/confirm")
                .then()
                .statusCode(204);
//        reserved = c.getAmountReserved();
//        assertEquals(reserved - 100, c.getAmountReserved());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void confirm() {
        Order c = repository.findById(1L);
//        assertEquals(100, c.getAmountReserved());
        given().contentType("application/json").body(createTestOrder(202L, OrderStatus.IN_PROGRESS)).post("/confirm")
                .then()
                .statusCode(204);

    }

    private Order createTestOrder(Long id, OrderStatus status) {
        Order o = new Order();
        // TODO - change to auto-increment
        o.setId(id);
        o.setSource("test");
        o.setStatus(status);
        o.setAmount(100);
        repository.persist(o);
        return o;
    }
}
