package si.fri.pictures.models.entities;




import si.fri.pictures.models.dtos.Catalogue;

import javax.persistence.*;
import java.util.List;

@Entity(name = "profile")
@NamedQueries(value = {
        @NamedQuery(name = "Profile.getById", query = "SELECT p FROM profile p WHERE p.id = :id")
})
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Transient
    private List<Catalogue> catalogues;


    private String ime;
    private String priimek;
    private String mail;

    public Integer getId() {
        return id;
    }

    public List<Catalogue> getCatalogues() {
        return catalogues;
    }

    public String getIme() {
        return ime;
    }

    public String getPriimek() {
        return priimek;
    }

    public String getMail() {
        return mail;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCatalogues(List<Catalogue> catalogues) {
        this.catalogues = catalogues;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPriimek(String priimek) {
        this.priimek = priimek;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
