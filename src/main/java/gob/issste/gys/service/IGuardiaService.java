package gob.issste.gys.service;

import java.sql.SQLException;

import gob.issste.gys.model.DatosGuardia;

public interface IGuardiaService {
	double CalculaImporteGuardia(String tipo_tabulador, String zona, String nivel, String subnivel, String tipo_jornada, Integer riesgos, String tipo, Double horas, String quincena) throws SQLException;
	int guardarGuardia(DatosGuardia guardia, double importe) throws SQLException;
	void actualizaImportesGuardias(String fechaPago, String tipo);
	void actualizaGuardia(DatosGuardia guardia, double importe);
	void eliminarGuardia(Integer id, String tipo);
	void actualizaImportesGuardias2(String fechaPago, String tipo);
}
