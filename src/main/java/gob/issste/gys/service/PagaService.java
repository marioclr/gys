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

	public boolean validaPagasAlCrear(Paga paga) {
		if (pagaRepository.existe_abierta(paga)>0)
			return true;
		else
			return false;
	}

	public boolean validaPagasAlAbrir(Paga paga) {
		if (pagaRepository.existe_abierta_al_cambiar(paga)>0)
			return true;
		else
			return false;
	}

	public boolean validaPagasAlCerrar(Paga paga) {
		if (pagaRepository.existe_abierta_al_cambiar(paga)>0)
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
		if (pagaRepository.existe_fecha_en_pagas(paga)>0)
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

}