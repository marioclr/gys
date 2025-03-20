package gob.issste.gys.model;

import java.util.List;

public class DelegacionPorFecha {

    private String id_div_geografica;
    private String n_div_geografica;
    private int estatus;
    List<DatosAdscripcion> centrosTrabajo;

    public String getId_div_geografica() {
        return id_div_geografica;
    }
    public void setId_div_geografica(String id_div_geografica) {
        this.id_div_geografica = id_div_geografica;
    }
    public String getN_div_geografica() {
        return n_div_geografica;
    }
    public void setN_div_geografica(String n_div_geografica) {
        this.n_div_geografica = n_div_geografica;
    }

    public int getEstatus(){
        return estatus;
    }
    public void setEstatus(int estatus){
        this.estatus = estatus;
    }
    public List<DatosAdscripcion> getCentrosTrabajo() {
        return centrosTrabajo;
    }
    public void setCentrosTrabajo(List<DatosAdscripcion> centrosTrabajo) {
        this.centrosTrabajo = centrosTrabajo;
    }

    @Override
    public String toString() {
        return "DatosDelegacionPorFecha{" +
                "id_div_geografica='" + id_div_geografica + '\'' +
                ", n_div_geografica='" + n_div_geografica + '\'' +
                ", estatus=" + estatus +
                ", centrosTrabajo=" + centrosTrabajo +
                '}';
    }
}
