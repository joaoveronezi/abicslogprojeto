package controllers;

import models.Usuario;
import play.libs.Json;
import play.mvc.Result;
import security.AbicsLogAuthenticator;
import entitys.response.UsuarioResponse;

//@Authenticated(ProjetoCIMAuthenticator.class)
public class TemplateController extends AbstractController {

    public static Result redirectLogin() {
//    	return ok(views.html.index.render());	
    	return ok(views.html.template.render());
    }
    
    public static Result usuarioLogado() {

    	Usuario usuarioLogado = getUsuarioLogado();
//    	String usuarioId = ctx().request().cookie(AbicsLogAuthenticator.COOKIE_USUARIO_ID).value();
//    	Usuario usuarioLogado = Usuario.findById(usuarioId);
    	UsuarioResponse response = new UsuarioResponse(usuarioLogado);
    	return ok(Json.toJson(response));
    }
//    
    public static Result logoutSistema() {
	
//    	session().clear();
    	response().discardCookie(AbicsLogAuthenticator.COOKIE_USUARIO_ID);
    	response().discardCookie(AbicsLogAuthenticator.COOKIE_USUARIO_PERMISSAO);
    	response().getHeaders().clear();

//    	MessageResponse response = new MessageResponse("s", "logout sistema");
    	return ok(Json.toJson("response"));
    }
    
}
