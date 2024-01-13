package gob.issste.gys.repository;

import java.util.List;

import gob.issste.gys.model.CifrasDeImpuestos;
import gob.issste.gys.model.CifrasDeImpuestosPorRepresentacion;
import gob.issste.gys.model.DetalleCifrasDeImpuestos;
import gob.issste.gys.model.DetalleCifrasDeImpuestosConPA;
import gob.issste.gys.model.DetalleCifrasDeImpuestosPorRep;
import gob.issste.gys.model.LayoutSpep;

public interface IAdminRepository {

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

	public String QUERY_CONSULTA_LAYOUT_SPEP_ORD = "Select A.rfc, A.nombre, A.apellidoPat, A.apellidoMat, A.id_beneficiario,\r\n"
												+ "  B.percepciones, B.isr, B.pension_1, B.pension_2, B.pension_3, B.pension_4,\r\n"
												+ "  B.guardia, B.id_clave_servicio_g, B.id_centro_trabajo_g,\r\n"
												+ "  B.suplencia, B.id_clave_servicio_s, B.id_centro_trabajo_s\r\n"
												+ "From (\r\n"
												+ "  Select rfc, MAX(nombre) nombre, MAX(apellidopat) apellidopat, MAX(apellidomat) apellidomat, MAX(iddelegacion) iddelegacion,\r\n"
												+ "    MAX(codigo_postal) codigo_postal, MAX(curp) curp, MAX(id_beneficiario) id_beneficiario\r\n"
												+ "  From gys_bolsatrabajo\r\n"
												+ "  Group By rfc\r\n"
												+ ") A, (\r\n"
												+ "  Select anio_ejercicio, mes_ejercicio, id_tipo_paga, id_ordinal, tipo, IMP.rfc, percepciones, isr, casos,\r\n"
												+ "      pension_1, pension_2, pension_3, pension_4, \r\n"
												+ "      SUM(NVL(GE.casos_gua,0)) casos_gua, SUM(NVL(guardia,0)) guardia, MAX(serv_gua) id_clave_servicio_g, MAX(ct_gua) id_centro_trabajo_g,\r\n"
												+ "      SUM(NVL(SE.casos_sup,0)) casos_sup, SUM(NVL(suplencia,0)) suplencia, MAX(serv_sup) id_clave_servicio_s, MAX(ct_sup) id_centro_trabajo_s\r\n"
												+ "  From gys_externos_isr2 IMP\r\n"
												+ "  Left Join (\r\n"
												+ "      Select rfc, fec_paga, NVL(COUNT(*), 0) casos_gua, NVL(SUM(importe), 0) guardia, MAX(id_clave_servicio) serv_gua, MAX(id_centro_trabajo) ct_gua\r\n"
												+ "      From gys_guardias_ext\r\n"
												+ "      Group By rfc, fec_paga\r\n"
												+ "  ) GE\r\n"
												+ "  ON IMP.rfc = GE.rfc\r\n"
												+ "    And GE.fec_paga Between IMP.fec_min And IMP.fec_max\r\n"
												+ "  Left Join (\r\n"
												+ "      Select rfc, fec_paga, NVL(COUNT(*), 0) casos_sup, NVL(SUM(importe), 0) suplencia, MAX(id_clave_servicio) serv_sup, MAX(id_centro_trabajo) ct_sup\r\n"
												+ "      From gys_suplencias_ext\r\n"
												+ "      Group By rfc, fec_paga\r\n"
												+ "  ) SE\r\n"
												+ "  ON IMP.rfc = SE.rfc\r\n"
												+ "    And SE.fec_paga Between IMP.fec_min And IMP.fec_max\r\n"
												+ "  Where anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = ? And id_ordinal = ?\r\n"
												+ "  Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, id_ordinal, tipo, IMP.rfc, percepciones, isr, casos,\r\n"
												+ "      pension_1,pension_2,pension_3,pension_4\r\n"
												+ ") B\r\n"
												+ "Where A.rfc = B.rfc";

	List<LayoutSpep> consultaLayoutSPEP(Integer anio, Integer mes, Integer tipoPaga, Integer id_ordinal);

