package gob.issste.gys.model;

public class Beneficiario {

	private int id;
	private int idBolsa;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private int porcentaje;
	private String numeroBenef;
	private String id_centro_trab;
	private String id_usuario;

	public Beneficiario() {
		super();
	}

	public Beneficiario(int idBolsa, String nombre, String apellidoPaterno, String apellidoMaterno, int porcentaje,
			String numeroBenef, String id_centro_trab, String id_usuario) {
		super();
		this.idBolsa = idBolsa;
		this.nombre = nombre;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.porcentaje = porcentaje;
		this.numeroBenef = numeroBenef;
		this.id_centro_trab = id_centro_trab;
		this.id_usuario = id_usuario;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdBolsa() {
		return idBolsa;
	}

	public void setIdBolsa(int idBolsa) {
		this.idBolsa = idBolsa;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public int getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(int porcentaje) {
		this.porcentaje = porcentaje;
	}

	public String getNumeroBenef() {
		return numeroBenef;
	}

	public void setNumeroBenef(String numeroBenef) {
		this.numeroBenef = numeroBenef;
	}

	public String getId_centro_trab() {
		return id_centro_trab;
	}

	public void setId_centro_trab(String id_centro_trab) {
		this.id_centro_trab = id_centro_trab;
	}

	public String getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}

}
