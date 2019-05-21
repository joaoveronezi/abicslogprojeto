package entitys.response;

import java.util.List;

		
public class ItemRelatorioResponse {
	
	private Long id;
	
	private String nome;
	
	private String valor;
	
	private Integer tipo;

	private String formula;
	
	private String variavel;

	private String unidade;
	
	private Integer isCategoriaTotal; 
	
	private String valorDolar;
	
	private String valorEuro;

//	private String valorSaca60kg;
//	
//	private String valorDolarSaca60kg;
//	
//	private String valorEuroSaca60kg;
	
	private Integer tipoMoeda;
	
	private String porcentagem;
	
//	private String diferencialES;
//	
//	private String diferencialRO;
//
//	private String diferencialPercentualES;
//	
//	private String diferencialPercentualRO;
	
//	private String valorES;
//	
//	private String valorRO;
//	
//	private String diferencialESDolar;
//	
//	private String diferencialRODolar;
//
//	private String valorESDolar;
//	
//	private String valorRODolar;
	
//	private String valorFOBRealSaca60kg;
//	
//	private String valorFOBDolarSaca60kg;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getVariavel() {
		return variavel;
	}

	public void setVariavel(String variavel) {
		this.variavel = variavel;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public Integer getIsCategoriaTotal() {
		return isCategoriaTotal;
	}

	public void setIsCategoriaTotal(Integer isCategoriaTotal) {
		this.isCategoriaTotal = isCategoriaTotal;
	}

	public String getValorDolar() {
		return valorDolar;
	}

	public void setValorDolar(String valorDolar) {
		this.valorDolar = valorDolar;
	}

	public String getValorEuro() {
		return valorEuro;
	}

	public void setValorEuro(String valorEuro) {
		this.valorEuro = valorEuro;
	}

	public Integer getTipoMoeda() {
		return tipoMoeda;
	}

	public void setTipoMoeda(Integer tipoMoeda) {
		this.tipoMoeda = tipoMoeda;
	}

	public String getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(String porcentagem) {
		this.porcentagem = porcentagem;
	}

//	public String getValorSaca60kg() {
//		return valorSaca60kg;
//	}
//
//	public void setValorSaca60kg(String valorSaca60kg) {
//		this.valorSaca60kg = valorSaca60kg;
//	}
//
//	public String getValorDolarSaca60kg() {
//		return valorDolarSaca60kg;
//	}
//
//	public void setValorDolarSaca60kg(String valorDolarSaca60kg) {
//		this.valorDolarSaca60kg = valorDolarSaca60kg;
//	}
//
//	public String getValorEuroSaca60kg() {
//		return valorEuroSaca60kg;
//	}
//
//	public void setValorEuroSaca60kg(String valorEuroSaca60kg) {
//		this.valorEuroSaca60kg = valorEuroSaca60kg;
//	}

//	public String getDiferencialES() {
//		return diferencialES;
//	}
//
//	public void setDiferencialES(String diferencialES) {
//		this.diferencialES = diferencialES;
//	}
//
//	public String getDiferencialRO() {
//		return diferencialRO;
//	}
//
//	public void setDiferencialRO(String diferencialRO) {
//		this.diferencialRO = diferencialRO;
//	}

//	public String getValorES() {
//		return valorES;
//	}
//
//	public void setValorES(String valorES) {
//		this.valorES = valorES;
//	}
//
//	public String getValorRO() {
//		return valorRO;
//	}
//
//	public void setValorRO(String valorRO) {
//		this.valorRO = valorRO;
//	}
//
//	public String getDiferencialESDolar() {
//		return diferencialESDolar;
//	}
//
//	public void setDiferencialESDolar(String diferencialESDolar) {
//		this.diferencialESDolar = diferencialESDolar;
//	}
//
//	public String getDiferencialRODolar() {
//		return diferencialRODolar;
//	}
//
//	public void setDiferencialRODolar(String diferencialRODolar) {
//		this.diferencialRODolar = diferencialRODolar;
//	}
//
//	public String getValorESDolar() {
//		return valorESDolar;
//	}
//
//	public void setValorESDolar(String valorESDolar) {
//		this.valorESDolar = valorESDolar;
//	}
//
//	public String getValorRODolar() {
//		return valorRODolar;
//	}
//
//	public void setValorRODolar(String valorRODolar) {
//		this.valorRODolar = valorRODolar;
//	}

//	public String getValorFOBRealSaca60kg() {
//		return valorFOBRealSaca60kg;
//	}
//
//	public void setValorFOBRealSaca60kg(String valorFOBRealSaca60kg) {
//		this.valorFOBRealSaca60kg = valorFOBRealSaca60kg;
//	}
//
//	public String getValorFOBDolarSaca60kg() {
//		return valorFOBDolarSaca60kg;
//	}
//
//	public void setValorFOBDolarSaca60kg(String valorFOBDolarSaca60kg) {
//		this.valorFOBDolarSaca60kg = valorFOBDolarSaca60kg;
//	}

//	public String getDiferencialPercentualES() {
//		return diferencialPercentualES;
//	}
//
//	public void setDiferencialPercentualES(String diferencialPercentualES) {
//		this.diferencialPercentualES = diferencialPercentualES;
//	}
//
//	public String getDiferencialPercentualRO() {
//		return diferencialPercentualRO;
//	}
//
//	public void setDiferencialPercentualRO(String diferencialPercentualRO) {
//		this.diferencialPercentualRO = diferencialPercentualRO;
//	}
}
