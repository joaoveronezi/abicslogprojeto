package entitys.response;
		
public class AbicsDataRelatorioComtradeMesResponse {
	
	private String tipo; 
	
	private String tipoCafe;
	
	private String pais;
	
	private String mes;
	
	private String ano;
	
	private String tradeValue;
	
	private String netWeight;
	
	public AbicsDataRelatorioComtradeMesResponse(String tipo, String tipoCafe, String pais, String mes, 
			String ano, String tradeValue, String netWeight) {
		
		this.tipo = tipo;
		this.tipoCafe = tipoCafe;
		this.pais = pais;
		this.mes = mes;
		this.ano = ano;
		this.tradeValue = tradeValue;
		this.netWeight = netWeight;
	}

	public String getTipoCafe() {
		return tipoCafe;
	}

	public String getPais() {
		return pais;
	}

	public String getAno() {
		return ano;
	}

	public String getTipo() {
		return tipo;
	}

	public String getTradeValue() {
		return tradeValue;
	}

	public String getNetWeight() {
		return netWeight;
	}

	public String getMes() {
		return mes;
	}

}
