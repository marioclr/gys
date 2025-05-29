package gob.issste.gys.model;

public class CifrasDeImpuestos {

	private String id_tipo_paga;
	private int anio_ejercicio;
	private int mes_ejercicio;
	private String id_delegacion;
	private String n_div_geografica;
	private int id_ordinal;
	private int casos;
	private Double percepciones;
	private Double isr;
	private String fec_min;
	private String fec_max;

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

	public String getId_delegacion() {
		return id_delegacion;
	}

	public void setId_delegacion(String id_delegacion) {
		this.id_delegacion = id_delegacion;
	}

	public String getN_div_geografica() {
		return n_div_geografica;
	}

	public void setN_div_geografica(String n_div_geografica) {
		this.n_div_geografica = n_div_geografica;
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