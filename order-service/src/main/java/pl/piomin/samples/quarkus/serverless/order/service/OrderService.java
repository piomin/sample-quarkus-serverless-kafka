package pl.piomin.samples.quarkus.serverless.order.service;

import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.order.model.Order;
import pl.piomin.samples.quarkus.serverless.order.model.OrderStatus;
import pl.piomin.samples.quarkus.serverless.order.repository.OrderRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrderService {

    @Inject
    Logger log;
    @Inject
    OrderRepository repository;

    @Transactional
    public Order doConfirm(Order o) {
        Order order = repository.findById(o.getId(), LockModeType.PESSIMISTIC_WRITE);
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
        return order;
    }
}
