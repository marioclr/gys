package gob.issste.gys.repository.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import gob.issste.gys.model.Delegacion;
import gob.issste.gys.model.Empleado;
import gob.issste.gys.model.NivelVisibilidad;
import gob.issste.gys.model.Usuario;

public class UsuarioMapper implements RowMapper<Usuario> {

	@Override
	public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {

		Usuario usuario = new Usuario();
		usuario.setIdUsuario(rs.getInt("IdUsuario"));
		usuario.setClave(rs.getString("Clave"));
		usuario.setPassword(rs.getString("Password"));
		usuario.setId_usuario(rs.getString("id_usuario"));

		Empleado empleado = new Empleado();
		empleado.setId_sociedad(rs.getString("id_sociedad"));
		empleado.setId_empleado(rs.getString("id_empleado"));
		empleado.setNombre(rs.getString("nombre"));
		empleado.setApellido_1(rs.getString("apellido_1"));
		empleado.setApellido_2(rs.getString("apellido_2"));
		empleado.setId_legal(rs.getString("id_legal"));
		usuario.setEmpleado(empleado);

		Delegacion delegacion =  new Delegacion();
		delegacion.setId_div_geografica(rs.getString("id_div_geografica"));
		delegacion.setN_div_geografica(rs.getString("n_div_geografica"));
		usuario.setDelegacion(delegacion);

		NivelVisibilidad nivelVisibilidad = new NivelVisibilidad();
		nivelVisibilidad.setIdNivelVisibilidad(rs.getInt("IdNivelVisibilidad"));
		nivelVisibilidad.setDescripcion(rs.getString("Descripcion"));
		usuario.setNivelVisibilidad(nivelVisibilidad);
		usuario.setIdTipoUsuario(rs.getInt("IdTipoUsuario"));
		usuario.setActivo(rs.getBoolean("activo"));
		usuario.setIntentos(rs.getInt("intentos"));
		return usuario;
	}

}
