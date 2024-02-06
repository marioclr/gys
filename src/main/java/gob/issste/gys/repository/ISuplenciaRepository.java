package gob.issste.gys.repository;

import java.sql.SQLException;
import java.util.List;

import gob.issste.gys.model.DatosEmpleado;
import gob.issste.gys.model.DatosSuplencia;
import gob.issste.gys.model.FactoresSuplencia;

public interface ISuplenciaRepository {

	public String QUERY_ADD_NEW_SUPLENCIA       = "INSERT INTO gys_suplencias_emp (id_sociedad, id_empleado, fec_paga, fec_inicio, id_ordinal, fec_fin, id_empresa, "
												+ "dias, inf_ordinal, id_usuario, fec_ult_actualizacion, coment, id_empleado_sup, "
												+ "id_clave_movimiento, id_tipo_ausentismo, importe, folio,  motivo) Values (?, ?, ?, ?, "
												+ "(Select NVL(MAX(id_ordinal),0) + 1 From gys_suplencias_emp Where id_empleado = ? And fec_paga = ? And fec_inicio = ?), "
												+ "?, ?, ?, ?, ?, CURRENT YEAR TO SECOND, ?, ?, ?, ?, ?, ?, ?)";
	int save(DatosSuplencia suplencia) throws SQLException;

	public String QUERY_ADD_NEW_SUPLENCIA_EMP   = "INSERT INTO gys_suplencias_emp (id_sociedad, id_empleado, fec_paga, fec_inicio, id_ordinal, fec_fin, id_empresa, "
												+ "dias, inf_ordinal, id_usuario, fec_ult_actualizacion, coment, id_empleado_sup, "
												+ "id_clave_movimiento, id_tipo_ausentismo, importe, folio,  motivo, riesgos, id_puesto_plaza, "
												+ "id_clave_servicio, id_tipo_jornada, id_nivel, id_sub_nivel, id_centro_trabajo, id_turno) "
												+ "Values (?, ?, ?, ?, "
												+ "(Select NVL(MAX(id_ordinal),0) + 1 From gys_suplencias_emp Where id_empleado = ? And fec_paga = ? And fec_inicio = ?), "
												+ "?, ?, ?, ?, ?, CURRENT YEAR TO SECOND, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	int save(DatosSuplencia suplencia, DatosEmpleado empleado) throws SQLException;

	public String QUERY_ADD_NEW_SUPLENCIA_EXT   = "INSERT INTO gys_suplencias_ext (rfc, fec_paga, fec_inicio, id_ordinal, fec_fin, "
												+ "dias, inf_ordinal, id_usuario, fec_ult_actualizacion, coment, id_empleado_sup, "
												+ "id_clave_movimiento, id_tipo_ausentismo, importe, folio,  motivo) "
												+ "Values (?, ?, ?, "
												+ "(Select NVL(MAX(id_ordinal),0) + 1 From gys_suplencias_ext Where rfc = ? And fec_paga = ? And fec_inicio = ?), "
												+ "?, ?, ?, ?, CURRENT YEAR TO SECOND, ?, ?, ?, ?, ?, ?, ?)";
	int saveExt(DatosSuplencia suplencia) throws SQLException;

	public String QUERY_ADD_NEW_SUPLENCIA_EXT_EMP = "INSERT INTO gys_suplencias_ext (rfc, fec_paga, fec_inicio, id_ordinal, fec_fin, "
												+ "dias, inf_ordinal, id_usuario, fec_ult_actualizacion, coment, id_empleado_sup, "
												+ "id_clave_movimiento, id_tipo_ausentismo, importe, folio,  motivo, riesgos, id_puesto_plaza, "
												+ "id_clave_servicio, id_tipo_jornada, id_nivel, id_sub_nivel, id_centro_trabajo, id_turno) "
												+ "Values (?, ?, ?, "
												+ "(Select NVL(MAX(id_ordinal),0) + 1 From gys_suplencias_ext Where rfc = ? And fec_paga = ? And fec_inicio = ?), "
												+ "?, ?, ?, ?, CURRENT YEAR TO SECOND, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	int saveExt(DatosSuplencia suplencia, DatosEmpleado empleado) throws SQLException;

