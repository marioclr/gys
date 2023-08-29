package gob.issste.gys.repository;

import java.sql.SQLException;

import gob.issste.gys.model.DocumentoDigital;

public interface IImagenBolsaRepository {

	public String QUERY_ADD_IMG_BOLSA_TRABAJO		= "INSERT INTO gys_bolsa_trabajo_img (id_bolsa, imagen) \r\n"
													+ "Values (?, ?)";
	int save(DocumentoDigital elemento) throws SQLException;

	public String QUERY_GET_IMG_BOLSA_TRABAJO_BY_ID = "Select * \r\n"
													+ "From gys_bolsa_trabajo_img\r\n"
													+ "Where id = ?\r\n"
													+ "Order by id";
	DocumentoDigital getElementById(int id);
}
