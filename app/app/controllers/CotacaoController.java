package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Categoria;
import models.Cotacao;
import models.RelCotacaoCategoria;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import entitys.request.CotacaoCategoriaRequest;
import entitys.response.AbstractResponse;
import entitys.response.RelCotacaoCategoriaResponse;

public class CotacaoController extends AbstractController {

    public static Result findAll() {
    	
    	List<Cotacao> cotacaoList = Cotacao.findAtivo(1);
    	return ok(Json.toJson(cotacaoList));
    }
    
    public static Result cadastrarCotacao() {
    	
    	Form<Cotacao> form = Form.form(Cotacao.class);
    	form = form.bindFromRequest();
    	Cotacao cotacaoRequest = form.get();
    	
    	boolean salvarSucesso = Cotacao.salvar(cotacaoRequest);
    	
    	AbstractResponse response = null;
    	if(salvarSucesso) {
    		
       		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
        			"Cotação adicionado com sucesso");
    	} else {
       		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
       				"Não foi possível adicionar cotação");
    	}
    	
    	return ok(Json.toJson(response));
    }
    
    public static Result alterarCotacao() {
    	
    	Form<Cotacao> form = Form.form(Cotacao.class);
    	form = form.bindFromRequest();
    	Cotacao cotacaoRequest = form.get();
    	
    	boolean salvarSucesso = Cotacao.atualizar(cotacaoRequest);
    	
    	AbstractResponse response = null;
    	if(salvarSucesso) {
    		
       		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
        			"Cotação alterada com sucesso");
    	} else {
       		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
       				"Não foi possível alterar cotação");
    	}
    	
    	return ok(Json.toJson(response));
    }
    
    public static Result removerCotacao(Long idCotacao) {

    	Cotacao cotacaoRemover = Cotacao.findById(idCotacao);
    	String nomeCotacaoRemovido = cotacaoRemover.getNome();
    	
    	List<RelCotacaoCategoria> relCotacaoCategoriaList = RelCotacaoCategoria.findByCotacao(Long.valueOf(cotacaoRemover.getId()));

    	for(RelCotacaoCategoria rel : relCotacaoCategoriaList) {
    		rel.delete();
    	}
 
    	boolean removerSucesso = Cotacao.remover(cotacaoRemover);
    	
    	AbstractResponse response = null;
    	if(removerSucesso) {
    		
       		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
        			"Cotação removido com sucesso");
    	} else {
       		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
       				"Não foi possível remover cotação");
    	}
    	
    	return ok(Json.toJson(response));
    }
    
    public static Result findAllCategoria() {
    	
    	List<Categoria> categoriaList = Categoria.findAll();
    	return ok(Json.toJson(categoriaList));
    }
    
    public static Result findCategoriasCotacao(Long idCotacao) {
    	
    	List<RelCotacaoCategoriaResponse> responseList = new ArrayList<RelCotacaoCategoriaResponse>();
    	List<RelCotacaoCategoria> relCotacaoCategoriaList = RelCotacaoCategoria.findByCotacao(idCotacao);
    	
    	for(RelCotacaoCategoria rel : relCotacaoCategoriaList) {
    		Categoria c = Categoria.findById(Long.valueOf(rel.getCategoriaId()));
    		RelCotacaoCategoriaResponse response = new RelCotacaoCategoriaResponse(c, rel.getOrdem());
    		responseList.add(response);
    	}
    	
    	return ok(Json.toJson(responseList));
    }
    
    public static Result vincularCategoriaCotacao() {
    	
    	Form<CotacaoCategoriaRequest> form = Form.form(CotacaoCategoriaRequest.class);
    	form = form.bindFromRequest();
    	CotacaoCategoriaRequest cotacaoCategoriaRequest = form.get();
    	
    	//remove os antigos
    	List<RelCotacaoCategoria> olds = RelCotacaoCategoria.findByCotacao(cotacaoCategoriaRequest.getCotacaoId());
    	if(olds != null && !olds.isEmpty()) {
    		for(RelCotacaoCategoria rel : olds) {
    			rel.delete();
    		}
    	}
    	
    	//salva os novos
    	if(cotacaoCategoriaRequest.getIdCategorias() != null && !cotacaoCategoriaRequest.getIdCategorias().isEmpty()) {
//        	for(Long idCategoria : cotacaoCategoriaRequest.gcotacaoCategoriaRequestetIdCategorias()) {
        	for(int i = 0; i < cotacaoCategoriaRequest.getIdCategorias().size(); i++) {
//        		Long idItem : categoriaItemRequest.getIdItens()
        		Long idCategoria = cotacaoCategoriaRequest.getIdCategorias().get(i);
        		Integer ordemNovo = cotacaoCategoriaRequest.getOrdemList().get(i);
        		
        		RelCotacaoCategoria relCotacaoCategoria = new RelCotacaoCategoria();
        		relCotacaoCategoria.setCotacaoId(cotacaoCategoriaRequest.getCotacaoId());
        		relCotacaoCategoria.setCategoriaId(idCategoria);
        		relCotacaoCategoria.setOrdem(ordemNovo);
        		relCotacaoCategoria.save();
        	}    		
    	}
    	
    	AbstractResponse response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
    			"Categorias vinculados a cotação com sucesso");
    	return ok(Json.toJson(response));
    }
}