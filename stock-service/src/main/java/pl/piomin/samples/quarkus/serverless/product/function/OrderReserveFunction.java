package pl.piomin.samples.quarkus.serverless.product.function;

import io.quarkus.funqy.Funq;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.product.client.OrderSender;
import pl.piomin.samples.quarkus.serverless.product.message.Order;
import pl.piomin.samples.quarkus.serverless.product.message.OrderStatus;
import pl.piomin.samples.quarkus.serverless.product.model.Product;
import pl.piomin.samples.quarkus.serverless.product.repository.ProductRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;

public class OrderReserveFunction {

    private static final String SOURCE = "stock";

    @Inject
    ProductRepository repository;
    @Inject
    Logger log;
    @Inject
    OrderSender sender;

    @Funq
//    @Transactional
    public Product reserve(Order order) {
        log.infof("Received order: %s", order);
        if (order.getStatus() == OrderStatus.NEW)
            return doReserve(order);
        else
            return doConfirm(order);
    }

    private Product doReserve(Order order) {
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
        return product;
    }

    private Product doConfirm(Order order) {
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
        return product;
    }
}
