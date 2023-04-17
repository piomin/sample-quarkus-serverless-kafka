package pl.piomin.samples.quarkus.serverless.customer.function;

import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.customer.message.Order;
import pl.piomin.samples.quarkus.serverless.customer.service.OrderConfirmService;
import pl.piomin.samples.quarkus.serverless.customer.service.OrderReserveService;

public class OrderReserveFunction {

    private static final String SOURCE = "payment";

    Logger log;
    OrderReserveService orderReserveService;
    OrderConfirmService orderConfirmService;

    public OrderReserveFunction(Logger log,
                                OrderReserveService orderReserveService,
                                OrderConfirmService orderConfirmService) {
        this.log = log;
        this.orderReserveService = orderReserveService;
        this.orderConfirmService = orderConfirmService;
    }

    public void reserve(Order order) {
        log.infof("Received order: %s", order);
    }

}
