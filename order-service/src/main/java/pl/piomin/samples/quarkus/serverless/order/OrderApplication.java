package pl.piomin.samples.quarkus.serverless.order;

import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.order.model.Order;
import pl.piomin.samples.quarkus.serverless.order.model.OrderStatus;
import pl.piomin.samples.quarkus.serverless.order.repository.OrderRepository;
import pl.piomin.samples.quarkus.serverless.order.service.OrderPublisher;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrderApplication {

    Logger log;
    OrderRepository repository;
    OrderPublisher publisher;

    public OrderApplication(Logger log, OrderRepository repository, OrderPublisher publisher) {
        this.log = log;
        this.repository = repository;
        this.publisher = publisher;
    }

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        for (int i = 0; i < 10; i++) {
            Order o = new Order(null, i%1000+1, i%100+1, 100, 1, OrderStatus.NEW);
            repository.persist(o);
        }
    }

    @Inject
    ManagedExecutor executor;
    @ConfigProperty(name = "app.orders.timeout", defaultValue = "10000")
    long timeout;

    void execute(@Observes StartupEvent ev) {
        executor.runAsync(() -> {
            while (true) {
               publisher.generate();
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
