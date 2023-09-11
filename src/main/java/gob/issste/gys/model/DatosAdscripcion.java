package gob.issste.gys.model;

import java.util.Objects;

public class DatosAdscripcion {

	private String clave;
	private String descripcion;
	private String tipo;
	private String zona;

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

	@Override
	public int hashCode() {
		return Objects.hashCode(clave);
	}
	@Override
	public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DatosAdscripcion adsc = (DatosAdscripcion) obj;
		return clave.equals(adsc.clave);
	}

}
