package gob.issste.gys.model;

public class CifrasImpuesto {

	private String tipo;
	private String fec_pago;
	private int casos;
	private Double percepciones;
	private Double isr;

	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getFec_pago() {
		return fec_pago;
	}
	public void setFec_pago(String fec_pago) {
		this.fec_pago = fec_pago;
	}
	public int getCasos() {
		return casos;
	}
	public void setCasos(int casos) {
		this.casos = casos;
	}
	public Double getPercepciones() {
		return percepciones;
	}
	public void setPercepciones(Double percepciones) {
		this.percepciones = percepciones;
	}
	public Double getIsr() {
		return isr;
	}
	public void setIsr(Double isr) {
		this.isr = isr;
	}

}
