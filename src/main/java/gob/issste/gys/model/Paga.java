package gob.issste.gys.model;

import java.util.List;

public class Paga {

	private int id;
	private String fec_pago;
	private String descripcion;
	private int estatus;
	private String fec_inicio;
	private String fec_fin;
	private int anio_ejercicio;
	private int mes_ejercicio;
	private int id_tipo_paga;
	private int idnivelvisibilidad;
	private String programas;
	private String id_usuario;
	private List<Delegacion> delegaciones;

	public Paga() {
		super();
	}

	public Paga(String fec_pago, String descripcion, int estatus, String fec_inicio, String fec_fin, 
			int anio_ejercicio, int mes_ejercicio, int id_tipo_paga, int nivel_visibilidad,
			String programas, String usuario, List<Delegacion> delegaciones) {
		super();
		this.fec_pago = fec_pago;
		this.descripcion = descripcion;
		this.estatus = estatus;
		this.fec_inicio = fec_inicio;
		this.fec_fin = fec_fin;
		this.anio_ejercicio = anio_ejercicio;
		this.mes_ejercicio = mes_ejercicio;
		this.id_tipo_paga = id_tipo_paga;
		this.idnivelvisibilidad = nivel_visibilidad;
		this.id_usuario = usuario;
		this.delegaciones = delegaciones;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFec_pago() {
		return fec_pago;
	}
	public void setFec_pago(String fec_paga) {
		this.fec_pago = fec_paga;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getEstatus() {
		return estatus;
	}
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}
	public String getFec_inicio() {
		return fec_inicio;
	}
	public void setFec_inicio(String fec_inicio) {
		this.fec_inicio = fec_inicio;
	}
	public String getFec_fin() {
		return fec_fin;
	}
	public void setFec_fin(String fec_fin) {
		this.fec_fin = fec_fin;
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
	public int getId_tipo_paga() {
		return id_tipo_paga;
	}
	public void setId_tipo_paga(int id_tipo_paga) {
		this.id_tipo_paga = id_tipo_paga;
	}
	public int getIdnivelvisibilidad() {
		return idnivelvisibilidad;
	}
	public void setIdnivelvisibilidad(int idnivelvisibilidad) {
		this.idnivelvisibilidad = idnivelvisibilidad;
	}
	public String getProgramas() {
		return programas;
	}
	public void setProgramas(String programas) {
		this.programas = programas;
	}
	public String getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}
	public List<Delegacion> getDelegaciones() {
		return delegaciones;
	}
	public void setDelegaciones(List<Delegacion> delegaciones) {
		this.delegaciones = delegaciones;
	}

}