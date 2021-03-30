package pl.piomin.samples.quarkus.serverless.product.function;

import io.quarkus.funqy.Funq;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import pl.piomin.samples.quarkus.serverless.product.message.Order;
import pl.piomin.samples.quarkus.serverless.product.message.OrderStatus;
import pl.piomin.samples.quarkus.serverless.product.model.Product;
import pl.piomin.samples.quarkus.serverless.product.repository.ProductRepository;

import javax.inject.Inject;

@Slf4j
public class OrderReserveFunction {

    @Inject
    private ProductRepository repository;

    @Inject
    @Channel("reserve-events")
    Emitter<Order> orderEmitter;

    @Funq
    public void reserve(Order order) {
        log.info("Received order: {}", order);
        doReserve(order);
    }

    private void doReserve(Order order) {
        Product product = repository.findById(order.getProductId());
        log.info("Product: {}", product);
        if (order.getStatus() == OrderStatus.NEW) {
            product.setReservedItems(product.getReservedItems() + order.getProductsCount());
            product.setAvailableItems(product.getAvailableItems() - order.getProductsCount());
            order.setStatus(OrderStatus.IN_PROGRESS);
            orderEmitter.send(order);
        } else if (order.getStatus() == OrderStatus.CONFIRMED) {
            product.setReservedItems(product.getReservedItems() - order.getProductsCount());
        }
        repository.persist(product);
    }
}
