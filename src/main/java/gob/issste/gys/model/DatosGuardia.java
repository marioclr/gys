package gob.issste.gys.model;

public class DatosGuardia {

	private Integer id; // 1
	private String clave_empleado;
	private String id_centro_trabajo;
	private String id_clave_servicio;
	private String id_puesto_plaza;
	private String id_nivel;
	private String id_sub_nivel;
	private String id_tipo_jornada;
	private Double horas;
	private String fec_inicio; // 10
	private String fec_fin;
	private Double importe;
	private String id_tipo_tabulador;
	private String fec_paga;
	private String id_zona;
	private Integer riesgos;
	private String estatus;
	private String id_ordinal; // 18
	private String tipo_guardia;
	// Extra Info
	private String id_usuario;
	private String folio;
	private String motivo;
	private String id_clave_movimiento;
	private String coment;
	private String id_empresa;
	private Integer hora_inicio;
	private Integer hora_fin;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getClave_empleado() {
		return clave_empleado;
	}
	public void setClave_empleado(String clave_empleado) {
		this.clave_empleado = clave_empleado;
	}
	public String getId_centro_trabajo() {
		return id_centro_trabajo;
	}
	public void setId_centro_trabajo(String id_centro_trabajo) {
		this.id_centro_trabajo = id_centro_trabajo;
	}
	public String getId_clave_servicio() {
		return id_clave_servicio;
	}
	public void setId_clave_servicio(String id_clave_servicio) {
		this.id_clave_servicio = id_clave_servicio;
	}
	public String getId_puesto_plaza() {
		return id_puesto_plaza;
	}
	public void setId_puesto_plaza(String id_puesto_plaza) {
		this.id_puesto_plaza = id_puesto_plaza;
	}
	public String getId_nivel() {
		return id_nivel;
	}
	public void setId_nivel(String id_nivel) {
		this.id_nivel = id_nivel;
	}
	public String getId_sub_nivel() {
		return id_sub_nivel;
	}
	public void setId_sub_nivel(String id_sub_nivel) {
		this.id_sub_nivel = id_sub_nivel;
	}
	public String getId_tipo_jornada() {
		return id_tipo_jornada;
	}
	public void setId_tipo_jornada(String id_tipo_jornada) {
		this.id_tipo_jornada = id_tipo_jornada;
	}
	public Double getHoras() {
		return horas;
	}
	public void setHoras(Double horas) {
		this.horas = horas;
	}
	public String getFec_inicio() {
		return fec_inicio;
	}
	public void setFec_inicio(String fec_inicio) {
		this.fec_inicio = fec_inicio;
	}
	public String getFec_fin() {
		return fec_fin;
	}
	public void setFec_fin(String fec_fin) {
		this.fec_fin = fec_fin;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public String getId_tipo_tabulador() {
		return id_tipo_tabulador;
	}
	public void setId_tipo_tabulador(String idtipo_tabulador) {
		this.id_tipo_tabulador = idtipo_tabulador;
	}
	public String getFec_paga() {
		return fec_paga;
	}
	public void setFec_paga(String fec_paga) {
		this.fec_paga = fec_paga;
	}
	public String getId_zona() {
		return id_zona;
	}
	public void setId_zona(String id_zona) {
		this.id_zona = id_zona;
	}
	public Integer getRiesgos() {
		return riesgos;
	}
	public void setRiesgos(Integer riesgos) {
		this.riesgos = riesgos;
	}
	public String getestatus() {
		return estatus;
	}
	public void setestatus(String estatus) {
		this.estatus = estatus;
	}
	public String getId_ordinal() {
		return id_ordinal;
	}
	public void setId_ordinal(String id_ordinal) {
		this.id_ordinal = id_ordinal;
	}
	public String getTipo_guardia() {
		return tipo_guardia;
	}
	public void setTipo_guardia(String tipo_guardia) {
		this.tipo_guardia = tipo_guardia;
	}

	// Extra Info
	public String getFolio() {
		return folio;
	}
	public String getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getId_clave_movimiento() {
		return id_clave_movimiento;
	}
	public void setId_clave_movimiento(String id_clave_movimiento) {
		this.id_clave_movimiento = id_clave_movimiento;
	}
	public String getComent() {
		return coment;
	}
	public void setComent(String coment) {
		this.coment = coment;
	}
	public String getId_empresa() {
		return id_empresa;
	}
	public void setId_empresa(String id_empresa) {
		this.id_empresa = id_empresa;
	}
	public Integer getHora_inicio() {
		return hora_inicio;
	}
	public void setHora_inicio(Integer hora_inicio) {
		this.hora_inicio = hora_inicio;
	}
	public Integer getHora_fin() {
		return hora_fin;
	}
	public void setHora_fin(Integer hora_fin) {
		this.hora_fin = hora_fin;
	}

	@Override
	public String toString() {
		return "DatosGuardia [clave_empleado=" + clave_empleado + ", id_centro_trabajo=" + id_centro_trabajo
				+ ", id_clave_servicio=" + id_clave_servicio + ", id_puesto_plaza=" + id_puesto_plaza + ", id_nivel="
				+ id_nivel + ", id_sub_nivel=" + id_sub_nivel + ", id_tipo_jornada=" + id_tipo_jornada + ", horas="
				+ horas + ", fec_inicio=" + fec_inicio + ", fec_fin=" + fec_fin + ", importe=" + importe
				+ ", id_tipo_tabulador=" + id_tipo_tabulador + ", riesgos=" + riesgos + "]";
	}

}
