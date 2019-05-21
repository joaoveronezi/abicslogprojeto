package entitys.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class CambioRequest {

	private Long id;
	
	private String valorReal;
	
	private String valorDolar;
	
	private String valorEuro;
	
	private Long data;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValorReal() {
		return valorReal;
	}

	public void setValorReal(String valorReal) {
		this.valorReal = valorReal;
	}

	public String getValorDolar() {
		return valorDolar;
	}

	public void setValorDolar(String valorDolar) {
		this.valorDolar = valorDolar;
	}

	public String getValorEuro() {
		return valorEuro;
	}

	public void setValorEuro(String valorEuro) {
		this.valorEuro = valorEuro;
	}

	public Long getData() {
		return data;
	}

	public void setData(Long data) {
		this.data = data;
	}
}