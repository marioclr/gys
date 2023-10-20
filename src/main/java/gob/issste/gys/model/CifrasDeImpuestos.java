package gob.issste.gys.model;

public class CifrasDeImpuestos {

	private String id_tipo_paga;
	private int anio_ejercicio;
	private int mes_ejercicio;
	private int casos;
	private Double percepciones;
	private Double isr;

	public String getId_tipo_paga() {
		return id_tipo_paga;
	}
	public void setId_tipo_paga(String id_tipo_paga) {
		this.id_tipo_paga = id_tipo_paga;
	}
	public int getAnio_ejercicio() {
		return anio_ejercicio;
	}
	public void setAnio_ejercicio(int anio_ejercicio) {
		this.anio_ejercicio = anio_ejercicio;
	}
	public int getMes_ejercicio() {
		return mes_ejercicio;
	}
	public void setMes_ejercicio(int mes_ejercicio) {
		this.mes_ejercicio = mes_ejercicio;
	}
	public int getCasos() {
		return casos;
	}
	public void setCasos(int casos) {
		this.casos = casos;
	}
	public Double getPercepciones() {
		return percepciones;
	}
	public void setPercepciones(Double percepciones) {
		this.percepciones = percepciones;
	}
	public Double getIsr() {
		return isr;
	}
	public void setIsr(Double isr) {
		this.isr = isr;
	}

}