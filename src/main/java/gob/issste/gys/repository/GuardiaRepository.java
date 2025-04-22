package gob.issste.gys.repository;

import java.sql.SQLException;
import java.util.List;

import gob.issste.gys.model.DatosGuardia;

public interface GuardiaRepository {

	public String QUERY_ADD_NEW_GUARDIA         = "INSERT INTO gys_guardias_emp (id_sociedad, id_empleado, fec_paga, fec_inicio, id_ordinal, fec_fin, id_empresa, "
												+ "id_puesto_plaza, id_clave_servicio, id_centro_trabajo, id_tipo_jornada, id_nivel, id_sub_nivel, horas, importe, folio, "
												+ "motivo, id_clave_movimiento, coment, id_usuario, fec_ult_actualizacion, riesgos, hora_inicio, hora_fin ) Values (?, ?, ?, ?, "
												+ "(Select NVL(MAX(id_ordinal),0) + 1 From gys_guardias_emp Where id_empleado = ? And fec_paga = ? And fec_inicio = ?), "
												+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT YEAR TO SECOND, ?, ?, ? )";
	int save(DatosGuardia guardia) throws SQLException;

	public String QUERY_ADD_NEW_GUARDIA_EXT     = "INSERT INTO gys_guardias_ext (rfc, fec_paga, fec_inicio, id_ordinal, fec_fin, "
												+ "id_puesto_plaza, id_clave_servicio, id_centro_trabajo, id_tipo_jornada, id_nivel, id_sub_nivel, horas, importe, folio, "
												+ "motivo, id_clave_movimiento, coment, id_usuario, fec_ult_actualizacion, riesgos, hora_inicio, hora_fin) Values (?, ?, ?, "
												+ "(Select NVL(MAX(id_ordinal),0) + 1 From gys_guardias_ext Where rfc = ? And fec_paga = ? And fec_inicio = ?), "
												+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT YEAR TO SECOND, ?, ?, ? )";
	int saveExt(DatosGuardia guardia) throws SQLException;

	public String QUERY_UPD_GUARDIA				= "Update gys_guardias_emp Set importe = ?, folio = ?, motivo = ?, id_clave_movimiento = ?, horas = ?, coment = ?, id_usuario = ?, hora_inicio = ?, hora_fin = ?\r\n"
												+ "Where id = ?";
	int updateGuardia(DatosGuardia guardia);

	public String QUERY_UPD_GUARDIA_EXT			= "Update gys_guardias_ext Set importe = ?, folio = ?, motivo = ?, id_clave_movimiento = ?, horas = ?, coment = ?, id_usuario = ?, hora_inicio = ?, hora_fin = ?\r\n"
												+ "Where id = ?";
	int                                             updateGuardiaExt(DatosGuardia guardia);

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

	public String QUERY_GET_GUARDIA_INTERNA     = "Select G.id, G.id_empleado As Clave_empleado, G.id_centro_trabajo, G.id_clave_servicio, G.id_puesto_plaza, 'GI' tipo_guardia,\r\n"
												+ "G.id_nivel, G.id_sub_nivel, G.id_tipo_jornada, G.horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment, hora_inicio, hora_fin,\r\n"
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

	public String QUERY_GET_GUARDIAS_X_FECHA    = "Select G.id, G.id_empleado As Clave_empleado, G.id_centro_trabajo, G.id_clave_servicio, G.id_puesto_plaza, 'GI' tipo_guardia,\r\n"
												+ "G.id_nivel, G.id_sub_nivel, G.id_tipo_jornada, G.horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment, hora_inicio, hora_fin,\r\n"
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

	public String QUERY_GET_GUARDIAS_BY_ID    	= "Select G.id, G.id_empleado As Clave_empleado, G.id_centro_trabajo, G.id_clave_servicio, G.id_puesto_plaza, 'GI' tipo_guardia,\r\n"
												+ "G.id_nivel, G.id_sub_nivel, G.id_tipo_jornada, G.horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment, hora_inicio, hora_fin,\r\n"
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
												+ "Where id_centro_trabajo IN (Select id_centro_trabajo From m4t_centros_trab Where id_area_generadora = ( Select id_area_generadora From m4t_delegaciones Where id_div_geografica = ? ) )\r\n"
												+ "And fec_paga IN (Select fec_pago From gys_fechas_control Where anio_ejercicio = ? And mes_ejercicio = ?)";
	double ObtenerSaldoUtilizado(String idDelegacion, int anio_ejercicio, int mes_ejercicio);

	public String QUERY_GET_SALDO_GUARDIA_INT_CT = "Select NVL(SUM(importe), 0) importe\r\n"
												+ "From gys_guardias_emp\r\n"
												+ "Where id<>? And id_centro_trabajo = ?\r\n"
												+ "And fec_paga IN (Select fec_pago From gys_fechas_control Where anio_ejercicio = ? And mes_ejercicio = ? And quincena = ?)";
	double ObtenerSaldoUtilizado_ct(int id, String id_centro_trabajo, int anio_ejercicio, int mes_ejercicio, int quincena);

	public String QUERY_GET_SALDO_GUARDIA_EXT   = "Select NVL(SUM(importe), 0) importe\r\n"
												+ "From gys_guardias_ext\r\n"
												+ "Where id_centro_trabajo IN (Select id_centro_trabajo From m4t_centros_trab Where id_area_generadora = ( Select id_area_generadora From m4t_delegaciones Where id_div_geografica = ? ) )\r\n"
												+ "And fec_paga IN (Select fec_pago From gys_fechas_control Where anio_ejercicio = ? And mes_ejercicio = ?)";
	double ObtenerSaldoUtilizadoExt(String idDelegacion, int anio_ejercicio, int mes_ejercicio);

	public String QUERY_GET_SALDO_GUARDIA_EXT_CT = "Select NVL(SUM(importe), 0) importe\r\n"
												+ "From gys_guardias_ext\r\n"
												+ "Where id<>? And id_centro_trabajo = ?\r\n"
												+ "And fec_paga IN (Select fec_pago From gys_fechas_control Where anio_ejercicio = ? And mes_ejercicio = ? And quincena = ?)";
	double ObtenerSaldoUtilizadoExt_ct(int id, String id_centro_trabajo, int anio_ejercicio, int mes_ejercicio, int quincena);

	public String QUERY_GET_GUARDIA_EXTERNA     = "Select G.id, rfc As Clave_empleado, G.id_centro_trabajo, G.id_clave_servicio, G.id_puesto_plaza, 'GE' tipo_guardia,\r\n"
												+ "G.id_nivel, G.id_sub_nivel, G.id_tipo_jornada, G.horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment, hora_inicio, hora_fin,\r\n"
												+ "G.importe, PU.id_tipo_tabulador, G.fec_paga, C.id_zona, P.estatus, G.id_ordinal, NVL(riesgos,0) riesgos, G.id_usuario\r\n"
												+ "From gys_guardias_ext G, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU \r\n"
												+ "Where G.fec_paga = P.fec_pago And \r\n"
												+ "G.id_centro_trabajo = C.id_centro_trabajo And \r\n"
												+ "G.id_puesto_plaza = PU.id_puesto_plaza And \r\n"
												+ "PU.id_sociedad = '01' And PU.id_empresa = '01' And \r\n"
												+ "rfc = ? \r\n"
												+ "Order by G.fec_paga desc, G.fec_inicio";
	List<DatosGuardia> ConsultaGuardiasExternas(String rfcEmpleado);

	public String QUERY_GET_GUARDIAS_EXT_X_FECHA = "Select G.id, rfc clave_empleado, G.id_centro_trabajo, id_clave_servicio, G.id_puesto_plaza, 'GE' tipo_guardia,\r\n"
												+ "id_nivel, id_sub_nivel, id_tipo_jornada, horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment, hora_inicio, hora_fin,\r\n"
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

	public String QUERY_GET_GUARDIAS_EXT_BY_ID  = "Select G.id, rfc clave_empleado, G.id_centro_trabajo, id_clave_servicio, G.id_puesto_plaza, 'GE' tipo_guardia,\r\n"
												+ "id_nivel, id_sub_nivel, id_tipo_jornada, horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment, hora_inicio, hora_fin,\r\n"
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

	public String QUERY_EXISTS_GUARDIA_INT      = "Select COUNT(*) \r\n"
												+ "From gys_guardias_emp\r\n"
												+ "Where id_empleado = ? And fec_inicio = ?\r\n"
												+ "  And (((hora_inicio>=?) And (hora_inicio <= ?)) \r\n"
												+ "    Or ((hora_fin>=?)    And (hora_fin <= ?))    \r\n"
												+ "    Or ((hora_fin>=?)    And (hora_inicio <= ?)))";
	public int existe_guardia(DatosGuardia guardia);

	public String QUERY_EXISTS_GUARDIA_INT_UPD  = "Select COUNT(*) \r\n"
												+ "From gys_guardias_emp\r\n"
												+ "Where id_empleado = ? And fec_inicio = ?\r\n"
												+ "  And id<>?\r\n"
												+ "  And (((hora_inicio>=?) And (hora_inicio <= ?)) \r\n"
												+ "    Or ((hora_fin>=?)    And (hora_fin <= ?))    \r\n"
												+ "    Or ((hora_fin>=?)    And (hora_inicio <= ?)))";
	public int existe_guardia_upd(DatosGuardia guardia);

	public String QUERY_EXISTS_GUARDIA_EXT      = "Select COUNT(*) \r\n"
												+ "From gys_guardias_ext\r\n"
												+ "Where rfc = ? And fec_inicio = ?\r\n"
												+ "  And (((hora_inicio>=?) And (hora_inicio <= ?)) \r\n"
												+ "    Or ((hora_fin>=?)    And (hora_fin <= ?))    \r\n"
												+ "    Or ((hora_fin>=?)    And (hora_inicio <= ?)))";
	public int existe_guardia_ext(DatosGuardia guardia);

	public String QUERY_EXISTS_GUARDIA_EXT_UPD  = "Select COUNT(*) \r\n"
												+ "From gys_guardias_ext\r\n"
												+ "Where rfc = ? And fec_inicio = ?\r\n"
												+ "  And id<>?\r\n"
												+ "  And (((hora_inicio>=?) And (hora_inicio <= ?)) \r\n"
												+ "    Or ((hora_fin>=?)    And (hora_fin <= ?))    \r\n"
												+ "    Or ((hora_fin>=?)    And (hora_inicio <= ?)))";
	public int existe_guardia_ext_upd(DatosGuardia guardia);

	/*
	 * 
	 * Consultas para validar los topes de las horas/días de guardias
	 * 
	 */

	public String QUERY_GET_HORAS_GUARDIA_INT   = "Select SUM(horas) \r\n"
												+ "From gys_guardias_emp\r\n"
												+ "Where id_empleado = ? \r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?)) \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))    \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int get_horas_guardia(String clave_empleado, String inicio, String fin);

	public String QUERY_GET_HORAS_GUARDIA_INT_UPD = "Select SUM(horas) \r\n"
												+ "From gys_guardias_emp\r\n"
												+ "Where id_empleado = ? \r\n"
												+ "  And id<>? \r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?)) \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))    \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int get_horas_guardia_upd(String clave_empleado, Integer id, String inicio, String fin);

	public String QUERY_GET_HORAS_GUARDIA_EXT   = "Select SUM(horas) \r\n"
												+ "From gys_guardias_ext\r\n"
												+ "Where rfc = ? \r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?)) \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))    \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int get_horas_guardia_ext(String clave_empleado, String inicio, String fin);

	public String QUERY_GET_HORAS_GUARDIA_EXT_UPD = "Select SUM(horas)                                                                 \r\n"
												+ "From gys_guardias_ext\r\n"
												+ "Where rfc = ? \r\n"
												+ "  And id<>? \r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?)) \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))    \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))";
	public int get_horas_guardia_ext_upd(String clave_empleado, Integer id, String inicio, String fin);

	public String QUERY_GET_DIAS_GUARDIA_INT   = "Select Count(Distinct fec_inicio) \r\n"
												+ "From gys_guardias_emp\r\n"
												+ "Where id_empleado = ?\r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?))\r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))   \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))\r\n"
												+ "  And fec_inicio <> ?";
	public int get_dias_guardia(String clave_empleado, String inicio, String fin, String inicio1);

	public String QUERY_GET_DIAS_GUARDIA_INT_UPD = "Select Count(Distinct fec_inicio) \r\n"
												+ "From gys_guardias_emp\r\n"
												+ "Where id_empleado = ?\r\n"
												+ "  And id<>? \r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?))\r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))   \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))\r\n"
												+ "  And fec_inicio <> ?";
	public int get_dias_guardia_upd(String clave_empleado, Integer id, String inicio, String fin, String inicio1);

	public String QUERY_GET_DIAS_GUARDIA_EXT    = "Select Count(Distinct fec_inicio) \r\n"
												+ "From gys_guardias_ext\r\n"
												+ "Where rfc = ?\r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?))\r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))   \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))\r\n"
												+ "  And fec_inicio <> ?";
	public int get_dias_guardia_ext(String clave_empleado, String inicio, String fin, String inicio1);

	public String QUERY_GET_DIAS_GUARDIA_EXT_UPD = "Select Count(Distinct fec_inicio) \r\n"
												+ "From gys_guardias_ext\r\n"
												+ "Where rfc = ?\r\n"
												+ "  And (((fec_inicio>=?) And (fec_inicio <= ?))\r\n"
												+ "    Or ((fec_fin>=?)    And (fec_fin <= ?))   \r\n"
												+ "    Or ((fec_fin>=?)    And (fec_inicio <= ?)))\r\n"
												+ "  And fec_inicio <> ?";
	public int get_dias_guardia_ext_upd(String clave_empleado, Integer id, String inicio, String fin, String inicio1);

	public String STMT_UPDATE_STATUS 			= "UPDATE gys_guardias_emp Set estatus = ?\r\n"
												+ "Where id = ?";
	int updateStatusGuardia(int status, int id);

	public String STMT_UPDATE_STATUS_EXT 		= "UPDATE gys_guardias_ext Set estatus = ?\r\n"
												+ "Where id = ?";
	int updateStatusGuardiaExt(int status, int id);

	public String STMT_UPDATE_AUTH_STATUS_1		= "UPDATE gys_autorizacion_guardias Set estatus1 = ?, comentarios1 = ?, id_usuario1 = ?, fec_validacion = CURRENT YEAR TO SECOND\r\n"
												+ "Where id_guardia = ? And id_tipo = ?";
	int updateAuthStatusGuardia1(int status, int id, String tipo, String comentarios, int idUsuario);

	public String STMT_UPDATE_AUTH_STATUS_2		= "UPDATE gys_autorizacion_guardias Set estatus2 = ?, comentarios2 = ?, id_usuario2 = ?, fec_autorizacion = CURRENT YEAR TO SECOND\r\n"
												+ "Where id_guardia = ? And id_tipo = ?";
	int updateAuthStatusGuardia2(int status, int id, String tipo, String comentarios, int idUsuario);

