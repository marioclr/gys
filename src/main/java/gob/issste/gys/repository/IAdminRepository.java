package gob.issste.gys.repository;

import java.util.List;

import gob.issste.gys.model.CifrasImpuesto;

public interface IAdminRepository {

	public String STMT_CALCULA_ISR_GUA			= "Insert Into gys_externos_isr ( tipo, fec_pago, rfc, casos, percepciones, isr )\r\n"
			   									+ "Select 'GE' tipo, fec_paga, rfc, COUNT(*) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr\r\n"
			   									+ "From gys_guardias_ext\r\n"
			   									+ "Where fec_paga = ?\r\n"
			   									+ "Group By fec_paga, rfc";

	int calcula_isr_gua(String fec_pago);

	public String STMT_ELIMINA_CALCULO_ISR_GUA  = "Delete From gys_externos_isr Where fec_pago = ? And tipo = 'GE'";

	int elimina_calculo_isr_guardia(String fec_pago);

	public String STMT_CALCULA_ISR_SUP			= "Insert Into gys_externos_isr ( tipo, fec_pago, rfc, casos, percepciones, isr, casos )\r\n"
			   									+ "Select 'SE' tipo, fec_paga, rfc, COUNT(*) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr\r\n"
			   									+ "From gys_suplencias_ext\r\n"
			   									+ "Where fec_paga = ?\r\n"
			   									+ "Group By fec_paga, rfc";

	int calcula_isr_sup(String fec_pago);

	public String STMT_ELIMINA_CALCULO_ISR_SUP  = "Delete From gys_externos_isr Where fec_pago = ? And tipo = 'SE'";

	int elimina_calculo_isr_suplencia(String fec_pago);

	public String QUERY_CONSULTA_CIFRAS_ISR      = "Select tipo, fec_pago, SUM(casos) casos, SUM(percepciones) percepciones, SUM(isr) isr\r\n"
												+ "From gys_externos_isr\r\n"
												+ "Where fec_pago = ?\r\n"
												+ "  And tipo = ?\r\n"
												+ "Group By tipo, fec_pago";

	CifrasImpuesto consultaCifras(String fec_pago, String tipo);

	public String QUERY_CONSULTA_LAYOUT_SPEP    = "Select (Lpad(rfc, 13, ' ') || Lpad(nombre, 30, ' ') || Lpad(apellidopat, 30, ' ') || Lpad(apellidomat, 30, ' ') || tipo || concepto || id_beneficiario) layout\r\n"
												+ "From (\r\n"
												+ "    Select Lpad(A.rfc, 13, ' ') rfc, Lpad(nombre, 30, ' ') nombre, Lpad(apellidopat, 30, ' ') apellidopat, Lpad(apellidomat, 30, ' ') apellidomat, \r\n"
												+ "      tipo, concepto, To_Char(concepto, '&&&&&&&') concepto, NVL(Lpad(id_beneficiario, 15, '000000000000000'), '000000000000000') id_beneficiario\r\n"
												+ "    From (\r\n"
												+ "      Select 'P' tipo, rfc, percepciones concepto\r\n"
												+ "      From gys_externos_isr\r\n"
												+ "      Where fec_pago = ? And tipo = ?\r\n"
												+ "      UNION\r\n"
												+ "      Select 'D' tipo, rfc, isr concepto\r\n"
												+ "      From gys_externos_isr\r\n"
												+ "      Where fec_pago = ? And tipo = ?\r\n"
												+ "      Order By tipo desc, rfc\r\n"
												+ "    ) A, gys_bolsatrabajo B\r\n"
												+ "    Where A.rfc = B.rfc\r\n"
												+ "    Order By rfc\r\n"
												+ ") ";

	List<String> consultaLayoutSPEP(String fec_pago, String tipo);

}