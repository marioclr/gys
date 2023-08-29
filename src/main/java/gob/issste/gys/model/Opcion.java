package gob.issste.gys.model;

public class Opcion {
	
	private int idOpcion;
	private String descripcion;
	private String componente;
	private int idNivelAcceso;
	private String id_usuario;
	
	public Opcion() {
		
	}

	public Opcion(int idOpcion, String descripcion, String componente, String id_usuario) {
		super();
		this.idOpcion = idOpcion;
		this.descripcion = descripcion;
		this.componente = componente;
		this.id_usuario = id_usuario;
	}

	public Opcion(String descripcion, String componente, String id_usuario) {
		super();
		this.descripcion = descripcion;
		this.componente = componente;
		this.id_usuario = id_usuario;
	}

	public int getIdOpcion() {
		return idOpcion;
	}

	public void setIdOpcion(int idOpcion) {
		this.idOpcion = idOpcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getComponente() {
		return componente;
	}

	public void setComponente(String componente) {
		this.componente = componente;
	}

	public int getIdNivelAcceso() {
		return idNivelAcceso;
	}

	public void setIdNivelAcceso(int idNivelAcceso) {
		this.idNivelAcceso = idNivelAcceso;
	}

	public String getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}

	@Override
	public String toString() {
		return "Opcion [idOpcion=" + idOpcion + ", descripcion=" + descripcion + ", componente=" + componente + "]";
	}

}
