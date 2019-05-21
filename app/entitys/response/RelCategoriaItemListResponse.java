package entitys.response;

import java.util.List;

import models.Categoria;

		
public class RelCategoriaItemListResponse {
	
	private String nome;
	
	private List<ItemValorResponse> itemList;

	public RelCategoriaItemListResponse(Categoria categoria, List<ItemValorResponse> itemValor) {
		
		if(categoria != null) {
			this.nome = categoria.getNome();
		}
		
		if(itemValor != null) {
			this.itemList = itemValor;
		}
	}

	public String getNome() {
		return nome;
	}

	public List<ItemValorResponse> getItemList() {
		return itemList;
	}
}
