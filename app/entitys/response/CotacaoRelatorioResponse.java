package entitys.response;

import java.util.List;

		
public class CotacaoRelatorioResponse {
	
	private Long id;
	
	private String nome;
	
	private String descricao;
	
	private int nacional;
	
	private String flag;
	
	private int flagEspiritoSanto;
	
	private String fonte;

	private String diferencialES;
	
	private String diferencialRO;

	private String diferencialESDolar;
	
	private String diferencialRODolar;
	
	private String diferencialPercentualES;
	
	private String diferencialPercentualRO;
	
	private String valorCustoImportacaoReal;
	
	private String valorCustoImportacaoDolar;
	
	private String valorMercadoriaReal;
	
	private String valorMercadoriaDolar;
	
	private List<CategoriaRelatorioResponse> categoriaList;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome= nome;
	}

	public List<CategoriaRelatorioResponse> getCategoriaList() {
		return categoriaList;
	}

	public void setCategoriaList(List<CategoriaRelatorioResponse> categoriaList) {
		this.categoriaList = categoriaList;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNacional() {
		return nacional;
	}

	public void setNacional(int nacional) {
		this.nacional = nacional;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public int getFlagEspiritoSanto() {
		return flagEspiritoSanto;
	}

	public void setFlagEspiritoSanto(int flagEspiritoSanto) {
		this.flagEspiritoSanto = flagEspiritoSanto;
	}

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public String getDiferencialES() {
		return diferencialES;
	}

	public void setDiferencialES(String diferencialES) {
		this.diferencialES = diferencialES;
	}

	public String getDiferencialRO() {
		return diferencialRO;
	}

	public void setDiferencialRO(String diferencialRO) {
		this.diferencialRO = diferencialRO;
	}

	public String getDiferencialPercentualES() {
		return diferencialPercentualES;
	}

	public void setDiferencialPercentualES(String diferencialPercentualES) {
		this.diferencialPercentualES = diferencialPercentualES;
	}

	public String getDiferencialPercentualRO() {
		return diferencialPercentualRO;
	}

	public void setDiferencialPercentualRO(String diferencialPercentualRO) {
		this.diferencialPercentualRO = diferencialPercentualRO;
	}

	public String getValorCustoImportacaoReal() {
		return valorCustoImportacaoReal;
	}

	public void setValorCustoImportacaoReal(String valorCustoImportacaoReal) {
		this.valorCustoImportacaoReal = valorCustoImportacaoReal;
	}

	public String getValorCustoImportacaoDolar() {
		return valorCustoImportacaoDolar;
	}

	public void setValorCustoImportacaoDolar(String valorCustoImportacaoDolar) {
		this.valorCustoImportacaoDolar = valorCustoImportacaoDolar;
	}

	public String getDiferencialESDolar() {
		return diferencialESDolar;
	}

	public void setDiferencialESDolar(String diferencialESDolar) {
		this.diferencialESDolar = diferencialESDolar;
	}

	public String getDiferencialRODolar() {
		return diferencialRODolar;
	}

	public void setDiferencialRODolar(String diferencialRODolar) {
		this.diferencialRODolar = diferencialRODolar;
	}

	public String getValorMercadoriaReal() {
		return valorMercadoriaReal;
	}

	public void setValorMercadoriaReal(String valorMercadoriaReal) {
		this.valorMercadoriaReal = valorMercadoriaReal;
	}

	public String getValorMercadoriaDolar() {
		return valorMercadoriaDolar;
	}

	public void setValorMercadoriaDolar(String valorMercadoriaDolar) {
		this.valorMercadoriaDolar = valorMercadoriaDolar;
	}

}
