package gob.issste.gys.repository;

import java.sql.SQLException;
import java.util.List;

import gob.issste.gys.model.Opcion;
import gob.issste.gys.model.Perfil;

public interface PerfilRepository {

	public String QUERY_ADD_NEW_PER      = "INSERT INTO gys_Perfiles (Descripcion, id_usuario) VALUES(?, ?)";
	int save(Perfil perfil) throws SQLException;

	public String QUERY_UPDATE_PER       = "UPDATE gys_Perfiles Set Descripcion=?, id_usuario=? Where IdPerfil=?";
	int update(Perfil perfil);

	public String QUERY_FIND_PER_BY_ID   = "SELECT * FROM gys_Perfiles o WHERE o.IdPerfil=?";
	Perfil findById(int id);

	public String QUERY_DELETE_PER_BY_ID = "DELETE FROM gys_Perfiles WHERE IdPerfil=?";
	int deleteById(int id);

    public String QUERY_GET_ALL_PER      = "SELECT * FROM gys_Perfiles";
    List<Perfil> findAll();

    public String QUERY_FIND_PER_BY_DESC = "SELECT * from gys_Perfiles WHERE Descripcion LIKE ?";
    List<Perfil> findByDesc(String desc);

    public String QUERY_ADD_OPT_TO_PERF  = "INSERT INTO gys_OpcionesPorPerfil (IdPerfil, IdOpcion) VALUES(?, ?)";
    int addOpcionToPerfil(int idUsuario, int idPerfil);

    public String QUERY_DEL_OPT_TO_PERF  = "Delete From gys_OpcionesPorPerfil Where IdPerfil = ?";
    int deleteOptToPerfil(int idPerfil);

    public String QUERY_GET_OPC_FOR_PER  = "SELECT OP.IdOpcion, O.Descripcion, O.Componente, O.id_usuario\r\n"
							    		 + "FROM gys_OpcionesPorPerfil OP\r\n"
							    		 + "INNER JOIN gys_Opciones O\r\n"
							    		 + "ON OP.IdOpcion = O.IdOpcion\r\n"
							    		 + "  AND OP.IdPerfil=?";
    List<Opcion> getOpcionesForPerfil(Perfil perfil);

    public String QUERY_ADD_PER_TO_USER  = "INSERT INTO gys_PerfilesPorUsuario (IdUsuario, IdPerfil) VALUES(?, ?)";
    int addPerfilToUser(int idUsuario, int idPerfil);

    public String QUERY_DEL_PER_TO_USER  = "DELETE FROM gys_PerfilesPorUsuario Where IdUsuario = ?"; // Este se hace en la modificación de usuario
	int removePerfilesToUser(int idUsuario);

}
