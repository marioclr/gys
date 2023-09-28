package gob.issste.gys.repository;

import java.sql.SQLException;
import java.util.List;

import gob.issste.gys.model.Presupuesto;

public interface IPresupuestoRepository {

	public String QUERY_ADD_PRESUPUESTO			    = "INSERT INTO gys_presupuesto (anio, mes, idDelegacion, idTipoPresup, saldo)\r\n"
													+ "Values ( ?, ?, ?, ?, ? )";
	int save(Presupuesto presupuesto) throws SQLException;

	public String QUERY_ADD_PRESUPUESTO_CT		    = "INSERT INTO gys_presupuesto (anio, mes, idDelegacion, id_centro_trabajo, idTipoPresup, saldo)\r\n"
													+ "Values ( ?, ?, ?, ?, ?, ? )";
	int save_ct(Presupuesto presupuesto) throws SQLException;

	public String QUERY_UPD_PRESUPUESTO			    = "Update gys_presupuesto\r\n"
													+ "Set saldo=saldo+?\r\n"
													+ "Where id = ?";
	int update(int idPresupuesto, Double actualizacion);

	public String QUERY_GET_PRESUPUESTO		    	= "Select P.id, P.anio, P.mes, P.saldo, P.idDelegacion, D.n_div_geografica,\r\n"
													+ "NVL(P.id_centro_trabajo, '00000') id_centro_trabajo, P.idTipoPresup, T.clave clave_tipo_presup, T.descripcion descripcion_tipo_presup,\r\n"
													+ "NVL(C.id_centro_trabajo, '') Clave, NVL(n_centro_trabajo, '') Descripcion, NVL(id_tipo_ct, '') Tipo, NVL(id_zona, '') Zona\r\n"
													+ "From gys_presupuesto P Left Join m4t_centros_trab C ON P.id_centro_trabajo = C.id_centro_trabajo, gys_tip_presupuesto T, m4t_delegaciones D\r\n"
													+ "Where P.idTipoPresup=T.id And P.idDelegacion=D.id_div_geografica\r\n"
													+ "Order by idDelegacion";
	List<Presupuesto> findAll();

	public String QUERY_GET_PRESUPUESTO_BY_USER   	= "Select P.id, P.anio, P.mes, P.saldo, P.idDelegacion, D.n_div_geografica,\r\n"
													+ "NVL(P.id_centro_trabajo, '00000') id_centro_trabajo, P.idTipoPresup, T.clave clave_tipo_presup, T.descripcion descripcion_tipo_presup,\r\n"
													+ "NVL(C.id_centro_trabajo, '') Clave, NVL(n_centro_trabajo, '') Descripcion, NVL(id_tipo_ct, '') Tipo, NVL(id_zona, '') Zona\r\n"
													+ "From gys_presupuesto P Left Join m4t_centros_trab C ON P.id_centro_trabajo = C.id_centro_trabajo, gys_tip_presupuesto T, m4t_delegaciones D\r\n"
													+ "Where P.idTipoPresup=T.id And P.idDelegacion=D.id_div_geografica\r\n"
													+ "  And P.idDelegacion IN ( Select IdDelegacion From gys_Usuarios Where IdUsuario = ? )\r\n"
													+ "  And P.id_centro_trabajo Is Null\r\n"
													+ "Order by idDelegacion";
	List<Presupuesto> findAllByUser(int idUsuario);

	public String QUERY_GET_PRESUPUESTO_BY_USER_CT	= "Select P.id, P.anio, P.mes, P.saldo, P.idDelegacion, D.n_div_geografica,\r\n"
													+ "NVL(P.id_centro_trabajo, '00000') id_centro_trabajo, P.idTipoPresup, T.clave clave_tipo_presup, T.descripcion descripcion_tipo_presup,\r\n"
													+ "NVL(C.id_centro_trabajo, '') Clave, NVL(n_centro_trabajo, '') Descripcion, NVL(id_tipo_ct, '') Tipo, NVL(id_zona, '') Zona\r\n"
													+ "From gys_presupuesto P Left Join m4t_centros_trab C ON P.id_centro_trabajo = C.id_centro_trabajo, gys_tip_presupuesto T, m4t_delegaciones D\r\n"
													+ "Where P.idTipoPresup=T.id And P.idDelegacion=D.id_div_geografica\r\n"
													+ "  And P.idDelegacion IN ( Select IdDelegacion From gys_Usuarios Where IdUsuario = ? )\r\n"
													+ "  And P.id_centro_trabajo IN ( Select IdCentroTrabajo From gys_Usuarios_Centros_Trab Where IdUsuario = ? )\r\n"
													+ "Order by idDelegacion";
	List<Presupuesto> findAllByUser(int idUsuario, String ct);

	public String QUERY_GET_PRESUPUESTO_BY_ID       = "Select P.id, P.anio, P.mes, P.saldo, P.idDelegacion, D.n_div_geografica,\r\n"
													+ "NVL(P.id_centro_trabajo, '00000') id_centro_trabajo, P.idTipoPresup, T.clave clave_tipo_presup, T.descripcion descripcion_tipo_presup,\r\n"
													+ "NVL(C.id_centro_trabajo, '') Clave, NVL(n_centro_trabajo, '') Descripcion, NVL(id_tipo_ct, '') Tipo, NVL(id_zona, '') Zona\r\n"
													+ "From gys_presupuesto P Left Join m4t_centros_trab C ON P.id_centro_trabajo = C.id_centro_trabajo, gys_tip_presupuesto T, m4t_delegaciones D\r\n"
													+ "Where P.idTipoPresup=T.id And P.idDelegacion=D.id_div_geografica\r\n"
													+ "  And P.id=?";
	Presupuesto getElementById(int id);

