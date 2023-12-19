package gob.issste.gys.repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.BolsaTrabajo;
import gob.issste.gys.repository.IBolsaTrabajoRepository;
import gob.issste.gys.repository.mappers.BolsaTrabajoMapper;

@Repository
public class JdbcBolsaTrabajoRepository implements IBolsaTrabajoRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public int save(BolsaTrabajo elemento) throws SQLException {
		logger.info(QUERY_ADD_BOLSA_TRABAJO);
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_BOLSA_TRABAJO, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, elemento.getRfc());
            preparedStatement.setString(2, elemento.getNombre());
            preparedStatement.setString(3, elemento.getApellidoPat());
            preparedStatement.setString(4, elemento.getApellidoMat());
            preparedStatement.setString(5, elemento.getDelegacion().getId_div_geografica());
            preparedStatement.setString(6, elemento.getCodigoPostal());
            preparedStatement.setString(7, elemento.getCurp());
            preparedStatement.setString(8, elemento.getId_beneficiario());
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
	public int update(BolsaTrabajo elemento) {
		logger.info(QUERY_UPD_BOLSA_TRABAJO);
		return jdbcTemplate.update(QUERY_UPD_BOLSA_TRABAJO, 
				new Object[] { elemento.getRfc(), elemento.getNombre(), elemento.getApellidoPat(), elemento.getApellidoMat(),
						elemento.getDelegacion().getId_div_geografica(), elemento.getCodigoPostal(), elemento.getCurp(), 
						elemento.getId_beneficiario(), elemento.getId() });
	}

	@Override
	public List<BolsaTrabajo> findAll() {
		
		logger.info(QUERY_GET_BOLSA_TRABAJO);
		List<BolsaTrabajo> bolsa = jdbcTemplate.query(QUERY_GET_BOLSA_TRABAJO, new BolsaTrabajoMapper());
		return bolsa;
	}

	@Override
	public BolsaTrabajo findByRFC(String rfc) {
		logger.info(QUERY_GET_BOLSA_TRABAJO_BY_RFC);
		try {
			BolsaTrabajo bolsa = jdbcTemplate.queryForObject(QUERY_GET_BOLSA_TRABAJO_BY_RFC, 
					new BolsaTrabajoMapper(), rfc);
			return bolsa;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public BolsaTrabajo findByRFCDel(String rfc, String idDeleg) {
		logger.info(QUERY_GET_BOLSA_TRABAJO_BY_RFC);
		try {
			BolsaTrabajo bolsa = jdbcTemplate.queryForObject(QUERY_GET_BOLSA_TRABAJO_BY_RFC, 
					new BolsaTrabajoMapper(), rfc, idDeleg);
			return bolsa;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<String> findLikeRFC(String rfc) {
		logger.info(QUERY_GET_BOLSA_TRABAJO_LIKE_RFC);
		
		try {
			List<String> rfcs = jdbcTemplate.queryForList(QUERY_GET_BOLSA_TRABAJO_LIKE_RFC, String.class, "%" + rfc + "%");
			return rfcs;
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<String> findLikeRFC(String rfc, String idDeleg) {
		logger.info(QUERY_GET_BOLSA_TRAB_LIKE_RFC_DEL);
		
		try {
			List<String> rfcs = jdbcTemplate.queryForList(QUERY_GET_BOLSA_TRAB_LIKE_RFC_DEL, String.class, "%" + rfc + "%", idDeleg);
			return rfcs;
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}

	@Override
	public int deleteById(int id) {
		logger.info(QUERY_DEL_BOLSA_TRABAJO);
		return jdbcTemplate.update(QUERY_DEL_BOLSA_TRABAJO, id);
	}

	@Override
	public BolsaTrabajo getElementById(int id) {
		logger.info(QUERY_GET_BOLSA_TRABAJO_BY_ID);
		try {
			BolsaTrabajo elemento = jdbcTemplate.queryForObject(QUERY_GET_BOLSA_TRABAJO_BY_ID,
					new BolsaTrabajoMapper(), id);
			return elemento;
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<BolsaTrabajo> findForDeleg(String idDeleg) {
		logger.info(QUERY_GET_BOLSA_TRABAJO_FOR_DELEG);

		try {
			List<BolsaTrabajo> elemento = jdbcTemplate.query(QUERY_GET_BOLSA_TRABAJO_FOR_DELEG,
					new BolsaTrabajoMapper(), idDeleg);
			return elemento;
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}

}