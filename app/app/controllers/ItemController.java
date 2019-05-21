package controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import models.Item;
import models.RelCategoriaItem;
import models.RelItemValor;
import models.RelItemValor.Unidade;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import util.DataUtil;
import entitys.request.ItemValorRequest;
import entitys.response.AbstractResponse;
import entitys.response.EnumResponse;
import entitys.response.ItemValorResponse;

public class ItemController extends AbstractController {

	
//	public static void main(String[] args) {
//		
//		try {
//
//		    ScriptEngineManager mgr = new ScriptEngineManager();
//		    ScriptEngine engine = mgr.getEngineByName("JavaScript");
//		    String foo = "40+2";
//		    System.out.println(engine.eval(foo));
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	

	
//	public static void main(String[] args) {
//		try {
//			
//			List<ItemValorRequest> itens = new ArrayList<ItemValorRequest>();
//			ItemValorRequest item1 = new ItemValorRequest();
//			item1.setValor("10.50");
//			itens.add(item1);
//			ItemValorRequest item2 = new ItemValorRequest();
//			item2.setValor("25.20");
//			itens.add(item2);
//			ItemValorRequest item3 = new ItemValorRequest();
//			item3.setValor("3");
//			itens.add(item3);			
//			
//			String formula = "(A + B) * C"; // A * (( B + (( C * ( B + D + E ) / (0.83))) / 0.9075
//			  int tam = itens.size();
//			  String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//			  
//			  for(int i = 0; i < tam; i++){
//				  formula = formula.replaceAll(Character.toString(letras.charAt(i)), itens.get(i).getValor());
//			  }
//			  
//			  //Para calcular o valor da formula
//			  ScriptEngineManager mgr = new ScriptEngineManager();
//			  ScriptEngine engine = mgr.getEngineByName("JavaScript");
//			  
//			  System.out.println(formula);
//			  
//			  String valor = engine.eval(formula).toString();
//			  System.out.println(valor);
//			  
//			  
//			  BigDecimal result = null;
//
//			  Expression expression = new Expression("1+1/3");
//			  result = expression.eval();
//			  expression.setPrecision(2);
//			  result = expression.eval();
//
//			  result = new Expression("(3.4 + -4.1)/2").eval();
//
//			  System.out.println(result);
//			  
//			  result = new Expression("SQRT(a^2 + b^2)").with("a","2.4").and("b","9.253").eval();
//
//			  System.out.println(result);
//			  
//			  BigDecimal a = new BigDecimal("2.4");
//			  BigDecimal b = new BigDecimal("9.235");
//			  result = new Expression("SQRT(a^2 + b^2)").with("a",a).and("b",b).eval();
//
//			  System.out.println(result);
//			  
//			  result = new Expression("2.4/PI").setPrecision(128).setRoundingMode(RoundingMode.UP).eval();
//
//			  System.out.println(result);
//			  
//			  result = new Expression("random() > 0.5").eval();
//
//			  System.out.println(result);
//			  
//			  result = new Expression("not(x<7 || sqrt(max(x,9,3,min(4,3))) <= 3)").with("x","22.9").eval();
//
//			  System.out.println(result);
//			  
//			  result = new Expression("log10(100)").eval();
//
//			  System.out.println(result);
//			  
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	
//	public static Result getEnumVisivel() {
//		
//		Visivel[] values = Item.Visivel.values();
//		List<EnumResponse> response = new ArrayList<EnumResponse>();
//		for(Visivel v : values) {
//			EnumResponse er = new EnumResponse();
//			er.setCodigo(v.getCodigo());
//			er.setDescricao(v.getDescricao());
//			response.add(er);
//		}
//		
//		return ok(Json.toJson(response));
//	}

	public static Result getEnumUnidade() {
		
		Unidade[] values = RelItemValor.Unidade.values();
		List<EnumResponse> response = new ArrayList<EnumResponse>();
		for(Unidade v : values) {
			EnumResponse er = new EnumResponse();
			er.setCodigo(v.getCodigo());
			er.setDescricao(v.getDescricao());
			response.add(er);
		}
		return ok(Json.toJson(response));
	}
	
//	public static Result getEnumTipo() {
//		
//		Tipo[] values = Item.Tipo.values();
//		List<EnumResponse> response = new ArrayList<EnumResponse>();
//		for(Tipo v : values) {
//			EnumResponse er = new EnumResponse();
//			er.setCodigo(v.getCodigo());
//			er.setDescricao(v.getDescricao());
//			response.add(er);
//		}
//		return ok(Json.toJson(response));
//	}
	
