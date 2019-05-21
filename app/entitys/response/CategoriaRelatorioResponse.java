package entitys.response;

import java.util.List;

		
public class CategoriaRelatorioResponse {
	
	private Long id;
	
	private String nome;
	
	private String descricao;
	
	private String valorTotalCategoriaReal;
	
	private String valorTotalCategoriaDolar;
	
	private String valorSacaCategoriaReal;
	
	private String valorSacaCategoriaDolar;
	
	private List<ItemRelatorioResponse> itemList;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<ItemRelatorioResponse> getItemList() {
		return itemList;
	}

	public void setItemList(List<ItemRelatorioResponse> itemList) {
		this.itemList = itemList;
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

	public String getValorTotalCategoriaReal() {
		return valorTotalCategoriaReal;
	}

	public void setValorTotalCategoriaReal(String valorTotalCategoriaReal) {
		this.valorTotalCategoriaReal = valorTotalCategoriaReal;
	}

	public String getValorTotalCategoriaDolar() {
		return valorTotalCategoriaDolar;
	}

	public void setValorTotalCategoriaDolar(String valorTotalCategoriaDolar) {
		this.valorTotalCategoriaDolar = valorTotalCategoriaDolar;
	}

	public String getValorSacaCategoriaReal() {
		return valorSacaCategoriaReal;
	}

	public void setValorSacaCategoriaReal(String valorSacaCategoriaReal) {
		this.valorSacaCategoriaReal = valorSacaCategoriaReal;
	}

	public String getValorSacaCategoriaDolar() {
		return valorSacaCategoriaDolar;
	}

	public void setValorSacaCategoriaDolar(String valorSacaCategoriaDolar) {
		this.valorSacaCategoriaDolar = valorSacaCategoriaDolar;
	}

}
