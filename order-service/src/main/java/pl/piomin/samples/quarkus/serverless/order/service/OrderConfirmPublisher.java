package pl.piomin.samples.quarkus.serverless.order.service;

import io.smallrye.mutiny.Multi;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import pl.piomin.samples.quarkus.serverless.order.model.Order;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@ApplicationScoped
@Slf4j
public class OrderConfirmPublisher {

    private BlockingQueue<Order> queue = new LinkedBlockingQueue<>();

    public void send(Order order) {
        queue.offer(order);
    }

    @Outgoing("reserve-events")
    public Multi<Order> reserveEventsPublish() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .filter(it -> queue.peek() != null)
                .map(tick -> {
                    Order o = queue.poll();
                    log.info("Send order: {}", o);
                    return o;
                });
    }

}
