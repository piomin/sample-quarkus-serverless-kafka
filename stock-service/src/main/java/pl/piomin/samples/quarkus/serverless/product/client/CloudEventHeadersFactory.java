package pl.piomin.samples.quarkus.serverless.product.client;

import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class CloudEventHeadersFactory implements ClientHeadersFactory {

    AtomicLong id = new AtomicLong();

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incoming,
                                                 MultivaluedMap<String, String> outgoing) {
        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        result.add("Ce-Id", String.valueOf(id.incrementAndGet()));
        result.add("Ce-Specversion", "1.0");
        result.add("Ce-Type", "reserve-event");
        result.add("Ce-Source", "stock");
        return result;
    }

}
