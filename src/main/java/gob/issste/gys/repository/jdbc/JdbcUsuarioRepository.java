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
import gob.issste.gys.model.DatosAdscripcion;
import gob.issste.gys.model.NivelVisibilidad;
import gob.issste.gys.model.Opcion;
import gob.issste.gys.model.Perfil;
import gob.issste.gys.model.Usuario;
import gob.issste.gys.repository.IDelegacionRepository;
import gob.issste.gys.repository.PerfilRepository;
import gob.issste.gys.repository.UsuarioRepository;
import gob.issste.gys.repository.mappers.UsuarioMapper;

@Repository
public class JdbcUsuarioRepository implements UsuarioRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	PerfilRepository perfilRepository;
	@Autowired
	IDelegacionRepository delegacionRepository;

	@Override
	public int save(Usuario usuario) throws SQLException {
		logger.info(QUERY_ADD_NEW_USU);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_ADD_NEW_USU, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, usuario.getClave());
            preparedStatement.setString(2, usuario.getPassword());
            preparedStatement.setString(3, usuario.getEmpleado().getId_empleado());
            preparedStatement.setString(4, usuario.getDelegacion().getId_div_geografica());
            preparedStatement.setInt(5, usuario.getNivelVisibilidad().getIdNivelVisibilidad());
            preparedStatement.setInt(6, usuario.getIdTipoUsuario());
            preparedStatement.setString(7, usuario.getId_usuario());
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
	public int update(Usuario usuario) {
		logger.info(QUERY_UPDATE_USU);

		return jdbcTemplate.update(QUERY_UPDATE_USU,
				new Object[] { usuario.getClave(), usuario.getPassword(), usuario.getEmpleado().getId_empleado(), usuario.getDelegacion().getId_div_geografica(), 
						usuario.getNivelVisibilidad().getIdNivelVisibilidad(), usuario.getId_usuario(), usuario.getIdUsuario() });
	}

	@Override
	public Usuario findById(int id) {
		logger.info(QUERY_FIND_USU_BY_ID);
		try {
			Usuario usuario = jdbcTemplate.queryForObject(QUERY_FIND_USU_BY_ID,
					new UsuarioMapper(), id);
			return usuario;
		} catch (IncorrectResultSizeDataAccessException e) {			
			return null;
		}
	}

	@Override
	public Usuario findByName(String userName) {
		logger.info(QUERY_FIND_USU_BY_NAME);
		try {
			Usuario usuario = jdbcTemplate.queryForObject(QUERY_FIND_USU_BY_NAME,
					new UsuarioMapper(), userName);
			return usuario;
		} catch (IncorrectResultSizeDataAccessException e) {			
			return null;
		}
	}

	@Override
	public int deleteById(int id) {
		logger.info(QUERY_DELETE_USU_BY_ID);
		return jdbcTemplate.update(QUERY_DELETE_USU_BY_ID, id);
	}

	@Override
	public List<Usuario> findAll(boolean conPerfiles) {
		
		logger.info(QUERY_GET_ALL_USU);
		List<Usuario> usuarios = jdbcTemplate.query(QUERY_GET_ALL_USU, new UsuarioMapper());
		if (conPerfiles == true) {
			for (Usuario usuario : usuarios) {
				List<Perfil> perfiles = getPerfilesForUsuario(usuario);
				//usuario.setPerfiles(perfiles);
				for (Perfil perfil : perfiles) {
					List<Opcion> opciones = perfilRepository.getOpcionesForPerfil(perfil);
					for (Opcion opcion : opciones) {
						int idNivelAcceso = getPermissionsForUserOption(usuario.getIdUsuario(), perfil.getIdPerfil(), opcion.getIdOpcion());
						opcion.setIdNivelAcceso(idNivelAcceso);
						perfil.setOpciones(opciones);
					}
				}
				usuario.setPerfiles(perfiles);
			}
		}
		return usuarios;
	}

//	@Override
//	public List<Usuario> findAll() {
//		logger.info(QUERY_GET_ALL_USU);
//		//return jdbcTemplate.query(QUERY_GET_ALL_USU, BeanPropertyRowMapper.newInstance(Usuario.class));
//		return jdbcTemplate.query(QUERY_GET_ALL_USU, new UsuarioMapper());
//	}

	@Override
	public List<Usuario> findByClave(String clave, boolean conPerfiles) {
		logger.info(QUERY_FIND_USU_BY_CLAVE);
		List<Usuario> usuarios = jdbcTemplate.query(QUERY_FIND_USU_BY_CLAVE, new UsuarioMapper(), "%" + clave + "%");
		if (conPerfiles == true) {
			for (Usuario usuario : usuarios) {
				List<Perfil> perfiles = getPerfilesForUsuario(usuario);
				usuario.setPerfiles(perfiles);
			}
		}
		return usuarios;
	}

