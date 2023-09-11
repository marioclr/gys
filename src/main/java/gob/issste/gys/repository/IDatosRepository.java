package gob.issste.gys.repository;

import java.util.List;

import gob.issste.gys.model.*;

public interface IDatosRepository {

	public String QUERY_GET_ADSCRIPCIONES           = "Select id_centro_trabajo Clave, n_centro_trabajo Descripcion, id_tipo_ct Tipo, id_zona Zona\r\n"
													+ "From m4t_centros_trab \r\n"
													+ "Where id_tipo_ct IN (Select Distinct id_tipo_ct From m4t_gys_matriz_puestos)";
	List<DatosAdscripcion> getDatosAdscripciones();

	public String QUERY_GET_ADSCRIPCIONES_FOR_DEL   = "Select id_centro_trabajo Clave, n_centro_trabajo Descripcion, id_tipo_ct Tipo, id_zona Zona\r\n"
													+ "From m4t_centros_trab \r\n"
													+ "Where id_tipo_ct IN (Select Distinct id_tipo_ct From m4t_gys_matriz_puestos)\r\n"
													+ "	And id_delegacion = ?";
	List<DatosAdscripcion> getDatosAdscForDeleg(String idDeleg);

	public String QUERY_GET_ADSCRIPCIONES_BY_USER   = "Select id_centro_trabajo Clave, n_centro_trabajo Descripcion, id_tipo_ct Tipo, id_zona Zona\r\n"
													+ "From m4t_centros_trab\r\n"
													+ "Where id_delegacion IN (Select IdDelegacion From gys_Usuarios Where IdUsuario = ?)\r\n"
													+ "And id_tipo_ct IN (Select Distinct id_tipo_ct From m4t_gys_matriz_puestos)";
	List<DatosAdscripcion> getDatosAdscripciones(int idUsuario);

	public String QUERY_GET_ADSCRIPCIONES_BY_USER_CT = "Select id_centro_trabajo Clave, n_centro_trabajo Descripcion, id_tipo_ct Tipo, id_zona Zona\r\n"
													+ "From m4t_centros_trab\r\n"
													+ "Where id_delegacion IN (Select IdDelegacion From gys_Usuarios Where IdUsuario = ?)\r\n"
													+ "And id_tipo_ct IN (Select Distinct id_tipo_ct From m4t_gys_matriz_puestos)"
													+ "And id_centro_trabajo IN (Select IdCentroTrab From gys_Usuarios_Centros_Trab Where IdUsuario=?)";
	List<DatosAdscripcion> getDatosAdscripciones_ct(int idUsuario);

	public String QUERY_GET_PUESTOS_GUARDIA         = "Select Distinct TRIM(P.id_puesto_plaza) Clave , n_puesto Descripcion, id_tipo_tabulador tipotabulador\r\n"
													+ "From m4t_puestos_plaza P, m4t_gys_matriz_puestos M\r\n"
													+ "Where TRIM(P.id_puesto_plaza)=TRIM(M.id_puesto_plaza)\r\n"
													+ "  And id_tipo_ct IN (Select id_tipo_ct From m4t_centros_trab)";
	List<DatosPuesto> getDatosPuestosGuardia();

	public String QUERY_GET_PUESTOS_GUARDIA_X_ADSC  = "Select Distinct TRIM(P.id_puesto_plaza) Clave , n_puesto Descripcion, id_tipo_tabulador tipotabulador\r\n"
													+ "From m4t_puestos_plaza P, m4t_gys_matriz_puestos M\r\n"
													+ "Where TRIM(P.id_puesto_plaza)=TRIM(M.id_puesto_plaza)\r\n"
													+ "  And id_tipo_ct IN (Select id_tipo_ct From m4t_centros_trab Where id_centro_trabajo =?)";
	List<DatosPuesto> getDatosPuestosGuardia(String adsc);

	public String QUERY_GET_SERVICIOS_GUARDIA       = "Select Distinct S.id_clave_servicio Clave, n_clave_servicio Descripcion\r\n"
													+ "From m4t_clave_servicio S, m4t_gys_matriz_puestos M\r\n"
													+ "Where S.id_clave_servicio=M.id_clave_servicio";
	List<DatosServicio> getDatosServiciosGuardia();

	public String QUERY_GET_SERVICIOS_GUARDIA_X_ADSC_PTO = "Select Distinct S.id_clave_servicio Clave, n_clave_servicio Descripcion\r\n"
													+ "From m4t_clave_servicio S, m4t_gys_matriz_puestos M\r\n"
													+ "Where S.id_clave_servicio=M.id_clave_servicio\r\n"
													+ "  And id_tipo_ct IN (Select id_tipo_ct From m4t_centros_trab Where id_centro_trabajo = ?\r\n"
													+ "  And TRIM(id_puesto_plaza) = ?)";
	List<DatosServicio> getDatosServiciosGuardia(String adsc, String puesto);

