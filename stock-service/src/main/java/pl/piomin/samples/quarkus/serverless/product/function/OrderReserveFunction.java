package pl.piomin.samples.quarkus.serverless.product.function;

import io.quarkus.funqy.Funq;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.product.message.Order;
import pl.piomin.samples.quarkus.serverless.product.message.OrderStatus;
import pl.piomin.samples.quarkus.serverless.product.service.OrderConfirmService;
import pl.piomin.samples.quarkus.serverless.product.service.OrderReserveService;

public class OrderReserveFunction {

    private static final String SOURCE = "stock";

    private final OrderReserveService orderReserveService;
    private final OrderConfirmService orderConfirmService;
    private final Logger log;

    public OrderReserveFunction(OrderReserveService orderReserveService,
                                OrderConfirmService orderConfirmService,
                                Logger log) {
        this.orderReserveService = orderReserveService;
        this.orderConfirmService = orderConfirmService;
        this.log = log;
    }

    @Funq
    public void reserve(Order order) {
        log.infof("Received order: %s", order);
        if (order.getStatus() == OrderStatus.NEW) {
            orderReserveService.doReserve(order);
        } else {
            orderConfirmService.doConfirm(order);
        }
    }

}
