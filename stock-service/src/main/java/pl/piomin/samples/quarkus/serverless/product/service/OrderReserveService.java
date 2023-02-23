package pl.piomin.samples.quarkus.serverless.product.service;

import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.product.message.Order;
import pl.piomin.samples.quarkus.serverless.product.message.OrderStatus;
import pl.piomin.samples.quarkus.serverless.product.model.Product;
import pl.piomin.samples.quarkus.serverless.product.repository.ProductRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrderReserveService {

    private static final String SOURCE = "stock";

    @Inject
    ProductRepository repository;
    @Inject
    Logger log;

    @Transactional
    public Product doReserve(Order order) {
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
//            sender.send(order);
        }
        return product;
    }
}
