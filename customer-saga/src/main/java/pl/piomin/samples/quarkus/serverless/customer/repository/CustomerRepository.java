package pl.piomin.samples.quarkus.serverless.customer.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import pl.piomin.samples.quarkus.serverless.customer.model.Customer;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {
}