	public String QUERY_UPD_SUPLENCIA			= "Update gys_suplencias_emp Set importe = ?, folio = ?, motivo = ?, id_clave_movimiento = ?, dias = ?, coment = ?, id_usuario = ?, fec_fin = ?\r\n"
												+ "Where id = ?";
	int updateSuplencia(DatosSuplencia suplencia);

	public String QUERY_UPD_SUPLENCIA_EXT		= "Update gys_suplencias_ext Set importe = ?, folio = ?, motivo = ?, id_clave_movimiento = ?, dias = ?, coment = ?, id_usuario = ?, fec_fin = ?\r\n"
												+ "Where id = ?";
	int updateSuplenciaExt(DatosSuplencia suplencia);

	public String QUERY_DELETE_SUPLENCIA		= "Delete From gys_suplencias_emp\r\n"
												+ "Where id = ?";
	int deleteSuplencia(Integer idSuplencia);

	public String QUERY_DELETE_SUPLENCIA_EXT	= "Delete From gys_suplencias_ext\r\n"
												+ "Where id = ?";
	int deleteSuplenciaExt(Integer idSuplencia);

	public String QUERY_UPD_IMP_SUPLENCIA	    = "Update gys_suplencias_emp Set importe = ?, riesgos = ?\r\n"
												+ "Where id_empleado = ? And fec_paga = ? And fec_inicio = ? And id_ordinal = ?";
	int updateImporteSuplencia(DatosSuplencia suplencia);

	public String QUERY_UPD_IMP_SUPLENCIA_EXT   = "Update gys_suplencias_ext Set importe = ?, riesgos = ?\r\n"
												+ "Where rfc = ? And fec_paga = ? And fec_inicio = ? And id_ordinal = ?";
	int updateImporteSuplenciaExt(DatosSuplencia guardia);

	public String QUERY_GET_SUPLENCIA_INTERNA   = "Select G.id, G.id_empleado As Clave_empleado, G.id_empleado_sup As Clave_empleado_suplir, \r\n"
												+ "G.dias, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment, G.estatus,\r\n"
												+ "G.importe, G.fec_paga, G.id_ordinal, NVL(riesgos, 0) riesgos, G.id_usuario, G.id_empresa, G.id_tipo_ausentismo \r\n"
												+ "From gys_suplencias_emp G, gys_fechas_control P \r\n"
												+ "Where G.fec_paga = P.fec_pago \r\n"
												+ "And id_empleado = ? \r\n"
												+ "Order by G.fec_paga desc, G.fec_inicio";
	List<DatosSuplencia> ConsultaSuplenciasInternas(String id_empleado);

	public String QUERY_GET_SUPLENCIAS_X_FECHA  = "Select G.id, G.id_empleado As Clave_empleado, G.id_empleado_sup As Clave_empleado_suplir, \r\n"
												+ "G.dias, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment, G.estatus,\r\n"
												+ "G.importe, G.fec_paga, G.id_ordinal, NVL(riesgos, 0) riesgos, G.id_usuario, G.id_empresa, G.id_tipo_ausentismo \r\n"
												+ "From gys_suplencias_emp G, gys_fechas_control P \r\n"
												+ "Where G.fec_paga = P.fec_pago\r\n"
												+ "And G.fec_paga = ?\r\n"
												+ "Order by G.fec_paga desc, G.fec_inicio";
	List<DatosSuplencia> ConsultaSuplenciasInternasXFecha(String fechaPago);

	public String QUERY_GET_SUPLENCIA_EXTERNA   = "Select G.id, G.rfc As Clave_empleado, G.id_empleado_sup As Clave_empleado_suplir, \r\n"
												+ "G.dias, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment, G.estatus,\r\n"
												+ "G.importe, G.fec_paga, G.id_ordinal, NVL(riesgos, 0) riesgos, G.id_usuario, G.id_tipo_ausentismo \r\n"
												+ "From gys_suplencias_ext G, gys_fechas_control P \r\n"
												+ "Where G.fec_paga = P.fec_pago \r\n"
												+ "And rfc = ? \r\n"
												+ "Order by G.fec_paga desc, G.fec_inicio";
	List<DatosSuplencia> ConsultaSuplenciasExternas(String rfcEmpleado);

