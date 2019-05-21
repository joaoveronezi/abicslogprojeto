package entitys.response;

import models.Usuario;
		
public class UsuarioResponse {
	
	private Long id;

	private String nome;

	private String login;

	private String email;
	
	private Integer permissao;

	private Integer primeiroLogin;

	private Integer tipoRelatorio;
	
	public UsuarioResponse(Usuario usuario) {
		
		if(usuario != null) {
			this.id = usuario.getId();
			this.nome = usuario.getNome();
			this.login = usuario.getLogin();
			this.email = usuario.getEmail();
			this.permissao = usuario.getPermissao();
			this.primeiroLogin = usuario.getPrimeiroLogin();
			this.tipoRelatorio = usuario.getTipoRelatorio();
		}
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getLogin() {
		return login;
	}

	public String getEmail() {
		return email;
	}

	public Integer getPermissao() {
		return permissao;
	}

	public Integer getPrimeiroLogin() {
		return primeiroLogin;
	}

	public Integer getTipoRelatorio() {
		return tipoRelatorio;
	}
}
