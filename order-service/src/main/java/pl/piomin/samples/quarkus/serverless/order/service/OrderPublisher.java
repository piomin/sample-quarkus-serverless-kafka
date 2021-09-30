package pl.piomin.samples.quarkus.serverless.order.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
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
    private OrderRepository repository;
    @Inject
    private UserTransaction transaction;

    @Outgoing("order-events")
    @Broadcast
    public Multi<Order> orderEventsPublish() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .map(tick -> {
                    Order o = new Order(++num, (int) num%10+1, (int) num%10+1, 100, 1, OrderStatus.NEW);
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
