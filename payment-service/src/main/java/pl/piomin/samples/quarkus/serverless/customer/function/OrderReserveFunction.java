package pl.piomin.samples.quarkus.serverless.customer.function;

import io.quarkus.funqy.Funq;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.customer.client.OrderSender;
import pl.piomin.samples.quarkus.serverless.customer.exception.NotFoundException;
import pl.piomin.samples.quarkus.serverless.customer.message.Order;
import pl.piomin.samples.quarkus.serverless.customer.message.OrderStatus;
import pl.piomin.samples.quarkus.serverless.customer.model.Customer;
import pl.piomin.samples.quarkus.serverless.customer.repository.CustomerRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;

public class OrderReserveFunction {

    private static final String SOURCE = "payment";

    @Inject
    Logger log;
    @Inject
    CustomerRepository repository;
    @Inject
    OrderSender sender;

    @Funq
    public Customer reserve(Order order) {
        log.infof("Received order: %s", order);
        if (order.getStatus() == OrderStatus.NEW)
            return doReserve(order);
        else
            return doConfirm(order);
    }

    private Customer doReserve(Order order) {
        Customer customer = repository.findById(order.getCustomerId());
        if (customer == null)
            throw new NotFoundException();
        log.infof("Customer: %s", customer);
        if (order.getAmount() < customer.getAmountAvailable()) {
            order.setStatus(OrderStatus.IN_PROGRESS);
            customer.setAmountReserved(customer.getAmountReserved() + order.getAmount());
            customer.setAmountAvailable(customer.getAmountAvailable() - order.getAmount());
        } else {
            order.setStatus(OrderStatus.REJECTED);
        }
        order.setSource(SOURCE);
        repository.persist(customer);
        log.infof("Order reserved: %s", order);
        sender.send(order);
        return customer;
    }

    private Customer doConfirm(Order order) {
        Customer customer = repository.findById(order.getCustomerId());
        if (customer == null)
            throw new NotFoundException();
        log.infof("Customer: %s", customer);
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            customer.setAmountReserved(customer.getAmountReserved() - order.getAmount());
            repository.persist(customer);
        } else if (order.getStatus() == OrderStatus.ROLLBACK && !order.getRejectedService().equals(SOURCE)) {
            customer.setAmountReserved(customer.getAmountReserved() - order.getAmount());
            customer.setAmountAvailable(customer.getAmountAvailable() + order.getAmount());
            repository.persist(customer);
        }
        return customer;
    }
}
