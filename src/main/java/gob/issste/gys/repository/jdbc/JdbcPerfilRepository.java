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
import gob.issste.gys.model.Opcion;
import gob.issste.gys.model.Perfil;
import gob.issste.gys.repository.PerfilRepository;

@Repository
public class JdbcPerfilRepository implements PerfilRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate  jdbcTemplate;

	@Override
	public int save(Perfil perfil) throws SQLException {
		logger.info(QUERY_ADD_NEW_PER);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_NEW_PER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, perfil.getDescripcion());
            preparedStatement.setString(2, perfil.getId_usuario());
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
	public int update(Perfil perfil) {
		logger.info(QUERY_UPDATE_PER);
		return jdbcTemplate.update(QUERY_UPDATE_PER,
	            new Object[] { perfil.getDescripcion(), perfil.getId_usuario(), perfil.getIdPerfil() });
	}

	@Override
	public Perfil findById(int id) {
		logger.info(QUERY_FIND_PER_BY_ID);
		try {
			Perfil perfil = jdbcTemplate.queryForObject(QUERY_FIND_PER_BY_ID,
			          BeanPropertyRowMapper.newInstance(Perfil.class), id);
	      return perfil;
	    } catch (IncorrectResultSizeDataAccessException e) {
	      return null;
	    }
	}

	@Override
	public int deleteById(int id) {
		logger.info(QUERY_DELETE_PER_BY_ID);
		return jdbcTemplate.update(QUERY_DELETE_PER_BY_ID, id);
	}

	@Override
	public List<Perfil> findAll() {
		logger.info(QUERY_GET_ALL_PER);
		return jdbcTemplate.query(QUERY_GET_ALL_PER, BeanPropertyRowMapper.newInstance(Perfil.class));
	}

	@Override
	public List<Perfil> findByDesc(String desc) {
		logger.info(QUERY_FIND_PER_BY_DESC);
		return jdbcTemplate.query(QUERY_FIND_PER_BY_DESC, BeanPropertyRowMapper.newInstance(Perfil.class), "%" + desc + "%");
	}

	@Override
	public int addOpcionToPerfil(int idUsuario, int idPerfil) {
		logger.info(QUERY_ADD_OPT_TO_PERF);
	    return jdbcTemplate.update(QUERY_ADD_OPT_TO_PERF,
	            new Object[] { idUsuario, idPerfil });
	}

	@Override
	public List<Opcion> getOpcionesForPerfil(Perfil perfil) {
		logger.info(QUERY_GET_OPC_FOR_PER);

		List<Opcion> opcionesList =
				this.jdbcTemplate.query(QUERY_GET_OPC_FOR_PER, 
				BeanPropertyRowMapper.newInstance(Opcion.class), perfil.getIdPerfil());

		return opcionesList;
	}

	@Override
	public int addPerfilToUser(int idUsuario, int idPerfil) {
		logger.info(QUERY_ADD_PER_TO_USER);
	    return jdbcTemplate.update(QUERY_ADD_PER_TO_USER,
	            new Object[] { idUsuario, idPerfil });
	}

	@Override
	public int removePerfilesToUser(int idUsuario) {
		logger.info(QUERY_DEL_PER_TO_USER);
		return jdbcTemplate.update(QUERY_DEL_PER_TO_USER, idUsuario);
	}

	@Override
	public int deleteOptToPerfil(int idPerfil) {
		logger.info(QUERY_DEL_OPT_TO_PERF);
		return jdbcTemplate.update(QUERY_DEL_OPT_TO_PERF, idPerfil);
	}

}
