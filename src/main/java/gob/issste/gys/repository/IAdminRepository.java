package gob.issste.gys.repository;

import gob.issste.gys.model.CifrasImpuesto;

public interface IAdminRepository {

	public String STMT_CALCULA_ISR_GUA			= "Insert Into gys_externos_isr ( tipo, fec_pago, rfc, percepciones, isr, casos )\r\n"
			   									+ "Select 'GE' tipo, fec_paga, rfc, COUNT(*) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr\r\n"
			   									+ "From gys_guardias_ext\r\n"
			   									+ "Where fec_paga = ?\r\n"
			   									+ "Group By fec_paga, rfc";

	int calcula_isr_gua(String fec_pago);

	public String STMT_ELIMINA_CALCULO_ISR_GUA  = "Delete From gys_externos_isr Where fec_paga = ? And tipo = 'GE'";

	int elimina_calculo_isr_guardia(String fec_pago);

	public String STMT_CALCULA_ISR_SUP			= "Insert Into gys_externos_isr ( tipo, fec_pago, rfc, percepciones, isr, casos )\r\n"
			   									+ "Select 'SE' tipo, fec_paga, rfc, COUNT(*) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr\r\n"
			   									+ "From gys_suplencias_ext\r\n"
			   									+ "Where fec_paga = ?\r\n"
			   									+ "Group By fec_paga, rfc";

	int calcula_isr_sup(String fec_pago);

	public String STMT_ELIMINA_CALCULO_ISR_SUP  = "Delete From gys_externos_isr Where fec_paga = ? And tipo = 'SE'";

	int elimina_calculo_isr_suplencia(String fec_pago);

	public String STMT_CONSULTA_CIFRAS_ISR      = "Select tipo, fec_pago, SUM(casos) casos, SUM(percepciones) percepciones, SUM(isr) isr\r\n"
												+ "From gys_externos_isr\r\n"
												+ "Where fec_pago = ?\r\n"
												+ "  And tipo = ?\r\n"
												+ "Group By tipo, fec_pago";

	CifrasImpuesto consultaCifras(String fec_pago, String tipo);
}
