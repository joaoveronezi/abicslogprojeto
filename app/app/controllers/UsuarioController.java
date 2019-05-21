package controllers;

import java.util.List;

import models.Usuario;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import util.AbicsLogConfig;
import util.EmailUtil;
import util.FileUtil;
import util.SecurityUtil;
import entitys.request.UsuarioRequest;
import entitys.response.AbstractResponse;

public class UsuarioController extends AbstractController {

    public static Result findAll() {
    	
    	List<Usuario> usuarioList = Usuario.findAll();
    	return ok(Json.toJson(usuarioList));
    }
    
    public static Result cadastrarUsuario() {
    	
    	Form<UsuarioRequest> form = Form.form(UsuarioRequest.class);
    	form = form.bindFromRequest();
    	UsuarioRequest usuarioRequest = form.get();

    	AbstractResponse response = null;
    	String emailRequest = usuarioRequest.getEmail();
    	Usuario usuarioExiste = Usuario.findByEmail(emailRequest);
    	
    	if(usuarioExiste != null && usuarioExiste.getId() != 0) {
    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
    				"Usuário com este email ja existe");
    		return ok(Json.toJson(response));
    	}
    	
    	
    	String login = emailRequest.split("@")[0];
    	String senhaCriada = Usuario.criarSenha(login);

    	Usuario usuarioNovo = new Usuario();
    	usuarioNovo.setLogin(login);
    	usuarioNovo.setEmail(emailRequest);
    	usuarioNovo.setSenha(SecurityUtil.toMD5(senhaCriada));
    	usuarioNovo.setPermissao(usuarioRequest.getPermissao());
    	usuarioNovo.setPrimeiroLogin(0);
    	usuarioNovo.setTipoRelatorio(0);
    	usuarioNovo.setNome("");
    	
    	boolean salvarSucesso = Usuario.salvar(usuarioNovo);

    	if(salvarSucesso) {
    		
    		EmailUtil.enviarEmailBoasVindas(usuarioNovo, senhaCriada, getUsuarioLogado());
    		FileUtil.criarPasta(AbicsLogConfig.getString(AbicsLogConfig.USER_REPORT_FOLDER) + usuarioNovo.getId() + "/");
    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
    				"Usuario convidado com sucesso");
    	} else {
    		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
    				"Não foi possível convidar usuario");
    	}
    	
    	return ok(Json.toJson(response));
    }
    
    public static Result removerUsuario(Long idUsuario) {

    	Usuario usuarioRemover = Usuario.findById(String.valueOf(idUsuario));
    	String nomeUsuarioRemovido = usuarioRemover.getNome();
    	
    	boolean removerSucesso = Usuario.remover(usuarioRemover);
    	
    	AbstractResponse response = null;
    	if(removerSucesso) {
    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
    				"Usuario removido com sucesso");
    	} else {
    		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
    				"Não foi possível remover usuario");
    	}
    	
    	return ok(Json.toJson(response));
    }
    
    public static Result ativarUsuario(Long idUsuario) {

    	Usuario usuarioAtivar = Usuario.findById(String.valueOf(idUsuario));
    	boolean ativarSucesso = Usuario.atualizar(usuarioAtivar);
    	
    	AbstractResponse response = null;
    	if(ativarSucesso) {
    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
    				"Usuario ativado com sucesso");
    	} else {
    		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
    				"Não foi possível ativar usuario");
    	}
    	
    	return ok(Json.toJson(response));
    }
    
    
    public static Result atualizar() {
    	
    	Form<UsuarioRequest> form = Form.form(UsuarioRequest.class);
    	form = form.bindFromRequest();
    	UsuarioRequest usuarioRequest = form.get();
    	
    	Usuario usuarioNovo = new Usuario();
    	usuarioNovo.setId(getUsuarioLogado().getId());
    	
    	if(usuarioRequest.getSenha() != null && !usuarioRequest.getSenha().isEmpty()) {
    		usuarioNovo.setSenha(SecurityUtil.toMD5(usuarioRequest.getSenha()));
    	}
    	
    	usuarioNovo.setPrimeiroLogin(1);
    	usuarioNovo.setNome(usuarioRequest.getNome());
    	usuarioNovo.setTipoRelatorio(usuarioRequest.getTipoRelatorio());
    	
    	boolean salvarSucesso = Usuario.atualizar(usuarioNovo);
    	
    	AbstractResponse response = null;
    	if(salvarSucesso) {
    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
    				"Usuario atualizado com sucesso");
    	} else {
    		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
    				"Não foi possível atualizado usuario");
    	}
    	
    	return ok(Json.toJson(response));
    }
}