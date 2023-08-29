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
import gob.issste.gys.model.Opcion;
import gob.issste.gys.repository.OpcionRepository;

@Repository
public class JdbcOpcionRepository implements OpcionRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate  jdbcTemplate;

	@Override
	public int save(Opcion opcion) {
		logger.info(QUERY_ADD_NEW_OPT);
	    return jdbcTemplate.update(QUERY_ADD_NEW_OPT,
	            new Object[] { opcion.getDescripcion(), opcion.getComponente(), opcion.getId_usuario() });
	}

	@Override
	public int update(Opcion opcion) {
		logger.info(QUERY_UPDATE_OPT);
		return jdbcTemplate.update(QUERY_UPDATE_OPT,
	            new Object[] { opcion.getDescripcion(), opcion.getComponente(), opcion.getId_usuario(), opcion.getIdOpcion() });
	}

	@Override
	public Opcion findById(int id) {
		logger.info(QUERY_FIND_OPT_BY_ID);
		try {
			Opcion opcion = jdbcTemplate.queryForObject(QUERY_FIND_OPT_BY_ID,
			          BeanPropertyRowMapper.newInstance(Opcion.class), id);
	      return opcion;
	    } catch (IncorrectResultSizeDataAccessException e) {
	      return null;
	    }
	}

	@Override
	public int deleteById(int id) {
		logger.info(QUERY_DELETE_OPT_BY_ID);
		return jdbcTemplate.update(QUERY_DELETE_OPT_BY_ID, id);
	}

	@Override
	public List<Opcion> findAll() {
		logger.info(QUERY_GET_ALL_OPT);
		return jdbcTemplate.query(QUERY_GET_ALL_OPT, BeanPropertyRowMapper.newInstance(Opcion.class));
	}

	@Override
	public List<Opcion> findByDesc(String desc) {
		logger.info(QUERY_FIND_OPT_BY_DESC);
		return jdbcTemplate.query(QUERY_FIND_OPT_BY_DESC, BeanPropertyRowMapper.newInstance(Opcion.class), "%" + desc + "%");
	}

}