	public String QUERY_GET_SUPL_EXT_X_FECHA  	= "Select G.id, G.rfc As Clave_empleado, G.id_empleado_sup As Clave_empleado_suplir, \r\n"
												+ "G.dias, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment, G.estatus,\r\n"
												+ "G.importe, G.fec_paga, G.id_ordinal, NVL(riesgos, 0) riesgos, G.id_usuario, G.id_tipo_ausentismo \r\n"
												+ "From gys_suplencias_ext G, gys_fechas_control P \r\n"
												+ "Where G.fec_paga = P.fec_pago \r\n"
												+ "And G.fec_paga = ?\r\n"
												+ "Order by G.fec_paga desc, G.fec_inicio";
	List<DatosSuplencia> ConsultaSuplenciasExternasXFecha(String fechaPago);

	public String QUERY_GET_FACTORES_SUPLENCIA  = "Select fact_jornada, fact_turno \r\n"
												+ "From m4t_factor_suple\r\n"
												+ "Where id_turno = ?";
	FactoresSuplencia ConsultaFactoresSuplencia(String id_turno);

	public String QUERY_GET_SUPLENCIA_BY_ID     = "Select G.id, G.id_empleado As Clave_empleado, G.id_empleado_sup As Clave_empleado_suplir, \r\n"
												+ "G.dias, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment, G.estatus,\r\n"
												+ "G.importe, G.fec_paga, G.id_ordinal, NVL(riesgos, 0) riesgos, G.id_usuario, G.id_empresa, G.id_tipo_ausentismo \r\n"
												+ "From gys_suplencias_emp G, gys_fechas_control P \r\n"
												+ "Where G.fec_paga = P.fec_pago \r\n"
												+ "And G.id = ? \r\n"
												+ "Order by G.fec_paga desc, G.fec_inicio";
	DatosSuplencia findById(Integer idSuplencia);

	public String QUERY_GET_SUPLENCIA_EXT_BY_ID = "Select G.id, G.rfc As Clave_empleado, G.id_empleado_sup As Clave_empleado_suplir, \r\n"
												+ "G.dias, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment, G.estatus,\r\n"
												+ "G.importe, G.fec_paga, G.id_ordinal, NVL(riesgos, 0) riesgos, G.id_usuario, G.id_tipo_ausentismo \r\n"
												+ "From gys_suplencias_ext G, gys_fechas_control P \r\n"
												+ "Where G.fec_paga = P.fec_pago \r\n"
												+ "And G.id = ? \r\n"
												+ "Order by G.fec_paga desc, G.fec_inicio";
	DatosSuplencia findByIdExt(Integer idSuplencia);

	public String QUERY_GET_SALDO_SUPLENCIA_INT = "Select NVL(SUM(importe), 0) importe\r\n"
												+ "From gys_suplencias_emp\r\n"
												+ "Where id_centro_trabajo IN (Select id_centro_trabajo From m4t_centros_trab Where id_area_generadora = ( Select id_area_generadora From m4t_delegaciones Where id_div_geografica = ? ) )\r\n"
												+ "And fec_paga IN (Select fec_pago From gys_fechas_control Where anio_ejercicio = ? And mes_ejercicio = ?)";
	double ObtenerSaldoUtilizado(String idDelegacion, int anio_ejercicio, int mes_ejercicio);

	public String QUERY_GET_SALDO_SUPLENCIA_INT_CT = "Select NVL(SUM(importe), 0) importe\r\n"
												+ "From gys_suplencias_emp\r\n"
												+ "Where id<>? And id_centro_trabajo = ?\r\n"
												+ "And fec_paga IN (Select fec_pago From gys_fechas_control Where anio_ejercicio = ? And mes_ejercicio = ?)";
	double ObtenerSaldoUtilizado_ct(int id, String id_centro_trabajo, int anio_ejercicio, int mes_ejercicio);
	
