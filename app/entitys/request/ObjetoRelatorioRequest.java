package entitys.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class ObjetoRelatorioRequest {

	private String tipoAgrupamento;
	private String dataInicial;
	private String dataFinal;
	private List<Integer> idItensSelecionado;
	
	public String getTipoAgrupamento() {
		return tipoAgrupamento;
	}
	
	public void setTipoAgrupamento(String tipoAgrupamento) {
		this.tipoAgrupamento = tipoAgrupamento;
	}
	
	public String getDataInicial() {
		return dataInicial;
	}
	
	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}
	
	public String getDataFinal() {
		return dataFinal;
	}
	
	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	public List<Integer> getIdItensSelecionado() {
		return idItensSelecionado;
	}
	
	public void setIdItensSelecionado(List<Integer> idItensSelecionado) {
		this.idItensSelecionado = idItensSelecionado;
	}
}
