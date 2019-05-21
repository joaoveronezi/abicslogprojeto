package entitys.response;

import models.AbicsProduto;
import models.AbicsExportacaoProduto;

		
public class ExportacaoProdutoResponse {
	
	private Long id;
	
	private int ano;
	
	private int mes;
	
	private Long peso;
	
	private Long receita;
	
	private Long saca;
	
	private Long produtoId;
	
	private String produtoNome;
	
	public ExportacaoProdutoResponse(AbicsExportacaoProduto exportacaoProduto, AbicsProduto produto) {
		
		if(exportacaoProduto != null) {
			this.id = exportacaoProduto.getId();
			this.ano = exportacaoProduto.getAno();
			this.mes = exportacaoProduto.getMes();
			this.peso = exportacaoProduto.getPeso();
			this.receita = exportacaoProduto.getReceita();
			this.saca = exportacaoProduto.getSaca60kg();
			this.produtoId = exportacaoProduto.getProdutoId();
		}
		if(produto != null){
			this.produtoNome = produto.getNome();
		}
			
	}

	public Long getId() {
		return id;
	}

	public int getAno() {
		return ano;
	}

	public int getMes() {
		return mes;
	}

	public Long getPeso() {
		return peso;
	}

	public Long getReceita() {
		return receita;
	}

	public Long getSaca() {
		return saca;
	}

	public Long getProdutoId() {
		return produtoId;
	}

	public String getProdutoNome() {
		return produtoNome;
	}
	
}