//	public String STMT_UPDATES_AUTH_STATUS_1	= "Merge Into gys_autorizacion_guardias A\r\n"
//												+ "Using gys_guardias_emp S\r\n"
//												+ "  ON  A.id_guardia = S.id\r\n"
//												+ "  And A.estatus1 = 0 And id_tipo = ?\r\n"
//												+ "  And S.fec_paga = ?\r\n"
//												+ "  And S.id_centro_trabajo in (select id_centro_trabajo from m4t_centros_trab where id_div_geografica = ? )\r\n"
//												+ "WHEN MATCHED THEN\r\n"
//												+ "    UPDATE SET estatus1 = 1, id_usuario1 = ?, fec_validacion = CURRENT YEAR TO SECOND";
	public String STMT_UPDATES_AUTH_STATUS_1	= "MERGE INTO gys_autorizacion_guardias AS A\r\n"
												+ "USING (\r\n"
												+ "		SELECT S.id, S.id_centro_trabajo, C.id_area_generadora, A.estatus1, A.id_tipo, S.fec_paga\r\n"
												+ "		FROM gys_guardias_emp S\r\n"
												+ "		INNER JOIN m4t_centros_trab C\r\n"
												+ "			ON C.id_centro_trabajo = S.id_centro_trabajo\r\n"
												+ "    	INNER JOIN m4t_conv_ct V\r\n"
												+ "        ON C.id_centro_trabajo = V.ct5\r\n"
												+ "    	INNER JOIN gys_autorizacion_guardias A\r\n"
												+ "        ON A.id_guardia = S.id\r\n"
												+ "		WHERE A.estatus1 = 0\r\n"
												+ "			AND A.id_tipo = ? \r\n"
												+ " 		AND S.fec_paga = ?\r\n"
												+ " 		AND C.id_area_generadora = ? \r\n"
												+ "        	AND C.id_tipo_ct IN (SELECT DISTINCT id_tipo_ct FROM m4t_gys_matriz_puestos)\r\n"
												+ "        	AND C.id_area_generadora = (SELECT id_area_generadora FROM m4t_delegaciones WHERE id_div_geografica = ?)\r\n"
												+ ") AS Source\r\n"
												+ "ON A.id_guardia = Source.id\r\n"
												+ "AND A.id_tipo = 'GI'\r\n"
												+ "WHEN MATCHED THEN\r\n"
												+ " UPDATE SET\r\n"
												+ "		A.estatus1 = 1,\r\n"
												+ "		A.id_usuario1 = ?,\r\n"
												+ "		A.fec_validacion = CURRENT YEAR TO SECOND";
	int updateAuthStatusGuardias1(String tipo, String fec_pago, String idDeleg, String idDeleg1, int idUsuario);

