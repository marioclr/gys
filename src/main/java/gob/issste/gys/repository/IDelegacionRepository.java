package gob.issste.gys.repository;

import java.util.List;

import gob.issste.gys.model.DatosAdscripcion;
import gob.issste.gys.model.Delegacion;

public interface IDelegacionRepository {

	public String QUERY_FIND_DELEG_BY_ID         = "SELECT * FROM m4t_delegaciones WHERE id_div_geografica=?";
	Delegacion findById(int id);

	public String QUERY_GET_ALL_DELEG            = "SELECT * FROM m4t_delegaciones";
	List<Delegacion> findAll();

	public String QUERY_FIND_DELEG_BY_DESC       = "SELECT * from m4t_delegaciones WHERE id_div_geografica=?";
	List<Delegacion> findByDeleg(String clave_deleg);

	public String QUERY_FIND_CENT_TRAB_FOR_DELEG = "SELECT id_centro_trabajo Clave, n_centro_trabajo Descripcion, id_tipo_ct Tipo, id_zona Zona\r\n"
												 + "From m4t_centros_trab WHERE id_delegacion=?";
	List<DatosAdscripcion> getCentrosTrabForDeleg(Delegacion clave_deleg);

}