	public String QUERY_GET_SALDO_SUPLENCIA_EXT = "Select NVL(SUM(importe), 0) importe\r\n"
												+ "From gys_suplencias_ext\r\n"
												+ "Where id_centro_trabajo IN (Select id_centro_trabajo From m4t_centros_trab Where id_area_generadora = ( Select id_area_generadora From m4t_delegaciones Where id_div_geografica = ? ) )\r\n"
												+ "And fec_paga IN (Select fec_pago From gys_fechas_control Where anio_ejercicio = ? And mes_ejercicio = ?)";
	double ObtenerSaldoUtilizadoExt(String idDelegacion, int anio_ejercicio, int mes_ejercicio);

	public String QUERY_GET_SALDO_SUPLENCIA_EXT_CT = "Select NVL(SUM(importe), 0) importe\r\n"
												+ "From gys_suplencias_ext\r\n"
												+ "Where id<>? And id_centro_trabajo = ?\r\n"
												+ "And fec_paga IN (Select fec_pago From gys_fechas_control Where anio_ejercicio = ? And mes_ejercicio = ?)";
	double ObtenerSaldoUtilizadoExt_ct(int id, String id_centro_trabajo, int anio_ejercicio, int mes_ejercicio);

	public String QUERY_EXISTS_SUPL_INT         = "Select COUNT(*) \r\n"
												+ "From gys_suplencias_emp\r\n"
												+ "Where id_empleado = ?\r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?)) \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))    \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int existe_suplencia(DatosSuplencia suplencia);

	public String QUERY_EXISTS_SUPLENTE_INT     = "Select COUNT(*) \r\n"
												+ "From gys_suplencias_emp\r\n"
												+ "Where id_empleado_sup = ?\r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?)) \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))    \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int existe_suplente(DatosSuplencia suplencia);

	public String QUERY_EXISTS_SUPLENTE_INT_UPD = "Select COUNT(*) \r\n"
												+ "From gys_suplencias_emp\r\n"
												+ "Where id_empleado_sup = ?\r\n"
												+ "  And id<>?\r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?)) \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))    \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int existe_suplente_upd(DatosSuplencia suplencia);

	public String QUERY_EXISTS_SUPL_EXT         = "Select COUNT(*) \r\n"
												+ "From gys_suplencias_ext\r\n"
												+ "Where rfc = ?\r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?)) \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))    \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int existe_suplenciaExt(DatosSuplencia suplencia);

	public String QUERY_EXISTS_SUPLENTE_EXT     = "Select COUNT(*) \r\n"
												+ "From gys_suplencias_ext\r\n"
												+ "Where id_empleado_sup = ?\r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?)) \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))    \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int existe_suplenteExt(DatosSuplencia suplencia);

	public String QUERY_EXISTS_SUPLENTE_EXT_UPD = "Select COUNT(*) \r\n"
												+ "From gys_suplencias_ext\r\n"
												+ "Where  id_empleado_sup = ?\r\n"
												+ "  And id<>?\r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?)) \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))    \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int existe_suplenteExt_upd(DatosSuplencia suplencia);

	public String QUERY_EXISTS_SUPL_INT_UPD     = "Select COUNT(*) \r\n"
												+ "From gys_suplencias_emp\r\n"
												+ "Where id_empleado = ?\r\n"
												+ "  And id<>?\r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?)) \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))    \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int existe_suplencia_upd(DatosSuplencia suplencia);

	public String QUERY_EXISTS_SUPL_EXT_UPD     = "Select COUNT(*) \r\n"
												+ "From gys_suplencias_ext\r\n"
												+ "Where rfc = ?\r\n"
												+ "  And id<>?\r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?)) \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))    \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int existe_suplenciaExt_upd(DatosSuplencia suplencia);

	/*
	 * 
	 * Pruebas para validar condiciones de cálculo
	 * 
	 */

	public String QUERY_UPD_SUPLENCIA_INT_VARS	= "Update gys_suplencias_emp Set importe = ?, riesgos = ?,\r\n"
											   + "  id_puesto_plaza = ?, id_clave_servicio = ?, id_nivel = ?, id_sub_nivel = ?,\r\n"
											   + "  id_tipo_jornada = ?, id_centro_trabajo = ?, id_turno = ?, id_tipo_ausentismo = ?\r\n"
											   + "Where id_empleado = ? And fec_paga = ? And fec_inicio = ? And id_ordinal = ?";
	int updateSuplenciaIntVars(DatosSuplencia suplencia);

