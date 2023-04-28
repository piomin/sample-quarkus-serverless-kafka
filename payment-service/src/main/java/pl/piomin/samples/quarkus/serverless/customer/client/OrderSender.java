package pl.piomin.samples.quarkus.serverless.customer.client;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pl.piomin.samples.quarkus.serverless.customer.message.Order;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@ApplicationScoped
@RegisterRestClient
@RegisterClientHeaders(CloudEventHeadersFactory.class)
public interface OrderSender {

    @POST
    @Path("/stock-sink")
    void send(Order order);

}
