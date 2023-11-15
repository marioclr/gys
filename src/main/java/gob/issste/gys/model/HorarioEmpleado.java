package gob.issste.gys.model;

import java.util.Date;

public class HorarioEmpleado {

	private String id_empleado;
	private Date fec_pago;
	private Date fec_inicio;
	private Date fec_fin;
	private String id_horario;
	private String n_horario;
	private int hora_entrada_to;
	private int hora_salida_to;

	public String getId_empleado() {
		return id_empleado;
	}
	public void setId_empleado(String id_empleado) {
		this.id_empleado = id_empleado;
	}
	public Date getFec_pago() {
		return fec_pago;
	}
	public void setFec_pago(Date fec_pago) {
		this.fec_pago = fec_pago;
	}
	public Date getFec_inicio() {
		return fec_inicio;
	}
	public void setFec_inicio(Date fec_inicio) {
		this.fec_inicio = fec_inicio;
	}
	public Date getFec_fin() {
		return fec_fin;
	}
	public void setFec_fin(Date fec_fin) {
		this.fec_fin = fec_fin;
	}
	public String getId_horario() {
		return id_horario;
	}
	public void setId_horario(String id_horario) {
		this.id_horario = id_horario;
	}
	public String getN_horario() {
		return n_horario;
	}
	public void setN_horario(String n_horario) {
		this.n_horario = n_horario;
	}
	public int getHora_entrada_to() {
		return hora_entrada_to;
	}
	public void setHora_entrada_to(int hora_entrada_to) {
		this.hora_entrada_to = hora_entrada_to;
	}
	public int getHora_salida_to() {
		return hora_salida_to;
	}
	public void setHora_salida_to(int hora_salida_to) {
		this.hora_salida_to = hora_salida_to;
	}

}
