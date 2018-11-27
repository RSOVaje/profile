package si.fri.pictures.models.dtos;

public class Catalogue {

    private Integer id;

    private Byte[] picture;

    private Integer idProfila;

    private String opis;


    public Integer getId() {
        return id;
    }
    public Byte[] getPicture() {
        return picture;
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

    public void setPicture(Byte[] picture) {
        this.picture = picture;
    }

    public void setIdProfila(Integer idProfila) {
        idProfila = idProfila;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }
}
