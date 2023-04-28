package pl.piomin.samples.quarkus.serverless.customer.service;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.customer.client.OrderSender;
import pl.piomin.samples.quarkus.serverless.customer.exception.NotFoundException;
import pl.piomin.samples.quarkus.serverless.customer.message.Order;
import pl.piomin.samples.quarkus.serverless.customer.message.OrderStatus;
import pl.piomin.samples.quarkus.serverless.customer.model.Customer;
import pl.piomin.samples.quarkus.serverless.customer.repository.CustomerRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class OrderReserveService {

    private static final String SOURCE = "payment";

    Logger log;
    CustomerRepository repository;
    OrderSender sender;

    public OrderReserveService(Logger log,
                               CustomerRepository repository,
                               @RestClient OrderSender sender) {
        this.log = log;
        this.repository = repository;
        this.sender = sender;
    }

    @Transactional
    public Customer doReserve(Order order) {
        Customer customer = repository.findById(order.getCustomerId(), LockModeType.PESSIMISTIC_WRITE);
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
}
