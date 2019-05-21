package models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;
		
@Entity
@Table(name="abics_pais_acordo_comercial")
public class AbicsPaisAcordoComercial extends Model {

	private static final long serialVersionUID = -8544694919253025752L;

	private static Finder<Long,AbicsPaisAcordoComercial> find = new Finder<Long,AbicsPaisAcordoComercial>(Long.class, AbicsPaisAcordoComercial.class);

	public enum TipoMercado {

		PROJETO_APEX(0, "Projeto Apex"),
		MERCADO_LOCAL(1, "Mercado Local"),
		CONCORRENTE(2, "Concorrente"),
		GRANDE_IMPORTADOR(3, "Grande Importador");
		
		private Integer codigo;
		private String descricao;
		
		private TipoMercado(Integer codigo, String descricao) {
			this.codigo = codigo;
			this.descricao = descricao;
		}

		public Integer getCodigo() {
			return codigo;
		}
		
		public String getDescricao() {
			return descricao;
		}
	}
	
	
	@Id
	private Long id;

	@Column(name="mes")
	private Integer mes;

	@Column(name="ano")
	private Integer ano;

	@Column(name="pais_principal")
	private Long paisPrincipal;
	
	@Column(name="pais_Acordo")
	private Long paisAcordo;
	
	@Column(name="andamento")
	private String andamento;
	
	@Column(name="tipo_mercado")
	private Long tipoMercado;
	
	@Column(name="tarifa_brasil")
	private BigDecimal tarifaBrasil;
	
	@Column(name="tarifa_geral")
	private BigDecimal tarifaGeral;
	
	@Column(name="valor_importacao_cafe_soluvel")
	private BigDecimal valorImportacaoCafeSoluvel;
	
	@Column(name="market_share_brasil")
	private BigDecimal marketShareBrasil;

	@Column(name="market_share_pais_negociando")
	private BigDecimal marketSharePaisNegociando;

}
