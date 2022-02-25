package pl.piomin.samples.quarkus.serverless.product;

import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.product.model.Product;
import pl.piomin.samples.quarkus.serverless.product.repository.ProductRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Random;

@ApplicationScoped
public class ProductApplication {

    private final Random r = new Random();

    @Inject
    Logger log;
    @Inject
    ProductRepository repository;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        for (long i = 0; i < 100; i++) {
            Product p = new Product(i, "Test" + i, r.nextInt(10000) + 100, 0);
            log.infof("Adding test data: %s", p);
            repository.persist(p);
        }
    }
}
