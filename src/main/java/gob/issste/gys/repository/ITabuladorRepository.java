package gob.issste.gys.repository;

import gob.issste.gys.model.ValoresTabulador;

public interface ITabuladorRepository {

	public String QUERY_GET_TABULADOR_MEDICO    = "Select id_zona,id_nivel,id_sub_nivel,id_tipo_jornada,NVL(SUM(sueldo),0) sueldo, NVL(SUM(compen_g6),0) compen_g6, NVL(SUM(compen_un),0) compen_un, NVL(SUM(asignacion),0) asignacion, NVL(SUM(ayuda),0) ayuda\r\n"
												+ "From gys_tabulador_medico\r\n"
												+ "Where id_zona = ?      And id_nivel = ?\r\n"
												+ "  And id_sub_nivel = ? And id_tipo_jornada = ?\r\n"
												+ "  And fec_inicio <= ? AND (fec_fin >= ? OR fec_fin IS NULL)\r\n"
												+ "Group By id_zona,id_nivel,id_sub_nivel,id_tipo_jornada";
	ValoresTabulador ConsultaTabuladorMedico(String zona, String nivel, String subnivel, String tipo_jornada, String quincena);

	public String QUERY_GET_TABULADOR_OPERATIVO = "Select *\r\n"
												+ "From gys_tabulador_operativo\r\n"
												+ "Where id_zona = ?      And id_nivel = ?\r\n"
												+ "  And id_sub_nivel = ?\r\n"
												+ "  And fec_inicio <= ? AND (fec_fin >= ? OR fec_fin IS NULL)";
	ValoresTabulador ConsultaTabuladorOperativo(String zona, String nivel, String subnivel, String tipo_jornada, String quincena);

}
