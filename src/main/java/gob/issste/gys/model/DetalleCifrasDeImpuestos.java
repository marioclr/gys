package gob.issste.gys.model;

public class DetalleCifrasDeImpuestos {

	private String id_tipo_paga;
	private int anio_ejercicio;
	private int mes_ejercicio;
	private String rfc;
	private int casos;
	private Double percepciones;
	private Double isr;
	private String fec_paga;
	private String id_clave_servicio;
	private String id_centro_trabajo;

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
	public String getFec_paga() {
		return fec_paga;
	}
	public void setFec_paga(String fec_paga) {
		this.fec_paga = fec_paga;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getId_clave_servicio() {
		return id_clave_servicio;
	}
	public void setId_clave_servicio(String id_clave_servicio) {
		this.id_clave_servicio = id_clave_servicio;
	}
	public String getId_centro_trabajo() {
		return id_centro_trabajo;
	}
	public void setId_centro_trabajo(String id_centro_trabajo) {
		this.id_centro_trabajo = id_centro_trabajo;
	}

}