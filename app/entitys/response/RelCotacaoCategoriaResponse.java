package entitys.response;

import models.Categoria;
		
public class RelCotacaoCategoriaResponse {
	
	private Long categoriaId;
	
	private String categoriaNome;
	
	private String categoriaDescricao;

	private Integer ordem;
	
	public RelCotacaoCategoriaResponse(Categoria categoria, Integer ordem) {
		
		if(categoria != null) {
			
			this.categoriaId = categoria.getId();
			this.categoriaNome = categoria.getNome();
			this.categoriaDescricao = categoria.getDescricao();
		}
		
		this.ordem = ordem;
	}

	public Long getCategoriaId() {
		return categoriaId;
	}

	public String getCategoriaNome() {
		return categoriaNome;
	}

	public String getCategoriaDescricao() {
		return categoriaDescricao;
	}

	public Integer getOrdem() {
		return ordem;
	}
	

}