	public String QUERY_GET_PRESUPUESTO_BY_TYPE     = "Select P.id, P.anio, P.mes, P.saldo, P.idDelegacion, D.n_div_geografica,\r\n"
													+ "NVL(P.id_centro_trabajo, '00000') id_centro_trabajo, P.idTipoPresup, T.clave clave_tipo_presup, T.descripcion descripcion_tipo_presup,\r\n"
													+ "NVL(C.id_centro_trabajo, '') Clave, NVL(n_centro_trabajo, '') Descripcion, NVL(id_tipo_ct, '') Tipo, NVL(id_zona, '') Zona\r\n"
													+ "From gys_presupuesto P Left Join m4t_centros_trab C ON P.id_centro_trabajo = C.id_centro_trabajo, gys_tip_presupuesto T, m4t_delegaciones D\r\n"
													+ "Where P.idTipoPresup=T.id And P.idDelegacion=D.id_div_geografica\r\n"
													+ "  And P.idDelegacion = ?\r\n"
													+ "  And T.clave = ?";
	Presupuesto getElementByType(String idDeleg, Integer idTipo);

	public String QUERY_GET_PRESUPUESTO_BY_TYPE_CT  = "Select P.id, P.anio, P.mes, P.saldo, P.idDelegacion, D.n_div_geografica,\r\n"
													+ "NVL(P.id_centro_trabajo, '00000') id_centro_trabajo, P.idTipoPresup, T.clave clave_tipo_presup, T.descripcion descripcion_tipo_presup,\r\n"
													+ "NVL(C.id_centro_trabajo, '') Clave, NVL(n_centro_trabajo, '') Descripcion, NVL(id_tipo_ct, '') Tipo, NVL(id_zona, '') Zona\r\n"
													+ "From gys_presupuesto P Left Join m4t_centros_trab C ON P.id_centro_trabajo = C.id_centro_trabajo, gys_tip_presupuesto T, m4t_delegaciones D\r\n"
													+ "Where P.idTipoPresup=T.id And P.idDelegacion=D.id_div_geografica\r\n"
													+ "  And P.id_centro_trabajo = ?\r\n"
													+ "  And T.clave = ?\r\n"
													+ "  And P.anio = ?\r\n"
													+ "  And P.mes = ?\r\n";
	Presupuesto getElementByType_ct(String idCentroTrab, String idTipo, Integer anio, Integer mes);

	public String QUERY_EXISTS_PRESUPUESTO          = "Select COUNT(*) \r\n"
													+ "From gys_presupuesto\r\n"
													+ "Where anio = ?\r\n"
													+ "  And mes = ?\r\n"
													+ "  And idDelegacion = ?\r\n"
													+ "  And idtipopresup = ?";
	public int existe_presupuesto(Presupuesto presupuesto);

	public String QUERY_EXISTS_PRESUPUESTO_CT       = "Select COUNT(*) \r\n"
													+ "From gys_presupuesto\r\n"
													+ "Where anio = ?\r\n"
													+ "  And mes = ?\r\n"
													+ "  And idDelegacion = ?\r\n"
													+ "  And id_centro_trabajo = ?\r\n"
													+ "  And idtipopresup = ?";
	public int existe_presupuesto_ct(Presupuesto presupuesto);

	public String QUERY_SUMA_PRESUPUESTO_CT       	= "Select NVL(SUM(saldo), 0) + ? saldo \r\n"
													+ "From gys_presupuesto\r\n"
													+ "Where anio = ?\r\n"
													+ "  And mes = ?\r\n"
													+ "  And idDelegacion = ?\r\n"
													+ "  And id_centro_trabajo Is Not Null\r\n"
													+ "  And idtipopresup = ?";
	public double validaSumaPresupuestal(Presupuesto presupuesto);

	public List<Presupuesto> get_dynamic_regs(String idDelegacion, String claveTipoPresup, Integer anio, Integer mes, String idCentTrab, boolean solo_deleg);

	public double getSaldoDelegCt(String idDelegacion, String idTipoPresup, Integer anio, Integer mes, String idCentTrab);

	public String QUERY_GET_SALDO_DISTRIBUIDO		= "Select NVL(SUM(saldo), 0) saldo\r\n"
													+ "From gys_presupuesto P Left Join m4t_centros_trab C ON P.id_centro_trabajo = C.id_centro_trabajo, gys_tip_presupuesto T, m4t_delegaciones D\r\n"
													+ "Where P.idTipoPresup=T.id And P.idDelegacion=D.id_div_geografica\r\n"
													+ "  And P.anio = ?\r\n"
													+ "  And mes = ?\r\n"
													+ "  And P.idDelegacion = ?\r\n"
													+ "  And T.clave = ?\r\n"
													+ "  And P.id_centro_trabajo Is NOT NULL\r\n"
													+ "Group By idDelegacion";
	public double getSaldoDistribuido(Integer anio, Integer mes, String idDelegacion, String idTipoPresup);

}