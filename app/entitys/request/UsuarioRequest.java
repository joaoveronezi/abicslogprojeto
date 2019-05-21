package entitys.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class UsuarioRequest {

	private Long id;

	private String nome;

	private String login;

	private String senha;

	private String email;
	
	private Integer permissao;

	private Integer primeiroLogin;

	private Integer tipoRelatorio;
	
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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getPermissao() {
		return permissao;
	}

	public void setPermissao(Integer permissao) {
		this.permissao = permissao;
	}

	public Integer getPrimeiroLogin() {
		return primeiroLogin;
	}

	public void setPrimeiroLogin(Integer primeiroLogin) {
		this.primeiroLogin = primeiroLogin;
	}

	public Integer getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(Integer tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}
	
}