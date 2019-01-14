package si.fri.pictures.services.beans;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Timed;
import si.fri.pictures.models.dtos.Catalogue;
import si.fri.pictures.models.dtos.Picture;
import si.fri.pictures.models.dtos.Share;
import si.fri.pictures.models.entities.Profile;
import si.fri.pictures.services.configuration.AppProperties;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class ProfileBean {

    private Logger log = Logger.getLogger(ProfileBean.class.getName());

    @Inject
    private EntityManager em;

    @Inject
    private AppProperties appProperties;

    @Inject
    private ProfileBean profileBean;

    private Client httpClient;

    @Inject
    @DiscoverService("catalogue")
    private Optional<String> catalogueUrl;

    @Inject
    @DiscoverService("picture")
    private Optional<String> pictureUrl;

    @Inject
    @DiscoverService("share")
    private Optional<String> shareUrl;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
        //baseUrl = "http://159.122.177.26:30304"; // only for demonstration
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

        List<Picture> pictures = profileBean.getPicture(id);
        profile.setPictures(pictures);

        return profile;

    }

    @Timed
    @CircuitBreaker(requestVolumeThreshold = 3)
    @Timeout(value = 2, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "getCataloguesFallback")
    public List<Catalogue> getCatalogues(Integer profileId) {
        if(appProperties.isExternalServicesEnabled() && catalogueUrl.isPresent()) {
            try {
                return httpClient
                        .target(catalogueUrl.get() + "/v1/catalogue/profil/" + profileId)
                        .request().get(new GenericType<List<Catalogue>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.severe(e.getMessage());
                throw new InternalServerErrorException(e);
            }
        }
        return null;
    }
    public List<Catalogue> getCataloguesFallback(Integer profileId) {
        return Collections.emptyList();
    }


    public List<Picture> getPicture(Integer profileId) {
        if(appProperties.isExternalServicesEnabled() && pictureUrl.isPresent()) {
            try {
                return httpClient
                        .target(pictureUrl.get() + "/v1/picture/profil/" + profileId)
                        .request().get(new GenericType<List<Picture>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.severe(e.getMessage());
                throw new InternalServerErrorException(e);
            }
        }
        return null;
    }

    public List<Share> getSharesOfProfile(Integer id) {
        if(appProperties.isExternalServicesEnabled() && shareUrl.isPresent()) {
            try {
                return httpClient
                        .target(shareUrl.get() + "/v1/share/profil/" + id)
                        .request().get(new GenericType<List<Share>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.severe(e.getMessage());
                throw new InternalServerErrorException(e);
            }
        }
        return null;
    }

    public List<Share> getAllowedPhotosOfProfile(Integer id) {
        if(appProperties.isExternalServicesEnabled() && shareUrl.isPresent()) {
            try {
                return httpClient
                        .target(shareUrl.get() + "/v1/share/allowed/" + id)
                        .request().get(new GenericType<List<Share>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.severe(e.getMessage());
                throw new InternalServerErrorException(e);
            }
        }
        return null;
    }

    public Share sharePhoto(Share share) {

        if(appProperties.isExternalServicesEnabled() && shareUrl.isPresent()) {
            try {
                return httpClient
                        .target(shareUrl.get() + "/v1/share")
                        .request().post(Entity.json(share), Share.class);
            } catch (WebApplicationException | ProcessingException e) {
                log.severe(e.getMessage());
                throw new InternalServerErrorException(e);
            }
        }
        return null;
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
            log.warning("Tezava z dodajanjem profila z id " + profile.getId());
            rollbackTx();
        }
        log.info("Uspesno dodan profil z id " + profile.getId());
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
