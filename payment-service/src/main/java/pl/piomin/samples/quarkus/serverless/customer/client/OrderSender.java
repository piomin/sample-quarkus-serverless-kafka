package pl.piomin.samples.quarkus.serverless.customer.client;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import pl.piomin.samples.quarkus.serverless.customer.message.Order;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrderSender {

    @Inject
    @Channel("reserve-events")
    Emitter<Order> emitter;

    public void send(Order order) {
        emitter.send(order);
    }

}