//	public String STMT_UPDATES_AUTH_STATUS_1ext	= "Merge Into gys_autorizacion_guardias A\r\n"
//												+ "Using gys_guardias_ext S\r\n"
//												+ "  ON  A.id_guardia = S.id\r\n"
//												+ "  And A.estatus1 = 0 And id_tipo = ?\r\n"
//												+ "  And S.fec_paga = ?\r\n"
//												+ "  And S.id_centro_trabajo in (select id_centro_trabajo from m4t_centros_trab where id_div_geografica = ? )\r\n"
//												+ "WHEN MATCHED THEN\r\n"
//												+ "    UPDATE SET estatus1 = 1, id_usuario1 = ?, fec_validacion = CURRENT YEAR TO SECOND";
public String STMT_UPDATES_AUTH_STATUS_1ext	= "MERGE INTO gys_autorizacion_guardias AS A\r\n"
											+ "USING (\r\n"
											+ "		SELECT S.id, S.id_centro_trabajo, C.id_area_generadora, A.estatus1, A.id_tipo, S.fec_paga\r\n"
											+ "		FROM gys_guardias_ext S\r\n"
											+ "		INNER JOIN m4t_centros_trab C\r\n"
											+ "			ON C.id_centro_trabajo = S.id_centro_trabajo\r\n"
											+ "    	INNER JOIN m4t_conv_ct V\r\n"
											+ "        ON C.id_centro_trabajo = V.ct5\r\n"
											+ "    	INNER JOIN gys_autorizacion_guardias A\r\n"
											+ "        ON A.id_guardia = S.id\r\n"
											+ "		WHERE A.estatus1 = 0\r\n"
											+ "			AND A.id_tipo = ? \r\n"
											+ " 		AND S.fec_paga = ?\r\n"
											+ " 		AND C.id_area_generadora = ? \r\n"
											+ "        	AND C.id_tipo_ct IN (SELECT DISTINCT id_tipo_ct FROM m4t_gys_matriz_puestos)\r\n"
											+ "        	AND C.id_area_generadora = (SELECT id_area_generadora FROM m4t_delegaciones WHERE id_div_geografica = ?)\r\n"
											+ ") AS Source\r\n"
											+ "ON A.id_guardia = Source.id\r\n"
											+ "AND A.id_tipo = 'GE'\r\n"
											+ "WHEN MATCHED THEN\r\n"
											+ " UPDATE SET\r\n"
											+ "		A.estatus1 = 1,\r\n"
											+ "		A.id_usuario1 = ?,\r\n"
											+ "		A.fec_validacion = CURRENT YEAR TO SECOND";
	int updateAuthStatusGuardias1Ext(String tipo, String fec_pago, String idDeleg, String idDeleg1, int idUsuario);

	public String STMT_COUNT_AUTH_STATUS_INT = "SELECT COUNT(*) as gua_reg_int_conf\r\n"
										 + "  FROM gys_autorizacion_guardias A\r\n"
										 + "  INNER JOIN gys_guardias_emp S\r\n"
										 + "    ON A.id_guardia = S.id \r\n"
										 + "  INNER JOIN m4t_centros_trab C \r\n"
										 + "  ON C.id_centro_trabajo = S.id_centro_trabajo\r\n"
										 + "  INNER JOIN m4t_conv_ct V\r\n"
										 + "  ON C.id_centro_trabajo = V.ct5\r\n"
										 + "  WHERE fec_pago = ?\r\n"
										 + "  	and id_tipo='GI'\r\n"
										 + " 	 and C.id_area_generadora = ?\r\n"
										 + "  	and C.id_tipo_ct IN (SELECT DISTINCT id_tipo_ct FROM m4t_gys_matriz_puestos)\r\n"
										 + "  	and C.id_area_generadora = (SELECT id_area_generadora FROM m4t_delegaciones WHERE id_div_geografica = ?)\r\n"
										 + "  AND estatus1 IN (0)\r\n";

	Integer countAuthGuardiasStatusInt(String fec_pago, String IdDeleg);

	public String STMT_COUNT_AUTH_STATUS_EXT = "SELECT COUNT(*) as gua_reg_ext_conf\r\n"
											+ "  FROM gys_autorizacion_guardias A\r\n"
											+ "  INNER JOIN gys_guardias_ext S\r\n"
											+ "    ON A.id_guardia = S.id \r\n"
											+ "  INNER JOIN m4t_centros_trab C \r\n"
											+ "  ON C.id_centro_trabajo = S.id_centro_trabajo\r\n"
											+ "  INNER JOIN m4t_conv_ct V\r\n"
											+ "  ON C.id_centro_trabajo = V.ct5\r\n"
											+ "  WHERE fec_pago = ?\r\n"
											+ "  	and id_tipo='GE'\r\n"
											+ " 	 and C.id_area_generadora = ?\r\n"
											+ "  	and C.id_tipo_ct IN (SELECT DISTINCT id_tipo_ct FROM m4t_gys_matriz_puestos)\r\n"
											+ "  	and C.id_area_generadora = (SELECT id_area_generadora FROM m4t_delegaciones WHERE id_div_geografica = ?)\r\n"
											+ "  AND estatus1 IN (0)\r\n";

	Integer countAuthGuardiasStatusExt(String fec_pago, String IdDeleg);
	//	public String STMT_UPDATES_AUTH_STATUS_2	= "Merge Into gys_autorizacion_guardias A\r\n"
