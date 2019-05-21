package entitys.response;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Id;

import util.MonetaryUtil;
import models.Cambio;

		
public class CambioResponse {
	
	private Long id;
	
	private BigDecimal valorReal;
	
	private BigDecimal valorDolar;
	
	private BigDecimal valorEuro;
	
	private BigDecimal valorEuroDolar;
	
	private Long data;
	
	public CambioResponse(Cambio cambio) {
		
		if(cambio != null) {
			this.id = cambio.getId();
			this.valorReal = cambio.getValorReal();
			this.valorDolar = cambio.getValorDolar();
			this.valorEuro = cambio.getValorEuro();
			this.data = cambio.getData();
			if(cambio.getValorEuro().equals(new BigDecimal(0)) || cambio.getValorDolar().equals(new BigDecimal(0))) {
				this.valorEuroDolar = new BigDecimal(0);
			} else {
				this.valorEuroDolar = MonetaryUtil.divide(cambio.getValorEuro(), cambio.getValorDolar());
			}
		}
			
	}

	public Long getId() {
		return id;
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
}
