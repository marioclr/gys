package gob.issste.gys.repository;

import java.sql.SQLException;
import java.util.List;

import gob.issste.gys.model.Delegacion;
import gob.issste.gys.model.DelegacionPorFecha;
import gob.issste.gys.model.Paga;

public interface IPagaRepository {

	/*
	 * Bloque de operaciones del Repositorio de Fechas de Control de pagos para las
	 * Operaciones básicas del CRUD
	 */
	public String QUERY_ADD_NEW_PAGAS      = "INSERT INTO gys_fechas_control ( fec_pago, descripcion, estatus, fec_inicio, fec_fin, "
										   + "anio_ejercicio, mes_ejercicio, quincena, id_tipo_paga, idnivelvisibilidad, programas, id_usuario ) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
	int save(Paga tipMovPresup) throws SQLException;

	public String QUERY_UPDATE_PAGAS       = "UPDATE gys_fechas_control Set descripcion = ?, estatus = ?, fec_inicio = ?, fec_fin = ?, anio_ejercicio = ?, "
										   + "mes_ejercicio = ?, quincena = ?, id_tipo_paga = ?, idnivelvisibilidad = ?, programas = ?, id_usuario = ? Where id = ?";
	int update(Paga tipMovPresup);

	public String QUERY_DELETE_PAGAS_BY_ID = "DELETE FROM gys_fechas_control WHERE id=?";
	int deleteById(int id);

	public String QUERY_FIND_PAGAS_BY_ID   = "SELECT * FROM gys_fechas_control WHERE id=?";
	Paga findById(int id);

	public String QUERY_GET_ALL_PAGAS      = "SELECT * FROM gys_fechas_control Order By fec_pago Desc";
	List<Paga> findAll();

	public String QUERY_FIND_PAGAS_BY_DESC = "SELECT * from gys_fechas_control WHERE Descripcion like ?";
	List<Paga> findByDesc(String desc);

	public String QUERY_FIND_PAGAS_BY_FEC  = "SELECT * from gys_fechas_control WHERE fec_pago = ?";
	Paga findByFecha(String fecha_pago);

	public String QUERY_ACTIVATE_PAGAS     = "UPDATE gys_fechas_control Set estatus = ? Where id = ?";
	int activate(Paga tipMovPresup);	

	public String QUERY_GET_ACTIVE_PAGAS   = "SELECT * from gys_fechas_control WHERE estatus = 1";
	List<Paga> findActivePagas();

//	public String QUERY_GET_ACTIVE_PAGAS_BY_USR = "Select *\r\n"
//											+ "From gys_fechas_control A, gys_DelegacionesPorFecha B\r\n"
//											+ "Where A.id = B.IdFecha\r\n"
//											+ "  And A.estatus = 1\r\n"
//											+ "  And (B.IdDelegacion = '00'\r\n"
//											+ "    Or B.IdDelegacion IN (\r\n"
//											+ "      Select iddelegacion From gys_Usuarios\r\n"
//											+ "      Where idusuario = ?\r\n"
//											+ "    )\r\n"
//											+ "  )";
//List<Paga> findActivePagasByUser(String idDivGeo);
	public String QUERY_GET_ACTIVE_PAGAS_BY_DEL = "Select *\r\n"
			+ "From gys_fechas_control A, gys_DelegacionesPorFecha B\r\n"
			+ "Where A.id = B.IdFecha\r\n"
			+ "  And B.estatus = 1\r\n"
			+ "  And B.IdDelegacion = ?";

	List<Paga> findActivePagasByDel(String idDivGeo);

	public String QUERY_GET_PAGAS_BY_STATUS = "SELECT * From gys_fechas_control\r\n"
											+ "WHERE anio_ejercicio = ?\r\n"
											+ "  And mes_ejercicio = ?\r\n"
											+ "  And id_tipo_paga = ?\r\n"
											+ "  And estatus = ?";
	List<Paga> findByStatus(int anio, int mes, int tipo, int status);

	public String QUERY_GET_PAGAS_BY_STATUS_DEL = "Select A.id, A.fec_pago, A.descripcion, B.estatus, A.fec_inicio, A.fec_fin, A.anio_ejercicio, A.mes_ejercicio, A.quincena, A.id_tipo_paga, A.idnivelvisibilidad, A.programas, A.id_usuario, A.fec_ult_actualizacion\r\n"
												+ "From gys_fechas_control A, gys_DelegacionesPorFecha B\r\n"
												+ "Where A.id = B.IdFecha \r\n"
												+ "  And B.estatus = ? \r\n"
												+ "  And B.IdDelegacion = ?";
	List<Paga> findByStatusByDeleg(int status, String idDeleg);

