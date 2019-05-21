package entitys.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class CategoriaItemRequest {

	private Long categoriaId;
	
	private List<Long> idItens;

	private List<Integer> ordemItemList;
	
	private Long categoriaIdTotalCategoria;
	
	public Long getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Long categoriaId) {
		this.categoriaId = categoriaId;
	}

	public List<Long> getIdItens() {
		return idItens;
	}

	public void setIdItens(List<Long> idItens) {
		this.idItens = idItens;
	}

	public List<Integer> getOrdemItemList() {
		return ordemItemList;
	}

	public void setOrdemItemList(List<Integer> ordemItemList) {
		this.ordemItemList = ordemItemList;
	}

	public Long getCategoriaIdTotalCategoria() {
		return categoriaIdTotalCategoria;
	}

	public void setCategoriaIdTotalCategoria(Long categoriaIdTotalCategoria) {
		this.categoriaIdTotalCategoria = categoriaIdTotalCategoria;
	}

}