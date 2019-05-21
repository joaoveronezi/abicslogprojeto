package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Categoria;
import models.Item;
import models.RelCategoriaItem;
import models.RelCotacaoCategoria;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import entitys.request.CategoriaItemRequest;
import entitys.response.AbstractResponse;
import entitys.response.ItemResponse;

public class CategoriaController extends AbstractController {

    public static Result findAll() {
    	
    	List<Categoria> categoriaList = Categoria.findAll();
    	return ok(Json.toJson(categoriaList));
    }

    public static Result findItemCategoria(Long idCategoria) {
    	
    	List<RelCategoriaItem> itensCategoria = RelCategoriaItem.findByCategoria(idCategoria);
    	List<ItemResponse> responseList = new ArrayList<ItemResponse>();
    	
    	for(RelCategoriaItem rel : itensCategoria) {
    		
    		Item item = Item.findById(Long.valueOf(rel.getItemId()));
    		ItemResponse response = new ItemResponse(item, rel.getOrdem(), rel.getIsCategoriaTotal());
    		responseList.add(response);
     	}
    	
    	return ok(Json.toJson(responseList));
    }
    
    public static Result findAllItem() {
    	
    	List<Item> itemList = Item.findAll();
    	return ok(Json.toJson(itemList));
    }

    public static Result vincularItemCategoria() {
    	
    	Form<CategoriaItemRequest> form = Form.form(CategoriaItemRequest.class);
    	form = form.bindFromRequest();
    	CategoriaItemRequest categoriaItemRequest = form.get();
    	
    	Long idTotalCategoria = categoriaItemRequest.getCategoriaIdTotalCategoria();
    	if(idTotalCategoria == null) {
    		idTotalCategoria = 0L;
    	}
    	
    	//remove os antigos
    	List<RelCategoriaItem> olds = RelCategoriaItem.findByCategoria(categoriaItemRequest.getCategoriaId());
    	if(olds != null && !olds.isEmpty()) {
    		for(RelCategoriaItem rel : olds) {
    			rel.delete();
    		}
    	}
    	
    	//salva os novos
    	if(categoriaItemRequest.getIdItens() != null && !categoriaItemRequest.getIdItens().isEmpty()) {
        	for(int i = 0; i < categoriaItemRequest.getIdItens().size(); i++) {
//        		Long idItem : categoriaItemRequest.getIdItens()
        		Long idItem = categoriaItemRequest.getIdItens().get(i);
        		Integer ordemNovo = categoriaItemRequest.getOrdemItemList().get(i);
        		
            	RelCategoriaItem relCategoriaItem = new RelCategoriaItem();
            	relCategoriaItem.setCategoriaId(categoriaItemRequest.getCategoriaId());
            	relCategoriaItem.setItemId(idItem);
            	relCategoriaItem.setOrdem(ordemNovo);
            	
            	if(idTotalCategoria.equals(idItem)) {
            		relCategoriaItem.setIsCategoriaTotal(1);
            	}
            	
            	relCategoriaItem.save();
        	}    		
    	}
    	
//    	Log log = new Log();
//    	log.setAcao("Adicionou itens a categoria " + categoriaItemRequest.getCategoriaId());
//    	log.setUsuarioId(getUsuarioLogado().getId());
//    	log.setData(Calendar.getInstance());
//    	log.save();
    	
    	AbstractResponse response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
    			"Itens vinculados a categoria com sucesso");
    	return ok(Json.toJson(response));
    }
    
    public static Result cadastrarCategoria() {
    	
    	Form<Categoria> form = Form.form(Categoria.class);
    	form = form.bindFromRequest();
    	Categoria categoriaRequest = form.get();
    	
    	boolean salvarSucesso = Categoria.salvar(categoriaRequest);
    	
    	AbstractResponse response = null;
    	if(salvarSucesso) {
    		
        	response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
        			"Categoria adicionado com sucesso");
    	} else {
    		
        	response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
        			"Não foi possível adicionar categoria");
    	}
    	
    	return ok(Json.toJson(response));
    }
    
    public static Result alterarCategoria() {
    	
    	Form<Categoria> form = Form.form(Categoria.class);
    	form = form.bindFromRequest();
    	Categoria categoriaRequest = form.get();
    	
    	boolean salvarSucesso = Categoria.atualizar(categoriaRequest);
    	
    	AbstractResponse response = null;
    	if(salvarSucesso) {
    		
        	response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
        			"Categoria alterada com sucesso");
    	} else {
    		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
        			"Não foi possível adicionar categoria");
    	}
    	
    	return ok(Json.toJson(response));
    }
    
    public static Result removerCategoria(Long idCategoria) {

    	Categoria categoriaRemover = Categoria.findById(idCategoria);
    	String nomeCategoriaRemovido = categoriaRemover.getNome();
    	
    	List<RelCotacaoCategoria> relCotacaoCategoriaList = RelCotacaoCategoria.findByCategoria(Long.valueOf(categoriaRemover.getId()));
    	for(RelCotacaoCategoria rel : relCotacaoCategoriaList) {
    		rel.delete();
    	}
 
    	List<RelCategoriaItem> relCategoriaItemList = RelCategoriaItem.findByCategoria(Long.valueOf(categoriaRemover.getId()));
    	for(RelCategoriaItem rel : relCategoriaItemList) {
    		rel.delete();
    	}
    	
    	boolean removerSucesso = Categoria.remover(categoriaRemover);
    	
    	AbstractResponse response = null;
    	if(removerSucesso) {
    		
    		
       		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
        			"Categoria removido com sucesso");
    	} else {
    		
       		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
        			"Não foi possível remover categoria");
    	}
    	
    	return ok(Json.toJson(response));
    }
}