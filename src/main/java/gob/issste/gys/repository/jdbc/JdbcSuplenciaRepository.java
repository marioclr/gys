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
import gob.issste.gys.model.DatosEmpleado;
import gob.issste.gys.model.DatosSuplencia;
import gob.issste.gys.model.FactoresSuplencia;
import gob.issste.gys.repository.ISuplenciaRepository;

@Repository
public class JdbcSuplenciaRepository implements ISuplenciaRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public int save(DatosSuplencia suplencia) throws SQLException {
        logger.info(QUERY_ADD_NEW_SUPLENCIA);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_NEW_SUPLENCIA, Statement.RETURN_GENERATED_KEYS);

            // id_sociedad, id_empleado, fec_paga, fec_inicio, id_ordinal, fec_fin, id_empresa, "
			//+ "dias, inf_ordinal, id_usuario, fec_ult_actualizacion, coment, id_empleado_sup, "
			//+ "id_clave_movimiento, id_tipo_ausentismo, importe, folio,  motivo

			//String folio, String motivo, String movim, String coment, String usuario, String riesgos
            preparedStatement.setString(1, "01");
            preparedStatement.setString(2, suplencia.getEmpleado().getClave_empleado());
            preparedStatement.setString(3, suplencia.getFec_paga());
            preparedStatement.setString(4, suplencia.getFec_inicio());
            preparedStatement.setString(5, suplencia.getEmpleado().getClave_empleado());
            preparedStatement.setString(6, suplencia.getFec_paga());
            preparedStatement.setString(7, suplencia.getFec_inicio());
            preparedStatement.setString(8, suplencia.getFec_fin());
            preparedStatement.setString(9, suplencia.getId_empresa().compareTo("")== 0 ? "01" :suplencia.getId_empresa() );
            preparedStatement.setDouble(10, suplencia.getDias());
            preparedStatement.setString(11, ""); // inf_ordinal
            preparedStatement.setString(12, suplencia.getId_usuario());
            preparedStatement.setString(13, suplencia.getComent());
            preparedStatement.setString(14, suplencia.getEmpleado_suplir().getClave_empleado());
            preparedStatement.setString(15, suplencia.getId_clave_movimiento());
            preparedStatement.setString(16, "0801"); //id_tipo_ausentismo
            preparedStatement.setDouble(17, suplencia.getImporte().doubleValue());
            preparedStatement.setString(18, suplencia.getFolio());
            preparedStatement.setString(19, suplencia.getMotivo());

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
	public int save(DatosSuplencia suplencia, DatosEmpleado empleado) throws SQLException {
        logger.info(QUERY_ADD_NEW_SUPLENCIA_EMP);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_NEW_SUPLENCIA_EMP, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, "01");
            preparedStatement.setString(2, suplencia.getEmpleado().getClave_empleado());
            preparedStatement.setString(3, suplencia.getFec_paga());
            preparedStatement.setString(4, suplencia.getFec_inicio());
            preparedStatement.setString(5, suplencia.getEmpleado().getClave_empleado());
            preparedStatement.setString(6, suplencia.getFec_paga());
            preparedStatement.setString(7, suplencia.getFec_inicio());
            preparedStatement.setString(8, suplencia.getFec_fin());
            preparedStatement.setString(9, suplencia.getId_empresa().compareTo("")== 0 ? "01" :suplencia.getId_empresa() );
            preparedStatement.setDouble(10, suplencia.getDias());
            preparedStatement.setString(11, ""); // inf_ordinal
            preparedStatement.setString(12, suplencia.getId_usuario());
            preparedStatement.setString(13, suplencia.getComent());
            preparedStatement.setString(14, suplencia.getEmpleado_suplir().getClave_empleado());
            preparedStatement.setString(15, suplencia.getId_clave_movimiento());
            preparedStatement.setString(16, "0801"); //id_tipo_ausentismo
            preparedStatement.setDouble(17, suplencia.getImporte().doubleValue());
            preparedStatement.setString(18, suplencia.getFolio());
            preparedStatement.setString(19, suplencia.getMotivo());
            preparedStatement.setInt(20, suplencia.getRiesgos());
            preparedStatement.setString(21, empleado.getId_puesto_plaza());
            preparedStatement.setString(22, empleado.getId_clave_servicio());
            preparedStatement.setString(23, empleado.getId_tipo_jornada());
            preparedStatement.setString(24, empleado.getId_nivel());
            preparedStatement.setString(25, empleado.getId_sub_nivel());
            preparedStatement.setString(26, empleado.getId_centro_trabajo());
            preparedStatement.setString(27, empleado.getId_turno());

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
	public int saveExt(DatosSuplencia suplencia) throws SQLException {
        logger.info(QUERY_ADD_NEW_SUPLENCIA_EXT);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_NEW_SUPLENCIA_EXT, Statement.RETURN_GENERATED_KEYS);

            // id_sociedad, id_empleado, fec_paga, fec_inicio, id_ordinal, fec_fin, id_empresa, "
			//+ "dias, inf_ordinal, id_usuario, fec_ult_actualizacion, coment, id_empleado_sup, "
			//+ "id_clave_movimiento, id_tipo_ausentismo, importe, folio,  motivo

			//String folio, String motivo, String movim, String coment, String usuario, String riesgos

            //preparedStatement.setString(1, "01");
            preparedStatement.setString(1, suplencia.getEmpleado().getClave_empleado());
            preparedStatement.setString(2, suplencia.getFec_paga());
            preparedStatement.setString(3, suplencia.getFec_inicio());
            preparedStatement.setString(4, suplencia.getEmpleado().getClave_empleado());
            preparedStatement.setString(5, suplencia.getFec_paga());
            preparedStatement.setString(6, suplencia.getFec_inicio());
            preparedStatement.setString(7, suplencia.getFec_fin());
            //preparedStatement.setString(9, suplencia.getId_empresa().compareTo("")== 0 ? "01" :suplencia.getId_empresa() );
            preparedStatement.setDouble(8, suplencia.getDias());
            preparedStatement.setString(9, ""); // inf_ordinal
            preparedStatement.setString(10, suplencia.getId_usuario());
            preparedStatement.setString(11, suplencia.getComent());
            preparedStatement.setString(12, suplencia.getEmpleado_suplir().getClave_empleado());
            preparedStatement.setString(13, suplencia.getId_clave_movimiento());
            preparedStatement.setString(14, "0801"); //id_tipo_ausentismo
            preparedStatement.setDouble(15, suplencia.getImporte().doubleValue());
            preparedStatement.setString(16, suplencia.getFolio());
            preparedStatement.setString(17, suplencia.getMotivo());

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
	public int saveExt(DatosSuplencia suplencia, DatosEmpleado empleado) throws SQLException {
        logger.info(QUERY_ADD_NEW_SUPLENCIA_EXT_EMP);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_NEW_SUPLENCIA_EXT_EMP, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, suplencia.getEmpleado().getClave_empleado());
            preparedStatement.setString(2, suplencia.getFec_paga());
            preparedStatement.setString(3, suplencia.getFec_inicio());
            preparedStatement.setString(4, suplencia.getEmpleado().getClave_empleado());
            preparedStatement.setString(5, suplencia.getFec_paga());
            preparedStatement.setString(6, suplencia.getFec_inicio());
            preparedStatement.setString(7, suplencia.getFec_fin());
            preparedStatement.setDouble(8, suplencia.getDias());
            preparedStatement.setString(9, ""); // inf_ordinal
            preparedStatement.setString(10, suplencia.getId_usuario());
            preparedStatement.setString(11, suplencia.getComent());
            preparedStatement.setString(12, suplencia.getEmpleado_suplir().getClave_empleado());
            preparedStatement.setString(13, suplencia.getId_clave_movimiento());
            preparedStatement.setString(14, "0801"); //id_tipo_ausentismo
            preparedStatement.setDouble(15, suplencia.getImporte().doubleValue());
            preparedStatement.setString(16, suplencia.getFolio());
            preparedStatement.setString(17, suplencia.getMotivo());
            preparedStatement.setInt(18, suplencia.getRiesgos());
            preparedStatement.setString(19, empleado.getId_puesto_plaza());
            preparedStatement.setString(20, empleado.getId_clave_servicio());
            preparedStatement.setString(21, empleado.getId_tipo_jornada());
            preparedStatement.setString(22, empleado.getId_nivel());
            preparedStatement.setString(23, empleado.getId_sub_nivel());
            preparedStatement.setString(24, empleado.getId_centro_trabajo());
            preparedStatement.setString(25, empleado.getId_turno());

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
	public FactoresSuplencia ConsultaFactoresSuplencia(String id_turno) {
		logger.info(QUERY_GET_FACTORES_SUPLENCIA);
		return jdbcTemplate.queryForObject(QUERY_GET_FACTORES_SUPLENCIA, BeanPropertyRowMapper.newInstance(FactoresSuplencia.class), id_turno);
	}

	@Override
	public int updateImporteSuplencia(DatosSuplencia suplencia) {
		return jdbcTemplate.update(QUERY_UPD_IMP_SUPLENCIA, 
				new Object[] { suplencia.getImporte(), suplencia.getRiesgos(), suplencia.getClave_empleado(), 
						suplencia.getFec_paga(), suplencia.getFec_inicio(), suplencia.getId_ordinal() });
	}

	@Override
	public List<DatosSuplencia> ConsultaSuplenciasInternas(String id_empleado) {
		logger.info(QUERY_GET_SUPLENCIA_INTERNA);
		return jdbcTemplate.query(QUERY_GET_SUPLENCIA_INTERNA, BeanPropertyRowMapper.newInstance(DatosSuplencia.class), id_empleado );
	}

	@Override
	public List<DatosSuplencia> ConsultaSuplenciasInternasXFecha(String fechaPago) {
		logger.info(QUERY_GET_SUPLENCIAS_X_FECHA);
		return jdbcTemplate.query(QUERY_GET_SUPLENCIAS_X_FECHA, BeanPropertyRowMapper.newInstance(DatosSuplencia.class), fechaPago );
	}

	@Override
	public List<DatosSuplencia> ConsultaSuplenciasExternas(String rfcEmpleado) {
		logger.info(QUERY_GET_SUPLENCIA_EXTERNA);
		return jdbcTemplate.query(QUERY_GET_SUPLENCIA_EXTERNA, BeanPropertyRowMapper.newInstance(DatosSuplencia.class), rfcEmpleado );
	}

	@Override
	public int updateSuplencia(DatosSuplencia suplencia) {
		return jdbcTemplate.update(QUERY_UPD_SUPLENCIA, 
				new Object[] { suplencia.getImporte(), suplencia.getFolio(), suplencia.getMotivo(), suplencia.getId_clave_movimiento(), suplencia.getDias(),
						suplencia.getComent(), suplencia.getId_usuario(), suplencia.getFec_fin(), suplencia.getId() });
	}

	@Override
	public int updateSuplenciaExt(DatosSuplencia suplencia) {
		return jdbcTemplate.update(QUERY_UPD_SUPLENCIA_EXT, 
				new Object[] { suplencia.getImporte(), suplencia.getFolio(), suplencia.getMotivo(), suplencia.getId_clave_movimiento(), suplencia.getDias(),
						suplencia.getComent(), suplencia.getId_usuario(), suplencia.getFec_fin(), suplencia.getId() });
	}

	@Override
	public int deleteSuplencia(Integer idSuplencia) {
		logger.info(QUERY_DELETE_SUPLENCIA);
		return jdbcTemplate.update(QUERY_DELETE_SUPLENCIA, idSuplencia);
	}

	@Override
	public int deleteSuplenciaExt(Integer idSuplencia) {
		logger.info(QUERY_DELETE_SUPLENCIA_EXT);
		return jdbcTemplate.update(QUERY_DELETE_SUPLENCIA_EXT, idSuplencia);
	}

	@Override
	public DatosSuplencia findById(Integer idSuplencia) {
		logger.info(QUERY_GET_SUPLENCIA_BY_ID);
		return jdbcTemplate.queryForObject(QUERY_GET_SUPLENCIA_BY_ID, BeanPropertyRowMapper.newInstance(DatosSuplencia.class), idSuplencia );
	}

	@Override
	public DatosSuplencia findByIdExt(Integer idSuplencia) {
		logger.info(QUERY_GET_SUPLENCIA_EXT_BY_ID);
		return jdbcTemplate.queryForObject(QUERY_GET_SUPLENCIA_EXT_BY_ID, BeanPropertyRowMapper.newInstance(DatosSuplencia.class), idSuplencia );
	}

	@Override
	public double ObtenerSaldoUtilizado(String idDelegacion, int anio_ejercicio, int mes_ejercicio) {
		logger.info(QUERY_GET_SALDO_SUPLENCIA_INT);
		return jdbcTemplate.queryForObject(QUERY_GET_SALDO_SUPLENCIA_INT, Double.class, idDelegacion, anio_ejercicio, mes_ejercicio );
	}

	@Override
	public double ObtenerSaldoUtilizado_ct(int id, String id_centro_trabajo, int anio_ejercicio, int mes_ejercicio) {
		logger.info(QUERY_GET_SALDO_SUPLENCIA_INT_CT);
		return jdbcTemplate.queryForObject(QUERY_GET_SALDO_SUPLENCIA_INT_CT, Double.class, id, id_centro_trabajo, anio_ejercicio, mes_ejercicio );
	}

	@Override
	public double ObtenerSaldoUtilizadoExt(String idDelegacion, int anio_ejercicio, int mes_ejercicio) {
		logger.info(QUERY_GET_SALDO_SUPLENCIA_EXT);
		return jdbcTemplate.queryForObject(QUERY_GET_SALDO_SUPLENCIA_EXT, Double.class, idDelegacion, anio_ejercicio, mes_ejercicio );
	}

	@Override
	public double ObtenerSaldoUtilizadoExt_ct(int id, String id_centro_trabajo, int anio_ejercicio, int mes_ejercicio) {
		logger.info(QUERY_GET_SALDO_SUPLENCIA_EXT_CT);
		return jdbcTemplate.queryForObject(QUERY_GET_SALDO_SUPLENCIA_EXT_CT, Double.class, id, id_centro_trabajo, anio_ejercicio, mes_ejercicio );
	}

	@Override
	public int updateImporteSuplenciaExt(DatosSuplencia suplencia) {
		logger.info(QUERY_UPD_IMP_SUPLENCIA_EXT);
		return jdbcTemplate.update(QUERY_UPD_IMP_SUPLENCIA_EXT, 
				new Object[] { suplencia.getImporte(), suplencia.getRiesgos(), suplencia.getClave_empleado(), 
						suplencia.getFec_paga(), suplencia.getFec_inicio(), suplencia.getId_ordinal() });
	}

	@Override
	public List<DatosSuplencia> ConsultaSuplenciasExternasXFecha(String fechaPago) {
		logger.info(QUERY_GET_SUPL_EXT_X_FECHA);
		return jdbcTemplate.query(QUERY_GET_SUPL_EXT_X_FECHA, BeanPropertyRowMapper.newInstance(DatosSuplencia.class), fechaPago );
	}

	@Override
	public int existe_suplencia(DatosSuplencia suplencia) {
		logger.info(QUERY_EXISTS_SUPL_INT);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_SUPL_INT, Integer.class,
				new Object[] { suplencia.getClave_empleado(), suplencia.getFec_inicio(), suplencia.getFec_fin(), 
						suplencia.getFec_inicio(), suplencia.getFec_fin(), suplencia.getFec_inicio(), suplencia.getFec_fin() } );
	}

	@Override
	public int updateSuplenciaIntVars(DatosSuplencia suplencia) {
		return jdbcTemplate.update(QUERY_UPD_SUPLENCIA_INT_VARS, 
				new Object[] { suplencia.getImporte(), suplencia.getRiesgos(),
						suplencia.getEmpleado_suplir().getId_puesto_plaza(), suplencia.getEmpleado_suplir().getId_clave_servicio(),
						suplencia.getEmpleado_suplir().getId_nivel(), suplencia.getEmpleado_suplir().getId_sub_nivel(), 
						suplencia.getEmpleado_suplir().getId_tipo_jornada(), suplencia.getEmpleado_suplir().getId_centro_trabajo(),
						suplencia.getEmpleado_suplir().getId_turno(),
						suplencia.getClave_empleado(), suplencia.getFec_paga(), suplencia.getFec_inicio(), suplencia.getId_ordinal() });
	}

	@Override
	public int updateSuplenciaExtVars(DatosSuplencia suplencia) {
		logger.info(QUERY_UPD_SUPLENCIA_EXT_VARS);
		return jdbcTemplate.update(QUERY_UPD_SUPLENCIA_EXT_VARS, 
				new Object[] { suplencia.getImporte(), suplencia.getRiesgos(),
						suplencia.getEmpleado_suplir().getId_puesto_plaza(), suplencia.getEmpleado_suplir().getId_clave_servicio(),
						suplencia.getEmpleado_suplir().getId_nivel(), suplencia.getEmpleado_suplir().getId_sub_nivel(), 
						suplencia.getEmpleado_suplir().getId_tipo_jornada(), suplencia.getEmpleado_suplir().getId_centro_trabajo(),
						suplencia.getEmpleado_suplir().getId_turno(), suplencia.getSueldo(),
						suplencia.getClave_empleado(), suplencia.getFec_paga(), suplencia.getFec_inicio(), suplencia.getId_ordinal() });
	}

	@Override
	public int existe_suplenciaExt(DatosSuplencia suplencia) {
		logger.info(QUERY_EXISTS_SUPL_EXT);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_SUPL_EXT, Integer.class,
				new Object[] { suplencia.getClave_empleado(), 
						suplencia.getFec_inicio(), suplencia.getFec_fin(), 
						suplencia.getFec_inicio(), suplencia.getFec_fin(), 
						suplencia.getFec_inicio(), suplencia.getFec_fin() } );
	}

	@Override
	public int existe_suplencia_upd(DatosSuplencia suplencia) {
		logger.info(QUERY_EXISTS_SUPL_INT_UPD);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_SUPL_INT_UPD, Integer.class,
				new Object[] { suplencia.getClave_empleado(), suplencia.getId(), 
						suplencia.getFec_inicio(), suplencia.getFec_fin(), 
						suplencia.getFec_inicio(), suplencia.getFec_fin(), 
						suplencia.getFec_inicio(), suplencia.getFec_fin() } );
	}

	@Override
	public int existe_suplenciaExt_upd(DatosSuplencia suplencia) {
		logger.info(QUERY_EXISTS_SUPL_EXT_UPD);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_SUPL_EXT_UPD, Integer.class,
				new Object[] { suplencia.getClave_empleado(), suplencia.getId(), 
						suplencia.getFec_inicio(), suplencia.getFec_fin(), 
						suplencia.getFec_inicio(), suplencia.getFec_fin(), 
						suplencia.getFec_inicio(), suplencia.getFec_fin() } );
	}

	@Override
	public List<DatosSuplencia> ConsultaDynamicSuplencias(String fechaPago, String tipo, String clave_empleado,
			Double importe_min, Double importe_max, String idDelegacion, String idCentroTrab, String claveServicio, String puesto, String emp_suplir) {

		String QUERY_CONDITION  = "";
		String EMPLOYEE_FIELD   = "";
		String QUERY_TABLE_BASE = "";
		List<Object> objects = new ArrayList<Object>();

		if (tipo.equals(String.valueOf("SI"))) {
			QUERY_TABLE_BASE = "gys_suplencias_emp";
			EMPLOYEE_FIELD   = "G.id_empleado";
		} else {
			QUERY_TABLE_BASE = "gys_suplencias_ext";
			EMPLOYEE_FIELD   = "G.rfc";
		}

		if (fechaPago != null) { 
			QUERY_CONDITION += "  And G.fec_paga = ?\r\n";
			objects.add(fechaPago);
		}

		if (clave_empleado != null) {
			if (tipo.equals(String.valueOf("SI"))) {
				QUERY_CONDITION += "And G.id_empleado = ?\r\n";
				objects.add(clave_empleado);
			} else {
				QUERY_CONDITION += "And G.rfc = ?\r\n";
				objects.add(clave_empleado);
			}
		}

		if (importe_min != null) {
			QUERY_CONDITION += "  And G.importe >= ?\r\n";
			objects.add(importe_min.toString());
		}

		if (importe_max != null) {
			QUERY_CONDITION += "  And G.importe <= ?\r\n";
			objects.add(importe_max.toString());
		}

		if (idDelegacion != null) { 
			QUERY_CONDITION += "  And C.id_delegacion = ?\r\n";
			objects.add(idDelegacion);
		}

		if (idCentroTrab != null) { 
			QUERY_CONDITION += "  And C.id_centro_trabajo = ?\r\n";
			objects.add(idCentroTrab);
		}

		if (claveServicio != null) { 
			QUERY_CONDITION += "  And S.id_clave_servicio = ?\r\n";
			objects.add(claveServicio);
		}

		if (puesto != null) { 
			QUERY_CONDITION += "  And P.id_puesto_plaza = ?\r\n";
			objects.add(puesto);
		}

		if (emp_suplir != null) { 
			QUERY_CONDITION += "  And G.id_empleado_sup = ?\r\n";
			objects.add(puesto);
		}

		String DYNAMIC_QUERY = "Select G.id, " + EMPLOYEE_FIELD + " clave_empleado, G.id_centro_trabajo, G.id_clave_servicio, G.id_puesto_plaza, '" + tipo + "' tipo_suplencia,\r\n"
							 + "  G.id_nivel, G.id_sub_nivel, G.id_tipo_jornada, dias, G.fec_inicio, G.fec_fin, G.folio, G.motivo, G.id_clave_movimiento, G.coment,\r\n"
							 + "  G.importe, P.id_tipo_tabulador, G.fec_paga, C.id_zona, F.estatus, G.id_ordinal, NVL(riesgos,0) riesgos, G.id_usuario,\r\n"
							 + "  G.id_empleado_sup clave_empleado_suplir\r\n"
							 + "From " + QUERY_TABLE_BASE + " G, gys_fechas_control F, m4t_puestos_plaza P, m4t_clave_servicio S, m4t_centros_trab C\r\n"
							 + "Where G.fec_paga = F.fec_pago\r\n"
							 + "  And G.id_puesto_plaza = P.id_puesto_plaza\r\n"
							 + "  And P.id_empresa='01'\r\n"
							 + "  And G.id_clave_servicio = S.id_clave_servicio\r\n"
							 + "  And S.id_empresa='01'\r\n"
							 + "  And G.id_centro_trabajo = C.id_centro_trabajo\r\n"
							 + QUERY_CONDITION
							 + "Order by G.fec_paga desc, G.fec_inicio";

		logger.info(DYNAMIC_QUERY);
		List<DatosSuplencia> suplencias = jdbcTemplate.query(DYNAMIC_QUERY, BeanPropertyRowMapper.newInstance(DatosSuplencia.class), objects.toArray() );

		return suplencias;

	}

}
