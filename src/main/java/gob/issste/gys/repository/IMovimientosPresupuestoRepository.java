package gob.issste.gys.repository;

import java.sql.SQLException;
import java.util.List;

import gob.issste.gys.model.MovimientosPresupuesto;

public interface IMovimientosPresupuestoRepository {

	public String QUERY_ADD_NEW_MOV_PRES          = "INSERT INTO gys_presupuesto_movs ( idPresupuesto, importe, comentarios, idTipoMovPresup ) VALUES( ?, ?, ?, ? )";
	int save(MovimientosPresupuesto movimPresup) throws SQLException;

	public String QUERY_UPDATE_MOV_PRES           = "UPDATE gys_presupuesto_movs Set comentarios=? Where id=?";
	int update(MovimientosPresupuesto movimPresup);

	public String QUERY_FIND_MOV_PRES_BY_ID       = "SELECT PM.id, idpresupuesto, PM.importe, PM.idtipomovpresup, PM.comentarios, PM.id_usuario, PM.fec_ult_actualizacion, TMP.clave, TMP.descripcion\r\n"
												  + "FROM gys_presupuesto_movs PM, gys_presupuesto_tip_movs TMP\r\n"
												  + "Where PM.idTipoMovPresup=TMP.id And PM.id=?";
	MovimientosPresupuesto findById(int id);

	public String QUERY_GET_ALL_MOV_PRES          = "SELECT PM.id, idpresupuesto, PM.importe, PM.idtipomovpresup, PM.comentarios, PM.id_usuario, PM.fec_ult_actualizacion, TMP.clave, TMP.descripcion\r\n"
												  + "FROM gys_presupuesto_movs PM, gys_presupuesto_tip_movs TMP\r\n"
												  + "Where PM.idTipoMovPresup=TMP.id";
	List<MovimientosPresupuesto> findAll();

	public String QUERY_FIND_MOV_PRES_BY_PRESUP   = "SELECT PM.id, idpresupuesto, PM.importe, PM.idtipomovpresup, PM.comentarios, PM.id_usuario, PM.fec_ult_actualizacion, TMP.clave, TMP.descripcion\r\n"
												  + "FROM gys_presupuesto_movs PM, gys_presupuesto_tip_movs TMP\r\n"
												  + "Where PM.idTipoMovPresup=TMP.id And PM.idPresupuesto=?\r\n"
												  + "Order By PM.id";
	List<MovimientosPresupuesto> findByPresupuesto(int tipo);

	public String QUERY_FIND_MOV_PRES_BY_TIPO     = "SELECT PM.id, idpresupuesto, PM.importe, PM.idtipomovpresup, PM.comentarios, PM.id_usuario, PM.fec_ult_actualizacion, TMP.clave, TMP.descripcion\r\n"
												  + "FROM gys_presupuesto_movs PM, gys_presupuesto_tip_movs TMP\r\n"
												  + "Where PM.idTipoMovPresup=TMP.id And TPM.idTipoMovPresup=?";
	List<MovimientosPresupuesto> findByTipo(int tipo);

}