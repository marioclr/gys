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
import gob.issste.gys.model.Beneficiario;
import gob.issste.gys.repository.IBeneficiarioRepository;

@Repository
public class JdbcBeneficiarioRepository implements IBeneficiarioRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public int save(Beneficiario beneficiario) throws SQLException {
		logger.info(STMT_ADD_NEW_BENEFICIARIO);
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(STMT_ADD_NEW_BENEFICIARIO, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, beneficiario.getIdBolsa());
            preparedStatement.setString(2, beneficiario.getRfc_bolsa());
            preparedStatement.setString(3, beneficiario.getNombre());
            preparedStatement.setString(4, beneficiario.getApellidoPaterno());
            preparedStatement.setString(5, beneficiario.getApellidoMaterno());
            preparedStatement.setString(6, beneficiario.getNumeroBenef());
			preparedStatement.setInt(7, beneficiario.getConsecutivo());
            preparedStatement.setInt(8, beneficiario.getPorcentaje());
            preparedStatement.setString(9, beneficiario.getId_centro_trab());
            preparedStatement.setString(10, beneficiario.getRfc());
            preparedStatement.setString(11, beneficiario.getFec_inicio());
            preparedStatement.setString(12, beneficiario.getFec_fin());
            preparedStatement.setInt(13, beneficiario.getCons_benef());
            preparedStatement.setString(14, beneficiario.getId_usuario());
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
	public int update(Beneficiario beneficiario) {
		logger.info(STMT_UPDATE_BENEFICIARIO);

		return jdbcTemplate.update(STMT_UPDATE_BENEFICIARIO, 
				new Object[] { beneficiario.getRfc_bolsa(), beneficiario.getNombre(), beneficiario.getApellidoPaterno(),
						beneficiario.getApellidoMaterno(), beneficiario.getNumeroBenef(), beneficiario.getConsecutivo(), beneficiario.getPorcentaje(),
						beneficiario.getId_centro_trab(), beneficiario.getRfc(), beneficiario.getFec_inicio(), beneficiario.getFec_fin(), 
						beneficiario.getCons_benef(), beneficiario.getId_usuario(), beneficiario.getId() });
	}

	@Override
	public int deleteById(int id) {
		logger.info(STMT_DELETE_BENEFICIARIO_BY_ID);
		return jdbcTemplate.update(STMT_DELETE_BENEFICIARIO_BY_ID, id);
	}

	@Override
	public Beneficiario findById(int id) {
		logger.info(QUERY_FIND_BENEFICIARIO_BY_ID);
		try {
			Beneficiario beneficiario = jdbcTemplate.queryForObject(QUERY_FIND_BENEFICIARIO_BY_ID,
			          BeanPropertyRowMapper.newInstance(Beneficiario.class), id);
	      return beneficiario;
	    } catch (IncorrectResultSizeDataAccessException e) {
	      return null;
	    }
	}

	@Override
	public List<Beneficiario> findAll() {
		logger.info(QUERY_GET_ALL_BENEFICIARIOS);
		return jdbcTemplate.query(QUERY_GET_ALL_BENEFICIARIOS, BeanPropertyRowMapper.newInstance(Beneficiario.class));
	}

	@Override
	public List<Beneficiario> findByName(String nombre) {
		logger.info(QUERY_FIND_BENEFICIARIO_BY_NAME);
		return jdbcTemplate.query(QUERY_FIND_BENEFICIARIO_BY_NAME, BeanPropertyRowMapper.newInstance(Beneficiario.class), "%" + nombre + "%");
	}

	@Override
	public int suma_porc_beneficiario(Beneficiario beneficiario) {
		logger.info(QUERY_SUMA_PORC_BENEFICIARIO);

		try {
			int suma = jdbcTemplate.queryForObject(QUERY_SUMA_PORC_BENEFICIARIO, Integer.class,
					new Object[] { beneficiario.getIdBolsa() } );
			return suma;
		} catch(NullPointerException Ex) {
			return 0;
		}
	}

	@Override
	public List<Beneficiario> get_dynamic_regs(String idDelegacion, Integer anio, String coment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Beneficiario> findByTrab(Integer idTrab) {
		logger.info(QUERY_FIND_BENEFICIARIO_BY_TRAB);
		return jdbcTemplate.query(QUERY_FIND_BENEFICIARIO_BY_TRAB, BeanPropertyRowMapper.newInstance(Beneficiario.class), idTrab);
	}

	@Override
	public int suma_porc_beneficiario_upd(Beneficiario beneficiario) {
		logger.info(QUERY_SUMA_PORC_BENEFICIARIO_UPD);

		try {

			int suma = jdbcTemplate.queryForObject(QUERY_SUMA_PORC_BENEFICIARIO_UPD, Integer.class,
					new Object[] { beneficiario.getIdBolsa(), beneficiario.getId() } ); 
			return suma;

		} catch(NullPointerException Ex) {
			return 0;
		}

	}

	@Override
	public List<Beneficiario> findByRFC(String rfc) {
		logger.info(QUERY_FIND_BENEFICIARIO_BY_RFC);
		return jdbcTemplate.query(QUERY_FIND_BENEFICIARIO_BY_RFC, BeanPropertyRowMapper.newInstance(Beneficiario.class), "%" + rfc + "%");
	}

}