	public String STMT_UPDATE_STATUS 		= "UPDATE gys_fechas_control Set estatus = ?\r\n"
			   								+ "Where estatus = 3 And anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = ?";
	int updateStatus(int status, int anio, int mes, int tipo);

	public String STMT_UPDATE_STATUS_REC    = "UPDATE gys_fechas_control Set estatus = ?\r\n"
											+ "Where estatus = 3 And anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = ?\r\n"
											+ "  And fec_pago >= ? And fec_pago <= ?";
	int updateStatus(int status, int anio, int mes, int tipo, String fec_min, String fec_max);

	/*
	 * Bloque de operaciones del Repositorio de Fechas de Control de pagos
	 * para realizar las Validaciones necesarias para la gestión de fechas de control
	 */
	public String QUERY_EXISTS_PAGA_ABIERTA         = "Select COUNT(*)\r\n"
													+ "From gys_fechas_control\r\n"
													+ "Where anio_ejercicio = ?\r\n"
													+ "  And mes_ejercicio = ?\r\n"
													+ "  And id_tipo_paga = ?\r\n"
													+ "  And estatus IN (0,1)";
	public int existe_abierta(Paga paga);

	public String QUERY_EXISTS_PAGA_ABIERTA_AL_CAMBIAR = "Select COUNT(*)\r\n"
													+ "From gys_fechas_control\r\n"
													+ "Where id <> ?\r\n"
													+ "  And anio_ejercicio = ?\r\n"
													+ "  And mes_ejercicio = ?\r\n"
													+ "  And id_tipo_paga = ?\r\n"
													+ "  And estatus IN (0,1)";
	public int existe_abierta_al_cambiar(Paga paga);

	public String QUERY_EXISTS_FECHA_EN_ISR         = "Select COUNT(*)\r\n"
													+ "From gys_externos_isr2\r\n"
													+ "Where (((fec_min                     >= ?) And (fec_min                     <= ?))\r\n"
													+ "    Or ((NVL(fec_max, date(current)) >= ?) And (NVL(fec_max, date(current)) <= ?))\r\n"
													+ "    Or ((NVL(fec_max, date(current)) >= ?) And (fec_min                     <= ?)))";
	public int existe_fecha_en_isr(Paga paga);

	public String QUERY_EXISTS_FECHA_EN_CALCULO_ISR = "Select COUNT(*)\r\n"
													+ "From gys_externos_isr2\r\n"
													+ "Where anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = ? And id_ordinal <> ?\r\n"
													+ "  And (((fec_min                     >= ?) And (fec_min                     <= ?))\r\n"
													+ "    Or ((NVL(fec_max, date(current)) >= ?) And (NVL(fec_max, date(current)) <= ?))\r\n"
													+ "    Or ((NVL(fec_max, date(current)) >= ?) And (fec_min                     <= ?)))";
	public int existe_fecha_en_calculo_isr(String fecPaga, Integer anio, Integer mes, Integer tipoFechaControl, Integer id_ordinal);

	public String QUERY_EXISTS_ANT_SIN_TERM_NON		= "Select COUNT(*)\r\n"
													+ "From gys_fechas_control\r\n"
													+ "Where ( ( anio_ejercicio = ? And mes_ejercicio < ? And id_tipo_paga = 1 And estatus <> 7 )\r\n"
													+ "    Or ( anio_ejercicio = ? And mes_ejercicio < ? And estatus <> 7 )\r\n"
													+ "    Or ( anio_ejercicio < ? And estatus <> 7 ) )";
	public int existe_anterior_sin_terminar_non(Paga paga);

	public String QUERY_EXISTS_ANT_SIN_TERM_PAR		= "Select COUNT(*)\r\n"
													+ "From gys_fechas_control\r\n"
													+ "Where ( ( anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = 4 And estatus <> 7 )\r\n"
													+ "    Or ( anio_ejercicio = ? And mes_ejercicio < ? And estatus <> 7 )\r\n"
													+ "    Or ( anio_ejercicio < ? And estatus <> 7 ) )";
	public int existe_anterior_sin_terminar_par(Paga paga);

	public String QUERY_EXISTS_FECHA_POSTERIOR		= "Select COUNT(*)\r\n"
													+ "From gys_fechas_control\r\n"
													+ "Where fec_pago > ?";
	public int existe_fecha_post_en_pagas(Paga paga);

