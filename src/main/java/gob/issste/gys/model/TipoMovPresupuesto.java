package gob.issste.gys.model;

public class TipoMovPresupuesto {
	private int id;
	private String clave;
	private String descripcion;

	public TipoMovPresupuesto() {
		super();
	}

	public TipoMovPresupuesto(String clave, String descripcion) {
		super();
		this.clave = clave;
		this.descripcion = descripcion;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}