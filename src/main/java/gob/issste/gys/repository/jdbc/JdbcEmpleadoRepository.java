package gob.issste.gys.repository.jdbc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.DatosEmpleado;
import gob.issste.gys.model.Empleado;
import gob.issste.gys.repository.IEmpleadoRepository;

@Repository
public class JdbcEmpleadoRepository implements IEmpleadoRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public DatosEmpleado getDatosEmpleado(String fecha, String claveEmpleado) {

		logger.info(QUERY_GET_EMPLEADOS);
		return jdbcTemplate.queryForObject(QUERY_GET_EMPLEADOS, BeanPropertyRowMapper.newInstance(DatosEmpleado.class), 
				new Object[] { fecha, fecha, fecha, fecha, fecha, fecha, fecha, fecha, fecha, fecha, fecha, fecha, fecha, fecha, fecha, fecha, fecha, fecha, fecha, fecha, fecha, claveEmpleado } );

	}

	@Override
	public int ConsultaRiesgosEmp(String clave_empleado, String fec_pago) {
		//logger.info(QUERY_GET_RIESGOS_EMPLEADO);
		int porcentaje;
		try {			
			porcentaje = jdbcTemplate.queryForObject( QUERY_GET_RIESGOS_EMPLEADO, Integer.class, new Object [] { clave_empleado, fec_pago, fec_pago } );
		} catch (EmptyResultDataAccessException ex) {
			porcentaje = 0;
		}
		return porcentaje;
	}

//	@Override
//	public int save(Empleado empleado) {
//	    return jdbcTemplate.update("INSERT INTO m4t_empleados (id_sociedad, id_empleado,nombre,apellido_1,apellido_2, id_legal) VALUES(?,?,?,?,?,?)",
//	            new Object[] { empleado.getId_sociedad(), empleado.getId_empleado(), empleado.getNombre(), empleado.getApellido_1(), empleado.getApellido_2(), empleado.getId_legal() });
//	}
//	@Override
//	public int update(Empleado empleado) {
//	    return jdbcTemplate.update("UPDATE m4t_empleados SET nombre=?, apellido_1=?, apellido_2=?, id_legal=? WHERE id_empleado=?",
//	            new Object[] { empleado.getNombre(), empleado.getApellido_1(), empleado.getApellido_2(), empleado.getId_legal(), empleado.getId_empleado() });
//	}
	@Override
	public Empleado findById(String id) {
		try {
			Empleado empleado = jdbcTemplate.queryForObject("SELECT * FROM m4t_empleados WHERE id_empleado=?",
	          BeanPropertyRowMapper.newInstance(Empleado.class), id);

	      return empleado;
	    } catch (IncorrectResultSizeDataAccessException e) {
	      return null;
	    }
	}
//	@Override
//	public int deleteById(int id) {
//	    return jdbcTemplate.update("DELETE FROM m4t_empleados WHERE id_empleado=?", id);
//	}
	@Override
	public List<Empleado> findAll() {
	    return jdbcTemplate.query("SELECT First 1000 * from m4t_empleados", BeanPropertyRowMapper.newInstance(Empleado.class));
	}

	@Override
	public List<Empleado> findByNombre(String nombre) {
		String q = "SELECT * from m4t_empleados WHERE nombre LIKE '%" + nombre + "%'";

	    return jdbcTemplate.query(q, BeanPropertyRowMapper.newInstance(Empleado.class));
	}
//	@Override
//	public int deleteAll() {
//	    return jdbcTemplate.update("DELETE from tutorials");
//	}
}
