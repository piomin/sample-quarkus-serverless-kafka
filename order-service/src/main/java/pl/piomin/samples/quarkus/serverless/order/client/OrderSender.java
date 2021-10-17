package pl.piomin.samples.quarkus.serverless.order.client;

import org.eclipse.microprofile.reactive.messaging.Emitter;
import pl.piomin.samples.quarkus.serverless.order.model.Order;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrderSender {

    @Inject
    Emitter<Order> emitter;

    public void send(Order order) {
        emitter.send(order);
    }
}
