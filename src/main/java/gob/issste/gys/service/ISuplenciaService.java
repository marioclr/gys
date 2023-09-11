package gob.issste.gys.service;

import java.sql.SQLException;

import gob.issste.gys.model.DatosEmpleado;
import gob.issste.gys.model.DatosSuplencia;

public interface ISuplenciaService {
	double CalculaImporteSuplencia(String quincena, String clave_empleado, int dias, String tipo);
	double CalculaImporteSuplencia(String quincena, DatosEmpleado empleado, int dias, String tipo);
	int GuardarSuplencia(DatosSuplencia suplencia, double importe) throws SQLException;
	void ActualizaImportesSuplencias(String fechaPago, String tipo);
	void ActualizaImportesSuplencias2(String fechaPago, String tipo);
	void actualizaSuplencia(DatosSuplencia suplencia, double importe);
	void eliminarSuplencia(Integer id, String tipo);
}
