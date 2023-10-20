package gob.issste.gys.repository.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.PresupuestoGlobal;
import gob.issste.gys.repository.IPresupuestoGlobalRepository;
import gob.issste.gys.repository.mappers.PresupuestoGlobalMapper;

@Repository
public class JdbcPresupuestoGlobalRepository implements IPresupuestoGlobalRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate  jdbcTemplate;

	@Override
	public int save(PresupuestoGlobal presupGlobal) {
		logger.info(STMT_ADD_NEW_PRES_GLOBAL);
	    return jdbcTemplate.update(STMT_ADD_NEW_PRES_GLOBAL,
	            new Object[] { presupGlobal.getAnio(), presupGlobal.getDelegacion().getId_div_geografica(), 
	            		presupGlobal.getSaldo(), presupGlobal.getComentarios(), presupGlobal.getId_usuario() });
	}

	@Override
	public int update(PresupuestoGlobal presupGlobal) {
		logger.info(STMT_UPDATE_PRES_GLOBAL);
		return jdbcTemplate.update(STMT_UPDATE_PRES_GLOBAL,
	            new Object[] { presupGlobal.getSaldo(), presupGlobal.getComentarios(), presupGlobal.getId_usuario(), presupGlobal.getId() });
	}

	@Override
	public int deleteById(int id) {
		logger.info(STMT_DELETE_PRES_GLOBAL_BY_ID);
		return jdbcTemplate.update(STMT_DELETE_PRES_GLOBAL_BY_ID, id);
	}

	@Override
	public PresupuestoGlobal findById(int id) {
		logger.info(QUERY_FIND_PRES_GLOBAL_BY_ID);
		try {
			PresupuestoGlobal tipo = jdbcTemplate.queryForObject(QUERY_FIND_PRES_GLOBAL_BY_ID,
			          new PresupuestoGlobalMapper(), id);
	      return tipo;
	    } catch (IncorrectResultSizeDataAccessException e) {
	      return null;
	    }
	}

	@Override
	public List<PresupuestoGlobal> findAll() {
		logger.info(QUERY_GET_ALL_PRES_GLOBAL);
		return jdbcTemplate.query(QUERY_GET_ALL_PRES_GLOBAL, new PresupuestoGlobalMapper());
	}

	@Override
	public List<PresupuestoGlobal> findByComent(String coment) {
		logger.info(QUERY_FIND_PRES_GLOBAL_BY_COMEN);
		return jdbcTemplate.query(QUERY_FIND_PRES_GLOBAL_BY_COMEN, new PresupuestoGlobalMapper(), "%" + coment + "%");
	}

	@Override
	public int existe_presupuesto(PresupuestoGlobal presupuestoGlobal) {
		logger.info(QUERY_EXISTS_PRESUPUESTO);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_PRESUPUESTO, Integer.class,
				new Object[] { presupuestoGlobal.getAnio(), presupuestoGlobal.getDelegacion().getId_div_geografica() } );
	}

	@Override
	public double saldo_presup_global(Integer anio_ejercicio, String idDelegacion) {
		logger.info(QUERY_GET_SALDO_BY_ANIO_DEL);
		return jdbcTemplate.queryForObject(QUERY_GET_SALDO_BY_ANIO_DEL, Double.class,
				new Object[] { anio_ejercicio, idDelegacion } );
	}

	@Override
	public List<PresupuestoGlobal> get_dynamic_regs(String idDelegacion, Integer anio, String coment) {
		String QUERY_CONDITION = "";
		List<Object> objects = new ArrayList<Object>();

		if (idDelegacion != null) {
			QUERY_CONDITION += "And P.idDelegacion = ?\r\n";
			objects.add(idDelegacion);
		}

		if (anio != null) {
			QUERY_CONDITION += "And P.anio = ?\r\n";
			objects.add(anio);
		}

		if (coment != null) {
			QUERY_CONDITION += "And comentarios like ?\r\n";
			objects.add("%" + coment + "%");
		}

		String QUERY_GET_DYNAMIC_PRESUP_GLOBAL  = "SELECT * FROM gys_presupuesto_global P, m4t_delegaciones D\r\n"
												+ "Where P.idDelegacion=D.id_div_geografica\r\n"
												+ QUERY_CONDITION
												+ "Order by idDelegacion";

		logger.info(QUERY_GET_DYNAMIC_PRESUP_GLOBAL);
		List<PresupuestoGlobal> presupuestos = jdbcTemplate.query(QUERY_GET_DYNAMIC_PRESUP_GLOBAL, new PresupuestoGlobalMapper(),	objects.toArray());

		return presupuestos;
	}

}