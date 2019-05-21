package controllers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import entitys.response.CambioResponse;
import entitys.response.CategoriaRelatorioResponse;
import entitys.response.CotacaoRelatorioResponse;
import entitys.response.ItemRelatorioResponse;
import entitys.response.RelValorMercadoriaCotacaoResponse;

public class LogisticaController extends AbstractController {

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
	
	public static Result findValorMercadoria(Long dataUnixtime) {
		
		List<Cotacao> cotacoes = Cotacao.findAllNacionalOrdered(0);
		List<RelValorMercadoriaCotacaoResponse> listRelValorMercadoriaCotacao = new ArrayList<RelValorMercadoriaCotacaoResponse>();
    	Date date = Date.from( Instant.ofEpochSecond( dataUnixtime / 1000 ) );

        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        Long unixtimeWithoutLocale = cal.getTimeInMillis() / 1000; 
		
		for(Cotacao cotacao : cotacoes) {
			RelValorMercadoriaCotacao valorMercadoriaCotacao = RelValorMercadoriaCotacao.findByDataCotacao(unixtimeWithoutLocale, cotacao.getId());
			RelValorMercadoriaCotacaoResponse response = new RelValorMercadoriaCotacaoResponse(valorMercadoriaCotacao, cotacao);
			listRelValorMercadoriaCotacao.add(response);
		}
		
		
		Collections.sort(listRelValorMercadoriaCotacao, new Comparator<RelValorMercadoriaCotacaoResponse>() {
	        @Override
	        public int compare(RelValorMercadoriaCotacaoResponse c1, RelValorMercadoriaCotacaoResponse c2) {
	            return  c1.getCotacaoOrdem().compareTo(c2.getCotacaoOrdem());
	        }
	    });
		
		return ok(Json.toJson(listRelValorMercadoriaCotacao));
	}
	
