package gob.issste.gys.repository.jdbc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
	public int save(Paga paga) {
		logger.info(QUERY_ADD_NEW_PAGAS);
	    return jdbcTemplate.update(QUERY_ADD_NEW_PAGAS,
	            new Object[] { paga.getFec_pago(), paga.getDescripcion(), paga.getEstatus(), paga.getFec_inicio(), paga.getFec_fin(), paga.getAnio_ejercicio() });
	}

	@Override
	public int update(Paga paga) {
		logger.info(QUERY_UPDATE_PAGAS);
		return jdbcTemplate.update(QUERY_UPDATE_PAGAS,
	            new Object[] { paga.getDescripcion(), paga.getEstatus(), paga.getFec_inicio(), paga.getFec_fin(), paga.getAnio_ejercicio(), paga.getId() });
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
