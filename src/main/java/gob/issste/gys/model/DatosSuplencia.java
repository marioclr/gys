package gob.issste.gys.model;

public class DatosSuplencia {

	private Integer id; // 1
	private String clave_empleado;
	private DatosEmpleado empleado;
	private String clave_empleado_suplir;
	private DatosEmpleado empleado_suplir;
	private Integer dias;
	private String fec_inicio; // 10
	private String fec_fin;
	private Double importe;
	private String fec_paga;
	private Integer riesgos;
	private String estado;
	private String id_ordinal; // 18
	private String tipo_suplencia;
	// Extra Info
	private String id_usuario;
	private String folio;
	private String motivo;
	private String id_clave_movimiento;
	private String coment;
	private String id_empresa;
	private String estatus;
	private String id_tipo_ausentismo;
	private String id_puesto_plaza;
	private String n_puesto_plaza;
	private String id_clave_servicio;
	private String n_clave_servicio;
	private String id_tipo_jornada;
	private String id_nivel;
	private String id_sub_nivel;
	private String id_centro_trabajo;
	private String n_centro_trabajo;
	private String id_turno;

	// Para Validaciones
	private Double sueldo;

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
	public DatosEmpleado getEmpleado() {
		return empleado;
	}
	public void setEmpleado(DatosEmpleado empleado) {
		this.empleado = empleado;
	}
	public String getClave_empleado_suplir() {
		return clave_empleado_suplir;
	}
	public void setClave_empleado_suplir(String clave_empleado_suplir) {
		this.clave_empleado_suplir = clave_empleado_suplir;
	}
	public DatosEmpleado getEmpleado_suplir() {
		return empleado_suplir;
	}
	public void setEmpleado_suplir(DatosEmpleado empleado_suplir) {
		this.empleado_suplir = empleado_suplir;
	}
	public Integer getDias() {
		return dias;
	}
	public void setDias(Integer dias) {
		this.dias = dias;
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
	public String getFec_paga() {
		return fec_paga;
	}
	public void setFec_paga(String fec_paga) {
		this.fec_paga = fec_paga;
	}
	public Integer getRiesgos() {
		return riesgos;
	}
	public void setRiesgos(Integer riesgos) {
		this.riesgos = riesgos;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getId_ordinal() {
		return id_ordinal;
	}
	public void setId_ordinal(String id_ordinal) {
		this.id_ordinal = id_ordinal;
	}
	public String getTipo_suplencia() {
		return tipo_suplencia;
	}
	public void setTipo_suplencia(String tipo_suplencia) {
		this.tipo_suplencia = tipo_suplencia;
	}
	// Extra Info

	public String getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}
	public String getFolio() {
		return folio;
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
	public Double getSueldo() {
		return sueldo;
	}
	public void setSueldo(Double sueldo) {
		this.sueldo = sueldo;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public String getId_tipo_ausentismo() {
		return id_tipo_ausentismo;
	}
	public void setId_tipo_ausentismo(String id_tipo_ausentismo) {
		this.id_tipo_ausentismo = id_tipo_ausentismo;
	}

	public String getId_puesto_plaza() {
		return id_puesto_plaza;
	}
	public void setId_puesto_plaza(String id_puesto_plaza) {
		this.id_puesto_plaza = id_puesto_plaza;
	}
	public String getN_puesto_plaza() {
		return n_puesto_plaza;
	}
	public void setN_puesto_plaza(String n_puesto_plaza) {
		this.n_puesto_plaza = n_puesto_plaza;
	}
	public String getId_clave_servicio() {
		return id_clave_servicio;
	}
	public void setId_clave_servicio(String id_clave_servicio) {
		this.id_clave_servicio = id_clave_servicio;
	}
	public String getN_clave_servicio() {
		return n_clave_servicio;
	}
	public void setN_clave_servicio(String n_clave_servicio) {
		this.n_clave_servicio = n_clave_servicio;
	}
	public String getId_tipo_jornada() {
		return id_tipo_jornada;
	}
	public void setId_tipo_jornada(String id_tipo_jornada) {
		this.id_tipo_jornada = id_tipo_jornada;
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
	public String getId_centro_trabajo() {
		return id_centro_trabajo;
	}
	public void setId_centro_trabajo(String id_centro_trabajo) {
		this.id_centro_trabajo = id_centro_trabajo;
	}
	public String getN_centro_trabajo() {
		return n_centro_trabajo;
	}
	public void setN_centro_trabajo(String n_centro_trabajo) {
		this.n_centro_trabajo = n_centro_trabajo;
	}
	public String getId_turno() {
		return id_turno;
	}
	public void setId_turno(String id_turno) {
		this.id_turno = id_turno;
	}

	@Override
	public String toString() {
		return "DatosSuplencia [id=" + id + ", empleado=" + empleado + ", empleado_suplir="
				+ empleado_suplir + ", dias=" + dias + ", fec_inicio=" + fec_inicio + ", fec_fin=" + fec_fin
				+ ", importe=" + importe + ", fec_paga=" + fec_paga + ", riesgos=" + riesgos + ", estado=" + estado
				+ ", id_ordinal=" + id_ordinal + ", tipo_guardia=" + tipo_suplencia + ", userName=" + id_usuario
				+ ", folio=" + folio + ", motivo=" + motivo + ", movimiento=" + id_clave_movimiento + ", coment=" + coment
				+ ", id_empresa=" + id_empresa + ", id_tipo_ausentismo=" + id_tipo_ausentismo + "]";
	}

}