	public static Result simularLogistica(Long dataUnixtime) {
		
	  	Date date = Date.from( Instant.ofEpochSecond( dataUnixtime / 1000 ) );
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        Long unixtimeWithoutLocale = cal.getTimeInMillis() / 1000;
        
        Cambio cambioResponse = Cambio.findByData(unixtimeWithoutLocale);
        if(cambioResponse == null) {
        	cambioResponse = Cambio.findUltimoCambio();
        }
        
        List<CotacaoRelatorioResponse> relatorioResponses = new ArrayList<CotacaoRelatorioResponse>();
		List<Cotacao> cotacaoList = Cotacao.findAllNacionalOrdered(0);
		
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
				
//				if(aplicarLogistica) {
//					cotacaoRelatorio.setValorCustoImportacaoReal(String.valueOf(relValorMercadoriaCotacao.getValorCustoImportacaoReal()));
//					cotacaoRelatorio.setValorCustoImportacaoDolar(String.valueOf(relValorMercadoriaCotacao.getValorCustoImportacaoDolar()));	
//				} else {
//					cotacaoRelatorio.setValorCustoImportacaoReal(String.valueOf(relValorMercadoriaCotacao.getValorMercadoriaReal()));
//					cotacaoRelatorio.setValorCustoImportacaoDolar(String.valueOf(relValorMercadoriaCotacao.getValorMercadoriaDolar()));
//				}
				
//				if(cotacao.getNacional() == 1) {
//					if(cotacao.getFlagEspiritoSanto() == 1) {
//						BigDecimal temp = new BigDecimal(0.0);
//						BigDecimal tempDolar = new BigDecimal(0.0);
//						if(aplicarLogistica) {
//							temp = MonetaryUtil.divide(relValorMercadoriaCotacao.getValorCustoImportacaoReal(), new BigDecimal(2));
//							tempDolar = MonetaryUtil.divide(relValorMercadoriaCotacao.getValorCustoImportacaoDolar(), new BigDecimal(2));
//						} else {
//							temp = MonetaryUtil.divide(relValorMercadoriaCotacao.getValorMercadoriaReal(), new BigDecimal(2));
//							tempDolar = MonetaryUtil.divide(relValorMercadoriaCotacao.getValorMercadoriaDolar(), new BigDecimal(2));
//						}
//						valorDiferencialES = MonetaryUtil.add(valorDiferencialES, temp);
//						valorDiferencialESDolar = MonetaryUtil.add(valorDiferencialESDolar, tempDolar);
//					} else {
//						if(aplicarLogistica) {
//							valorDiferencialRO = relValorMercadoriaCotacao.getValorCustoImportacaoReal();
//							valorDiferencialRODolar = relValorMercadoriaCotacao.getValorCustoImportacaoDolar();
//						} else {
//							valorDiferencialRO = relValorMercadoriaCotacao.getValorMercadoriaReal();
//							valorDiferencialRODolar = relValorMercadoriaCotacao.getValorMercadoriaDolar();							
//						}
//					}
//				}
				
				if(cotacao.getNacional() == 0) {
					
					BigDecimal valorCalculo = new BigDecimal(0.0);
					BigDecimal valorCalculoDolar = new BigDecimal(0.0);
//					if(aplicarLogistica) {
//						valorCalculo = relValorMercadoriaCotacao.getValorCustoImportacaoReal();
//						valorCalculoDolar = relValorMercadoriaCotacao.getValorCustoImportacaoDolar();
//					} else {
						valorCalculo = relValorMercadoriaCotacao.getValorMercadoriaReal();
						valorCalculoDolar = relValorMercadoriaCotacao.getValorMercadoriaDolar();
//					}
					
					cotacaoRelatorio.setDiferencialES(MonetaryUtil.subtract(valorDiferencialES, valorCalculo).toString());
					cotacaoRelatorio.setDiferencialRO(MonetaryUtil.subtract(valorDiferencialRO, valorCalculo).toString());
					cotacaoRelatorio.setDiferencialES(MonetaryUtil.multiply(new BigDecimal(-1.0), new BigDecimal(cotacaoRelatorio.getDiferencialES())).toString());
					cotacaoRelatorio.setDiferencialRO(MonetaryUtil.multiply(new BigDecimal(-1.0), new BigDecimal(cotacaoRelatorio.getDiferencialRO())).toString());
	
					cotacaoRelatorio.setDiferencialESDolar(MonetaryUtil.subtract(valorDiferencialESDolar, valorCalculoDolar).toString());
					cotacaoRelatorio.setDiferencialRODolar(MonetaryUtil.subtract(valorDiferencialRODolar, valorCalculoDolar).toString());
					cotacaoRelatorio.setDiferencialESDolar(MonetaryUtil.multiply(new BigDecimal(-1.0), new BigDecimal(cotacaoRelatorio.getDiferencialESDolar())).toString());
					cotacaoRelatorio.setDiferencialRODolar(MonetaryUtil.multiply(new BigDecimal(-1.0), new BigDecimal(cotacaoRelatorio.getDiferencialRODolar())).toString());
					
					BigDecimal valorDivisaoSaca60kg = new BigDecimal(0.0);
//					if(aplicarLogistica) {
//						valorDivisaoSaca60kg = relValorMercadoriaCotacao.getValorCustoImportacaoReal();
//					} else {
						valorDivisaoSaca60kg = relValorMercadoriaCotacao.getValorMercadoriaReal();
//					}
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
	
	
	
	
//	
//	public static Result calcularCustoLogistico(Long unixtime) {
//		
//    	Date date = Date.from( Instant.ofEpochSecond( unixtime / 1000 ) );
//
//        Calendar cal = Calendar.getInstance();  
//        cal.setTime(date);  
//        cal.set(Calendar.HOUR_OF_DAY, 0);  
//        cal.set(Calendar.MINUTE, 0);  
//        cal.set(Calendar.SECOND, 0);  
//        cal.set(Calendar.MILLISECOND, 0);  
//        Long unixtimeWithoutLocale = cal.getTimeInMillis() / 1000;
//        
//        Cambio cambioResponse = Cambio.findByData(unixtimeWithoutLocale);
////        BigDecimal relacaoDolarEuro = new BigDecimal(0.0);
//        if(cambioResponse == null) {
//        	cambioResponse = Cambio.findUltimoCambio();
//        }
////        if(cambioResponse != null) {
////        	relacaoDolarEuro = MonetaryUtil.divide(cambioResponse.getValorEuro(), cambioResponse.getValorDolar());
////        }
//        
//        List<CotacaoRelatorioResponse> relatorioResponses = new ArrayList<CotacaoRelatorioResponse>();
//		List<Cotacao> cotacaoList = Cotacao.findAllOrderByNacional();
//		
//		BigDecimal valorDiferencialES = new BigDecimal(0.0);
//		BigDecimal valorDiferencialRO = new BigDecimal(0.0);
//		
//		BigDecimal valorDiferencialESDolar = new BigDecimal(0.0);
//		BigDecimal valorDiferencialRODolar = new BigDecimal(0.0);
//		
//		for(Cotacao cotacao : cotacaoList) {
//
//			RelValorMercadoriaCotacao relValorMercadoriaCotacao = RelValorMercadoriaCotacao.findByDataCotacao(unixtimeWithoutLocale, cotacao.getId());
//			List<CategoriaRelatorioResponse> categoriaResponseList = new ArrayList<CategoriaRelatorioResponse>();
//			List<RelCotacaoCategoria> cotacaoCategorias = RelCotacaoCategoria.findByCotacao(cotacao.getId());
//			
//			if(relValorMercadoriaCotacao != null) {
//				
//				CotacaoRelatorioResponse cotacaoRelatorio = new CotacaoRelatorioResponse();
//				cotacaoRelatorio.setId(cotacao.getId());
//				cotacaoRelatorio.setNome(cotacao.getNome());
//				cotacaoRelatorio.setDescricao(cotacao.getDescricao());
//				cotacaoRelatorio.setNacional(cotacao.getNacional());
//				cotacaoRelatorio.setFlag(cotacao.getFlag());
//				cotacaoRelatorio.setFlagEspiritoSanto(cotacao.getFlagEspiritoSanto());
//				cotacaoRelatorio.setFonte(cotacao.getFonte());
//				cotacaoRelatorio.setValorCustoImportacaoReal(String.valueOf(relValorMercadoriaCotacao.getValorCustoImportacaoReal()));
//				cotacaoRelatorio.setValorCustoImportacaoDolar(String.valueOf(relValorMercadoriaCotacao.getValorCustoImportacaoDolar()));
//				
//				if(cotacao.getNacional() == 1) {
//					if(cotacao.getFlagEspiritoSanto() == 1) {
//						BigDecimal temp = MonetaryUtil.divide(relValorMercadoriaCotacao.getValorCustoImportacaoReal(), new BigDecimal(2));
//						valorDiferencialES = MonetaryUtil.add(valorDiferencialES, temp);
//						BigDecimal tempDolar = MonetaryUtil.divide(relValorMercadoriaCotacao.getValorCustoImportacaoDolar(), new BigDecimal(2));
//						valorDiferencialESDolar = MonetaryUtil.add(valorDiferencialESDolar, tempDolar);
//					} else {
//						valorDiferencialRO = relValorMercadoriaCotacao.getValorCustoImportacaoReal();
//						valorDiferencialRODolar = relValorMercadoriaCotacao.getValorCustoImportacaoDolar();
//					}
//				}
//				
//				if(cotacao.getNacional() == 0) {
//					
//					cotacaoRelatorio.setDiferencialES(MonetaryUtil.subtract(valorDiferencialES, relValorMercadoriaCotacao.getValorCustoImportacaoReal()).toString());
//					cotacaoRelatorio.setDiferencialRO(MonetaryUtil.subtract(valorDiferencialRO, relValorMercadoriaCotacao.getValorCustoImportacaoReal()).toString());
//					
//					cotacaoRelatorio.setDiferencialES(MonetaryUtil.multiply(new BigDecimal(-1.0), new BigDecimal(cotacaoRelatorio.getDiferencialES())).toString());
//					cotacaoRelatorio.setDiferencialRO(MonetaryUtil.multiply(new BigDecimal(-1.0), new BigDecimal(cotacaoRelatorio.getDiferencialRO())).toString());
//	
//	//				BigDecimal valorDivisaoSaca60kg = new BigDecimal(itemRelatorio.getValorSaca60kg());
//					BigDecimal valorDivisaoSaca60kg = relValorMercadoriaCotacao.getValorCustoImportacaoReal();
//					if(valorDivisaoSaca60kg.toString().equals("0.00") || valorDivisaoSaca60kg.equals(new BigDecimal("0.00"))) {
//						valorDivisaoSaca60kg = new BigDecimal(1.0);
//					}
//					
//	//				(valorDivisaoSaca60kg - valorDiferencialES) % valorDivisaoSaca60kg;
//					BigDecimal difES = MonetaryUtil.subtract(valorDivisaoSaca60kg, valorDiferencialES);
//					BigDecimal difRO = MonetaryUtil.subtract(valorDivisaoSaca60kg, valorDiferencialRO);
//					cotacaoRelatorio.setDiferencialPercentualES(MonetaryUtil.divide(difES, valorDivisaoSaca60kg).toString());
//					cotacaoRelatorio.setDiferencialPercentualRO(MonetaryUtil.divide(difRO, valorDivisaoSaca60kg).toString());
//	
//					cotacaoRelatorio.setDiferencialPercentualES(MonetaryUtil.multiply(new BigDecimal(cotacaoRelatorio.getDiferencialPercentualES()), 
//							new BigDecimal(100.00)).toString());
//					cotacaoRelatorio.setDiferencialPercentualRO(MonetaryUtil.multiply(new BigDecimal(cotacaoRelatorio.getDiferencialPercentualRO()), 
//							new BigDecimal(100.00)).toString());
//				}
//				
//				for(RelCotacaoCategoria relCotCat : cotacaoCategorias) {
//					
//					Categoria categoria = Categoria.findById(relCotCat.getCategoriaId());
//					CategoriaRelatorioResponse categoriaRelatorio = new CategoriaRelatorioResponse();
//					categoriaRelatorio.setId(categoria.getId());
//					categoriaRelatorio.setNome(categoria.getNome());
//					categoriaRelatorio.setDescricao(categoria.getDescricao());
//					
//					List<RelCategoriaItem> categoriaItens = RelCategoriaItem.findByCategoria(categoria.getId());
//					List<ItemRelatorioResponse> itemResponseList = new ArrayList<ItemRelatorioResponse>();
//					
//					for(RelCategoriaItem relCatItem : categoriaItens) {
//						
//						Item item = Item.findById(relCatItem.getItemId());
//						ItemRelatorioResponse itemRelatorio = new ItemRelatorioResponse();
//						itemRelatorio.setId(item.getId());
//						itemRelatorio.setNome(item.getNome());
//						itemRelatorio.setIsCategoriaTotal(relCatItem.getIsCategoriaTotal());
//							
//						RelItemValor relItemValor = RelItemValor.findByItemUltimo(item.getId());
//						
//						if(relItemValor != null) {
//							
//							itemRelatorio.setTipoMoeda(relItemValor.getUnidade());
//							
//							if(relItemValor.getUnidade() == RelItemValor.Unidade.REAL.getCodigo()) {
//								itemRelatorio.setValor(String.valueOf(relItemValor.getValor()));
//								itemRelatorio.setValorDolar(String.valueOf(MonetaryUtil.divide(relItemValor.getValor(), cambioResponse.getValorDolar())));
//							} else if(relItemValor.getUnidade() == RelItemValor.Unidade.DOLAR.getCodigo()) {
//								itemRelatorio.setValor(String.valueOf(MonetaryUtil.multiply(relItemValor.getValor(), cambioResponse.getValorDolar())));
//								itemRelatorio.setValorDolar(String.valueOf(relItemValor.getValor()));
//							} else if(relItemValor.getUnidade() == RelItemValor.Unidade.PERCENTUAL.getCodigo()) {
//								itemRelatorio.setValor(String.valueOf(relItemValor.getValor()));
//								itemRelatorio.setValorDolar(String.valueOf(relItemValor.getValor()));
//							}
//						}
//						
//						itemResponseList.add(itemRelatorio);
//					}
//					
//					categoriaRelatorio.setItemList(itemResponseList);
//					categoriaResponseList.add(categoriaRelatorio);
//				}
//				
//				cotacaoRelatorio.setCategoriaList(categoriaResponseList);
//				relatorioResponses.add(cotacaoRelatorio);
//			}
//		}
//        
//		return ok(Json.toJson(relatorioResponses));
//	}
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	public static Result findAll() {
//
//		LogisticaHistorico logisticaHistorico = LogisticaHistorico.findAtual();
//		List<Logistica> list = Logistica.findAll();//findAllByHistorico(logisticaHistorico.getId());
//		
//		return ok(Json.toJson(list));
//			
//	}
	
}