//	@Override
//	public List<Usuario> findByClave(String clave, boolean conPerfiles) {
//		logger.info(QUERY_FIND_USU_BY_CLAVE);
//		return jdbcTemplate.query(QUERY_FIND_USU_BY_CLAVE, 
//				new UsuarioMapper(), "%" + clave + "%");
//	}

	@Override
	public List<Perfil> getPerfilesForUsuario(Usuario usuario) {
		logger.info(QUERY_GET_PER_FOR_USU);

		List<Perfil> perfilesList =
				this.jdbcTemplate.query(QUERY_GET_PER_FOR_USU,
				BeanPropertyRowMapper.newInstance(Perfil.class), usuario.getIdUsuario());

		return perfilesList;
	}

	@Override
	public Usuario loginUser(String username, String password) {
		logger.info(QUERY_LOGIN_USU);

		try {
			Usuario usuario = jdbcTemplate.queryForObject(QUERY_LOGIN_USU,
					new UsuarioMapper(), username, password);
			return usuario;
		} catch (IncorrectResultSizeDataAccessException e) {			
			return null;
		}
	}

	@Override
	public int savePermissions(int IdUsuario, int IdPerfil, int IdOpcion, int IdNivelAcceso) throws SQLException {
		logger.info(QUERY_ADD_USU_PERMISSIONS);

		try {
			return jdbcTemplate.update(QUERY_ADD_USU_PERMISSIONS,
					new Object[] { IdUsuario, IdPerfil, IdOpcion, IdNivelAcceso });
			
		} catch (Exception e) {
			throw new SQLException("Expected one row insert.");
		}
	}

	@Override
	public Usuario getPermissionsForUser(Usuario usuario) {

		if (usuario != null) {
			List<Perfil> perfiles = this.getPerfilesForUsuario(usuario);
			for (Perfil perfil : perfiles) {
				List<Opcion> opciones = perfilRepository.getOpcionesForPerfil(perfil);
				for (Opcion opcion : opciones) {
					int idNivelAcceso = getPermissionsForUserOption(usuario.getIdUsuario(), perfil.getIdPerfil(), opcion.getIdOpcion());
					opcion.setIdNivelAcceso(idNivelAcceso);
					perfil.setOpciones(opciones);
				}
			}
			usuario.setPerfiles(perfiles);
			//usuario.getDelegacion().setCentrosTrabajo(delegacionRepository.getCentrosTrabForDeleg(usuario.getDelegacion())); // A solicitud de JAM
		}
		return usuario;
	}

	@Override
	public int getPermissionsForUserOption(int IdUsuario, int IdPerfil, int IdOpcion) {
		int nivelAcceso = 0;
		Object[] inputs = new Object[] {IdUsuario, IdPerfil, IdOpcion};
		try {
			nivelAcceso = jdbcTemplate.queryForObject(QUERY_GET_USU_OPT_PERMIS, Integer.class, inputs);
		} catch (Exception e) {
			nivelAcceso = 0;
		}
		return nivelAcceso;
	}

	@Override
	public int removePermissions(int id) {
		logger.info(QUERY_DEL_USU_PERMISSIONS);
		return jdbcTemplate.update(QUERY_DEL_USU_PERMISSIONS, id);
	}

	@Override
	public int saveCentTrabForUsu(int IdUsuario, String IdCentroTrab, String id_usuario)
			throws SQLException {
		logger.info(QUERY_ADD_USU_CTS);

		try {
			return jdbcTemplate.update(QUERY_ADD_USU_CTS,
					new Object[] { IdUsuario, IdCentroTrab, id_usuario });
			
		} catch (Exception e) {
			throw new SQLException("Error al insertar centro de trabajo para el usuario.");
		}
	}

	@Override
	public List<DatosAdscripcion> getCentTrabForUsu(int IdUsuario) {
		logger.info(QUERY_GET_USU_CTS);

		List<DatosAdscripcion> centrosTrabList =
				this.jdbcTemplate.query(QUERY_GET_USU_CTS,
				BeanPropertyRowMapper.newInstance(DatosAdscripcion.class), IdUsuario);

		return centrosTrabList;
	}

	@Override
	public List<NivelVisibilidad> getNivelVisibilidadUsuarios() {
		logger.info(QUERY_GET_NIVEL_VIS_USU);

		List<NivelVisibilidad> nivelVisibilidadList =
				this.jdbcTemplate.query(QUERY_GET_NIVEL_VIS_USU,
				BeanPropertyRowMapper.newInstance(NivelVisibilidad.class));

		return nivelVisibilidadList;
	}

}
