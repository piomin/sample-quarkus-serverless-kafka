package pl.piomin.samples.quarkus.serverless.customer;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.piomin.samples.quarkus.serverless.customer.message.Order;
import pl.piomin.samples.quarkus.serverless.customer.message.OrderStatus;
import pl.piomin.samples.quarkus.serverless.customer.model.Customer;
import pl.piomin.samples.quarkus.serverless.customer.repository.CustomerRepository;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderReserveFunctionTests {

    private static int reserved;
    private CustomerRepository repository;

    public OrderReserveFunctionTests(CustomerRepository repository) {
        this.repository = repository;
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void reserve() {
        Customer c = repository.findById(1L);
        given().contentType("application/json").body(createTestOrder(OrderStatus.NEW)).post("/reserve")
                .then()
                .statusCode(204);
        reserved = c.getAmountReserved();
//        assertEquals(reserved - 100, c.getAmountReserved());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void confirm() {
        Customer c = repository.findById(1L);
//        assertEquals(100, c.getAmountReserved());
        given().contentType("application/json").body(createTestOrder(OrderStatus.IN_PROGRESS)).post("/reserve")
                .then()
                .statusCode(204);

    }

    private Order createTestOrder(OrderStatus status) {
        Order o = new Order();
        o.setId(1L);
        o.setSource("test");
        o.setStatus(status);
        o.setAmount(100);
        o.setCustomerId(1L);
        return o;
    }
}
