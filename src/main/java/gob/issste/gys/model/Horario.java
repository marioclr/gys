package gob.issste.gys.model;

public class Horario {
	private String id_horario;
	private String n_horario;
	private Double hora_entrada_to; 
	private Double hora_salida_to; 
	private Double hora_entrada_op; 
	private Double hora_salida_op;
	private Double num_horas; 
	private Double id_jornada_horario;

	public String getId_horario() {
		return id_horario;
	}
	public void setId_horario(String id_horario) {
		this.id_horario = id_horario;
	}
	public String getN_horario() {
		return n_horario;
	}
	public void setN_horario(String n_horario) {
		this.n_horario = n_horario;
	}
	public Double getHora_entrada_to() {
		return hora_entrada_to;
	}
	public void setHora_entrada_to(Double hora_entrada_to) {
		this.hora_entrada_to = hora_entrada_to;
	}
	public Double getHora_salida_to() {
		return hora_salida_to;
	}
	public void setHora_salida_to(Double hora_salida_to) {
		this.hora_salida_to = hora_salida_to;
	}
	public Double getHora_entrada_op() {
		return hora_entrada_op;
	}
	public void setHora_entrada_op(Double hora_entrada_op) {
		this.hora_entrada_op = hora_entrada_op;
	}
	public Double getHora_salida_op() {
		return hora_salida_op;
	}
	public void setHora_salida_op(Double hora_salida_op) {
		this.hora_salida_op = hora_salida_op;
	}
	public Double getNum_horas() {
		return num_horas;
	}
	public void setNum_horas(Double num_horas) {
		this.num_horas = num_horas;
	}
	public Double getId_jornada_horario() {
		return id_jornada_horario;
	}
	public void setId_jornada_horario(Double id_jornada_horario) {
		this.id_jornada_horario = id_jornada_horario;
	}
}