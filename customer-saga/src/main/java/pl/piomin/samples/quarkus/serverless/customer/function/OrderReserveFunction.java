package pl.piomin.samples.quarkus.serverless.customer.function;

import io.quarkus.funqy.Funq;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import pl.piomin.samples.quarkus.serverless.customer.message.Order;
import pl.piomin.samples.quarkus.serverless.customer.message.OrderStatus;
import pl.piomin.samples.quarkus.serverless.customer.model.Customer;
import pl.piomin.samples.quarkus.serverless.customer.repository.CustomerRepository;
import pl.piomin.samples.quarkus.serverless.customer.service.OrderReservePublisher;

import javax.inject.Inject;

@Slf4j
public class OrderReserveFunction {

    @Inject
    private CustomerRepository repository;
    @Inject
    private OrderReservePublisher publisher;
    @Inject
    @Channel("reserve-events")
    Emitter<Order> orderEmitter;

    @Funq
    public void reserve(Order order) {
        log.info("Received order: {}", order);
        doReserve(order);
    }

    private void doReserve(Order order) {
        Customer customer = repository.findById(order.getCustomerId());
        log.info("Customer: {}", customer);
        if (order.getStatus() == OrderStatus.NEW) {
            customer.setAmountReserved(customer.getAmountReserved() + order.getAmount());
            customer.setAmountAvailable(customer.getAmountAvailable() - order.getAmount());
            order.setStatus(OrderStatus.IN_PROGRESS);
            log.info("Order reserved: {}", order);
//            publisher.send(order);
            orderEmitter.send(order);
        } else if (order.getStatus() == OrderStatus.CONFIRMED) {
            customer.setAmountReserved(customer.getAmountReserved() - order.getAmount());
        }
        repository.persist(customer);
    }
}
