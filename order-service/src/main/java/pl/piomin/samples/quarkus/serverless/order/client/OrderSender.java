package pl.piomin.samples.quarkus.serverless.order.client;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import pl.piomin.samples.quarkus.serverless.order.model.Order;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrderSender {

    @Inject
    @Channel("order-events")
    Emitter<Order> emitter;

    public void send(Order order) {
        emitter.send(order);
    }

}
