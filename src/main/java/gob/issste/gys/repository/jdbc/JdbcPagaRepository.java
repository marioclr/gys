package gob.issste.gys.repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.Delegacion;
import gob.issste.gys.model.Paga;
import gob.issste.gys.repository.IPagaRepository;

@Repository
public class JdbcPagaRepository implements IPagaRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate  jdbcTemplate;

	@Override
	public int save(Paga paga) throws SQLException {
		logger.info(QUERY_ADD_NEW_PAGAS);
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_NEW_PAGAS, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, paga.getFec_pago());
            preparedStatement.setString(2, paga.getDescripcion());
            preparedStatement.setInt(3, paga.getEstatus());
            preparedStatement.setString(4, paga.getFec_inicio());
            preparedStatement.setString(5, paga.getFec_fin());
            preparedStatement.setInt(6, paga.getAnio_ejercicio());
            preparedStatement.setInt(7, paga.getMes_ejercicio());
            preparedStatement.setInt(8, paga.getId_tipo_paga());
            preparedStatement.setInt(9, paga.getIdnivelvisibilidad());
            return preparedStatement;
        };
        int updatesCount = jdbcTemplate.update(statementCreator, keyHolder);
        if (updatesCount == 1) {
            Number generatedKey = keyHolder.getKey();
            if (generatedKey == null) {
                throw new SQLException("Error al obtener ID de bolsa de trabajo.");
            }
            return generatedKey.intValue();
        }
        throw new SQLException("Se esperaba un registro de bolsa de trabajo insertado.");
	}

	@Override
	public int update(Paga paga) {
		logger.info(QUERY_UPDATE_PAGAS);
		return jdbcTemplate.update(QUERY_UPDATE_PAGAS,
	            new Object[] { paga.getDescripcion(), paga.getEstatus(), paga.getFec_inicio(), paga.getFec_fin(), paga.getAnio_ejercicio(), 
	            		paga.getMes_ejercicio(), paga.getId_tipo_paga(), paga.getIdnivelvisibilidad(), paga.getId() });
	}

	@Override
	public int deleteById(int id) {
		logger.info(QUERY_DELETE_PAGAS_BY_ID);
		return jdbcTemplate.update(QUERY_DELETE_PAGAS_BY_ID, id);
	}

	@Override
	public Paga findById(int id) {
		logger.info(QUERY_FIND_PAGAS_BY_ID);
		try {
			Paga paga = jdbcTemplate.queryForObject(QUERY_FIND_PAGAS_BY_ID,
			          BeanPropertyRowMapper.newInstance(Paga.class), id);
	      return paga;
	    } catch (IncorrectResultSizeDataAccessException e) {
	      return null;
	    }
	}

	@Override
	public List<Paga> findAll() {
		logger.info(QUERY_GET_ALL_PAGAS);
		return jdbcTemplate.query(QUERY_GET_ALL_PAGAS, BeanPropertyRowMapper.newInstance(Paga.class));
	}

	@Override
	public List<Paga> findByDesc(String desc) {
		logger.info(QUERY_FIND_PAGAS_BY_DESC);
		return jdbcTemplate.query(QUERY_FIND_PAGAS_BY_DESC, BeanPropertyRowMapper.newInstance(Paga.class), "%" + desc + "%");
	}

	@Override
	public Paga findByFecha(String fecha_pago) {
		logger.info(QUERY_FIND_PAGAS_BY_FEC);
		return jdbcTemplate.queryForObject(QUERY_FIND_PAGAS_BY_FEC, BeanPropertyRowMapper.newInstance(Paga.class), fecha_pago);
	}

	@Override
	public int activate(Paga paga) {
		logger.info(QUERY_ACTIVATE_PAGAS);
		return jdbcTemplate.update(QUERY_ACTIVATE_PAGAS,
	            new Object[] { paga.getEstatus(), paga.getId() });
	}

	@Override
	public List<Paga> findActivePagas() {
		logger.info(QUERY_GET_ACTIVE_PAGAS);
		return jdbcTemplate.query(QUERY_GET_ACTIVE_PAGAS, BeanPropertyRowMapper.newInstance(Paga.class));
	}

	@Override
	public List<Paga> findActivePagasByUser(int idUser) {
		logger.info(QUERY_GET_ACTIVE_PAGAS_BY_USR);
		return jdbcTemplate.query(QUERY_GET_ACTIVE_PAGAS_BY_USR, BeanPropertyRowMapper.newInstance(Paga.class), idUser);
	}

	@Override
	public List<Paga> findByStatus(int anio, int mes, int tipo, int status) {
		logger.info(QUERY_GET_PAGAS_BY_STATUS);
		return jdbcTemplate.query(QUERY_GET_PAGAS_BY_STATUS, BeanPropertyRowMapper.newInstance(Paga.class), anio, mes, tipo, status);
	}

	@Override
	public int updateStatus(int status, int anio, int mes, int tipo) {
		logger.info(STMT_UPDATE_STATUS);
		return jdbcTemplate.update(STMT_UPDATE_STATUS,
	            new Object[] { status, anio, mes, tipo });
	}

	@Override
	public int updateStatus(int status, int anio, int mes, int tipo, String fec_min, String fec_max) {
		logger.info(STMT_UPDATE_STATUS_REC);
		return jdbcTemplate.update(STMT_UPDATE_STATUS_REC,
	            new Object[] { status, anio, mes, tipo, fec_min, fec_max });
	}

	@Override
	public int existe_abierta(Paga paga) {
		logger.info(QUERY_EXISTS_PAGA_ABIERTA);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_PAGA_ABIERTA, Integer.class,
				new Object[] { paga.getAnio_ejercicio(), paga.getMes_ejercicio(), paga.getId_tipo_paga() } );
	}

	@Override
	public int existe_abierta_al_cambiar(Paga paga) {
		logger.info(QUERY_EXISTS_PAGA_ABIERTA_AL_CAMBIAR);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_PAGA_ABIERTA_AL_CAMBIAR, Integer.class,
				new Object[] { paga.getId(), paga.getAnio_ejercicio(), paga.getMes_ejercicio(), paga.getId_tipo_paga() } );
	}

	@Override
	public int existe_fecha_en_isr(Paga paga) {
		logger.info(QUERY_EXISTS_FECHA_EN_ISR);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_FECHA_EN_ISR, Integer.class,
				new Object[] { paga.getFec_pago(), paga.getFec_pago(),
							   paga.getFec_pago(), paga.getFec_pago(),
							   paga.getFec_pago(), paga.getFec_pago() } );
	}

	@Override
	public int existe_anterior_sin_terminar_non(Paga paga) {
		logger.info(QUERY_EXISTS_ANT_SIN_TERM_NON);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_ANT_SIN_TERM_NON, Integer.class,
				new Object[] { paga.getAnio_ejercicio(), paga.getMes_ejercicio(),
							   paga.getAnio_ejercicio(), paga.getMes_ejercicio(),
							   paga.getAnio_ejercicio() } );
	}

	@Override
	public int existe_anterior_sin_terminar_par(Paga paga) {
		logger.info(QUERY_EXISTS_ANT_SIN_TERM_PAR);
		int cuenta = jdbcTemplate.queryForObject(QUERY_EXISTS_ANT_SIN_TERM_PAR, Integer.class,
				new Object[] { paga.getAnio_ejercicio(), paga.getMes_ejercicio(),
						   paga.getAnio_ejercicio(), paga.getMes_ejercicio(),
						   paga.getAnio_ejercicio() } );
		return cuenta;
	}

	@Override
	public int existe_fecha_post_en_pagas(Paga paga) {
		logger.info(QUERY_EXISTS_FECHA_POSTERIOR);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_FECHA_POSTERIOR, Integer.class,
				new Object[] { paga.getFec_pago() } );
	}

	@Override
	public int existe_fecha_en_calculo_isr(String fecPaga, Integer anio, Integer mes, Integer tipoFechaControl,
			Integer id_ordinal) {
		logger.info(QUERY_EXISTS_FECHA_EN_CALCULO_ISR);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_FECHA_EN_CALCULO_ISR, Integer.class,
				new Object[] { anio, mes, tipoFechaControl, id_ordinal,
						fecPaga, fecPaga, fecPaga, fecPaga, fecPaga, fecPaga } );
	}

	@Override
	public int saveDelegForFecha(int IdFecha, String IdDeleg, String id_usuario) {
		logger.info(QUERY_ADD_DELEG_X_FECHA);
		return jdbcTemplate.update(QUERY_ADD_DELEG_X_FECHA, IdFecha, IdDeleg, id_usuario);
	}

	@Override
	public int removeDelegForFecha(int IdFecha) {
		logger.info(QUERY_DEL_FOR_FECHA);
		return jdbcTemplate.update(QUERY_DEL_FOR_FECHA, IdFecha);
	}

	@Override
	public List<Delegacion> getDelegForFecha(int IdFecha) {
		logger.info(QUERY_GET_DEL_FECHA);

		List<Delegacion> delegacionList =
				this.jdbcTemplate.query(QUERY_GET_DEL_FECHA,
				BeanPropertyRowMapper.newInstance(Delegacion.class), IdFecha);

		return delegacionList;
	}

	@Override
	public int AuthGuardiasInt(Paga paga) {
		logger.info(STMT_INSERT_GUARD_INT_AUT);
		return jdbcTemplate.update(STMT_INSERT_GUARD_INT_AUT, paga.getFec_pago());
	}

	@Override
	public int AuthGuardiasExt(Paga paga) {
		logger.info(STMT_INSERT_GUARD_EXT_AUT);
		return jdbcTemplate.update(STMT_INSERT_GUARD_EXT_AUT, paga.getFec_pago());
	}

	@Override
	public int AuthSuplenciasInt(Paga paga) {
		logger.info(STMT_INSERT_SUPLE_INT_AUT);
		return jdbcTemplate.update(STMT_INSERT_SUPLE_INT_AUT, paga.getFec_pago());
	}

	@Override
	public int AuthSuplenciasExt(Paga paga) {
		logger.info(STMT_INSERT_SUPLE_EXT_AUT);
		return jdbcTemplate.update(STMT_INSERT_SUPLE_EXT_AUT, paga.getFec_pago());
	}

	@Override
	public int BorraAuthGuardias(Paga paga) {
		logger.info(STMT_DELETE_GUARDIAS_AUT);
		return jdbcTemplate.update(STMT_DELETE_GUARDIAS_AUT, paga.getFec_pago());
	}

	@Override
	public int BorraAuthSuplencias(Paga paga) {
		logger.info(STMT_DELETE_SUPLENCIAS_AUT);
		return jdbcTemplate.update(STMT_DELETE_SUPLENCIAS_AUT, paga.getFec_pago());
	}

	@Override
	public int verifica_paga_cerrada(String fecha) {
		logger.info(QUERY_VERIFY_PAGA_CERRADA);
		return jdbcTemplate.queryForObject(QUERY_VERIFY_PAGA_CERRADA, Integer.class,
				fecha );
	}

}