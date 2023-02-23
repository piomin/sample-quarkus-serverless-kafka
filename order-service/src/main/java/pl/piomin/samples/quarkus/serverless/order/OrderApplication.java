package pl.piomin.samples.quarkus.serverless.order;

import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.order.model.Order;
import pl.piomin.samples.quarkus.serverless.order.model.OrderStatus;
import pl.piomin.samples.quarkus.serverless.order.repository.OrderRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrderApplication {

    Logger log;
    OrderRepository repository;

    public OrderApplication(Logger log, OrderRepository repository) {
        this.log = log;
        this.repository = repository;
    }

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        for (int i = 0; i < 10; i++) {
            Order o = new Order(null, i%1000+1, i%100+1, 100, 1, OrderStatus.NEW);
            repository.persist(o);
        }
    }
}
