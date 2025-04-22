package gob.issste.gys.repository.jdbc;

import java.sql.*;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

	public JdbcSuplenciaRepository() {
	}

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
	public double ObtenerSaldoUtilizado_ct(int id, String id_centro_trabajo, int anio_ejercicio, int mes_ejercicio, int quincena) {
		logger.info(QUERY_GET_SALDO_SUPLENCIA_INT_CT);
		return jdbcTemplate.queryForObject(QUERY_GET_SALDO_SUPLENCIA_INT_CT, Double.class, id, id_centro_trabajo, anio_ejercicio, mes_ejercicio, quincena );
	}

	@Override
	public double ObtenerSaldoUtilizadoExt(String idDelegacion, int anio_ejercicio, int mes_ejercicio) {
		logger.info(QUERY_GET_SALDO_SUPLENCIA_EXT);
		return jdbcTemplate.queryForObject(QUERY_GET_SALDO_SUPLENCIA_EXT, Double.class, idDelegacion, anio_ejercicio, mes_ejercicio );
	}

	@Override
	public double ObtenerSaldoUtilizadoExt_ct(int id, String id_centro_trabajo, int anio_ejercicio, int mes_ejercicio, int quincena) {
		logger.info(QUERY_GET_SALDO_SUPLENCIA_EXT_CT);
		return jdbcTemplate.queryForObject(QUERY_GET_SALDO_SUPLENCIA_EXT_CT, Double.class, id, id_centro_trabajo, anio_ejercicio, mes_ejercicio, quincena );
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
	public int existe_suplente(DatosSuplencia suplencia) {
		logger.info(QUERY_EXISTS_SUPLENTE_INT);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_SUPLENTE_INT, Integer.class,
				new Object[] { suplencia.getClave_empleado_suplir() , suplencia.getFec_inicio(), suplencia.getFec_fin(), 
						suplencia.getFec_inicio(), suplencia.getFec_fin(), suplencia.getFec_inicio(), suplencia.getFec_fin() } );
	}

	@Override
	public int existe_suplente_upd(DatosSuplencia suplencia) {
		logger.info(QUERY_EXISTS_SUPLENTE_INT_UPD);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_SUPLENTE_INT_UPD, Integer.class,
				new Object[] { suplencia.getClave_empleado_suplir(), suplencia.getId(), suplencia.getFec_inicio(), suplencia.getFec_fin(), 
						suplencia.getFec_inicio(), suplencia.getFec_fin(), suplencia.getFec_inicio(), suplencia.getFec_fin() } );
	}

	@Override
	public int updateSuplenciaIntVars(DatosSuplencia suplencia) {
		return jdbcTemplate.update(QUERY_UPD_SUPLENCIA_INT_VARS, 
				new Object[] { suplencia.getImporte(), suplencia.getRiesgos(),
						suplencia.getEmpleado_suplir().getId_puesto_plaza(), suplencia.getEmpleado_suplir().getId_clave_servicio(),
						suplencia.getEmpleado_suplir().getId_nivel(), suplencia.getEmpleado_suplir().getId_sub_nivel(), 
						suplencia.getEmpleado_suplir().getId_tipo_jornada(), suplencia.getEmpleado_suplir().getId_centro_trabajo(),
						suplencia.getEmpleado_suplir().getId_turno(), suplencia.getId_tipo_ausentismo(),
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
						suplencia.getEmpleado_suplir().getId_turno(), suplencia.getSueldo(), suplencia.getId_tipo_ausentismo(),
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
	public int existe_suplenteExt(DatosSuplencia suplencia) {
		logger.info(QUERY_EXISTS_SUPLENTE_EXT);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_SUPLENTE_EXT, Integer.class,
				new Object[] { suplencia.getClave_empleado_suplir(), 
						suplencia.getFec_inicio(), suplencia.getFec_fin(), 
						suplencia.getFec_inicio(), suplencia.getFec_fin(), 
						suplencia.getFec_inicio(), suplencia.getFec_fin() } );
	}

	@Override
	public int existe_suplenteExt_upd(DatosSuplencia suplencia) {
		logger.info(QUERY_EXISTS_SUPLENTE_EXT_UPD);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_SUPLENTE_EXT_UPD, Integer.class,
				new Object[] { suplencia.getClave_empleado_suplir(), suplencia.getId(),
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
	public int get_horas_suplencia(String clave_empleado, String inicio, String fin) {
		logger.info(QUERY_GET_HORAS_SUPLENCIA_INT);
		try {
			int horas = jdbcTemplate.queryForObject(QUERY_GET_HORAS_SUPLENCIA_INT, Integer.class,
					new Object[] { clave_empleado,
							inicio, fin, inicio, fin, inicio, fin } );
			return horas;
		} catch(EmptyResultDataAccessException Ex) {
			return 0;
		}catch(NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public int get_horas_suplencia_upd(String clave_empleado, Integer id, String inicio, String fin) {
		logger.info(QUERY_GET_HORAS_SUPLENCIA_INT_UPD);
		try {
			int horas = jdbcTemplate.queryForObject(QUERY_GET_HORAS_SUPLENCIA_INT_UPD, Integer.class,
					new Object[] { clave_empleado, id,
							inicio, fin, inicio, fin, inicio, fin } );
			return horas;
		} catch(EmptyResultDataAccessException Ex) {
			return 0;
		}catch(NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public int get_horas_suplencia_ext(String clave_empleado, String inicio, String fin) {
		logger.info(QUERY_GET_HORAS_SUPLENCIA_EXT);
		try {
			int horas = jdbcTemplate.queryForObject(QUERY_GET_HORAS_SUPLENCIA_EXT, Integer.class,
					new Object[] { clave_empleado,
							inicio, fin, inicio, fin, inicio, fin } );
			return horas;
		} catch(EmptyResultDataAccessException Ex) {
			return 0;
		}catch(NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public int get_horas_suplencia_ext_upd(String clave_empleado, Integer id, String inicio, String fin) {
		logger.info(QUERY_GET_HORAS_SUPLENCIA_EXT_UPD);
		try {
			int horas = jdbcTemplate.queryForObject(QUERY_GET_HORAS_SUPLENCIA_EXT_UPD, Integer.class,
					new Object[] { clave_empleado, id,
							inicio, fin, inicio, fin, inicio, fin } );
			return horas;
		} catch(EmptyResultDataAccessException Ex) {
			return 0;
		}catch(NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public int get_dias_suplencia(String clave_empleado, String inicio, String fin) {
		logger.info(QUERY_GET_DIAS_SUPLENCIA_INT);
		try {
			int dias = jdbcTemplate.queryForObject(QUERY_GET_DIAS_SUPLENCIA_INT, Integer.class,
					new Object[] { clave_empleado,
							inicio, fin, inicio, fin, inicio, fin } );
			return dias;
		} catch(NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public int get_dias_suplencia_upd(String clave_empleado, Integer id, String inicio, String fin) {
		logger.info(QUERY_GET_DIAS_SUPLENCIA_INT_UPD);
		try {
			int dias = jdbcTemplate.queryForObject(QUERY_GET_DIAS_SUPLENCIA_INT_UPD, Integer.class,
					new Object[] { clave_empleado, id,
							inicio, fin, inicio, fin, inicio, fin } );
			return dias;
		} catch(NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public int get_dias_suplencia_ext(String clave_empleado, String inicio, String fin) {
		logger.info(QUERY_GET_DIAS_SUPLENCIA_EXT);
		try {
			int dias = jdbcTemplate.queryForObject(QUERY_GET_DIAS_SUPLENCIA_EXT, Integer.class,
					new Object[] { clave_empleado,
							inicio, fin, inicio, fin, inicio, fin } );
			return dias;
		} catch(NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public int get_dias_suplencia_ext_upd(String clave_empleado, Integer id, String inicio, String fin) {
		logger.info(QUERY_GET_DIAS_SUPLENCIA_EXT_UPD);
		try {
			int dias = jdbcTemplate.queryForObject(QUERY_GET_DIAS_SUPLENCIA_EXT_UPD, Integer.class,
					new Object[] { clave_empleado, id,
							inicio, fin, inicio, fin, inicio, fin } );
			return dias;
		} catch(NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public int updateStatusSuplencia(int status, int id) {
		logger.info(STMT_UPDATE_STATUS);
		return jdbcTemplate.update(STMT_UPDATE_STATUS,
	            new Object[] { status, id });
	}

	@Override
	public int updateStatusSuplenciaExt(int status, int id) {
		logger.info(STMT_UPDATE_STATUS_EXT);
		return jdbcTemplate.update(STMT_UPDATE_STATUS_EXT,
	            new Object[] { status, id });
	}

	@Override
	public int updateAuthStatusSuplencia1(int status, int id, String tipo, String comentarios, int idUsuario) {
		logger.info(STMT_UPDATE_AUTH_STATUS_1);
		return jdbcTemplate.update(STMT_UPDATE_AUTH_STATUS_1,
	            new Object[] { status, comentarios, idUsuario, id, tipo });
	}

	@Override
	public int updateAuthStatusSuplencia2(int status, int id, String tipo, String comentarios, int idUsuario) {
		logger.info(STMT_UPDATE_AUTH_STATUS_2);
		return jdbcTemplate.update(STMT_UPDATE_AUTH_STATUS_2,
	            new Object[] { status, comentarios, idUsuario, id, tipo });
	}

	@Override
	public int updateAuthStatusSuplencias1(String tipo, String fec_pago, String idDeleg, String idDeleg1, int idUsuario) {
		logger.info(STMT_UPDATES_AUTH_STATUS_1);
	return jdbcTemplate.update(STMT_UPDATES_AUTH_STATUS_1,
	            new Object[] { tipo, fec_pago, idDeleg, idDeleg1, idUsuario });
	}

	@Override
	public int updateAuthStatusSuplencias1Ext(String tipo, String fec_pago, String idDeleg, String idDeleg1, int idUsuario) {
		logger.info(STMT_UPDATES_AUTH_STATUS_1ext);
		return jdbcTemplate.update(STMT_UPDATES_AUTH_STATUS_1ext,
	            new Object[] { tipo, fec_pago, idDeleg, idDeleg1, idUsuario });
	}

	@Override
	public int updateAuthStatusSuplencias2(String tipo, String fec_pago, String idDeleg, String idDeleg1, int idUsuario) {
		logger.info(STMT_UPDATES_AUTH_STATUS_2);
		return jdbcTemplate.update(STMT_UPDATES_AUTH_STATUS_2,
	            new Object[] { tipo, fec_pago, idDeleg, idDeleg1, idUsuario });
	}

	@Override
	public int updateAuthStatusSuplencias2Ext(String tipo, String fec_pago, String idDeleg, String idDeleg1, int idUsuario) {
		logger.info(STMT_UPDATES_AUTH_STATUS_2ext);
		return jdbcTemplate.update(STMT_UPDATES_AUTH_STATUS_2ext,
	            new Object[] { tipo, fec_pago, idDeleg, idDeleg1, idUsuario });
	}

	@Override
	public Integer countAuthSuplenciasStatusInt(String fec_pago, String idDeleg) {
		logger.info(STMT_COUNT_AUTH_STATUS_INT);
		try {
			Integer dias = jdbcTemplate.queryForObject(STMT_COUNT_AUTH_STATUS_INT, Integer.class,
					new Object[]{ fec_pago, idDeleg, idDeleg });
			return (dias != null) ? dias : 0;
		} catch (Exception ex) {
			logger.error("Error al contar los registros de las suplencias internas: ", ex);
			return 0;
		}
	}

	@Override
	public Integer countAuthSuplenciasStatusExt(String fec_pago, String idDeleg) {
		logger.info(STMT_COUNT_AUTH_STATUS_EXT);
		try {
			Integer dias = jdbcTemplate.queryForObject(STMT_COUNT_AUTH_STATUS_EXT, Integer.class,
					new Object[]{ fec_pago, idDeleg, idDeleg });
			return (dias != null) ? dias : 0;
		} catch (Exception ex) {
			logger.error("Error al contar los registros de las suplencias externas: ", ex);
			return 0;
		}
	}


	@Override
	public List<DatosSuplencia>
	ConsultaDynamicSuplencias(String fechaPago, String tipo, String clave_empleado,
														  Double importe_min, Double importe_max, String idDelegacion, String idCentroTrab, String claveServicio,
														  String puesto, String emp_suplir, Integer estatus) {
		String QUERY_CONDITION  = "";
		String EMPLOYEE_FIELD   = "";
		String QUERY_TABLE_BASE = "";

		if (tipo.equals(String.valueOf("SI"))) {
			QUERY_TABLE_BASE = "gys_suplencias_emp";
			EMPLOYEE_FIELD   = "S.id_empleado";
		} else {
			QUERY_TABLE_BASE = "gys_suplencias_ext";
			EMPLOYEE_FIELD   = "S.rfc";
		}

		if (fechaPago != null) {
			QUERY_CONDITION += "  And S.fec_paga = ?\r\n";
		}

		if (clave_empleado != null) {
			if (tipo.equals(String.valueOf("SI"))) {
				QUERY_CONDITION += "And S.id_empleado = ?\r\n";
			} else {
				QUERY_CONDITION += "And S.rfc = ?\r\n";
			}
		}

		if (importe_min != null) {
			QUERY_CONDITION += "  And S.importe >= ?\r\n";
		}

		if (importe_max != null) {
			QUERY_CONDITION += "  And S.importe <= ?\r\n";
		}

		if (idDelegacion != null) {
			QUERY_CONDITION += "  And C.id_area_generadora = ( Select id_area_generadora From m4t_delegaciones Where id_div_geografica = ? )\r\n";
		}

		if (idCentroTrab != null) {
			QUERY_CONDITION += "  And C.id_centro_trabajo = ?\r\n";
		}
		if (puesto != null) {
			QUERY_CONDITION += "  And P.id_puesto_plaza = ?\r\n";
		}

		if (claveServicio != null) {
			QUERY_CONDITION += "  And SE.id_clave_servicio = ?\r\n";
		}


		if (emp_suplir != null) {
			QUERY_CONDITION += "  And S.id_empleado_sup = ?\r\n";
		}

		if (estatus != null) {
			QUERY_CONDITION += "  And S.estatus = ?\r\n";
		}

		 final String DYNAMIC_QUERY = "Select S.id, " + EMPLOYEE_FIELD + " clave_empleado, S.id_centro_trabajo, n_centro_trabajo,\r\n"
				+ "  S.id_clave_servicio, n_clave_servicio, S.id_puesto_plaza, n_puesto_plaza, '" + tipo + "' tipo_suplencia,\r\n"
				+ "  S.id_nivel, S.id_sub_nivel, S.id_tipo_jornada, S.id_turno, dias, S.fec_inicio, S.fec_fin, S.folio, S.motivo, S.id_clave_movimiento, S.coment, S.estatus,\r\n"
				+ "  S.importe, P.id_tipo_tabulador, S.fec_paga, C.id_zona, S.id_ordinal, NVL(riesgos,0) riesgos, S.id_usuario,\r\n"
				+ "  S.id_empleado_sup clave_empleado_suplir\r\n"
				+ "	 From " + QUERY_TABLE_BASE + " S, gys_fechas_control F, m4t_puestos_plaza P, m4t_clave_servicio SE, m4t_centros_trab C\r\n"
				+ "	 Where S.fec_paga = F.fec_pago\r\n"
				+ "  And S.id_puesto_plaza = P.id_puesto_plaza\r\n"
				+ "  And P.id_empresa='01'\r\n"
				+ "  And S.id_clave_servicio = SE.id_clave_servicio\r\n"
				+ "  And SE.id_empresa='01'\r\n"
				+ "  And S.id_centro_trabajo = C.id_centro_trabajo\r\n"
				+    QUERY_CONDITION
				+ "	 Order by S.fec_paga desc, S.fec_inicio";

		List<DatosSuplencia> suplencias = jdbcTemplate.query( DYNAMIC_QUERY, ps -> {
		int count = 0;
    	if (fechaPago != null) {
		count ++;
        ps.setString(count, fechaPago);
    	}

    	if (clave_empleado != null) {
		count ++;
		ps.setString(count, clave_empleado);
    	}

    	if (importe_min != null) {
		count ++;
        ps.setString(count, importe_min.toString());
    	}

    	if (importe_max != null) {
		count ++;
        ps.setString(count, importe_max.toString());
    	}

    	if (idDelegacion != null) {
		count ++;
        ps.setString(count, idDelegacion);
    	}

    	if (idCentroTrab != null) {
		count ++;
        ps.setString(count, idCentroTrab);
    	}

		if (puesto != null) {
		count ++;
		ps.setString(count, puesto);
		}

    	if (claveServicio != null) {
		count ++;
        ps.setString(count, claveServicio);
    	}

    	if (emp_suplir != null) {
		count ++;
        ps.setString(count, emp_suplir);
    	}

    	if (estatus != null) {
		count ++;
        ps.setString(count, estatus.toString());
    	}
		logger.info("Prepared statement: "+ DYNAMIC_QUERY);
		}, BeanPropertyRowMapper.newInstance(DatosSuplencia.class));
			return suplencias;
	}

	@Override
	public List<DatosSuplencia> ConsultaDynamicAuthSuplencias(String fechaPago, String tipo, String idDelegacion,
			String idCentroTrab, Integer estatus) {

		String QUERY_CONDITION  = "";
		String EMPLOYEE_FIELD   = "";
		String QUERY_TABLE_BASE = "";
		switch (tipo) {
			case "SI":
				QUERY_TABLE_BASE = "gys_autorizacion_suplencias A, gys_suplencias_emp";
				EMPLOYEE_FIELD   = "S.id_empleado";
				break;
			case "SE":
				QUERY_TABLE_BASE = "gys_autorizacion_suplencias A, gys_suplencias_ext";
				EMPLOYEE_FIELD   = "S.rfc";
				break;
		}
		if (fechaPago != null) { 
			QUERY_CONDITION += "  And S.fec_paga = ?\r\n";
		}

		if (idDelegacion != null) { 
			QUERY_CONDITION += "  And C.id_area_generadora = ( Select id_area_generadora From m4t_delegaciones Where id_div_geografica = ? )\r\n";
		}

		if (idCentroTrab != null) {
			QUERY_CONDITION += "  And C.id_centro_trabajo = ?\r\n";
		}

		if (estatus != null) {
			switch (estatus) {
				case 0:
					QUERY_CONDITION += "  And A.estatus1 = ?\r\n";
					break;
				case 1,2:
					QUERY_CONDITION += "  And A.estatus1 = ?\r\n";
					break;
				case 3,4:
					QUERY_CONDITION += "  And A.estatus2 = ?\r\n";
					break;
			}
		}

		if (tipo != null){
			QUERY_CONDITION += (tipo.equals("SI")) ? "And A.id_tipo='SI'\r\n" : "And A.id_tipo='SE'\r\n";
		}

		String DYNAMIC_QUERY = "Select S.id, " + EMPLOYEE_FIELD + " clave_empleado, S.id_centro_trabajo, n_centro_trabajo,\r\n"
							 + "  S.id_clave_servicio, n_clave_servicio, S.id_puesto_plaza, n_puesto_plaza, '" + tipo + "' tipo_suplencia,\r\n"
							 + "  S.id_nivel, S.id_sub_nivel, S.id_tipo_jornada, S.id_turno, dias, S.fec_inicio, S.fec_fin, S.folio, S.motivo, S.id_clave_movimiento, S.coment,\r\n"
							 + "  Case When A.estatus2 = 0 Then A.estatus1 Else A.estatus2 End estatus,\r\n"
							 + "  S.importe, P.id_tipo_tabulador, S.fec_paga, C.id_zona, S.id_ordinal, NVL(riesgos,0) riesgos, S.id_usuario,\r\n"
							 + "  S.id_empleado_sup clave_empleado_suplir\r\n"
							 + "From " + QUERY_TABLE_BASE + " S, gys_fechas_control F, m4t_puestos_plaza P, m4t_clave_servicio SE, m4t_centros_trab C\r\n"
							 + "Where A.id_suplencia = S.id\r\n"
							 + "  And S.fec_paga = F.fec_pago\r\n"
							 + "  And S.fec_paga = A.fec_pago\r\n"
							 + "  And S.id_puesto_plaza = P.id_puesto_plaza\r\n"
							 + "  And P.id_empresa='01'\r\n"
							 + "  And S.id_clave_servicio = SE.id_clave_servicio\r\n"
							 + "  And SE.id_empresa='01'\r\n"
							 + "  And S.id_centro_trabajo = C.id_centro_trabajo\r\n"
							 + 	  QUERY_CONDITION
							 + "  Order by S.fec_paga desc, S.fec_inicio";

		List<DatosSuplencia> suplencias = jdbcTemplate.query(DYNAMIC_QUERY, ps ->{
			int count = 0;

			if (fechaPago != null) {
				count ++;
				ps.setString(count, fechaPago);
			}

			if (idDelegacion != null) {
				count ++;
				ps.setString(count, idDelegacion);
			}

			if (idCentroTrab != null) {
				count ++;
				ps.setString(count, idCentroTrab);
			}

			if (estatus != null) {
				count ++;
				ps.setString(count, estatus.toString());
			}

			logger.info("Prepared statement: "+ DYNAMIC_QUERY);
		}, BeanPropertyRowMapper.newInstance(DatosSuplencia.class));

		return suplencias;
	}

}
