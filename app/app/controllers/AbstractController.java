package controllers;

import models.Usuario;
import play.mvc.Controller;
import play.mvc.Security.Authenticated;
import security.AbicsLogAuthenticator;

@Authenticated(AbicsLogAuthenticator.class)
public abstract class AbstractController extends Controller {

	public static Usuario getUsuarioLogado() {
		
    	String usuarioId = ctx().request().cookie(AbicsLogAuthenticator.COOKIE_USUARIO_ID).value();
    	Usuario usuarioLogado = Usuario.findById(usuarioId);
    	return usuarioLogado;
	}
	
}
