package gob.issste.gys.repository.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import gob.issste.gys.model.BolsaTrabajo;
import gob.issste.gys.model.Delegacion;

public class BolsaTrabajoMapper implements RowMapper<BolsaTrabajo> {

	@Override
	public BolsaTrabajo mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		BolsaTrabajo bolsa = new BolsaTrabajo();
		bolsa.setId(rs.getInt("id"));
		bolsa.setRfc(rs.getString("rfc"));
		bolsa.setNombre(rs.getString("nombre"));
		bolsa.setApellidoPat(rs.getString("apellidopat"));
		bolsa.setApellidoMat(rs.getString("apellidomat"));
		bolsa.setCodigoPostal(rs.getString("codigo_postal"));
		bolsa.setCurp(rs.getString("curp"));
		bolsa.setId_beneficiario(rs.getString("id_beneficiario"));

		Delegacion delegacion =  new Delegacion();
		delegacion.setId_div_geografica(rs.getString("id_div_geografica"));
		delegacion.setN_div_geografica(rs.getString("n_div_geografica"));
		
		bolsa.setDelegacion(delegacion);

		return bolsa;
	}

}
