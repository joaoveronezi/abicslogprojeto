package entitys.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class CotacaoCategoriaRequest {

	private Long cotacaoId;
	
	private List<Long> idCategorias;

	private List<Integer> ordemList;
	
	public Long getCotacaoId() {
		return cotacaoId;
	}

	public void setCotacaoId(Long cotacaoId) {
		this.cotacaoId = cotacaoId;
	}

	public List<Long> getIdCategorias() {
		return idCategorias;
	}

	public void setIdCategorias(List<Long> idCategorias) {
		this.idCategorias = idCategorias;
	}

	public List<Integer> getOrdemList() {
		return ordemList;
	}

	public void setOrdemList(List<Integer> ordemList) {
		this.ordemList = ordemList;
	}

}