package gob.issste.gys.repository;

import java.util.List;

import gob.issste.gys.model.TiposPresupuesto;

public interface ITiposPresupuestoRepository {

	public String QUERY_ADD_NEW_TIP_PRES      = "INSERT INTO gys_tip_presupuesto ( Clave, Descripcion ) VALUES( ?, ? )";
	int save(TiposPresupuesto tipMovPresup);

	public String QUERY_UPDATE_TIP_PRES       = "UPDATE gys_tip_presupuesto Set Clave=?, Descripcion=? Where id=?";
	int update(TiposPresupuesto tipMovPresup);

	public String QUERY_DELETE_TIP_PRES_BY_ID = "DELETE FROM gys_tip_presupuesto WHERE id=?";
	int deleteById(int id);

	public String QUERY_FIND_TIP_PRES_BY_ID   = "SELECT * FROM gys_tip_presupuesto WHERE id=?";
	TiposPresupuesto findById(int id);

	public String QUERY_FIND_TIP_PRES_BY_CLAVE = "SELECT * FROM gys_tip_presupuesto WHERE clave=?";
	TiposPresupuesto findByClave(String clave);

	public String QUERY_GET_ALL_TIP_PRES      = "SELECT * FROM gys_tip_presupuesto";
	List<TiposPresupuesto> findAll();

	public String QUERY_FIND_TIP_PRES_BY_DESC = "SELECT * from gys_tip_presupuesto WHERE Descripcion like ?";
	List<TiposPresupuesto> findByDesc(String desc);

}