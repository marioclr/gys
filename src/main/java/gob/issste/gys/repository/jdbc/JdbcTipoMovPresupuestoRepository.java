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
import gob.issste.gys.model.TipoMovPresupuesto;
import gob.issste.gys.repository.ITipoMovPresupuestoRepository;

@Repository
public class JdbcTipoMovPresupuestoRepository implements ITipoMovPresupuestoRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate  jdbcTemplate;

	@Override
	public int save(TipoMovPresupuesto tipMovPresup) {
		logger.info(QUERY_ADD_NEW_TIP_MOV_PRES);
	    return jdbcTemplate.update(QUERY_ADD_NEW_TIP_MOV_PRES,
	            new Object[] { tipMovPresup.getClave(), tipMovPresup.getDescripcion() });
	}

	@Override
	public int update(TipoMovPresupuesto tipMovPresup) {
		logger.info(QUERY_UPDATE_TIP_MOV_PRES);
		return jdbcTemplate.update(QUERY_UPDATE_TIP_MOV_PRES,
	            new Object[] { tipMovPresup.getClave(), tipMovPresup.getDescripcion(), tipMovPresup.getId() });
	}

	@Override
	public int deleteById(int id) {
		logger.info(QUERY_DELETE_TIP_MOV_PRES_BY_ID);
		return jdbcTemplate.update(QUERY_DELETE_TIP_MOV_PRES_BY_ID, id);
	}

	@Override
	public TipoMovPresupuesto findById(int id) {
		logger.info(QUERY_FIND_TIP_MOV_PRES_BY_ID);
		try {
			TipoMovPresupuesto tipo = jdbcTemplate.queryForObject(QUERY_FIND_TIP_MOV_PRES_BY_ID,
			          BeanPropertyRowMapper.newInstance(TipoMovPresupuesto.class), id);
	      return tipo;
	    } catch (IncorrectResultSizeDataAccessException e) {
	      return null;
	    }
	}

	@Override
	public List<TipoMovPresupuesto> findAll() {
		logger.info(QUERY_GET_ALL_TIP_MOV_PRES);
		return jdbcTemplate.query(QUERY_GET_ALL_TIP_MOV_PRES, BeanPropertyRowMapper.newInstance(TipoMovPresupuesto.class));
	}

	@Override
	public List<TipoMovPresupuesto> findByDesc(String desc) {
		logger.info(QUERY_FIND_TIP_MOV_PRES_BY_DESC);
		return jdbcTemplate.query(QUERY_FIND_TIP_MOV_PRES_BY_DESC, BeanPropertyRowMapper.newInstance(TipoMovPresupuesto.class), "%" + desc + "%");
	}

}
