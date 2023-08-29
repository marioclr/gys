package gob.issste.gys.model;

public class Empleado {

	private String id_sociedad;
	private String id_empleado;
	private String nombre;
	private String apellido_1;
	private String apellido_2;
	private String id_legal;

	public Empleado() {
		
	}

	public Empleado(String id_empleado, String nombre, String apellido_1, String apellido_2, String id_legal) {
		super();
		this.id_sociedad = "01";
		this.id_empleado = id_empleado;
		this.nombre = nombre;
		this.apellido_1 = apellido_1;
		this.apellido_2 = apellido_2;
		this.id_legal = id_legal;
	}

	public Empleado(String nombre, String apellido_1, String apellido_2, String id_legal) {
		super();
		this.id_sociedad = "01";
		this.nombre = nombre;
		this.apellido_1 = apellido_1;
		this.apellido_2 = apellido_2;
		this.id_legal = id_legal;
	}

	public String getId_sociedad() {
		return id_sociedad;
	}

	public void setId_sociedad(String id_sociedad) {
		this.id_sociedad = id_sociedad;
	}

	public String getId_empleado() {
		return id_empleado;
	}

	public void setId_empleado(String id_empleado) {
		this.id_empleado = id_empleado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido_1() {
		return apellido_1;
	}

	public void setApellido_1(String apellido_1) {
		this.apellido_1 = apellido_1;
	}

	public String getApellido_2() {
		return apellido_2;
	}

	public void setApellido_2(String apellido_2) {
		this.apellido_2 = apellido_2;
	}

	public String getId_legal() {
		return id_legal;
	}

	public void setId_legal(String id_legal) {
		this.id_legal = id_legal;
	}

	@Override
	public String toString() {
		return "Empleado [id_sociedad=" + id_sociedad + ", id_empleado=" + id_empleado + ", nombre=" + nombre
				+ ", apellido_1=" + apellido_1 + ", apellido_2=" + apellido_2 + ", id_legal=" + id_legal + "]";
	}

}
