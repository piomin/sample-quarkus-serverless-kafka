package pl.piomin.samples.quarkus.serverless.order.service;

import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.order.repository.OrderRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrderPublisher {

    private static long num = 0;

    @Inject
    Logger log;
    @Inject
    OrderRepository repository;

    // TODO - add method for generating and sending messages

}
