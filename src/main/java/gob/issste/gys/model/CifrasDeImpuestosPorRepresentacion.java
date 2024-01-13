package gob.issste.gys.model;

public class CifrasDeImpuestosPorRepresentacion {

	private String id_representacion;
	private String n_representacion;
	private String id_tipo_paga;
	private int anio_ejercicio;
	private int mes_ejercicio;
	private int id_ordinal;
	private int casos;
	private Double percepciones;
	private Double isr;
	private String fec_min;
	private String fec_max;

	public String getId_representacion() {
		return id_representacion;
	}
	public void setId_representacion(String id_representacion) {
		this.id_representacion = id_representacion;
	}
	public String getN_representacion() {
		return n_representacion;
	}
	public void setN_representacion(String n_representacion) {
		this.n_representacion = n_representacion;
	}
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
	public int getId_ordinal() {
		return id_ordinal;
	}
	public void setId_ordinal(int id_ordinal) {
		this.id_ordinal = id_ordinal;
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
	public String getFec_min() {
		return fec_min;
	}
	public void setFec_min(String fec_min) {
		this.fec_min = fec_min;
	}
	public String getFec_max() {
		return fec_max;
	}
	public void setFec_max(String fec_max) {
		this.fec_max = fec_max;
	}

}