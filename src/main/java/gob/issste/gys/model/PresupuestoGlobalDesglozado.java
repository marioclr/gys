package gob.issste.gys.model;

public class PresupuestoGlobalDesglozado {

	private PresupuestoGlobal presupuestoGlobal;
	private Double saldoDesglozado;

	public PresupuestoGlobalDesglozado() {
		super();
	}

	public PresupuestoGlobal getPresupuestoGlobal() {
		return presupuestoGlobal;
	}

	public void setPresupuestoGlobal(PresupuestoGlobal presupuestoGlobal) {
		this.presupuestoGlobal = presupuestoGlobal;
	}

	public Double getSaldoDesglozado() {
		return saldoDesglozado;
	}

	public void setSaldoDesglozado(Double saldoDesglozado) {
		this.saldoDesglozado = saldoDesglozado;
	}

}