	/* 
	 * Bloque de operaciones del Repositorio de Fechas de Control de pagos para gestionar la Visibilidad
	 * de cada fecha de control, creada dentro del sistema.
	 */
    public String QUERY_ADD_DELEG_X_FECHA	= "Insert Into gys_DelegacionesPorFecha ( IdFecha, IdDelegacion, estatus, id_usuario ) Values ( ?, ?, ?, ? )";
    int saveDelegForFecha(int IdFecha, String IdDeleg, int estatus, String id_usuario);

    public String QUERY_DEL_FOR_FECHA       = "Delete gys_DelegacionesPorFecha Where IdFecha=?";
    int removeDelegForFecha(int IdFecha);

    public String QUERY_GET_DEL_FECHA		= "Select id_div_geografica, n_div_geografica, estatus\r\n"
								    		+ "From gys_DelegacionesPorFecha F, m4t_delegaciones D\r\n"
								    		+ "Where F.IdDelegacion = D.id_div_geografica\r\n"
								    		+ "  And IdFecha = ?";
    List<DelegacionPorFecha> getDelegForFecha(int IdFecha);

    /*
     * Bloque de operaciones del Repositorio de Fechas de Control de pagos, para gestionar la fase de
     * Validación de guardias y suplencias para la autorización y confirmación de cada registro de
     * Guardias y Suplecias 
     */
	public String STMT_INSERT_GUARD_INT_AUT	= "Insert Into gys_autorizacion_guardias\r\n"
											+ "(id_guardia, fec_pago, id_tipo, estatus1, comentarios1, id_usuario1, fec_validacion,\r\n"
											+ "estatus2, comentarios2, id_usuario2, fec_autorizacion)\r\n"
											+ "Select id, fec_paga, 'GI' id_tipo,\r\n"
											+ "  0 estatus1, '' comentarios1, '' id_usuario1, '' fec_validacion, \r\n"
											+ "  0 estatus2, '' comentarios2, '' id_usuario2, '' fec_autorizacion\r\n"
											+ "From gys_guardias_emp\r\n"
											+ "Where fec_paga = ?";
	int AuthGuardiasInt(Paga paga);

	public String STMT_INSERT_GUARD_INT_AUT_X_DEL	= "Insert Into gys_autorizacion_guardias\r\n"
													+ "(id_guardia, fec_pago, id_tipo, estatus1, comentarios1, id_usuario1, fec_validacion,\r\n"
													+ "estatus2, comentarios2, id_usuario2, fec_autorizacion)\r\n"
													+ "	Select G.id, G.fec_paga, 'GI' id_tipo, 0 estatus1, '' comentarios1, '' id_usuario1, '' fec_validacion, 0 estatus2, '' comentarios2, '' id_usuario2, '' fec_autorizacion\r\n"
													+ "		From gys_guardias_emp G, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU, m4t_clave_servicio SE, m4t_delegaciones D, gys_delegacionesporfecha DF\r\n"
													+ "	Where G.fec_paga = P.fec_pago And \r\n"
													+ "		P.id = DF.idfecha And \r\n"
													+ "		DF.iddelegacion = D.id_div_geografica And \r\n"
													+ "		G.id_centro_trabajo = C.id_centro_trabajo And \r\n"
													+ "		G.id_puesto_plaza = PU.id_puesto_plaza And \r\n"
													+ "		PU.id_sociedad = '01' And PU.id_empresa = '01' And \r\n"
													+ "		G.id_clave_servicio = SE.id_clave_servicio And \r\n"
													+ "		SE.id_empresa='01' And \r\n"
													+ "		D.id_area_generadora = C.id_area_generadora And \r\n"
													+ "		C.id_area_generadora = ( Select id_area_generadora From m4t_delegaciones Where id_div_geografica = ? ) And\r\n"
													+ "		DF.idfecha = ? \r\n"
													+ "Order by G.fec_paga desc, G.fec_inicio";
	int AuthGuardiasIntForDeleg( String idDeleg, int idFecha);

	public String STMT_INSERT_GUARD_EXT_AUT	= "Insert Into gys_autorizacion_guardias\r\n"
											+ "(id_guardia, fec_pago, id_tipo, estatus1, comentarios1, id_usuario1, fec_validacion,\r\n"
											+ "estatus2, comentarios2, id_usuario2, fec_autorizacion)\r\n"
											+ "Select id, fec_paga, 'GE' id_tipo,\r\n"
											+ "  0 estatus1, '' comentarios1, '' id_usuario1, '' fec_validacion, \r\n"
											+ "  0 estatus2, '' comentarios2, '' id_usuario2, '' fec_autorizacion\r\n"
											+ "From gys_guardias_ext\r\n"
											+ "Where fec_paga = ?";
	int AuthGuardiasExt(Paga paga);

