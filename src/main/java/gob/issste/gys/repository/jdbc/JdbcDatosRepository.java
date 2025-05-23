package gob.issste.gys.repository.jdbc;

import java.util.ArrayList;
import java.util.List;

import gob.issste.gys.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import gob.issste.gys.JdbcTemplateDemo01Application;
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
	public List<DatosAdscripcion> getDatosAdscripciones_ct(int idUsuario) {
		logger.info(QUERY_GET_ADSCRIPCIONES_BY_USER_CT);
		return jdbcTemplate.query(QUERY_GET_ADSCRIPCIONES_BY_USER_CT, BeanPropertyRowMapper.newInstance(DatosAdscripcion.class), idUsuario, idUsuario);
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
	public List<DelegacionPorFecha> getDatosDelegacionesPorFecha(int idFecha) {
		logger.info(QUERY_GET_DELEGACIONES_FECHA);
		return jdbcTemplate.query(QUERY_GET_DELEGACIONES_FECHA, BeanPropertyRowMapper.newInstance(DelegacionPorFecha.class), idFecha);

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

	@Override
	public Delegacion getDatosDelegacionById(String idDelegacion) {
		logger.info(QUERY_GET_DELEGACION_BY_ID);
		return jdbcTemplate.queryForObject(QUERY_GET_DELEGACION_BY_ID, BeanPropertyRowMapper.newInstance(Delegacion.class), idDelegacion);
	}

	@Override
	public List<DatosMatrizPuestos> ConsultaPuestoAutorizado(String tipo_ct, String clave_servicio, String puesto,
			String nivel, String sub_nivel, String tipo_jornada, String tipo_guardia) {
		String QUERY_CONDITION  = "";

		if (tipo_ct != null) { 
			QUERY_CONDITION += "And id_tipo_ct = ?\r\n";
		}

		if (clave_servicio != null) { 
			QUERY_CONDITION += "And id_clave_servicio = ?\r\n";
		}

		if (puesto != null) { 
			QUERY_CONDITION += "And id_puesto_plaza = ?\r\n";
		}

		if (nivel != null) { 
			QUERY_CONDITION += "And id_nivel = ?\r\n";
		}

		if (sub_nivel != null) { 
			QUERY_CONDITION += "And id_sub_nivel = ?\r\n";
		}

		if (tipo_jornada != null) { 
			QUERY_CONDITION += "And id_tipo_jornada = ?\r\n";
		}

		if (tipo_guardia != null) { 
			QUERY_CONDITION += "And id_tipo_per = ?\r\n";
		}

		final String DYNAMIC_QUERY = "Select id_tipo_ct, id_clave_servicio, n_des_servicio, \r\n"
							 + "  id_puesto_plaza, n_puesto, id_nivel, id_sub_nivel, \r\n"
							 + "  id_tipo_jornada, id_tipo_per tipo \r\n"
							 + "From m4t_gys_matriz_puestos \r\n"
							 + "Where 1=1 \r\n"
							 + QUERY_CONDITION
							 + "Order By 1";

		List<DatosMatrizPuestos> matrix = jdbcTemplate.query(DYNAMIC_QUERY, ps ->{
			int cont = 0;

			if (tipo_ct != null) {
				cont++;
				ps.setString(cont,tipo_ct);
			}

			if (clave_servicio != null) {
				cont++;
				ps.setString(cont,clave_servicio);
			}

			if (puesto != null) {
				cont++;
				ps.setString(cont,puesto);
			}

			if (nivel != null) {
				cont++;
				ps.setString(cont,nivel);
			}

			if (sub_nivel != null) {
				cont++;
				ps.setString(cont,sub_nivel);
			}

			if (tipo_jornada != null) {
				cont++;
				ps.setString(cont,tipo_jornada);
			}

			if (tipo_guardia != null) {
				cont++;
				ps.setString(cont,tipo_guardia);
			}

//			logger.info("Prepared statement: "+ DYNAMIC_QUERY);
		}, BeanPropertyRowMapper.newInstance(DatosMatrizPuestos.class));

		return matrix;
	}

	@Override
	public List<String> getGf(String tipo,String ur, String ct, String aux) {
		logger.info(tipo);
		try {
			List<String> gfs = jdbcTemplate.queryForList(QUERY_GET_GF,String.class, tipo, ur, ct, aux);
			return gfs;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override
	public List<String> getFn(String tipo,String ur, String ct, String aux) {
		logger.info(tipo);
		try {
			List<String> fns = jdbcTemplate.queryForList(QUERY_GET_FN,String.class, tipo, ur, ct, aux);
			return fns;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override
	public List<String> getSf(String tipo,String ur, String ct, String aux) {
		logger.info(tipo);
		try {
			List<String> sfs = jdbcTemplate.queryForList(QUERY_GET_SF,String.class, tipo, ur, ct, aux);
			return sfs;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override
	public List<String> getPg(String tipo,String ur, String ct, String aux) {
		logger.info(tipo);
		try {
			List<String> pgs = jdbcTemplate.queryForList(QUERY_GET_PG,String.class, tipo, ur, ct, aux);
			return pgs;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override
	public List<String> getFf(String tipo,String ur, String ct, String aux) {
		logger.info(tipo);
		try {
			List<String> ffs = jdbcTemplate.queryForList(QUERY_GET_FF,String.class, tipo, ur, ct, aux);
			return ffs;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override
	public List<String> getMun(String tipo,String ur, String ct, String aux) {
		logger.info(tipo);
		try {
			List<String> mun = jdbcTemplate.queryForList(QUERY_GET_MUN,String.class, tipo, ur, ct, aux);
			return mun;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override
	public List<String> getFd(String tipo,String ur, String ct, String aux) {
		logger.info(tipo);
		try {
			List<String> fd = jdbcTemplate.queryForList(QUERY_GET_FD,String.class, tipo, ur, ct, aux);
			return fd;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override
	public List<String> getPtda(String tipo,String ur, String ct, String aux) {
		logger.info(tipo);
		try {
			List<String> ptdas = jdbcTemplate.queryForList(QUERY_GET_PTDA,String.class, tipo, ur, ct, aux);
			return ptdas;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override
	public List<String> getSbptd(String tipo,String ur, String ct, String aux) {
		logger.info(tipo);
		try {
			List<String> sbptds = jdbcTemplate.queryForList(QUERY_GET_SBPTD,String.class, tipo, ur, ct, aux);
			return sbptds;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override
	public List<String> getTp(String tipo,String ur, String ct, String aux) {
		logger.info(tipo);
		try {
			List<String> tps = jdbcTemplate.queryForList(QUERY_GET_TP,String.class, tipo, ur, ct, aux);
			return tps;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override
	public List<String> getTpp(String tipo,String ur, String ct, String aux) {
		logger.info(tipo);
		try {
			List<String> tpps = jdbcTemplate.queryForList(QUERY_GET_TPP,String.class, tipo, ur, ct, aux);
			return tpps;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override
	public List<String> getFdo(String tipo,String ur, String ct, String aux) {
		logger.info(tipo);
		try {
			List<String> fdos = jdbcTemplate.queryForList(QUERY_GET_FDO,String.class, tipo, ur, ct, aux);
			return fdos;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override
	public List<String> getArea(String tipo,String ur, String ct, String aux) {
		logger.info(tipo);
		try {
			List<String> areas = jdbcTemplate.queryForList(QUERY_GET_AREA,String.class, tipo, ur, ct, aux);
			return areas;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override
	public List<String> getAi(String tipo,String ur, String ct, String aux) {
		logger.info(tipo);
		try {
			List<String> ai = jdbcTemplate.queryForList(QUERY_GET_AI,String.class, tipo, ur, ct, aux);
			return ai;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override
	public List<String> getAp(String tipo,String ur, String ct, String aux) {
		logger.info(tipo);
		try {
			List<String> ap = jdbcTemplate.queryForList(QUERY_GET_AP,String.class, tipo, ur, ct, aux);
			return ap;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override
	public List<String> getSp(String tipo,String ur, String ct, String aux) {
		logger.info(tipo);
		try {
			List<String> sp = jdbcTemplate.queryForList(QUERY_GET_SP,String.class, tipo, ur, ct, aux);
			return sp;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override
	public List<String> getR(String tipo,String ur, String ct, String aux) {
		logger.info(QUERY_GET_R);
		try {
			List<String> r = jdbcTemplate.queryForList(QUERY_GET_R,String.class, tipo, ur, ct, aux);
			return r;
		}catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	@Override

	public int validaGuardiasConfirmadas(String fec_pago){
		logger.info(VALIDA_GUARDIAS_CONFIRMADAS);
		return jdbcTemplate.queryForObject(VALIDA_GUARDIAS_CONFIRMADAS, Integer.class, fec_pago,fec_pago,fec_pago,fec_pago);
	}


}