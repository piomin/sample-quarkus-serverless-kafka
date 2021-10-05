package pl.piomin.samples.quarkus.serverless.customer.function;

import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.customer.message.Order;
import pl.piomin.samples.quarkus.serverless.customer.repository.CustomerRepository;

import javax.inject.Inject;

public class OrderReserveFunction {

    private static final String SOURCE = "payment";

    @Inject
    Logger log;
    @Inject
    CustomerRepository repository;

    public void reserve(Order order) {
        log.infof("Received order: %s", order);
        // TODO - finish implementation
    }

    private void doReserve(Order order) {
        // TODO - provide implementation
    }

    private void doConfirm(Order order) {
        // TODO - provide implementation
    }

}
