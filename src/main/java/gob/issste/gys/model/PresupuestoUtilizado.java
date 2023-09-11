package gob.issste.gys.model;

public class PresupuestoUtilizado {

	private Presupuesto presupuesto;
	private Double saldoUtilizado;

	public PresupuestoUtilizado() {
		
	}

	public PresupuestoUtilizado(Presupuesto p) {
		this.presupuesto = p;
	}

	public Presupuesto getPresupuesto() {
		return presupuesto;
	}

	public void setPresupuesto(Presupuesto presupuesto) {
		this.presupuesto = presupuesto;
	}

	public Double getsaldoUtilizado() {
		return saldoUtilizado;
	}

	public void setSaldo(Double saldoUtilizado) {
		this.saldoUtilizado = saldoUtilizado;
	}

}
