package gob.issste.gys.repository;

import java.sql.SQLException;
import java.util.List;

import gob.issste.gys.model.Presupuesto;

public interface IPresupuestoRepository {

	public String QUERY_ADD_PRESUPUESTO			    = "INSERT INTO gys_presupuesto (anio, idDelegacion, idTipoPresup, saldo)\r\n"
													+ "Values ( ?, ?, ?, ? )";
	int save(Presupuesto presupuesto) throws SQLException;

	public String QUERY_UPD_PRESUPUESTO			    = "Update gys_presupuesto\r\n"
													+ "Set saldo=saldo+?\r\n"
													+ "Where id = ?";
	int update(int idPresupuesto, Double actualizacion);

	public String QUERY_GET_PRESUPUESTO		    	= "Select P.id, P.anio, P.saldo, P.idDelegacion, D.n_div_geografica,\r\n"
													+ "NVL(P.id_centro_trabajo, '00000') id_centro_trabajo, P.idTipoPresup, T.clave, T.descripcion\r\n"
													+ "From gys_presupuesto P, gys_tip_presupuesto T, m4t_delegaciones D\r\n"
													+ "Where P.idTipoPresup=T.id And P.idDelegacion=D.id_div_geografica\r\n"
													+ "Order by idDelegacion";
	List<Presupuesto> findAll();

	public String QUERY_GET_PRESUPUESTO_BY_USER   	= "Select P.id, P.anio, P.saldo, P.idDelegacion, D.n_div_geografica,\r\n"
													+ "NVL(P.id_centro_trabajo, '00000') id_centro_trabajo, P.idTipoPresup, T.clave, T.descripcion\r\n"
													+ "From gys_presupuesto P, gys_tip_presupuesto T, m4t_delegaciones D\r\n"
													+ "Where P.idTipoPresup=T.id And P.idDelegacion=D.id_div_geografica\r\n"
													+ "  And P.idDelegacion IN ( Select IdDelegacion From gys_Usuarios Where IdUsuario = ? )\r\n"
													+ "  And P.id_centro_trabajo Is Null\r\n"
													+ "Order by idDelegacion";
	List<Presupuesto> findAllByUser(int idUsuario);

	public String QUERY_GET_PRESUPUESTO_BY_USER_CT	= "Select P.id, P.anio, P.saldo, P.idDelegacion, D.n_div_geografica,\r\n"
													+ "NVL(P.id_centro_trabajo, '00000') id_centro_trabajo, P.idTipoPresup, T.clave, T.descripcion\r\n"
													+ "From gys_presupuesto P, gys_tip_presupuesto T, m4t_delegaciones D\r\n"
													+ "Where P.idTipoPresup=T.id And P.idDelegacion=D.id_div_geografica\r\n"
													+ "  And P.idDelegacion IN ( Select IdDelegacion From gys_Usuarios Where IdUsuario = ? )\r\n"
													+ "  And P.id_centro_trabajo IN ( Select IdCentroTrabajo From gys_Usuarios_Centros_Trab Where IdUsuario = ? )\r\n"
													+ "Order by idDelegacion";
	List<Presupuesto> findAllByUser(int idUsuario, String ct);

	public String QUERY_GET_PRESUPUESTO_BY_ID       = "Select P.id, P.anio, P.saldo, P.idDelegacion, D.n_div_geografica,\r\n"
													+ "NVL(P.id_centro_trabajo, '00000') id_centro_trabajo, P.idTipoPresup, T.clave, T.descripcion\r\n"
													+ "From gys_presupuesto P, gys_tip_presupuesto T, m4t_delegaciones D\r\n"
													+ "Where P.idTipoPresup=T.id And P.idDelegacion=D.id_div_geografica\r\n"
													+ "  And P.id=?";
	Presupuesto getElementById(String id);

	public String QUERY_GET_PRESUPUESTO_BY_TYPE     = "Select P.id, P.anio, P.saldo, P.idDelegacion, D.n_div_geografica,\r\n"
													+ "NVL(P.id_centro_trabajo, '00000') id_centro_trabajo, P.idTipoPresup, T.clave, T.descripcion\r\n"
													+ "From gys_presupuesto P, gys_tip_presupuesto T, m4t_delegaciones D\r\n"
													+ "Where P.idTipoPresup=T.id And P.idDelegacion=D.id_div_geografica\r\n"
													+ "  And P.idDelegacion = ?\r\n"
													+ "  And P.idTipoPresup = ?";
	Presupuesto getElementByType(String idDeleg, Integer idTipo);

	public String QUERY_EXISTS_PRESUPUESTO          = "Select COUNT(*) \r\n"
													+ "From gys_presupuesto\r\n"
													+ "Where anio = ?\r\n"
													+ "  And idDelegacion = ?\r\n"
													+ "  And idtipopresup = ?";
	public int existe_presupuesto(Presupuesto presupuesto);

	List<Presupuesto> get_dynamic_regs(String idDelegacion, String idTipoPresup);

}