	public String QUERY_GET_NIVELES_GUARDIA         = "Select Distinct id_nivel Nivel, id_sub_nivel Subnivel, id_nivel||id_sub_nivel Nivelsubnivel\r\n"
													+ "From m4t_gys_matriz_puestos\r\n"
													+ "Where id_tipo_ct IN (Select id_tipo_ct From m4t_centros_trab)";
	List<DatosNivel> getDatosNivelesGuardia();

	public String QUERY_GET_NIVELES_GUARDIA_X_ADSC_PTO_SERV  = "Select Distinct id_nivel Nivel, id_sub_nivel Subnivel, id_nivel||id_sub_nivel Nivelsubnivel \r\n"
													+ "From m4t_gys_matriz_puestos\r\n"
													+ "Where id_tipo_ct IN (Select id_tipo_ct From m4t_centros_trab Where id_centro_trabajo = ?\r\n"
													+ "  And TRIM(id_puesto_plaza) = ?\r\n"
													+ "  And id_clave_servicio = ?)";
	List<DatosNivel> getDatosNivelesGuardia(String adsc, String puesto, String servicio);

	public String QUERY_GET_JORNADAS_GUARDIA        = "Select Distinct M.id_tipo_jornada Clave, n_tipo_jornada Descripcion \r\n"
													+ "From m4t_gys_matriz_puestos M, m4t_tip_jornada_st J\r\n"
													+ "Where M.id_tipo_jornada = J.id_tipo_jornada";
	List<DatosJornada> getDatosJornadasGuardia();

	public String QUERY_GET_JORNADAS_GUARDIA_X_ADSC_PTO_SERV_NIV = 
													  "Select Distinct M.id_tipo_jornada Clave, n_tipo_jornada Descripcion \r\n"
													+ "From m4t_gys_matriz_puestos M, m4t_tip_jornada_st J \r\n"
													+ "Where M.id_tipo_jornada = J.id_tipo_jornada"
													+ "  And id_tipo_ct IN (Select id_tipo_ct From m4t_centros_trab Where id_centro_trabajo = ? )\r\n"
													+ "  And TRIM(id_puesto_plaza) = ? \r\n"
													+ "  And id_clave_servicio = ? \r\n"
													+ "  And id_nivel = ? \r\n"
													+ "  And id_sub_nivel = ?";
	List<DatosJornada> getDatosJornadasGuardia(String adsc, String puesto, String servicio, String niveles);

	public String QUERY_VALIDA_PUESTO_AUTORIZADO    = "Select NVL(COUNT(*), 0) "
													+ "From m4t_gys_matriz_puestos \r\n"
													+ "Where id_tipo_ct = ?  And id_clave_servicio = ? \r\n"
													+ "  And TRIM(id_puesto_plaza) = ? And id_nivel = ? \r\n"
													+ "  And id_sub_nivel = ? And id_tipo_jornada = ? \r\n"
													+ "  And id_tipo_per = ?";
	long ValidaPuestoAutorizado(String tipo_ct, String clave_servicio, String puesto, String nivel, String sub_nivel, String tipo_jornada, String tipo_guardia);

	public String QUERY_CONSULTA_GUARDIAS_INTERNAS  = "Select G.id_empleado As Clave_empleado, G.id_centro_trabajo, G.id_clave_servicio, G.id_puesto_plaza, "
													+ "G.id_nivel, G.id_sub_nivel, G.id_tipo_jornada, G.horas, G.fec_inicio, G.fec_fin, "
													+ "G.importe, PU.id_tipo_tabulador, G.fec_paga, C.id_zona, NVL(G.riesgos,'A') riesgos, P.estatus, G.id_ordinal "
													+ "From gys_guardias_emp G, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU "
													+ "Where G.fec_paga = P.fec_pago And "
													+ "G.id_centro_trabajo = C.id_centro_trabajo And "
													+ "G.id_puesto_plaza = PU.id_puesto_plaza And "
													+ "PU.id_sociedad = '01' And PU.id_empresa = '01' "
													+ "And id_empleado = ? "
													+ "Order by G.fec_paga desc, G.fec_inicio";
	public List<DatosGuardia> ConsultaGuardiasInternas(String claveEmpleado);

	public String QUERY_GET_DELEGACIONES			= "Select * From m4t_delegaciones";
	List<Delegacion> getDatosDelegaciones();

	public String QUERY_GET_HORARIOS				= "Select * From m4t_horarios";
	List<Horario> getHorarios();

	public String QUERY_GET_TIP_INCIDENCIA			= "Select * From m4t_tip_ausentismo Order By Length(id_tipo_ausentismo), id_tipo_ausentismo";
	List<Incidencia> getIncidencia();

	public String QUERY_GET_PAGAS					= "Select * From gys_fechas_control Where estatus <> 0";
	List<Paga> getPagas();

	public String QUERY_VALIDA_PERSONAL_EXTERNO		= "Select nombre From gys_bolsa_trabajo Where id_legal = ?";
	String ValidaPersonalExterno(String rfc);

	public String QUERY_GET_DELEGACION_BY_ID		= "Select * From m4t_delegaciones Where id_div_geografica = ?";
	Delegacion getDatosDelegacionById(String idDelegacion);

}