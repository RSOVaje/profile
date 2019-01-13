package si.fri.pictures.models.dtos;

public class Picture {

    private Integer id;

    private Byte[] pictureByte;

    private Integer idProfila;

    private String opis;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte[] getPictureByte() {
        return pictureByte;
    }

    public void setPictureByte(Byte[] pictureByte) {
        this.pictureByte = pictureByte;
    }

    public Integer getIdProfila() {
        return idProfila;
    }

    public void setIdProfila(Integer idProfila) {
        this.idProfila = idProfila;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

}
