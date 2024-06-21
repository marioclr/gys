package gob.issste.gys.model;

import java.util.List;

public class Usuario {

	private int idUsuario;
	private String clave;
	private String password;
	private Empleado empleado;
	private List<Perfil> perfiles;
	private Delegacion delegacion;
	private List<DatosAdscripcion> centrosTrabajo;
	private NivelVisibilidad nivelVisibilidad;
	private int idTipoUsuario;
	private String sistema;
	private String id_usuario;

	public Usuario() {

	}

	public Usuario(int idUsuario, String clave, String password, Empleado empleado, String id_usuario) {
		super();
		this.idUsuario = idUsuario;
		this.clave = clave;
		this.password = password;
		this.empleado = empleado;
		this.id_usuario = id_usuario;
	}

	public Usuario(String clave, String password, Empleado empleado, Delegacion delegacion,
				   List<DatosAdscripcion> centrosTrabajo, NivelVisibilidad nivelVisibilidad,
			int idTipoUsuario, String sistema, String id_usuario) {
		super();
		this.clave = clave;
		this.password = password;
		this.empleado = empleado;
		this.delegacion = delegacion;
		this.centrosTrabajo = centrosTrabajo;
		this.nivelVisibilidad = nivelVisibilidad;
		this.idTipoUsuario = idTipoUsuario;
		this.sistema = sistema;
		this.id_usuario = id_usuario;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public List<Perfil> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(List<Perfil> perfiles) {
		this.perfiles = perfiles;
	}

	public Delegacion getDelegacion() {
		return delegacion;
	}

	public void setDelegacion(Delegacion delegacion) {
		this.delegacion = delegacion;
	}

	public List<DatosAdscripcion> getCentrosTrabajo() {
		return centrosTrabajo;
	}

	public void setCentrosTrabajo(List<DatosAdscripcion> centrosTrabajo) {
		this.centrosTrabajo = centrosTrabajo;
	}

	public NivelVisibilidad getNivelVisibilidad() {
		return nivelVisibilidad;
	}

	public void setNivelVisibilidad(NivelVisibilidad nivelVisibilidad) {
		this.nivelVisibilidad = nivelVisibilidad;
	}

	public int getIdTipoUsuario() {
		return idTipoUsuario;
	}

	public void setIdTipoUsuario(int idTipoUsuario) {
		this.idTipoUsuario = idTipoUsuario;
	}
	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public String getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}

	@Override
	public String toString() {
		return "Usuario [idUsuario=" + idUsuario + ", clave=" + clave + ", password=" + password + ", empleado="
				+ empleado + ", perfiles=" + perfiles + "]";
	}

	public boolean contieneCT(String centroTrab) {
		boolean encontrado = false;
		for (int x = 0; x < centrosTrabajo.size(); x++) {
		  DatosAdscripcion adsc = centrosTrabajo.get(x);
		  if (adsc.getClave().equals(centroTrab)) {
		    encontrado = true;
		    break;
		  }
		}
		return encontrado;
	}
}