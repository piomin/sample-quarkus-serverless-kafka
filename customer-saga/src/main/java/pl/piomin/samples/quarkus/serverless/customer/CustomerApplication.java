package pl.piomin.samples.quarkus.serverless.customer;

import io.quarkus.runtime.StartupEvent;
import pl.piomin.samples.quarkus.serverless.customer.model.Customer;
import pl.piomin.samples.quarkus.serverless.customer.repository.CustomerRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class CustomerApplication {

    @Inject
    private CustomerRepository repository;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        repository.persist(new Customer(1L, "Test1", 10000, 0));
        repository.persist(new Customer(2L, "Test2", 50000, 0));
        repository.persist(new Customer(3L, "Test3", 20000, 0));
        repository.persist(new Customer(4L, "Test4", 10000, 0));
        repository.persist(new Customer(5L, "Test5", 15000, 0));
        repository.persist(new Customer(6L, "Test6", 30000, 0));
        repository.persist(new Customer(7L, "Test7", 10000, 0));
        repository.persist(new Customer(8L, "Test8", 40000, 0));
        repository.persist(new Customer(9L, "Test9", 50000, 0));
        repository.persist(new Customer(10L, "Test10", 50000, 0));
    }
}
