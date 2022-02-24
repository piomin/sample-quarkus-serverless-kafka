package pl.piomin.samples.quarkus.serverless.order.service;

import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.order.model.Order;
import pl.piomin.samples.quarkus.serverless.order.model.OrderStatus;
import pl.piomin.samples.quarkus.serverless.order.repository.OrderRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import java.time.Duration;

@ApplicationScoped
public class OrderPublisher {

    private static long num = 0;

    @Inject
    Logger log;
    @Inject
    OrderRepository repository;
    @Inject
    UserTransaction transaction;
    @ConfigProperty(name = "app.orders.timeout", defaultValue = "10000")
    int timeout;


    public Multi<Order> orderEventsPublish() {
        return Multi.createFrom().ticks().every(Duration.ofMillis(timeout))
                .map(tick -> {
                    Order o = new Order(++num, (int) num%1000+1, (int) num%100+1, 100, 1, OrderStatus.NEW);
                    try {
                        transaction.begin();
                        repository.persist(o);
                        transaction.commit();
                    } catch (Exception e) {
                        log.error("Error in transaction", e);
                    }

                    log.infof("Order published: %s", o);
                    return o;
                });
    }

}