    public static Result findAll() {
    	
    	List<Item> itemList = Item.findAll();
    	List<ItemValorResponse> response = new ArrayList<ItemValorResponse>();
    	for(Item i : itemList) {
    		RelItemValor v = RelItemValor.findByItemUltimo(i.getId());
    		ItemValorResponse ivr = new ItemValorResponse(i, v, 0);
    		response.add(ivr);
    	}
    	
    	return ok(Json.toJson(response));
    }
    
    public static Result cadastrarItem() {

    	Form<ItemValorRequest> form = Form.form(ItemValorRequest.class);
    	form = form.bindFromRequest();
    	ItemValorRequest itemRequest = form.get();
    	
    	AbstractResponse response = null;
    	boolean salvoSucesso = false;
    	
    	Item item = new Item();
    	item.setNome(itemRequest.getNome());
    	item.setDescricao(itemRequest.getDescricao());
//    	item.setUnidade(itemRequest.getUnidade());
    	Item.salvar(item);
    	
    	RelItemValor relItemValor = new RelItemValor();
    	relItemValor.setData(DataUtil.nowUnixtimeWithoutLocale());
    	relItemValor.setValor(new BigDecimal(itemRequest.getValor()));
    	relItemValor.setItemId(item.getId());
    	relItemValor.setUnidade(itemRequest.getUnidade());
    	salvoSucesso = RelItemValor.salvar(relItemValor);

    	if(salvoSucesso) {
    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
    				"Item cadastrado com sucesso");
    	} else {
    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
    				"Item não foi possível de cadastrar");
    	}
    	
    	return ok(Json.toJson(response));
    }
    
    public static Result alterarItem() {

    	Form<ItemValorRequest> form = Form.form(ItemValorRequest.class);
    	form = form.bindFromRequest();
    	ItemValorRequest itemRequest = form.get();
    	
    	AbstractResponse response = null;
    	boolean atualizadoSucesso = false;//Item.alterar(itemRequest);
    	
    	Item item = Item.findById(itemRequest.getId());
    	item.setNome(itemRequest.getNome());
    	item.setDescricao(itemRequest.getDescricao());
//    	item.setUnidade(itemRequest.getUnidade());
    	Item.alterar(item);
    	
    	RelItemValor relItemValor = new RelItemValor();
    	relItemValor.setData(DataUtil.nowUnixtimeWithoutLocale());
    	relItemValor.setValor(new BigDecimal(itemRequest.getValor()));
    	relItemValor.setItemId(item.getId());
    	relItemValor.setUnidade(itemRequest.getUnidade());
    	atualizadoSucesso = RelItemValor.salvar(relItemValor);
    	
    	if(atualizadoSucesso) {
    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
    				"Item editado com sucesso");
    	} else {
    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
    				"Item não foi possível de editar");
    	}
    	
    	return ok(Json.toJson(response));
    }
    
