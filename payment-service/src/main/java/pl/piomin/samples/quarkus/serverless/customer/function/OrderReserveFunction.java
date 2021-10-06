package pl.piomin.samples.quarkus.serverless.customer.function;

import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.customer.exception.NotFoundException;
import pl.piomin.samples.quarkus.serverless.customer.message.Order;
import pl.piomin.samples.quarkus.serverless.customer.model.Customer;
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

    }

    private void doReserve(Order order) {
        Customer c = repository.findById(order.getCustomerId());
        if (c == null)
            throw new NotFoundException();
        log.infof("Customer found: %s", c);
    }

    private void doConfirm(Order order) {
        Customer c = repository.findById(order.getCustomerId());
        if (c == null)
            throw new NotFoundException();
        log.infof("Customer found: %s", c);
    }

}