	public String STMT_INSERT_GUARD_EXT_AUT_X_DEL	= "Insert Into gys_autorizacion_guardias\r\n"
													+ "(id_guardia, fec_pago, id_tipo, estatus1, comentarios1, id_usuario1, fec_validacion,\r\n"
													+ "estatus2, comentarios2, id_usuario2, fec_autorizacion)\r\n"
													+ "	Select G.id, G.fec_paga, 'GE' id_tipo, 0 estatus1, '' comentarios1, '' id_usuario1, '' fec_validacion, 0 estatus2, '' comentarios2, '' id_usuario2, '' fec_autorizacion\r\n"
													+ "		From gys_guardias_ext G, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU, m4t_clave_servicio SE, m4t_delegaciones D, gys_delegacionesporfecha DF\r\n"
													+ "	Where G.fec_paga = P.fec_pago And \r\n"
													+ "		P.id = DF.idfecha And \r\n"
													+ "		DF.iddelegacion = D.id_div_geografica And \r\n"
													+ "		G.id_centro_trabajo = C.id_centro_trabajo And \r\n"
													+ "		G.id_puesto_plaza = PU.id_puesto_plaza And \r\n"
													+ "		PU.id_sociedad = '01' And PU.id_empresa = '01' And \r\n"
													+ "		G.id_clave_servicio = SE.id_clave_servicio And \r\n"
													+ "		SE.id_empresa='01' And \r\n"
													+ "		D.id_area_generadora = C.id_area_generadora And \r\n"
													+ "		C.id_area_generadora = ( Select id_area_generadora From m4t_delegaciones Where id_div_geografica = ? )\r\n"
													+ "		And DF.idfecha = ? \r\n"
													+ "Order by G.fec_paga desc, G.fec_inicio";
	int AuthGuardiasExtForDeleg(String idDeleg, int idFecha);


	public String STMT_INSERT_SUPLE_INT_AUT = "Insert Into gys_autorizacion_suplencias\r\n"
											+ "(id_suplencia, fec_pago, id_tipo, estatus1, comentarios1, id_usuario1, fec_validacion,\r\n"
											+ "estatus2, comentarios2, id_usuario2, fec_autorizacion)\r\n"
											+ "Select id, fec_paga, 'SI' id_tipo,\r\n"
											+ "  0 estatus1, '' comentarios1, '' id_usuario1, '' fec_validacion, \r\n"
											+ "  0 estatus2, '' comentarios2, '' id_usuario2, '' fec_autorizacion\r\n"
											+ "From gys_suplencias_emp\r\n"
											+ "Where fec_paga = ?";
	int AuthSuplenciasInt(Paga paga);

	public String STMT_INSERT_SUPLE_INT_AUT_X_DEL	= "Insert Into gys_autorizacion_suplencias\r\n"
													+ "(id_suplencia, fec_pago, id_tipo, estatus1, comentarios1, id_usuario1, fec_validacion,\r\n"
													+ "estatus2, comentarios2, id_usuario2, fec_autorizacion)\r\n"
													+ "	Select S.id, S.fec_paga, 'SI' id_tipo, 0 estatus1, '' comentarios1, '' id_usuario1, '' fec_validacion, 0 estatus2, '' comentarios2, '' id_usuario2, '' fec_autorizacion\r\n"
													+ "		From gys_suplencias_emp S, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU, m4t_clave_servicio SE, m4t_delegaciones D, gys_delegacionesporfecha DF\r\n"
													+ "	Where S.fec_paga = P.fec_pago And \r\n"
													+ "		P.id = DF.idfecha And\r\n"
													+ "		DF.iddelegacion = D.id_div_geografica And\r\n"
													+ "		S.id_centro_trabajo = C.id_centro_trabajo And \r\n"
													+ "		S.id_puesto_plaza = PU.id_puesto_plaza And \r\n"
													+ "		PU.id_sociedad = '01' And PU.id_empresa = '01' And \r\n"
													+ "		S.id_clave_servicio = SE.id_clave_servicio And \r\n"
													+ "		SE.id_empresa='01' And \r\n"
													+ "		D.id_area_generadora = C.id_area_generadora And \r\n"
													+ "		C.id_area_generadora = ( Select id_area_generadora From m4t_delegaciones Where id_div_geografica = ? )\r\n"
													+ "		And DF.idfecha = ? \r\n"
													+ "Order by S.fec_paga desc, S.fec_inicio";
	int AuthSuplenciasIntForDeleg(String idDeleg, int idFecha);

