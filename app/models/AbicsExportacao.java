package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;
		
@Entity
@Table(name="abics_exportacao")
public class AbicsExportacao extends Model {
	
	private static final long serialVersionUID = 1082655520877249798L;

	private static Finder<Long,AbicsExportacao> find = 
			new Finder<Long,AbicsExportacao>(Long.class, AbicsExportacao.class);
	
	public static List<AbicsExportacao> findAll() {
		
		return find.orderBy("ano").findList();
	}
	
	public static AbicsExportacao findById(Long id) {
		
		return find.byId(id);
	}
	
	public static AbicsExportacao findByAnoMes(int ano, int mes) {
		
		return find.where().eq("ano", ano).eq("mes", mes).findUnique();
	}
	
	public static AbicsExportacao findByAnoMesEmpresaId(int ano, int mes, Long empresaId) {
		
		return find.where().eq("ano", ano).eq("mes", mes).eq("empresaId", empresaId).findUnique();
	}
	
	public static boolean salvar(AbicsExportacao abics) {
		
		try {
			abics.save();
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public static boolean atualizar(AbicsExportacao abics) {
		
		try {
			abics.update();
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

}
