package entitys.response;
		
public class AbicsDataRelatorioUSDAResponse {
	
	private String tipo; 
	
	private String tipoCafe;
	
	private String pais;
	
	private String ano;
	
	private String quantidade;
	
	public AbicsDataRelatorioUSDAResponse(String tipo, String tipoCafe, String pais, String ano, String quantidade) {
		
		this.tipo = tipo;
		this.tipoCafe = tipoCafe;
		this.pais = pais;
		this.ano = ano;
		this.quantidade = quantidade;
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

	public String getQuantidade() {
		return quantidade;
	}

	public String getTipo() {
		return tipo;
	}

}
