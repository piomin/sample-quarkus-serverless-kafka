package pl.piomin.samples.quarkus.serverless.order.service;

import org.eclipse.microprofile.reactive.messaging.Emitter;
import pl.piomin.samples.quarkus.serverless.order.model.Order;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderSender {


    Emitter<Order> emitter;

    public void send(Order order) {
        emitter.send(order);
    }
}
