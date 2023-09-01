package gob.issste.gys.repository;

import java.sql.SQLException;
import java.util.List;

import gob.issste.gys.model.DatosAdscripcion;
import gob.issste.gys.model.NivelVisibilidad;
import gob.issste.gys.model.Perfil;
import gob.issste.gys.model.Usuario;

public interface UsuarioRepository {

	public String QUERY_ADD_NEW_USU         = "INSERT INTO gys_Usuarios (Clave, Password, IdEmpleado, IdDelegacion, IdNivelVisibilidad, IdTipoUsuario, id_usuario) VALUES ( ?, ?, ?, ?, ?, ?, ? )";
	int save(Usuario usuario) throws SQLException;

	public String QUERY_UPDATE_USU          = "UPDATE gys_Usuarios Set Clave=?, Password=?, IdEmpleado=?, IdDelegacion=?, IdNivelVisibilidad=?, IdTipoUsuario=?, id_usuario=? Where IdUsuario=?";
	int update(Usuario usuario);

	public String QUERY_FIND_USU_BY_ID      = "SELECT u.*, e.id_sociedad, e.id_empleado, e.nombre, e.apellido_1, e.apellido_2, e.id_legal,\r\n"
											+ "  d.id_div_geografica, n_div_geografica, n.Descripcion\r\n"
											+ "FROM gys_Usuarios u, m4t_empleados e, m4t_delegaciones d, gys_NivelVisibilidad n\r\n"
											+ "Where u.IdEmpleado=e.id_empleado And u.IdDelegacion=d.id_div_geografica\r\n"
											+ "  And u.IdNivelVisibilidad=n.IdNivelVisibilidad And u.IdUsuario=?";
	Usuario findById(int id);

	public String QUERY_FIND_USU_BY_NAME    = "SELECT u.*, e.id_sociedad, e.id_empleado, e.nombre, e.apellido_1, e.apellido_2, e.id_legal,\r\n"
											+ "d.id_div_geografica, n_div_geografica, n.Descripcion\r\n"
											+ "FROM gys_Usuarios u, m4t_empleados e, m4t_delegaciones d, gys_NivelVisibilidad n\r\n"
											+ "Where u.IdEmpleado=e.id_empleado And u.IdDelegacion=d.id_div_geografica"
											+ "  And u.IdNivelVisibilidad=n.IdNivelVisibilidad And u.Clave = ?";
	Usuario findByName(String userName);

	public String QUERY_DELETE_USU_BY_ID    = "DELETE FROM gys_Usuarios WHERE IdUsuario=?";
	int deleteById(int id);

    public String QUERY_GET_ALL_USU         = "SELECT u.*, e.id_sociedad, e.id_empleado, e.nombre, e.apellido_1, e.apellido_2, e.id_legal,\r\n"
    										+ "d.id_div_geografica, n_div_geografica, n.Descripcion\r\n"
    										+ "FROM gys_Usuarios u, m4t_empleados e, m4t_delegaciones d, gys_NivelVisibilidad n\r\n"
    										+ "Where u.IdEmpleado=e.id_empleado And u.IdDelegacion=d.id_div_geografica\r\n"
    										+ "  And u.IdNivelVisibilidad=n.IdNivelVisibilidad";
    List<Usuario> findAll(boolean conPerfiles);

    public String QUERY_FIND_USU_BY_CLAVE   = "SELECT u.*, e.id_sociedad, e.id_empleado, e.nombre, e.apellido_1, e.apellido_2, e.id_legal,\r\n"
    										+ "d.id_div_geografica, n_div_geografica, n.Descripcion\r\n"
    										+ "FROM gys_Usuarios u, m4t_empleados e, m4t_delegaciones d, gys_NivelVisibilidad n\r\n"
    										+ "Where u.IdEmpleado=e.id_empleado And u.IdDelegacion=d.id_div_geografica"
    										+ "  And u.IdNivelVisibilidad=n.IdNivelVisibilidad And Clave LIKE ?";
    List<Usuario> findByClave(String desc, boolean conPerfiles);

    public String QUERY_GET_PER_FOR_USU     = "SELECT P.IdPerfil, P.Descripcion, PU.IdUsuario, P.id_usuario \r\n"
								    		+ "FROM gys_PerfilesPorUsuario PU\r\n"
								    		+ "INNER JOIN gys_Perfiles P\r\n"
								    		+ "ON PU.IdPerfil = P.IdPerfil\r\n"
								    		+ "  And PU.IdUsuario=?";
    List<Perfil> getPerfilesForUsuario(Usuario usuario);

    public String QUERY_LOGIN_USU           = "SELECT u.*, e.id_sociedad, e.id_empleado, e.nombre, e.apellido_1, e.apellido_2, e.id_legal,\r\n"
    										+ "d.id_div_geografica, n_div_geografica, n.Descripcion\r\n"
    										+ "FROM gys_Usuarios u, m4t_empleados e, m4t_delegaciones d, gys_NivelVisibilidad n\r\n"
    										+ "Where u.IdEmpleado=e.id_empleado And u.IdDelegacion=d.id_div_geografica"
    										+ "  And u.IdNivelVisibilidad=n.IdNivelVisibilidad And Clave=? And Password=?";
    Usuario loginUser(String username, String password);

    public String QUERY_ADD_USU_PERMISSIONS = "INSERT INTO gys_PermisosUsuarios (IdUsuario, IdPerfil, IdOpcion, IdNivelAcceso) VALUES (?, ?, ?, ?)";
    int savePermissions(int IdUsuario, int IdPerfil, int IdOpcion, int IdNivelAcceso) throws SQLException;

    Usuario getPermissionsForUser(Usuario usuario); // Sin query

    public String QUERY_GET_USU_OPT_PERMIS  = "SELECT IdNivelAcceso\r\n"
											+ "FROM gys_PermisosUsuarios Where IdUsuario=? And IdPerfil=? And IdOpcion=?";
    int getPermissionsForUserOption(int IdUsuario, int IdPerfil, int IdOpcion);

    public String QUERY_DEL_USU_PERMISSIONS = "Delete gys_PermisosUsuarios Where IdUsuario=?";
    int removePermissions(int id);

    public String QUERY_ADD_USU_CTS 		= "Insert Into gys_Usuarios_Centros_Trab ( IdUsuario, IdCentroTrab, id_usuario ) Values ( ?, ?, ? )";
    int saveCentTrabForUsu(int IdUsuario, String IdCentroTrab, String id_usuario) throws SQLException;

    public String QUERY_GET_USU_CTS			= "SELECT id_centro_trabajo Clave, n_centro_trabajo Descripcion, id_tipo_ct Tipo, id_zona Zona\r\n"
											+ "FROM gys_Usuarios_Centros_Trab U, m4t_centros_trab C\r\n"
											+ "Where U.IdCentroTrab = C.id_centro_trabajo And U.IdUsuario = ?\r\n";
    List<DatosAdscripcion> getCentTrabForUsu(int IdUsuario);

    public String QUERY_DEL_USU_CTS         = "Delete gys_Usuarios_Centros_Trab Where IdUsuario=?";
    int removeCentTrabForUsu(int IdUsuario);

    public String QUERY_GET_NIVEL_VIS_USU	= "SELECT IdNivelVisibilidad, Descripcion\r\n"
											+ "FROM gys_NivelVisibilidad";
    List<NivelVisibilidad> getNivelVisibilidadUsuarios();

}
