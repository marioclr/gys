package gob.issste.gys.repository;

import java.util.List;

import gob.issste.gys.model.Opcion;

public interface OpcionRepository {

	public String QUERY_ADD_NEW_OPT      = "INSERT INTO gys_Opciones (Descripcion, Componente, id_usuario) VALUES(?,?,?)";
	int save(Opcion opcion);

	public String QUERY_UPDATE_OPT       = "UPDATE gys_Opciones Set Descripcion=?, Componente=?, id_usuario=? Where IdOpcion=?";
	int update(Opcion opcion);

	public String QUERY_FIND_OPT_BY_ID   = "SELECT * FROM gys_Opciones o WHERE o.IdOpcion=?";
	Opcion findById(int id);

	public String QUERY_GET_ALL_OPT      = "SELECT * FROM gys_Opciones";
	List<Opcion> findAll();

	public String QUERY_DELETE_OPT_BY_ID = "DELETE FROM gys_Opciones WHERE IdOpcion=?";
	int deleteById(int id);

	public String QUERY_FIND_OPT_BY_DESC = "SELECT * from gys_Opciones WHERE Descripcion LIKE ?";
	List<Opcion> findByDesc(String desc);

}