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
import gob.issste.gys.model.DatosAdscripcion;
import gob.issste.gys.model.Delegacion;
import gob.issste.gys.repository.IDatosRepository;
import gob.issste.gys.repository.IDelegacionRepository;

@Repository
public class JdbcDelegacionRepository implements IDelegacionRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate  jdbcTemplate;
	@Autowired
	IDatosRepository datosRepository;

	@Override
	public Delegacion findById(int id) {
		logger.info(QUERY_FIND_DELEG_BY_ID);
		try {
			Delegacion opcion = jdbcTemplate.queryForObject(QUERY_FIND_DELEG_BY_ID,
			          BeanPropertyRowMapper.newInstance(Delegacion.class), id);
	      return opcion;
	    } catch (IncorrectResultSizeDataAccessException e) {
	      return null;
	    }
	}

	@Override
	public List<Delegacion> findAll() {
		logger.info(QUERY_GET_ALL_DELEG);
		return jdbcTemplate.query(QUERY_GET_ALL_DELEG, BeanPropertyRowMapper.newInstance(Delegacion.class));
	}

	@Override
	public List<Delegacion> findByDeleg(String clave_deleg) {
		logger.info(QUERY_FIND_DELEG_BY_DESC);
		return jdbcTemplate.query(QUERY_FIND_DELEG_BY_DESC, BeanPropertyRowMapper.newInstance(Delegacion.class), clave_deleg);
	}

	@Override
	public List<DatosAdscripcion> getCentrosTrabForDeleg(Delegacion delegacion) {
		logger.info(QUERY_FIND_CENT_TRAB_FOR_DELEG);

		List<DatosAdscripcion> centrosTrabList = null;
		
		if (delegacion.getId_div_geografica().compareTo("00")==0) {
			centrosTrabList = this.datosRepository.getDatosAdscripciones();
		} else {
			centrosTrabList = this.jdbcTemplate.query(QUERY_FIND_CENT_TRAB_FOR_DELEG,
					BeanPropertyRowMapper.newInstance(DatosAdscripcion.class), delegacion.getId_div_geografica());
		}
		return centrosTrabList;
	}
}
