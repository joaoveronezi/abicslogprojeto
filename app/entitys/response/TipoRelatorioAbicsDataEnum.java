package entitys.response;

public enum TipoRelatorioAbicsDataEnum {
	
	DESEMPENHO_MENSAL(0),
	EXPORTADOR_MENSAL(1),
	EXPORTADOR_ACUMULADO(2),
	PAIS_MENSAL(3),
	PAIS_ACUMULADO(4),
	PRODUTO_MENSAL(5),
	PRODUTO_ACUMULADO(6),
	PAIS_ANUAL(7),
	PRODUTO_ANUAL(8),
	PAIS_NCM_21011190(9),
	PAIS_NCM_21011200(10);
	
	private Integer tipo;
	
	private TipoRelatorioAbicsDataEnum(Integer tipo) {
		this.tipo = tipo;
	}

	public Integer getTipo() {
		return tipo;
	}
	
}
