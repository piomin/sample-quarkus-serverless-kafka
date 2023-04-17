package pl.piomin.samples.quarkus.serverless.customer.client;

import pl.piomin.samples.quarkus.serverless.customer.message.Order;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface OrderSender {

    void send(Order order);

}
