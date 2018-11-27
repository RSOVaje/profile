package si.fri.pictures.api.v1.resources;

import si.fri.pictures.models.dtos.Catalogue;
import si.fri.pictures.models.entities.Profile;
import si.fri.pictures.services.beans.ProfileBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;


@RequestScoped
@Path("/profile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProfileResource {

    @Inject
    private ProfileBean profileBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getPrfoiles() {
        List<Profile> profiles = profileBean.getProfiles();
        if (profiles == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(profiles).build();
    }

    @POST
    public Response createProfile(Profile profile) {


        profile = profileBean.createProfile(profile);

        if (profile.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(profile).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(profile).build();
        }
    }

    /*@GET
    @Path("catalogi/{idProfila}")
    public Response getCataloguesByPerson(@PathParam("idProfila") Integer idProfila) {
        List<Catalogue> list = profileBean.getCataloguesByPerson(idProfila);
        if (list == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(list).build();
    }*/

    @GET
    @Path("/{id}")
    public Response getPrfoileById(@PathParam("id") Integer id) {
        Profile profile = profileBean.getProfileById(id);
        if (profile == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(profile).build();
    }

}
