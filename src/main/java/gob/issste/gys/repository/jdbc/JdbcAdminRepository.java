package gob.issste.gys.repository.jdbc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.CifrasDeImpuestos;
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

		logger.info(QUERY_CONSULTA_CIFRAS_ISR);

		return jdbcTemplate.queryForObject(QUERY_CONSULTA_CIFRAS_ISR, BeanPropertyRowMapper.newInstance(CifrasImpuesto.class), 
		new Object [] { fec_pago, tipo } );

	}

	@Override
	public List<String> consultaLayoutSPEP(String fec_pago, String tipo) {
		logger.info(QUERY_CONSULTA_LAYOUT_SPEP);

		return jdbcTemplate.queryForList(QUERY_CONSULTA_LAYOUT_SPEP, String.class, fec_pago, tipo, fec_pago, tipo );
	}

	@Override
	public int calcula_isr_guardia_non(Integer anio, Integer mes) {
		logger.info(STMT_CALCULA_ISR_GUARIDAS_NON);

		return jdbcTemplate.update(STMT_CALCULA_ISR_GUARIDAS_NON, anio, mes );
	}

	@Override
	public int calcula_isr_guardia_par(Integer anio, Integer mes) {
		logger.info(STMT_CALCULA_ISR_GUARIDAS_PAR);

		return jdbcTemplate.update(STMT_CALCULA_ISR_GUARIDAS_PAR, anio, mes );
	}

	@Override
	public CifrasDeImpuestos consultaCifrasDeImpuestos(Integer anio, Integer mes, Integer tipoPaga, String tipo) {
		logger.info(QUERY_GET_CIFRAS_ISR);

		return jdbcTemplate.queryForObject(QUERY_GET_CIFRAS_ISR, BeanPropertyRowMapper.newInstance(CifrasDeImpuestos.class), 
		new Object [] { anio, mes, tipoPaga, tipo } );
	}

	@Override
	public int calcula_isr_suplencia_non(Integer anio, Integer mes) {
		logger.info(STMT_CALCULA_ISR_SUPLENC_NON);

		return jdbcTemplate.update(STMT_CALCULA_ISR_SUPLENC_NON, anio, mes );
	}

	@Override
	public int calcula_isr_suplencia_par(Integer anio, Integer mes) {
		logger.info(STMT_CALCULA_ISR_SUPLENC_PAR);

		return jdbcTemplate.update(STMT_CALCULA_ISR_SUPLENC_PAR, anio, mes );
	}

	@Override
	public int elimina_cifras_impuesto(Integer anio, Integer mes, Integer tipoPaga, String tipo) {
		logger.info(STMT_ELIMINA_CALCULO_ISR);

		return jdbcTemplate.update(STMT_ELIMINA_CALCULO_ISR, anio, mes, tipoPaga, tipo );
	}

}