//												+ "Using gys_guardias_emp S\r\n"
//												+ "  ON  A.id_guardia = S.id\r\n"
//												+ "  And A.estatus1 = 1 And A.estatus2 = 0\r\n"
//												+ "  And id_tipo = ? And S.fec_paga = ?\r\n"
//												+ "  And S.id_centro_trabajo in (select id_centro_trabajo from m4t_centros_trab where id_div_geografica = ? )\r\n"
//												+ "WHEN MATCHED THEN\r\n"
//												+ "    UPDATE SET estatus2 = 3, id_usuario2 = ?, fec_autorizacion = CURRENT YEAR TO SECOND";
public String STMT_UPDATES_AUTH_STATUS_2	= "MERGE INTO gys_autorizacion_guardias AS A\r\n"
											+ "USING (\r\n"
											+ "		SELECT S.id, S.id_centro_trabajo, C.id_area_generadora, A.estatus1, A.id_tipo, S.fec_paga\r\n"
											+ "		FROM gys_guardias_emp S\r\n"
											+ "		INNER JOIN m4t_centros_trab C\r\n"
											+ "			ON C.id_centro_trabajo = S.id_centro_trabajo\r\n"
											+ "    INNER JOIN m4t_conv_ct V\r\n"
											+ "        ON C.id_centro_trabajo = V.ct5\r\n"
											+ "    	INNER JOIN gys_autorizacion_guardias A\r\n"
											+ "        ON A.id_guardia = S.id\r\n"
											+ "		WHERE A.estatus1 = 1\r\n"
											+ "			AND A.estatus2 = 0\r\n"
											+ "			AND A.id_tipo = ? \r\n"
											+ " 		AND S.fec_paga = ?\r\n"
											+ " 		AND C.id_area_generadora = ? \r\n"
											+ "        	AND C.id_tipo_ct IN (SELECT DISTINCT id_tipo_ct FROM m4t_gys_matriz_puestos)\r\n"
											+ "        	AND C.id_area_generadora = (SELECT id_area_generadora FROM m4t_delegaciones WHERE id_div_geografica = ?)\r\n"
											+ ") AS Source\r\n"
											+ "ON A.id_guardia = Source.id\r\n"
											+ "AND A.id_tipo = 'GI'\r\n"
											+ "WHEN MATCHED THEN\r\n"
											+ " UPDATE SET\r\n"
											+ "		A.estatus2 = 3,\r\n"
											+ "		A.id_usuario2 = ?,\r\n"
											+ "		A.fec_autorizacion = CURRENT YEAR TO SECOND";
	int updateAuthStatusGuardias2(String tipo, String fec_pago, String idDeleg, String idDeleg1, int idUsuario);

