package gob.issste.gys.repository;

import java.util.List;

import gob.issste.gys.model.PresupuestoGlobal;

public interface IPresupuestoGlobalRepository {

	public String STMT_ADD_NEW_PRES_GLOBAL			= "INSERT INTO gys_presupuesto_global ( anio, idDelegacion, saldo, comentarios, id_usuario )\r\n"
													+ "VALUES ( ?, ?, ?, ?, ? )";
	int save(PresupuestoGlobal presupuestoGlobal);

	public String STMT_UPDATE_PRES_GLOBAL			= "UPDATE gys_presupuesto_global Set saldo = ?, comentarios = ?, id_usuario = ? Where id = ?";
	int update(PresupuestoGlobal presupuestoGlobal);

	public String STMT_DELETE_PRES_GLOBAL_BY_ID		= "DELETE FROM gys_presupuesto_global WHERE id=?";
	int deleteById(int id);

	public String QUERY_FIND_PRES_GLOBAL_BY_ID		= "SELECT * FROM gys_presupuesto_global P, m4t_delegaciones D\r\n"
													+ "Where P.idDelegacion=D.id_div_geografica And id=?";
	PresupuestoGlobal findById(int id);

	public String QUERY_GET_ALL_PRES_GLOBAL			= "SELECT * FROM gys_presupuesto_global P, m4t_delegaciones D\r\n"
													+ "Where P.idDelegacion=D.id_div_geografica";
	List<PresupuestoGlobal> findAll();

	public String QUERY_FIND_PRES_GLOBAL_BY_COMEN	= "SELECT * from gys_presupuesto_global WHERE comentarios like ?";
	List<PresupuestoGlobal> findByComent(String coment);

	public String QUERY_EXISTS_PRESUPUESTO          = "Select COUNT(*) \r\n"
													+ "From gys_presupuesto_global\r\n"
													+ "Where anio = ?\r\n"
													+ "  And idDelegacion = ?";
	public int existe_presupuesto(PresupuestoGlobal presupuestoGlobal);

	public String QUERY_GET_SALDO_BY_ANIO_DEL		= "Select NVL(SUM(saldo), 0) saldo \r\n"
													+ "From gys_presupuesto_global\r\n"
													+ "Where anio = ?\r\n"
													+ "  And idDelegacion = ?";
	public double saldo_presup_global(Integer anio_ejercicio, String idDelegacion);

	public List<PresupuestoGlobal> get_dynamic_regs(String idDelegacion, Integer anio, String coment);

}