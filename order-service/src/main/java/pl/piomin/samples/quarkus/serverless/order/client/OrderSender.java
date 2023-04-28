package pl.piomin.samples.quarkus.serverless.order.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pl.piomin.samples.quarkus.serverless.order.model.Order;

@ApplicationScoped
@RegisterRestClient
@RegisterClientHeaders(CloudEventHeadersFactory.class)
public interface OrderSender {

    @POST
    @Path("/order-sink")
    void send(Order order);

}
