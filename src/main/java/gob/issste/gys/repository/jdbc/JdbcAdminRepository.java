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
import gob.issste.gys.model.DetalleCifrasDeImpuestos;
import gob.issste.gys.repository.IAdminRepository;

@Repository
public class JdbcAdminRepository implements IAdminRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<String> consultaLayoutSPEP(String fec_pago, String tipo) {
		logger.info(QUERY_CONSULTA_LAYOUT_SPEP);

		return jdbcTemplate.queryForList(QUERY_CONSULTA_LAYOUT_SPEP, String.class, fec_pago, tipo, fec_pago, tipo );
	}

	@Override
	public int calcula_isr_guardia_non(Integer anio, Integer mes) {
		logger.info(STMT_CALCULA_ISR_GUARIDAS_NON);

		return jdbcTemplate.update(STMT_CALCULA_ISR_GUARIDAS_NON, anio, mes, anio, mes );
	}


	@Override
	public int calcula_isr_non(Integer anio, Integer mes) {
		logger.info(STMT_CALCULA_ISR_NON);

		return jdbcTemplate.update(STMT_CALCULA_ISR_NON, anio, mes, anio, mes, anio, mes );
	}

	@Override
	public int re_calcula_isr_non(Integer anio, Integer mes, String fec_min, String fec_max) {
		logger.info(STMT_RE_CALCULA_ISR_NON);

		return jdbcTemplate.update(STMT_RE_CALCULA_ISR_NON, anio, mes, anio, mes, fec_min, fec_max, anio, mes, fec_min, fec_max );
	}

	@Override
	public int re_calcula_isr_guardia_non(Integer anio, Integer mes, String fec_min, String fec_max) {
		logger.info(STMT_RECALCULA_ISR_GUARIDAS_NON);

		return jdbcTemplate.update(STMT_RECALCULA_ISR_GUARIDAS_NON, anio, mes, fec_min, fec_max );
	}

	@Override
	public int calcula_isr_guardia_par(Integer anio, Integer mes) {
		logger.info(STMT_CALCULA_ISR_GUARIDAS_PAR);

		return jdbcTemplate.update(STMT_CALCULA_ISR_GUARIDAS_PAR, anio, mes );
	}

	@Override
	public List<CifrasDeImpuestos> consultaCifrasDeImpuestos(Integer anio, Integer mes, Integer tipoPaga) {
		logger.info(QUERY_GET_CIFRAS_ISR);

		return jdbcTemplate.query(QUERY_GET_CIFRAS_ISR, BeanPropertyRowMapper.newInstance(CifrasDeImpuestos.class), new Object [] { anio, mes, tipoPaga } );
	}

	@Override
	public CifrasDeImpuestos consultaCifrasDeImpuestosByOrdinal(Integer anio, Integer mes, Integer tipoPaga, Integer id_ordinal) {
		logger.info(QUERY_GET_CIFRAS_ISR_BY_ORDINAL);

		return jdbcTemplate.queryForObject(QUERY_GET_CIFRAS_ISR_BY_ORDINAL, 
				BeanPropertyRowMapper.newInstance(CifrasDeImpuestos.class), new Object [] { anio, mes, tipoPaga, id_ordinal } );
	}

	@Override
	public int calcula_isr_suplencia_non(Integer anio, Integer mes) {
		logger.info(STMT_CALCULA_ISR_SUPLENC_NON);

		return jdbcTemplate.update(STMT_CALCULA_ISR_SUPLENC_NON, anio, mes, anio, mes );
	}

	@Override
	public int calcula_isr_suplencia_par(Integer anio, Integer mes) {
		logger.info(STMT_CALCULA_ISR_SUPLENC_PAR);

		return jdbcTemplate.update(STMT_CALCULA_ISR_SUPLENC_PAR, anio, mes );
	}

	@Override
	public int elimina_cifras_impuesto(Integer anio, Integer mes, Integer tipoPaga) {
		logger.info(STMT_ELIMINA_CALCULO_ISR);

		return jdbcTemplate.update(STMT_ELIMINA_CALCULO_ISR, anio, mes, tipoPaga );
	}

	@Override
	public int elimina_cifras_impuesto_x_rec(Integer anio, Integer mes, Integer tipoPaga, 
			String fec_min, String fec_max) {
		logger.info(STMT_ELIMINA_CALCULO_ISR_X_REC);

		return jdbcTemplate.update(STMT_ELIMINA_CALCULO_ISR_X_REC, anio, mes, tipoPaga, fec_min, fec_max, fec_min, fec_max );

	}

	@Override
	public List<DetalleCifrasDeImpuestos> getDetalleCifrasDeImpuestos(Integer anio, Integer mes, Integer tipoPaga, Integer id_ordinal) {
		logger.info(QUERY_GET_DETALLE_CIFRAS_ISR);

		return jdbcTemplate.query(QUERY_GET_DETALLE_CIFRAS_ISR, 
				BeanPropertyRowMapper.newInstance(DetalleCifrasDeImpuestos.class), 
				new Object [] { anio, mes, tipoPaga, id_ordinal } );

	}

}