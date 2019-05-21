package entitys.response;
		
public class AbicsDataRelatorioComtradeResponse {
	
	private String tipo; 
	
	private String tipoCafe;
	
	private String pais;
	
	private String ano;
	
	private String tradeValue;
	
	private String netWeight;
	
	public AbicsDataRelatorioComtradeResponse(String tipo, String tipoCafe, String pais, String ano, 
			String tradeValue, String netWeight) {
		
		this.tipo = tipo;
		this.tipoCafe = tipoCafe;
		this.pais = pais;
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

}
