package gob.issste.gys.repository;

import java.util.List;

import gob.issste.gys.model.TipoMovPresupuesto;

public interface ITipoMovPresupuestoRepository {

	public String QUERY_ADD_NEW_TIP_MOV_PRES      = "INSERT INTO gys_presupuesto_tip_movs ( Clave, Descripcion ) VALUES( ?, ? )";
	int save(TipoMovPresupuesto tipMovPresup);

	public String QUERY_UPDATE_TIP_MOV_PRES       = "UPDATE gys_presupuesto_tip_movs Set Clave=?, Descripcion=? Where id=?";
	int update(TipoMovPresupuesto tipMovPresup);

	public String QUERY_DELETE_TIP_MOV_PRES_BY_ID = "DELETE FROM gys_presupuesto_tip_movs WHERE id=?";
	int deleteById(int id);

	public String QUERY_FIND_TIP_MOV_PRES_BY_ID   = "SELECT * FROM gys_presupuesto_tip_movs WHERE id=?";
	TipoMovPresupuesto findById(int id);

	public String QUERY_GET_ALL_TIP_MOV_PRES      = "SELECT * FROM gys_presupuesto_tip_movs";
	List<TipoMovPresupuesto> findAll();

	public String QUERY_FIND_TIP_MOV_PRES_BY_DESC = "SELECT * from gys_presupuesto_tip_movs WHERE Descripcion like ?";
	List<TipoMovPresupuesto> findByDesc(String desc);

}