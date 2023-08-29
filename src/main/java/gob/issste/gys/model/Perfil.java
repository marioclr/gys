package gob.issste.gys.model;

import java.util.List;

public class Perfil {
	
	private int idPerfil;
	private String descripcion;
	private List<Opcion> opciones;
	private String id_usuario;

	public Perfil() {
		
	}

	public Perfil(int idPerfil, String descripcion, String id_usuario) {
		super();
		this.idPerfil = idPerfil;
		this.descripcion = descripcion;
		this.id_usuario = id_usuario;
	}

	public Perfil(String descripcion, String id_usuario) {
		super();
		this.descripcion = descripcion;
		this.id_usuario = id_usuario;
	}

	public int getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(int idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Opcion> getOpciones() {
		return opciones;
	}

	public void setOpciones(List<Opcion> opciones) {
		this.opciones = opciones;
	}

	public String getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}

	@Override
	public String toString() {
		return "Perfil [idPerfil=" + idPerfil + ", descripcion=" + descripcion + "]";
	}

}
