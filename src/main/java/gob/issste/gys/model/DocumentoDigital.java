package gob.issste.gys.model;

public class DocumentoDigital {

	private int id;
	private int id_asociado;
	private byte[] imagen;
	private TipoDocumento tipoDoc;
	private FormatoDocumento formato;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_asociado() {
		return id_asociado;
	}

	public void setId_asociado(int id_asociado) {
		this.id_asociado = id_asociado;
	}

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public TipoDocumento getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(TipoDocumento tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public FormatoDocumento getFormato() {
		return formato;
	}

	public void setFormato(FormatoDocumento formato) {
		this.formato = formato;
	}

}
