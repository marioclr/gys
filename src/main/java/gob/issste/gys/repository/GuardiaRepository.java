package gob.issste.gys.repository;

import java.sql.SQLException;
import java.util.List;

import gob.issste.gys.model.DatosGuardia;

public interface GuardiaRepository {

	public String QUERY_ADD_NEW_GUARDIA         = "INSERT INTO gys_guardias_emp (id_sociedad, id_empleado, fec_paga, fec_inicio, id_ordinal, fec_fin, id_empresa, "
												+ "id_puesto_plaza, id_clave_servicio, id_centro_trabajo, id_tipo_jornada, id_nivel, id_sub_nivel, horas, importe, folio, "
												+ "motivo, id_clave_movimiento, coment, id_usuario, fec_ult_actualizacion, riesgos) Values (?, ?, ?, ?, "
												+ "(Select NVL(MAX(id_ordinal),0) + 1 From gys_guardias_emp Where id_empleado = ? And fec_paga = ? And fec_inicio = ?), "
												+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT YEAR TO SECOND, ? )";
	int save(DatosGuardia guardia) throws SQLException;

	public String QUERY_ADD_NEW_GUARDIA_EXT     = "INSERT INTO gys_guardias_ext (rfc, fec_paga, fec_inicio, id_ordinal, fec_fin, "
												+ "id_puesto_plaza, id_clave_servicio, id_centro_trabajo, id_tipo_jornada, id_nivel, id_sub_nivel, horas, importe, folio, "
												+ "motivo, id_clave_movimiento, coment, id_usuario, fec_ult_actualizacion, riesgos) Values (?, ?, ?, "
												+ "(Select NVL(MAX(id_ordinal),0) + 1 From gys_guardias_ext Where rfc = ? And fec_paga = ? And fec_inicio = ?), "
												+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT YEAR TO SECOND, ? )";
	int saveExt(DatosGuardia guardia) throws SQLException;

	public String QUERY_UPD_GUARDIA				= "Update gys_guardias_emp Set importe = ?, folio = ?, motivo = ?, id_clave_movimiento = ?, horas = ?, coment = ?, id_usuario = ?\r\n"
												+ "Where id = ?";
	int updateGuardia(DatosGuardia guardia);

	public String QUERY_UPD_GUARDIA_EXT			= "Update gys_guardias_ext Set importe = ?, folio = ?, motivo = ?, id_clave_movimiento = ?, horas = ?, coment = ?, id_usuario = ?\r\n"
												+ "Where id = ?";
	int updateGuardiaExt(DatosGuardia guardia);

	public String QUERY_DELETE_GUARDIA			= "Delete From gys_guardias_emp\r\n"
												+ "Where id = ?";
	int deleteGuardia(Integer idGuardia);

	public String QUERY_DELETE_GUARDIA_EXT		= "Delete From gys_guardias_ext\r\n"
												+ "Where id = ?";
	int deleteGuardiaExt(Integer idGuardia);

	public String QUERY_UPD_GUARDIA_INTERNA		= "Update gys_guardias_emp Set importe = ?, riesgos = ?\r\n"
												+ "Where id_empleado = ? And fec_paga = ? And fec_inicio = ? And id_ordinal = ?";
	int updateImporteGuardia(DatosGuardia guardia);

	public String QUERY_UPD_GUARDIA_EXTERNA		= "Update gys_guardias_ext Set importe_java = ?\r\n"
												+ "Where id = ?";
	int updateImporteGuardiaExt(DatosGuardia guardia);

	public String QUERY_GET_GUARDIA_INTERNA     = "Select G.id, G.id_empleado As Clave_empleado, G.id_centro_trabajo, G.id_clave_servicio, G.id_puesto_plaza, 'I' tipo_guardia,\r\n"
												+ "G.id_nivel, G.id_sub_nivel, G.id_tipo_jornada, G.horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment,\r\n"
												+ "G.importe, PU.id_tipo_tabulador, G.fec_paga, C.id_zona, P.estatus, G.id_ordinal, NVL(riesgos,0) riesgos, NVL(G.id_usuario, '') id_usuario, G.id_empresa\r\n"
												+ "From gys_guardias_emp G, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU \r\n"
												+ "Where G.fec_paga = P.fec_pago And \r\n"
												+ "G.id_centro_trabajo = C.id_centro_trabajo And \r\n"
												+ "G.id_puesto_plaza = PU.id_puesto_plaza And \r\n"
												+ "G.id_empresa=PU.id_empresa And \r\n"
												+ "PU.id_sociedad = '01' And PU.id_empresa = '01' \r\n"
												+ "And id_empleado = ? \r\n"
												+ "Order by G.fec_paga desc, G.fec_inicio";
	List<DatosGuardia> ConsultaGuardiasInternas(String id_empleado);

	public String QUERY_GET_GUARDIAS_X_FECHA    = "Select G.id, G.id_empleado As Clave_empleado, G.id_centro_trabajo, G.id_clave_servicio, G.id_puesto_plaza, 'I' tipo_guardia,\r\n"
												+ "G.id_nivel, G.id_sub_nivel, G.id_tipo_jornada, G.horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment,\r\n"
												+ "G.importe, PU.id_tipo_tabulador, G.fec_paga, C.id_zona, P.estatus, G.id_ordinal, NVL(riesgos,0) riesgos, NVL(G.id_usuario, '') id_usuario, G.id_empresa\r\n"
												+ ", empleado1\r\n"
												+ "From gys_guardias_emp G, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU\r\n"
												+ "Where G.fec_paga = P.fec_pago And\r\n"
												+ "G.id_centro_trabajo = C.id_centro_trabajo And\r\n"
												+ "G.id_puesto_plaza = PU.id_puesto_plaza And\r\n"
												+ "G.id_empresa=PU.id_empresa And \r\n"
												+ "PU.id_sociedad = '01' And PU.id_empresa = '01'\r\n"
												+ "And G.fec_paga = ?\r\n"
												+ "Order by G.fec_paga desc, G.fec_inicio";
	List<DatosGuardia> ConsultaGuardiasInternasXFecha(String fechaPago);

	public String QUERY_GET_GUARDIAS_BY_ID    	= "Select G.id, G.id_empleado As Clave_empleado, G.id_centro_trabajo, G.id_clave_servicio, G.id_puesto_plaza, 'I' tipo_guardia,\r\n"
												+ "G.id_nivel, G.id_sub_nivel, G.id_tipo_jornada, G.horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment,\r\n"
												+ "G.importe, PU.id_tipo_tabulador, G.fec_paga, C.id_zona, P.estatus, G.id_ordinal, NVL(riesgos,0) riesgos, NVL(G.id_usuario, '') id_usuario, G.id_empresa\r\n"
												+ "From gys_guardias_emp G, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU\r\n"
												+ "Where G.fec_paga = P.fec_pago And\r\n"
												+ "G.id_centro_trabajo = C.id_centro_trabajo And\r\n"
												+ "G.id_puesto_plaza = PU.id_puesto_plaza And\r\n"
												+ "G.id_empresa=PU.id_empresa And \r\n"
												+ "PU.id_sociedad = '01' And PU.id_empresa = '01'\r\n"
												+ "And G.id = ?\r\n"
												+ "Order by G.fec_paga desc, G.fec_inicio";
	DatosGuardia findById(Integer idGuardia);

	public String QUERY_GET_SALDO_GUARDIA_INT   = "Select NVL(SUM(importe), 0) importe\r\n"
												+ "From gys_guardias_emp\r\n"
												+ "Where id_centro_trabajo IN (Select id_centro_trabajo From m4t_centros_trab Where id_delegacion = ?)\r\n"
												+ "And fec_paga IN (Select fec_pago From gys_fechas_control Where anio_ejercicio = ?)";
	double ObtenerSaldoUtilizado(String idDelegacion, int anio_ejercicio);

	public String QUERY_GET_SALDO_GUARDIA_INT_CT = "Select NVL(SUM(importe), 0) importe\r\n"
												+ "From gys_guardias_emp\r\n"
												+ "Where id_centro_trabajo = ?\r\n"
												+ "And fec_paga IN (Select fec_pago From gys_fechas_control Where anio_ejercicio = ?)";
	double ObtenerSaldoUtilizado_ct(String id_centro_trabajo, int anio_ejercicio);

	public String QUERY_GET_SALDO_GUARDIA_EXT   = "Select NVL(SUM(importe), 0) importe\r\n"
												+ "From gys_guardias_ext\r\n"
												+ "Where id_centro_trabajo IN (Select id_centro_trabajo From m4t_centros_trab Where id_delegacion = ?)\r\n"
												+ "And fec_paga IN (Select fec_pago From gys_fechas_control Where anio_ejercicio = ?)";
	double ObtenerSaldoUtilizadoExt(String idDelegacion, int anio_ejercicio);

	public String QUERY_GET_SALDO_GUARDIA_EXT_CT = "Select NVL(SUM(importe), 0) importe\r\n"
												+ "From gys_guardias_ext\r\n"
												+ "Where id_centro_trabajo = ?\r\n"
												+ "And fec_paga IN (Select fec_pago From gys_fechas_control Where anio_ejercicio = ?)";
	double ObtenerSaldoUtilizadoExt_ct(String id_centro_trabajo, int anio_ejercicio);

	public String QUERY_GET_GUARDIA_EXTERNA     = "Select G.id, rfc As Clave_empleado, G.id_centro_trabajo, G.id_clave_servicio, G.id_puesto_plaza, 'E' tipo_guardia,\r\n"
												+ "G.id_nivel, G.id_sub_nivel, G.id_tipo_jornada, G.horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment,\r\n"
												+ "G.importe, PU.id_tipo_tabulador, G.fec_paga, C.id_zona, P.estatus, G.id_ordinal, NVL(riesgos,0) riesgos, G.id_usuario\r\n"
												+ "From gys_guardias_ext G, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU \r\n"
												+ "Where G.fec_paga = P.fec_pago And \r\n"
												+ "G.id_centro_trabajo = C.id_centro_trabajo And \r\n"
												+ "G.id_puesto_plaza = PU.id_puesto_plaza And \r\n"
												+ "PU.id_sociedad = '01' And PU.id_empresa = '01' And \r\n"
												+ "rfc = ? \r\n"
												+ "Order by G.fec_paga desc, G.fec_inicio";
	List<DatosGuardia> ConsultaGuardiasExternas(String rfcEmpleado);

	public String QUERY_GET_GUARDIAS_EXT_X_FECHA = "Select G.id, rfc clave_empleado, G.id_centro_trabajo, id_clave_servicio, G.id_puesto_plaza, 'E' tipo_guardia,\r\n"
												+ "id_nivel, id_sub_nivel, id_tipo_jornada, horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment,\r\n"
												+ "G.importe, PU.id_tipo_tabulador, G.fec_paga, C.id_zona, P.estatus, G.id_ordinal, NVL(riesgos,0) riesgos, G.id_usuario \r\n"
												+ ", empleado1 \r\n"
												+ "From gys_guardias_ext G, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU \r\n"
												+ "Where G.fec_paga = P.fec_pago And \r\n"
												+ "G.id_centro_trabajo = C.id_centro_trabajo And \r\n"
												+ "G.id_puesto_plaza = PU.id_puesto_plaza And \r\n"
												+ "PU.id_sociedad = '01' And PU.id_empresa = '01' And \r\n"
												+ "G.fec_paga = ? \r\n"
												+ "Order by G.fec_paga desc, G.fec_inicio";
	List<DatosGuardia> ConsultaGuardiasExternasXFecha(String fechaPago);

	public String QUERY_GET_GUARDIAS_EXT_BY_ID  = "Select G.id, rfc clave_empleado, G.id_centro_trabajo, id_clave_servicio, G.id_puesto_plaza, 'E' tipo_guardia,\r\n"
												+ "id_nivel, id_sub_nivel, id_tipo_jornada, horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment,\r\n"
												+ "G.importe, PU.id_tipo_tabulador, G.fec_paga, C.id_zona, P.estatus, G.id_ordinal, NVL(riesgos,0) riesgos, G.id_usuario \r\n"
												+ "From gys_guardias_ext G, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU \r\n"
												+ "Where G.fec_paga = P.fec_pago And \r\n"
												+ "G.id_centro_trabajo = C.id_centro_trabajo And \r\n"
												+ "G.id_puesto_plaza = PU.id_puesto_plaza And \r\n"
												+ "PU.id_sociedad = '01' And PU.id_empresa = '01' And \r\n"
												+ "G.id = ? \r\n";
	DatosGuardia findByIdExterno(Integer idGuardia);

	/*
	 * 
	 * Pruebas para validar condiciones de cálculo
	 * 
	 */

	public String QUERY_UPD_GUARDIAS_INT_VARS = "Update gys_guardias_emp Set importe = ?, riesgos = ?,\r\n"
											   + "  id_puesto_plaza = ?, id_clave_servicio = ?, id_nivel = ?, id_sub_nivel = ?,\r\n"
											   + "  id_tipo_jornada = ?, id_centro_trabajo = ?\r\n"
											   + "Where id_empleado = ? And fec_paga = ? And fec_inicio = ? And id_ordinal = ?";
	int updateGuardiaIntVars(DatosGuardia guardia);

	public String QUERY_UPD_GUARDIAS_EXT_VARS = "Update gys_guardias_ext Set importe_java = ?, riesgos1 = ?,\r\n"
											   + "  id_puesto_plaza1 = ?, id_clave_servicio1 = ?, id_nivel1 = ?, id_sub_nivel1 = ?,\r\n"
											   + "  id_tipo_jornada1 = ?, id_centro_trabajo1 = ?\r\n"
											   + "Where rfc = ? And fec_paga = ? And fec_inicio = ? And id_ordinal = ?";
	int updateGuardiaExtVars(DatosGuardia guardia);

	List<DatosGuardia> ConsultaDynamicGuardias(String fechaPago, String tipo, String clave_empleado, Double importe_inicio, Double importe_fin,
			String idDelegacion, String idCentroTrab, String claveServicio, String puesto);

}