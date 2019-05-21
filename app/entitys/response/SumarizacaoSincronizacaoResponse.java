package entitys.response;

import java.math.BigDecimal;

import models.Cambio;
import models.Cotacao;
import models.RelValorMercadoriaCotacao;
import models.SumarizacaoSincronizacao;
import util.MonetaryUtil;

		
public class SumarizacaoSincronizacaoResponse {
	
	
	// SumarizacaoSincronizacao
	private Long data;
	
	private int status;
	
	// Cambio
	private Long cambioId;
	
	private BigDecimal valorReal;
	
	private BigDecimal valorDolar;
	
	private BigDecimal valorEuro;
	
	private BigDecimal valorEuroDolar;
	
	private BigDecimal valorColetado;

	// RelValorMercadoriaCotacao
	private Long relValorMercadoriaCotacaoId;
	
	private BigDecimal valorMercadoria;

	private Long cotacaoId;
	
	private String cotacaoNome;
	
	private String cotacaoDescricao;
	
	private int cotacaoNacional;

	private BigDecimal valorCustoImportacao;
	
	
	private BigDecimal valorCustoImportacaoSaca60kgReal;
	
	private BigDecimal valorMercadoriaSaca60kgReal;
	
	private BigDecimal valorCustoImportacaoSaca60kgDolar;
	
	private BigDecimal valorMercadoriaSaca60kgDolar;
	
	
	public SumarizacaoSincronizacaoResponse(Long data, int status, Cambio cambio, RelValorMercadoriaCotacao rel, Cotacao cotacao, BigDecimal valorEuroParametro, BigDecimal valorDolarParametro) {
		
		this.data = data;
		this.status = status;
		
		if(cambio != null) {
			this.cambioId = cambio.getId();
			this.valorReal = cambio.getValorReal();
			this.valorDolar = cambio.getValorDolar();
			this.valorEuro = cambio.getValorEuro();
//			this.data = cambio.getData();
			if(cambio.getValorEuro().equals(new BigDecimal(0)) || cambio.getValorDolar().equals(new BigDecimal(0))) {
				this.valorEuroDolar = new BigDecimal(0);
			} else {
				this.valorEuroDolar = MonetaryUtil.divide(cambio.getValorEuro(), cambio.getValorDolar());
			}
		}
		
		
		if(rel != null && cotacao != null) {
			this.relValorMercadoriaCotacaoId = rel.getId();
			this.valorMercadoria = rel.getValorMercadoria();
			this.valorCustoImportacao = rel.getValorCustoImportacao();
			this.cotacaoId = cotacao.getId();
			this.cotacaoNome = cotacao.getNome();
			this.cotacaoDescricao = cotacao.getDescricao();
			this.cotacaoNacional = cotacao.getNacional();
			
			this.valorMercadoriaSaca60kgReal = rel.getValorMercadoriaReal();
			this.valorMercadoriaSaca60kgDolar = rel.getValorMercadoriaDolar();

			this.valorCustoImportacaoSaca60kgReal = rel.getValorCustoImportacaoReal();
			this.valorCustoImportacaoSaca60kgDolar = rel.getValorCustoImportacaoDolar();
			
			this.valorColetado = rel.getValorColetado();
			
//			this.valorMercadoriaSaca60kgReal = MonetaryUtil.multiply(MonetaryUtil.divide(rel.getValorMercadoria(), new BigDecimal(1000)), new BigDecimal(60));
//			this.valorMercadoriaSaca60kgReal = MonetaryUtil.multiply(valorMercadoriaSaca60kgReal, valorEuroParametro);
//			this.valorMercadoriaSaca60kgDolar = MonetaryUtil.divide(valorMercadoriaSaca60kgReal, valorDolarParametro);
//			
//			this.valorCustoImportacaoSaca60kgReal = MonetaryUtil.multiply(MonetaryUtil.divide(rel.getValorCustoImportacao(), new BigDecimal(1000)), new BigDecimal(60));
//			this.valorCustoImportacaoSaca60kgReal = MonetaryUtil.multiply(valorCustoImportacaoSaca60kgReal, valorEuroParametro);
//			this.valorCustoImportacaoSaca60kgDolar = MonetaryUtil.divide(valorCustoImportacaoSaca60kgReal, valorDolarParametro);
		}
	}

	public BigDecimal getValorReal() {
		return valorReal;
	}

	public BigDecimal getValorDolar() {
		return valorDolar;
	}

	public BigDecimal getValorEuro() {
		return valorEuro;
	}

	public BigDecimal getValorEuroDolar() {
		return valorEuroDolar;
	}

	public Long getData() {
		return data;
	}

	public BigDecimal getValorMercadoria() {
		return valorMercadoria;
	}

	public Long getCotacaoId() {
		return cotacaoId;
	}

	public String getCotacaoNome() {
		return cotacaoNome;
	}

	public int getCotacaoNacional() {
		return cotacaoNacional;
	}

	public BigDecimal getValorCustoImportacao() {
		return valorCustoImportacao;
	}

	public int getStatus() {
		return status;
	}

	public Long getCambioId() {
		return cambioId;
	}

	public Long getRelValorMercadoriaCotacaoId() {
		return relValorMercadoriaCotacaoId;
	}

	public BigDecimal getValorCustoImportacaoSaca60kgReal() {
		return valorCustoImportacaoSaca60kgReal;
	}

	public BigDecimal getValorMercadoriaSaca60kgReal() {
		return valorMercadoriaSaca60kgReal;
	}

	public BigDecimal getValorCustoImportacaoSaca60kgDolar() {
		return valorCustoImportacaoSaca60kgDolar;
	}

	public BigDecimal getValorMercadoriaSaca60kgDolar() {
		return valorMercadoriaSaca60kgDolar;
	}

	public String getCotacaoDescricao() {
		return cotacaoDescricao;
	}

	public BigDecimal getValorColetado() {
		return valorColetado;
	}
}
