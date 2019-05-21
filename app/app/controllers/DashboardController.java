package controllers;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import models.Cambio;
import models.Categoria;
import models.Cotacao;
import models.Item;
import models.RelCategoriaItem;
import models.RelCotacaoCategoria;
import models.RelItemValor;
import models.RelValorMercadoriaCotacao;
import play.libs.Json;
import play.mvc.Result;
import util.MonetaryUtil;
import entitys.report.RelatorioExcelMensalAcumulado;
import entitys.response.CambioResponse;
import entitys.response.CategoriaRelatorioResponse;
import entitys.response.CotacaoRelatorioResponse;
import entitys.response.GraficoCategoriesColumnsResponse;
import entitys.response.ItemRelatorioResponse;

public class DashboardController extends AbstractController {

	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");
	private final static int qteDiasAtras = -30;
//	private final static Calendar calendar31Agosto = Calendar.getInstance();
	
	public static Result findAllCotacao() {

		List<Cotacao> cotacaoList = Cotacao.findAtivo(1);
		return ok(Json.toJson(cotacaoList));
	}
	
	public static Result findCambioByData(Long dataUnixtime) {

    	Date date = Date.from( Instant.ofEpochSecond( dataUnixtime / 1000 ) );

        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        Long unixtimeWithoutLocale = cal.getTimeInMillis() / 1000; 
		
		Cambio cambio = Cambio.findByData(unixtimeWithoutLocale);
		if(cambio == null) {
			cambio = new Cambio();
			cambio.setData(unixtimeWithoutLocale);
			cambio.setValorDolar(new BigDecimal((0)));
			cambio.setValorEuro(new BigDecimal((0)));
			cambio.setValorReal(new BigDecimal((0)));
		}
		
		CambioResponse cambioResponse = new CambioResponse(cambio);
		return ok(Json.toJson(cambioResponse));
	}  
	
