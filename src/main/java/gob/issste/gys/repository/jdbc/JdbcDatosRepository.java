package gob.issste.gys.repository.jdbc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.DatosAdscripcion;
import gob.issste.gys.model.DatosGuardia;
import gob.issste.gys.model.DatosJornada;
import gob.issste.gys.model.DatosNivel;
import gob.issste.gys.model.DatosPuesto;
import gob.issste.gys.model.DatosServicio;
import gob.issste.gys.model.Delegacion;
import gob.issste.gys.model.Horario;
import gob.issste.gys.model.Incidencia;
import gob.issste.gys.model.Paga;
import gob.issste.gys.repository.IDatosRepository;

@Repository
public class JdbcDatosRepository implements IDatosRepository {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<DatosAdscripcion> getDatosAdscripciones() {

		logger.info(QUERY_GET_ADSCRIPCIONES);
		return jdbcTemplate.query(QUERY_GET_ADSCRIPCIONES, BeanPropertyRowMapper.newInstance(DatosAdscripcion.class));

	}

	@Override
	public List<DatosAdscripcion> getDatosAdscForDeleg(String idDeleg) {

		logger.info(QUERY_GET_ADSCRIPCIONES_FOR_DEL);
		return jdbcTemplate.query(QUERY_GET_ADSCRIPCIONES_FOR_DEL, BeanPropertyRowMapper.newInstance(DatosAdscripcion.class), idDeleg);

	}

	@Override
	public List<DatosAdscripcion> getDatosAdscripciones(int idUsuario) {
		logger.info(QUERY_GET_ADSCRIPCIONES_BY_USER);
		return jdbcTemplate.query(QUERY_GET_ADSCRIPCIONES_BY_USER, BeanPropertyRowMapper.newInstance(DatosAdscripcion.class), idUsuario);
	}

	@Override
	public List<DatosPuesto> getDatosPuestosGuardia() {

		logger.info(QUERY_GET_PUESTOS_GUARDIA);
		return jdbcTemplate.query(QUERY_GET_PUESTOS_GUARDIA, BeanPropertyRowMapper.newInstance(DatosPuesto.class));

	}

	@Override
	public List<DatosPuesto> getDatosPuestosGuardia(String adsc) {

		logger.info(QUERY_GET_PUESTOS_GUARDIA_X_ADSC);
		return jdbcTemplate.query(QUERY_GET_PUESTOS_GUARDIA_X_ADSC, BeanPropertyRowMapper.newInstance(DatosPuesto.class), adsc);

	}

	@Override
	public List<DatosServicio> getDatosServiciosGuardia() {

		logger.info(QUERY_GET_SERVICIOS_GUARDIA);
		return jdbcTemplate.query(QUERY_GET_SERVICIOS_GUARDIA, BeanPropertyRowMapper.newInstance(DatosServicio.class));

	}

	@Override
	public List<DatosServicio> getDatosServiciosGuardia(String adsc, String puesto) {

		logger.info(QUERY_GET_SERVICIOS_GUARDIA_X_ADSC_PTO);
		return jdbcTemplate.query(QUERY_GET_SERVICIOS_GUARDIA_X_ADSC_PTO, BeanPropertyRowMapper.newInstance(DatosServicio.class), adsc, puesto);

	}

	@Override
	public List<DatosNivel> getDatosNivelesGuardia() {

		logger.info(QUERY_GET_NIVELES_GUARDIA);
		return jdbcTemplate.query(QUERY_GET_NIVELES_GUARDIA, BeanPropertyRowMapper.newInstance(DatosNivel.class));

	}

	@Override
	public List<DatosNivel> getDatosNivelesGuardia(String adsc, String puesto, String servicio) {

		logger.info(QUERY_GET_NIVELES_GUARDIA_X_ADSC_PTO_SERV);
		return jdbcTemplate.query(QUERY_GET_NIVELES_GUARDIA_X_ADSC_PTO_SERV, BeanPropertyRowMapper.newInstance(DatosNivel.class), adsc, puesto, servicio);

	}

	@Override
	public List<DatosJornada> getDatosJornadasGuardia() {

		logger.info(QUERY_GET_JORNADAS_GUARDIA);
		return jdbcTemplate.query(QUERY_GET_JORNADAS_GUARDIA, BeanPropertyRowMapper.newInstance(DatosJornada.class));

	}

	@Override
	public List<DatosJornada> getDatosJornadasGuardia(String adsc, String puesto, String servicio, String niveles) {

		logger.info(QUERY_GET_JORNADAS_GUARDIA_X_ADSC_PTO_SERV_NIV);
		return jdbcTemplate.query(QUERY_GET_JORNADAS_GUARDIA_X_ADSC_PTO_SERV_NIV, BeanPropertyRowMapper.newInstance(DatosJornada.class), adsc, puesto, servicio, niveles.substring(0, 2), niveles.substring(2, 3));

	}

	@Override
	public long ValidaPuestoAutorizado(String tipo_ct, String clave_servicio, String puesto, String nivel,
			String sub_nivel, String tipo_jornada, String tipo_guardia) {

		logger.info(QUERY_VALIDA_PUESTO_AUTORIZADO);

		return jdbcTemplate.queryForObject(QUERY_VALIDA_PUESTO_AUTORIZADO, Long.class, new Object[] { tipo_ct, clave_servicio, puesto, nivel, sub_nivel, tipo_jornada, tipo_guardia });
	}

	@Override
	public List<DatosGuardia> ConsultaGuardiasInternas(String claveEmpleado) {

		logger.info(QUERY_CONSULTA_GUARDIAS_INTERNAS);
		return jdbcTemplate.query(QUERY_CONSULTA_GUARDIAS_INTERNAS, BeanPropertyRowMapper.newInstance(DatosGuardia.class), claveEmpleado);
		//return jdbcTemplate.query(QUERY_CONSULTA_GUARDIAS_INTERNAS, new DatosGuardiaMapper(), claveEmpleado);
	}

	@Override
	public List<Delegacion> getDatosDelegaciones() {

		logger.info(QUERY_GET_DELEGACIONES);
		return jdbcTemplate.query(QUERY_GET_DELEGACIONES, BeanPropertyRowMapper.newInstance(Delegacion.class));

	}

	@Override
	public String ValidaPersonalExterno(String rfc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Horario> getHorarios() {

		logger.info(QUERY_GET_HORARIOS);
		return jdbcTemplate.query(QUERY_GET_HORARIOS, BeanPropertyRowMapper.newInstance(Horario.class));

	}

	@Override
	public List<Incidencia> getIncidencia() {
		logger.info(QUERY_GET_TIP_INCIDENCIA);
		return jdbcTemplate.query(QUERY_GET_TIP_INCIDENCIA, BeanPropertyRowMapper.newInstance(Incidencia.class));
	}

	@Override
	public List<Paga> getPagas() {
		logger.info(QUERY_GET_PAGAS);
		return jdbcTemplate.query(QUERY_GET_PAGAS, BeanPropertyRowMapper.newInstance(Paga.class));
	}

}