package gob.issste.gys.repository.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import gob.issste.gys.model.*;
import org.springframework.jdbc.core.RowMapper;

public class PresupuestoMapper implements RowMapper<Presupuesto> {

	@Override
	public Presupuesto mapRow(ResultSet rs, int rowNum) throws SQLException {

		// SELECT u.*, e.id_sociedad, e.id_empleado, e.nombre, e.apellido_1, e.apellido_2, e.id_legal
		Presupuesto presupuesto = new Presupuesto();

		presupuesto.setId(rs.getInt("id"));
		presupuesto.setAnio(rs.getInt("anio"));
		presupuesto.setMes(rs.getInt("mes"));
		presupuesto.setQuincena(rs.getInt("quincena"));
		presupuesto.setSaldo(rs.getDouble("saldo"));
		
		Delegacion delegacion = new Delegacion();
		delegacion.setId_div_geografica(rs.getString("idDelegacion")); 
		delegacion.setN_div_geografica(rs.getString("n_div_geografica"));

		presupuesto.setDelegacion(delegacion);

		DatosAdscripcion centroTrab = new DatosAdscripcion();
		centroTrab.setClave(rs.getString("id_centro_trabajo"));
		centroTrab.setDescripcion(rs.getString("Descripcion"));
		centroTrab.setTipo(rs.getString("Tipo"));
		centroTrab.setZona(rs.getString("Zona"));

		if (centroTrab.getClave().compareTo("00000") != 0)
			presupuesto.setCentroTrabajo(centroTrab);

		TiposPresupuesto tipoPresupuesto = new TiposPresupuesto();
		tipoPresupuesto.setId(rs.getInt("idTipoPresup"));
		tipoPresupuesto.setClave(rs.getString("clave_tipo_presup"));
		tipoPresupuesto.setDescripcion(rs.getString("descripcion_tipo_presup"));
		presupuesto.setTipoPresup(tipoPresupuesto);

		return presupuesto;
	}

}
