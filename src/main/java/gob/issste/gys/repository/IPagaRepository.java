package gob.issste.gys.repository;

import java.sql.SQLException;
import java.util.List;

import gob.issste.gys.model.Delegacion;
import gob.issste.gys.model.Paga;

public interface IPagaRepository {

	public String QUERY_ADD_NEW_PAGAS      = "INSERT INTO gys_fechas_control ( fec_pago, descripcion, estatus, fec_inicio, fec_fin, "
										   + "anio_ejercicio, mes_ejercicio, id_tipo_paga, idnivelvisibilidad ) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ? )";
	int save(Paga tipMovPresup) throws SQLException;

	public String QUERY_UPDATE_PAGAS       = "UPDATE gys_fechas_control Set descripcion = ?, estatus = ?, fec_inicio = ?, fec_fin = ?, "
										   + "anio_ejercicio = ?, mes_ejercicio = ?, id_tipo_paga = ?, idnivelvisibilidad = ? Where id = ?";
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

	public String QUERY_GET_ACTIVE_PAGAS_BY_USR = "Select *\r\n"
											+ "From gys_fechas_control A, gys_DelegacionesPorFecha B\r\n"
											+ "Where A.id = B.IdFecha\r\n"
											+ "  And A.estatus = 1\r\n"
											+ "  And (B.IdDelegacion = '00'\r\n"
											+ "    Or B.IdDelegacion IN (\r\n"
											+ "      Select iddelegacion From gys_Usuarios\r\n"
											+ "      Where idusuario = ?\r\n"
											+ "    )\r\n"
											+ "  )";
	List<Paga> findActivePagasByUser(int idUser);

	public String QUERY_GET_PAGAS_BY_STATUS = "SELECT * From gys_fechas_control\r\n"
											+ "WHERE anio_ejercicio = ?\r\n"
											+ "  And mes_ejercicio = ?\r\n"
											+ "  And id_tipo_paga = ?\r\n"
											+ "  And estatus = ?";
	List<Paga> findByStatus(int anio, int mes, int tipo, int status);

	public String STMT_UPDATE_STATUS 		= "UPDATE gys_fechas_control Set estatus = ?\r\n"
			   								+ "Where estatus = 3 And anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = ?";
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

    public String QUERY_ADD_DELEG_X_FECHA	= "Insert Into gys_DelegacionesPorFecha ( IdFecha, IdDelegacion, id_usuario ) Values ( ?, ?, ? )";
    int saveDelegForFecha(int IdFecha, String IdDeleg, String id_usuario);

    public String QUERY_DEL_FOR_FECHA       = "Delete gys_DelegacionesPorFecha Where IdFecha=?";
    int removeDelegForFecha(int IdFecha);

    public String QUERY_GET_DEL_FECHA		= "Select id_div_geografica, n_div_geografica\r\n"
								    		+ "From gys_DelegacionesPorFecha F, m4t_delegaciones D\r\n"
								    		+ "Where F.IdDelegacion = D.id_div_geografica\r\n"
								    		+ "  And IdFecha = ?";
    List<Delegacion> getDelegForFecha(int IdFecha);



}