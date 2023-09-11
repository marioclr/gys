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
	            		paga.getMes_ejercicio(), paga.getId_tipo_paga(), paga.getId() });
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

}
