package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
		
@Entity
@Table(name="abics_exportacao_pais")
public class AbicsExportacaoPais extends Model {
	
	private static final long serialVersionUID = 1082655520877249798L;

	private static Finder<Long,AbicsExportacaoPais> find = 
			new Finder<Long,AbicsExportacaoPais>(Long.class, AbicsExportacaoPais.class);
	
	public static List<Long> getMaioresReceitaAno(String ano, Integer qteMaiores) {
	    
//		SELECT SUM( receita ) , pais_id
//		FROM  `abics_exportacao_pais` 
//		WHERE ano =2016
//		GROUP BY pais_id
//		LIMIT 0 , 30
		
		String sql = "SELECT SUM(receita) as total, pais_id FROM abics_exportacao_pais WHERE ano = " + ano + " group by pais_id";

	    SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
	    List<SqlRow> row = sqlQuery.setMaxRows(qteMaiores).findList();
	    List<Long> idPaises = new ArrayList<Long>();
	    for(SqlRow r : row) {
	    	Long pId = r.getLong("pais_id");
	    	idPaises.add(pId);
	    }
	    return idPaises;
//	    return row.getLong("total");
	}
	
	public static String getMaioresReceitasByPaisIdAnoList(Long paisId, String[] listaAno) {
		
		String sql = "SELECT SUM(receita) AS total, pais_id FROM abics_exportacao_pais WHERE ano = ";
		String retorno = "";
		
		for(String ano: listaAno) {
			 sql += ano + " AND ";
			 sql += "pais_id = " + paisId;
			 sql += " OR ano = ";
		}
		
		int index = sql.lastIndexOf("OR");
		String split = sql.substring(0, index);
		sql = split + "GROUP BY pais_id";
		
		SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
		SqlRow row = sqlQuery.findUnique();
		
		if(row != null) {
			Long pId = row.getLong("pais_id");
			Long total = row.getLong("total");
			retorno = pId.toString();
			retorno += ",";
			retorno += total.toString();
		} else {
			retorno = paisId.toString();
			retorno += ",";
			retorno += "0";
		}
		
		return retorno;
	}
	
	public static List<AbicsExportacaoPais> findAll() {
		
		return find.orderBy("ano").findList();
	}
	
	public static AbicsExportacaoPais findById(Long id) {
		
		return find.byId(id);
	}
	
	public static List<AbicsExportacaoPais> findByAnoMes(int ano, int mes) {
		
		return find.where().eq("ano", ano).eq("mes", mes).findList();
	}
	
	public static AbicsExportacaoPais findByAnoMesPaisId(int ano, int mes, Long paisId) {
		
		return find.where().eq("ano", ano).eq("mes", mes).eq("paisId", paisId).findUnique();
	}
	
	public static List<AbicsExportacaoPais> findMaioresByYr(Integer pfCode, Integer yr, Integer qteMaiores) {
		
		return find.where().eq("yr", yr).eq("pfCode", pfCode).ne("ptCode", 0).orderBy("totalTradeValue DESC").findList();
	}
	
	public static List<AbicsExportacaoPais> findByAnoPaisId(int ano, Long paisId){
		return find.where().eq("ano", ano).eq("paisId", paisId).findList();
	}
	
	public static boolean salvar(AbicsExportacaoPais exportacaoPais) {
		
		try {
			exportacaoPais.save();
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public static boolean atualizar(AbicsExportacaoPais exportacaoPais) {
		
		try {
			exportacaoPais.update();
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
	
	@Column(name="pais_id")
	private Long paisId;
	
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

	public Long getPaisId() {
		return paisId;
	}

	public void setPaisId(Long paisId) {
		this.paisId = paisId;
	}

	@Override
	public String toString() {
		return "AbicsExportacaoPais [id=" + id + ", ano=" + ano + ", mes="
				+ mes + ", peso=" + peso + ", receita=" + receita
				+ ", saca60kg=" + saca60kg + ", paisId=" + paisId + "]\n";
	}

}
