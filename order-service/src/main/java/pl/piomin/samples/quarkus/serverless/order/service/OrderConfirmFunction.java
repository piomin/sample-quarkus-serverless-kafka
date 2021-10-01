package pl.piomin.samples.quarkus.serverless.order.service;

import io.quarkus.funqy.Funq;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.order.model.Order;
import pl.piomin.samples.quarkus.serverless.order.repository.OrderRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;

public class OrderConfirmFunction {

    @Inject
    Logger log;
    @Inject
    OrderRepository repository;

    @Funq
    @Transactional
    public void confirm(Order order) {
        log.infof("Response order: %s", order);
        // TODO - finish implementation
    }

    private void doConfirm(Order o) {
        // TODO - finish implementation
    }
}
