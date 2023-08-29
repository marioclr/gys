package gob.issste.gys.repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.Presupuesto;
import gob.issste.gys.repository.IPresupuestoRepository;
import gob.issste.gys.repository.mappers.PresupuestoMapper;

@Repository
public class JdbcPresupuestoRepository implements IPresupuestoRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public int save(Presupuesto presupuesto) throws SQLException {
		logger.info(QUERY_ADD_PRESUPUESTO);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_PRESUPUESTO, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, presupuesto.getAnio());
            preparedStatement.setString(2, presupuesto.getDelegacion().getId_div_geografica());
            preparedStatement.setInt(3, presupuesto.getTipoPresup().getId());
            preparedStatement.setDouble(4, presupuesto.getSaldo());
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
	public int update(int idPresupuesto, Double actualizacion) {
		logger.info(QUERY_UPD_PRESUPUESTO);
		return jdbcTemplate.update(QUERY_UPD_PRESUPUESTO,
	            new Object[] { actualizacion, idPresupuesto });
	}

	@Override
	public List<Presupuesto> findAll() {
		logger.info(QUERY_GET_PRESUPUESTO);
		List<Presupuesto> presupuestos = jdbcTemplate.query(QUERY_GET_PRESUPUESTO, new PresupuestoMapper());
//		if (conPerfiles == true) {
//			for (Usuario usuario : presupuestos) {
//				List<Perfil> perfiles = getPerfilesForUsuario(usuario);
//				usuario.setPerfiles(perfiles);
//			}
//		}
		return presupuestos;
	}

	@Override
	public List<Presupuesto> findAllByUser(int idUsuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Presupuesto> findAllByUser(int idUsuario, String ct) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Presupuesto getElementById(String id) {
		logger.info(QUERY_GET_PRESUPUESTO_BY_ID);
		return jdbcTemplate.queryForObject(QUERY_GET_PRESUPUESTO_BY_ID, new PresupuestoMapper(), id );
	}

	@Override
	public Presupuesto getElementByType(String idDeleg, Integer idTipo) {
		logger.info(QUERY_GET_PRESUPUESTO_BY_TYPE);
		return jdbcTemplate.queryForObject(QUERY_GET_PRESUPUESTO_BY_TYPE, new PresupuestoMapper(), idDeleg, idTipo );
	}

	@Override
	public int existe_presupuesto(Presupuesto presupuesto) {
		logger.info(QUERY_EXISTS_PRESUPUESTO);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_PRESUPUESTO, Integer.class,
				new Object[] { presupuesto.getAnio(), presupuesto.getDelegacion().getId_div_geografica(), presupuesto.getTipoPresup().getId() } );
	}

	@Override
	public List<Presupuesto> get_dynamic_regs(String idDelegacion, String idTipoPresup) {

		String QUERY_CONDITION               = "";
		List<Object> objects = new ArrayList<Object>();

		if (idDelegacion != null) { 
			QUERY_CONDITION += "And P.idDelegacion = ?\r\n";
			objects.add(idDelegacion);
		}

		if (idTipoPresup != null) {
			QUERY_CONDITION += "And P.idTipoPresup = ?\r\n";
			objects.add(idTipoPresup);
		}

		String QUERY_GET_DYNAMIC_PRESUPUESTO = "Select P.id, P.anio, P.saldo, P.idDelegacion, D.n_div_geografica,\r\n"
											 + "P.idTipoPresup, T.clave, T.descripcion\r\n"
											 + "From gys_presupuesto P, gys_tip_presupuesto T, m4t_delegaciones D\r\n"
											 + "Where P.idTipoPresup=T.id And P.idDelegacion=D.id_div_geografica\r\n"
											 + QUERY_CONDITION
											 + "Order by idDelegacion";

		logger.info(QUERY_GET_DYNAMIC_PRESUPUESTO);
		//logger.info(objects.toString());
		List<Presupuesto> presupuestos = jdbcTemplate.query(QUERY_GET_DYNAMIC_PRESUPUESTO, new PresupuestoMapper(),	objects.toArray() );
				//idDelegacion, idTipoPresup);
		
		return presupuestos;
	}

}