	public String STMT_INSERT_SUPLE_EXT_AUT = "Insert Into gys_autorizacion_suplencias\r\n"
											+ "(id_suplencia, fec_pago, id_tipo, estatus1, comentarios1, id_usuario1, fec_validacion,\r\n"
											+ "estatus2, comentarios2, id_usuario2, fec_autorizacion)\r\n"
											+ "Select id, fec_paga, 'SE' id_tipo,\r\n"
											+ "  0 estatus1, '' comentarios1, '' id_usuario1, '' fec_validacion, \r\n"
											+ "  0 estatus2, '' comentarios2, '' id_usuario2, '' fec_autorizacion\r\n"
											+ "From gys_suplencias_ext\r\n"
											+ "Where fec_paga = ?";
	int AuthSuplenciasExt(Paga paga);

	public String STMT_INSERT_SUPLE_EXT_AUT_X_DEL	= "Insert Into gys_autorizacion_suplencias\r\n"
													+ "(id_suplencia, fec_pago, id_tipo, estatus1, comentarios1, id_usuario1, fec_validacion,\r\n"
													+ "estatus2, comentarios2, id_usuario2, fec_autorizacion)\r\n"
													+ "	Select S.id, S.fec_paga, 'SE' id_tipo, 0 estatus1, '' comentarios1, '' id_usuario1, '' fec_validacion, 0 estatus2, '' comentarios2, '' id_usuario2, '' fec_autorizacion\r\n"
													+ "		From gys_suplencias_ext S, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU, m4t_clave_servicio SE, m4t_delegaciones D,  gys_delegacionesporfecha DF\r\n"
													+ "	Where S.fec_paga = P.fec_pago And \r\n"
													+ "		P.id = DF.idfecha And\r\n"
													+ "		DF.iddelegacion = D.id_div_geografica And\r\n"
													+ "		S.id_centro_trabajo = C.id_centro_trabajo And \r\n"
													+ "		S.id_puesto_plaza = PU.id_puesto_plaza And \r\n"
													+ "		PU.id_sociedad = '01' And PU.id_empresa = '01' And \r\n"
													+ "		S.id_clave_servicio = SE.id_clave_servicio And \r\n"
													+ "		SE.id_empresa='01' And \r\n"
													+ "		D.id_area_generadora = C.id_area_generadora And \r\n"
													+ "		C.id_area_generadora = ( Select id_area_generadora From m4t_delegaciones Where id_div_geografica = ? )\r\n"
													+ "		And DF.idfecha = ? \r\n"
													+ "Order by S.fec_paga desc, S.fec_inicio";
	int AuthSuplenciasExtForDeleg(String idDeleg, int idFecha);

	public String STMT_DELETE_GUARDIAS_AUT	= "Delete From gys_autorizacion_guardias\r\n"
											+ "Where fec_pago = ?";
	int BorraAuthGuardias(Paga paga);

	//QUERY PARA BORRAR REGISTROS POR DELEGACION
	public String STMT_DELETE_GUARDIAS_INT_AUT_X_DELEG	= "DELETE FROM gys_autorizacion_guardias AG\r\n"
														+ "WHERE AG.id_guardia IN ( \r\n"
														+ "    SELECT G.id\r\n"
														+ "    FROM gys_guardias_emp G\r\n"
														+ "     INNER JOIN gys_fechas_control P ON G.fec_paga = P.fec_pago\r\n"
														+ "     INNER JOIN gys_delegacionesporfecha DF ON P.id = DF.idfecha \r\n"
														+ "     INNER JOIN m4t_delegaciones D ON DF.iddelegacion = D.id_div_geografica\r\n"
														+ "     INNER JOIN m4t_centros_trab C ON G.id_centro_trabajo = C.id_centro_trabajo\r\n"
														+ "     INNER JOIN m4t_puestos_plaza PU ON G.id_puesto_plaza = PU.id_puesto_plaza\r\n"
														+ "     INNER JOIN m4t_clave_servicio SE ON G.id_clave_servicio = SE.id_clave_servicio\r\n"
//														+ "     INNER JOIN gys_autorizacion_guardias AG ON G.id = AG.id_guardia AND AG.id_tipo in ('GI')\r\n"
														+ "     WHERE PU.id_sociedad = '01'\r\n"
														+ "       AND PU.id_empresa = '01'\r\n"
														+ "       AND SE.id_empresa = '01'\r\n"
														+ "       AND DF.idfecha = ?\r\n"
														+ "       AND D.id_area_generadora = C.id_area_generadora\r\n"
														+ "       AND C.id_area_generadora = (SELECT id_area_generadora FROM m4t_delegaciones WHERE id_div_geografica = ?)\r\n"
														+ ")\r\n"
														+ "And Ag.id_tipo = 'GI'";
	int BorraAuthGuardiasIntXDeleg(int idFecha, String idDeleg);