	public String QUERY_UPD_SUPLENCIA_EXT_VARS = "Update gys_suplencias_ext Set importe = ?, riesgos = ?,\r\n"
											   + "  id_puesto_plaza = ?, id_clave_servicio = ?, id_nivel = ?, id_sub_nivel = ?,\r\n"
											   + "  id_tipo_jornada = ?, id_centro_trabajo = ?, id_turno = ?, sueldo = ?, id_tipo_ausentismo = ?\r\n"
											   + "Where rfc = ? And fec_paga = ? And fec_inicio = ? And id_ordinal = ?";
	int updateSuplenciaExtVars(DatosSuplencia suplencia);

	/*
	 * 
	 * Consultas para validar los topes de las horas/días de guardias
	 * 
	 */

	public String QUERY_GET_HORAS_SUPLENCIA_INT = "Select SUM(TRUNC(id_tipo_jornada*dias)) \r\n"
												+ "From gys_suplencias_emp\r\n"
												+ "Where id_empleado = ? \r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?)) \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))    \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int get_horas_suplencia(String clave_empleado, String inicio, String fin);

	public String QUERY_GET_HORAS_SUPLENCIA_INT_UPD = "Select SUM(TRUNC(id_tipo_jornada*dias)) \r\n"
												+ "From gys_suplencias_emp\r\n"
												+ "Where id_empleado = ? \r\n"
												+ "  And id<>? \r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?)) \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))    \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int get_horas_suplencia_upd(String clave_empleado, Integer id, String inicio, String fin);

	public String QUERY_GET_HORAS_SUPLENCIA_EXT = "Select SUM(TRUNC(id_tipo_jornada*dias)) \r\n"
												+ "From gys_suplencias_ext\r\n"
												+ "Where rfc = ? \r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?)) \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))    \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int get_horas_suplencia_ext(String clave_empleado, String inicio, String fin);

	public String QUERY_GET_HORAS_SUPLENCIA_EXT_UPD = "Select SUM(TRUNC(id_tipo_jornada*dias)) \r\n"
												+ "From gys_suplencias_ext\r\n"
												+ "Where rfc = ? \r\n"
												+ "  And id<>? \r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?)) \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))    \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int get_horas_suplencia_ext_upd(String clave_empleado, Integer id, String inicio, String fin);

	public String QUERY_GET_DIAS_SUPLENCIA_INT   = "Select SUM(dias) \r\n"
												+ "From gys_suplencias_emp\r\n"
												+ "Where id_empleado = ?\r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?))\r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))   \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int get_dias_suplencia(String clave_empleado, String inicio, String fin);

	public String QUERY_GET_DIAS_SUPLENCIA_INT_UPD = "Select SUM(dias) \r\n"
												+ "From gys_suplencias_emp\r\n"
												+ "Where id_empleado = ?\r\n"
												+ "  And id<>? \r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?))\r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))   \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int get_dias_suplencia_upd(String clave_empleado, Integer id, String inicio, String fin);

	public String QUERY_GET_DIAS_SUPLENCIA_EXT    = "Select SUM(dias) \r\n"
												+ "From gys_suplencias_ext\r\n"
												+ "Where rfc = ?\r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?))\r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))   \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int get_dias_suplencia_ext(String clave_empleado, String inicio, String fin);

	public String QUERY_GET_DIAS_SUPLENCIA_EXT_UPD = "Select SUM(dias) \r\n"
												+ "From gys_suplencias_ext\r\n"
												+ "Where rfc = ?\r\n"
												+ "  And id<>? \r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?))\r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))   \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int get_dias_suplencia_ext_upd(String clave_empleado, Integer id, String inicio, String fin);


	public String STMT_UPDATE_STATUS 			= "UPDATE gys_suplencias_emp Set estatus = ?\r\n"
												+ "Where id = ?";
	int updateStatusSuplencia(int status, int id);

	public String STMT_UPDATE_STATUS_EXT 		= "UPDATE gys_suplencias_ext Set estatus = ?\r\n"
												+ "Where id = ?";
	int updateStatusSuplenciaExt(int status, int id);

