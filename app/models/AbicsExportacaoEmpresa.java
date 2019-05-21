package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;
		
@Entity
@Table(name="abics_exportacao_empresa")
public class AbicsExportacaoEmpresa extends Model {
	
	private static final long serialVersionUID = 1082655520877249798L;

	private static Finder<Long,AbicsExportacaoEmpresa> find = 
			new Finder<Long,AbicsExportacaoEmpresa>(Long.class, AbicsExportacaoEmpresa.class);
	
	public static List<AbicsExportacaoEmpresa> findAll() {
		
		return find.orderBy("ano").findList();
	}
	
	public static AbicsExportacaoEmpresa findById(Long id) {
		
		return find.byId(id);
	}
	
	public static List<AbicsExportacaoEmpresa> findByAnoMes(int ano, int mes) {
		
		return find.where().eq("ano", ano).eq("mes", mes).findList();
	}
	
	public static AbicsExportacaoEmpresa findByAnoMesEmpresaId(int ano, int mes, Long empresaId) {
		
		return find.where().eq("ano", ano).eq("mes", mes).eq("empresaId", empresaId).findUnique();
	}
	
	public static boolean salvar(AbicsExportacaoEmpresa exportacaoEmpresa) {
		
		try {
			exportacaoEmpresa.save();
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public static boolean atualizar(AbicsExportacaoEmpresa exportacaoEmpresa) {
		
		try {
			exportacaoEmpresa.update();
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
	
	@Column(name="empresa_id")
	private Long empresaId;


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

	public Long getEmpresaId() {
		return empresaId;
	}

	public void setEmpresaId(Long empresaId) {
		this.empresaId = empresaId;
	}

}
