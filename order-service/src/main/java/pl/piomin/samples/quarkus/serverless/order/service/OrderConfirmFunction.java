package pl.piomin.samples.quarkus.serverless.order.service;

import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.order.client.OrderSender;
import pl.piomin.samples.quarkus.serverless.order.model.Order;

import javax.inject.Inject;

public class OrderConfirmFunction {

    @Inject
    Logger log;
    @Inject
    OrderService orderService;
    @Inject
    OrderSender sender;

    public void confirm(Order order) {
        log.infof("Response order: %s", order);

    }

}
