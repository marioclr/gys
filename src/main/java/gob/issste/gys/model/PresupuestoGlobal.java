package gob.issste.gys.model;

public class PresupuestoGlobal {

	private int id;
	private int anio;
	private Delegacion delegacion;
	private Double saldo;
	private String comentarios;
	private String id_usuario;

	public PresupuestoGlobal() {
	}

	public PresupuestoGlobal(int anio, Delegacion delegacion, Double saldo, String comentarios, String id_usuario) {
		super();
		this.anio = anio;
		this.delegacion = delegacion;
		this.saldo = saldo;
		this.comentarios = comentarios;
		this.id_usuario = id_usuario;
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
	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	public String getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}

	@Override
	public String toString() {
		return "PresupuestoGlobal [anio=" + anio + ", delegacion=" + delegacion + ", saldo=" 
				+ saldo + ", comentarios=" + comentarios + "]";
	}

}