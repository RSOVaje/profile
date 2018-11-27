package si.fri.pictures.services.beans;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.pictures.models.dtos.Catalogue;
import si.fri.pictures.models.entities.Profile;
import si.fri.pictures.services.configuration.AppProperties;


import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RequestScoped
public class ProfileBean {

    private Logger log = Logger.getLogger(ProfileBean.class.getName());

    @Inject
    private EntityManager em;

    @Inject
    private AppProperties appProperties;

    @Inject
    private ProfileBean profileBean;

    private Client httpClient;
    private String baseUrl;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
        baseUrl = "http://192.168.99.100:8080"; // only for demonstration
    }

    public List<Profile> getProfiles() {

        TypedQuery<Profile> query = em.createNamedQuery("Profile.getAll", Profile.class);

        return query.getResultList();

    }

    public Profile getProfileById(Integer id) {

        /*TypedQuery<Profile> query = em.createNamedQuery("Profile.getById", Profile.class).setParameter("id", id);

        return query.getSingleResult();*/
        Profile profile = em.find(Profile.class, id);

        if (profile == null) {
            throw new NotFoundException();
        }

        List<Catalogue> catalogues = profileBean.getCatalogues(id);
        profile.setCatalogues(catalogues);

        return profile;

    }


    public List<Catalogue> getCatalogues(Integer profileId) {

        try {
            return httpClient
                    .target(baseUrl + "/v1/catalogue/profil/" + profileId)
                    .request().get(new GenericType<List<Catalogue>>() {
                    });
        } catch (WebApplicationException | ProcessingException e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException(e);
        }

    }

   /* public List<Catalogue> getCataloguesByPerson(Integer profileId) {

        try {
            return httpClient
                    .target(baseUrl + "/v1/catalogue?where=customerId:EQ:" + profileId)
                    .request().get(new GenericType<List<Catalogue>>() {
                    });
        } catch (WebApplicationException | ProcessingException e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException(e);
        }


    }*/

    public Profile createProfile(Profile profile) {

        try {
            beginTx();
            em.persist(profile);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return profile;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }


}
