package pl.piomin.samples.quarkus.serverless.product.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import pl.piomin.samples.quarkus.serverless.product.model.Product;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
}
