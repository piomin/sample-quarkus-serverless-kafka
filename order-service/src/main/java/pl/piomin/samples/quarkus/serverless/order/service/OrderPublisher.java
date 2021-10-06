package pl.piomin.samples.quarkus.serverless.order.service;

import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.order.model.Order;
import pl.piomin.samples.quarkus.serverless.order.repository.OrderRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Random;

@ApplicationScoped
public class OrderPublisher {

    private static long num = 0;

    private Random random = new Random();

    @Inject
    Logger log;
    @Inject
    OrderRepository repository;

    public Multi<Order> publisher() {
        // TODO - add method for generating and sending messages
        return null;
    }

}
