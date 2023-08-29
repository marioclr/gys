package gob.issste.gys.repository.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import gob.issste.gys.model.ValoresTabulador;

public class ValoresTabuladorMapper implements RowMapper<ValoresTabulador> {

	@Override
	public ValoresTabulador mapRow(ResultSet rs, int rowNum) throws SQLException {
		ValoresTabulador valTab = new ValoresTabulador();
		
		valTab.setSueldo(rs.getDouble("sueldo"));
		valTab.setSueldo(rs.getDouble("compen_g6"));
		valTab.setSueldo(rs.getDouble("compen_un"));
		valTab.setSueldo(rs.getDouble("asignacion"));
		valTab.setSueldo(rs.getDouble("ayuda"));

		return valTab;
	}

}
