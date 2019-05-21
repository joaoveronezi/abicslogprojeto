import play.GlobalSettings;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import security.AbicsLogAuthenticator;

public class AbicsLogGlobal extends GlobalSettings {
	

	@Override
	public Promise<Result> onHandlerNotFound(RequestHeader request) {
		
		if(request.cookie(AbicsLogAuthenticator.COOKIE_USUARIO_ID) == null) {
			return Promise.<Result>pure(play.mvc.Results.notFound(views.html.index.render()));
		}
		
		String usuarioTokenAuth = request.cookie(AbicsLogAuthenticator.COOKIE_USUARIO_ID).value();
		if(usuarioTokenAuth != null) {
			return Promise.<Result>pure(play.mvc.Results.notFound(views.html.template.render()));
		}
		
		return Promise.<Result>pure(play.mvc.Results.notFound(views.html.index.render()));
	}
}
