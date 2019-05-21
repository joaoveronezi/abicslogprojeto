package entitys.response;

import models.Item;
		
/**
 * Diferente de outros *RESPONSE que sao construidos no construtor
 * */
public class EnumResponse {
	
	private Integer codigo;
	
	private String descricao;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
