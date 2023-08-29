package gob.issste.gys.repository;

import java.util.List;

import gob.issste.gys.model.DatosEmpleado;
import gob.issste.gys.model.Empleado;

public interface IEmpleadoRepository {

	public String QUERY_GET_EMPLEADOS 				= "SELECT E.id_empleado As Clave_empleado, E.nombre, E.apellido_1, E.apellido_2, \r\n"
													+ " J.id_tipo_jornada, C.id_tipo_ct, E.id_legal, DI.id_centro_trabajo, n_centro_trabajo, DI.id_clave_servicio, S.n_clave_servicio, \r\n"
													+ " DI.id_puesto_plaza, P.n_puesto_plaza, DI.id_turno, n_turno, DI.id_nivel, DI.id_sub_nivel, DI.id_tipo_tabulador, DI.id_zona, \r\n"
													+ " DI.F_INICIO_PLAZA, C.id_delegacion\r\n"
													+ "FROM M4_DATOS_INDIVIDUO DI, m4t_puestos_plaza P, m4t_clave_servicio S, m4t_hist_jornada_plaza J, m4t_centros_trab C, m4t_empleados E, m4t_turnos T \r\n"
													+ "WHERE DI.id_puesto_plaza = P.id_puesto_plaza And DI.id_sociedad = P.id_sociedad \r\n"
													+ " AND DI.id_empresa = P.id_empresa And DI.id_sociedad = E.id_sociedad And DI.id_empleado = E.id_empleado \r\n"
													+ " AND DI.id_empresa = S.id_empresa And DI.id_sociedad = S.id_sociedad And DI.id_clave_servicio = S.id_clave_servicio \r\n"
													+ " AND DI.id_plaza_empleado = J.id_plaza_empleado AND DI.id_centro_trabajo=C.id_centro_trabajo And DI.id_turno = T.id_turno \r\n"
													+ "	AND (C.fec_fin >= ? OR C.fec_fin IS NULL) AND J.fec_inicio <= ? AND (J.fec_fin >= ? OR J.fec_fin IS NULL) \r\n"
													+ "	AND DI.F_INICIO_PLAZA <= ? AND (DI.F_FIN_PLAZA >= ? OR DI.F_FIN_PLAZA IS NULL) \r\n"
													+ "	AND DI.F_INICIO_EMPRESA <= ? AND (DI.F_FIN_EMPRESA >= ? OR DI.F_FIN_EMPRESA IS NULL) \r\n"
													+ "	AND DI.F_INICIO_CT <= ? AND (DI.F_FIN_CT >= ? OR DI.F_FIN_CT IS NULL) \r\n"
													+ "	AND DI.F_INICIO_CS <= ? AND (DI.F_FIN_CS >= ? OR DI.F_FIN_CS IS NULL) \r\n"
													+ "	AND DI.F_INICIO_PTO <= ? AND (DI.F_FIN_PTO >= ? OR DI.F_FIN_PTO IS NULL) \r\n"
													+ "	AND DI.F_INICIO_JOR <= ? AND (DI.F_FIN_JOR >= ? OR DI.F_FIN_JOR IS NULL) \r\n"
													+ "	AND DI.F_INICIO_TURNO <= ? AND (DI.F_FIN_TURNO >= ? OR DI.F_FIN_TURNO IS NULL) \r\n"
													+ "	AND DI.F_INICIO_SITPZA <= ? AND (DI.F_FIN_SITPZA >= ? OR DI.F_FIN_SITPZA IS NULL) \r\n"
													+ "	AND DI.F_INICIO_SITEMP <= ? AND (DI.F_FIN_SITEMP >= ? OR DI.F_FIN_SITEMP IS NULL) \r\n"
													+ "	AND di.id_tipo_tabulador<>'H' AND DI.id_empleado=? Order By DI.F_INICIO_PLAZA";

	public DatosEmpleado getDatosEmpleado(String fecha, String claveEmpleado);

	public String QUERY_GET_RIESGOS_EMPLEADO    	= "Select NVL(valor,0) valor\r\n"
													+ "From m4t_val_fase_alta\r\n"
													+ "Where id_concepto = '1502'\r\n"
													+ "  And id_empleado = ?\r\n"
													+ "  And fec_inicio <= ?\r\n"
													+ "  And (fec_fin >= ? Or fec_fin IS NULL)";
	int ConsultaRiesgosEmp(String id_empleado, String fec_pago);

	//int save(Empleado empleado);
	//int update(Empleado empleado);
	Empleado findById(String id);
	//int deleteById(int id);
	List<Empleado> findAll();
	List<Empleado> findByNombre(String nombre);
	//int deleteAll();

}
