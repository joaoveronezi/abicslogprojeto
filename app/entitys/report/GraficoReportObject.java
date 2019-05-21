package entitys.report;

import java.util.List;

import models.Item;

public class GraficoReportObject {

	private List<ItemReport> itens;
	
	private String caminhoImagemGraficoItem;
	
	private List<Item> categorias;
	
	private String caminhoImagemGraficoCategoria;

	public List<ItemReport> getItens() {
		return itens;
	}

	public void setItens(List<ItemReport> itens) {
		this.itens = itens;
	}

	public String getCaminhoImagemGraficoItem() {
		return caminhoImagemGraficoItem;
	}

	public void setCaminhoImagemGraficoItem(String caminhoImagemGraficoItem) {
		this.caminhoImagemGraficoItem = caminhoImagemGraficoItem;
	}

	public List<Item> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Item> categorias) {
		this.categorias = categorias;
	}

	public String getCaminhoImagemGraficoCategoria() {
		return caminhoImagemGraficoCategoria;
	}

	public void setCaminhoImagemGraficoCategoria(
			String caminhoImagemGraficoCategoria) {
		this.caminhoImagemGraficoCategoria = caminhoImagemGraficoCategoria;
	}
	
//	private String nome;
//	
//	private String valor;
//	
//	private String formula;
//
//	private String data;
//	
//	public String getNome() {
//		return nome;
//	}
//
//	public void setNome(String nome) {
//		this.nome = nome;
//	}
//
//	public String getValor() {
//		return valor;
//	}
//
//	public void setValor(String valor) {
//		this.valor = valor;
//	}
//
//	public String getFormula() {
//		return formula;
//	}
//
//	public void setFormula(String formula) {
//		this.formula = formula;
//	}
//
//	public String getData() {
//		return data;
//	}
//
//	public void setData(String data) {
//		this.data = data;
//	}
	
	
//	private String caminhoImagemGrafico;
//	
//	private String dataFinal;
//
//	private String dataInicial;
//	
//	private List<Item> itens;
//	
//	public String getCaminhoImagemGrafico() {
//		return caminhoImagemGrafico;
//	}
//
//	public void setCaminhoImagemGrafico(String caminhoImagemGrafico) {
//		this.caminhoImagemGrafico = caminhoImagemGrafico;
//	}
//
//	public String getDataFinal() {
//		return dataFinal;
//	}
//
//	public void setDataFinal(String dataFinal) {
//		this.dataFinal = dataFinal;
//	}
//
//	public List<Item> getItens() {
//		return itens;
//	}
//
//	public void setItens(List<Item> itens) {
//		this.itens = itens;
//	}
//
//	public String getDataInicial() {
//		return dataInicial;
//	}
//
//	public void setDataInicial(String dataInicial) {
//		this.dataInicial = dataInicial;
//	}
}