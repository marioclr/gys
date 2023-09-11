package gob.issste.gys.repository;

import java.sql.SQLException;
import java.util.List;

import gob.issste.gys.model.Paga;

public interface IPagaRepository {

	public String QUERY_ADD_NEW_PAGAS      = "INSERT INTO gys_fechas_control ( fec_pago, descripcion, estatus, fec_inicio, fec_fin, "
										   + "anio_ejercicio, mes_ejercicio, id_tipo_paga ) VALUES( ?, ?, ?, ?, ?, ?, ?, ? )";
	int save(Paga tipMovPresup) throws SQLException;

	public String QUERY_UPDATE_PAGAS       = "UPDATE gys_fechas_control Set descripcion = ?, estatus = ?, fec_inicio = ?, fec_fin = ?, "
										   + "anio_ejercicio = ?, mes_ejercicio = ?, id_tipo_paga = ? Where id = ?";
	int update(Paga tipMovPresup);

	public String QUERY_DELETE_PAGAS_BY_ID = "DELETE FROM gys_fechas_control WHERE id=?";
	int deleteById(int id);

	public String QUERY_FIND_PAGAS_BY_ID   = "SELECT * FROM gys_fechas_control WHERE id=?";
	Paga findById(int id);

	public String QUERY_GET_ALL_PAGAS      = "SELECT * FROM gys_fechas_control Order By fec_pago Desc";
	List<Paga> findAll();

	public String QUERY_FIND_PAGAS_BY_DESC = "SELECT * from gys_fechas_control WHERE Descripcion like ?";
	List<Paga> findByDesc(String desc);

	public String QUERY_FIND_PAGAS_BY_FEC  = "SELECT * from gys_fechas_control WHERE fec_pago = ?";
	Paga findByFecha(String fecha_pago);

	public String QUERY_ACTIVATE_PAGAS     = "UPDATE gys_fechas_control Set estatus = ? Where id = ?";
	int activate(Paga tipMovPresup);	

	public String QUERY_GET_ACTIVE_PAGAS   = "SELECT * from gys_fechas_control WHERE estatus = 1";
	List<Paga> findActivePagas();
}
