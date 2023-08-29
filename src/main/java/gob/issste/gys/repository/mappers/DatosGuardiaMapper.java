package gob.issste.gys.repository.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import gob.issste.gys.model.DatosGuardia;

public class DatosGuardiaMapper implements RowMapper<DatosGuardia> {

	@Override
	public DatosGuardia mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		DatosGuardia guardia = new DatosGuardia();

		guardia.setClave_empleado(rs.getString("clave_empleado"));
		guardia.setId_centro_trabajo(rs.getString("id_centro_trabajo"));
		guardia.setId_clave_servicio(rs.getString("id_clave_servicio"));
		guardia.setId_puesto_plaza(rs.getString("id_puesto_plaza"));
		guardia.setId_nivel(rs.getString("id_nivel"));
		guardia.setId_sub_nivel(rs.getString("id_sub_nivel"));
		guardia.setId_tipo_jornada(rs.getString("id_tipo_jornada"));
		guardia.setHoras(rs.getDouble("horas"));
		guardia.setFec_inicio(rs.getString("fec_inicio"));
		guardia.setFec_fin(rs.getString("fec_fin"));
		guardia.setImporte(rs.getDouble("importe"));
		guardia.setId_tipo_tabulador(rs.getString("id_tipo_tabulador"));
		guardia.setFec_paga(rs.getString("fec_paga"));
		guardia.setId_zona(rs.getString("id_zona"));
		guardia.setRiesgos(rs.getInt("riesgos"));
		guardia.setestatus(rs.getString("estatus"));
		guardia.setId_ordinal(rs.getString("id_ordinal"));

		return guardia;
	}

}
