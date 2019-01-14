package si.fri.pictures.models.dtos;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

public class Share {

    private Integer id;

    private Integer idProfila;

    private Integer idSProfila;

    private Integer idPicture;

    @Transient
    private Picture pictures;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdProfila() {
        return idProfila;
    }

    public void setIdProfila(Integer idProfila) {
        this.idProfila = idProfila;
    }

    public Integer getIdSProfila() {
        return idSProfila;
    }

    public void setIdSProfila(Integer idSProfila) {
        this.idSProfila = idSProfila;
    }

    public Integer getIdPicture() {
        return idPicture;
    }

    public void setIdPicture(Integer idPhoto) {
        this.idPicture = idPhoto;
    }

    public Picture getPictures() {
        return pictures;
    }

    public void setPictures(Picture pictures) {
        this.pictures = pictures;
    }

}
