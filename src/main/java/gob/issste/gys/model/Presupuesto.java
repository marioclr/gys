package gob.issste.gys.model;

import java.util.List;

public class Presupuesto {
	private int id;
	private int anio;
	private int mes;
	private Delegacion delegacion;
	private TiposPresupuesto tipoPresup;
	private Double saldo;
	private DatosAdscripcion centroTrabajo;
	private List<MovimientosPresupuesto> movimientos;

	public Presupuesto() {
	}
	public Presupuesto(int anio, int mes, Delegacion delegacion, DatosAdscripcion centroTrabajo, TiposPresupuesto tipoPresup, Double saldo) {
		super();
		this.anio = anio;
		this.mes = mes;
		this.delegacion = delegacion;
		this.centroTrabajo = centroTrabajo;
		this.tipoPresup = tipoPresup;
		this.saldo = saldo;
	}
	public Presupuesto(int id, int anio, int mes, Delegacion delegacion, DatosAdscripcion centroTrabajo, TiposPresupuesto tipoPresup, Double saldo) {
		super();
		this.id = id;
		this.anio = anio;
		this.mes = mes;
		this.delegacion = delegacion;
		this.centroTrabajo = centroTrabajo;
		this.tipoPresup = tipoPresup;
		this.saldo = saldo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAnio() {
		return anio;
	}
	public void setAnio(int anio) {
		this.anio = anio;
	}
	public int getMes() {
		return mes;
	}
	public void setMes(int mes) {
		this.mes = mes;
	}
	public Delegacion getDelegacion() {
		return delegacion;
	}
	public void setDelegacion(Delegacion delegacion) {
		this.delegacion = delegacion;
	}
	public DatosAdscripcion getCentroTrabajo() {
		return centroTrabajo;
	}
	public void setCentroTrabajo(DatosAdscripcion centroTrabajo) {
		this.centroTrabajo = centroTrabajo;
	}
	public TiposPresupuesto getTipoPresup() {
		return tipoPresup;
	}
	public void setTipoPresup(TiposPresupuesto tipoPresup) {
		this.tipoPresup = tipoPresup;
	}
	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	public List<MovimientosPresupuesto> getMovimientos() {
		return movimientos;
	}
	public void setMovimientos(List<MovimientosPresupuesto> movimientos) {
		this.movimientos = movimientos;
	}

	@Override
	public String toString() {
		return "Presupuesto [id=" + id + ", anio=" + anio + ", mes=" + mes + ", delegacion=" + delegacion + ", tipoPresup="
				+ tipoPresup + ", saldo=" + saldo + ", centroTrabajo=" + centroTrabajo + "]";
	}

}
