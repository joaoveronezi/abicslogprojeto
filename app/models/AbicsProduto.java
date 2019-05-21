package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

import play.db.ebean.Model;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
		
@Entity
@Table(name="abics_produto")
public class AbicsProduto extends Model {

	private static final long serialVersionUID = 1L;

	public static final long PAIS_NCM_21011190 = 3L;
	public static final long PAIS_NCM_21011200 = 5L;
	
	
	private static Finder<Long,AbicsProduto> find = new Finder<Long,AbicsProduto>(Long.class, AbicsProduto.class);


	public static AbicsProduto findById(Long id) {
		return find.where().eq("id", id).findUnique();
	}
	
	public static List<AbicsProduto> findAll() {
		return find.all();
	}

	public static AbicsProduto findByNome(String nome){
		return find.where().eq("nome", nome).findUnique();
	}
	
	public static AbicsProduto findByNomeToLowerCase(String nome){
		
		String sql = "SELECT * FROM abics_produto WHERE nome = \"" + nome + "\"";
		SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
		
	    SqlRow row = sqlQuery.findUnique();
	    
	    AbicsProduto abicsProduto = new AbicsProduto();
	    
	    if(row != null) {
	    	Long id = row.getLong("id");
	    	String nomeRow = row.getString("nome");
	    	
	    	abicsProduto.setId(id);
	    	abicsProduto.setNome(nomeRow);
	    	
	    	return abicsProduto;
	    	
	    } else {
	    	
	    	return null;
	    }
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
