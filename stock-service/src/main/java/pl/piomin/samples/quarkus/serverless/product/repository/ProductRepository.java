package pl.piomin.samples.quarkus.serverless.product.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import pl.piomin.samples.quarkus.serverless.product.model.Product;

public class ProductRepository implements PanacheRepository<Product> {
}
