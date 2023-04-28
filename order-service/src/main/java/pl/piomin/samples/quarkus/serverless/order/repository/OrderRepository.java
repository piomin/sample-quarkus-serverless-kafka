package pl.piomin.samples.quarkus.serverless.order.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import pl.piomin.samples.quarkus.serverless.order.model.Order;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<Order> {
}
