package pl.piomin.samples.quarkus.serverless.product;

import io.quarkus.runtime.StartupEvent;
import pl.piomin.samples.quarkus.serverless.product.model.Product;
import pl.piomin.samples.quarkus.serverless.product.repository.ProductRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class ProductApplication {

    @Inject
    private ProductRepository repository;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        repository.persist(new Product(1L, "Test1", 10000, 0));
        repository.persist(new Product(2L, "Test2", 50000, 0));
        repository.persist(new Product(3L, "Test3", 20000, 0));
        repository.persist(new Product(4L, "Test4", 10000, 0));
        repository.persist(new Product(5L, "Test5", 15000, 0));
        repository.persist(new Product(6L, "Test6", 30000, 0));
        repository.persist(new Product(7L, "Test7", 10000, 0));
        repository.persist(new Product(8L, "Test8", 40000, 0));
        repository.persist(new Product(9L, "Test9", 50000, 0));
        repository.persist(new Product(10L, "Test10", 50000, 0));
    }
}