//    public static Result adicionaValorItem() {
//    	
//    	Form<ItemValorRequestOLD> form = Form.form(ItemValorRequestOLD.class);
//    	form = form.bindFromRequest();
//    	ItemValorRequestOLD itemValorRequest = form.get();
//    	AbstractResponse response = null;
//    	boolean salvoSucesso = false;
//    	
////    	if(itemValorRequest.getTipo().equals(Item.Tipo.INFORMADO_USUARIO.getCodigo().toString())) {
////        	
////    		if(itemValorRequest.getValor() == null) {
////        		
////        		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
////        				"Valores obrigatórios");
////    			
////        		return ok(Json.toJson(response));
////        	}
////    		
////    		RelItemValor valor = new RelItemValor();
////    		valor.setData(DataUtil.nowUnixtimeWithoutLocale());
////    		valor.setValor(new BigDecimal(itemValorRequest.getValor()));
////    		valor.setItemId(itemValorRequest.getItemId());
////    		valor.setTipoMoeda(itemValorRequest.getTipoMoeda());
////    		
////    		salvoSucesso = RelItemValor.salvar(valor);
////    		
////    	} else {
////    		
////    		Item itemEditar = Item.findById(Long.valueOf(itemValorRequest.getItemId()));
////    		itemEditar.setFormula(itemValorRequest.getFormula());
////    		salvoSucesso = Item.salvar(itemEditar);
////
////    		List<RelItemItemVariavel> relItemItemList = RelItemItemVariavel.findAllByItemPai(itemEditar.getId());
////    		if(relItemItemList != null && !relItemItemList.isEmpty()) {
////    			for(RelItemItemVariavel rel : relItemItemList) {
////    				rel.delete();
////    			}
////    		}
////    		
////    		if(itemValorRequest != null && itemValorRequest.getIdItens() != null && !itemValorRequest.getIdItens().isEmpty()) {
////    			
////    			for(int i = 0; i < itemValorRequest.getIdItens().size(); i++) {
////    				
////    				Long idNovo = itemValorRequest.getIdItens().get(i);
////    				String variavelNovo = itemValorRequest.getVariavelFormulaList().get(i);
////    				Integer ordemNovo = itemValorRequest.getOrdemItemList().get(i);
////    				
////    				RelItemItemVariavel novoRel = new RelItemItemVariavel();
////    				novoRel.setVariavel(variavelNovo);
////    				novoRel.setItemPai(itemEditar.getId());
////    				novoRel.setItemFilho(idNovo);
////    				novoRel.setOrdem(ordemNovo);
////    				novoRel.save();
////    				
////    			}
////    			
////    		}
////    	}
//    	
//		if(salvoSucesso) {
//    		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
//    				"Atualizado com sucesso");
//    	} else {
//    		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
//    				"Não foi possível atualizar");
//		}
//    	return ok(Json.toJson(response));
//    }
    
//    private static BigDecimal calcularValor(String formula, List<RelItemValor> itens) {
//
//    	try {
//    		
//			  int tam = itens.size();
//			  String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//			  
//			  for(int i = 0; i < tam; i++){
//				  formula = formula.toUpperCase().replaceAll(Character.toString(letras.charAt(i)), String.valueOf(itens.get(i).getValor()));
//			  }
//			  
//			  BigDecimal result = new BigDecimal(0.0);
//			  Expression expression = new Expression(formula);
//			  result = expression.eval();
//			  return result;
//		} catch(Exception e) {
//
//			e.printStackTrace();
//			return null;
//		}
//    }
    
    public static Result removerItem(Long idItem) {
    	
    	Item itemRemove = Item.findById(idItem);
    	List<RelItemValor> valoresItem = RelItemValor.findAllByItem(Long.valueOf(itemRemove.getId()));
    	String nomeItem = itemRemove.getNome();
    	
    	for(RelItemValor v : valoresItem) {
    		RelItemValor.remover(v);
    	}

    	List<RelCategoriaItem> relCategoriaItemList = RelCategoriaItem.findByItem(idItem);
    	for(RelCategoriaItem rel : relCategoriaItemList) {
    		rel.delete();
    	}
    	
    	AbstractResponse response = null;
    	boolean removidoSucesso = Item.remover(itemRemove);
    	
    	if(removidoSucesso) {
    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
    				"Item removido com sucesso");
    	} else {
    		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
    				"Item não foi possível de remover");
    	}
    	
    	return ok(Json.toJson(response));
    }
    
    
    public static Result findAllItemValorByItem(Long idItem) {
    	
    	List<RelItemValor> itemValorList = RelItemValor.findAllByItem(idItem);
    	Item item = Item.findById(idItem);
    	List<ItemValorResponse> responseList = new ArrayList<ItemValorResponse>();
    	int ordem = 0;
    	for(RelItemValor rel : itemValorList) {
    		ItemValorResponse response = new ItemValorResponse(item, rel, ordem);
    		responseList.add(response);
    		ordem++;
    	}
    	
    	return ok(Json.toJson(responseList));
    }
}