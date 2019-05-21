package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;
		
@Entity
@Table(name="abics_exportacao_produto")
public class AbicsExportacaoProduto extends Model {
	
	private static final long serialVersionUID = 1082655520877249798L;

	private static Finder<Long,AbicsExportacaoProduto> find = 
			new Finder<Long,AbicsExportacaoProduto>(Long.class, AbicsExportacaoProduto.class);
	
	public static List<AbicsExportacaoProduto> findAll() {
		
		return find.orderBy("ano").findList();
	}
	
	public static AbicsExportacaoProduto findById(Long id) {
		
		return find.byId(id);
	}
	
	public static List<AbicsExportacaoProduto> findByAnoMes(int ano, int mes) {
		
		return find.where().eq("ano", ano).eq("mes", mes).findList();
	}
	
	public static AbicsExportacaoProduto findByAnoMesProdutoId(int ano, int mes, Long produtoId) {
		
		return find.where().eq("ano", ano).eq("mes", mes).eq("produtoId", produtoId).findUnique();
	}
	
	public static boolean salvar(AbicsExportacaoProduto exportacaoProduto) {
		
		try {
			exportacaoProduto.save();
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public static boolean atualizar(AbicsExportacaoProduto exportacaoProduto) {
		
		try {
			exportacaoProduto.update();
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	@Id
	private Long id;
	
	private Integer ano;
	
	private Integer mes;
	
	private Long peso;
	
	private Long receita;
	
	private Long saca60kg;
	
	@Column(name="produto_id")
	private Long produtoId;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Long getPeso() {
		return peso;
	}

	public void setPeso(Long peso) {
		this.peso = peso;
	}

	public Long getReceita() {
		return receita;
	}

	public void setReceita(Long receita) {
		this.receita = receita;
	}

	public Long getSaca60kg() {
		return saca60kg;
	}

	public void setSaca60kg(Long saca60kg) {
		this.saca60kg = saca60kg;
	}

	public Long getProdutoId() {
		return produtoId;
	}

	public void setProdutoId(Long produtoId) {
		this.produtoId = produtoId;
	}

	@Override
	public String toString() {
		return "ExportacaoProduto [id=" + id + ", ano=" + ano + ", mes=" + mes
				+ ", peso=" + peso + ", receita=" + receita + ", saca60kg="
				+ saca60kg + ", produtoId=" + produtoId + "]";
	}
	
	

}
