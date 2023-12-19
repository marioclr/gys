package gob.issste.gys.repository;

import java.sql.SQLException;
import java.util.List;

import gob.issste.gys.model.Beneficiario;

public interface IBeneficiarioRepository {

	public String STMT_ADD_NEW_BENEFICIARIO			= "INSERT INTO gys_beneficiarios ( idBolsa, rfc_bolsa, nombre, apellidoPaterno, apellidoMaterno, "
													+ "numeroBenef, porcentaje, id_centro_trab, rfc, fec_inicio, fec_fin, cons_benef, id_usuario )\r\n"
													+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
	int save(Beneficiario beneficiario) throws SQLException;

	public String STMT_UPDATE_BENEFICIARIO			= "UPDATE gys_beneficiarios Set rfc_bolsa=?, nombre = ?, apellidoPaterno = ?, apellidoMaterno = ?,\r\n"
													+ "numeroBenef = ?, porcentaje = ?, id_centro_trab = ?, rfc = ?, fec_inicio = ?, fec_fin = ?,\r\n"
													+ "cons_benef = ?, id_usuario = ?\r\n"
													+ "Where id = ?";
	int update(Beneficiario beneficiario);

	public String STMT_DELETE_BENEFICIARIO_BY_ID	= "DELETE FROM gys_beneficiarios WHERE id=?";
	int deleteById(int id);

	public String QUERY_FIND_BENEFICIARIO_BY_ID		= "SELECT * From gys_beneficiarios\r\n"
													+ "Where id=?";
	Beneficiario findById(int id);

	public String QUERY_GET_ALL_BENEFICIARIOS		= "SELECT * FROM gys_beneficiarios\r\n";
	List<Beneficiario> findAll();

	public String QUERY_FIND_BENEFICIARIO_BY_NAME	= "SELECT * from gys_beneficiarios WHERE nombre like ?";
	List<Beneficiario> findByName(String coment);

	public String QUERY_FIND_BENEFICIARIO_BY_TRAB	= "SELECT * from gys_beneficiarios WHERE idBolsa = ?";
	List<Beneficiario> findByTrab(Integer idTrab);

	public String QUERY_FIND_BENEFICIARIO_BY_RFC	= "SELECT * from gys_beneficiarios WHERE rfc like ?";
	List<Beneficiario> findByRFC(String rfc);

	public String QUERY_SUMA_PORC_BENEFICIARIO      = "Select SUM(porcentaje)\r\n"
													+ "From gys_beneficiarios\r\n"
													+ "Where idBolsa = ?";
	public int suma_porc_beneficiario(Beneficiario beneficiario);

	public String QUERY_SUMA_PORC_BENEFICIARIO_UPD  = "Select SUM(porcentaje)\r\n"
													+ "From gys_beneficiarios\r\n"
													+ "Where idBolsa = ?\r\n"
													+ "	 And id<>?";
	public int suma_porc_beneficiario_upd(Beneficiario beneficiario);

	public List<Beneficiario> get_dynamic_regs(String idDelegacion, Integer anio, String coment);

}