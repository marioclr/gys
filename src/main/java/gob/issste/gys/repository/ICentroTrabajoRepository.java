package gob.issste.gys.repository;

import java.util.List;

import gob.issste.gys.model.CentroTrabajo;

public interface ICentroTrabajoRepository {

    public String QUERY_ADD_NEW_CENT_TRAB 		= "INSERT INTO m4t_centros_trab (Descripcion, Componente) VALUES(?,?)";
	int save(CentroTrabajo CentroTrabajo);

	public String QUERY_UPDATE_CENT_TRAB		= "UPDATE m4t_centros_trab Set Descripcion=?, Componente=? Where IdCentroTrabajo=?";
	int update(CentroTrabajo CentroTrabajo);

	public String QUERY_FIND_CENT_TRAB_BY_ID	= "SELECT * FROM m4t_centros_trab o WHERE o.IdCentroTrabajo=?";

	public String QUERY_DELETE_CENT_TRAB_BY_ID	= "DELETE FROM m4t_centros_trab WHERE IdCentroTrabajo=?";
	CentroTrabajo findById(int id);

	int deleteById(int id);

	public String QUERY_GET_ALL_CENT_TRAB		= "SELECT * FROM m4t_centros_trab";
	List<CentroTrabajo> findAll();

	public String QUERY_FIND_CENT_TRAB_BY_DESC	= "SELECT * from m4t_centros_trab WHERE id_div_geografica=?";
	List<CentroTrabajo> findByDeleg(String clave_deleg);

}