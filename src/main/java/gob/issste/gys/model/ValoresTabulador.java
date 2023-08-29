package gob.issste.gys.model;

public class ValoresTabulador {

	private Double sueldo;
	private Double compen_g6;
	private Double compen_un;
	private Double asignacion;
	private Double ayuda;

	public ValoresTabulador() {
		
	}

	public ValoresTabulador(Double sueldo, Double compen_g6, Double compen_un, Double asignacion, Double ayuda) {
		super();
		this.sueldo = sueldo;
		this.compen_g6 = compen_g6;
		this.compen_un = compen_un;
		this.asignacion = asignacion;
		this.ayuda = ayuda;
	}

	public Double getSueldo() {
		return sueldo;
	}
	public void setSueldo(Double sueldo) {
		this.sueldo = sueldo;
	}
	public Double getCompen_g6() {
		return compen_g6;
	}
	public void setCompen_g6(Double compen_g6) {
		this.compen_g6 = compen_g6;
	}
	public Double getCompen_un() {
		return compen_un;
	}
	public void setCompen_un(Double compen_un) {
		this.compen_un = compen_un;
	}
	public Double getAsignacion() {
		return asignacion;
	}
	public void setAsignacion(Double asignacion) {
		this.asignacion = asignacion;
	}
	public Double getAyuda() {
		return ayuda;
	}
	public void setAyuda(Double ayuda) {
		this.ayuda = ayuda;
	}

	
}
