package pl.piomin.samples.quarkus.serverless.customer.service;

import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.customer.exception.NotFoundException;
import pl.piomin.samples.quarkus.serverless.customer.message.Order;
import pl.piomin.samples.quarkus.serverless.customer.message.OrderStatus;
import pl.piomin.samples.quarkus.serverless.customer.model.Customer;
import pl.piomin.samples.quarkus.serverless.customer.repository.CustomerRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class OrderConfirmService {

    private static final String SOURCE = "payment";

    Logger log;
    CustomerRepository repository;

    public OrderConfirmService(Logger log, CustomerRepository repository) {
        this.log = log;
        this.repository = repository;
    }

    @Transactional
    public Customer doConfirm(Order order) {
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
