package gob.issste.gys.repository.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import gob.issste.gys.model.Delegacion;
import gob.issste.gys.model.PresupuestoGlobal;

public class PresupuestoGlobalMapper implements RowMapper<PresupuestoGlobal> {

	@Override
	public PresupuestoGlobal mapRow(ResultSet rs, int rowNum) throws SQLException {

		PresupuestoGlobal presupuesto = new PresupuestoGlobal();

		presupuesto.setId(rs.getInt("id"));
		presupuesto.setAnio(rs.getInt("anio"));
		presupuesto.setSaldo(rs.getDouble("saldo"));
		presupuesto.setComentarios(rs.getString("comentarios"));
		presupuesto.setId_usuario(rs.getString("id_usuario"));

		Delegacion delegacion = new Delegacion();
		delegacion.setId_div_geografica(rs.getString("id_div_geografica")); 
		delegacion.setN_div_geografica(rs.getString("n_div_geografica"));

		presupuesto.setDelegacion(delegacion);

		return presupuesto;
	}

}
