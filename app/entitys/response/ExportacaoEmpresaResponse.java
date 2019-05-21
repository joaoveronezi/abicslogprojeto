package entitys.response;

import models.AbicsEmpresa;
import models.AbicsExportacaoEmpresa;

		
public class ExportacaoEmpresaResponse {
	
	private Long id;
	
	private int ano;
	
	private int mes;
	
	private Long peso;
	
	private Long receita;
	
	private Long saca;
	
	private Long empresaId;
	
	private String empresaNome;
	
	public ExportacaoEmpresaResponse(AbicsExportacaoEmpresa exportacaoEmpresa, AbicsEmpresa empresa) {
		
		if(exportacaoEmpresa != null) {
			this.id = exportacaoEmpresa.getId();
			this.ano = exportacaoEmpresa.getAno();
			this.mes = exportacaoEmpresa.getMes();
			this.peso = exportacaoEmpresa.getPeso();
			this.receita = exportacaoEmpresa.getReceita();
			this.saca = exportacaoEmpresa.getSaca60kg();
			this.empresaId = exportacaoEmpresa.getEmpresaId();
		}
		if(empresa != null){
			this.empresaNome = empresa.getNome();
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

	public Long getEmpresaId() {
		return empresaId;
	}

	public String getEmpresaNome() {
		return empresaNome;
	}
	
}