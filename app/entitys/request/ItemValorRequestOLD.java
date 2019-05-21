package entitys.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class ItemValorRequestOLD {

	private Long itemId;
	
	private String valor;

	private String itemNome;
	
	private String formula;
	
	private String tipo;
	
	private Integer tipoMoeda;
	
	private List<Long> idItens;
	
	private List<String> variavelFormulaList;
	
	private List<Integer> ordemItemList;
	
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getItemNome() {
		return itemNome;
	}

	public void setItemNome(String itemNome) {
		this.itemNome = itemNome;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<Long> getIdItens() {
		return idItens;
	}

	public void setIdItens(List<Long> idItens) {
		this.idItens = idItens;
	}

	public List<String> getVariavelFormulaList() {
		return variavelFormulaList;
	}

	public void setVariavelFormulaList(List<String> variavelFormulaList) {
		this.variavelFormulaList = variavelFormulaList;
	}

	public List<Integer> getOrdemItemList() {
		return ordemItemList;
	}

	public void setOrdemItemList(List<Integer> ordemItemList) {
		this.ordemItemList = ordemItemList;
	}

	public Integer getTipoMoeda() {
		return tipoMoeda;
	}

	public void setTipoMoeda(Integer tipoMoeda) {
		this.tipoMoeda = tipoMoeda;
	}
}