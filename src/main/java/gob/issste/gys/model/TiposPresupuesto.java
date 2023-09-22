package gob.issste.gys.model;

public class TiposPresupuesto {
	private int id;
	private String clave;
	private String descripcion;

	public TiposPresupuesto() {
		super();
	}

	public TiposPresupuesto(String clave, String descripcion) {
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

	@Override
	public String toString() {
		return "TiposPresupuesto [clave=" + clave + ", descripcion=" + descripcion + "]";
	}
	
}