package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

import play.db.ebean.Model;
		
@Entity
@Table(name="abics_empresa")
public class AbicsEmpresa extends Model {

	private static final long serialVersionUID = 1L;

	private static Finder<Long,AbicsEmpresa> find = new Finder<Long,AbicsEmpresa>(Long.class, AbicsEmpresa.class);


	public static AbicsEmpresa findById(Long id) {
		return find.where().eq("id", id).findUnique();
	}
	
	public static List<AbicsEmpresa> findAll() {
		return find.all();
	}

	public static AbicsEmpresa findByNome(String nome){
		return find.where().eq("nome", nome).findUnique();
	}
	
	public static AbicsEmpresa findByNomeToLowerCase(String nome){
		
		String sql = "SELECT * FROM abics_empresa WHERE nome = \"" + nome + "\"";
		SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
		
	    SqlRow row = sqlQuery.findUnique();
	    
	    AbicsEmpresa abicsEmpresa = new AbicsEmpresa();
	    
	    if(row != null) {
	    	Long id = row.getLong("id");
	    	String nomeRow = row.getString("nome");
	    	
	    	abicsEmpresa.setId(id);
	    	abicsEmpresa.setNome(nomeRow);
	    	
	    	return abicsEmpresa;
	    	
	    } else {
	    	
	    	return null;
	    }
	}
	
	public static boolean salvar(AbicsEmpresa empresa) {
		
		try {
			empresa.save();
			return true;
		} catch(Exception e) {
			return false;
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
