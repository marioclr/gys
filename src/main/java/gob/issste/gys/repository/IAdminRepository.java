package gob.issste.gys.repository;

import java.util.List;

import gob.issste.gys.model.CifrasDeImpuestos;
import gob.issste.gys.model.CifrasImpuesto;

public interface IAdminRepository {

	public String STMT_CALCULA_ISR_GUA			= "Insert Into gys_externos_isr ( tipo, fec_pago, rfc, casos, percepciones, isr, id_clave_servicio, id_centro_trabajo )\r\n"
			   									+ "Select 'GE' tipo, fec_paga, rfc, COUNT(*) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr,\r\n"
			   									+ "  MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo\r\n"
			   									+ "From gys_guardias_ext\r\n"
			   									+ "Where fec_paga = ?\r\n"
			   									+ "Group By fec_paga, rfc";

	int calcula_isr_gua(String fec_pago);

	public String STMT_ELIMINA_CALCULO_ISR_GUA  = "Delete From gys_externos_isr Where fec_pago = ? And tipo = 'GE'";

	int elimina_calculo_isr_guardia(String fec_pago);

	public String STMT_CALCULA_ISR_SUP			= "Insert Into gys_externos_isr ( tipo, fec_pago, rfc, casos, casos, percepciones, isr )\r\n"
			   									+ "Select 'SE' tipo, fec_paga, rfc, COUNT(*) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr,\r\n"
			   									+ "  MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo\r\n"
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

	public String QUERY_CONSULTA_LAYOUT_SPEP    = "Select ( id_beneficiario || '|1|' || substr(rfc,1,4) || '|' || substr(rfc,5,6) || '|' || substr(rfc,11,3) || '|' ||\r\n"
												+ "         concepto || '|' || actividad_inst_ai || '|' || programa_presup_pp || '|' || ur || '|' || ct || '|' || aux || '|' ||\r\n"
												+ "         patidag || '|' || subpartg || '|' || tpg || '|NG|FOLIO QNA' || idDelegacion || '|' || importe) layout\r\n"
												+ "From (\r\n"
												+ "    Select Lpad(A.rfc, 13, ' ') rfc, Lpad(nombre, 30, ' ') nombre, Lpad(apellidopat, 30, ' ') apellidopat, Lpad(apellidomat, 30, ' ') apellidomat, \r\n"
												+ "      tipo, concepto, To_Char(concepto, '&&&&&&&') concepto, NVL(Lpad(id_beneficiario, 15, '000000000000000'), '000000000000000') id_beneficiario,\r\n"
												+ "      id_clave_servicio, id_centro_trabajo, actividad_inst_ai, programa_presup_pp,\r\n"
												+ "      B.idDelegacion, ur, ct, aux,\r\n"
												+ "      patidag, subpartg, tpg, importe\r\n"
												+ "    From (\r\n"
												+ "      Select 'P' tipo, rfc, '330' concepto, percepciones importe, id_clave_servicio, id_centro_trabajo\r\n"
												+ "      From gys_externos_isr\r\n"
												+ "      Where fec_pago = ? And tipo = ?\r\n"
												+ "      UNION\r\n"
												+ "      Select 'D' tipo, rfc, '290' concepto, isr importe, id_clave_servicio, id_centro_trabajo\r\n"
												+ "      From gys_externos_isr\r\n"
												+ "      Where fec_pago = ? And tipo = ?\r\n"
												+ "      Order By tipo desc, rfc\r\n"
												+ "    ) A, gys_bolsatrabajo B, m4t_cat_lay_spep C, m4t_conv_ct D\r\n"
												+ "    Where A.rfc = B.rfc\r\n"
												+ "      And A.id_clave_servicio = C.id_serviciog\r\n"
												+ "      And A.id_centro_trabajo = D.ct5\r\n"
												+ "    Order By rfc\r\n"
												+ ")";

	List<String> consultaLayoutSPEP(String fec_pago, String tipo);

	public String STMT_CALCULA_ISR_GUARIDAS_NON	= "Insert Into gys_externos_isr2 ( anio_ejercicio, mes_ejercicio, id_tipo_paga, tipo, rfc, casos, percepciones, isr, id_clave_servicio, id_centro_trabajo )\r\n"
												+ "Select anio_ejercicio, mes_ejercicio, id_tipo_paga, 'GE' tipo, rfc, COUNT(*) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr,\r\n"
												+ "  MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo\r\n"
												+ "From gys_guardias_ext G, gys_fechas_control F\r\n"
												+ "Where G.fec_paga = F.fec_pago\r\n"
												+ "  And anio_ejercicio = ?\r\n"
												+ "  And mes_ejercicio = ?\r\n"
												+ "  And id_tipo_paga = 1\r\n"
												+ "Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc";

	int calcula_isr_guardia_non(Integer anio, Integer mes);

