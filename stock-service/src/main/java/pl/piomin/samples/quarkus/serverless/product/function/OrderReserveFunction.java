package pl.piomin.samples.quarkus.serverless.product.function;

import io.quarkus.funqy.Funq;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.product.message.Order;
import pl.piomin.samples.quarkus.serverless.product.message.OrderStatus;
import pl.piomin.samples.quarkus.serverless.product.model.Product;
import pl.piomin.samples.quarkus.serverless.product.repository.ProductRepository;
import pl.piomin.samples.quarkus.serverless.product.service.OrderSender;

import javax.inject.Inject;

public class OrderReserveFunction {

    private static final String SOURCE = "stock";

    @Inject
    ProductRepository repository;
    @Inject
    Logger log;

    @Inject
    OrderSender sender;

    @Funq
    public void reserve(Order order) {
        log.infof("Received order: %s", order);
        if (order.getStatus() == OrderStatus.NEW)
            doReserve(order);
        else
            doConfirm(order);
    }

    private void doReserve(Order order) {
        Product product = repository.findById(order.getProductId());
        log.infof("Product: %s", product);
        if (order.getStatus() == OrderStatus.NEW) {
            if (order.getProductsCount() < product.getAvailableItems()) {
                product.setReservedItems(product.getReservedItems() + order.getProductsCount());
                product.setAvailableItems(product.getAvailableItems() - order.getProductsCount());
                order.setStatus(OrderStatus.IN_PROGRESS);
                repository.persist(product);
            } else {
                order.setStatus(OrderStatus.REJECTED);
            }
            order.setSource(SOURCE);
            log.infof("Order reserved: %s", order);
            sender.send(order);
        }
    }

    private void doConfirm(Order order) {
        Product product = repository.findById(order.getProductId());
        log.infof("Product: %s", product);
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            product.setReservedItems(product.getReservedItems() - order.getProductsCount());
            repository.persist(product);
        } else if (order.getStatus() == OrderStatus.ROLLBACK && !order.getRejectedService().equals(SOURCE)) {
            product.setReservedItems(product.getReservedItems() - order.getProductsCount());
            product.setAvailableItems(product.getAvailableItems() + order.getProductsCount());
            repository.persist(product);
        }
    }
}
