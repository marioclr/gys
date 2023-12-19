package gob.issste.gys.repository;

import java.sql.SQLException;
import java.util.List;

import gob.issste.gys.model.BolsaTrabajo;

public interface IBolsaTrabajoRepository {

	public String QUERY_ADD_BOLSA_TRABAJO			= "INSERT INTO gys_bolsatrabajo (rfc, nombre, apellidopat, apellidomat, idDelegacion, codigo_postal, curp, id_beneficiario ) \r\n"
													+ "Values (?, ?, ?, ?, ?, ?, ?, ?)";
	int save(BolsaTrabajo elemento) throws SQLException;

	public String QUERY_UPD_BOLSA_TRABAJO			= "Update gys_bolsatrabajo \r\n"
													+ "Set rfc=?, nombre=?, apellidopat=?, apellidomat=?, \r\n"
													+ "idDelegacion=?, codigo_postal=?, curp=?, id_beneficiario =? \r\n"
													+ "Where id = ?";
	int update(BolsaTrabajo elemento);

	public String QUERY_GET_BOLSA_TRABAJO			= "Select b.id id, b.rfc rfc, b.nombre nombre, b.apellidopat apellidopat, b.apellidomat apellidomat, d.id_div_geografica, n_div_geografica,\r\n"
													+ "codigo_postal, curp, id_beneficiario\r\n"
													+ "From gys_bolsatrabajo b, m4t_delegaciones d\r\n"
													+ "Where b.idDelegacion=d.id_div_geografica\r\n"
													+ "Order by id";
	List<BolsaTrabajo> findAll();

	public String QUERY_GET_BOLSA_TRABAJO_BY_RFC	= "Select b.id id, b.rfc rfc, b.nombre nombre, b.apellidopat apellidopat, b.apellidomat apellidomat, d.id_div_geografica, n_div_geografica,\r\n"
													+ "codigo_postal, curp, id_beneficiario\r\n"
													+ "From gys_bolsatrabajo b, m4t_delegaciones d\r\n"
													+ "Where b.idDelegacion=d.id_div_geografica\r\n"
													+ "	And rfc = ?\r\n"
													+ " And id_div_geografica = ?\r\n"
													+ "Order by id";
	BolsaTrabajo findByRFC(String rfc);

	public String QUERY_GET_BOLSA_TRABAJO_BY_RFC_DEL = "Select b.id id, b.rfc rfc, b.nombre nombre, b.apellidopat apellidopat, b.apellidomat apellidomat, d.id_div_geografica, n_div_geografica,\r\n"
													+ "codigo_postal, curp, id_beneficiario\r\n"
													+ "From gys_bolsatrabajo b, m4t_delegaciones d\r\n"
													+ "Where b.idDelegacion=d.id_div_geografica And rfc = ?\r\n"
													+ "Order by id";
	BolsaTrabajo findByRFCDel(String rfc, String idDeleg);

	public String QUERY_GET_BOLSA_TRABAJO_LIKE_RFC  = "Select Distinct b.rfc || '-' || d.id_div_geografica || '-' || b.id rfc\r\n"
													+ "From gys_bolsatrabajo b, m4t_delegaciones d\r\n"
													+ "Where b.idDelegacion=d.id_div_geografica And rfc like ?\r\n"
													+ "Order by 1";
	List<String> findLikeRFC(String rfc);
	
	public String QUERY_GET_BOLSA_TRAB_LIKE_RFC_DEL = "Select Distinct b.rfc || '-' || d.id_div_geografica || '-' || b.id rfc\r\n"
													+ "From gys_bolsatrabajo b, m4t_delegaciones d\r\n"
													+ "Where b.idDelegacion=d.id_div_geografica And rfc like ? And id_div_geografica = ?\r\n"
													+ "Order by 1";
	List<String> findLikeRFC(String rfc, String idDeleg);

	public String QUERY_DEL_BOLSA_TRABAJO			= "Delete gys_bolsatrabajo Where id = ?";
	int deleteById(int id);

	public String QUERY_GET_BOLSA_TRABAJO_BY_ID     = "Select b.id id, b.rfc rfc, b.nombre nombre, b.apellidopat apellidopat, b.apellidomat apellidomat, d.id_div_geografica, n_div_geografica,\r\n"
													+ "codigo_postal, curp, id_beneficiario\r\n"
													+ "From gys_bolsatrabajo b, m4t_delegaciones d\r\n"
													+ "Where b.idDelegacion=d.id_div_geografica And id = ?\r\n"
													+ "Order by id";
	BolsaTrabajo getElementById(int id);

	public String QUERY_GET_BOLSA_TRABAJO_FOR_DELEG	= "Select b.id id, b.rfc rfc, b.nombre nombre, b.apellidopat apellidopat, b.apellidomat apellidomat, d.id_div_geografica, n_div_geografica,\r\n"
													+ "codigo_postal, curp, id_beneficiario\r\n"
													+ "From gys_bolsatrabajo b, m4t_delegaciones d\r\n"
													+ "Where b.idDelegacion=d.id_div_geografica\r\n"
													+ "  And b.idDelegacion=?\r\n"
													+ "Order by id";
	List<BolsaTrabajo> findForDeleg(String idDeleg);

}