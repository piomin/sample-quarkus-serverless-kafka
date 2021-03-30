package pl.piomin.samples.quarkus.serverless.order.service;

import io.quarkus.funqy.Funq;
import lombok.extern.slf4j.Slf4j;
import pl.piomin.samples.quarkus.serverless.order.model.Order;
import pl.piomin.samples.quarkus.serverless.order.model.OrderStatus;
import pl.piomin.samples.quarkus.serverless.order.repository.OrderRepository;

import javax.inject.Inject;

@Slf4j
public class OrderConfirmFunction {

    @Inject
    private OrderRepository repository;
    @Inject
    private OrderConfirmPublisher publisher;

    @Funq
    public void confirm(Order order) {
        log.info("Confirmed order: {}", order);
        doConfirm(order);
    }

    private void doConfirm(Order o) {
        Order order = repository.findById(o.getId());
        if (order.getStatus() == OrderStatus.NEW) {
            order.setStatus(OrderStatus.IN_PROGRESS);
        } else if (order.getStatus() == OrderStatus.IN_PROGRESS) {
            order.setStatus(OrderStatus.CONFIRMED);
            log.info("Order confirmed : {}", order);
            publisher.send(order);
        }
        repository.persist(order);
    }
}
