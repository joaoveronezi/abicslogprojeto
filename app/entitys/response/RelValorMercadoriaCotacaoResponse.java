package entitys.response;

import java.math.BigDecimal;

import models.Cotacao;
import models.RelValorMercadoriaCotacao;

		
public class RelValorMercadoriaCotacaoResponse {
	
	private Long id;
	
	private BigDecimal valorMercadoria;
	
	private BigDecimal valorMercadoriaReal;
	
	private BigDecimal valorMercadoriaDolar;
	
	private Long data;

	private Long cotacaoId;

	private String cotacaoNome;
	
	private String cotacaoDescricao;
	
	private Integer cotacaoOrdem;
	
	private BigDecimal valorColetado;
	
	private BigDecimal valorCustoTotalImportacao;
	
	private BigDecimal valorCustoImportacaoReal;

	private BigDecimal valorCustoImportacaoDolar;
	
	public RelValorMercadoriaCotacaoResponse(RelValorMercadoriaCotacao relValorMercadoriaCotacao, Cotacao cotacao) {
		
		if(relValorMercadoriaCotacao != null) {
			this.id = relValorMercadoriaCotacao.getId();
			this.valorMercadoria = relValorMercadoriaCotacao.getValorMercadoria();
			this.data = relValorMercadoriaCotacao.getData();
			this.cotacaoId = relValorMercadoriaCotacao.getCotacaoId();
			this.valorCustoTotalImportacao = relValorMercadoriaCotacao.getValorCustoImportacao();
			
			this.valorMercadoriaReal = relValorMercadoriaCotacao.getValorMercadoriaReal();
			this.valorMercadoriaDolar = relValorMercadoriaCotacao.getValorMercadoriaDolar();

			this.valorCustoImportacaoReal = relValorMercadoriaCotacao.getValorCustoImportacaoReal();
			this.valorCustoImportacaoDolar = relValorMercadoriaCotacao.getValorCustoImportacaoDolar();
			
			this.valorColetado = relValorMercadoriaCotacao.getValorColetado();
		}
		
		if(cotacao != null) {
			this.cotacaoNome = cotacao.getNome();
			this.cotacaoDescricao = cotacao.getDescricao();
			this.cotacaoOrdem = cotacao.getOrdem();
		}
	}

	public Long getId() {
		return id;
	}

	public BigDecimal getValorMercadoria() {
		return valorMercadoria;
	}

	public BigDecimal getValorMercadoriaReal() {
		return valorMercadoriaReal;
	}

	public BigDecimal getValorMercadoriaDolar() {
		return valorMercadoriaDolar;
	}

	public Long getData() {
		return data;
	}

	public Long getCotacaoId() {
		return cotacaoId;
	}

	public String getCotacaoNome() {
		return cotacaoNome;
	}

	public String getCotacaoDescricao() {
		return cotacaoDescricao;
	}

	public Integer getCotacaoOrdem() {
		return cotacaoOrdem;
	}

	public BigDecimal getValorColetado() {
		return valorColetado;
	}

	public BigDecimal getValorCustoTotalImportacao() {
		return valorCustoTotalImportacao;
	}

	public BigDecimal getValorCustoImportacaoReal() {
		return valorCustoImportacaoReal;
	}

	public BigDecimal getValorCustoImportacaoDolar() {
		return valorCustoImportacaoDolar;
	}

}
