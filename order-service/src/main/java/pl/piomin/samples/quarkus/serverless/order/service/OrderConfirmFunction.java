package pl.piomin.samples.quarkus.serverless.order.service;

import io.quarkus.funqy.Funq;
import org.eclipse.microprofile.rest.client.inject.RestClient;
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
    @RestClient
    OrderSender sender;

    @Funq
    public Order confirm(Order order) {
        log.infof("Accepted order: %s", order);
        Order o = orderService.doConfirm(order);
        if (o != null) {
            sender.send(o);
        }
        return o;
    }

}
