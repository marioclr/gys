package gob.issste.gys.model;

public enum TipoDocumento {

	CEDULA_PROF("Cédula profesional", 1),
	CERTIFICADO_MED("Cértificado Médico", 2),
	CONSTANCIA_FISCAL("Constancia de Situación Fiscal", 3);

    private final String descripcion;
    private final int identificador;

    TipoDocumento (String desc, int id) {
    	this.identificador = id;
    	this.descripcion = desc;
    }

    public String getDesc() { return descripcion; }
    public int getId() { return identificador; }

}