	public String STMT_UPDATE_AUTH_STATUS_1		= "UPDATE gys_autorizacion_suplencias Set estatus1 = ?, comentarios1 = ?, id_usuario1 = ?, fec_validacion = CURRENT YEAR TO SECOND\r\n"
												+ "Where id_suplencia = ? And id_tipo = ?";
	int updateAuthStatusSuplencia1(int status, int id, String tipo, String comentarios, int idUsuario);

	public String STMT_UPDATE_AUTH_STATUS_2		= "UPDATE gys_autorizacion_suplencias Set estatus2 = ?, comentarios2 = ?, id_usuario2 = ?, fec_autorizacion = CURRENT YEAR TO SECOND\r\n"
												+ "Where id_suplencia = ? And id_tipo = ?";
	int updateAuthStatusSuplencia2(int status, int id, String tipo, String comentarios, int idUsuario);

	public String STMT_UPDATES_AUTH_STATUS_1	= "Merge Into gys_autorizacion_suplencias A\r\n"
												+ "Using gys_suplencias_emp S\r\n"
												+ "  ON  A.id_suplencia = S.id\r\n"
												+ "  And A.estatus1 = 0 And id_tipo = ?\r\n"
												+ "  And S.fec_paga = ?\r\n"
												+ "WHEN MATCHED THEN\r\n"
												+ "    UPDATE SET estatus1 = 1, id_usuario1 = ?, fec_validacion = CURRENT YEAR TO SECOND";
	int updateAuthStatusSuplencias1(String tipo, String fec_pago, int idUsuario);

	public String STMT_UPDATES_AUTH_STATUS_1ext	= "Merge Into gys_autorizacion_suplencias A\r\n"
												+ "Using gys_suplencias_ext S\r\n"
												+ "  ON  A.id_suplencia = S.id\r\n"
												+ "  And A.estatus1 = 0 And id_tipo = ?\r\n"
												+ "  And S.fec_paga = ?\r\n"
												+ "WHEN MATCHED THEN\r\n"
												+ "    UPDATE SET estatus1 = 1, id_usuario1 = ?, fec_validacion = CURRENT YEAR TO SECOND";
	int updateAuthStatusSuplencias1Ext(String tipo, String fec_pago, int idUsuario);

	public String STMT_UPDATES_AUTH_STATUS_2	= "Merge Into gys_autorizacion_suplencias A\r\n"
												+ "Using gys_suplencias_emp S\r\n"
												+ "  ON  A.id_suplencia = S.id\r\n"
												+ "  And A.estatus1 = 1 And A.estatus2 = 0\r\n"
												+ "  And id_tipo = ? And S.fec_paga = ?\r\n"
												+ "WHEN MATCHED THEN\r\n"
												+ "    UPDATE SET estatus2 = 3, id_usuario2 = ?, fec_autorizacion = CURRENT YEAR TO SECOND";
	int updateAuthStatusSuplencias2(String tipo, String fec_pago, int idUsuario);

	public String STMT_UPDATES_AUTH_STATUS_2ext	= "Merge Into gys_autorizacion_suplencias A\r\n"
												+ "Using gys_suplencias_ext S\r\n"
												+ "  ON  A.id_suplencia = S.id\r\n"
												+ "  And A.estatus1 = 1 And A.estatus2 = 0\r\n"
												+ "  And id_tipo = ? And S.fec_paga = ?\r\n"
												+ "WHEN MATCHED THEN\r\n"
												+ "    UPDATE SET estatus2 = 3, id_usuario2 = ?, fec_autorizacion = CURRENT YEAR TO SECOND";
	int updateAuthStatusSuplencias2Ext(String tipo, String fec_pago, int idUsuario);

	List<DatosSuplencia> ConsultaDynamicSuplencias(String fechaPago, String tipo, String clave_empleado, Double importe_inicio, Double importe_fin,
			String idDelegacion, String idCentroTrab, String claveServicio, String puesto, String emp_suplir, Integer estatus);

	List<DatosSuplencia> ConsultaDynamicAuthSuplencias(String fechaPago, String tipo, String idDelegacion, String idCentroTrab, Integer estatus);

}