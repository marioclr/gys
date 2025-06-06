package gob.issste.gys.repository.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import gob.issste.gys.service.ParamsValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	ParamsValidatorService paramsValidatorService = new ParamsValidatorService();
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String user;
	@Value("${spring.datasource.password}")
	private String password;

	@Autowired
	JdbcTemplate jdbcTemplate;

    public JdbcGuardiaRepository() throws SQLException {
    }

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
			preparedStatement.setString(9, guardia.getId_empresa().compareTo("") == 0 ? "01" : guardia.getId_empresa());
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
				new Object[]{guardia.getImporte(), guardia.getFolio(), guardia.getMotivo(), guardia.getId_clave_movimiento(), guardia.getHoras(),
						guardia.getComent(), guardia.getId_usuario(), guardia.getHora_inicio(), guardia.getHora_fin(), guardia.getId()});
	}

	@Override
	public List<DatosGuardia> ConsultaGuardiasInternas(String id_empleado) {

		logger.info(QUERY_GET_GUARDIA_INTERNA);
		return jdbcTemplate.query(QUERY_GET_GUARDIA_INTERNA, BeanPropertyRowMapper.newInstance(DatosGuardia.class), id_empleado);

	}

	@Override
	public List<DatosGuardia> ConsultaGuardiasExternas(String rfcEmpleado) {

		logger.info(QUERY_GET_GUARDIA_EXTERNA);
		return jdbcTemplate.query(QUERY_GET_GUARDIA_EXTERNA, BeanPropertyRowMapper.newInstance(DatosGuardia.class), rfcEmpleado);

	}

	@Override
	public double ObtenerSaldoUtilizado(String idDelegacion, int anio_ejercicio, int mes_ejercicio) {
		logger.info(QUERY_GET_SALDO_GUARDIA_INT);
		return jdbcTemplate.queryForObject(QUERY_GET_SALDO_GUARDIA_INT, Double.class, idDelegacion, anio_ejercicio, mes_ejercicio);
	}

	@Override
	public double ObtenerSaldoUtilizado_ct(int id, String id_centro_trabajo, int anio_ejercicio, int mes_ejercicio, int quincena) {
		logger.info(QUERY_GET_SALDO_GUARDIA_INT_CT);
		return jdbcTemplate.queryForObject(QUERY_GET_SALDO_GUARDIA_INT_CT, Double.class, id, id_centro_trabajo, anio_ejercicio, mes_ejercicio, quincena);
	}

	@Override
	public double ObtenerSaldoUtilizadoExt(String idDelegacion, int anio_ejercicio, int mes_ejercicio) {
		logger.info(QUERY_GET_SALDO_GUARDIA_EXT);
		return jdbcTemplate.queryForObject(QUERY_GET_SALDO_GUARDIA_EXT, Double.class, idDelegacion, anio_ejercicio, mes_ejercicio);
	}

	@Override
	public double ObtenerSaldoUtilizadoExt_ct(int id, String id_centro_trabajo, int anio_ejercicio, int mes_ejercicio, int quincena) {
		logger.info(QUERY_GET_SALDO_GUARDIA_EXT_CT);
		return jdbcTemplate.queryForObject(QUERY_GET_SALDO_GUARDIA_EXT_CT, Double.class, id, id_centro_trabajo, anio_ejercicio, mes_ejercicio, quincena);
	}

	@Override
	public int updateImporteGuardia(DatosGuardia guardia) {
		//logger.info("Guardia a actualizar: " + guardia.getImporte() + "\t" + guardia.getClave_empleado() + "\t" + guardia.getFec_paga() + "\t" + guardia.getFec_fin() + "\t" + guardia.getId_ordinal());
		//logger.info(QUERY_UPD_GUARDIA_INTERNA);
		return jdbcTemplate.update(QUERY_UPD_GUARDIA_INTERNA,
				new Object[]{guardia.getImporte(), guardia.getRiesgos(), guardia.getClave_empleado(), guardia.getFec_paga(), guardia.getFec_inicio(),
						guardia.getId_ordinal()});
	}

	@Override
	public int updateImporteGuardiaExt(DatosGuardia guardia) {
		return jdbcTemplate.update(QUERY_UPD_GUARDIA_EXTERNA,
				new Object[]{guardia.getImporte(), guardia.getId()});
	}

	@Override
	public List<DatosGuardia> ConsultaGuardiasInternasXFecha(String fechaPago) {
		logger.info(QUERY_GET_GUARDIAS_X_FECHA);
		return jdbcTemplate.query(QUERY_GET_GUARDIAS_X_FECHA, BeanPropertyRowMapper.newInstance(DatosGuardia.class), fechaPago);
	}

	@Override
	public DatosGuardia findById(Integer idGuardia) {
		logger.info(QUERY_GET_GUARDIAS_BY_ID);
		return jdbcTemplate.queryForObject(QUERY_GET_GUARDIAS_BY_ID, BeanPropertyRowMapper.newInstance(DatosGuardia.class), idGuardia);
	}

	@Override
	public List<DatosGuardia> ConsultaGuardiasExternasXFecha(String fechaPago) {
		logger.info(QUERY_GET_GUARDIAS_EXT_X_FECHA);
		return jdbcTemplate.query(QUERY_GET_GUARDIAS_EXT_X_FECHA, BeanPropertyRowMapper.newInstance(DatosGuardia.class), fechaPago);
	}

	@Override
	public DatosGuardia findByIdExterno(Integer idGuardia) {
		logger.info(QUERY_GET_GUARDIAS_EXT_BY_ID);
		return jdbcTemplate.queryForObject(QUERY_GET_GUARDIAS_EXT_BY_ID, BeanPropertyRowMapper.newInstance(DatosGuardia.class), idGuardia);
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
				new Object[]{guardia.getImporte(), guardia.getFolio(), guardia.getMotivo(), guardia.getId_clave_movimiento(), guardia.getHoras(),
						guardia.getComent(), guardia.getId_usuario(), guardia.getHora_inicio(), guardia.getHora_fin(), guardia.getId()});
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
	public int existe_guardia(DatosGuardia guardia) {
		logger.info(QUERY_EXISTS_GUARDIA_INT);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_GUARDIA_INT, Integer.class,
				new Object[]{guardia.getClave_empleado(), guardia.getFec_inicio(),
						guardia.getHora_inicio(), guardia.getHora_fin(),
						guardia.getHora_inicio(), guardia.getHora_fin(),
						guardia.getHora_inicio(), guardia.getHora_fin()});
	}

	@Override
	public int existe_guardia_upd(DatosGuardia guardia) {
		logger.info(QUERY_EXISTS_GUARDIA_INT_UPD);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_GUARDIA_INT_UPD, Integer.class,
				new Object[]{guardia.getClave_empleado(), guardia.getFec_inicio(),
						guardia.getId(),
						guardia.getHora_inicio(), guardia.getHora_fin(),
						guardia.getHora_inicio(), guardia.getHora_fin(),
						guardia.getHora_inicio(), guardia.getHora_fin()});
	}

	@Override
	public int existe_guardia_ext(DatosGuardia guardia) {
		logger.info(QUERY_EXISTS_GUARDIA_EXT);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_GUARDIA_EXT, Integer.class,
				new Object[]{guardia.getClave_empleado(), guardia.getFec_inicio(),
						guardia.getHora_inicio(), guardia.getHora_fin(),
						guardia.getHora_inicio(), guardia.getHora_fin(),
						guardia.getHora_inicio(), guardia.getHora_fin()});
	}

	@Override
	public int existe_guardia_ext_upd(DatosGuardia guardia) {
		logger.info(QUERY_EXISTS_GUARDIA_EXT_UPD);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_GUARDIA_EXT_UPD, Integer.class,
				new Object[]{guardia.getClave_empleado(), guardia.getFec_inicio(),
						guardia.getId(),
						guardia.getHora_inicio(), guardia.getHora_fin(),
						guardia.getHora_inicio(), guardia.getHora_fin(),
						guardia.getHora_inicio(), guardia.getHora_fin()});
	}

	// Validaciones de topes


	@Override
	public int get_horas_guardia(String clave_empleado, String inicio, String fin) {
		logger.info(QUERY_GET_HORAS_GUARDIA_INT);
		try {
			int horas = jdbcTemplate.queryForObject(QUERY_GET_HORAS_GUARDIA_INT, Integer.class,
					new Object[]{clave_empleado,
							inicio, fin, inicio, fin, inicio, fin});
			return horas;
		} catch (NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public int get_horas_guardia_upd(String clave_empleado, Integer id, String inicio, String fin) {
		logger.info(QUERY_GET_HORAS_GUARDIA_INT_UPD);
		try {
			int horas = jdbcTemplate.queryForObject(QUERY_GET_HORAS_GUARDIA_INT_UPD, Integer.class,
					new Object[]{clave_empleado, id,
							inicio, fin, inicio, fin, inicio, fin});
			return horas;
		} catch (NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public int get_horas_guardia_ext(String clave_empleado, String inicio, String fin) {
		logger.info(QUERY_GET_HORAS_GUARDIA_EXT);
		try {
			int horas = jdbcTemplate.queryForObject(QUERY_GET_HORAS_GUARDIA_EXT, Integer.class,
					new Object[]{clave_empleado,
							inicio, fin, inicio, fin, inicio, fin});
			return horas;
		} catch (NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public int get_horas_guardia_ext_upd(String clave_empleado, Integer id, String inicio, String fin) {
		logger.info(QUERY_GET_HORAS_GUARDIA_EXT_UPD);
		try {
			int horas = jdbcTemplate.queryForObject(QUERY_GET_HORAS_GUARDIA_EXT_UPD, Integer.class,
					new Object[]{clave_empleado,
							id, inicio, fin, inicio, fin, inicio, fin});
			return horas;
		} catch (NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public int get_dias_guardia(String clave_empleado, String inicio, String fin, String inicio1) {
		logger.info(QUERY_GET_DIAS_GUARDIA_INT);
		try {
			int dias = jdbcTemplate.queryForObject(QUERY_GET_DIAS_GUARDIA_INT, Integer.class,
					new Object[]{clave_empleado,
							inicio, fin, inicio, fin, inicio, fin, inicio1});
			return dias;
		} catch (NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public int get_dias_guardia_upd(String clave_empleado, Integer id, String inicio, String fin, String inicio1) {
		logger.info(QUERY_GET_DIAS_GUARDIA_INT_UPD);
		try {
			int dias = jdbcTemplate.queryForObject(QUERY_GET_DIAS_GUARDIA_INT_UPD, Integer.class,
					new Object[]{clave_empleado, id,
							inicio, fin, inicio, fin, inicio, fin, inicio1});
			return dias;
		} catch (NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public int get_dias_guardia_ext(String clave_empleado, String inicio, String fin, String inicio1) {
		logger.info(QUERY_GET_DIAS_GUARDIA_EXT);
		try {
			int dias = jdbcTemplate.queryForObject(QUERY_GET_DIAS_GUARDIA_EXT, Integer.class,
					new Object[]{clave_empleado,
							inicio, fin, inicio, fin, inicio, fin, inicio1});
			return dias;
		} catch (NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public int get_dias_guardia_ext_upd(String clave_empleado, Integer id, String inicio, String fin, String inicio1) {
		logger.info(QUERY_GET_DIAS_GUARDIA_EXT_UPD);
		try {
			int dias = jdbcTemplate.queryForObject(QUERY_GET_DIAS_GUARDIA_EXT_UPD, Integer.class,
					new Object[]{clave_empleado,
							inicio, fin, inicio, fin, inicio, fin, inicio1});
			return dias;
		} catch (NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public int updateStatusGuardia(int status, int id) {
		logger.info(STMT_UPDATE_STATUS);
		return jdbcTemplate.update(STMT_UPDATE_STATUS,
				new Object[]{status, id});
	}

	@Override
	public int updateStatusGuardiaExt(int status, int id) {
		logger.info(STMT_UPDATE_STATUS_EXT);
		return jdbcTemplate.update(STMT_UPDATE_STATUS_EXT,
				new Object[]{status, id});
	}

	@Override
	public int updateAuthStatusGuardia1(int status, int id, String tipo, String comentarios, int idUsuario) {
		logger.info(STMT_UPDATE_AUTH_STATUS_1);
		return jdbcTemplate.update(STMT_UPDATE_AUTH_STATUS_1,
				new Object[]{status, comentarios, idUsuario, id, tipo});
	}

	@Override
	public int updateAuthStatusGuardia2(int status, int id, String tipo, String comentarios, int idUsuario) {
		logger.info(STMT_UPDATE_AUTH_STATUS_2);
		return jdbcTemplate.update(STMT_UPDATE_AUTH_STATUS_2,
				new Object[]{status, comentarios, idUsuario, id, tipo});
	}

	@Override
	public int updateAuthStatusGuardias1(String tipo, String fec_pago, String idDeleg, String idDeleg1, int idUsuario) {
		logger.info(STMT_UPDATES_AUTH_STATUS_1);
		return jdbcTemplate.update(STMT_UPDATES_AUTH_STATUS_1,
				new Object[]{tipo, fec_pago, idDeleg, idDeleg1, idUsuario});
	}

	@Override
	public int updateAuthStatusGuardias1Ext(String tipo, String fec_pago, String idDeleg, String idDeleg1, int idUsuario) {
		logger.info(STMT_UPDATES_AUTH_STATUS_1ext);
		return jdbcTemplate.update(STMT_UPDATES_AUTH_STATUS_1ext,
				new Object[]{tipo, fec_pago, idDeleg, idDeleg1, idUsuario});
	}

//	@Override
//	public int countAuthGuardiasStatusInt(String fec_pago, String idDeleg) {
//		logger.info(STMT_COUNT_AUTH_STATUS_INT);
//		try {
//			int dias = jdbcTemplate.queryForObject(STMT_COUNT_AUTH_STATUS_INT, Integer.class,
//					new Object[]{ fec_pago, idDeleg, idDeleg });
//			return dias;
//		} catch (NullPointerException Ex) {
//			return 0;
//		}
//
//	}

	@Override
	public Integer countAuthGuardiasStatusInt(String fec_pago, String idDeleg) {
		logger.info(STMT_COUNT_AUTH_STATUS_INT);
		try {
			Integer dias = jdbcTemplate.queryForObject(STMT_COUNT_AUTH_STATUS_INT, Integer.class,
					new Object[]{ fec_pago, idDeleg, idDeleg });
			return (dias != null) ? dias : 0;
		} catch (Exception ex) {
			logger.error("Error al contar el estado de los guardias internas: ", ex);
			return 0;
		}
	}

	@Override
	public Integer countAuthGuardiasStatusExt(String fec_pago, String idDeleg) {
		logger.info(STMT_COUNT_AUTH_STATUS_EXT);
		try {
			Integer dias = jdbcTemplate.queryForObject(STMT_COUNT_AUTH_STATUS_EXT, Integer.class,
					new Object[]{ fec_pago, idDeleg, idDeleg });
			return (dias != null) ? dias : 0;
		} catch (Exception ex) {
			logger.error("Error al contar el estado de los guardias externas: ", ex);
			return 0;
		}
	}

	@Override
	public int updateAuthStatusGuardias2(String tipo, String fec_pago, String idDeleg, String idDeleg1, int idUsuario) {
		logger.info(STMT_UPDATES_AUTH_STATUS_2);
		return jdbcTemplate.update(STMT_UPDATES_AUTH_STATUS_2,
				new Object[]{tipo, fec_pago, idDeleg, idDeleg1, idUsuario});
	}

	@Override
	public int updateAuthStatusGuardias2Ext(String tipo, String fec_pago, String idDeleg, String idDeleg1, int idUsuario) {
		logger.info(STMT_UPDATES_AUTH_STATUS_2ext);
		return jdbcTemplate.update(STMT_UPDATES_AUTH_STATUS_2ext,
				new Object[]{tipo, fec_pago, idDeleg, idDeleg1, idUsuario});
	}

	@Override
	public List<DatosGuardia> ConsultaDynamicGuardias( String fechaPago, String tipo, String clave_empleado, Double importe_min, Double importe_max,
													  String idDelegacion, String idCentroTrab, String claveServicio, String puesto, Integer estatus) throws SQLException {

		String QUERY_CONDITION = "";
		String EMPLOYEE_FIELD = "";
		String QUERY_TABLE_BASE = "";


		if (tipo.equals(String.valueOf("GI"))) {
			QUERY_TABLE_BASE = "gys_guardias_emp";
			EMPLOYEE_FIELD = "id_empleado";

		}else if(tipo.equals(String.valueOf("GE"))){
			QUERY_TABLE_BASE = "gys_guardias_ext";
			EMPLOYEE_FIELD = "rfc";
		}else{
			throw new SQLException("No existe ese tipo de guardia en el sistema");
		}

		if (fechaPago != null) {
			QUERY_CONDITION += "And G.fec_paga = ?\r\n";
		}

		if (clave_empleado != null) {
			if (tipo.equals(String.valueOf("GI"))) {
				QUERY_CONDITION += "And id_empleado = ?\r\n";

			} else if(tipo.equals(String.valueOf("GE"))){
				QUERY_CONDITION += "And rfc = ?\r\n";
			}else{
				throw new SQLException("El numero de empleado es incorrecto");
			}
		}

		if (importe_min != null) {
			QUERY_CONDITION += "And G.importe >= ?\r\n";
		}

		if (importe_max != null) {
			QUERY_CONDITION += "And G.importe <= ?\r\n";
		}

		if (idDelegacion != null) {
			QUERY_CONDITION += "And C.id_area_generadora = ( Select id_area_generadora From m4t_delegaciones Where id_div_geografica = ? )\r\n";
		}

		if (idCentroTrab != null) {
			QUERY_CONDITION += "And C.id_centro_trabajo = ?\r\n";
		}

		if (claveServicio != null) {
			QUERY_CONDITION += "And G.id_clave_servicio = ?\r\n";
		}

		if (puesto != null) {
			QUERY_CONDITION += "And PU.id_puesto_plaza = ?\r\n";
		}

		if (estatus != null) {
			QUERY_CONDITION += "  And G.estatus = ?\r\n";
		}

		final String DYNAMIC_QUERY = "Select G.id, " + EMPLOYEE_FIELD + " clave_empleado, G.id_centro_trabajo, n_centro_trabajo,\r\n"
				+ "  G.id_clave_servicio, n_clave_servicio, G.id_puesto_plaza, n_puesto_plaza, '" + tipo + "' tipo_guardia,\r\n"
				+ "  id_nivel, id_sub_nivel, id_tipo_jornada, horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, hora_inicio, hora_fin, G.coment, G.estatus,\r\n"
				+ "  G.importe, PU.id_tipo_tabulador, G.fec_paga, C.id_zona, G.id_ordinal, NVL(riesgos,0) riesgos, G.id_usuario \r\n"
				+ "From " + QUERY_TABLE_BASE + " G, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU, m4t_clave_servicio SE\r\n"
				+ "Where G.fec_paga = P.fec_pago And \r\n"
				+ "  G.id_centro_trabajo = C.id_centro_trabajo And \r\n"
				+ "  G.id_puesto_plaza = PU.id_puesto_plaza And \r\n"
				+ "  PU.id_sociedad = '01' And PU.id_empresa = '01' And\r\n"
				+ "  G.id_clave_servicio = SE.id_clave_servicio And\r\n"
				+ "  SE.id_empresa='01'\r\n"
				+ QUERY_CONDITION
				+ "Order by G.fec_paga desc, G.fec_inicio";

		List<DatosGuardia> guardias = jdbcTemplate.query(DYNAMIC_QUERY, ps -> {

				int cont = 0;

				if (fechaPago != null) {
					cont++;
					ps.setString(cont, fechaPago);
				}

				if (clave_empleado != null) {
					cont++;
					ps.setString(cont, clave_empleado);
				}

				if (importe_min != null) {
					cont++;
					ps.setDouble(cont, importe_min);
				}

				if (importe_max != null) {
					cont++;
					ps.setDouble(cont, importe_max);
				}

				if (idDelegacion != null) {
					cont++;
					ps.setString(cont, idDelegacion);
				}

				if (idCentroTrab != null) {
					cont++;
					ps.setString(cont, idCentroTrab);
				}

				if (claveServicio != null) {
					cont++;
					ps.setString(cont, claveServicio);
				}

				if (puesto != null) {
					cont++;
					ps.setString(cont, puesto);
				}

				if (estatus != null) {
					cont++;
					ps.setInt(cont, estatus);
				}

			logger.info("Prepared statement: "+ DYNAMIC_QUERY);

			}, BeanPropertyRowMapper.newInstance(DatosGuardia.class));

			return guardias;

	}

	@Override
	public List<DatosGuardia> ConsultaDynamicGuardiasPage(
			String fechaPago, String tipo, String clave_empleado, Double importe_min, Double importe_max,
			String idDelegacion, String idCentroTrab, String claveServicio, String puesto, Integer estatus, int page, int size) throws SQLException {

		String QUERY_CONDITION = "";
		String EMPLOYEE_FIELD = "";
		String QUERY_TABLE_BASE = "";


		if (tipo.equals(String.valueOf("GI"))) {
			QUERY_TABLE_BASE = "gys_guardias_emp";
			EMPLOYEE_FIELD = "id_empleado";

		}else if(tipo.equals(String.valueOf("GE"))){
			QUERY_TABLE_BASE = "gys_guardias_ext";
			EMPLOYEE_FIELD = "rfc";
		}else{
			throw new SQLException("No existe ese tipo de guardia en el sistema");
		}

		if (fechaPago != null) {
			QUERY_CONDITION += "And G.fec_paga = ?\r\n";
		}

		if (clave_empleado != null) {
			if (tipo.equals(String.valueOf("GI"))) {
				QUERY_CONDITION += "And id_empleado = ?\r\n";

			} else {
				QUERY_CONDITION += "And rfc = ?\r\n";
			}
		}

		if (importe_min != null) {
			QUERY_CONDITION += "And G.importe >= ?\r\n";
		}

		if (importe_max != null) {
			QUERY_CONDITION += "And G.importe <= ?\r\n";
		}

		if (idDelegacion != null) {
			QUERY_CONDITION += "And C.id_area_generadora = ( Select id_area_generadora From m4t_delegaciones Where id_div_geografica = ? )\r\n";
		}

		if (idCentroTrab != null) {
			QUERY_CONDITION += "And C.id_centro_trabajo = ?\r\n";
		}

		if (claveServicio != null) {
			QUERY_CONDITION += "And G.id_clave_servicio = ?\r\n";
		}

		if (puesto != null) {
			QUERY_CONDITION += "And PU.id_puesto_plaza = ?\r\n";
		}

		if (estatus != null) {
			QUERY_CONDITION += "  And G.estatus = ?\r\n";
		}

		final String DYNAMIC_QUERY = "Select G.id, " + EMPLOYEE_FIELD + " clave_empleado, G.id_centro_trabajo, n_centro_trabajo,\r\n"
				+ "  G.id_clave_servicio, n_clave_servicio, G.id_puesto_plaza, n_puesto_plaza, '" + tipo + "' tipo_guardia,\r\n"
				+ "  id_nivel, id_sub_nivel, id_tipo_jornada, horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, hora_inicio, hora_fin, G.coment, G.estatus,\r\n"
				+ "  G.importe, PU.id_tipo_tabulador, G.fec_paga, C.id_zona, G.id_ordinal, NVL(riesgos,0) riesgos, G.id_usuario \r\n"
				+ "From " + QUERY_TABLE_BASE + " G, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU, m4t_clave_servicio SE\r\n"
				+ "Where G.fec_paga = P.fec_pago And \r\n"
				+ "  G.id_centro_trabajo = C.id_centro_trabajo And \r\n"
				+ "  G.id_puesto_plaza = PU.id_puesto_plaza And \r\n"
				+ "  PU.id_sociedad = '01' And PU.id_empresa = '01' And\r\n"
				+ "  G.id_clave_servicio = SE.id_clave_servicio And\r\n"
				+ "  SE.id_empresa='01'\r\n"
				+ QUERY_CONDITION
				+ "Order by G.fec_paga desc, G.fec_inicio\r\n"
				+ "Skip ?\r\n"
				+ "First ?";

        return jdbcTemplate.query(DYNAMIC_QUERY, ps -> {

			int cont = 0;

			if (fechaPago != null) {
				cont++;
				ps.setString(cont, fechaPago);
			}

			if (clave_empleado != null) {
				cont++;
				ps.setString(cont, clave_empleado);
			}

			if (importe_min != null) {
				cont++;
				ps.setDouble(cont, importe_min);
			}

			if (importe_max != null) {
				cont++;
				ps.setDouble(cont, importe_max);
			}

			if (idDelegacion != null) {
				cont++;
				ps.setString(cont, idDelegacion);
			}

			if (idCentroTrab != null) {
				cont++;
				ps.setString(cont, idCentroTrab);
			}

			if (claveServicio != null) {
				cont++;
				ps.setString(cont, claveServicio);
			}

			if (puesto != null) {
				cont++;
				ps.setString(cont, puesto);
			}

			if (estatus != null) {
				cont++;
				ps.setInt(cont, estatus);
			}

			logger.info("Prepared statement: "+ DYNAMIC_QUERY);

		}, BeanPropertyRowMapper.newInstance(DatosGuardia.class));

	}

	@Override
	public Long ConsultaDynamicGuardiasCount(
			String fechaPago, String tipo, String clave_empleado, Double importe_min, Double importe_max,
			String idDelegacion, String idCentroTrab, String claveServicio, String puesto, Integer estatus) throws SQLException {

		String QUERY_CONDITION = "";
		String EMPLOYEE_FIELD = "";
		String QUERY_TABLE_BASE = "";

		List<Object> params = new ArrayList<>();

		if (tipo.equals(String.valueOf("GI"))) {
			QUERY_TABLE_BASE = "gys_guardias_emp";
			EMPLOYEE_FIELD = "id_empleado";

		}else if(tipo.equals(String.valueOf("GE"))){
			QUERY_TABLE_BASE = "gys_guardias_ext";
			EMPLOYEE_FIELD = "rfc";
		}else{
			throw new SQLException("No existe ese tipo de guardia en el sistema");
		}

		if (fechaPago != null) {
			QUERY_CONDITION += "And G.fec_paga = ?\r\n";
			params.add(fechaPago);
		}

		if (clave_empleado != null) {
			if (tipo.equals(String.valueOf("GI"))) {
				QUERY_CONDITION += "And id_empleado = ?\r\n";

			} else {
				QUERY_CONDITION += "And rfc = ?\r\n";
			}
			params.add(clave_empleado);
		}

		if (importe_min != null) {
			QUERY_CONDITION += "And G.importe >= ?\r\n";
			params.add(importe_min);
		}

		if (importe_max != null) {
			QUERY_CONDITION += "And G.importe <= ?\r\n";
			params.add(importe_max);
		}

		if (idDelegacion != null) {
			QUERY_CONDITION += "And C.id_area_generadora = ( Select id_area_generadora From m4t_delegaciones Where id_div_geografica = ? )\r\n";
			params.add(idDelegacion);
		}

		if (idCentroTrab != null) {
			QUERY_CONDITION += "And C.id_centro_trabajo = ?\r\n";
			params.add(idCentroTrab);
		}

		if (claveServicio != null) {
			QUERY_CONDITION += "And G.id_clave_servicio = ?\r\n";
			params.add(claveServicio);
		}

		if (puesto != null) {
			QUERY_CONDITION += "And PU.id_puesto_plaza = ?\r\n";
			params.add(puesto);
		}

		if (estatus != null) {
			QUERY_CONDITION += "  And G.estatus = ?\r\n";
			params.add(estatus);
		}

		final String DYNAMIC_QUERY = "Select Count(*)\r\n"
				+ "From " + QUERY_TABLE_BASE + " G, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU, m4t_clave_servicio SE\r\n"
				+ "Where G.fec_paga = P.fec_pago And \r\n"
				+ "  G.id_centro_trabajo = C.id_centro_trabajo And \r\n"
				+ "  G.id_puesto_plaza = PU.id_puesto_plaza And \r\n"
				+ "  PU.id_sociedad = '01' And PU.id_empresa = '01' And\r\n"
				+ "  G.id_clave_servicio = SE.id_clave_servicio And\r\n"
				+ "  SE.id_empresa='01'\r\n"
				+ QUERY_CONDITION
				+ "Order by G.fec_paga desc, G.fec_inicio";

//		return jdbcTemplate.queryForObject(DYNAMIC_QUERY, Long.class,
//				fechaPago, clave_empleado, importe_min, importe_max, idDelegacion, idCentroTrab, claveServicio, puesto, estatus);
		return jdbcTemplate.queryForObject(DYNAMIC_QUERY, params.toArray(), Long.class);
	}



	@Override
	public List<DatosGuardia> ConsultaDynamicAuthGuardias(String fechaPago, String tipo, String idDelegacion,
														  String idCentroTrab, Integer estatus) {
		String QUERY_CONDITION = "";
		String EMPLOYEE_FIELD = "";
		String QUERY_TABLE_BASE = "";

		switch (tipo) {
			case "GI":
				QUERY_TABLE_BASE = "gys_autorizacion_guardias A, gys_guardias_emp";
				EMPLOYEE_FIELD = "id_empleado";
				break;

			case "GE":
				QUERY_TABLE_BASE = "gys_autorizacion_guardias A, gys_guardias_ext";
				EMPLOYEE_FIELD = "rfc";
				break;
		}

		if (fechaPago != null) {
			QUERY_CONDITION += "And G.fec_paga = ?\r\n";
		}

		if (idDelegacion != null) {
			QUERY_CONDITION += "And C.id_area_generadora = ( Select id_area_generadora From m4t_delegaciones Where id_div_geografica = ? )\r\n";
		}

		if (idCentroTrab != null) {
			QUERY_CONDITION += "And C.id_centro_trabajo = ?\r\n";
		}

		if (estatus != null) {

			switch (estatus) {

				case 0:
					QUERY_CONDITION += "  And A.estatus1 = ?\r\n";
					break;

				case 1, 2:
					QUERY_CONDITION += "  And A.estatus1 = ?\r\n";
					break;

				case 3, 4:
					QUERY_CONDITION += "  And A.estatus2 = ?\r\n";
					break;

			}

		}

		if (tipo != null){
			QUERY_CONDITION += (tipo.equals("GI")) ? "And A.id_tipo='GI'\r\n" : "And A.id_tipo='GE'\r\n";
		}

		final String DYNAMIC_QUERY = "Select G.id, " + EMPLOYEE_FIELD + " clave_empleado, G.id_centro_trabajo, n_centro_trabajo,\r\n"
				+ "  G.id_clave_servicio, n_clave_servicio, G.id_puesto_plaza, n_puesto_plaza, '" + tipo + "' tipo_guardia,\r\n"
				+ "  id_nivel, id_sub_nivel, id_tipo_jornada, horas, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, hora_inicio, hora_fin, G.coment, "
				+ "  Case When A.estatus2 = 0 Then A.estatus1 Else A.estatus2 End estatus,\r\n"
				+ "  G.importe, PU.id_tipo_tabulador, G.fec_paga, C.id_zona, G.id_ordinal, NVL(riesgos,0) riesgos, G.id_usuario \r\n"
				+ "From " + QUERY_TABLE_BASE + " G, gys_fechas_control P, m4t_centros_trab C, m4t_puestos_plaza PU, m4t_clave_servicio SE\r\n"
				+ "Where A.id_guardia = G.id And\r\n"
				+ "  G.fec_paga = P.fec_pago And\r\n"
				+ "	 G.fec_paga = A.fec_pago And\r\n"
				+ "  G.id_centro_trabajo = C.id_centro_trabajo And\r\n"
				+ "  G.id_puesto_plaza = PU.id_puesto_plaza And\r\n"
				+ "  PU.id_sociedad = '01' And PU.id_empresa = '01'\r\n And"
				+ "  G.id_clave_servicio = SE.id_clave_servicio And\r\n"
				+ "  SE.id_empresa='01'\r\n"
				+ QUERY_CONDITION
				+ "Order by G.fec_paga desc, G.fec_inicio";

		List<DatosGuardia> guardias = jdbcTemplate.query(DYNAMIC_QUERY, ps -> {
			int cont = 0;

			if (fechaPago != null) {
				cont ++ ;
				ps.setString(cont, fechaPago);
			}

			if (idDelegacion != null) {
				cont ++;
				ps.setString(cont, idDelegacion);
			}

			if (idCentroTrab != null) {
				cont++;
				ps.setString(cont, idCentroTrab);
			}

			if (estatus != null) {
				cont++;
				ps.setInt(cont, estatus);
			}

			logger.info("Prepared statement: "+ DYNAMIC_QUERY);
		}, BeanPropertyRowMapper.newInstance(DatosGuardia.class));

		return guardias;
	}

}