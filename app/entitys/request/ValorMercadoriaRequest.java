package entitys.request;

import java.math.BigDecimal;
import java.util.List;

import models.RelValorMercadoriaCotacao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class ValorMercadoriaRequest {

	private List<RelValorMercadoriaCotacao> listaRelValorMercadoriaCotacao;

	public List<RelValorMercadoriaCotacao> getListaRelValorMercadoriaCotacao() {
		return listaRelValorMercadoriaCotacao;
	}

	public void setListaRelValorMercadoriaCotacao(List<RelValorMercadoriaCotacao> listaRelValorMercadoriaCotacao) {
		this.listaRelValorMercadoriaCotacao = listaRelValorMercadoriaCotacao;
	}
}