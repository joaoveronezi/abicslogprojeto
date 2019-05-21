package entitys.response;
		
public class AbicsDataRelatorioAbicsResponse {
	
	private String tipo; 
	
	private String tipoCafe;
	
	private String pais;
	
	private String mes;
	
	private String ano;
	
	private String peso;
	
	private String receita;
	
	private String saca60kg;
	
	public AbicsDataRelatorioAbicsResponse(String tipo, String tipoCafe, String pais, String mes,  String ano, 
			String peso, String receita, String saca60kg) {
		
		this.tipo = tipo;
		this.tipoCafe = tipoCafe;
		this.mes = mes;
		this.pais = pais;
		this.ano = ano;
		this.peso = peso;
		this.receita = receita;
		this.saca60kg = saca60kg;
	}

	public String getTipoCafe() {
		return tipoCafe;
	}

	public String getPais() {
		return pais;
	}

	public String getMes() {
		return mes;
	}

	public String getAno() {
		return ano;
	}
	
	public String getTipo() {
		return tipo;
	}

	public String getPeso() {
		return peso;
	}

	public String getReceita() {
		return receita;
	}

	public String getSaca60kg() {
		return saca60kg;
	}
}
