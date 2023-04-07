package pl.piomin.samples.quarkus.serverless.order.service;

import io.quarkus.funqy.Funq;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.order.model.Order;

public class OrderConfirmFunction {

    private final Logger log;
    private final OrderService orderService;

    public OrderConfirmFunction(Logger log, OrderService orderService) {
        this.log = log;
        this.orderService = orderService;
    }

    @Funq
    public void confirm(Order order) {
        log.infof("Accepted order: %s", order);
        orderService.doConfirm(order);
    }

}
