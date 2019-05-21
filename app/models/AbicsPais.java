package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;
		
@Entity
@Table(name="abics_pais")
public class AbicsPais extends Model {

	private static final long serialVersionUID = 1L;

	private static Finder<Long,AbicsPais> find = new Finder<Long,AbicsPais>(Long.class, AbicsPais.class);


	public static AbicsPais findById(Long id) {
		return find.where().eq("id", id).findUnique();
	}
	
	public static List<AbicsPais> findAll() {
		return find.all();
	}

	public static AbicsPais findByNome(String nome){
		return find.where().eq("nome", nome).findUnique();
	}
	
	public static AbicsPais findByNomeToLowerCase(String nome){
		
		return find.where().ieq("nome", nome).findUnique();
		
//		String sql = "SELECT * FROM abics_pais WHERE nome = \"" + nome + "\"";
//		SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
//		
//	    SqlRow row = sqlQuery.findUnique();
//	    
//	    AbicsPais abicsPais = new AbicsPais();
//	    
//	    if(row != null) {
//	    	Long id = row.getLong("id");
//	    	String nomeRow = row.getString("nome");
//	    	
//	    	abicsPais.setId(id);
//	    	abicsPais.setNome(nomeRow);
//	    	
//	    	return abicsPais;
//	    	
//	    } else {
//	    	
//	    	return null;
//	    }
	}
	
	@Id
	private Long id;

	private String nome;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
