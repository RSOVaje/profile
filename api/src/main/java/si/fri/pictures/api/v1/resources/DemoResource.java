package si.fri.pictures.api.v1.resources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;
import com.kumuluz.ee.common.runtime.EeRuntime;
import com.kumuluz.ee.logs.cdi.Log;
import si.fri.pictures.api.v1.dtos.Health;
import si.fri.pictures.services.configuration.AppProperties;

@Path("demo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Log
public class DemoResource {

    private Logger log = Logger.getLogger(DemoResource.class.getName());

    @Inject
    private AppProperties appProperties;

    @POST
    @Path("healthy")
    public Response setHealth(Health health) {
        appProperties.setHealthy(health.getHealthy());
        log.info("Setting health to " + health.getHealthy());
        return Response.ok().build();
    }

}
