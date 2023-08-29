package gob.issste.gys.model;

public class MovimientosPresupuesto {

	private int id;
	private int idPresup;
	private double importe;
	private String comentarios;
	private String fec_ult_actualizacion;
	private TipoMovPresupuesto tipMovPresup;

	public MovimientosPresupuesto() {
		super();
	}

	public MovimientosPresupuesto(int idPresup, double importe, String comentarios, int tipMovPresup) {
		this.idPresup = idPresup; 
		this.importe = importe;
		this.comentarios = comentarios;
		this.tipMovPresup = new TipoMovPresupuesto();
		this.tipMovPresup.setId(tipMovPresup);
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdPresup() {
		return idPresup;
	}
	public void setIdPresup(int idPresup) {
		this.idPresup = idPresup;
	}
	public double getImporte() {
		return importe;
	}
	public void setImporte(double importe) {
		this.importe = importe;
	}
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	public String getFec_ult_actualizacion() {
		return fec_ult_actualizacion;
	}
	public void setFec_ult_actualizacion(String fec_ult_actualizacion) {
		this.fec_ult_actualizacion = fec_ult_actualizacion;
	}
	public TipoMovPresupuesto getTipMovPresup() {
		return tipMovPresup;
	}
	public void setTipMovPresup(TipoMovPresupuesto tipMovPresup) {
		this.tipMovPresup = tipMovPresup;
	}
}