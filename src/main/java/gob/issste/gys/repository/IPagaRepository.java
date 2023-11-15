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

	public String QUERY_GET_PAGAS_BY_STATUS = "SELECT * From gys_fechas_control\r\n"
											+ "WHERE anio_ejercicio = ?\r\n"
											+ "  And mes_ejercicio = ?\r\n"
											+ "  And id_tipo_paga = ?\r\n"
											+ "  And estatus = ?";
	List<Paga> findByStatus(int anio, int mes, int tipo, int status);

	public String STMT_UPDATE_STATUS 		= "UPDATE gys_fechas_control Set estatus = ?\r\n"
			   								+ "Where estatus = 2 And anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = ?";
	int updateStatus(int status, int anio, int mes, int tipo);

	public String QUERY_EXISTS_PAGA_ABIERTA         = "Select COUNT(*) \r\n"
													+ "From gys_fechas_control\r\n"
													+ "Where anio_ejercicio = ?\r\n"
													+ "  And mes_ejercicio = ?\r\n"
													+ "  And id_tipo_paga = ?\r\n"
													+ "  And estatus IN (0,1)";
	public int existe_abierta(Paga paga);

	public String QUERY_EXISTS_PAGA_ABIERTA_AL_CAMBIAR = "Select COUNT(*) \r\n"
													+ "From gys_fechas_control\r\n"
													+ "Where id <> ?\r\n"
													+ "  And anio_ejercicio = ?\r\n"
													+ "  And mes_ejercicio = ?\r\n"
													+ "  And id_tipo_paga = ?\r\n"
													+ "  And estatus IN (0,1)";
	public int existe_abierta_al_cambiar(Paga paga);

}