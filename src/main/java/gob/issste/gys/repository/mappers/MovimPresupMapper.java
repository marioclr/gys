package gob.issste.gys.repository.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import gob.issste.gys.model.MovimientosPresupuesto;
import gob.issste.gys.model.TipoMovPresupuesto;

public class MovimPresupMapper implements RowMapper<MovimientosPresupuesto> {

	@Override
	public MovimientosPresupuesto mapRow(ResultSet rs, int rowNum) throws SQLException {
		// id, importe, idtipomovpresup, id_usuario, fec_ult_actualizacion, id, clave, descripcion, id_usuario, fec_ult_actualizacion
		
		MovimientosPresupuesto mov = new MovimientosPresupuesto();
		mov.setId(rs.getInt("id"));
		mov.setIdPresup(rs.getInt("idPresupuesto"));
		mov.setImporte(rs.getDouble("importe"));
		mov.setComentarios(rs.getString("comentarios"));
		mov.setFec_ult_actualizacion(rs.getString("fec_ult_actualizacion"));

		TipoMovPresupuesto tipMovPresup = new TipoMovPresupuesto();
		tipMovPresup.setId(rs.getInt("idtipomovpresup"));
		tipMovPresup.setClave(rs.getString("clave"));
		tipMovPresup.setDescripcion(rs.getString("descripcion"));

		mov.setTipMovPresup(tipMovPresup);
		
		return mov;
	}
}