	public static Result graficoEvolucaoPreco(Long dataFinalunix, String tipoMoeda, Boolean aplicarLogistica) {
	    	
		Long dataInicialUnix = 0L;
		List<Cotacao> cotacoesDesordenada = Cotacao.findAll();//new ArrayList<Cotacao>();
		
		//cotacoes ordenadas
		List<Cotacao> cotacoes = new ArrayList<Cotacao>();
		cotacoes.add(null);
		cotacoes.add(null);
		cotacoes.add(null);
		cotacoes.add(null);
		cotacoes.add(null);
		cotacoes.add(null);
		
		//ordena cotações
		for(Cotacao cotacao: cotacoesDesordenada) {
			if(cotacao.getNome().equalsIgnoreCase("Indonésia")) {
				cotacoes.set(0, cotacao);
				
			} else if(cotacao.getNome().equalsIgnoreCase("Vietnam")) {
				cotacoes.set(1, cotacao);
				
			} else if(cotacao.getNome().equalsIgnoreCase("Bolsa de Londres")) {
				cotacoes.set(2, cotacao);
			
			} else if(cotacao.getNome().equalsIgnoreCase("Espírito Santo")) {
				if(cotacao.getDescricao().equalsIgnoreCase("(CCCV)")) {
					cotacoes.set(3, cotacao);
					
				} else {
					cotacoes.set(4, cotacao);
				}
			} else if(cotacao.getNome().equalsIgnoreCase("Rondônia")) {
				cotacoes.set(5, cotacao);
			}
		}
		
//		calendar31Agosto.set(2016, 7, 31);
		
		Calendar calendarInicial = Calendar.getInstance();
		calendarInicial.setTimeInMillis(dataFinalunix);
		calendarInicial.set(Calendar.HOUR_OF_DAY, 0);  
		calendarInicial.set(Calendar.MINUTE, 0);  
		calendarInicial.set(Calendar.SECOND, 0);  
		calendarInicial.set(Calendar.MILLISECOND, 0); 
		calendarInicial.add(Calendar.DAY_OF_YEAR, qteDiasAtras);
		dataInicialUnix = calendarInicial.getTimeInMillis() / 1000;
		
		Calendar calendarFinal = Calendar.getInstance();
		calendarFinal.setTimeInMillis(dataFinalunix);
		
		Date firstDate = calendarInicial.getTime();
     	Date secondDate = calendarFinal.getTime();

        long diff = secondDate.getTime() - firstDate.getTime();
		Long qteDiasMes =  diff / (24 * 60 * 60 * 1000);

		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();
    	
    	Calendar calendarCategories = Calendar.getInstance();
    	calendarCategories.setTimeInMillis(dataInicialUnix * 1000);

    	for(int i = 1; i <= qteDiasMes; i++) {
    		calendarCategories.add(Calendar.DAY_OF_YEAR, 1);
    		String diaMes = simpleDateFormat.format(calendarCategories.getTime());
			categories.add(diaMes);
		}
		graficoItemValorEntity.setCategories(categories);

		for(Cotacao cotacao : cotacoes) {

			calendarCategories.add(Calendar.DAY_OF_MONTH, -qteDiasMes.intValue());
			List<String> columns = new ArrayList<String>();
			columns.add(cotacao.getNome() + " " + cotacao.getDescricao().toLowerCase());
			
			for(int i = 1; i <= qteDiasMes; i++) {
				
				calendarCategories.add(Calendar.DAY_OF_YEAR, 1);
				calendarCategories.set(Calendar.HOUR_OF_DAY, 0);  
				calendarCategories.set(Calendar.MINUTE, 0);  
				calendarCategories.set(Calendar.SECOND, 0);  
				calendarCategories.set(Calendar.MILLISECOND, 0);  
		        Long unixtimeWithoutLocale = calendarCategories.getTimeInMillis() / 1000;

//				if(calendarCategories.getTime().after(calendar31Agosto.getTime()) && cotacao.getId() == 7L) {
//					continue;
//				}
		        
		        BigDecimal valorMercadoria = new BigDecimal(0.0);
		        
			    RelValorMercadoriaCotacao valorMercadoriaCotacao = RelValorMercadoriaCotacao.findByDataCotacao(unixtimeWithoutLocale, cotacao.getId()); 
			    if(valorMercadoriaCotacao != null && valorMercadoriaCotacao.getValorCustoImportacao() != null) {
			    	
			    	Cambio cambio = Cambio.findByData(valorMercadoriaCotacao.getData());
			    	
		    		if(tipoMoeda.equals("dolar")) {
		    			if(aplicarLogistica) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoDolar();
		    			} else {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaDolar();
		    			}
		    		} else {
		    			if(aplicarLogistica) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoReal();
		    			} else {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaReal();
		    			}
		    		}
			    }
			    
			    columns.add(valorMercadoria.toString());
			}
			graficoItemValorEntity.addColumns(columns);
		}
		
		graficoItemValorEntity.setCategories(categories);
		return ok(Json.toJson(graficoItemValorEntity));
    }
	
	
	public static Result graficoDiferencaPreco(Integer flagES, Long dataFinalunix, String tipoMoeda, Boolean aplicarLogistica) {

//		calendar31Agosto.set(2016, 7, 31);
		
		Long dataInicialUnix = 0L;
		List<Cotacao> cotacoesNacionais = Cotacao.findAllNacional(1);
		List<Cotacao> cotacoesInternacionais = Cotacao.findAllNacional(0);
		
		Calendar calendarInicial = Calendar.getInstance();
		calendarInicial.setTimeInMillis(dataFinalunix);
		calendarInicial.set(Calendar.HOUR_OF_DAY, 0);  
		calendarInicial.set(Calendar.MINUTE, 0);  
		calendarInicial.set(Calendar.SECOND, 0);  
		calendarInicial.set(Calendar.MILLISECOND, 0); 
		calendarInicial.add(Calendar.DAY_OF_YEAR, qteDiasAtras);
		dataInicialUnix = calendarInicial.getTimeInMillis() / 1000;
		
		Calendar calendarFinal = Calendar.getInstance();
		calendarFinal.setTimeInMillis(dataFinalunix);
		
		Date firstDate = calendarInicial.getTime();
     	Date secondDate = calendarFinal.getTime();

        long diff = secondDate.getTime() - firstDate.getTime();
		Long qteDiasMes =  diff / (24 * 60 * 60 * 1000);

		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();
    	
    	Calendar calendarCategories = Calendar.getInstance();
    	calendarCategories.setTimeInMillis(dataInicialUnix * 1000);

    	for(int i = 1; i <= qteDiasMes; i++) {
    		calendarCategories.add(Calendar.DAY_OF_YEAR, 1);
    		String diaMes = simpleDateFormat.format(calendarCategories.getTime());
			categories.add(diaMes);
		}
		graficoItemValorEntity.setCategories(categories);

		List<String> mediaEspiritoSanto = new ArrayList<String>(categories.size());
		if(flagES == 1) {
			mediaEspiritoSanto.add(0, "Media BR-ES");
		} else {
			mediaEspiritoSanto.add(0, "BR-RO");
		}
		for(int i = 1; i <= categories.size(); i++) {
			mediaEspiritoSanto.add(i, "0.0");
		}
		
//		boolean antesAgosto = false;
//		if(calendarFinal.getTime().after(calendar30Agosto.getTime()) && flagES == 0) {
//			antesAgosto = true;
//		}
		
		for(Cotacao cotacao : cotacoesNacionais) {
			
			calendarCategories.add(Calendar.DAY_OF_MONTH, -qteDiasMes.intValue());
			List<String> columns = new ArrayList<String>();
			columns.add(cotacao.getNome());
			
//			if(antesAgosto && cotacao.getId() == 7) {
//				continue;
//			}
			
//			if(calendarFinal.getTime().after(calendar30Agosto.getTime()) && flagES == 0) {
//				antesAgosto = true;
//				continue;
//			}
			
			for(int i = 1; i <= qteDiasMes; i++) {

				calendarCategories.add(Calendar.DAY_OF_YEAR, 1);
				calendarCategories.set(Calendar.HOUR_OF_DAY, 0);  
				calendarCategories.set(Calendar.MINUTE, 0);  
				calendarCategories.set(Calendar.SECOND, 0);  
				calendarCategories.set(Calendar.MILLISECOND, 0);  
		        Long unixtimeWithoutLocale = calendarCategories.getTimeInMillis() / 1000;
				
//		        if(calendarCategories.getTime().after(calendar31Agosto.getTime()) && flagES == 0) {
//					continue;
//				}
		        
		        BigDecimal valorMercadoria = new BigDecimal(0.0);
		        
			    RelValorMercadoriaCotacao valorMercadoriaCotacao = RelValorMercadoriaCotacao.findByDataCotacao(unixtimeWithoutLocale, cotacao.getId()); 
			    if(valorMercadoriaCotacao != null) {

			    	Cambio cambio = Cambio.findByData(valorMercadoriaCotacao.getData());
		    		if(tipoMoeda.equals("dolar")) {
		    			if(aplicarLogistica) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoDolar();
		    			} else {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaDolar();
		    			}
		    		} else {
		    			if(aplicarLogistica) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoReal();
		    			} else {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaReal();
		    			}
		    		}
			    }

			    String valueOld = mediaEspiritoSanto.get(i);
			    if(valueOld != null && !valueOld.isEmpty()) {

			    	BigDecimal novoValor = new BigDecimal(0.0);
			    	
			    	if(cotacao.getFlagEspiritoSanto() == 1 && flagES == 1) {
			    		novoValor = MonetaryUtil.add(new BigDecimal(valueOld), MonetaryUtil.divide(valorMercadoria, new BigDecimal(2)));
			    		mediaEspiritoSanto.set(i, String.valueOf(novoValor));

			    	} else if(cotacao.getFlagEspiritoSanto() == 0 && flagES == 0) {
			    		novoValor = valorMercadoria;
			    		mediaEspiritoSanto.set(i, String.valueOf(novoValor));
			    	}
			    }
			}
		}
		
		for(Cotacao cotacao : cotacoesInternacionais) {

			calendarCategories.add(Calendar.DAY_OF_MONTH, -qteDiasMes.intValue());
			List<String> columns = new ArrayList<String>();
			columns.add(cotacao.getNome());
			
			for(int i = 1; i <= qteDiasMes; i++) {

				calendarCategories.add(Calendar.DAY_OF_YEAR, 1);
				calendarCategories.set(Calendar.HOUR_OF_DAY, 0);  
				calendarCategories.set(Calendar.MINUTE, 0);  
				calendarCategories.set(Calendar.SECOND, 0);  
				calendarCategories.set(Calendar.MILLISECOND, 0);  
		        Long unixtimeWithoutLocale = calendarCategories.getTimeInMillis() / 1000;
	
//		        if(calendarCategories.getTime().after(calendar31Agosto.getTime()) && flagES == 0) {
//					continue;
//				}
		        
		        BigDecimal valorMercadoria = new BigDecimal(0.0);
		        
			    RelValorMercadoriaCotacao valorMercadoriaCotacao = RelValorMercadoriaCotacao.findByDataCotacao(unixtimeWithoutLocale, cotacao.getId()); 
			    if(valorMercadoriaCotacao != null) {
			    	Cambio cambio = Cambio.findByData(valorMercadoriaCotacao.getData());
		    		if(tipoMoeda.equals("dolar")) {
		    			if(aplicarLogistica) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoDolar();
		    			} else {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaDolar();
		    			}
		    		} else {
		    			if(aplicarLogistica) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoReal();
		    			} else {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaReal();
		    			}
		    		}
			    }
			    
			    BigDecimal valorNacional = new BigDecimal(mediaEspiritoSanto.get(i));
			    BigDecimal novoValor = MonetaryUtil.subtract(valorNacional, valorMercadoria); // valorMercadoria, valorNacional
			    
		    	columns.add(novoValor.toString());
				graficoItemValorEntity.addColumns(columns);
			}
		}

		graficoItemValorEntity.setCategories(categories);
		return ok(Json.toJson(graficoItemValorEntity));
    }
	
	
	public static Result graficoEvolucaoPrecoNacional(Long dataFinalunix, Long idCotacaoInternacional, String tipoMoeda, Boolean isAplicarLogistica) {
    	
//		calendar31Agosto.set(2016, 7, 31);
		Long dataInicialUnix = 0L;
		List<Cotacao> cotacoes = Cotacao.findAllNacional(1);
		Cotacao cotacaoInternacional = Cotacao.findById(idCotacaoInternacional);
		cotacoes.add(cotacaoInternacional);
		
		Calendar calendarInicial = Calendar.getInstance();
		calendarInicial.setTimeInMillis(dataFinalunix);
		calendarInicial.set(Calendar.HOUR_OF_DAY, 0);  
		calendarInicial.set(Calendar.MINUTE, 0);  
		calendarInicial.set(Calendar.SECOND, 0);  
		calendarInicial.set(Calendar.MILLISECOND, 0); 
		calendarInicial.add(Calendar.DAY_OF_YEAR, qteDiasAtras);
		dataInicialUnix = calendarInicial.getTimeInMillis() / 1000;
		
		Calendar calendarFinal = Calendar.getInstance();
		calendarFinal.setTimeInMillis(dataFinalunix);
		
		Date firstDate = calendarInicial.getTime();
     	Date secondDate = calendarFinal.getTime();

        long diff = secondDate.getTime() - firstDate.getTime();
		Long qteDiasMes =  diff / (24 * 60 * 60 * 1000);

		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();
    	
    	Calendar calendarCategories = Calendar.getInstance();
    	calendarCategories.setTimeInMillis(dataInicialUnix * 1000);

    	for(int i = 1; i <= qteDiasMes; i++) {
    		calendarCategories.add(Calendar.DAY_OF_YEAR, 1);
    		String diaMes = simpleDateFormat.format(calendarCategories.getTime());
			categories.add(diaMes);
		}
		graficoItemValorEntity.setCategories(categories);
		
		for(Cotacao cotacao : cotacoes) {

			calendarCategories.add(Calendar.DAY_OF_MONTH, -qteDiasMes.intValue());
			List<String> columns = new ArrayList<String>();
			columns.add(cotacao.getNome() + " " + cotacao.getDescricao().toLowerCase());
			
			for(int i = 1; i <= qteDiasMes; i++) {

				calendarCategories.add(Calendar.DAY_OF_YEAR, 1);
				calendarCategories.set(Calendar.HOUR_OF_DAY, 0);  
				calendarCategories.set(Calendar.MINUTE, 0);  
				calendarCategories.set(Calendar.SECOND, 0);  
				calendarCategories.set(Calendar.MILLISECOND, 0);  
		        Long unixtimeWithoutLocale = calendarCategories.getTimeInMillis() / 1000;
	
//				if(calendarCategories.getTime().after(calendar31Agosto.getTime()) && cotacao.getId() == 7L) {
//					continue;
//				}
		        
		        BigDecimal valorMercadoria = new BigDecimal(0.0);
		        
			    RelValorMercadoriaCotacao valorMercadoriaCotacao = RelValorMercadoriaCotacao.findByDataCotacao(unixtimeWithoutLocale, cotacao.getId()); 
			    if(valorMercadoriaCotacao != null) {
			    	
			    	Cambio cambio = Cambio.findByData(valorMercadoriaCotacao.getData());
		    		if(tipoMoeda.equals("dolar")) {
		    			if(isAplicarLogistica) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoDolar();	
		    			} else {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaDolar();
		    			}
		    			
		    		} else {
		    			if(isAplicarLogistica) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoReal();
		    			} else {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaReal();
		    			}
		    		}
			    }
			    
			    columns.add(valorMercadoria.toString());
			}
			
			graficoItemValorEntity.addColumns(columns);
		}

		graficoItemValorEntity.setCategories(categories);
		return ok(Json.toJson(graficoItemValorEntity));
    }
	
	public static Result graficoDiferencaPrecoNacional(Integer flagES, Long dataFinalunix, Long idCotacaoInternacional, String tipoMoeda, Boolean isAplicarLogistica) {

//		calendar31Agosto.set(2016, 7, 31);
		
		Long dataInicialUnix = 0L;
		List<Cotacao> cotacoesNacionais = Cotacao.findAllNacional(1);//new ArrayList<Cotacao>();
		Cotacao cotacaoInternacional = Cotacao.findById(idCotacaoInternacional);//new ArrayList<Cotacao>();
		
		Calendar calendarInicial = Calendar.getInstance();
		calendarInicial.setTimeInMillis(dataFinalunix);
		calendarInicial.set(Calendar.HOUR_OF_DAY, 0);  
		calendarInicial.set(Calendar.MINUTE, 0);  
		calendarInicial.set(Calendar.SECOND, 0);  
		calendarInicial.set(Calendar.MILLISECOND, 0); 
		calendarInicial.add(Calendar.DAY_OF_YEAR, qteDiasAtras);
		
		dataInicialUnix = calendarInicial.getTimeInMillis() / 1000;
		
		Calendar calendarFinal = Calendar.getInstance();
		calendarFinal.setTimeInMillis(dataFinalunix);
		
		Date firstDate = calendarInicial.getTime();
     	Date secondDate = calendarFinal.getTime();

        long diff = secondDate.getTime() - firstDate.getTime();
		Long qteDiasMes =  diff / (24 * 60 * 60 * 1000);

		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();
    	
    	Calendar calendarCategories = Calendar.getInstance();
    	calendarCategories.setTimeInMillis(dataInicialUnix * 1000);

    	for(int i = 1; i <= qteDiasMes; i++) {
    		calendarCategories.add(Calendar.DAY_OF_YEAR, 1);
    		String diaMes = simpleDateFormat.format(calendarCategories.getTime());
			categories.add(diaMes);
		}
		graficoItemValorEntity.setCategories(categories);

		List<String> mediaEspiritoSanto = new ArrayList<String>(categories.size());
		if(flagES == 1) {
			mediaEspiritoSanto.add(0, "Espírito Santo");
		} else {
			mediaEspiritoSanto.add(0, "Rondônia");
		}
		for(int i = 1; i <= categories.size(); i++) {
			mediaEspiritoSanto.add(i, "0.0");
		}
		
		for(Cotacao cotacao : cotacoesNacionais) {
			
			calendarCategories.add(Calendar.DAY_OF_MONTH, -qteDiasMes.intValue());
			List<String> columns = new ArrayList<String>();
			columns.add(cotacao.getNome());
			
			for(int i = 1; i <= qteDiasMes; i++) {

				calendarCategories.add(Calendar.DAY_OF_YEAR, 1);
				calendarCategories.set(Calendar.HOUR_OF_DAY, 0);  
				calendarCategories.set(Calendar.MINUTE, 0);  
				calendarCategories.set(Calendar.SECOND, 0);  
				calendarCategories.set(Calendar.MILLISECOND, 0);  
		        Long unixtimeWithoutLocale = calendarCategories.getTimeInMillis() / 1000;
		        
//		        if(calendarCategories.getTime().after(calendar31Agosto.getTime()) && flagES == 0) {
//					continue;
//				}
		        
		        BigDecimal valorMercadoria = new BigDecimal(0.0);
		        
			    RelValorMercadoriaCotacao valorMercadoriaCotacao = RelValorMercadoriaCotacao.findByDataCotacao(unixtimeWithoutLocale, cotacao.getId()); 
			    if(valorMercadoriaCotacao != null) {
		    		if(tipoMoeda.equals("dolar")) {
		    			if(isAplicarLogistica) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoDolar();
		    			} else {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaDolar();
		    			}
		    			
		    		} else {
		    			if(isAplicarLogistica) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoReal();
		    			} else {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaReal();
		    			}
		    		}
			    }

			    String valueOld = mediaEspiritoSanto.get(i);
			    if(valueOld != null && !valueOld.isEmpty()) {

			    	BigDecimal novoValor = new BigDecimal(0.0);
			    	
			    	if(cotacao.getFlagEspiritoSanto() == 1 && flagES == 1) {
			    		novoValor = MonetaryUtil.add(new BigDecimal(valueOld), MonetaryUtil.divide(valorMercadoria, new BigDecimal(2)));
			    		mediaEspiritoSanto.set(i, String.valueOf(novoValor));

			    	} else if(cotacao.getFlagEspiritoSanto() == 0 && flagES == 0) {
			    		novoValor = valorMercadoria;
			    		mediaEspiritoSanto.set(i, String.valueOf(novoValor));
			    	}
			    }
			}
		}

		calendarCategories.add(Calendar.DAY_OF_MONTH, -qteDiasMes.intValue());
		List<String> columns = new ArrayList<String>();
		columns.add(cotacaoInternacional.getNome());
		
		for(int i = 1; i <= qteDiasMes; i++) {

			calendarCategories.add(Calendar.DAY_OF_YEAR, 1);
			calendarCategories.set(Calendar.HOUR_OF_DAY, 0);  
			calendarCategories.set(Calendar.MINUTE, 0);  
			calendarCategories.set(Calendar.SECOND, 0);  
			calendarCategories.set(Calendar.MILLISECOND, 0);  
	        Long unixtimeWithoutLocale = calendarCategories.getTimeInMillis() / 1000;

//	        if(calendarCategories.getTime().after(calendar31Agosto.getTime()) && flagES == 0) {
//				continue;
//			}
	        
	        BigDecimal valorMercadoria = new BigDecimal(0.0);
	        
		    RelValorMercadoriaCotacao valorMercadoriaCotacao = RelValorMercadoriaCotacao.findByDataCotacao(unixtimeWithoutLocale, cotacaoInternacional.getId()); 
		    if(valorMercadoriaCotacao != null) {
		    
	    		if(tipoMoeda.equals("dolar")) {
	    			if(isAplicarLogistica) {
	    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoDolar();
	    			} else {
	    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaDolar();
	    			}
	    		} else {
	    			if(isAplicarLogistica) {
	    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoReal();	    				
	    			} else {
	    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaReal();
	    			}
	    		}
		    }
		    BigDecimal valorNacional = new BigDecimal(mediaEspiritoSanto.get(i));
		    BigDecimal novoValor = MonetaryUtil.subtract(valorMercadoria, valorNacional);
		    
	    	columns.add(novoValor.toString());
			graficoItemValorEntity.addColumns(columns);
		}

		graficoItemValorEntity.setCategories(categories);
		return ok(Json.toJson(graficoItemValorEntity));
    }
	
	public static Result montarGraficoCotacao(Long idCotacao, Long dataFinalunix, String tipoMoeda, Boolean isAplicarLogistica) {
		
//		calendar31Agosto.set(2016, 7, 31);
		
		Long dataInicialUnix = 0L;
		List<Cotacao> cotacoes = Cotacao.findAllNacional(1);
		cotacoes.add(Cotacao.findById(idCotacao));
		
		Calendar calendarInicial = Calendar.getInstance();
		calendarInicial.setTimeInMillis(dataFinalunix);
		calendarInicial.set(Calendar.HOUR_OF_DAY, 0);  
		calendarInicial.set(Calendar.MINUTE, 0);  
		calendarInicial.set(Calendar.SECOND, 0);  
		calendarInicial.set(Calendar.MILLISECOND, 0);
		calendarInicial.add(Calendar.DAY_OF_YEAR, qteDiasAtras);
		dataInicialUnix = calendarInicial.getTimeInMillis() / 1000;
		
		Calendar calendarFinal = Calendar.getInstance();
		calendarFinal.setTimeInMillis(dataFinalunix);
		
		Date firstDate = calendarInicial.getTime();
     	Date secondDate = calendarFinal.getTime();

        long diff = secondDate.getTime() - firstDate.getTime();
		Long qteDiasMes =  diff / (24 * 60 * 60 * 1000);

		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();
    	
    	Calendar calendarCategories = Calendar.getInstance();
    	calendarCategories.setTimeInMillis(dataInicialUnix * 1000);
    	
    	for(int i = 1; i <= qteDiasMes; i++) {
    		calendarCategories.add(Calendar.DAY_OF_YEAR, 1);
    		String diaMes = simpleDateFormat.format(calendarCategories.getTime());
			categories.add(diaMes);
		}
		graficoItemValorEntity.setCategories(categories);

		List<String> mediaEspiritoSanto = new ArrayList<String>(categories.size());
		mediaEspiritoSanto.add(0, "Espírito Santo (média)");
		for(int i = 1; i <= qteDiasMes; i++) {
			mediaEspiritoSanto.add(i, "0.0");
		}
		
		for(Cotacao cotacao : cotacoes) {

			calendarCategories.add(Calendar.DAY_OF_MONTH, -qteDiasMes.intValue());
			List<String> columns = new ArrayList<String>();
			if(cotacao.getFlagEspiritoSanto() != 1) {
				columns.add(cotacao.getNome());
			}
			
			for(int i = 1; i <= qteDiasMes; i++) {

				calendarCategories.add(Calendar.DAY_OF_YEAR, 1);
				calendarCategories.set(Calendar.HOUR_OF_DAY, 0);  
				calendarCategories.set(Calendar.MINUTE, 0);  
				calendarCategories.set(Calendar.SECOND, 0);  
				calendarCategories.set(Calendar.MILLISECOND, 0);  
		        Long unixtimeWithoutLocale = calendarCategories.getTimeInMillis() / 1000;
	
//				if(calendarCategories.getTime().after(calendar31Agosto.getTime()) && cotacao.getId() == 7L) {
//					continue;
//				}
		        
		        BigDecimal valorMercadoria = new BigDecimal(0.0);
		        
			    RelValorMercadoriaCotacao valorMercadoriaCotacao = RelValorMercadoriaCotacao.findByDataCotacao(unixtimeWithoutLocale, cotacao.getId()); 
			    if(valorMercadoriaCotacao != null && valorMercadoriaCotacao.getValorCustoImportacao() != null) {
			    	
			    	Cambio cambio = Cambio.findByData(valorMercadoriaCotacao.getData());
		    		if(tipoMoeda.equals("dolar")) {
		    			if(isAplicarLogistica) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoDolar();
		    			} else {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaDolar();
		    			}
		    		} else {
		    			if(isAplicarLogistica) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoReal();
		    			} else {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaReal();
		    			}
		    		}
			    }
			    
				if(cotacao.getFlagEspiritoSanto() == 1) {
					BigDecimal novoValor = new BigDecimal(0.0);
		    		String valueOld = mediaEspiritoSanto.get(i);
		    		novoValor = MonetaryUtil.add(new BigDecimal(valueOld), MonetaryUtil.divide(valorMercadoria, new BigDecimal(2)));
		    		mediaEspiritoSanto.set(i, String.valueOf(novoValor));
				} else {
					columns.add(valorMercadoria.toString());
				}
			}
			if(cotacao.getFlagEspiritoSanto() != 1) {
				graficoItemValorEntity.addColumns(columns);
			}
		}
		
		graficoItemValorEntity.addColumns(mediaEspiritoSanto);
		graficoItemValorEntity.setCategories(categories);
		return ok(Json.toJson(graficoItemValorEntity));
		
	}
	
	public static Result montarGraficoDiferencialCotacao(Long idCotacao, Long dataFinalunix, String tipoMoeda, Boolean isAplicarLogistica) {
		
//		calendar31Agosto.set(2016, 7, 31);
		
		Long dataInicialUnix = 0L;
		List<Cotacao> cotacoesNacionais = Cotacao.findAllNacional(1);//new ArrayList<Cotacao>();
		List<Cotacao> cotacoesInternacionais = new ArrayList<Cotacao>();
		Cotacao cotacaoInternacional = Cotacao.findById(idCotacao);
		cotacoesInternacionais.add(cotacaoInternacional);
		
		Calendar calendarInicial = Calendar.getInstance();
		calendarInicial.setTimeInMillis(dataFinalunix);
		calendarInicial.set(Calendar.HOUR_OF_DAY, 0);  
		calendarInicial.set(Calendar.MINUTE, 0);  
		calendarInicial.set(Calendar.SECOND, 0);  
		calendarInicial.set(Calendar.MILLISECOND, 0); 
		calendarInicial.add(Calendar.DAY_OF_YEAR, qteDiasAtras);
		dataInicialUnix = calendarInicial.getTimeInMillis() / 1000;
		
		Calendar calendarFinal = Calendar.getInstance();
		calendarFinal.setTimeInMillis(dataFinalunix);
		
		Date firstDate = calendarInicial.getTime();
     	Date secondDate = calendarFinal.getTime();

        long diff = secondDate.getTime() - firstDate.getTime();
		Long qteDiasMes =  diff / (24 * 60 * 60 * 1000);

		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();
    	
    	Calendar calendarCategories = Calendar.getInstance();
    	calendarCategories.setTimeInMillis(dataInicialUnix * 1000);

//        long diff31Agosto = calendar31Agosto.getTime().getTime() - firstDate.getTime();
//		Long qteDiasMes31Agosto =  diff31Agosto / (24 * 60 * 60 * 1000);
		
//		boolean depois31Agosto = false;
//		if(secondDate.after(calendar31Agosto.getTime())) {
//			depois31Agosto = true;
//		}
    	
    	for(int i = 1; i <= qteDiasMes; i++) {
    		calendarCategories.add(Calendar.DAY_OF_YEAR, 1);
    		String diaMes = simpleDateFormat.format(calendarCategories.getTime());
			categories.add(diaMes);
		}
		graficoItemValorEntity.setCategories(categories);

		List<String> mediaEspiritoSanto = new ArrayList<String>(categories.size()+1);
		mediaEspiritoSanto.add(0, "Espírito Santo (média)"); // cotacaoInternacional.getNome() + " Espírito Santo"
		List<String> mediaRondonia = new ArrayList<String>(categories.size()+1);
		mediaRondonia.add(0, "Rondônia"); //cotacaoInternacional.getNome() + "Rondônia"
		for(int i = 1; i <= categories.size(); i++) {
			mediaEspiritoSanto.add(i, "0.0");
			mediaRondonia.add(i, "0.0");
//			if(qteDiasMes31Agosto > i - 1) {
//				mediaRondonia.add(i, "0.0");
//			}
		}
		
		for(Cotacao cotacao : cotacoesNacionais) {
			
			calendarCategories.add(Calendar.DAY_OF_MONTH, -qteDiasMes.intValue());
			List<String> columns = new ArrayList<String>();
			columns.add(cotacao.getNome());
			
			for(int i = 1; i <= qteDiasMes; i++) {

				calendarCategories.add(Calendar.DAY_OF_YEAR, 1);
				calendarCategories.set(Calendar.HOUR_OF_DAY, 0);  
				calendarCategories.set(Calendar.MINUTE, 0);  
				calendarCategories.set(Calendar.SECOND, 0);  
				calendarCategories.set(Calendar.MILLISECOND, 0);  
		        Long unixtimeWithoutLocale = calendarCategories.getTimeInMillis() / 1000;
	
//		        if(qteDiasMes31Agosto < i && cotacao.getId() == 7L) {
//		        	continue;
//		        }
		        
		        BigDecimal valorMercadoria = new BigDecimal(0.0);
		        
			    RelValorMercadoriaCotacao valorMercadoriaCotacao = RelValorMercadoriaCotacao.findByDataCotacao(unixtimeWithoutLocale, cotacao.getId()); 
			    if(valorMercadoriaCotacao != null) {

			    	Cambio cambio = Cambio.findByData(valorMercadoriaCotacao.getData());
		    		if(tipoMoeda.equals("dolar")) {
		    			if(isAplicarLogistica) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoDolar();
		    			} else {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaDolar();
		    			}
		    		} else {
		    			if(isAplicarLogistica) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoReal();
		    			} else {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaReal();
		    			}
		    		}
			    }
			  
		    	BigDecimal novoValor = new BigDecimal(0.0);
		    	
		    	if(cotacao.getFlagEspiritoSanto() == 1) {
		    		String valueOld = mediaEspiritoSanto.get(i);
		    		novoValor = MonetaryUtil.add(new BigDecimal(valueOld), MonetaryUtil.divide(valorMercadoria, new BigDecimal(2)));
		    		mediaEspiritoSanto.set(i, String.valueOf(novoValor));

		    	} else if(cotacao.getFlagEspiritoSanto() == 0) {
		    		novoValor = valorMercadoria;
		    		mediaRondonia.set(i, String.valueOf(novoValor));
		    	}
			}
		}
		
		for(Cotacao cotacao : cotacoesInternacionais) {

			calendarCategories.add(Calendar.DAY_OF_MONTH, -qteDiasMes.intValue());
			List<String> columns = new ArrayList<String>();
			columns.add(cotacao.getNome());
			
			for(int i = 1; i <= qteDiasMes; i++) {

				calendarCategories.add(Calendar.DAY_OF_YEAR, 1);
				calendarCategories.set(Calendar.HOUR_OF_DAY, 0);  
				calendarCategories.set(Calendar.MINUTE, 0);  
				calendarCategories.set(Calendar.SECOND, 0);  
				calendarCategories.set(Calendar.MILLISECOND, 0);  
		        Long unixtimeWithoutLocale = calendarCategories.getTimeInMillis() / 1000;
	
		        BigDecimal valorMercadoria = new BigDecimal(0.0);
		        
			    RelValorMercadoriaCotacao valorMercadoriaCotacao = RelValorMercadoriaCotacao.findByDataCotacao(unixtimeWithoutLocale, cotacao.getId()); 
			    if(valorMercadoriaCotacao != null) {
			    	
			    	Cambio cambio = Cambio.findByData(valorMercadoriaCotacao.getData());
		    		valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoReal();
		    		if(tipoMoeda.equals("dolar")) {
		    			if(isAplicarLogistica) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoDolar();
		    			} else {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaDolar();
		    			}
		    		} else {
		    			if(isAplicarLogistica) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoReal();
		    			} else {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaReal();
		    			}
		    		}
			    }
			    
			    String valorOldES = mediaEspiritoSanto.get(i);
			    
			    BigDecimal novoValorES = MonetaryUtil.subtract(valorMercadoria, new BigDecimal(valorOldES));
			    novoValorES = MonetaryUtil.multiply(novoValorES, new BigDecimal(-1));
			    mediaEspiritoSanto.set(i, String.valueOf(novoValorES));
			    
