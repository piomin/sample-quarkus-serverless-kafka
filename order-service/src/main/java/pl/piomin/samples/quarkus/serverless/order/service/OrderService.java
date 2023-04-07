package pl.piomin.samples.quarkus.serverless.order.service;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.order.client.OrderSender;
import pl.piomin.samples.quarkus.serverless.order.model.Order;
import pl.piomin.samples.quarkus.serverless.order.model.OrderStatus;
import pl.piomin.samples.quarkus.serverless.order.repository.OrderRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrderService {

    private final Logger log;
    private final OrderRepository repository;
    private final OrderSender sender;

    public OrderService(Logger log, OrderRepository repository, @RestClient OrderSender sender) {
        this.log = log;
        this.repository = repository;
        this.sender = sender;
    }

    @Transactional
    public Order doConfirm(Order o) {
        Order order = repository.findById(o.getId(), LockModeType.PESSIMISTIC_WRITE);
        log.infof("Order found: %s", order);
        if (order == null) {
            return null;
        } else if (order.getStatus() == OrderStatus.NEW) {
            order.setStatus(o.getStatus());
            if (o.getStatus() == OrderStatus.REJECTED)
                order.setRejectedService(o.getSource());
//            return null;
        } else if (order.getStatus() == OrderStatus.IN_PROGRESS) {
            if (o.getStatus() == OrderStatus.REJECTED)
                order.setStatus(OrderStatus.ROLLBACK);
            else
                order.setStatus(OrderStatus.CONFIRMED);
            log.infof("Order confirmed: %s", order);
        } else if (order.getStatus() == OrderStatus.REJECTED) {
            order.setStatus(OrderStatus.ROLLBACK);
            order.setRejectedService(o.getSource());
            log.infof("Order rejected: %s", order);
        }
        repository.persist(order);
        sender.send(order);
        return order;
    }
}
