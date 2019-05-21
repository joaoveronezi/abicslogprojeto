package controllers;

import java.util.Map;

import models.Usuario;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import security.AbicsLogAuthenticator;
import util.EmailUtil;
import util.SecurityUtil;
import entitys.response.AbstractResponse;

public class IndexController extends Controller {

    public static Result index() {

		response().discardCookie(AbicsLogAuthenticator.COOKIE_USUARIO_ID);
		response().discardCookie(AbicsLogAuthenticator.COOKIE_USUARIO_PERMISSAO);
    	return ok(views.html.index.render());
    }

    public static Result logarSistema() {
    	
    	DynamicForm requestData = Form.form().bindFromRequest();
    	Map<String, String> emailRequestMap = requestData.data();
    	String login = emailRequestMap.get("login");
    	String senha = emailRequestMap.get("senha");
    	
    	AbstractResponse response = null;
    	Usuario usuarioLogado = Usuario.logarSistema(login, senha);
    	
    	if(usuarioLogado == null || usuarioLogado.getId() == null) {
    		
    		response().discardCookie(AbicsLogAuthenticator.COOKIE_USUARIO_ID);
    		response().discardCookie(AbicsLogAuthenticator.COOKIE_USUARIO_PERMISSAO);

    		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
					"Usuario e/ou senha incorreto!");
    		
    		return ok(Json.toJson(response));
    	}

		response().discardCookie(AbicsLogAuthenticator.COOKIE_USUARIO_ID);
		response().discardCookie(AbicsLogAuthenticator.COOKIE_USUARIO_PERMISSAO);
    	
    	String tokenUsuario = usuarioLogado.criarToken();
    	response().setCookie(AbicsLogAuthenticator.COOKIE_USUARIO_ID, tokenUsuario);
    	response().setCookie(AbicsLogAuthenticator.COOKIE_USUARIO_PERMISSAO, usuarioLogado.getPermissao().toString());
		
		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
				"Bem vindo " + usuarioLogado.getNome() + "!");
    	return ok(Json.toJson(response));
    }
    
    public static Result recuperarSenha() {
    	
    	DynamicForm requestData = Form.form().bindFromRequest();
    	Map<String, String> emailRequestMap = requestData.data();
    	String email = emailRequestMap.get("email");

    	AbstractResponse response = null;
    	
    	if(email != null && !email.isEmpty()) {
    		Usuario usuarioRecuperar = Usuario.findByEmail(email);
    		
    		if(usuarioRecuperar != null) {
    			
    	    	String login = email.split("@")[0];
    	    	String senha = Usuario.criarSenha(login);
    	    	
    	    	usuarioRecuperar.setSenha(SecurityUtil.toMD5(senha));
    	    	usuarioRecuperar.setPrimeiroLogin(0);
    	    	Usuario.atualizar(usuarioRecuperar);

    	    	EmailUtil.enviarEmailRecuperarSenha(usuarioRecuperar, senha);
    			
    			response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
    					"Email enviado com sucesso para: " + email);
    		} else {
    			response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
    					"Não existe usuario com o email: " + email);
    		}
    	} else {
			response = new AbstractResponse(AbstractResponse.IconEnum.WARNING_SIGN.getDescricao(), AbstractResponse.TypeEnum.WARNING.getDescricao(), 
					"Email é obrigatório!!");	
    	}

    	return ok(Json.toJson(response));
    }
    
    public static Result graficoRelatorio() {
    	
    	System.out.println("chegou");
    	
    	return ok();
    }
    		
}
