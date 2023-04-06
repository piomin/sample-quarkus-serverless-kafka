package pl.piomin.samples.quarkus.serverless.order.client;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pl.piomin.samples.quarkus.serverless.order.model.Order;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@ApplicationScoped
@RegisterRestClient
@RegisterClientHeaders(CloudEventHeadersFactory.class)
public interface OrderSender {

    @POST
    @Path("/order-sink")
    void send(Order order);

}