	public String QUERY_CONSULTA_LAYOUT_SPEP_REP = "Select A.rfc, A.nombre, A.apellidoPat, A.apellidoMat, A.id_beneficiario,\r\n"
												+ "  B.percepciones, B.isr, B.pension_1, B.pension_2, B.pension_3, B.pension_4,\r\n"
												+ "  B.id_representacion, B.n_representacion, B.id_centro_trabajo, B.n_centro_trabajo, B.id_clave_servicio, \r\n"
												+ "  B.guardia, B.id_clave_servicio_g, B.id_puesto_plaza_g,\r\n"
												+ "  B.suplencia, B.id_clave_servicio_s, B.id_puesto_plaza_s\r\n"
												+ "From (\r\n"
												+ "  Select rfc, MAX(nombre) nombre, MAX(apellidopat) apellidopat, MAX(apellidomat) apellidomat, MAX(iddelegacion) iddelegacion,\r\n"
												+ "    MAX(codigo_postal) codigo_postal, MAX(curp) curp, MAX(id_beneficiario) id_beneficiario\r\n"
												+ "  From gys_bolsatrabajo\r\n"
												+ "  Group By rfc\r\n"
												+ ") A, (\r\n"
												+ "  Select anio_ejercicio, mes_ejercicio, id_tipo_paga, id_ordinal, tipo, IMP.rfc, percepciones, isr, casos,\r\n"
												+ "      pension_1, pension_2, pension_3, pension_4, IMP.id_centro_trabajo, n_centro_trabajo, \r\n"
												+ "      B.id_delegacion id_representacion, C.n_div_geografica n_representacion, IMP.id_clave_servicio,\r\n"
												+ "      SUM(NVL(GE.casos_gua,0)) casos_gua, SUM(NVL(guardia,0)) guardia, MAX(NVL(serv_gua,'')) id_clave_servicio_g, MAX(NVL(puesto_gua,'')) id_puesto_plaza_g,\r\n"
												+ "      SUM(NVL(SE.casos_sup,0)) casos_sup, SUM(NVL(suplencia,0)) suplencia, MAX(NVL(serv_sup,'')) id_clave_servicio_s, MAX(NVL(puesto_sup,'')) id_puesto_plaza_s\r\n"
												+ "  From gys_externos_isr2 IMP\r\n"
												+ "  Inner Join m4t_centros_trab B\r\n"
												+ "  ON IMP.id_centro_trabajo = B.id_centro_trabajo\r\n"
												+ "  Inner Join m4t_delegaciones C\r\n"
												+ "  ON B.id_delegacion = C.id_div_geografica\r\n"
												+ "  Left Join (\r\n"
												+ "      Select rfc, fec_paga, NVL(COUNT(*), 0) casos_gua, NVL(SUM(importe), 0) guardia, NVL(MAX(id_clave_servicio),'') serv_gua, NVL(MAX(id_puesto_plaza),'') puesto_gua\r\n"
												+ "      From gys_guardias_ext\r\n"
												+ "      Group By rfc, fec_paga\r\n"
												+ "  ) GE\r\n"
												+ "  ON IMP.rfc = GE.rfc\r\n"
												+ "    And GE.fec_paga Between IMP.fec_min And IMP.fec_max\r\n"
												+ "  Left Join (\r\n"
												+ "      Select rfc, fec_paga, NVL(COUNT(*), 0) casos_sup, NVL(SUM(importe), 0) suplencia, NVL(MAX(id_clave_servicio),'') serv_sup, NVL(MAX(id_puesto_plaza),'') puesto_sup\r\n"
												+ "      From gys_suplencias_ext\r\n"
												+ "      Group By rfc, fec_paga\r\n"
												+ "  ) SE\r\n"
												+ "  ON IMP.rfc = SE.rfc\r\n"
												+ "    And SE.fec_paga Between IMP.fec_min And IMP.fec_max \r\n"
												+ "  Where anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = ? And id_ordinal = ? And B.id_delegacion = ?\r\n"
												+ "  Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, id_ordinal, tipo, IMP.rfc, percepciones, isr, casos,\r\n"
												+ "      pension_1,pension_2,pension_3,pension_4, IMP.id_centro_trabajo, n_centro_trabajo, B.id_delegacion, C.n_div_geografica, IMP.id_clave_servicio\r\n"
												+ ") B\r\n"
												+ "Where A.rfc = B.rfc \r\n"
												+ "Order By B.id_representacion, B.id_centro_trabajo, A.rfc";

	List<LayoutSpep> consultaLayoutSPEP_X_Rep(Integer anio, Integer mes, Integer tipoPaga, Integer id_ordinal, String id_rep);

	public String STMT_CALCULA_ISR_GUARIDAS_NON	= "Insert Into gys_externos_isr2 ( anio_ejercicio, mes_ejercicio, id_tipo_paga, id_ordinal,\r\n"
												+ "tipo, rfc, casos, percepciones, isr, id_clave_servicio, id_centro_trabajo, fec_min, fec_max )\r\n"
												+ "Select anio_ejercicio, mes_ejercicio, id_tipo_paga,\r\n"
												+ "(Select NVL(MAX(id_ordinal),0) + 1 From gys_externos_isr2 Where anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = 4 And tipo = 'GE'),"
												+ "'GE' tipo, rfc, COUNT(*) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr,\r\n"
												+ "  MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(F.fec_pago) fec_min,  MAX(F.fec_pago) fec_max\r\n"
												+ "From gys_guardias_ext G, gys_fechas_control F\r\n"
												+ "Where G.fec_paga = F.fec_pago\r\n"
												+ "  And anio_ejercicio = ?\r\n"
												+ "  And mes_ejercicio = ?\r\n"
												+ "  And id_tipo_paga = 4\r\n"
												+ "  And F.estatus = 2\r\n"
												+ "Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc";

