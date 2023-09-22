package gob.issste.gys.service;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.DatosGuardia;
import gob.issste.gys.model.ValoresTabulador;
import gob.issste.gys.repository.GuardiaRepository;
import gob.issste.gys.repository.IEmpleadoRepository;
import gob.issste.gys.repository.ITabuladorRepository;

@Service
public class GuardiaServiceImpl implements IGuardiaService {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	GuardiaRepository guardiaRepository;
	@Autowired
	ITabuladorRepository tabuladorRepository;
	@Autowired
	IEmpleadoRepository empleadoRepository;

	@Override
	public double CalculaImporteGuardia(String tipo_tabulador, String zona, String nivel, String subnivel,
			String tipo_jornada, Integer riesgos, String tipo, Double horas, String quincena) throws SQLException {

		double importe = 0.0;
		ValoresTabulador valores;

		try {
			if (tipo_tabulador.equals(String.valueOf("M"))) {
				valores = tabuladorRepository.ConsultaTabuladorMedico(zona, nivel, subnivel, tipo_jornada, quincena);
			} else {
				valores = tabuladorRepository.ConsultaTabuladorOperativo(zona, nivel, subnivel, tipo_jornada, quincena);
			}
		} catch (Exception Ex) {
			valores = new ValoresTabulador(0.0, 0.0, 0.0, 0.0, 0.0);
			throw new SQLException("No existe tabulador vigente");
		}

		switch (riesgos)
        {
	        case 10: importe = valores.getSueldo() * 1.1; break;
	        case 20: importe = valores.getSueldo() * 1.2; break;
	        default: importe = valores.getSueldo(); break;
        }

		if (tipo.equals(String.valueOf("GI"))) {
	        switch (tipo_tabulador)
	        {
	            case "M": importe = importe + valores.getAsignacion(); break;
	            default: importe = importe + valores.getCompen_un(); break;
	        }
		}

		importe = (tipo.equals(String.valueOf("GI"))) ? importe = (importe / 30) * 2 : (importe / 30);

        importe = importe / Double.valueOf(tipo_jornada) * horas;

		return importe;
	}

	@Override
	public int guardarGuardia(DatosGuardia guardia, double importe) throws SQLException {
		guardia.setImporte(importe);
		if (guardia.getTipo_guardia().equals(String.valueOf("GI"))) {
			return guardiaRepository.save(guardia);
		} else {
			return guardiaRepository.saveExt(guardia);
		}
	}

	@Override
	public void actualizaImportesGuardias(String fechaPago, String tipo) {
		if (tipo.equals(String.valueOf("GI"))) {
			List<DatosGuardia> guardias = guardiaRepository.ConsultaGuardiasInternasXFecha(fechaPago);
			for(DatosGuardia g:guardias) {
				try {
					g.setRiesgos(empleadoRepository.ConsultaRiesgosEmp(g.getClave_empleado(), g.getFec_paga()));
					g.setImporte(this.CalculaImporteGuardia(g.getId_tipo_tabulador(), g.getId_zona(), g.getId_nivel(), g.getId_sub_nivel(), g.getId_tipo_jornada(), 
							g.getRiesgos(), "GI", g.getHoras(), fechaPago));
					guardiaRepository.updateImporteGuardia(g);
				} catch (Exception Ex) {
					logger.info(g.toString());
				}
			}
		} else {
			List<DatosGuardia> guardias = guardiaRepository.ConsultaGuardiasExternasXFecha(fechaPago);
			for(DatosGuardia g:guardias) {
				try {
					g.setImporte(this.CalculaImporteGuardia(g.getId_tipo_tabulador(), g.getId_zona(), g.getId_nivel(), g.getId_sub_nivel(), g.getId_tipo_jornada(), 
							g.getRiesgos(), "GE", g.getHoras(), fechaPago));
					guardiaRepository.updateImporteGuardiaExt(g);
				} catch (Exception Ex) {
					logger.info(g.toString());
				}
			}
		}
	}

	@Override
	public void actualizaGuardia(DatosGuardia guardia, double importe) {
		guardia.setImporte(importe);
		if (guardia.getTipo_guardia().equals(String.valueOf("GI")))
			guardiaRepository.updateGuardia(guardia);
		else
			guardiaRepository.updateGuardiaExt(guardia);
	}

	@Override
	public void eliminarGuardia(Integer id, String tipo) {
		if (tipo.equals(String.valueOf("GI")))
			guardiaRepository.deleteGuardia(id);
		else
			guardiaRepository.deleteGuardiaExt(id);
	}

	@Override
	public void actualizaImportesGuardias2(String fechaPago, String tipo) {
		List<DatosGuardia> guardias = null;

		if (tipo.equals(String.valueOf("GI"))) 
			guardias = guardiaRepository.ConsultaGuardiasInternasXFecha(fechaPago);
		else
			guardias = guardiaRepository.ConsultaGuardiasExternasXFecha(fechaPago);

		for(DatosGuardia g:guardias) {
			try {
				g.setRiesgos(empleadoRepository.ConsultaRiesgosEmp(g.getClave_empleado(), g.getFec_paga())); // Se probo aquí el poner el empleado1
			} catch(Exception ex) {
				g.setRiesgos(0);
			}
			try {
				//DatosEmpleado empleado = empleadoRepository.getDatosEmpleado(fechaPago, g.getClave_empleado()); // Se probo aquí el poner el empleado1
				//g.setDatos_empleado1(empleado);
				g.setImporte(this.CalculaImporteGuardia(g.getId_tipo_tabulador(), g.getId_zona(), g.getId_nivel(), g.getId_sub_nivel(), g.getId_tipo_jornada(), 
						g.getRiesgos(), "GE", g.getHoras(), fechaPago));
			} catch(Exception ex) {
				g.setImporte((double) 0);
				continue;
			}
			if (tipo.equals(String.valueOf("GI"))) {
				guardiaRepository.updateGuardiaIntVars(g);
			} else {
				guardiaRepository.updateGuardiaExtVars(g);
			}
		}
	}

}