	public String STMT_DELETE_GUARDIAS_EXT_AUT_X_DELEG	= "DELETE FROM gys_autorizacion_guardias AG\r\n"
														+ "WHERE AG.id_guardia IN ( \r\n"
														+ "    SELECT G.id\r\n"
														+ "    FROM gys_guardias_ext G\r\n"
														+ "     INNER JOIN gys_fechas_control P ON G.fec_paga = P.fec_pago\r\n"
														+ "     INNER JOIN gys_delegacionesporfecha DF ON P.id = DF.idfecha \r\n"
														+ "     INNER JOIN m4t_delegaciones D ON DF.iddelegacion = D.id_div_geografica\r\n"
														+ "     INNER JOIN m4t_centros_trab C ON G.id_centro_trabajo = C.id_centro_trabajo\r\n"
														+ "     INNER JOIN m4t_puestos_plaza PU ON G.id_puesto_plaza = PU.id_puesto_plaza\r\n"
														+ "     INNER JOIN m4t_clave_servicio SE ON G.id_clave_servicio = SE.id_clave_servicio\r\n"
//														+ "     INNER JOIN gys_autorizacion_guardias AG ON G.id = AG.id_guardia AND AG.id_tipo in ('GE')\r\n"
														+ "     WHERE PU.id_sociedad = '01'\r\n"
														+ "       AND PU.id_empresa = '01'\r\n"
														+ "       AND SE.id_empresa = '01'\r\n"
														+ "       AND DF.idfecha = ?\r\n"
														+ "       AND D.id_area_generadora = C.id_area_generadora\r\n"
														+ "       AND C.id_area_generadora = (SELECT id_area_generadora FROM m4t_delegaciones WHERE id_div_geografica = ?)\r\n"
														+ ")\r\n"
														+ "And Ag.id_tipo = 'GE'";
	int BorraAuthGuardiasExtXDeleg(int idFecha, String idDeleg);

	public String STMT_DELETE_SUPLENCIAS_AUT = "Delete From gys_autorizacion_suplencias\r\n"
											+ "Where fec_pago = ?";
	int BorraAuthSuplencias(Paga paga);

	public String STMT_DELETE_SUPLENCIAS_INT_AUT_X_DELEG	= "DELETE FROM gys_autorizacion_suplencias AG\r\n"
															+ "WHERE AG.id_suplencia IN ( \r\n"
															+ "    SELECT G.id\r\n"
															+ "    FROM gys_suplencias_emp G\r\n"
															+ "     INNER JOIN gys_fechas_control P ON G.fec_paga = P.fec_pago\r\n"
															+ "     INNER JOIN gys_delegacionesporfecha DF ON P.id = DF.idfecha\r\n"
															+ "     INNER JOIN m4t_delegaciones D ON DF.iddelegacion = D.id_div_geografica\r\n"
															+ "     INNER JOIN m4t_centros_trab C ON G.id_centro_trabajo = C.id_centro_trabajo\r\n"
															+ "     INNER JOIN m4t_puestos_plaza PU ON G.id_puesto_plaza = PU.id_puesto_plaza\r\n"
															+ "     INNER JOIN m4t_clave_servicio SE ON G.id_clave_servicio = SE.id_clave_servicio\r\n"
//															+ "     INNER JOIN gys_autorizacion_suplencias AG ON G.id = AG.id_suplencia AND AG.id_tipo in ('SI')\r\n"
															+ "     WHERE PU.id_sociedad = '01'\r\n"
															+ "       AND PU.id_empresa = '01'\r\n"
															+ "       AND SE.id_empresa = '01'\r\n"
															+ "       AND DF.idfecha = ?\r\n"
															+ "       AND D.id_area_generadora = C.id_area_generadora\r\n"
															+ "       AND C.id_area_generadora = (SELECT id_area_generadora FROM m4t_delegaciones WHERE id_div_geografica = ?)\r\n"
															+ ")\r\n"
															+ "And Ag.id_tipo = 'SI'";
	int borraAuthSuplenciasIntXDeleg(int idFecha, String idDeleg);

