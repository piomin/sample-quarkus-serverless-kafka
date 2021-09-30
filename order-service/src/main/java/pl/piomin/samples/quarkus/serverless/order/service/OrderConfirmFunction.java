package pl.piomin.samples.quarkus.serverless.order.service;

import io.quarkus.funqy.Funq;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.order.model.Order;
import pl.piomin.samples.quarkus.serverless.order.model.OrderStatus;
import pl.piomin.samples.quarkus.serverless.order.repository.OrderRepository;

import javax.inject.Inject;

public class OrderConfirmFunction {

    @Inject
    private Logger log;
    @Inject
    private OrderRepository repository;
    @Inject
    private OrderSender sender;

    @Funq
    public void confirm(Order order) {
        log.infof("Confirmed order: %s", order);
        doConfirm(order);
    }

    private void doConfirm(Order o) {
        Order order = repository.findById(o.getId());
        if (order.getStatus() == OrderStatus.NEW) {
            order.setStatus(o.getStatus());
            if (o.getStatus() == OrderStatus.REJECTED)
                order.setRejectedService(o.getSource());
        } else if (order.getStatus() == OrderStatus.IN_PROGRESS) {
            if (o.getStatus() == OrderStatus.REJECTED)
                order.setStatus(OrderStatus.ROLLBACK);
            else
                order.setStatus(OrderStatus.CONFIRMED);
            log.infof("Order confirmed: %s", order);
            sender.send(order);
        } else if (order.getStatus() == OrderStatus.REJECTED) {
            order.setStatus(OrderStatus.ROLLBACK);
            order.setRejectedService(o.getSource());
            log.infof("Order rejected: %s", order);
            sender.send(order);
        }
        repository.persist(order);
    }
}
