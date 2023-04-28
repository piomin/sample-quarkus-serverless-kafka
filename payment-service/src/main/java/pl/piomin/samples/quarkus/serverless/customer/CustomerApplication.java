package pl.piomin.samples.quarkus.serverless.customer;

import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;
import pl.piomin.samples.quarkus.serverless.customer.model.Customer;
import pl.piomin.samples.quarkus.serverless.customer.repository.CustomerRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Random;

@ApplicationScoped
public class CustomerApplication {

    private final Random r = new Random();

    @Inject
    Logger log;
    @Inject
    CustomerRepository repository;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        for (long i = 0; i < 1000; i++) {
            Customer c = new Customer(i, "Test" + i, r.nextInt(100000) + 1000, 0);
            log.infof("Adding test data: %s", c);
            repository.persist(c);
        }
    }
}
