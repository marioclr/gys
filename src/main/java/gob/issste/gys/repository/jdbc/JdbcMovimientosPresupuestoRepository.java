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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.MovimientosPresupuesto;
import gob.issste.gys.repository.IMovimientosPresupuestoRepository;
import gob.issste.gys.repository.mappers.MovimPresupMapper;

@Repository
public class JdbcMovimientosPresupuestoRepository implements IMovimientosPresupuestoRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate  jdbcTemplate;

	@Override
	public int save(MovimientosPresupuesto movimPresup) throws SQLException {
		logger.info(QUERY_ADD_NEW_MOV_PRES);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_NEW_MOV_PRES, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, movimPresup.getIdPresup());
            preparedStatement.setDouble(2, movimPresup.getImporte());
            preparedStatement.setString(3, movimPresup.getComentarios());
            preparedStatement.setInt(4, movimPresup.getTipMovPresup().getId());
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
	public int update(MovimientosPresupuesto movimPresup) {
		logger.info(QUERY_UPDATE_MOV_PRES);
		return jdbcTemplate.update(QUERY_UPDATE_MOV_PRES,
	            new Object[] { movimPresup.getComentarios(), movimPresup.getId() });
	}

	@Override
	public MovimientosPresupuesto findById(int id) {
		logger.info(QUERY_FIND_MOV_PRES_BY_ID);
		try {
			MovimientosPresupuesto mov = jdbcTemplate.queryForObject(QUERY_FIND_MOV_PRES_BY_ID,
					new MovimPresupMapper(), id);
	      return mov;
	    } catch (IncorrectResultSizeDataAccessException e) {
	      return null;
	    }
	}

	@Override
	public List<MovimientosPresupuesto> findAll() {
		logger.info(QUERY_GET_ALL_MOV_PRES);
		return jdbcTemplate.query(QUERY_GET_ALL_MOV_PRES, new MovimPresupMapper());
	}

	@Override
	public List<MovimientosPresupuesto> findByPresupuesto(int idPresup) {
		logger.info(QUERY_FIND_MOV_PRES_BY_PRESUP);
		try {
			List<MovimientosPresupuesto> movs = jdbcTemplate.query(QUERY_FIND_MOV_PRES_BY_PRESUP,
					new MovimPresupMapper(), idPresup);
	      return movs;
	    } catch (IncorrectResultSizeDataAccessException e) {
	      return null;
	    }
	}

	@Override
	public List<MovimientosPresupuesto> findByTipo(int tipo) {
		logger.info(QUERY_FIND_MOV_PRES_BY_TIPO);
		try {
			List<MovimientosPresupuesto> movs = jdbcTemplate.query(QUERY_FIND_MOV_PRES_BY_TIPO,
					new MovimPresupMapper(), tipo);
	      return movs;
	    } catch (IncorrectResultSizeDataAccessException e) {
	      return null;
	    }
	}

}