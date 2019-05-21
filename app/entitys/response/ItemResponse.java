package entitys.response;

import models.Item;
		
public class ItemResponse {
	
	private Long itemId;
	
	private String itemNome;
	
	private String itemDescricao;

	private Integer ordem;
	
	private Integer isCategoriaTotal;
	
	public ItemResponse(Item item, Integer ordem, Integer isCategoriaTotal) {
		
		if(item != null) {
			
			this.itemId = item.getId();
			this.itemNome = item.getNome();
			this.itemDescricao = item.getDescricao();
		}

		this.ordem = ordem;
		this.isCategoriaTotal = isCategoriaTotal;
	}

	public Long getItemId() {
		return itemId;
	}

	public String getItemNome() {
		return itemNome;
	}

	public String getItemDescricao() {
		return itemDescricao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public Integer getIsCategoriaTotal() {
		return isCategoriaTotal;
	}
}
