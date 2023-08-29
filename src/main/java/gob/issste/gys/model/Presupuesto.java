package gob.issste.gys.model;

import java.util.List;

public class Presupuesto {
	private int id;
	private int anio;
	private Delegacion delegacion;
	private TiposPresupuesto tipoPresup;
	private Double saldo;
	private DatosAdscripcion centroTrabajo;
	private List<MovimientosPresupuesto> movimientos;

	public Presupuesto() {
	}

	public Presupuesto(int anio, Delegacion delegacion, DatosAdscripcion centroTrabajo, TiposPresupuesto tipoPresup, Double saldo) {
		super();
		this.anio = anio;
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
}
