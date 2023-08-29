package gob.issste.gys.model;

import java.util.Date;

public class Guardia {

	private int idGuardia;
	private Empleado empleado;
	private String centroTrabajo;
	private String claveServicio;
	private String puesto;
	private String nivel;
	private String subNivel;
	private double tipoJornada;
	private double horas;
	private String tipoGuardia;
	private Date   inicio;
	private Date   fin;
	private String tipoTabulador;
	private String zona;
	private String riesgos;
	private String userName;
	private Date   quincena;
	private String folio;
	private String motivo;
	private String movimiento;
	private String coment;

	public Guardia() {
		super();
	}

	public Guardia(Empleado empleado, String centroTrabajo, String claveServicio, String puesto, String nivel,
			String subNivel, double tipoJornada, double horas, String tipoGuardia, Date inicio, Date fin,
			String tipoTabulador, String zona, String riesgos, String userName, Date quincena, String folio,
			String motivo, String movimiento, String coment) {
		super();
		this.empleado = empleado;
		this.centroTrabajo = centroTrabajo;
		this.claveServicio = claveServicio;
		this.puesto = puesto;
		this.nivel = nivel;
		this.subNivel = subNivel;
		this.tipoJornada = tipoJornada;
		this.horas = horas;
		this.tipoGuardia = tipoGuardia;
		this.inicio = inicio;
		this.fin = fin;
		this.tipoTabulador = tipoTabulador;
		this.zona = zona;
		this.riesgos = riesgos;
		this.userName = userName;
		this.quincena = quincena;
		this.folio = folio;
		this.motivo = motivo;
		this.movimiento = movimiento;
		this.coment = coment;
	}

	public int getIdGuardia() {
		return idGuardia;
	}

	public void setIdGuardia(int idGuardia) {
		this.idGuardia = idGuardia;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public String getCentroTrabajo() {
		return centroTrabajo;
	}

	public void setCentroTrabajo(String centroTrabajo) {
		this.centroTrabajo = centroTrabajo;
	}

	public String getClaveServicio() {
		return claveServicio;
	}

	public void setClaveServicio(String claveServicio) {
		this.claveServicio = claveServicio;
	}

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getSubNivel() {
		return subNivel;
	}

	public void setSubNivel(String subNivel) {
		this.subNivel = subNivel;
	}

	public double getTipoJornada() {
		return tipoJornada;
	}

	public void setTipoJornada(double tipoJornada) {
		this.tipoJornada = tipoJornada;
	}

	public double getHoras() {
		return horas;
	}

	public void setHoras(double horas) {
		this.horas = horas;
	}

	public String getTipoGuardia() {
		return tipoGuardia;
	}

	public void setTipoGuardia(String tipoGuardia) {
		this.tipoGuardia = tipoGuardia;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFin() {
		return fin;
	}

	public void setFin(Date fin) {
		this.fin = fin;
	}

	public String getTipoTabulador() {
		return tipoTabulador;
	}

	public void setTipoTabulador(String tipoTabulador) {
		this.tipoTabulador = tipoTabulador;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getRiesgos() {
		return riesgos;
	}

	public void setRiesgos(String riesgos) {
		this.riesgos = riesgos;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getQuincena() {
		return quincena;
	}

	public void setQuincena(Date quincena) {
		this.quincena = quincena;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(String movimiento) {
		this.movimiento = movimiento;
	}

	public String getComent() {
		return coment;
	}

	public void setComent(String coment) {
		this.coment = coment;
	}

	@Override
	public String toString() {
		return "Guardia [idGuardia=" + idGuardia + ", empleado=" + empleado + ", centroTrabajo=" + centroTrabajo
				+ ", claveServicio=" + claveServicio + ", puesto=" + puesto + ", nivel=" + nivel + ", subNivel="
				+ subNivel + ", tipoJornada=" + tipoJornada + ", horas=" + horas + ", tipoGuardia=" + tipoGuardia
				+ ", inicio=" + inicio + ", fin=" + fin + ", tipoTabulador=" + tipoTabulador + ", zona=" + zona
				+ ", riesgos=" + riesgos + ", userName=" + userName + ", quincena=" + quincena + ", folio=" + folio
				+ ", motivo=" + motivo + ", movimiento=" + movimiento + ", coment=" + coment + "]";
	}

}