//	public String STMT_UPDATES_AUTH_STATUS_2ext	= "Merge Into gys_autorizacion_guardias A\r\n"
//												+ "Using gys_guardias_ext S\r\n"
//												+ "  ON  A.id_guardia = S.id\r\n"
//												+ "  And A.estatus1 = 1 And A.estatus2 = 0\r\n"
//												+ "  And id_tipo = ? And S.fec_paga = ?\r\n"
//												+ "  And S.id_centro_trabajo in (select id_centro_trabajo from m4t_centros_trab where id_div_geografica = ? )\r\n"
//												+ "WHEN MATCHED THEN\r\n"
//												+ "    UPDATE SET estatus2 = 3, id_usuario2 = ?, fec_autorizacion = CURRENT YEAR TO SECOND";
	public String STMT_UPDATES_AUTH_STATUS_2ext	= "MERGE INTO gys_autorizacion_guardias AS A\r\n"
												+ "USING (\r\n"
												+ "		SELECT S.id, S.id_centro_trabajo, C.id_area_generadora, A.estatus1, A.id_tipo, S.fec_paga\r\n"
												+ "		FROM gys_guardias_ext S\r\n"
												+ "		INNER JOIN m4t_centros_trab C\r\n"
												+ "			ON C.id_centro_trabajo = S.id_centro_trabajo\r\n"
												+ "    INNER JOIN m4t_conv_ct V\r\n"
												+ "        ON C.id_centro_trabajo = V.ct5\r\n"
												+ "    	INNER JOIN gys_autorizacion_guardias A\r\n"
												+ "        ON A.id_guardia = S.id\r\n"
												+ "		WHERE A.estatus1 = 1\r\n"
												+ "			AND A.estatus2 = 0 \r\n"
												+ "			AND A.id_tipo = ? \r\n"
												+ " 		AND S.fec_paga = ?\r\n"
												+ " 		AND C.id_area_generadora = ? \r\n"
												+ "        	AND C.id_tipo_ct IN (SELECT DISTINCT id_tipo_ct FROM m4t_gys_matriz_puestos)\r\n"
												+ "        	AND C.id_area_generadora = (SELECT id_area_generadora FROM m4t_delegaciones WHERE id_div_geografica = ?)\r\n"
												+ ") AS Source\r\n"
												+ "ON A.id_guardia = Source.id\r\n"
												+ "AND A.id_tipo = 'GE'\r\n"
												+ "WHEN MATCHED THEN\r\n"
												+ " UPDATE SET\r\n"
												+ "		A.estatus2 = 3,\r\n"
												+ "		A.id_usuario2 = ?,\r\n"
												+ "		A.fec_autorizacion = CURRENT YEAR TO SECOND";
	int updateAuthStatusGuardias2Ext(String tipo, String fec_pago, String idDeleg, String idDeleg1, int idUsuario);

	List<DatosGuardia> ConsultaDynamicGuardias(String fechaPago, String tipo, String clave_empleado, Double importe_inicio, Double importe_fin,
			String idDelegacion, String idCentroTrab, String claveServicio, String puesto, Integer estatus) throws SQLException;

	List<DatosGuardia> ConsultaDynamicGuardiasPage(String fechaPago, String tipo, String clave_empleado, Double importe_inicio, Double importe_fin,
											   String idDelegacion, String idCentroTrab, String claveServicio, String puesto, Integer estatus, int page, int size) throws SQLException;


	Long ConsultaDynamicGuardiasCount(String fechaPago, String tipo, String clave_empleado, Double importe_inicio, Double importe_fin,
									  String idDelegacion, String idCentroTrab, String claveServicio, String puesto, Integer estatus) throws SQLException;


	List<DatosGuardia> ConsultaDynamicAuthGuardias(String fechaPago, String tipo, String idDelegacion, String idCentroTrab, Integer estatus);

}