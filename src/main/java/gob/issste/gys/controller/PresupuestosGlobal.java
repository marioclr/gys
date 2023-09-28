package gob.issste.gys.controller;

import java.util.List;

import gob.issste.gys.model.Presupuesto;

public class PresupuestosGlobal {

	List<Presupuesto> presupuestos;
	List<String> comentarios;

	public List<Presupuesto> getPresupuestos() {
		return presupuestos;
	}
	public void setPresupuestos(List<Presupuesto> presupuestos) {
		this.presupuestos = presupuestos;
	}
	public List<String> getComentarios() {
		return comentarios;
	}
	public void setComentarios(List<String> comentarios) {
		this.comentarios = comentarios;
	}

}