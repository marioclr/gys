package gob.issste.gys.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.CifrasImpuesto;
import gob.issste.gys.repository.IAdminRepository;

@Repository
public class JdbcAdminRepository implements IAdminRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public int calcula_isr_gua(String fec_pago) {

		logger.info(STMT_CALCULA_ISR_GUA);

		return jdbcTemplate.update(STMT_CALCULA_ISR_GUA, fec_pago );
	}

	@Override
	public int elimina_calculo_isr_guardia(String fec_pago) {

		logger.info(STMT_ELIMINA_CALCULO_ISR_GUA);

		return jdbcTemplate.update(STMT_ELIMINA_CALCULO_ISR_GUA, fec_pago );
	}

	@Override
	public int calcula_isr_sup(String fec_pago) {

		logger.info(STMT_CALCULA_ISR_SUP);

		return jdbcTemplate.update(STMT_CALCULA_ISR_SUP, fec_pago );
	}

	@Override
	public int elimina_calculo_isr_suplencia(String fec_pago) {

		logger.info(STMT_ELIMINA_CALCULO_ISR_SUP);

		return jdbcTemplate.update(STMT_ELIMINA_CALCULO_ISR_SUP, fec_pago );
	}

	@Override
	public CifrasImpuesto consultaCifras(String fec_pago, String tipo) {

		logger.info(STMT_CONSULTA_CIFRAS_ISR);

		return jdbcTemplate.queryForObject(STMT_CONSULTA_CIFRAS_ISR, BeanPropertyRowMapper.newInstance(CifrasImpuesto.class), 
		new Object [] { fec_pago, tipo } );

	}

}
