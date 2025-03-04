package gob.issste.gys.service;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.DatosEmpleado;
import gob.issste.gys.model.DatosSuplencia;
import gob.issste.gys.model.FactoresSuplencia;
import gob.issste.gys.model.ValoresTabulador;
import gob.issste.gys.repository.IEmpleadoRepository;
import gob.issste.gys.repository.ISuplenciaRepository;
import gob.issste.gys.repository.ITabuladorRepository;

@Service
public class SuplenciaServiceImpl implements ISuplenciaService {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);

	@Autowired
	ISuplenciaRepository suplenciaRepository;
	@Autowired
	IEmpleadoRepository empleadoRepository;
	@Autowired
	ITabuladorRepository tabuladorRepository;

	//Primer metodo

	@Override
	public double CalculaImporteSuplencia(String quincena, String clave_empleado, int dias, String tipo, int riesgos ) {

		double importe = 0.0;
		ValoresTabulador valores;
		FactoresSuplencia factor = new FactoresSuplencia();

		DatosEmpleado empleado = empleadoRepository.getDatosEmpleado(quincena, clave_empleado);

		if (empleado.getId_tipo_tabulador().equals(String.valueOf("M"))) {
			valores = tabuladorRepository.ConsultaTabuladorMedico(empleado.getId_zona(), empleado.getId_nivel(), empleado.getId_sub_nivel(), empleado.getId_tipo_jornada(), quincena);
		} else {
			valores = tabuladorRepository.ConsultaTabuladorOperativo(empleado.getId_zona(), empleado.getId_nivel(), empleado.getId_sub_nivel(), empleado.getId_tipo_jornada(), quincena);
		}

		switch ( riesgos )
        {
	        case 10: importe = valores.getSueldo() * 1.1; break;
	        case 20: importe = valores.getSueldo() * 1.2; break;
	        default: importe = valores.getSueldo(); break;
        }

        if (tipo.equals(String.valueOf("SI"))) {
            switch (empleado.getId_tipo_tabulador())
            {
                case "M": importe = importe + valores.getAsignacion(); break;
                default: importe = importe + valores.getCompen_un(); break;
            }

            importe = (importe / 30);

            factor = suplenciaRepository.ConsultaFactoresSuplencia(empleado.getId_turno());

            if (dias >= factor.getFact_turno()) {
            	importe = importe * dias * factor.getFact_jornada();
            } else {
            	importe = importe * dias;
            }
        } else {

        	importe = (importe / 30);

        	int turno = Integer.valueOf(empleado.getId_turno());

        	logger.info("Factor turno: " + factor.getFact_turno() + ", Factor jornada: " + factor.getFact_jornada() + ", Turno: " + turno);

//			switch(turno) {
//	    		case 11, 12:
//	    			if (dias == 5) {
//	    				importe = importe * dias * 1.4;
//	    			} else {
//	    				importe = importe * dias;
//	    			}
//	    			break;
//				case 13:
//					if (dias == 5) {
//						importe = importe * dias * 1.4 * 2;
//					} else {
//						importe = importe * dias * 2;
//					}
//					break;
//				case 21, 22, 23:
//					if (dias == 5) {
//	    				importe = importe * dias * 1.4 * 2;
//	    			} else {
//	    				importe = importe * dias * 2;
//	    			}
//					break;
//	    		case 31, 32:
//	    			if (dias >= 2) {
//	    				importe = importe * dias * 1.4 * 2;
//	    			} else {
//	    				importe = importe * dias * 2;
//	    			}
//	    			break;
//				case 41, 42:
//					if (dias >= 1) {
//						importe = importe * dias * 1.4 * 4;
//					} else {
//						importe = importe * dias * 4;
//					}
//					break;
//        	}

/**
 *  Mecanica anterior
 */
        	switch(turno) {
	    		case 11, 12:
	    			if (dias >= 5) {
	    				importe = importe * dias * 1.4;
	    			} else {
	    				importe = importe * dias;
	    			}
	    			break;
	    		case 13:
	    			if (dias >= 5) {
	    				importe = importe * dias * 1.4 * 2;
	    			} else {
	    				importe = importe * dias * 2;
	    			}
	    			break;
	    		case 21, 22, 23:
	    			if (dias >= 3) {
	    				importe = importe * dias * 1.4 * 2;
	    			} else {
	    				importe = importe * dias * 2;
	    			}
	    			break;
				case 31, 32:
					if (dias >= 4) {
						importe = importe * dias * 1.4 * 2;
					} else {
						importe = importe * dias * 2;
					}
					break;
				case 41, 42:
					if (dias >= 2) {
						importe = importe * dias * 1.4 * 4;
					} else {
						importe = importe * dias * 4;
					}
					break;
            }
        }
        return importe;
	}

	@Override
	public int GuardarSuplencia(DatosSuplencia suplencia, double importe, DatosEmpleado empleado) throws SQLException {
		suplencia.setImporte(importe);
		if( suplencia.getTipo_suplencia().equals(String.valueOf("SI"))) {
			return suplenciaRepository.save(suplencia, empleado);
		} else {
			return suplenciaRepository.saveExt(suplencia, empleado);
		}
	}

	@Override
	public void ActualizaImportesSuplencias(String fechaPago, String tipo) {
		List<DatosSuplencia> suplencias = null;

		if (tipo.equals(String.valueOf("SI"))) 
			suplencias = suplenciaRepository.ConsultaSuplenciasInternasXFecha(fechaPago);
		else
			suplencias = suplenciaRepository.ConsultaSuplenciasExternasXFecha(fechaPago);

		for(DatosSuplencia s:suplencias) {
			try {
				s.setRiesgos(empleadoRepository.ConsultaRiesgosEmp(s.getClave_empleado_suplir(), s.getFec_paga()));
			} catch(Exception ex) {
				s.setRiesgos(0);
			}
			try {
				//Primer metodo
				s.setImporte(this.CalculaImporteSuplencia(fechaPago, s.getClave_empleado_suplir(), s.getDias(), tipo, s.getRiesgos()));
			} catch(Exception ex) {
				s.setImporte((double) 0);
			}
			if (tipo.equals(String.valueOf("SI"))) {
				suplenciaRepository.updateImporteSuplencia(s);
			} else {
				suplenciaRepository.updateImporteSuplenciaExt(s);
			}
		}
	}

	@Override
	public void actualizaSuplencia(DatosSuplencia suplencia, double importe) {
		suplencia.setImporte(importe);
		if (suplencia.getTipo_suplencia().equals(String.valueOf("SI")))
			suplenciaRepository.updateSuplencia(suplencia);
		else
			suplenciaRepository.updateSuplenciaExt(suplencia);
	}

	@Override
	public void eliminarSuplencia(Integer id, String tipo) {
		if (tipo.equals(String.valueOf("SI")))
			suplenciaRepository.deleteSuplencia(id);
		else
			suplenciaRepository.deleteSuplenciaExt(id);
	}

	//Segundo metodo
	@Override
	public double CalculaImporteSuplencia(String quincena, DatosEmpleado empleado, int dias, String tipo, int riesgos) {
		//int riesgos = 0;
		double importe = 0.0;
		ValoresTabulador valores;
		FactoresSuplencia factor = new FactoresSuplencia();


		if (empleado.getId_tipo_tabulador().equals(String.valueOf("M"))) {
			valores = tabuladorRepository.ConsultaTabuladorMedico(empleado.getId_zona(), empleado.getId_nivel(), empleado.getId_sub_nivel(), empleado.getId_tipo_jornada(), quincena);
		} else {
			valores = tabuladorRepository.ConsultaTabuladorOperativo(empleado.getId_zona(), empleado.getId_nivel(), empleado.getId_sub_nivel(), empleado.getId_tipo_jornada(), quincena);
		}

		logger.info("Sueldo: " + valores.getSueldo());
	    logger.info("Asigna: " + valores.getAsignacion());
	    logger.info("Compen: " + valores.getCompen_un());

		//riesgos = empleadoRepository.ConsultaRiesgosEmp(empleado.getClave_empleado(), quincena);
		logger.info("Riesgos: " + riesgos);

		switch ( riesgos )
        {
	        case 10: importe = valores.getSueldo() * 1.1; break;
	        case 20: importe = valores.getSueldo() * 1.2; break;
	        default: importe = valores.getSueldo(); break;
        }

        if (tipo.equals(String.valueOf("SI"))) {
            switch (empleado.getId_tipo_tabulador())
            {
                case "M": importe = importe + valores.getAsignacion(); break;
                default: importe = importe + valores.getCompen_un(); break;
            }

            importe = (importe / 30);

            factor = suplenciaRepository.ConsultaFactoresSuplencia(empleado.getId_turno());

            //int diasAdic = 0;
            //diasAdic = (dias / factor.getFact_turno()) * 2; 
            //importe  = importe * (dias + diasAdic) * factor.getFact_jornada();

            if (dias >= factor.getFact_turno()) {
            	importe = importe * dias * factor.getFact_jornada();
            } else {
            	importe = importe * dias;
            }

        } else {

        	importe = (importe / 30);
        	logger.info("Importe /30: " + importe); 

        	int turno = Integer.valueOf(empleado.getId_turno());

        	logger.info("Factor turno: " + factor.getFact_turno() + ", Factor jornada: " + factor.getFact_jornada() + ", Turno: " + turno);

        	switch(turno) {
	    		case 11, 12:
	    			if (dias >= 5) {
	    				importe = importe * dias * 1.4;
	    			} else {
	    				importe = importe * dias;
	    			}
	    			break;
	    		case 13:
	    			if (dias >= 5) {
	    				importe = importe * dias * 1.4 * 2;
	    			} else {
	    				importe = importe * dias * 2;
	    			}
	    			break;
	    		case 41, 42:
	    			if (dias >= 2) {
	    				importe = importe * dias * 1.4 * 4;
	    			} else {
	    				importe = importe * dias * 4;
	    			}
	    			break;
	    		case 31, 32:
	    			if (dias >= 4) {
	    				importe = importe * dias * 1.4 * 2;
	    			} else {
	    				importe = importe * dias * 2;
	    			}
	    			break;
	    		case 21, 22, 23:
	    			if (dias >= 3) {
	    				importe = importe * dias * 1.4 * 2;
	    			} else {
	    				importe = importe * dias * 2;
	    			}
	    			break;
            }
        }
        return importe;
	}

	@Override
	public void ActualizaImportesSuplencias2(String fechaPago, String tipo) {
		List<DatosSuplencia> suplencias = null;

		if (tipo.equals(String.valueOf("SI"))) 
			suplencias = suplenciaRepository.ConsultaSuplenciasInternasXFecha(fechaPago);
		else
			suplencias = suplenciaRepository.ConsultaSuplenciasExternasXFecha(fechaPago);

		for(DatosSuplencia s:suplencias) {

			ValoresTabulador valores;
			DatosEmpleado empleado;

			try {
				s.setRiesgos(empleadoRepository.ConsultaRiesgosEmp(s.getClave_empleado_suplir(), s.getFec_paga()));
			} catch(Exception ex) {
				s.setRiesgos(0);
			}
			try {
				empleado = empleadoRepository.getDatosEmpleado(fechaPago, s.getClave_empleado_suplir());
				s.setEmpleado_suplir(empleado);
				s.setImporte(this.CalculaImporteSuplencia(fechaPago, empleado, s.getDias(), tipo, s.getRiesgos()));
			} catch(Exception ex) {
				s.setImporte((double) 0);
				continue;
			}
			try {
				if (empleado.getId_tipo_tabulador().equals(String.valueOf("M"))) {
					valores = tabuladorRepository.ConsultaTabuladorMedico(empleado.getId_zona(), empleado.getId_nivel(), empleado.getId_sub_nivel(), empleado.getId_tipo_jornada(), fechaPago);
				} else {
					valores = tabuladorRepository.ConsultaTabuladorOperativo(empleado.getId_zona(), empleado.getId_nivel(), empleado.getId_sub_nivel(), empleado.getId_tipo_jornada(), fechaPago);
				}
				s.setSueldo(valores.getSueldo());
			} catch(Exception ex) {
				s.setSueldo((double) 0);
			}
			if (tipo.equals(String.valueOf("SI"))) {
				//suplenciaRepository.updateImporteSuplencia(s);
				suplenciaRepository.updateSuplenciaIntVars(s);
			} else {
				suplenciaRepository.updateSuplenciaExtVars(s);
			}
		}
	}

}