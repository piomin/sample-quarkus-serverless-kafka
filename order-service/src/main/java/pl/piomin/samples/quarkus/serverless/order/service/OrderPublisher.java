package pl.piomin.samples.quarkus.serverless.order.service;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.order.client.OrderSender;
import pl.piomin.samples.quarkus.serverless.order.model.Order;
import pl.piomin.samples.quarkus.serverless.order.model.OrderStatus;
import pl.piomin.samples.quarkus.serverless.order.repository.OrderRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrderPublisher {

    private static long num = 0;

    private final Logger log;
    private final OrderRepository repository;
    private final OrderSender sender;

    public OrderPublisher(Logger log,
                          OrderRepository repository,
                          @RestClient OrderSender sender) {
        this.log = log;
        this.repository = repository;
        this.sender = sender;
    }

    @Transactional
    public void generate() {
        Order o = new Order(null, (int) ++num%1000+1, (int) num%100+1, 100, 1, OrderStatus.NEW);
        repository.persist(o);
        sender.send(o);
        log.infof("Order published: %s", o);
    }

}
