package gob.issste.gys.model;

public class LayoutSpep {
	
	private String rfc;
	private String nombre;
	private String apellidoPat;
	private String apellidoMat;
	private String id_beneficiario;
	private Double percepciones;
	private Double isr;
	private Double pension_1;
	private Double pension_2;
	private Double pension_3;
	private Double pension_4;
	private String id_representacion;
	private String n_representacion;
	private String id_centro_trabajo;
	private String n_centro_trabajo;
	private String id_clave_servicio;
	private Double guardia;
	private String id_clave_servicio_g;
	private String id_puesto_plaza_g;
	private Double suplencia;
	private String id_clave_servicio_s;
	private String id_puesto_plaza_s;

	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidoPat() {
		return apellidoPat;
	}
	public void setApellidoPat(String apellidoPat) {
		this.apellidoPat = apellidoPat;
	}
	public String getApellidoMat() {
		return apellidoMat;
	}
	public void setApellidoMat(String apellidoMat) {
		this.apellidoMat = apellidoMat;
	}
	public String getId_beneficiario() {
		return id_beneficiario;
	}
	public void setId_beneficiario(String id_beneficiario) {
		this.id_beneficiario = id_beneficiario;
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
	public Double getPension_1() {
		return pension_1;
	}
	public void setPension_1(Double pension_1) {
		this.pension_1 = pension_1;
	}
	public Double getPension_2() {
		return pension_2;
	}
	public void setPension_2(Double pension_2) {
		this.pension_2 = pension_2;
	}
	public Double getPension_3() {
		return pension_3;
	}
	public void setPension_3(Double pension_3) {
		this.pension_3 = pension_3;
	}
	public Double getPension_4() {
		return pension_4;
	}
	public void setPension_4(Double pension_4) {
		this.pension_4 = pension_4;
	}
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
	public String getId_centro_trabajo() {
		return id_centro_trabajo;
	}
	public void setId_centro_trabajo(String id_centro_trabajo) {
		this.id_centro_trabajo = id_centro_trabajo;
	}
	public String getN_centro_trabajo() {
		return n_centro_trabajo;
	}
	public void setN_centro_trabajo(String n_centro_trabajo) {
		this.n_centro_trabajo = n_centro_trabajo;
	}
	public String getId_clave_servicio() {
		return id_clave_servicio;
	}
	public void setId_clave_servicio(String id_clave_servicio) {
		this.id_clave_servicio = id_clave_servicio;
	}
	public Double getGuardia() {
		return guardia;
	}
	public void setGuardia(Double guardia) {
		this.guardia = guardia;
	}
	public String getId_clave_servicio_g() {
		return id_clave_servicio_g;
	}
	public void setId_clave_servicio_g(String id_clave_servicio_g) {
		this.id_clave_servicio_g = id_clave_servicio_g;
	}
	public Double getSuplencia() {
		return suplencia;
	}
	public void setSuplencia(Double suplencia) {
		this.suplencia = suplencia;
	}
	public String getId_clave_servicio_s() {
		return id_clave_servicio_s;
	}
	public void setId_clave_servicio_s(String id_clave_servicio_s) {
		this.id_clave_servicio_s = id_clave_servicio_s;
	}
	public String getId_puesto_plaza_g() {
		return id_puesto_plaza_g;
	}
	public void setId_puesto_plaza_g(String id_puesto_plaza_g) {
		this.id_puesto_plaza_g = id_puesto_plaza_g;
	}
	public String getId_puesto_plaza_s() {
		return id_puesto_plaza_s;
	}
	public void setId_puesto_plaza_s(String id_puesto_plaza_s) {
		this.id_puesto_plaza_s = id_puesto_plaza_s;
	}
	@Override
	public String toString() {
		return "LayoutSpep [rfc=" + rfc + ", nombre=" + nombre + ", apellidoPat=" + apellidoPat + ", apellidoMat="
				+ apellidoMat + ", id_beneficiario=" + id_beneficiario + ", percepciones=" + percepciones + ", isr="
				+ isr + ", pension_1=" + pension_1 + ", pension_2=" + pension_2 + ", pension_3=" + pension_3
				+ ", pension_4=" + pension_4 + ", id_representacion=" + id_representacion + ", n_representacion="
				+ n_representacion + ", id_centro_trabajo=" + id_centro_trabajo + ", n_centro_trabajo="
				+ n_centro_trabajo + ", id_clave_servicio=" + id_clave_servicio + ", guardia=" + guardia
				+ ", suplencia=" + suplencia + "]";
	}

//	@Override
//	public String toString() {
//		return "LayoutSpep [rfc=" + rfc + ", nombre=" + nombre + ", apellidoPat=" + apellidoPat + ", apellidoMat="
//				+ apellidoMat + ", id_beneficiario=" + id_beneficiario + ", percepciones=" + percepciones + ", isr="
//				+ isr + ", pension_1=" + pension_1 + ", pension_2=" + pension_2 + ", pension_3=" + pension_3
//				+ ", pension_4=" + pension_4 + ", guardia=" + guardia + ", suplencia=" + suplencia + "]";
//	}

}