package security;

import models.Usuario;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticator;

public class AbicsLogAuthenticator extends Authenticator {

//	public static String COOKIE_X_AUTH_TOKEN = "X-AUTH-TOKEN";
	public static String COOKIE_USUARIO_ID = "___ID";
	public static String COOKIE_USUARIO_PERMISSAO = "___P";
			
    @Override
    public String getUsername(Context ctx) {
    	
    	if(ctx.request().cookie(COOKIE_USUARIO_ID) == null) {
    		return null;
    	}
    	
    	String usuarioTokenAuth = ctx.request().cookie(COOKIE_USUARIO_ID).value();//ctx.session().get(COOKIE_USUARIO_ID);
    	
        if(usuarioTokenAuth != null) {
        	
        	Usuario user = Usuario.findById(Usuario.extrairToken(usuarioTokenAuth));
        	if(user != null) {
            	
        		return usuarioTokenAuth;
        	} else {
        		System.out.println("usuario nao encontrado, token invalido");
        		return null;
        	}
        	
        } else {
        	
        	System.out.println("nao existe token na requisicao");
        	return null;
        }
    }

    @Override
    public Result onUnauthorized(Context ctx) {
    	
        return redirect(controllers.routes.IndexController.index());
    }
}
