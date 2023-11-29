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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
            preparedStatement.setInt(2, presupuesto.getMes());
            preparedStatement.setString(3, presupuesto.getDelegacion().getId_div_geografica());
            preparedStatement.setInt(4, presupuesto.getTipoPresup().getId());
            preparedStatement.setDouble(5, presupuesto.getSaldo());
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
	public int save_ct(Presupuesto presupuesto) throws SQLException {
		logger.info(QUERY_ADD_PRESUPUESTO_CT);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_PRESUPUESTO_CT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, presupuesto.getAnio());
            preparedStatement.setInt(2, presupuesto.getMes());
            preparedStatement.setString(3, presupuesto.getDelegacion().getId_div_geografica());
            preparedStatement.setString(4, presupuesto.getCentroTrabajo().getClave());
            preparedStatement.setInt(5, presupuesto.getTipoPresup().getId());
            preparedStatement.setDouble(6, presupuesto.getSaldo());
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
	public Presupuesto getElementById(int id) {
		logger.info(QUERY_GET_PRESUPUESTO_BY_ID);
		return jdbcTemplate.queryForObject(QUERY_GET_PRESUPUESTO_BY_ID, new PresupuestoMapper(), id );
	}

	@Override
	public Presupuesto getElementByType(String idDeleg, Integer idTipo) {
		logger.info(QUERY_GET_PRESUPUESTO_BY_TYPE);
		return jdbcTemplate.queryForObject(QUERY_GET_PRESUPUESTO_BY_TYPE, new PresupuestoMapper(), idDeleg, idTipo );
	}

	@Override
	public Presupuesto getElementByType_ct(String idCentroTrab, String idTipo, Integer anio, Integer mes) {
		logger.info(QUERY_GET_PRESUPUESTO_BY_TYPE_CT);
		return jdbcTemplate.queryForObject(QUERY_GET_PRESUPUESTO_BY_TYPE_CT, new PresupuestoMapper(), idCentroTrab, idTipo, anio, mes);
	}

	@Override
	public int existe_presupuesto(Presupuesto presupuesto) {
		logger.info(QUERY_EXISTS_PRESUPUESTO);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_PRESUPUESTO, Integer.class,
				new Object[] { presupuesto.getAnio(), presupuesto.getMes(), presupuesto.getDelegacion().getId_div_geografica(), presupuesto.getTipoPresup().getId() } );
	}

	@Override
	public int existe_presupuesto_ct(Presupuesto presupuesto) {
		logger.info(QUERY_EXISTS_PRESUPUESTO_CT);
		return jdbcTemplate.queryForObject(QUERY_EXISTS_PRESUPUESTO_CT, Integer.class,
				new Object[] { presupuesto.getAnio(), presupuesto.getMes(), presupuesto.getDelegacion().getId_div_geografica(), presupuesto.getCentroTrabajo().getClave(), presupuesto.getTipoPresup().getId() } );
	}

	@Override
	public double validaSumaPresupuestal(Presupuesto presupuesto) {
		logger.info(QUERY_SUMA_PRESUPUESTO_CT);
		return jdbcTemplate.queryForObject(QUERY_SUMA_PRESUPUESTO_CT, Double.class,
				new Object[] { presupuesto.getSaldo(), presupuesto.getAnio(), presupuesto.getMes(), presupuesto.getDelegacion().getId_div_geografica(), presupuesto.getTipoPresup().getId() } );
	}

	@Override
	public List<Presupuesto> get_dynamic_regs(String idDelegacion, String claveTipoPresup, Integer anio, Integer mes, String idCentTrab, boolean solo_deleg) {

		String QUERY_CONDITION = ""; // , ADSC_TABLE = "", ADSC_FIELDS = "", ADSC_WHERE = "";
		List<Object> objects = new ArrayList<Object>();

		if (idDelegacion != null) {
			QUERY_CONDITION += "And P.idDelegacion = ?\r\n";
			objects.add(idDelegacion);
		}

		if (claveTipoPresup != null) {
			QUERY_CONDITION += "And T.clave = ?\r\n";
			objects.add(claveTipoPresup);
		}

		if (anio != null) {
			QUERY_CONDITION += "And P.anio = ?\r\n";
			objects.add(anio);
		}

		if (mes != null) {
			QUERY_CONDITION += "And P.mes = ?\r\n";
			objects.add(mes);
		}

		if (idCentTrab != null) {
			QUERY_CONDITION += "And P.id_centro_trabajo = ?\r\n";
			//ADSC_FIELDS = "C.id_centro_trabajo Clave, n_centro_trabajo Descripcion, id_tipo_ct Tipo, id_zona Zona,\r\n";
			//ADSC_TABLE = ", m4t_centros_trab C";
			//ADSC_WHERE = " And P.id_centro_trabajo = C.id_centro_trabajo";
			objects.add(idCentTrab);
		}

		if (solo_deleg) {
			QUERY_CONDITION += "And P.id_centro_trabajo Is NULL\r\n";
		} else {
			//QUERY_CONDITION += "And P.id_centro_trabajo Is Not NULL\r\n";
		}

		String QUERY_GET_DYNAMIC_PRESUPUESTO = "Select P.id, P.anio, P.mes, P.saldo, P.idDelegacion, D.n_div_geografica, NVL(P.id_centro_trabajo, '00000') id_centro_trabajo,\r\n"
				 + "P.idTipoPresup, T.clave clave_tipo_presup, T.descripcion descripcion_tipo_presup,\r\n"
				 + "NVL(C.id_centro_trabajo, '') Clave, NVL(n_centro_trabajo, '') Descripcion, NVL(id_tipo_ct, '') Tipo, NVL(id_zona, '') Zona\r\n"
				 + "From gys_presupuesto P Left Join m4t_centros_trab C ON P.id_centro_trabajo = C.id_centro_trabajo, gys_tip_presupuesto T, m4t_delegaciones D\r\n"
				 + "Where P.idTipoPresup=T.id And P.idDelegacion=D.id_div_geografica\r\n"
				 + QUERY_CONDITION
				 + "Order by idDelegacion";

		logger.info(QUERY_GET_DYNAMIC_PRESUPUESTO);
		List<Presupuesto> presupuestos = jdbcTemplate.query(QUERY_GET_DYNAMIC_PRESUPUESTO, new PresupuestoMapper(),	objects.toArray());

		return presupuestos;
	}

	@Override
	public Presupuesto getDatosPresup(String idDelegacion, String idTipoPresup, Integer anio, Integer mes) {
		logger.info(QUERY_GET_DATOS_PRESUP);
		Presupuesto presup = jdbcTemplate.queryForObject(QUERY_GET_DATOS_PRESUP, BeanPropertyRowMapper.newInstance(Presupuesto.class), new Object [] { idDelegacion, idTipoPresup, anio, mes } );
		return presup;
	}

	@Override
	public Presupuesto getDatosPresupCt(String idDelegacion, String idTipoPresup, Integer anio, Integer mes,
			String idCentTrab) {
		logger.info(QUERY_GET_DATOS_PRESUP_CT);
		Presupuesto presup = jdbcTemplate.queryForObject(QUERY_GET_DATOS_PRESUP_CT, BeanPropertyRowMapper.newInstance(Presupuesto.class), new Object [] { idDelegacion, idTipoPresup, anio, mes, idCentTrab } );
		return presup;
	}

	@Override
	public double getSaldoDelegCt(String idDelegacion, String claveTipoPresup, Integer anio, Integer mes, String idCentTrab) {

		String QUERY_CONDITION = "";
		List<Object> objects = new ArrayList<Object>();

		if (idDelegacion != null) {
			QUERY_CONDITION += "And P.idDelegacion = ?\r\n";
			objects.add(idDelegacion);
		}
		if (claveTipoPresup != null) {
			QUERY_CONDITION += "And T.clave = ?\r\n";
			objects.add(claveTipoPresup);
		}
		if (anio != null) {
			QUERY_CONDITION += "And P.anio = ?\r\n";
			objects.add(anio);
		}
		if (mes != null) {
			QUERY_CONDITION += "And P.mes = ?\r\n";
			objects.add(mes);
		}
		if (idCentTrab != null) {
			QUERY_CONDITION += "And P.id_centro_trabajo = ?\r\n";
			objects.add(idCentTrab);
		}

		String QUERY_GET_PRESUPUESTO_DEL_CT = "Select NVL( SUM(P.saldo), 0.0) saldo \r\n"
											+ "From gys_presupuesto P, gys_tip_presupuesto T, m4t_delegaciones D\r\n"
											+ "Where P.idTipoPresup=T.id And P.idDelegacion=D.id_div_geografica\r\n"
											+ QUERY_CONDITION;

		logger.info(QUERY_GET_PRESUPUESTO_DEL_CT);
		double saldo = jdbcTemplate.queryForObject(QUERY_GET_PRESUPUESTO_DEL_CT, Double.class, objects.toArray() );

		return saldo;
	}

	@Override
	public double getSaldoDistribuido(Integer anio, Integer mes, String idDelegacion, String idTipoPresup) {
		logger.info(QUERY_GET_SALDO_DISTRIBUIDO);
		
		try {
			double saldo = jdbcTemplate.queryForObject(QUERY_GET_SALDO_DISTRIBUIDO, Double.class,
					new Object[] { anio, mes, idDelegacion, idTipoPresup } );
			return saldo;
		} catch (EmptyResultDataAccessException e) {
			return 0.0;
		}
	}

	@Override
	public double suma_presupuestos(Integer anio_ejercicio, String idDelegacion) {
		logger.info(QUERY_GET_SUM_PRESUP_BY_DEL);
		return jdbcTemplate.queryForObject(QUERY_GET_SUM_PRESUP_BY_DEL, Double.class,
				new Object[] { anio_ejercicio, idDelegacion } );
	}

}