	public String STMT_DELETE_SUPLENCIAS_EXT_AUT_X_DELEG	= "DELETE FROM gys_autorizacion_suplencias AG\r\n"
															+ "WHERE AG.id_suplencia IN ( \r\n"
															+ "    SELECT G.id\r\n"
															+ "    FROM gys_suplencias_ext G\r\n"
															+ "     INNER JOIN gys_fechas_control P ON G.fec_paga = P.fec_pago\r\n"
															+ "     INNER JOIN gys_delegacionesporfecha DF ON P.id = DF.idfecha\r\n"
															+ "     INNER JOIN m4t_delegaciones D ON DF.iddelegacion = D.id_div_geografica\r\n"
															+ "     INNER JOIN m4t_centros_trab C ON G.id_centro_trabajo = C.id_centro_trabajo\r\n"
															+ "     INNER JOIN m4t_puestos_plaza PU ON G.id_puesto_plaza = PU.id_puesto_plaza\r\n"
															+ "     INNER JOIN m4t_clave_servicio SE ON G.id_clave_servicio = SE.id_clave_servicio\r\n"
//															+ "     INNER JOIN gys_autorizacion_suplencias AG ON G.id = AG.id_suplencia AND AG.id_tipo in ('SE')\r\n"
															+ "     WHERE PU.id_sociedad = '01'\r\n"
															+ "       AND PU.id_empresa = '01'\r\n"
															+ "       AND SE.id_empresa = '01'\r\n"
															+ "       AND DF.idfecha = ?\r\n"
															+ "       AND D.id_area_generadora = C.id_area_generadora\r\n"
															+ "       AND C.id_area_generadora = (SELECT id_area_generadora FROM m4t_delegaciones WHERE id_div_geografica = ?)\r\n"
															+ ")\r\n"
															+ "And Ag.id_tipo = 'SE'";
	int borraAuthSuplenciasExtXDeleg(int idFecha, String idDeleg);

	public String QUERY_VERIFY_PAGA_CERRADA = "Select COUNT(*)\r\n"
											+ "From gys_fechas_control\r\n"
											+ "Where fec_pago = ? And estatus >= 2";
	public int verifica_paga_cerrada(String fecha);

	public String QUERY_VERIFY_PAGA_CERRADA_DELEG = "Select COUNT(*)\r\n"
			                                     + "	From gys_fechas_control F, gys_DelegacionesPorFecha D\r\n"
											     + "	Where F.id = D.idfecha \r\n"
												 + "	And F.id = ?\r\n"
												 + "	And D.estatus >= 2\r\n"
												 + "	And D.iddelegacion = ?";
	public int verifica_paga_cerrada_deleg(int idFecha, String idDeleg);

	public final String UPDATE_DATE_BY_DELEG = "Update gys_delegacionesporfecha\r\n" +
												"Set estatus=?\r\n" +
												"Where idfecha=?\r\n" +
												"And iddelegacion=?";

    int changeEstatusByIdDeleg(int idFecha, String idDeleg, int estatus);

	public final String UPDATE_ESTATUS_FEC_DELEG = "Update gys_delegacionesporfecha \r\n"+
													"Set estatus = ? \r\n"+
													"Where idfecha= ?";

	int changeEstatusForAllDelegByDate(int idFecha, int estatus);

	public final String LISTA_REPRESENTACIONES_POR_FECHA = "SELECT iddelegacion FROM gys_delegacionesporfecha where idfecha = ?";