	int calcula_isr_guardia_non(Integer anio, Integer mes);

	public String STMT_CALCULA_ISR_NON			= "Insert Into gys_externos_isr2 ( anio_ejercicio, mes_ejercicio, id_tipo_paga, id_ordinal,\r\n"
												+ "tipo, rfc, casos, percepciones, isr, id_clave_servicio, id_centro_trabajo, fec_min, fec_max )\r\n"
												+ "Select anio_ejercicio, mes_ejercicio, id_tipo_paga,\r\n"
												+ "(Select NVL(MAX(id_ordinal),0) + 1 From gys_externos_isr2 Where anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = 4) id_ordinal,\r\n"
												+ "'DOS' tipo, rfc, SUM(casos) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr,\r\n"
												+ "MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(fec_min) fec_min,  MAX(fec_max) fec_max\r\n"
												+ "From (\r\n"
												+ "  Select\r\n"
												+ "    anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc, COUNT(*) casos, SUM(importe) importe,\r\n"
												+ "    MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(F.fec_pago) fec_min,  MAX(F.fec_pago) fec_max\r\n"
												+ "  From gys_guardias_ext G, gys_fechas_control F\r\n"
												+ "  Where G.fec_paga = F.fec_pago\r\n"
												+ "    And anio_ejercicio = ?\r\n"
												+ "    And mes_ejercicio = ?\r\n"
												+ "    And id_tipo_paga = 4\r\n"
												+ "    And F.estatus = 3\r\n"
												+ "  Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ "  UNION ALL\r\n"
												+ "  Select\r\n"
												+ "    anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc, COUNT(*) casos, SUM(importe) importe,\r\n"
												+ "    MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(F.fec_pago) fec_min,  MAX(F.fec_pago) fec_max\r\n"
												+ "  From gys_suplencias_ext S, gys_fechas_control F\r\n"
												+ "  Where S.fec_paga = F.fec_pago\r\n"
												+ "    And anio_ejercicio = ?\r\n"
												+ "    And mes_ejercicio = ?\r\n"
												+ "    And id_tipo_paga = 4\r\n"
												+ "    And F.estatus = 3\r\n"
												+ "  Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ ") Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc";

	int calcula_isr_non(Integer anio, Integer mes);

	public String STMT_CALCULA_ISR_PAR			= "Insert Into gys_externos_isr2 ( anio_ejercicio, mes_ejercicio, id_tipo_paga, id_ordinal,\r\n"
												+ "tipo, rfc, casos, percepciones, isr, id_clave_servicio, id_centro_trabajo, fec_min, fec_max )\r\n"
												+ "Select \r\n"
												+ "  A.anio_ejercicio, A.mes_ejercicio, A.id_tipo_paga, \r\n"
												+ "  (Select NVL(MAX(id_ordinal),0) + 1 From gys_externos_isr2 Where anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = 1) id_ordinal,\r\n"
												+ "  'DOS' tipo, A.rfc, SUM(A.casos) casos, SUM(A.percep) percep, SUM(_get_ispt_(percep + NVL(percepciones, 0)) - NVL(N.isr, 0)) isr, \r\n"
												+ "  MAX(A.id_clave_servicio) id_clave_servicio, MAX(A.id_centro_trabajo) id_centro_trabajo, MIN(fec_min) fec_min,  MAX(fec_max) fec_max\r\n"
												+ "From (\r\n"
												+ "  Select anio_ejercicio, mes_ejercicio, id_tipo_paga,\r\n"
												+ "    'DOS' tipo, rfc, SUM(casos) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr,\r\n"
												+ "    MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(fec_min) fec_min,  MAX(fec_max) fec_max\r\n"
												+ "  From (\r\n"
												+ "    Select\r\n"
												+ "      anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc, COUNT(*) casos, SUM(importe) importe,\r\n"
												+ "      MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(F.fec_pago) fec_min,  MAX(F.fec_pago) fec_max\r\n"
												+ "    From gys_guardias_ext G, gys_fechas_control F\r\n"
												+ "    Where G.fec_paga = F.fec_pago\r\n"
												+ "      And anio_ejercicio = ?\r\n"
												+ "      And mes_ejercicio = ?\r\n"
												+ "      And id_tipo_paga = 1\r\n"
												+ "      And F.estatus = 3\r\n"
												+ "    Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ "    UNION ALL\r\n"
												+ "    Select\r\n"
												+ "      anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc, COUNT(*) casos, SUM(importe) importe,\r\n"
												+ "      MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(F.fec_pago) fec_min,  MAX(F.fec_pago) fec_max\r\n"
												+ "    From gys_suplencias_ext S, gys_fechas_control F\r\n"
												+ "    Where S.fec_paga = F.fec_pago\r\n"
												+ "      And anio_ejercicio = ?\r\n"
												+ "      And mes_ejercicio = ?\r\n"
												+ "      And id_tipo_paga = 1\r\n"
												+ "      And F.estatus = 3\r\n"
												+ "    Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ "  ) Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ ") A Left Outer Join (\r\n"
												+ "  Select anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc, SUM(percepciones) percepciones, SUM(isr) isr\r\n"
												+ "  From gys_externos_isr2\r\n"
												+ "  Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ ") N\r\n"
												+ "  ON A.anio_ejercicio=N.anio_ejercicio\r\n"
												+ "    And A.mes_ejercicio=N.mes_ejercicio\r\n"
												+ "    And A.rfc=N.rfc\r\n"
												+ "    And N.id_tipo_paga=4\r\n"
												+ "Group By A.anio_ejercicio, A.mes_ejercicio, A.id_tipo_paga, A.rfc";

	int calcula_isr_par(Integer anio, Integer mes);

	public String STMT_RE_CALCULA_ISR_NON		= "Insert Into gys_externos_isr2 ( anio_ejercicio, mes_ejercicio, id_tipo_paga, id_ordinal,\r\n"
												+ "tipo, rfc, casos, percepciones, isr, id_clave_servicio, id_centro_trabajo, fec_min, fec_max )\r\n"
												+ "Select anio_ejercicio, mes_ejercicio, id_tipo_paga,\r\n"
												+ "(Select NVL(MAX(id_ordinal),0) + 1 From gys_externos_isr2 Where anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = 4) id_ordinal,\r\n"
												+ "'DOS' tipo, rfc, SUM(casos) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr,\r\n"
												+ "MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(fec_min) fec_min,  MAX(fec_max) fec_max\r\n"
												+ "From (\r\n"
												+ "  Select\r\n"
												+ "    anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc, COUNT(*) casos, SUM(importe) importe,\r\n"
												+ "    MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(F.fec_pago) fec_min,  MAX(F.fec_pago) fec_max\r\n"
												+ "  From gys_guardias_ext G, gys_fechas_control F\r\n"
												+ "  Where G.fec_paga = F.fec_pago\r\n"
												+ "    And anio_ejercicio = ?\r\n"
												+ "    And mes_ejercicio = ?\r\n"
												+ "    And id_tipo_paga = 4\r\n"
												+ "    And F.fec_pago between ? And ?\r\n"
												+ "  Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ "  UNION ALL\r\n"
												+ "  Select\r\n"
												+ "    anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc, COUNT(*) casos, SUM(importe) importe,\r\n"
												+ "    MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(F.fec_pago) fec_min,  MAX(F.fec_pago) fec_max\r\n"
												+ "  From gys_suplencias_ext S, gys_fechas_control F\r\n"
												+ "  Where S.fec_paga = F.fec_pago\r\n"
												+ "    And anio_ejercicio = ?\r\n"
												+ "    And mes_ejercicio = ?\r\n"
												+ "    And id_tipo_paga = 4\r\n"
												+ "    And F.fec_pago between ? And ?\r\n"
												+ "  Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ ") Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc";

	int re_calcula_isr_non(Integer anio, Integer mes, String fec_min, String fec_max);

	public String STMT_RE_CALCULA_ISR_PAR		= "Insert Into gys_externos_isr2 ( anio_ejercicio, mes_ejercicio, id_tipo_paga, id_ordinal,\r\n"
												+ "tipo, rfc, casos, percepciones, isr, id_clave_servicio, id_centro_trabajo, fec_min, fec_max )\r\n"
												+ "Select \r\n"
												+ "  A.anio_ejercicio, A.mes_ejercicio, A.id_tipo_paga, \r\n"
												+ "  (Select NVL(MAX(id_ordinal),0) + 1 From gys_externos_isr2 Where anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = 1) id_ordinal,\r\n"
												+ "  'DOS' tipo, A.rfc, SUM(A.casos) casos, SUM(A.percep) percep, SUM(_get_ispt_(percep + NVL(percepciones, 0)) - NVL(N.isr, 0)) isr, \r\n"
												+ "  MAX(A.id_clave_servicio) id_clave_servicio, MAX(A.id_centro_trabajo) id_centro_trabajo, MIN(fec_min) fec_min,  MAX(fec_max) fec_max\r\n"
												+ "From (\r\n"
												+ "  Select anio_ejercicio, mes_ejercicio, id_tipo_paga,\r\n"
												+ "    'DOS' tipo, rfc, SUM(casos) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr,\r\n"
												+ "    MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(fec_min) fec_min,  MAX(fec_max) fec_max\r\n"
												+ "  From (\r\n"
												+ "    Select\r\n"
												+ "      anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc, COUNT(*) casos, SUM(importe) importe,\r\n"
												+ "      MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(F.fec_pago) fec_min,  MAX(F.fec_pago) fec_max\r\n"
												+ "    From gys_guardias_ext G, gys_fechas_control F\r\n"
												+ "    Where G.fec_paga = F.fec_pago\r\n"
												+ "      And anio_ejercicio = ?\r\n"
												+ "      And mes_ejercicio = ?\r\n"
												+ "      And id_tipo_paga = 1\r\n"
												+ "      And F.fec_pago between ? And ?\r\n"
												+ "    Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ "    UNION ALL\r\n"
												+ "    Select\r\n"
												+ "      anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc, COUNT(*) casos, SUM(importe) importe,\r\n"
												+ "      MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(F.fec_pago) fec_min,  MAX(F.fec_pago) fec_max\r\n"
												+ "    From gys_suplencias_ext S, gys_fechas_control F\r\n"
												+ "    Where S.fec_paga = F.fec_pago\r\n"
												+ "      And anio_ejercicio = ?\r\n"
												+ "      And mes_ejercicio = ?\r\n"
												+ "      And id_tipo_paga = 1\r\n"
												+ "      And F.fec_pago between ? And ?\r\n"
												+ "    Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ "  ) Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ ") A Left Outer Join (\r\n"
												+ "  Select anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc, SUM(percepciones) percepciones, SUM(isr) isr\r\n"
												+ "  From gys_externos_isr2\r\n"
												+ "  Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ ") N\r\n"
												+ "  ON A.anio_ejercicio=N.anio_ejercicio\r\n"
												+ "    And A.mes_ejercicio=N.mes_ejercicio\r\n"
												+ "    And A.rfc=N.rfc\r\n"
												+ "    And N.id_tipo_paga=4\r\n"
												+ "Group By A.anio_ejercicio, A.mes_ejercicio, A.id_tipo_paga, A.rfc";

	int re_calcula_isr_par(Integer anio, Integer mes, String fec_min, String fec_max);

	public String STMT_CALCULA_ISR_SUPLENC_NON	= "Insert Into gys_externos_isr2 ( anio_ejercicio, mes_ejercicio, id_tipo_paga, id_ordinal,\r\n"
												+ "tipo, rfc, casos, percepciones, isr, id_clave_servicio, id_centro_trabajo, fec_min, fec_max )\r\n"
												+ "Select anio_ejercicio, mes_ejercicio, id_tipo_paga,\r\n"
												+ "(Select NVL(MAX(id_ordinal),0) + 1 From gys_externos_isr2 Where anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = 4 And tipo = 'SE'),"
												+ "'SE' tipo, rfc, COUNT(*) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr,\r\n"
												+ "  MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(F.fec_pago) fec_min,  MAX(F.fec_pago) fec_max\r\n"
												+ "From gys_suplencias_ext G, gys_fechas_control F\r\n"
												+ "Where G.fec_paga = F.fec_pago\r\n"
												+ "  And anio_ejercicio = ?\r\n"
												+ "  And mes_ejercicio = ?\r\n"
												+ "  And id_tipo_paga = 4\r\n"
												+ "  And F.estatus = 2\r\n"
												+ "Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc";

	int calcula_isr_suplencia_non(Integer anio, Integer mes);

	public String STMT_RECALCULA_ISR_GUARIDAS_NON = "Insert Into gys_externos_isr2 ( anio_ejercicio, mes_ejercicio, id_tipo_paga, tipo, rfc, casos, percepciones, isr, id_clave_servicio, id_centro_trabajo, fec_min, fec_max )\r\n"
												+ "Select anio_ejercicio, mes_ejercicio, id_tipo_paga, 'GE' tipo, rfc, COUNT(*) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr,\r\n"
												+ "  MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(F.fec_pago) fec_min,  MAX(F.fec_pago) fec_max\r\n"
												+ "From gys_guardias_ext G, gys_fechas_control F\r\n"
												+ "Where G.fec_paga = F.fec_pago\r\n"
												+ "  And anio_ejercicio = ?\r\n"
												+ "  And mes_ejercicio = ?\r\n"
												+ "  And id_tipo_paga = 4\r\n"
												+ "  And F.fec_pago between ? And ?\r\n"
												+ "Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc";

	int re_calcula_isr_guardia_non(Integer anio, Integer mes, String fec_min, String fec_max);

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
												+ "    And F.estatus = 2\r\n"
												+ "  Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ ") A\r\n"
												+ "Left Outer Join gys_externos_isr2 N\r\n"
												+ "  ON A.anio_ejercicio=N.anio_ejercicio\r\n"
												+ "    And A.mes_ejercicio=N.mes_ejercicio\r\n"
												+ "    And A.rfc=N.rfc\r\n"
												+ "    And N.id_tipo_paga=4";

	int calcula_isr_guardia_par(Integer anio, Integer mes);

	public String QUERY_GET_CIFRAS_ISR			= "Select id_tipo_paga, anio_ejercicio, mes_ejercicio, id_ordinal, SUM(casos) casos, SUM(percepciones) percepciones, SUM(isr) isr,\r\n"
												+ "MIN(fec_min) fec_min, MAX(fec_max) fec_max\r\n"
												+ "From gys_externos_isr2\r\n"
												+ "Where anio_ejercicio = ?\r\n"
												+ "	 And mes_ejercicio = ?\r\n"
												+ "  And id_tipo_paga = ?\r\n"
												+ "Group By id_tipo_paga, anio_ejercicio, mes_ejercicio, id_ordinal";

	List<CifrasDeImpuestos> consultaCifrasDeImpuestos(Integer anio, Integer mes, Integer tipoPaga);

	public String QUERY_GET_CIFRAS_ISR_REP		= "Select id_tipo_paga, anio_ejercicio, mes_ejercicio, id_ordinal, id_delegacion id_representacion, n_div_geografica n_representacion,\r\n"
												+ "  SUM(casos) casos, SUM(percepciones) percepciones, SUM(isr) isr,\r\n"
												+ "  MIN(fec_min) fec_min, MAX(fec_max) fec_max\r\n"
												+ "From gys_externos_isr2 A, m4t_centros_trab B, m4t_delegaciones C\r\n"
												+ "Where A.id_centro_trabajo = B.id_centro_trabajo\r\n"
												+ "  And B.id_delegacion = C.id_div_geografica\r\n"
												+ "	 And anio_ejercicio = ?\r\n"
												+ "	 And mes_ejercicio = ?\r\n"
												+ "  And id_tipo_paga = ?\r\n"
												+ "Group By id_tipo_paga, anio_ejercicio, mes_ejercicio, id_ordinal, id_delegacion, n_div_geografica";

	List<CifrasDeImpuestosPorRepresentacion> consultaCifrasDeImpuestosPorRep(Integer anio, Integer mes, Integer tipoPaga);

	public String QUERY_GET_CIFRAS_ISR_BY_ORDINAL = "Select id_tipo_paga, anio_ejercicio, mes_ejercicio, id_ordinal, SUM(casos) casos, SUM(percepciones) percepciones, SUM(isr) isr,\r\n"
												+ "MIN(fec_min) fec_min, MAX(fec_max) fec_max\r\n"
												+ "From gys_externos_isr2\r\n"
												+ "Where anio_ejercicio = ?\r\n"
												+ "	 And mes_ejercicio = ?\r\n"
												+ "  And id_tipo_paga = ?\r\n"
												+ "  And id_ordinal = ?\r\n"
												+ "Group By id_tipo_paga, anio_ejercicio, mes_ejercicio, id_ordinal";

	CifrasDeImpuestos consultaCifrasDeImpuestosByOrdinal(Integer anio, Integer mes, Integer tipoPaga, Integer id_ordinal);

	public String STMT_CALCULA_ISR_SUPLENC_PAR	= "Insert Into gys_externos_isr2 ( anio_ejercicio, mes_ejercicio, id_tipo_paga, tipo, rfc, casos, percepciones, isr, id_clave_servicio, id_centro_trabajo )\r\n"
												+ "Select \r\n"
												+ "  A.anio_ejercicio, A.mes_ejercicio, A.id_tipo_paga, A.tipo, A.rfc, A.casos, A.percep, _get_ispt_(percep + NVL(percepciones, 0)) - NVL(N.isr, 0) isr, A.id_clave_servicio, A.id_centro_trabajo\r\n"
												+ "From (\r\n"
												+ "  Select anio_ejercicio, mes_ejercicio, id_tipo_paga, 'SE' tipo, rfc, COUNT(*) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr,\r\n"
												+ "    MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo\r\n"
												+ "  From gys_suplencias_ext G, gys_fechas_control F\r\n"
												+ "  Where G.fec_paga = F.fec_pago\r\n"
												+ "    And anio_ejercicio = ?\r\n"
												+ "    And mes_ejercicio = ?\r\n"
												+ "    And id_tipo_paga = 1\r\n" // Se necesita agregar status de sólo cuando esten cerradas
												+ "  Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ ") A\r\n"
												+ "Left Outer Join gys_externos_isr2 N\r\n"
												+ "  ON A.anio_ejercicio=N.anio_ejercicio\r\n"
												+ "    And A.mes_ejercicio=N.mes_ejercicio\r\n"
												+ "    And A.rfc=N.rfc\r\n"
												+ "    And N.id_tipo_paga=4";

	int calcula_isr_suplencia_par(Integer anio, Integer mes);

	public String STMT_ELIMINA_CALCULO_ISR  	= "Delete From gys_externos_isr2 Where anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = ?";

	int elimina_cifras_impuesto(Integer anio, Integer mes, Integer tipoPaga);

	public String STMT_ELIMINA_CALCULO_ISR_X_REC = "Delete From gys_externos_isr2 Where anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = ?\r\n"
												+ " And fec_min between ? And ?"
												+ " And fec_max between ? And ?";

	int elimina_cifras_impuesto_x_rec(Integer anio, Integer mes, Integer tipoPaga, String fec_min, String fec_max );

	public String QUERY_GET_DETALLE_CIFRAS_ISR  = "Select * From gys_externos_isr2\r\n"
												+ "Where anio_ejercicio = ?\r\n"
												+ " And mes_ejercicio = ?\r\n"
												+ " And id_tipo_paga = ?\r\n"
												+ " And id_ordinal = ?\r\n";
	List<DetalleCifrasDeImpuestos> getDetalleCifrasDeImpuestos(Integer anio, Integer mes, Integer tipoPaga, Integer id_ordinal);

	public String QUERY_GET_DETALLE_CIFRAS_ISR_REP = "Select A.*, B.id_delegacion id_representacion, C.n_div_geografica n_representacion \r\n"
												+ "From gys_externos_isr2 A, m4t_centros_trab B, m4t_delegaciones C\r\n"
												+ "Where A.id_centro_trabajo = B.id_centro_trabajo\r\n"
												+ "  And B.id_delegacion = C.id_div_geografica\r\n"
												+ "  And A.anio_ejercicio = ?\r\n"
												+ "  And A.mes_ejercicio = ?\r\n"
												+ "  And A.id_tipo_paga = ?\r\n"
												+ "  And A.id_ordinal = ?\r\n"
												+ "  And C.id_div_geografica = ?";
	List<DetalleCifrasDeImpuestosPorRep> getDetalleCifrasDeImpuestosXRep(Integer anio, Integer mes, Integer tipoPaga, Integer id_ordinal, String id_rep);

	public String QUERY_GET_DETALLE_CIFRAS_ISR_PA = "Select * From gys_externos_isr2\r\n"
												+ "Where anio_ejercicio = ?\r\n"
												+ " And mes_ejercicio = ?\r\n"
												+ " And id_tipo_paga = ?\r\n"
												+ " And id_ordinal = ?\r\n";
	List<DetalleCifrasDeImpuestosConPA> getDetalleCifrasDeImpuestosPA(Integer anio, Integer mes, Integer tipoPaga, Integer id_ordinal);

	// Pruebas Cálculo
	public String STMT_ELIMINA_CALCULO_ISR_X_ORD = "Delete From gys_externos_isr2\r\n"
												+ "Where anio_ejercicio = ? And mes_ejercicio = ? And id_tipo_paga = ?\r\n"
												+ "And id_ordinal = ?";

	int elimina_cifras_impuesto_x_ord(Integer anio, Integer mes, Integer tipoPaga, Integer ord );

	public String STMT_RE_CALCULA_ISR_ORD_NON	= "Insert Into gys_externos_isr2 ( anio_ejercicio, mes_ejercicio, id_tipo_paga, id_ordinal,\r\n"
												+ "tipo, rfc, casos, percepciones, isr, id_clave_servicio, id_centro_trabajo, fec_min, fec_max )\r\n"
												+ "Select anio_ejercicio, mes_ejercicio, id_tipo_paga,\r\n"
												+ "### id_ordinal, 'DOS' tipo, rfc, SUM(casos) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr,\r\n"
												+ "MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(fec_min) fec_min,  MAX(fec_max) fec_max\r\n"
												+ "From (\r\n"
												+ "  Select\r\n"
												+ "    anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc, COUNT(*) casos, SUM(importe) importe,\r\n"
												+ "    MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(F.fec_pago) fec_min,  MAX(F.fec_pago) fec_max\r\n"
												+ "  From gys_guardias_ext G, gys_fechas_control F\r\n"
												+ "  Where G.fec_paga = F.fec_pago\r\n"
												+ "    And anio_ejercicio = ?\r\n"
												+ "    And mes_ejercicio = ?\r\n"
												+ "    And id_tipo_paga = 4\r\n"
												+ "    And F.fec_pago between ? And ?\r\n"
												+ "  Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ "  UNION ALL\r\n"
												+ "  Select\r\n"
												+ "    anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc, COUNT(*) casos, SUM(importe) importe,\r\n"
												+ "    MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(F.fec_pago) fec_min,  MAX(F.fec_pago) fec_max\r\n"
												+ "  From gys_suplencias_ext S, gys_fechas_control F\r\n"
												+ "  Where S.fec_paga = F.fec_pago\r\n"
												+ "    And anio_ejercicio = ?\r\n"
												+ "    And mes_ejercicio = ?\r\n"
												+ "    And id_tipo_paga = 4\r\n"
												+ "    And F.fec_pago between ? And ?\r\n"
												+ "  Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ ") Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc";

	int re_calcula_isr_ord_non(Integer anio, Integer mes, String fec_min, String fec_max, Integer ord);

	public String STMT_RE_CALCULA_ISR_ORD_PAR	= "Insert Into gys_externos_isr2 ( anio_ejercicio, mes_ejercicio, id_tipo_paga, id_ordinal,\r\n"
												+ "tipo, rfc, casos, percepciones, isr, id_clave_servicio, id_centro_trabajo, fec_min, fec_max )\r\n"
												+ "Select \r\n"
												+ "  A.anio_ejercicio, A.mes_ejercicio, A.id_tipo_paga, \r\n"
												+ "  ### id_ordinal, 'DOS' tipo, A.rfc, SUM(A.casos) casos, SUM(A.percep) percep, SUM(_get_ispt_(percep + NVL(percepciones, 0)) - NVL(N.isr, 0)) isr, \r\n"
												+ "  MAX(A.id_clave_servicio) id_clave_servicio, MAX(A.id_centro_trabajo) id_centro_trabajo, MIN(fec_min) fec_min,  MAX(fec_max) fec_max\r\n"
												+ "From (\r\n"
												+ "  Select anio_ejercicio, mes_ejercicio, id_tipo_paga,\r\n"
												+ "    'DOS' tipo, rfc, SUM(casos) casos, SUM(importe) percep, _get_ispt_( SUM(importe) * 2 ) / 2 isr,\r\n"
												+ "    MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(fec_min) fec_min,  MAX(fec_max) fec_max\r\n"
												+ "  From (\r\n"
												+ "    Select\r\n"
												+ "      anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc, COUNT(*) casos, SUM(importe) importe,\r\n"
												+ "      MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(F.fec_pago) fec_min,  MAX(F.fec_pago) fec_max\r\n"
												+ "    From gys_guardias_ext G, gys_fechas_control F\r\n"
												+ "    Where G.fec_paga = F.fec_pago\r\n"
												+ "      And anio_ejercicio = ?\r\n"
												+ "      And mes_ejercicio = ?\r\n"
												+ "      And id_tipo_paga = 1\r\n"
												+ "      And F.fec_pago between ? And ?\r\n"
												+ "    Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ "    UNION ALL\r\n"
												+ "    Select\r\n"
												+ "      anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc, COUNT(*) casos, SUM(importe) importe,\r\n"
												+ "      MAX(id_clave_servicio) id_clave_servicio, MAX(id_centro_trabajo) id_centro_trabajo, MIN(F.fec_pago) fec_min,  MAX(F.fec_pago) fec_max\r\n"
												+ "    From gys_suplencias_ext S, gys_fechas_control F\r\n"
												+ "    Where S.fec_paga = F.fec_pago\r\n"
												+ "      And anio_ejercicio = ?\r\n"
												+ "      And mes_ejercicio = ?\r\n"
												+ "      And id_tipo_paga = 1\r\n"
												+ "      And F.fec_pago between ? And ?\r\n"
												+ "    Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ "  ) Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ ") A Left Outer Join (\r\n"
												+ "  Select anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc, SUM(percepciones) percepciones, SUM(isr) isr\r\n"
												+ "  From gys_externos_isr2\r\n"
												+ "  Group By anio_ejercicio, mes_ejercicio, id_tipo_paga, rfc\r\n"
												+ ") N\r\n"
												+ "  ON A.anio_ejercicio=N.anio_ejercicio\r\n"
												+ "    And A.mes_ejercicio=N.mes_ejercicio\r\n"
												+ "    And A.rfc=N.rfc\r\n"
												+ "    And N.id_tipo_paga=4\r\n"
												+ "Group By A.anio_ejercicio, A.mes_ejercicio, A.id_tipo_paga, A.rfc";

	int re_calcula_isr_ord_par(Integer anio, Integer mes, String fec_min, String fec_max, Integer ord);

	public String STMT_EXECUTE_SP_PA			= "spm4_gys_pension(?, ?, ?, ?)";

	int calculaPensionAlimenticia(Integer anio, Integer mes, Integer tipoPaga, Integer id_ordinal);

}