	public String STMT_CALCULA_ISR_GUARIDAS_PAR	= "Insert Into gys_externos_isr2 ( anio_ejercicio, mes_ejercicio, id_tipo_paga, tipo, rfc, casos, percepciones, isr, id_clave_servicio, id_centro_trabajo )\r\n"
												+ "Select \r\n"
												+ "  A.anio_ejercicio, A.mes_ejercicio, A.id_tipo_paga, A.tipo, A.rfc, A.casos, A.percep, _get_ispt_(percep + NVL(percepciones, 0)) - NVL(N.isr, 0) isr, A.id_clave_servicio, A.id_centro_trabajo\r\n"
												+ "From (\r\n"
												+ "  Select anio_ejercicio, mes_ejercicio, id_tipo_paga, 'GE' tipo, rfc, COUNT(*) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr,\r\n"
												+ "    MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo\r\n"
												+ "  From gys_guardias_ext G, gys_fechas_control F\r\n"
												+ "  Where G.fec_paga = F.fec_pago\r\n"
												+ "    And anio_ejercicio = ?\r\n"
												+ "    And mes_ejercicio = ?\r\n"
												+ "    And id_tipo_paga = 1\r\n"
												+ "  Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ ") A\r\n"
												+ "Left Outer Join gys_externos_isr2 N\r\n"
												+ "  ON A.anio_ejercicio=N.anio_ejercicio\r\n"
												+ "    And A.mes_ejercicio=N.mes_ejercicio\r\n"
												+ "    And A.rfc=N.rfc\r\n"
												+ "    And N.id_tipo_paga=4";

	int calcula_isr_guardia_par(Integer anio, Integer mes);

	public String QUERY_GET_CIFRAS_ISR			= "Select id_tipo_paga, anio_ejercicio, mes_ejercicio, SUM(casos) casos, SUM(percepciones) percepciones, SUM(isr) isr\r\n"
												+ "From gys_externos_isr2\r\n"
												+ "Where anio_ejercicio = ?"
												+ "	 And mes_ejercicio = ?"
												+ "  And id_tipo_paga = ?"
												+ "  And tipo = ?"
												+ "Group By id_tipo_paga, anio_ejercicio, mes_ejercicio";

	CifrasDeImpuestos consultaCifrasDeImpuestos(Integer anio, Integer mes, Integer tipoPaga, String tipo);

//	public String QUERY_GET_ISR_GUARIDAS_PAR	= "Select id_tipo_paga, anio_ejercicio, mes_ejercicio, SUM(casos) casos, SUM(percepciones) percepciones, SUM(isr) isr\r\n"
//												+ "From gys_externos_isr2\r\n"
//												+ "Where anio_ejercicio = ?"
//												+ "	 And mes_ejercicio = ?"
//												+ "  And id_tipo_paga = ?";
//
//	CifrasDeImpuestos existe_calculo_isr_par(Integer anio, Integer mes, Integer tipo);

	public String STMT_CALCULA_ISR_SUPLENC_NON	= "Insert Into gys_externos_isr2 ( anio_ejercicio, mes_ejercicio, id_tipo_paga, tipo, rfc, casos, percepciones, isr, id_clave_servicio, id_centro_trabajo )\r\n"
												+ "Select anio_ejercicio, mes_ejercicio, id_tipo_paga, 'GE' tipo, rfc, COUNT(*) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr,\r\n"
												+ "  MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo\r\n"
												+ "From gys_suplencias_ext G, gys_fechas_control F\r\n"
												+ "Where G.fec_paga = F.fec_pago\r\n"
												+ "  And anio_ejercicio = ?\r\n"
												+ "  And mes_ejercicio = ?\r\n"
												+ "  And id_tipo_paga = 1\r\n"
												+ "Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc";

	int calcula_isr_suplencia_non(Integer anio, Integer mes);

	public String STMT_CALCULA_ISR_SUPLENC_PAR	= "Insert Into gys_externos_isr2 ( anio_ejercicio, mes_ejercicio, id_tipo_paga, tipo, rfc, casos, percepciones, isr, id_clave_servicio, id_centro_trabajo )\r\n"
												+ "Select \r\n"
												+ "  A.anio_ejercicio, A.mes_ejercicio, A.id_tipo_paga, A.tipo, A.rfc, A.casos, A.percep, _get_ispt_(percep + NVL(percepciones, 0)) - NVL(N.isr, 0) isr, A.id_clave_servicio, A.id_centro_trabajo\r\n"
												+ "From (\r\n"
												+ "  Select anio_ejercicio, mes_ejercicio, id_tipo_paga, 'GE' tipo, rfc, COUNT(*) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr,\r\n"
												+ "    MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo\r\n"
												+ "  From gys_suplencias_ext G, gys_fechas_control F\r\n"
												+ "  Where G.fec_paga = F.fec_pago\r\n"
												+ "    And anio_ejercicio = ?\r\n"
												+ "    And mes_ejercicio = ?\r\n"
												+ "    And id_tipo_paga = 1\r\n"
												+ "  Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ ") A\r\n"
												+ "Left Outer Join gys_externos_isr2 N\r\n"
												+ "  ON A.anio_ejercicio=N.anio_ejercicio\r\n"
												+ "    And A.mes_ejercicio=N.mes_ejercicio\r\n"
												+ "    And A.rfc=N.rfc\r\n"
												+ "    And N.id_tipo_paga=4";

	int calcula_isr_suplencia_par(Integer anio, Integer mes);

	public String STMT_ELIMINA_CALCULO_ISR  	= "Delete From gys_externos_isr2 Where anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = ? And tipo = ?";

	int elimina_cifras_impuesto(Integer anio, Integer mes, Integer tipoPaga, String tipo);

}