package si.fri.pictures.models.dtos;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.List;

public class Catalogue {

    private Integer id;

    @Transient
    private List<Picture> pictures;

    private Integer idProfila;


    private String opis;


    public Integer getId() {
        return id;
    }



    public List<Picture> getPictures() {
        return pictures;
    }

    public Integer getIdProfila() {
        return idProfila;
    }

    public String getOpis() {
        return opis;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public void setIdProfila(Integer idProfila) {
        this.idProfila = idProfila;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }
}
