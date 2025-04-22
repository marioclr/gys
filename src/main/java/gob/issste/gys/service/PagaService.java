package gob.issste.gys.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gob.issste.gys.JdbcTemplateDemo01Application;
import gob.issste.gys.model.Paga;
import gob.issste.gys.repository.IPagaRepository;

@Service
public class PagaService {

	Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);
	
	@Autowired
	IPagaRepository pagaRepository;

	// Validaciones al cambiar a un estatus diferente

	public boolean validaPagasAbiertasAlCrear(Paga paga) {
		if (pagaRepository.existe_abierta(paga)>0)
			return true;
		else
			return false;
	}

	public boolean existe_anterior_sin_terminar(Paga paga) {
		boolean resp = false;
		switch (paga.getId_tipo_paga()) {
			case 4:
				if (pagaRepository.existe_anterior_sin_terminar_non(paga)>0)
					resp = true;
				else
					resp = false;
				break;
			case 1:
				if (pagaRepository.existe_anterior_sin_terminar_par(paga)>0)
					resp = true;
				else
					resp = false;
				break;
		}
		return resp;
	}

	public boolean validaPagasAlAbrir(Paga paga) {
		if (pagaRepository.existe_abierta_al_cambiar(paga)>0)
			return true;
		else
			return false;
	}

	public boolean validaPagasAlCerrar(Paga paga) {
//		if (pagaRepository.existe_abierta_al_cambiar(paga)>0)
//			return true;
//		else
//			return false;
		return false;
	}

	public boolean validaPagasAlComplementar(String fecPaga, Integer anio, Integer mes, Integer tipoFechaControl, Integer id_ordinal) {
		if (pagaRepository.existe_fecha_en_calculo_isr(fecPaga, anio, mes, tipoFechaControl, id_ordinal)>0)
			return true;
		else
			return false;
	}

	public boolean existe_fecha_en_isr(Paga paga) {
		if (pagaRepository.existe_fecha_en_isr(paga)>0)
			return true;
		else
			return false;
	}

	public boolean existe_fecha_en_paga(Paga paga) {
		if (pagaRepository.existe_fecha_post_en_pagas(paga)>0)
			return true;
		else
			return false;
	}

	boolean validaPagasAlCalcularISR(Paga paga) {
		return false;
	}

	boolean validaPagasAlCalcularPensiones(Paga paga) {
		return false;
	}

	boolean validaPagasAlGenerarLayoutSPEP(Paga paga) {
		return false;
	}

	public boolean validaSigueAbierta(String fecha) {
		int i = pagaRepository.verifica_paga_cerrada(fecha);
		
		if (pagaRepository.verifica_paga_cerrada(fecha)>0)
			return false;
		else
			return true;
	}

	public boolean validaSigueAbiertaDeleg(int idFecha, String idDeleg){
//		int i = pagaRepository.verifica_paga_cerrada_deleg(fecha, idDeleg);

		if (pagaRepository.verifica_paga_cerrada_deleg(idFecha, idDeleg) > 0)
			return false;
		else
			return true;
	}

    public int changeEstatusByDeleg(int idFecha, String idDeleg, int estatus) {
		return pagaRepository.changeEstatusByIdDeleg(idFecha, idDeleg, estatus);
    }

	public int changeEstatusForAllDelegByDate(int idFecha, int estatus) {
		return pagaRepository.changeEstatusForAllDelegByDate(idFecha, estatus);
	}
}