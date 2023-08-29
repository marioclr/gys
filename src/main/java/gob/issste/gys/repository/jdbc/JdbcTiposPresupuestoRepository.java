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
import gob.issste.gys.model.TiposPresupuesto;
import gob.issste.gys.repository.ITiposPresupuestoRepository;

@Repository
public class JdbcTiposPresupuestoRepository implements ITiposPresupuestoRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate  jdbcTemplate;

	@Override
	public int save(TiposPresupuesto tipMovPresup) {
		logger.info(QUERY_ADD_NEW_TIP_PRES);
	    return jdbcTemplate.update(QUERY_ADD_NEW_TIP_PRES,
	            new Object[] { tipMovPresup.getClave(), tipMovPresup.getDescripcion() });
	}

	@Override
	public int update(TiposPresupuesto tipMovPresup) {
		logger.info(QUERY_UPDATE_TIP_PRES);
		return jdbcTemplate.update(QUERY_UPDATE_TIP_PRES,
	            new Object[] { tipMovPresup.getClave(), tipMovPresup.getDescripcion(), tipMovPresup.getId() });
	}

	@Override
	public int deleteById(int id) {
		logger.info(QUERY_DELETE_TIP_PRES_BY_ID);
		return jdbcTemplate.update(QUERY_DELETE_TIP_PRES_BY_ID, id);
	}

	@Override
	public TiposPresupuesto findById(int id) {
		logger.info(QUERY_FIND_TIP_PRES_BY_ID);
		try {
			TiposPresupuesto tipo = jdbcTemplate.queryForObject(QUERY_FIND_TIP_PRES_BY_ID,
			          BeanPropertyRowMapper.newInstance(TiposPresupuesto.class), id);
	      return tipo;
	    } catch (IncorrectResultSizeDataAccessException e) {
	      return null;
	    }
	}

	@Override
	public List<TiposPresupuesto> findAll() {
		logger.info(QUERY_GET_ALL_TIP_PRES);
		return jdbcTemplate.query(QUERY_GET_ALL_TIP_PRES, BeanPropertyRowMapper.newInstance(TiposPresupuesto.class));
	}

	@Override
	public List<TiposPresupuesto> findByDesc(String desc) {
		logger.info(QUERY_FIND_TIP_PRES_BY_DESC);
		return jdbcTemplate.query(QUERY_FIND_TIP_PRES_BY_DESC, BeanPropertyRowMapper.newInstance(TiposPresupuesto.class), "%" + desc + "%");
	}

}
