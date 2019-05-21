package entitys.response;

import java.math.BigDecimal;

import models.Item;
import models.RelItemValor;

public class ItemValorResponse {

//	public ItemValorResponse(Item item, RelItemValor valor, String variavel, Integer ordem) {
	public ItemValorResponse(Item item, RelItemValor valor, Integer ordem) {
		
		if(item != null) {
			this.id = item.getId();
			this.nome = item.getNome();
			this.descricao = item.getDescricao();
//			this.tipo = item.getTipo();
//			this.formula =  item.getFormula();
//			this.visivel = item.getVisivel();
//			this.unidade = item.getUnidade();
		}
		
		if(valor != null) {
			this.valor = valor.getValor();
			this.data = valor.getData();
			this.unidade = valor.getUnidade();
		}
		
//		this.variavel = variavel;
		this.ordem = ordem;
	}
	
	private Long id;
	
	private BigDecimal valor;

	private String nome;
	
	private String descricao;
	
	private Long data;
	
//	private Integer tipo;
	
//	private String formula;

//	private int visivel;
	
	private int unidade;

//	private String variavel;

	private Integer ordem;
	
	public Long getId() {
		return id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}

//	public Integer getTipo() {
//		return tipo;
//	}
//
//	public String getFormula() {
//		return formula;
//	}
//
//	public int getVisivel() {
//		return visivel;
//	}

	public int getUnidade() {
		return unidade;
	}

//	public String getVariavel() {
//		return variavel;
//	}

	public Integer getOrdem() {
		return ordem;
	}

	public Long getData() {
		return data;
	}
	
}