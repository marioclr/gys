package gob.issste.gys.model;

public class DatosAdscripcion {

	private String clave;
	private String descripcion;
	private String tipo;
	private String zona;
	private String ur;
	private String ct;
	private String aux;

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
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
	}
	public String getUr() {
		return ur;
	}
	public void setUr(String ur) {
		this.ur = ur;
	}
	public String getCt() {
		return ct;
	}
	public void setCt(String ct) {
		this.ct = ct;
	}
	public String getAux() {
		return aux;
	}
	public void setAux(String aux) {
		this.aux = aux;
	}

	@Override
	public String toString() {
		return "DatosAdscripcion [clave=" + clave + ", descripcion=" + descripcion + ", tipo=" + tipo + ", zona=" + zona
				+ ", ur=" + ur + ", ct=" + ct + ", aux=" + aux + "]";
	}

}