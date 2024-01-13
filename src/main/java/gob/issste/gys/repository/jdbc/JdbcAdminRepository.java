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
import gob.issste.gys.model.CifrasDeImpuestosPorRepresentacion;
import gob.issste.gys.model.DetalleCifrasDeImpuestos;
import gob.issste.gys.model.DetalleCifrasDeImpuestosConPA;
import gob.issste.gys.model.DetalleCifrasDeImpuestosPorRep;
import gob.issste.gys.model.LayoutSpep;
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
	public List<LayoutSpep> consultaLayoutSPEP(Integer anio, Integer mes, Integer tipoPaga, Integer id_ordinal) {
		logger.info(QUERY_CONSULTA_LAYOUT_SPEP_ORD);

		return jdbcTemplate.query(QUERY_CONSULTA_LAYOUT_SPEP_ORD, BeanPropertyRowMapper.newInstance(LayoutSpep.class), anio, mes, tipoPaga, id_ordinal );

	}

	@Override
	public List<LayoutSpep> consultaLayoutSPEP_X_Rep(Integer anio, Integer mes, Integer tipoPaga, Integer id_ordinal, String id_rep) {
		logger.info(QUERY_CONSULTA_LAYOUT_SPEP_REP);

		List<LayoutSpep> layout = jdbcTemplate.query(QUERY_CONSULTA_LAYOUT_SPEP_REP, BeanPropertyRowMapper.newInstance(LayoutSpep.class), anio, mes, tipoPaga, id_ordinal, id_rep );
		return layout;
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
	public int calcula_isr_par(Integer anio, Integer mes) {
		logger.info(STMT_CALCULA_ISR_PAR);

		return jdbcTemplate.update(STMT_CALCULA_ISR_PAR, anio, mes, anio, mes, anio, mes );
	}

	@Override
	public int re_calcula_isr_non(Integer anio, Integer mes, String fec_min, String fec_max) {
		logger.info(STMT_RE_CALCULA_ISR_NON);

		return jdbcTemplate.update(STMT_RE_CALCULA_ISR_NON, anio, mes, anio, mes, fec_min, fec_max, anio, mes, fec_min, fec_max );
	}

	@Override
	public int re_calcula_isr_par(Integer anio, Integer mes, String fec_min, String fec_max) {
		logger.info(STMT_RE_CALCULA_ISR_PAR);

		return jdbcTemplate.update(STMT_RE_CALCULA_ISR_PAR, anio, mes, anio, mes, fec_min, fec_max, anio, mes, fec_min, fec_max );
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
	public List<CifrasDeImpuestosPorRepresentacion> consultaCifrasDeImpuestosPorRep(Integer anio, Integer mes, Integer tipoPaga) {
		logger.info(QUERY_GET_CIFRAS_ISR_REP);

		return jdbcTemplate.query(QUERY_GET_CIFRAS_ISR_REP, BeanPropertyRowMapper.newInstance(CifrasDeImpuestosPorRepresentacion.class), new Object [] { anio, mes, tipoPaga } );
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

	@Override
	public List<DetalleCifrasDeImpuestosPorRep> getDetalleCifrasDeImpuestosXRep(Integer anio, Integer mes, Integer tipoPaga, Integer id_ordinal, String id_rep) {
		logger.info(QUERY_GET_DETALLE_CIFRAS_ISR_REP);

		return jdbcTemplate.query(QUERY_GET_DETALLE_CIFRAS_ISR_REP, 
				BeanPropertyRowMapper.newInstance(DetalleCifrasDeImpuestosPorRep.class),
				new Object [] { anio, mes, tipoPaga, id_ordinal, id_rep } );
	}

	@Override
	public List<DetalleCifrasDeImpuestosConPA> getDetalleCifrasDeImpuestosPA(Integer anio, Integer mes,
			Integer tipoPaga, Integer id_ordinal) {
		logger.info(QUERY_GET_DETALLE_CIFRAS_ISR_PA);

		return jdbcTemplate.query(QUERY_GET_DETALLE_CIFRAS_ISR_PA,
				BeanPropertyRowMapper.newInstance(DetalleCifrasDeImpuestosConPA.class), 
				new Object [] { anio, mes, tipoPaga, id_ordinal } );
	}

	@Override
	public int elimina_cifras_impuesto_x_ord(Integer anio, Integer mes, Integer tipoPaga, Integer ord) {
		logger.info(STMT_ELIMINA_CALCULO_ISR_X_ORD);

		return jdbcTemplate.update(STMT_ELIMINA_CALCULO_ISR_X_ORD, anio, mes, tipoPaga, ord );
	}

	@Override
	public int re_calcula_isr_ord_non(Integer anio, Integer mes, String fec_min, String fec_max, Integer ord) {
		logger.info(STMT_RE_CALCULA_ISR_ORD_NON.replaceFirst("###", ord.toString()));

		return jdbcTemplate.update(STMT_RE_CALCULA_ISR_ORD_NON.replaceFirst("###", ord.toString()), anio, mes, fec_min, fec_max, anio, mes, fec_min, fec_max );
	}

	@Override
	public int re_calcula_isr_ord_par(Integer anio, Integer mes, String fec_min, String fec_max, Integer ord) {
		logger.info(STMT_RE_CALCULA_ISR_ORD_PAR.replaceFirst("###", ord.toString()));

		return jdbcTemplate.update(STMT_RE_CALCULA_ISR_ORD_NON.replaceFirst("###", ord.toString()), anio, mes, fec_min, fec_max, anio, mes, fec_min, fec_max );
	}

	@Override
	public int calculaPensionAlimenticia(Integer anio, Integer mes, Integer tipoPaga, Integer id_ordinal) {
		logger.info( "execute procedure " + STMT_EXECUTE_SP_PA);

		return jdbcTemplate.update( "execute procedure " + STMT_EXECUTE_SP_PA, anio, mes, tipoPaga, id_ordinal);
		//return jdbcTemplate.update( "execute procedure mclr_sp_prueba(" + mes + ")" );

	}

}