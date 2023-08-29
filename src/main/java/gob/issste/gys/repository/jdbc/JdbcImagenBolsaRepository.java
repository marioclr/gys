package gob.issste.gys.repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
import gob.issste.gys.model.DocumentoDigital;
import gob.issste.gys.repository.IImagenBolsaRepository;

@Repository
public class JdbcImagenBolsaRepository implements IImagenBolsaRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public int save(DocumentoDigital elemento) throws SQLException {
		logger.info(QUERY_ADD_IMG_BOLSA_TRABAJO);

		KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_IMG_BOLSA_TRABAJO, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, elemento.getId_asociado());
            preparedStatement.setBytes(2, elemento.getImagen());
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
	public DocumentoDigital getElementById(int id) {
		logger.info(QUERY_GET_IMG_BOLSA_TRABAJO_BY_ID);
		try {
			DocumentoDigital elemento = jdbcTemplate.queryForObject(QUERY_GET_IMG_BOLSA_TRABAJO_BY_ID,
					BeanPropertyRowMapper.newInstance(DocumentoDigital.class), id);
			return elemento;
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}

}
