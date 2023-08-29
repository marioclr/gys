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
import gob.issste.gys.model.CentroTrabajo;
import gob.issste.gys.repository.ICentroTrabajoRepository;

@Repository
public class JdbcCentroTrabajoRepository implements ICentroTrabajoRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate  jdbcTemplate;

	@Override
	public int save(CentroTrabajo CentroTrabajo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(CentroTrabajo CentroTrabajo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CentroTrabajo findById(int id) {
		logger.info(QUERY_FIND_CENT_TRAB_BY_ID);
		try {
			CentroTrabajo opcion = jdbcTemplate.queryForObject(QUERY_FIND_CENT_TRAB_BY_ID,
			          BeanPropertyRowMapper.newInstance(CentroTrabajo.class), id);
	      return opcion;
	    } catch (IncorrectResultSizeDataAccessException e) {
	      return null;
	    }
	}

	@Override
	public int deleteById(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<CentroTrabajo> findAll() {
		logger.info(QUERY_GET_ALL_CENT_TRAB);
		return jdbcTemplate.query(QUERY_GET_ALL_CENT_TRAB, BeanPropertyRowMapper.newInstance(CentroTrabajo.class));
	}

	@Override
	public List<CentroTrabajo> findByDeleg(String clave_deleg) {
		logger.info(QUERY_FIND_CENT_TRAB_BY_DESC);
		return jdbcTemplate.query(QUERY_FIND_CENT_TRAB_BY_DESC, BeanPropertyRowMapper.newInstance(CentroTrabajo.class), clave_deleg);
	}

}
