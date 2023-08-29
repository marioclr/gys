package gob.issste.gys.model;

public class Paga {

	private int id;
	private String fec_pago;
	private String descripcion;
	private int estatus;
	private String fec_inicio;
	private String fec_fin;
	private int anio_ejercicio;
	private String id_usuario;

	public Paga() {
		super();
	}

	public Paga(String fec_pago, String descripcion, int estatus, String fec_inicio, String fec_fin, int anio_ejercicio) {
		super();
		this.fec_pago = fec_pago;
		this.descripcion = descripcion;
		this.estatus = estatus;
		this.fec_inicio = fec_inicio;
		this.fec_fin = fec_fin;
		this.anio_ejercicio = anio_ejercicio;
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
	public String getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}
}