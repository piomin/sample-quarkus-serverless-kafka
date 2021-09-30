package pl.piomin.samples.quarkus.serverless.product.service;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import pl.piomin.samples.quarkus.serverless.product.message.Order;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrderSender {

    @Inject
    @Channel("reserve-events")
    Emitter<Order> orderEmitter;

    public void send(Order order) {
        orderEmitter.send(order);
    }
}
