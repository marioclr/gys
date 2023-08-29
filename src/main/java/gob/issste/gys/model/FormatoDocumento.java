package gob.issste.gys.model;

public enum FormatoDocumento {

	IMAGEN_PNG("png", 1),
	IMAGEN_JPG("jpg", 2),
	EXCEL("excel", 3),
	WORD("word", 4),
	PDF("pdf", 5);

    private final String descripcion;
    private final int identificador;

    FormatoDocumento (String desc, int id) {
    	this.identificador = id;
    	this.descripcion = desc;
    }

    public String getDesc() { return descripcion; }
    public int getId() { return identificador; }

}
