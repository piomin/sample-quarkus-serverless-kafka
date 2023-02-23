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
public class OrderConfirmService {

    private static final String SOURCE = "stock";

    @Inject
    ProductRepository repository;
    @Inject
    Logger log;

    @Transactional
    public Product doConfirm(Order order) {
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
