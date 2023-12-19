package gob.issste.gys.repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.DatosGuardia;
import gob.issste.gys.repository.GuardiaRepository;

@Repository
public class JdbcGuardiaRepository implements GuardiaRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public int save(DatosGuardia guardia) throws SQLException {

        //String fecha = guardia.getFec_paga().toLocalDate().toString();
        //LocalDate ld = LocalDate.parse( (CharSequence) guardia.getFec_paga() ) ;

        logger.info(QUERY_ADD_NEW_GUARDIA);
        //logger.info(fecha);
        //logger.info(ld.toString());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_NEW_GUARDIA, Statement.RETURN_GENERATED_KEYS);

			//String folio, String motivo, String movim, String coment, String usuario, String riesgos
            preparedStatement.setString(1, "01");
            preparedStatement.setString(2, guardia.getClave_empleado());
            preparedStatement.setString(3, guardia.getFec_paga());
            preparedStatement.setString(4, guardia.getFec_inicio());
            preparedStatement.setString(5, guardia.getClave_empleado());
            preparedStatement.setString(6, guardia.getFec_paga());
            preparedStatement.setString(7, guardia.getFec_inicio());
            preparedStatement.setString(8, guardia.getFec_fin());
            preparedStatement.setString(9, guardia.getId_empresa().compareTo("")== 0 ? "01" :guardia.getId_empresa() );
            preparedStatement.setString(10, guardia.getId_puesto_plaza());
            preparedStatement.setString(11, guardia.getId_clave_servicio());
            preparedStatement.setString(12, guardia.getId_centro_trabajo());
            preparedStatement.setString(13, guardia.getId_tipo_jornada());
            preparedStatement.setString(14, guardia.getId_nivel());
            preparedStatement.setString(15, guardia.getId_sub_nivel());
            preparedStatement.setDouble(16, guardia.getHoras().doubleValue());
            preparedStatement.setDouble(17, guardia.getImporte().doubleValue());
            preparedStatement.setString(18, guardia.getFolio());
            preparedStatement.setString(19, guardia.getMotivo());
            preparedStatement.setString(20, guardia.getId_clave_movimiento());
            preparedStatement.setString(21, guardia.getComent());
            preparedStatement.setString(22, guardia.getId_usuario());
            preparedStatement.setInt(23, guardia.getRiesgos().intValue());
            preparedStatement.setInt(24, guardia.getHora_inicio());
            preparedStatement.setInt(25, guardia.getHora_fin());
            return preparedStatement;
        };

        int updatesCount = jdbcTemplate.update(statementCreator, keyHolder);
        if (updatesCount == 1) {
            Number generatedKey = keyHolder.getKey();
            if (generatedKey == null) {
                throw new SQLException("Getting user id error.");
            }
            return generatedKey.intValue();
        }
        throw new SQLException("Expected one row insert.");
	}

	@Override
	public int updateGuardia(DatosGuardia guardia) {
		logger.info(QUERY_UPD_GUARDIA);
		return jdbcTemplate.update(QUERY_UPD_GUARDIA, 
				new Object[] { guardia.getImporte(), guardia.getFolio(), guardia.getMotivo(), guardia.getId_clave_movimiento(), guardia.getHoras(),
						guardia.getComent(), guardia.getId_usuario(), guardia.getHora_inicio(), guardia.getHora_fin(), guardia.getId() });
	}

	@Override
	public List<DatosGuardia> ConsultaGuardiasInternas(String id_empleado) {

		logger.info(QUERY_GET_GUARDIA_INTERNA);
		return jdbcTemplate.query(QUERY_GET_GUARDIA_INTERNA, BeanPropertyRowMapper.newInstance(DatosGuardia.class), id_empleado );

	}

	@Override
	public List<DatosGuardia> ConsultaGuardiasExternas(String rfcEmpleado) {

		logger.info(QUERY_GET_GUARDIA_EXTERNA);
		return jdbcTemplate.query(QUERY_GET_GUARDIA_EXTERNA, BeanPropertyRowMapper.newInstance(DatosGuardia.class), rfcEmpleado);

	}

	@Override
	public double ObtenerSaldoUtilizado(String idDelegacion, int anio_ejercicio, int mes_ejercicio) {
		logger.info(QUERY_GET_SALDO_GUARDIA_INT);
		return jdbcTemplate.queryForObject(QUERY_GET_SALDO_GUARDIA_INT, Double.class, idDelegacion, anio_ejercicio, mes_ejercicio );
	}

	@Override
	public double ObtenerSaldoUtilizado_ct(int id, String id_centro_trabajo, int anio_ejercicio, int mes_ejercicio) {
		logger.info(QUERY_GET_SALDO_GUARDIA_INT_CT);
		return jdbcTemplate.queryForObject(QUERY_GET_SALDO_GUARDIA_INT_CT, Double.class, id, id_centro_trabajo, anio_ejercicio, mes_ejercicio );
	}

	@Override
	public double ObtenerSaldoUtilizadoExt(String idDelegacion, int anio_ejercicio, int mes_ejercicio) {
		logger.info(QUERY_GET_SALDO_GUARDIA_EXT);
		return jdbcTemplate.queryForObject(QUERY_GET_SALDO_GUARDIA_EXT, Double.class, idDelegacion, anio_ejercicio, mes_ejercicio );
	}

	@Override
	public double ObtenerSaldoUtilizadoExt_ct(int id, String id_centro_trabajo, int anio_ejercicio, int mes_ejercicio) {
		logger.info(QUERY_GET_SALDO_GUARDIA_EXT_CT);
		return jdbcTemplate.queryForObject(QUERY_GET_SALDO_GUARDIA_EXT_CT, Double.class, id, id_centro_trabajo, anio_ejercicio, mes_ejercicio );
	}

	@Override
	public int updateImporteGuardia(DatosGuardia guardia) {
		//logger.info("Guardia a actualizar: " + guardia.getImporte() + "\t" + guardia.getClave_empleado() + "\t" + guardia.getFec_paga() + "\t" + guardia.getFec_fin() + "\t" + guardia.getId_ordinal());
		//logger.info(QUERY_UPD_GUARDIA_INTERNA);
		return jdbcTemplate.update(QUERY_UPD_GUARDIA_INTERNA, 
				new Object[] { guardia.getImporte(), guardia.getRiesgos(), guardia.getClave_empleado(), guardia.getFec_paga(), guardia.getFec_inicio(),
						guardia.getId_ordinal() });
	}

	@Override
	public int updateImporteGuardiaExt(DatosGuardia guardia) {
		return jdbcTemplate.update(QUERY_UPD_GUARDIA_EXTERNA, 
				new Object[] { guardia.getImporte(), guardia.getId() });
	}

	@Override
	public List<DatosGuardia> ConsultaGuardiasInternasXFecha(String fechaPago) {
		logger.info(QUERY_GET_GUARDIAS_X_FECHA);
		return jdbcTemplate.query(QUERY_GET_GUARDIAS_X_FECHA, BeanPropertyRowMapper.newInstance(DatosGuardia.class), fechaPago );
	}

	@Override
	public DatosGuardia findById(Integer idGuardia) {
		logger.info(QUERY_GET_GUARDIAS_BY_ID);
		return jdbcTemplate.queryForObject(QUERY_GET_GUARDIAS_BY_ID, BeanPropertyRowMapper.newInstance(DatosGuardia.class), idGuardia );
	}
	
	@Override
	public List<DatosGuardia> ConsultaGuardiasExternasXFecha(String fechaPago) {
		logger.info(QUERY_GET_GUARDIAS_EXT_X_FECHA);
		return jdbcTemplate.query(QUERY_GET_GUARDIAS_EXT_X_FECHA, BeanPropertyRowMapper.newInstance(DatosGuardia.class), fechaPago );
	}

	@Override
	public DatosGuardia findByIdExterno(Integer idGuardia) {
		logger.info(QUERY_GET_GUARDIAS_EXT_BY_ID);
		return jdbcTemplate.queryForObject(QUERY_GET_GUARDIAS_EXT_BY_ID, BeanPropertyRowMapper.newInstance(DatosGuardia.class), idGuardia );
	}

	@Override
	public int saveExt(DatosGuardia guardia) throws SQLException {
        logger.info(QUERY_ADD_NEW_GUARDIA_EXT);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_NEW_GUARDIA_EXT, Statement.RETURN_GENERATED_KEYS);

			//String folio, String motivo, String movim, String coment, String usuario, String riesgos
            preparedStatement.setString(1, guardia.getClave_empleado());
            preparedStatement.setString(2, guardia.getFec_paga());
            preparedStatement.setString(3, guardia.getFec_inicio());
            preparedStatement.setString(4, guardia.getClave_empleado());
            preparedStatement.setString(5, guardia.getFec_paga());
            preparedStatement.setString(6, guardia.getFec_inicio());
            preparedStatement.setString(7, guardia.getFec_fin());
            preparedStatement.setString(8, guardia.getId_puesto_plaza());
            preparedStatement.setString(9, guardia.getId_clave_servicio());
            preparedStatement.setString(10, guardia.getId_centro_trabajo());
            preparedStatement.setString(11, guardia.getId_tipo_jornada());
            preparedStatement.setString(12, guardia.getId_nivel());
            preparedStatement.setString(13, guardia.getId_sub_nivel());
            preparedStatement.setDouble(14, guardia.getHoras().doubleValue());
            preparedStatement.setDouble(15, guardia.getImporte().doubleValue());
            preparedStatement.setString(16, guardia.getFolio());
            preparedStatement.setString(17, guardia.getMotivo());
            preparedStatement.setString(18, guardia.getId_clave_movimiento());
            preparedStatement.setString(19, guardia.getComent());
            preparedStatement.setString(20, guardia.getId_usuario());
            preparedStatement.setInt(21, guardia.getRiesgos().intValue());
            preparedStatement.setInt(22, guardia.getHora_inicio());
            preparedStatement.setInt(23, guardia.getHora_fin());
            return preparedStatement;
        };

        int updatesCount = jdbcTemplate.update(statementCreator, keyHolder);
        if (updatesCount == 1) {
            Number generatedKey = keyHolder.getKey();
            if (generatedKey == null) {
                throw new SQLException("Getting user id error.");
            }
            return generatedKey.intValue();
        }
        throw new SQLException("Expected one row insert.");
	}

	@Override
	public int updateGuardiaExt(DatosGuardia guardia) {
		return jdbcTemplate.update(QUERY_UPD_GUARDIA_EXT, 
				new Object[] { guardia.getImporte(), guardia.getFolio(), guardia.getMotivo(), guardia.getId_clave_movimiento(), guardia.getHoras(),
						guardia.getComent(), guardia.getId_usuario(), guardia.getId() });
	}

	@Override
	public int deleteGuardia(Integer idGuardia) {
		logger.info(QUERY_DELETE_GUARDIA);
		return jdbcTemplate.update(QUERY_DELETE_GUARDIA, idGuardia);
	}

	@Override
	public int deleteGuardiaExt(Integer idGuardia) {
		logger.info(QUERY_DELETE_GUARDIA_EXT);
		return jdbcTemplate.update(QUERY_DELETE_GUARDIA_EXT, idGuardia);
	}

	@Override
	public int updateGuardiaIntVars(DatosGuardia guardia) {
//		return jdbcTemplate.update(QUERY_UPD_GUARDIAS_INT_VARS, 
//				new Object[] { guardia.getImporte(), guardia.getRiesgos(),
//						guardia.getEmpleado_suplir().getId_puesto_plaza(), guardia.getEmpleado_suplir().getId_clave_servicio(),
//						guardia.getEmpleado_suplir().getId_nivel(), guardia.getEmpleado_suplir().getId_sub_nivel(), 
//						guardia.getEmpleado_suplir().getId_tipo_jornada(), guardia.getEmpleado_suplir().getId_centro_trabajo(),
//						guardia.getClave_empleado(), guardia.getFec_paga(), guardia.getFec_inicio(), guardia.getId_ordinal() });
		return 0;
	}

	@Override
	public int updateGuardiaExtVars(DatosGuardia guardia) {
//		return jdbcTemplate.update(QUERY_UPD_GUARDIAS_EXT_VARS, 
//				new Object[] { guardia.getImporte(), guardia.getRiesgos(),
//						guardia.getDatos_empleado1().getId_puesto_plaza(), guardia.getDatos_empleado1().getId_clave_servicio(),
//						guardia.getDatos_empleado1().getId_nivel(), guardia.getDatos_empleado1().getId_sub_nivel(), 
//						guardia.getDatos_empleado1().getId_tipo_jornada(), guardia.getDatos_empleado1().getId_centro_trabajo(),
//						guardia.getClave_empleado(), guardia.getFec_paga(), guardia.getFec_inicio(), guardia.getId_ordinal() });
		return 0;
	}

	@Override
	public List<DatosGuardia> ConsultaDynamicGuardias(String fechaPago, String tipo, String clave_empleado, Double importe_min, Double importe_max,
			String idDelegacion, String idCentroTrab, String claveServicio, String puesto, Integer estatus ) {

		String QUERY_CONDITION  = "";
		String EMPLOYEE_FIELD   = "";
		String QUERY_TABLE_BASE = "";
		List<Object> objects = new ArrayList<Object>();

		if (tipo.equals(String.valueOf("GI"))) {
			QUERY_TABLE_BASE = "gys_guardias_emp";
			EMPLOYEE_FIELD   = "id_empleado";
		} else {
			QUERY_TABLE_BASE = "gys_guardias_ext";
			EMPLOYEE_FIELD   = "rfc";
		}

		if (fechaPago != null) { 
			QUERY_CONDITION += "And G.fec_paga = ?\r\n";
			objects.add(fechaPago);
		}

		if (clave_empleado != null) {
			if (tipo.equals(String.valueOf("GI"))) {
				QUERY_CONDITION += "And id_empleado = ?\r\n";
				objects.add(clave_empleado);
			} else {
				QUERY_CONDITION += "And rfc = ?\r\n";
				objects.add(clave_empleado);
			}
		}

		if (importe_min != null) {
			QUERY_CONDITION += "And G.importe >= ?\r\n";
			objects.add(importe_min.toString());
		}

		if (importe_max != null) {
			QUERY_CONDITION += "And G.importe <= ?\r\n";
			objects.add(importe_max.toString());
		}

		if (idDelegacion != null) { 
			QUERY_CONDITION += "And C.id_delegacion = ?\r\n";
			objects.add(idDelegacion);
		}

		if (idCentroTrab != null) { 
			QUERY_CONDITION += "And C.id_centro_trabajo = ?\r\n";
			objects.add(idCentroTrab);
		}

		if (claveServicio != null) { 
			QUERY_CONDITION += "And G.id_clave_servicio = ?\r\n";
			objects.add(claveServicio);
		}

		if (puesto != null) { 
			QUERY_CONDITION += "And PU.id_puesto_plaza = ?\r\n";
			objects.add(puesto);
		}

		if (estatus != null) { 
			QUERY_CONDITION += "  And G.estatus = ?\r\n";
			objects.add(estatus);
		}

		String DYNAMIC_QUERY = "Select G.id, " + EMPLOYEE_FIELD + " clave_empleado, G.id_centro_trabajo, id_clave_servicio, G.id_puesto_plaza, '" + tipo + "' tipo_guardia,\r\n"
							 + "id_nivel, id_sub_nivel, id_tipo_jornada, horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment, G.estatus,\r\n"
							 + "G.importe, PU.id_tipo_tabulador, G.fec_paga, C.id_zona, G.id_ordinal, NVL(riesgos,0) riesgos, G.id_usuario \r\n"
							 + "From " + QUERY_TABLE_BASE + " G, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU \r\n"
							 + "Where G.fec_paga = P.fec_pago And \r\n"
							 + "G.id_centro_trabajo = C.id_centro_trabajo And \r\n"
							 + "G.id_puesto_plaza = PU.id_puesto_plaza And \r\n"
							 + "PU.id_sociedad = '01' And PU.id_empresa = '01'\r\n"
							 + QUERY_CONDITION
							 + "Order by G.fec_paga desc, G.fec_inicio";

		logger.info(DYNAMIC_QUERY);
		List<DatosGuardia> guardias = jdbcTemplate.query(DYNAMIC_QUERY, BeanPropertyRowMapper.newInstance(DatosGuardia.class), objects.toArray() );

		return guardias;

	}

	@Override
	public int existe_guardia(DatosGuardia guardia) {
		logger.info(QUERY_EXISTS_GUARDIA_INT);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_GUARDIA_INT, Integer.class,
				new Object[] { guardia.getClave_empleado(), guardia.getFec_inicio(), 
							   guardia.getHora_inicio(), guardia.getHora_fin(),
							   guardia.getHora_inicio(), guardia.getHora_fin(),
							   guardia.getHora_inicio(), guardia.getHora_fin() } );
	}

	@Override
	public int existe_guardia_upd(DatosGuardia guardia) {
		logger.info(QUERY_EXISTS_GUARDIA_INT_UPD);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_GUARDIA_INT_UPD, Integer.class,
				new Object[] { guardia.getClave_empleado(), guardia.getFec_inicio(),
						guardia.getId(),
						guardia.getHora_inicio(), guardia.getHora_fin(),
						guardia.getHora_inicio(), guardia.getHora_fin(),
						guardia.getHora_inicio(), guardia.getHora_fin() } );
	}

	@Override
	public int existe_guardia_ext(DatosGuardia guardia) {
		logger.info(QUERY_EXISTS_GUARDIA_EXT);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_GUARDIA_EXT, Integer.class,
				new Object[] { guardia.getClave_empleado(), guardia.getFec_inicio(),
							   guardia.getHora_inicio(), guardia.getHora_fin(),
							   guardia.getHora_inicio(), guardia.getHora_fin(),
							   guardia.getHora_inicio(), guardia.getHora_fin() } );
	}

	@Override
	public int existe_guardia_ext_upd(DatosGuardia guardia) {
		logger.info(QUERY_EXISTS_GUARDIA_EXT_UPD);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_GUARDIA_EXT_UPD, Integer.class,
				new Object[] { guardia.getClave_empleado(), guardia.getFec_inicio(),
						guardia.getId(),
						guardia.getHora_inicio(), guardia.getHora_fin(),
						guardia.getHora_inicio(), guardia.getHora_fin(),
						guardia.getHora_inicio(), guardia.getHora_fin() } );
	}

	@Override
	public int updateStatusGuardia(int status, int id) {
		logger.info(STMT_UPDATE_STATUS);
		return jdbcTemplate.update(STMT_UPDATE_STATUS,
	            new Object[] { status, id });
	}

	@Override
	public int updateStatusGuardiaExt(int status, int id) {
		logger.info(STMT_UPDATE_STATUS_EXT);
		return jdbcTemplate.update(STMT_UPDATE_STATUS_EXT,
	            new Object[] { status, id });
	}

	@Override
	public int updateAuthStatusGuardia1(int status, int id, String tipo, String comentarios, int idUsuario) {
		logger.info(STMT_UPDATE_AUTH_STATUS_1);
		return jdbcTemplate.update(STMT_UPDATE_AUTH_STATUS_1,
	            new Object[] { status, comentarios, idUsuario, id, tipo });
	}

	@Override
	public int updateAuthStatusGuardia2(int status, int id, String tipo, String comentarios, int idUsuario) {
		logger.info(STMT_UPDATE_AUTH_STATUS_2);
		return jdbcTemplate.update(STMT_UPDATE_AUTH_STATUS_2,
	            new Object[] { status, comentarios, idUsuario, id, tipo });
	}

	@Override
	public List<DatosGuardia> ConsultaDynamicAuthGuardias(String fechaPago, String tipo, String idDelegacion,
			String idCentroTrab, Integer estatus) {
		String QUERY_CONDITION  = "";
		String EMPLOYEE_FIELD   = "";
		String QUERY_TABLE_BASE = "";
		List<Object> objects = new ArrayList<Object>();

		switch (tipo) {

			case "GI":

				QUERY_TABLE_BASE = "gys_autorizacion_guardias A, gys_guardias_emp";
				EMPLOYEE_FIELD   = "id_empleado";
				break;

			case "GE":

				QUERY_TABLE_BASE = "gys_autorizacion_guardias A, gys_guardias_ext";
				EMPLOYEE_FIELD   = "rfc";
				break;

		}

		if (fechaPago != null) { 
			QUERY_CONDITION += "And G.fec_paga = ?\r\n";
			objects.add(fechaPago);
		}

		if (idDelegacion != null) { 
			QUERY_CONDITION += "And C.id_delegacion = ?\r\n";
			objects.add(idDelegacion);
		}

		if (idCentroTrab != null) { 
			QUERY_CONDITION += "And C.id_centro_trabajo = ?\r\n";
			objects.add(idCentroTrab);
		}

		if (estatus != null) {

			switch (estatus) {

				case 0:

					QUERY_CONDITION += "  And A.estatus1 = ?\r\n";
					objects.add(estatus);
					break;

				case 1,2:

					QUERY_CONDITION += "  And A.estatus1 = ?\r\n";
					objects.add(estatus);
					break;

				case 3,4:

					QUERY_CONDITION += "  And A.estatus2 = ?\r\n";
					objects.add(estatus);
					break;

			}

		}

		String DYNAMIC_QUERY = "Select G.id, " + EMPLOYEE_FIELD + " clave_empleado, G.id_centro_trabajo, id_clave_servicio, G.id_puesto_plaza, '" + tipo + "' tipo_guardia,\r\n"
							 + "  id_nivel, id_sub_nivel, id_tipo_jornada, horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment, "
							 + "  Case When A.estatus2 = 0 Then A.estatus1 Else A.estatus2 End estatus,\r\n"
							 + "  G.importe, PU.id_tipo_tabulador, G.fec_paga, C.id_zona, G.id_ordinal, NVL(riesgos,0) riesgos, G.id_usuario \r\n"
							 + "From " + QUERY_TABLE_BASE + " G, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU \r\n"
							 + "Where A.id_guardia = G.id And\r\n"
							 + "G.fec_paga = P.fec_pago And\r\n"
							 + "G.id_centro_trabajo = C.id_centro_trabajo And\r\n"
							 + "G.id_puesto_plaza = PU.id_puesto_plaza And\r\n"
							 + "PU.id_sociedad = '01' And PU.id_empresa = '01'\r\n"
							 + QUERY_CONDITION
							 + "Order by G.fec_paga desc, G.fec_inicio";

		logger.info(DYNAMIC_QUERY);
		List<DatosGuardia> guardias = jdbcTemplate.query(DYNAMIC_QUERY, BeanPropertyRowMapper.newInstance(DatosGuardia.class), objects.toArray() );

		return guardias;
	}


}