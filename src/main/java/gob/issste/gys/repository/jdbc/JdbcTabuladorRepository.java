package gob.issste.gys.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.ValoresTabulador;
import gob.issste.gys.repository.ITabuladorRepository;

@Repository
public class JdbcTabuladorRepository implements ITabuladorRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public ValoresTabulador ConsultaTabuladorMedico(String zona, String nivel, String subnivel, String tipo_jornada, String quincena) {

		logger.info(QUERY_GET_TABULADOR_MEDICO);
		return jdbcTemplate.queryForObject(QUERY_GET_TABULADOR_MEDICO, BeanPropertyRowMapper.newInstance(ValoresTabulador.class), 
				new Object [] { zona, nivel, subnivel, tipo_jornada, quincena, quincena } );

	}

	@Override
	public ValoresTabulador ConsultaTabuladorOperativo(String zona, String nivel, String subnivel, String tipo_jornada, String quincena) {

		logger.info(QUERY_GET_TABULADOR_OPERATIVO);
		return jdbcTemplate.queryForObject(QUERY_GET_TABULADOR_OPERATIVO, BeanPropertyRowMapper.newInstance(ValoresTabulador.class), 
				new Object [] { zona, nivel, subnivel, quincena, quincena } );

	}
}