	List<String> listaDeRepresentacionesByFecha(int idFecha);
	public final String VALIDACION_REGISTROS_GUARDIAS_Y_SUPLENCIAS =
			"SELECT (GI.total_g_internas + GE.total_g_externas + SI.total_s_internas + total_s_externas) total_registros\r\n"
			+"    FROM(\r\n"
			+"        SELECT count(*) total_g_internas\r\n"
			+"            FROM gys_guardias_emp G\r\n"
			+"            INNER JOIN gys_fechas_control P ON G.fec_paga = P.fec_pago\r\n"
			+"            INNER JOIN gys_delegacionesporfecha DF ON P.id = DF.idfecha \r\n"
			+"            INNER JOIN m4t_delegaciones D ON DF.iddelegacion = D.id_div_geografica\r\n"
			+"            INNER JOIN m4t_centros_trab C ON G.id_centro_trabajo = C.id_centro_trabajo\r\n"
			+"            INNER JOIN m4t_puestos_plaza PU ON G.id_puesto_plaza = PU.id_puesto_plaza\r\n"
			+"            INNER JOIN m4t_clave_servicio SE ON G.id_clave_servicio = SE.id_clave_servicio\r\n"
			+"            WHERE PU.id_sociedad = '01'\r\n"
			+"                AND PU.id_empresa = '01'\r\n"
			+"                AND SE.id_empresa = '01'\r\n"
			+"                AND DF.idfecha = ?\r\n"
			+"                AND D.id_area_generadora = C.id_area_generadora\r\n"
			+"                AND C.id_area_generadora = (SELECT id_area_generadora FROM m4t_delegaciones WHERE id_div_geografica = ?)\r\n"
			+"    )GI, (\r\n"
			+"        SELECT count(*) total_g_externas\r\n"
			+"            FROM gys_guardias_ext G\r\n"
			+"            INNER JOIN gys_fechas_control P ON G.fec_paga = P.fec_pago\r\n"
			+"            INNER JOIN gys_delegacionesporfecha DF ON P.id = DF.idfecha \r\n"
			+"            INNER JOIN m4t_delegaciones D ON DF.iddelegacion = D.id_div_geografica\r\n"
			+"            INNER JOIN m4t_centros_trab C ON G.id_centro_trabajo = C.id_centro_trabajo\r\n"
			+"            INNER JOIN m4t_puestos_plaza PU ON G.id_puesto_plaza = PU.id_puesto_plaza\r\n"
			+"            INNER JOIN m4t_clave_servicio SE ON G.id_clave_servicio = SE.id_clave_servicio\r\n"
			+"            WHERE PU.id_sociedad = '01'\r\n"
			+"                AND PU.id_empresa = '01'\r\n"
			+"                AND SE.id_empresa = '01'\r\n"
			+"                AND DF.idfecha = ?\r\n"
			+"                AND D.id_area_generadora = C.id_area_generadora\r\n"
			+"                AND C.id_area_generadora = (SELECT id_area_generadora FROM m4t_delegaciones WHERE id_div_geografica = ?)\r\n"
			+"    )GE, (\r\n"
			+"        SELECT count(*) total_s_internas\r\n"
			+"            FROM gys_suplencias_emp G\r\n"
			+"            INNER JOIN gys_fechas_control P ON G.fec_paga = P.fec_pago\r\n"
			+"            INNER JOIN gys_delegacionesporfecha DF ON P.id = DF.idfecha \r\n"
			+"            INNER JOIN m4t_delegaciones D ON DF.iddelegacion = D.id_div_geografica\r\n"
			+"            INNER JOIN m4t_centros_trab C ON G.id_centro_trabajo = C.id_centro_trabajo\r\n"
			+"            INNER JOIN m4t_puestos_plaza PU ON G.id_puesto_plaza = PU.id_puesto_plaza\r\n"
			+"            INNER JOIN m4t_clave_servicio SE ON G.id_clave_servicio = SE.id_clave_servicio\r\n"
			+"            WHERE PU.id_sociedad = '01'\r\n"
			+"                AND PU.id_empresa = '01'\r\n"
			+"                AND SE.id_empresa = '01'\r\n"
			+"                AND DF.idfecha = ?\r\n"
			+"                AND D.id_area_generadora = C.id_area_generadora\r\n"
			+"                AND C.id_area_generadora = (SELECT id_area_generadora FROM m4t_delegaciones WHERE id_div_geografica = ?)\r\n"
			+"    )SI,(\r\n"
			+"        SELECT count(*) total_s_externas\r\n"
			+"            FROM gys_suplencias_ext G\r\n"
			+"            INNER JOIN gys_fechas_control P ON G.fec_paga = P.fec_pago\r\n"
			+"            INNER JOIN gys_delegacionesporfecha DF ON P.id = DF.idfecha \r\n"
			+"            INNER JOIN m4t_delegaciones D ON DF.iddelegacion = D.id_div_geografica\r\n"
			+"            INNER JOIN m4t_centros_trab C ON G.id_centro_trabajo = C.id_centro_trabajo\r\n"
			+"            INNER JOIN m4t_puestos_plaza PU ON G.id_puesto_plaza = PU.id_puesto_plaza\r\n"
			+"            INNER JOIN m4t_clave_servicio SE ON G.id_clave_servicio = SE.id_clave_servicio\r\n"
			+"            WHERE PU.id_sociedad = '01'\r\n"
			+"                AND PU.id_empresa = '01'\r\n"
			+"                AND SE.id_empresa = '01'\r\n"
			+"                AND DF.idfecha = ?\r\n"
			+"                AND D.id_area_generadora = C.id_area_generadora\r\n"
			+"                AND C.id_area_generadora = (SELECT id_area_generadora FROM m4t_delegaciones WHERE id_div_geografica = ?)\r\n"
			+"    )SE";
	boolean existenRegistrosGuardiasYSuplencias(int idFecha, String idDeleg);

	public final String LISTA_REPRESENTACIONES_POR_ESTATUS = "SELECT iddelegacion FROM gys_delegacionesporfecha \r\n" +
			"WHERE idfecha = ?\r\n" +
			"AND estatus = ?";

	List<String> listaDeRepresentacionesByEstatus(int idFecha, int estatus);

	public final String ID_FECHA_CONTROL = "SELECT id FROM gys_fechas_control WHERE fec_pago = ?";

	int idFechaControl(String fec_pago);
}
