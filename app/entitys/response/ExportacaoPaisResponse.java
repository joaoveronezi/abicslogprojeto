package entitys.response;

import models.AbicsExportacaoPais;
import models.ComtradePartner;

public class ExportacaoPaisResponse {
	
	private Long id;
	
	private int ano;
	
	private int mes;
	
	private Long peso;
	
	private Long receita;
	
	private Long saca;
	
	private Long paisId;
	
	private String paisNomeComtrade;
	
	public ExportacaoPaisResponse(AbicsExportacaoPais exportacaoPais, ComtradePartner pais) {
		
		if(exportacaoPais != null) {
			this.id = exportacaoPais.getId();
			this.ano = exportacaoPais.getAno();
			this.mes = exportacaoPais.getMes();
			this.peso = exportacaoPais.getPeso();
			this.receita = exportacaoPais.getReceita();
			this.saca = exportacaoPais.getSaca60kg();
			this.paisId = exportacaoPais.getPaisId();
		}
		if(pais != null){
			paisNomeComtrade = pais.getText();
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

	public Long getPaisId() {
		return paisId;
	}

	public String getPaisNomeComtrade() {
		return paisNomeComtrade;
	}
	
}