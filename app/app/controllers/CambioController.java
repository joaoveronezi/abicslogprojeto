package controllers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import models.Cambio;
import models.Categoria;
import models.Cotacao;
import models.Item;
import models.RelCategoriaItem;
import models.RelCotacaoCategoria;
import models.RelItemValor;
import models.RelValorMercadoriaCotacao;
import models.SumarizacaoSincronizacao;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import util.DataUtil;
import util.MonetaryUtil;
import entitys.request.CambioRequest;
import entitys.response.AbstractResponse;
import entitys.response.GraficoCategoriesColumnsResponse;
import entitys.response.RelValorMercadoriaCotacaoResponse;

public class CambioController extends AbstractController {

	private final static DateTimeZone dtz = DateTimeZone.forID("America/Sao_Paulo");
	
	
    public static Result findAll() {
    	
    	List<Cambio> cambioList = Cambio.findAll();
    	return ok(Json.toJson(cambioList));
    }
    
    public static Result findByPeriodo(Long dataInicialUnix) {
    	
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dataInicialUnix * 1000);
		int qteDiasMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.add(Calendar.DAY_OF_MONTH, qteDiasMes - 1);
    	
		Long dataFinalUnix = calendar.getTimeInMillis() / 1000;
		
    	List<Cambio> cambioList = Cambio.findByPeriodo(dataInicialUnix, dataFinalUnix);
    	return ok(Json.toJson(cambioList));
    }
    
    public static Result findValorMercadoriaByPeriodo(Long dataInicialUnix) {
    	
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dataInicialUnix * 1000);
		int qteDiasMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.add(Calendar.DAY_OF_MONTH, qteDiasMes - 1);
    	
		Long dataFinalUnix = calendar.getTimeInMillis() / 1000;
    	List<RelValorMercadoriaCotacao> relList = RelValorMercadoriaCotacao.findByPeriodo(dataInicialUnix, dataFinalUnix);
    	List<RelValorMercadoriaCotacaoResponse> responseList = new ArrayList<RelValorMercadoriaCotacaoResponse>();
    	
    	for(RelValorMercadoriaCotacao vmc : relList) {
    		
    		Cotacao cotacao = Cotacao.findById(vmc.getCotacaoId());
//    		Cambio cambio = Cambio.findByData(vmc.getData());
    		
//    		RelValorMercadoriaCotacaoResponse response = new RelValorMercadoriaCotacaoResponse(vmc, cotacao, cambio);
    		RelValorMercadoriaCotacaoResponse response = new RelValorMercadoriaCotacaoResponse(vmc, cotacao);
    		responseList.add(response);
    	}
    	
    	return ok(Json.toJson(responseList));
    }
    public static Result findAllCotacao() {
    	
    	List<Cotacao> cotacaoList = Cotacao.findAll();
    	return ok(Json.toJson(cotacaoList));
    }
    
    public static Result findAllValorMercadoria() {
    	
    	List<RelValorMercadoriaCotacao> relList = RelValorMercadoriaCotacao.findAll();
    	List<RelValorMercadoriaCotacaoResponse> responseList = new ArrayList<RelValorMercadoriaCotacaoResponse>();
    	
    	for(RelValorMercadoriaCotacao vmc : relList) {
    		
    		Cotacao cotacao = Cotacao.findById(vmc.getCotacaoId());
//    		Cambio cambio = Cambio.findByData(vmc.getData());
    		
//    		RelValorMercadoriaCotacaoResponse response = new RelValorMercadoriaCotacaoResponse(vmc, cotacao, cambio);
    		RelValorMercadoriaCotacaoResponse response = new RelValorMercadoriaCotacaoResponse(vmc, cotacao);
    		responseList.add(response);
    	}
    	
    	return ok(Json.toJson(responseList));
    }
    
    public static Result cadastrarCambio() {

    	Form<CambioRequest> form = Form.form(CambioRequest.class);
    	form = form.bindFromRequest();
    	CambioRequest cambioRequest = form.get();
    	cambioRequest.setValorDolar(cambioRequest.getValorDolar().replace(",", "."));
    	cambioRequest.setValorEuro(cambioRequest.getValorEuro().replace(",", "."));
    	AbstractResponse response = null;
    	
    	Date date = Date.from( Instant.ofEpochSecond( cambioRequest.getData() / 1000 ) );

        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        Long unixtimeWithoutLocale = cal.getTimeInMillis() / 1000; 
    	
    	Cambio cambioExiste = Cambio.findByData(unixtimeWithoutLocale);
    	if(cambioExiste != null) {
    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
    				"Já existe cambio com nesta data");
    		return ok(Json.toJson(response));
    	}
    	
    	Cambio cambioSalvo = new Cambio();
    	cambioSalvo.setData(unixtimeWithoutLocale);
    	cambioSalvo.setValorReal(new BigDecimal(1));
    	cambioSalvo.setValorEuro(new BigDecimal(cambioRequest.getValorEuro()));
    	cambioSalvo.setValorDolar(new BigDecimal(cambioRequest.getValorDolar()));
    	
    	boolean salvoSucesso = Cambio.salvar(cambioSalvo);
    	if(salvoSucesso) {
    		
    		SumarizacaoSincronizacao sumarizacao = new SumarizacaoSincronizacao();
    		sumarizacao.setCambioId(cambioSalvo.getId());
    		sumarizacao.setData(cambioSalvo.getData());
    		sumarizacao.setTipoSincronizacao(SumarizacaoSincronizacao.TipoSincronizacao.MANUAL.getCodigo());
    		SumarizacaoSincronizacao.salvar(sumarizacao);
//    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
    				"Câmbio cadastrado com sucesso");
    	} else {
    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
    				"Não foi possível de cadastrar câmbio");
    	}
    	
    	return ok(Json.toJson(response));
    }
    
    public static Result atualizarCambio() {

    	Form<CambioRequest> form = Form.form(CambioRequest.class);
    	form = form.bindFromRequest();
    	CambioRequest cambioRequest = form.get();
    	cambioRequest.setValorDolar(cambioRequest.getValorDolar().replace(",", "."));
    	cambioRequest.setValorEuro(cambioRequest.getValorEuro().replace(",", "."));
    	AbstractResponse response = null;
    	
    	Cambio cambioEditar = Cambio.findById(cambioRequest.getId());
    	cambioEditar.setValorDolar(new BigDecimal(cambioRequest.getValorDolar()));
    	cambioEditar.setValorEuro(new BigDecimal(cambioRequest.getValorEuro()));
    	cambioEditar.setValorReal(new BigDecimal(1));
    	
    	boolean atualizadoSucesso = Cambio.atualizar(cambioEditar);
    	if(atualizadoSucesso) {
    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
    				"Câmbio editado com sucesso");
    	} else {
    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
    				"Não foi possível editar o câmbio");
    	}
    	
    	return ok(Json.toJson(response));
    }
    
    public static Result atualizarValorMercadoria() {

    	AbstractResponse response = null;
    	DynamicForm requestData = Form.form().bindFromRequest();
    	Map<String, String> emailRequestMap = requestData.data();
    	String idRelValorMercadoriaCotacaoStr = emailRequestMap.get("id");
    	String valorColetadoStr = emailRequestMap.get("valorColetado");
    	valorColetadoStr = valorColetadoStr.replace(",", ".");
    	Long idRelValorMercadoriaCotacao = Long.valueOf(idRelValorMercadoriaCotacaoStr);
    	
    	RelValorMercadoriaCotacao relValorMercadoriaCotacaoEditar = RelValorMercadoriaCotacao.findById(idRelValorMercadoriaCotacao);
    	Cotacao cotacao = Cotacao.findById(relValorMercadoriaCotacaoEditar.getCotacaoId());
    	Cambio cambioResponse = Cambio.findByData(relValorMercadoriaCotacaoEditar.getData());
    	BigDecimal valorColetado = new BigDecimal(valorColetadoStr);
    	calcularRelValorMercadoriaCotacao(cotacao, cambioResponse, valorColetado, relValorMercadoriaCotacaoEditar);
    	
    	boolean atualizadoSucesso = RelValorMercadoriaCotacao.atualizar(relValorMercadoriaCotacaoEditar);
    	
    	if(atualizadoSucesso) {
    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
    				"Valor Mercadoria editado com sucesso");
    	} else {
    		
    		response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
    				"Não foi possível editar o Valor Mercadoria");
    	}
    	
    	return ok(Json.toJson(response));
    } 
    
    public static Result findGraficoVariacaoCambio(Long dataInicialUnix) {
    	
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dataInicialUnix * 1000);
		int qteDiasMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
    	GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();
		List<String> euroColumns = new ArrayList<String>();
		List<String> dolarColumns = new ArrayList<String>();
		dolarColumns.add("dolar");
		euroColumns.add("euro");
			
		for(int i = 1; i <= qteDiasMes; i++) {
			
			categories.add(String.valueOf(i));
    		
			LocalDateTime data = new LocalDateTime(dataInicialUnix * 1000);
			data = data.withDayOfMonth(i);
			
			int horaDia = 0;
		    if (dtz.isLocalDateTimeGap(data)) {
		    	horaDia = 1;
		    }
			
		    Long dataInicial = data.withMillisOfSecond(0).withSecondOfMinute(0).withMinuteOfHour(0).withHourOfDay(horaDia).toDateTime().getMillis() / 1000;
		    
			Cambio cambioDia = Cambio.findByData(dataInicial);
			if(cambioDia == null) {
				dolarColumns.add(new BigDecimal(0.0).toString());
				euroColumns.add(new BigDecimal(0.0).toString());
			} else {
				dolarColumns.add(cambioDia.getValorDolar().toString());
				euroColumns.add(cambioDia.getValorEuro().toString());
			}
			
			graficoItemValorEntity.addColumns(dolarColumns);
			graficoItemValorEntity.addColumns(euroColumns);
		}
		
		graficoItemValorEntity.setCategories(categories);
		
		
		return ok(Json.toJson(graficoItemValorEntity));
    }
    
    public static Result findGraficoVariacaoValoresMercadoria(Long dataInicialUnix) {
    	
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dataInicialUnix * 1000);
		int qteDiasMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
    	GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();
    	
    	for(int i = 1; i <= qteDiasMes; i++) {
			categories.add(String.valueOf(i));
		}		
		graficoItemValorEntity.setCategories(categories);
		
		List<Cotacao> cotacoes = Cotacao.findAll();
		
		for(Cotacao cotacao : cotacoes) {

			List<String> columns = new ArrayList<String>();
			columns.add(cotacao.getNome());
			for(int i = 1; i <= qteDiasMes; i++) {
	    		
				LocalDateTime data = new LocalDateTime(dataInicialUnix * 1000);
				data = data.withDayOfMonth(i);
				
				int horaDia = 0;
			    if (dtz.isLocalDateTimeGap(data)) {
			    	horaDia = 1;
			    }
				
			    Long dataInicial = data.withMillisOfSecond(0).withSecondOfMinute(0).withMinuteOfHour(0).withHourOfDay(horaDia).toDateTime().getMillis() / 1000;
	
			    RelValorMercadoriaCotacao valorMercadoriaCotacao = RelValorMercadoriaCotacao.findByDataCotacao(dataInicial, cotacao.getId()); 
			    if(valorMercadoriaCotacao != null && valorMercadoriaCotacao.getValorCustoImportacao() != null) {
			    	
			    	BigDecimal valorSaca60kg = valorMercadoriaCotacao.getValorCustoImportacaoReal();
			    	columns.add(valorSaca60kg.toString());
			    	
			    } else {
			    	columns.add(new BigDecimal(0.0).toString());
			    }
			    
				graficoItemValorEntity.addColumns(columns);
			}
		}		
		
		return ok(Json.toJson(graficoItemValorEntity));
    }
    
    public static Result cadastrarValorMercadoriaCotacao() {

    	AbstractResponse response = null;
    	DynamicForm requestData = Form.form().bindFromRequest();
    	Map<String, String> emailRequestMap = requestData.data();
    	String idCotacaoStr = emailRequestMap.get("idCotacao");
    	String valorColetadoStr = emailRequestMap.get("valorColetado");
    	valorColetadoStr = valorColetadoStr.replace(",", ".");
    	String dataCadastroStr = emailRequestMap.get("dataCadastro");
    	
    	Long idCotacao = Long.valueOf(idCotacaoStr);
    	Cotacao cotacao = Cotacao.findById(idCotacao);
    	BigDecimal valorColetado = new BigDecimal(valorColetadoStr);
    	Long dataCadastro = Long.valueOf(dataCadastroStr);
    	
		Date date = Date.from( Instant.ofEpochSecond( dataCadastro / 1000 ) );
		
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);  
		cal.set(Calendar.HOUR_OF_DAY, 0);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, 0);  
		cal.set(Calendar.MILLISECOND, 0);  
		Long unixtimeWithoutLocale = cal.getTimeInMillis() / 1000; 
		
		RelValorMercadoriaCotacao existeRel = RelValorMercadoriaCotacao.findByDataCotacao(unixtimeWithoutLocale, idCotacao);
		Cambio cambioResponse = Cambio.findByData(unixtimeWithoutLocale);
		
		if(cambioResponse == null) {
			response = new AbstractResponse(AbstractResponse.IconEnum.REMOVE.getDescricao(), AbstractResponse.TypeEnum.DANGER.getDescricao(), 
					"Não existe cambio para este dia: " + DataUtil.sdfDataBR.format(cal.getTime()));
	    	return ok(Json.toJson(response));
		}
		
		if(existeRel == null) {
			RelValorMercadoriaCotacao novoRel = new RelValorMercadoriaCotacao();
			calcularRelValorMercadoriaCotacao(cotacao, cambioResponse, valorColetado, novoRel);
			novoRel.save();
			
    		SumarizacaoSincronizacao sumarizacao = new SumarizacaoSincronizacao();
    		sumarizacao.setRelValorMercadoriaCotacaoId(novoRel.getId());
    		sumarizacao.setData(novoRel.getData());
    		sumarizacao.setTipoSincronizacao(SumarizacaoSincronizacao.TipoSincronizacao.MANUAL.getCodigo());
    		SumarizacaoSincronizacao.salvar(sumarizacao);
    		response = new AbstractResponse(AbstractResponse.IconEnum.THUMBS_UP.getDescricao(), AbstractResponse.TypeEnum.SUCCESS.getDescricao(), 
    				"Valores cadastrados");
    		return ok(Json.toJson(response));
		} else {
			
    		response = new AbstractResponse(AbstractResponse.IconEnum.WARNING_SIGN.getDescricao(), AbstractResponse.TypeEnum.WARNING.getDescricao(), 
    				"Ja existe valor: " + existeRel.getValorColetado() + " para esta cotação: " + cotacao.getNome() + " " + cotacao.getDescricao());
    		return ok(Json.toJson(response));

		}
    }

	public static RelValorMercadoriaCotacao calcularRelValorMercadoriaCotacao(Cotacao cotacao, Cambio cambio, 
			BigDecimal valorColetado, RelValorMercadoriaCotacao relRetorno) {
		
		BigDecimal valorSacaReal = new BigDecimal(0.0);
		BigDecimal valorSacaDolar = new BigDecimal(0.0);
		BigDecimal valorSacaEuro = new BigDecimal(0.0);
		
		relRetorno.setCotacaoId(cotacao.getId());
		relRetorno.setData(cambio.getData());

		if(cotacao.getNacional() == 1) {
			
			valorSacaReal = valorColetado;
			valorSacaDolar = MonetaryUtil.divide(valorColetado, cambio.getValorDolar());
			valorSacaEuro = MonetaryUtil.divide(valorColetado, cambio.getValorEuro());
			
		} else {
			
			valorSacaReal = MonetaryUtil.divide(valorColetado, new BigDecimal(1000));
			valorSacaReal = MonetaryUtil.multiply(valorSacaReal, new BigDecimal(60));
			if(cotacao.getId() != 6L) { //Londres
				valorSacaReal = MonetaryUtil.multiply(valorSacaReal, cambio.getValorEuro());
			} else {
				valorSacaReal = MonetaryUtil.multiply(valorSacaReal, cambio.getValorDolar());
			}
			
			valorSacaDolar = MonetaryUtil.divide(valorSacaReal, cambio.getValorDolar());
			valorSacaEuro = MonetaryUtil.divide(valorSacaReal, cambio.getValorEuro());
		}
		
		relRetorno.setValorColetado(valorColetado);
		relRetorno.setValorMercadoria(valorSacaEuro);
		relRetorno.setValorMercadoriaDolar(valorSacaDolar);
		relRetorno.setValorMercadoriaReal(valorSacaReal);
		
		BigDecimal valorLogisticaCustoImportacaoReal = new BigDecimal(0.0);
		BigDecimal valorLogisticaCustoImportacaoDolar = new BigDecimal(0.0);
		
		List<RelCotacaoCategoria> cotacaoCategorias = RelCotacaoCategoria.findByCotacao(cotacao.getId());
		for(RelCotacaoCategoria relCotCat : cotacaoCategorias) {
			
			Categoria categoria = Categoria.findById(relCotCat.getCategoriaId());
			List<RelCategoriaItem> categoriaItens = RelCategoriaItem.findByCategoria(categoria.getId());

			for(RelCategoriaItem relCatItem : categoriaItens) {
				
				Item item = Item.findById(relCatItem.getItemId());
				RelItemValor relItemValor = RelItemValor.findByItemUltimo(item.getId());
				
				if(relItemValor != null) {
					
					if(relItemValor.getUnidade() == RelItemValor.Unidade.REAL.getCodigo()) {
						valorLogisticaCustoImportacaoReal = MonetaryUtil.add(valorLogisticaCustoImportacaoReal, relItemValor.getValor());
						valorLogisticaCustoImportacaoDolar = MonetaryUtil.add(valorLogisticaCustoImportacaoDolar, MonetaryUtil.divide(relItemValor.getValor(), cambio.getValorDolar()));
					} else if(relItemValor.getUnidade() == RelItemValor.Unidade.DOLAR.getCodigo()) {
						valorLogisticaCustoImportacaoReal = MonetaryUtil.add(valorLogisticaCustoImportacaoReal, MonetaryUtil.multiply(relItemValor.getValor(), cambio.getValorDolar()));
						valorLogisticaCustoImportacaoDolar = MonetaryUtil.add(valorLogisticaCustoImportacaoDolar, relItemValor.getValor());
					} else if(relItemValor.getUnidade() == RelItemValor.Unidade.PERCENTUAL.getCodigo()) {
						valorLogisticaCustoImportacaoReal = MonetaryUtil.add(valorLogisticaCustoImportacaoReal, relItemValor.getValor());
						valorLogisticaCustoImportacaoDolar = MonetaryUtil.add(valorLogisticaCustoImportacaoDolar, relItemValor.getValor());
					}
				}
				
			}
			
		}
		
		valorLogisticaCustoImportacaoReal = MonetaryUtil.divide(valorLogisticaCustoImportacaoReal, new BigDecimal(320));
		valorLogisticaCustoImportacaoDolar = MonetaryUtil.divide(valorLogisticaCustoImportacaoDolar, new BigDecimal(320));
		
		valorLogisticaCustoImportacaoReal = MonetaryUtil.add(valorLogisticaCustoImportacaoReal, relRetorno.getValorMercadoriaReal());
		valorLogisticaCustoImportacaoDolar = MonetaryUtil.add(valorLogisticaCustoImportacaoDolar, relRetorno.getValorMercadoriaDolar());
		
		relRetorno.setValorCustoImportacao(new BigDecimal(0));
		relRetorno.setValorCustoImportacaoDolar(valorLogisticaCustoImportacaoDolar);
		relRetorno.setValorCustoImportacaoReal(valorLogisticaCustoImportacaoReal);
		
		return relRetorno;
	}
    
}