//			    if(qteDiasMes31Agosto < i) {
//		        	continue;
//		        }
			    String valorOldRO = mediaRondonia.get(i);
			    BigDecimal novoValorRO = MonetaryUtil.subtract(valorMercadoria, new BigDecimal(valorOldRO));
			    novoValorRO = MonetaryUtil.multiply(novoValorRO, new BigDecimal(-1));
			    mediaRondonia.set(i, String.valueOf(novoValorRO));
			}
		}

		graficoItemValorEntity.addColumns(mediaEspiritoSanto);
		graficoItemValorEntity.addColumns(mediaRondonia);
		graficoItemValorEntity.setCategories(categories);
		return ok(Json.toJson(graficoItemValorEntity));
	}
	
	public static Result simularRelatorio(Long unixtime, Boolean aplicarLogistica) {
		
//		calendar31Agosto.set(2016, 7, 31);
    	Date date = Date.from( Instant.ofEpochSecond( unixtime / 1000 ) );

        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        Long unixtimeWithoutLocale = cal.getTimeInMillis() / 1000;
        
        Cambio cambioResponse = Cambio.findByData(unixtimeWithoutLocale);
//        BigDecimal relacaoDolarEuro = new BigDecimal(0.0);
        if(cambioResponse == null) {
        	cambioResponse = Cambio.findUltimoCambio();
        }
//        if(cambioResponse != null) {
//        	relacaoDolarEuro = MonetaryUtil.divide(cambioResponse.getValorEuro(), cambioResponse.getValorDolar());
//        }
        
        List<CotacaoRelatorioResponse> relatorioResponses = new ArrayList<CotacaoRelatorioResponse>();
		List<Cotacao> cotacaoList = Cotacao.findAllOrderByNacional();
		
		BigDecimal valorDiferencialES = new BigDecimal(0.0);
		BigDecimal valorDiferencialRO = new BigDecimal(0.0);
		
		BigDecimal valorDiferencialESDolar = new BigDecimal(0.0);
		BigDecimal valorDiferencialRODolar = new BigDecimal(0.0);
		
		for(Cotacao cotacao : cotacaoList) {

			RelValorMercadoriaCotacao relValorMercadoriaCotacao = RelValorMercadoriaCotacao.findByDataCotacao(unixtimeWithoutLocale, cotacao.getId());
			List<CategoriaRelatorioResponse> categoriaResponseList = new ArrayList<CategoriaRelatorioResponse>();
			List<RelCotacaoCategoria> cotacaoCategorias = RelCotacaoCategoria.findByCotacao(cotacao.getId());
			
			if(relValorMercadoriaCotacao != null) {
				
				CotacaoRelatorioResponse cotacaoRelatorio = new CotacaoRelatorioResponse();
				cotacaoRelatorio.setId(cotacao.getId());
				cotacaoRelatorio.setNome(cotacao.getNome());
				cotacaoRelatorio.setDescricao(cotacao.getDescricao());
				cotacaoRelatorio.setNacional(cotacao.getNacional());
				cotacaoRelatorio.setFlag(cotacao.getFlag());
				cotacaoRelatorio.setFlagEspiritoSanto(cotacao.getFlagEspiritoSanto());
				cotacaoRelatorio.setFonte(cotacao.getFonte());
				cotacaoRelatorio.setValorMercadoriaReal(String.valueOf(relValorMercadoriaCotacao.getValorMercadoriaReal()));
				cotacaoRelatorio.setValorMercadoriaDolar(String.valueOf(relValorMercadoriaCotacao.getValorMercadoriaDolar()));
				
				if(aplicarLogistica) {
					cotacaoRelatorio.setValorCustoImportacaoReal(String.valueOf(relValorMercadoriaCotacao.getValorCustoImportacaoReal()));
					cotacaoRelatorio.setValorCustoImportacaoDolar(String.valueOf(relValorMercadoriaCotacao.getValorCustoImportacaoDolar()));	
				} else {
					cotacaoRelatorio.setValorCustoImportacaoReal(String.valueOf(relValorMercadoriaCotacao.getValorMercadoriaReal()));
					cotacaoRelatorio.setValorCustoImportacaoDolar(String.valueOf(relValorMercadoriaCotacao.getValorMercadoriaDolar()));
				}
				
				if(cotacao.getNacional() == 1) {
					if(cotacao.getFlagEspiritoSanto() == 1) {
						BigDecimal temp = new BigDecimal(0.0);
						BigDecimal tempDolar = new BigDecimal(0.0);
						if(aplicarLogistica) {
							temp = MonetaryUtil.divide(relValorMercadoriaCotacao.getValorCustoImportacaoReal(), new BigDecimal(2));
							tempDolar = MonetaryUtil.divide(relValorMercadoriaCotacao.getValorCustoImportacaoDolar(), new BigDecimal(2));
						} else {
							temp = MonetaryUtil.divide(relValorMercadoriaCotacao.getValorMercadoriaReal(), new BigDecimal(2));
							tempDolar = MonetaryUtil.divide(relValorMercadoriaCotacao.getValorMercadoriaDolar(), new BigDecimal(2));
						}
						valorDiferencialES = MonetaryUtil.add(valorDiferencialES, temp);
						valorDiferencialESDolar = MonetaryUtil.add(valorDiferencialESDolar, tempDolar);
					} else {
						if(aplicarLogistica) {
							valorDiferencialRO = relValorMercadoriaCotacao.getValorCustoImportacaoReal();
							valorDiferencialRODolar = relValorMercadoriaCotacao.getValorCustoImportacaoDolar();
						} else {
							valorDiferencialRO = relValorMercadoriaCotacao.getValorMercadoriaReal();
							valorDiferencialRODolar = relValorMercadoriaCotacao.getValorMercadoriaDolar();							
						}
					}
				}
				
				if(cotacao.getNacional() == 0) {
					
					BigDecimal valorCalculo = new BigDecimal(0.0);
					BigDecimal valorCalculoDolar = new BigDecimal(0.0);
					if(aplicarLogistica) {
						valorCalculo = relValorMercadoriaCotacao.getValorCustoImportacaoReal();
						valorCalculoDolar = relValorMercadoriaCotacao.getValorCustoImportacaoDolar();
					} else {
						valorCalculo = relValorMercadoriaCotacao.getValorMercadoriaReal();
						valorCalculoDolar = relValorMercadoriaCotacao.getValorMercadoriaDolar();
					}
					
					cotacaoRelatorio.setDiferencialES(MonetaryUtil.subtract(valorDiferencialES, valorCalculo).toString());
					cotacaoRelatorio.setDiferencialRO(MonetaryUtil.subtract(valorDiferencialRO, valorCalculo).toString());
//					cotacaoRelatorio.setDiferencialES(MonetaryUtil.multiply(new BigDecimal(-1.0), new BigDecimal(cotacaoRelatorio.getDiferencialES())).toString());
//					cotacaoRelatorio.setDiferencialRO(MonetaryUtil.multiply(new BigDecimal(-1.0), new BigDecimal(cotacaoRelatorio.getDiferencialRO())).toString());
	
					cotacaoRelatorio.setDiferencialESDolar(MonetaryUtil.subtract(valorDiferencialESDolar, valorCalculoDolar).toString());
					cotacaoRelatorio.setDiferencialRODolar(MonetaryUtil.subtract(valorDiferencialRODolar, valorCalculoDolar).toString());
					cotacaoRelatorio.setDiferencialESDolar(MonetaryUtil.multiply(new BigDecimal(-1.0), new BigDecimal(cotacaoRelatorio.getDiferencialESDolar())).toString());
					cotacaoRelatorio.setDiferencialRODolar(MonetaryUtil.multiply(new BigDecimal(-1.0), new BigDecimal(cotacaoRelatorio.getDiferencialRODolar())).toString());
					
					BigDecimal valorDivisaoSaca60kg = new BigDecimal(0.0);
					if(aplicarLogistica) {
						valorDivisaoSaca60kg = relValorMercadoriaCotacao.getValorCustoImportacaoReal();
					} else {
						valorDivisaoSaca60kg = relValorMercadoriaCotacao.getValorMercadoriaReal();
					}
					if(valorDivisaoSaca60kg.toString().equals("0.00") || valorDivisaoSaca60kg.equals(new BigDecimal("0.00"))) {
						valorDivisaoSaca60kg = new BigDecimal(1.0);
					}
					
					BigDecimal difES = MonetaryUtil.subtract(valorDivisaoSaca60kg, valorDiferencialES);
					BigDecimal difRO = MonetaryUtil.subtract(valorDivisaoSaca60kg, valorDiferencialRO);
					cotacaoRelatorio.setDiferencialPercentualES(MonetaryUtil.divide(difES, valorDivisaoSaca60kg).toString());
					cotacaoRelatorio.setDiferencialPercentualRO(MonetaryUtil.divide(difRO, valorDivisaoSaca60kg).toString());
	
					cotacaoRelatorio.setDiferencialPercentualES(MonetaryUtil.multiply(new BigDecimal(cotacaoRelatorio.getDiferencialPercentualES()), 
							new BigDecimal(100.00)).toString());
					cotacaoRelatorio.setDiferencialPercentualRO(MonetaryUtil.multiply(new BigDecimal(cotacaoRelatorio.getDiferencialPercentualRO()), 
							new BigDecimal(100.00)).toString());
				}
				
				for(RelCotacaoCategoria relCotCat : cotacaoCategorias) {
					
					BigDecimal valorTotalCategoriaReal = new BigDecimal(0.0);
					BigDecimal valorTotalCategoriaDolar = new BigDecimal(0.0);

					Categoria categoria = Categoria.findById(relCotCat.getCategoriaId());
					CategoriaRelatorioResponse categoriaRelatorio = new CategoriaRelatorioResponse();
					categoriaRelatorio.setId(categoria.getId());
					categoriaRelatorio.setNome(categoria.getNome());
					categoriaRelatorio.setDescricao(categoria.getDescricao());
					
					List<RelCategoriaItem> categoriaItens = RelCategoriaItem.findByCategoria(categoria.getId());
					List<ItemRelatorioResponse> itemResponseList = new ArrayList<ItemRelatorioResponse>();

					for(RelCategoriaItem relCatItem : categoriaItens) {
						
						Item item = Item.findById(relCatItem.getItemId());
						ItemRelatorioResponse itemRelatorio = new ItemRelatorioResponse();
						itemRelatorio.setId(item.getId());
						itemRelatorio.setNome(item.getNome());
						itemRelatorio.setIsCategoriaTotal(relCatItem.getIsCategoriaTotal());
							
						RelItemValor relItemValor = RelItemValor.findByItemUltimo(item.getId());
						
						if(relItemValor != null) {
							
							itemRelatorio.setTipoMoeda(relItemValor.getUnidade());
							
							if(relItemValor.getUnidade() == RelItemValor.Unidade.REAL.getCodigo()) {
								itemRelatorio.setValor(String.valueOf(relItemValor.getValor()));
								itemRelatorio.setValorDolar(String.valueOf(MonetaryUtil.divide(relItemValor.getValor(), cambioResponse.getValorDolar())));
							} else if(relItemValor.getUnidade() == RelItemValor.Unidade.DOLAR.getCodigo()) {
								itemRelatorio.setValor(String.valueOf(MonetaryUtil.multiply(relItemValor.getValor(), cambioResponse.getValorDolar())));
								itemRelatorio.setValorDolar(String.valueOf(relItemValor.getValor()));
							} else if(relItemValor.getUnidade() == RelItemValor.Unidade.PERCENTUAL.getCodigo()) {
								itemRelatorio.setValor(String.valueOf(relItemValor.getValor()));
								itemRelatorio.setValorDolar(String.valueOf(relItemValor.getValor()));
							}
						}

						valorTotalCategoriaReal = MonetaryUtil.add(valorTotalCategoriaReal, new BigDecimal(itemRelatorio.getValor()));
						valorTotalCategoriaDolar = MonetaryUtil.add(valorTotalCategoriaDolar, new BigDecimal(itemRelatorio.getValorDolar()));
						
						itemResponseList.add(itemRelatorio);
					}
					
					categoriaRelatorio.setValorTotalCategoriaReal(valorTotalCategoriaReal.toString());
					categoriaRelatorio.setValorTotalCategoriaDolar(valorTotalCategoriaDolar.toString());

					categoriaRelatorio.setValorSacaCategoriaReal(String.valueOf(MonetaryUtil.divide(valorTotalCategoriaReal, new BigDecimal(320))));
					categoriaRelatorio.setValorSacaCategoriaDolar(String.valueOf(MonetaryUtil.divide(valorTotalCategoriaDolar, new BigDecimal(320))));
					
					categoriaRelatorio.setItemList(itemResponseList);
					categoriaResponseList.add(categoriaRelatorio);
				}
				cotacaoRelatorio.setCategoriaList(categoriaResponseList);
				relatorioResponses.add(cotacaoRelatorio);
			}
		}
        
		return ok(Json.toJson(relatorioResponses));
	}
}