package pl.piomin.samples.quarkus.serverless.product.function;

import io.quarkus.funqy.Funq;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.product.client.OrderSender;
import pl.piomin.samples.quarkus.serverless.product.message.Order;
import pl.piomin.samples.quarkus.serverless.product.message.OrderStatus;
import pl.piomin.samples.quarkus.serverless.product.model.Product;
import pl.piomin.samples.quarkus.serverless.product.service.OrderConfirmService;
import pl.piomin.samples.quarkus.serverless.product.service.OrderReserveService;

import javax.inject.Inject;

public class OrderReserveFunction {

    private static final String SOURCE = "stock";

    @Inject
    OrderReserveService orderReserveService;
    @Inject
    OrderConfirmService orderConfirmService;
    @Inject
    Logger log;
    @Inject
    OrderSender sender;

    @Funq
    public Product reserve(Order order) {
        log.infof("Received order: %s", order);
        if (order.getStatus() == OrderStatus.NEW) {
            Product p = orderReserveService.doReserve(order);
            sender.send(order);
            return p;
        } else {
            return orderConfirmService.doConfirm(order);
        }
    }

}
