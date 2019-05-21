package controllers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.BlocoEconomico;
import models.ComtradeDataSumarizacao;
import models.Continente;
import models.Pais;
import models.RelPaisBlocoEconomico;
import models.USDAData;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import play.libs.Json;
import play.mvc.Result;
import util.AbicsLogConfig;
import util.ExcelUtil;
import entitys.response.AbicsDataRelatorioComtradeResponse;
import entitys.response.AbicsDataRelatorioUSDAResponse;
import entitys.response.GraficoCategoriesColumnsResponse;

public class AbicsDataRelatorioController extends AbstractController {

//	private static String yearList[] = new String[]{"2016", "2015", "2014","2013","2012","2011","2010","2009","2008","2007","2006","2005","2004","2003","2002","2001","2000"};

//	public static Result findPaisBrasil() {
//		Pais brasil = Pais.findById(68L);
//		return ok(Json.toJson(brasil));
//	}
	
	public static Result findAllPais() {
		
		List<Pais> list = Pais.findAll();
		return ok(Json.toJson(list));
//		return ok(Json.toJson(USDAData.distinctPais()));
		
	}
	
	public static Result findAllContinente() {
		
		List<Continente> list = Continente.findAll();
		return ok(Json.toJson(list));
	}
	
	public static Result findAllBlocoEconomico() {
		
		List<BlocoEconomico> list = BlocoEconomico.findAll();
		return ok(Json.toJson(list));
	}
	
//	public static Result findAllPaisByContinente(Long idContinente) {
//		
//		List<Pais> paisList = Pais.findByContinente(idContinente);
//		return ok(Json.toJson(paisList));
//	}

//	public static Result findAllPaisByBlocoEconomico(Long idBlocoEconomico) {
//
//		List<Pais> paisList = new ArrayList<Pais>();
//		List<RelPaisBlocoEconomico> blocoEconomicoList = RelPaisBlocoEconomico.findAllByBlocoEconomico(idBlocoEconomico);
//		
//		for(RelPaisBlocoEconomico rpbe : blocoEconomicoList) {
//			
//			Pais p = Pais.findById(rpbe.getPaisId());
//			paisList.add(p);
//		}
//		
//		return ok(Json.toJson(paisList));		
//	}
	
    public static Result consultarGraficoUSDA() {

    	Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] tipoArr = asFormUrlEncoded.get("tipo");
		String tipo = tipoArr[0];
		String[] fonteArr = asFormUrlEncoded.get("fonte");
		String fonte = fonteArr[0];
		String[] paisListArr = asFormUrlEncoded.get("paisList");
		String[] paisIdList = paisListArr[0].split(",");
		String[] continenteListArr = asFormUrlEncoded.get("continenteList");
		String[] continenteIdList = continenteListArr[0].split(",");
		String[] blocoEconomicoListArr = asFormUrlEncoded.get("blocoEconomicoList");
		String[] blocoEconomicoIdList = blocoEconomicoListArr[0].split(",");
		String[] tipoCafeArr = asFormUrlEncoded.get("tipoCafe");
		String[] tipoCafes = tipoCafeArr[0].split(",");
		
		String[] anoInicialArr = asFormUrlEncoded.get("anoInicial");
		String anoInicial = anoInicialArr[0];
		
		String[] anoFinalArr = asFormUrlEncoded.get("anoFinal");
		String anoFinal = anoFinalArr[0];
		
		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
		List<String> categories = new ArrayList<String>();
		Set<Pais> paisAgregadoList = new HashSet<Pais>();
		
		if(paisIdList != null && paisIdList.length > 0) {
			for(String idPais : paisIdList) {
				if(idPais.length() > 0) { 
					paisAgregadoList.add(Pais.findById(Long.valueOf(idPais)));
				}
			}			
		}
		
		if(continenteIdList != null && continenteIdList.length > 0) {
			
			for(String idContinente : continenteIdList) {
				if(idContinente.length() > 0) {
					paisAgregadoList.addAll(Pais.findByContinente(Long.valueOf(idContinente)));	
				}
			}
		}
		
		if(blocoEconomicoIdList != null && blocoEconomicoIdList.length > 0) {
			for(String idBlocoEconomico : blocoEconomicoIdList) {
				if(idBlocoEconomico.length() > 0) {
					List<RelPaisBlocoEconomico> rpbeList = RelPaisBlocoEconomico.findAllByBlocoEconomico(Long.valueOf(idBlocoEconomico));
					for(RelPaisBlocoEconomico rpbe : rpbeList) {
						Pais p = Pais.findById(rpbe.getPaisId());
						paisAgregadoList.add(p);
					}
				}
			}
		}
		
		Integer anoInicialInteger = Integer.valueOf(anoInicial);
		Integer anoFinalInteger = Integer.valueOf(anoFinal);
		Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
		Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
		
		for(Integer i = 0; i < tamanhoArrayAno; i++) {
			anoPeriodo[i] = anoInicialInteger + i;
		}
		
    	for(int i = 0; i < anoPeriodo.length; i++) {
			categories.add(anoPeriodo[i].toString());
		}
    	
		graficoItemValorEntity.setCategories(categories);
		
		if(tipo.equalsIgnoreCase("exportação")) {
			tipo = "export";
		} else if(tipo.equalsIgnoreCase("importação")) {
			tipo = "import";
		} else if(tipo.equalsIgnoreCase("produção")) {
			tipo = "production";
		}
		
		for(String tipoCafe : tipoCafes) {
			for(Pais p : paisAgregadoList) {
				
				List<String> columns = new ArrayList<String>();
				columns.add(p.getNome() + " - "+ tipoCafe);
				
				for(Integer ano : anoPeriodo) {
					String valor = "0";
					
					USDAData usdaData = USDAData.findUniqueByCommodityPaisAnoTipo(tipoCafe.toUpperCase(), p.getUsdaNome(),ano.toString(),tipo);
					
					if(usdaData != null) {
						valor = String.valueOf(usdaData.getQuantidade());
					}
					
					columns.add(valor);
					graficoItemValorEntity.addColumns(columns);
					
				}
			}
		}
		graficoItemValorEntity.setCountTipoCafes(tipoCafes.length);
		graficoItemValorEntity.setCountPaises(paisAgregadoList.size());
		return ok(Json.toJson(graficoItemValorEntity));
    }
    
    public static Result consultarGraficoUSDAAgregado(String tipoConsulta) {

    	Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] tipoArr = asFormUrlEncoded.get("tipo");
		String tipo = tipoArr[0];
		String[] fonteArr = asFormUrlEncoded.get("fonte");
		String fonte = fonteArr[0];
		String[] paisListArr = asFormUrlEncoded.get("paisList");
		String[] paisIdList = paisListArr[0].split(",");
		String[] continenteListArr = asFormUrlEncoded.get("continenteList");
		String[] continenteIdList = continenteListArr[0].split(",");
		String[] blocoEconomicoListArr = asFormUrlEncoded.get("blocoEconomicoList");
		String[] blocoEconomicoIdList = blocoEconomicoListArr[0].split(",");
		String[] tipoCafeArr = asFormUrlEncoded.get("tipoCafe");
		String[] tipoCafes = tipoCafeArr[0].split(",");
		
		String[] anoInicialArr = asFormUrlEncoded.get("anoInicial");
		String anoInicial = anoInicialArr[0];
		
		String[] anoFinalArr = asFormUrlEncoded.get("anoFinal");
		String anoFinal = anoFinalArr[0];
		
		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
		List<String> categories = new ArrayList<String>();
		Set<Pais> paisAgregadoList = new HashSet<Pais>();
		
		if(paisIdList != null && paisIdList.length > 0) {
			for(String idPais : paisIdList) {
				if(idPais.length() > 0) { 
					paisAgregadoList.add(Pais.findById(Long.valueOf(idPais)));
				}
			}			
		}
		
		if(continenteIdList != null && continenteIdList.length > 0) {
			
			for(String idContinente : continenteIdList) {
				if(idContinente.length() > 0) {
					paisAgregadoList.addAll(Pais.findByContinente(Long.valueOf(idContinente)));	
				}
			}
		}
		
		if(blocoEconomicoIdList != null && blocoEconomicoIdList.length > 0) {
			for(String idBlocoEconomico : blocoEconomicoIdList) {
				if(idBlocoEconomico.length() > 0) {
					List<RelPaisBlocoEconomico> rpbeList = RelPaisBlocoEconomico.findAllByBlocoEconomico(Long.valueOf(idBlocoEconomico));
					for(RelPaisBlocoEconomico rpbe : rpbeList) {
						Pais p = Pais.findById(rpbe.getPaisId());
						paisAgregadoList.add(p);
					}
				}
			}
		}
		
		Integer anoInicialInteger = Integer.valueOf(anoInicial);
		Integer anoFinalInteger = Integer.valueOf(anoFinal);
		Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
		Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
		
		for(Integer i = 0; i < tamanhoArrayAno; i++) {
			anoPeriodo[i] = anoInicialInteger + i;
		}
		
    	for(int i = 0; i < anoPeriodo.length; i++) {
			categories.add(anoPeriodo[i].toString());
		}
    	
		graficoItemValorEntity.setCategories(categories);
		
		if(tipo.equalsIgnoreCase("exportação")) {
			tipo = "export";
		} else if(tipo.equalsIgnoreCase("importação")) {
			tipo = "import";
		} else if(tipo.equalsIgnoreCase("produção")) {
			tipo = "production";
		}

		if(tipoConsulta.equals("cafe")) {
			for(Pais p : paisAgregadoList) {
				List<String> columns = new ArrayList<String>();
				columns.add(p.getNome() + " - "+ "café agregado");
					
				for(Integer ano : anoPeriodo) {
					Long valor = 0L;
					
					for(String tipoCafe : tipoCafes) {
						
						USDAData usdaData = USDAData.findUniqueByCommodityPaisAnoTipo(tipoCafe.toUpperCase(), p.getUsdaNome(),ano.toString(),tipo);
						
						if(usdaData != null) {
							valor += usdaData.getQuantidade();
						}
					}
					columns.add(valor.toString());
					graficoItemValorEntity.addColumns(columns);
				}
			}
		} else if(tipoConsulta.equals("pais")){
			for(String tipoCafe : tipoCafes) {
				List<String> columns = new ArrayList<String>();
				columns.add("Países Agregados - "+ tipoCafe);
				
				for(Integer ano : anoPeriodo) {
					Long valor = 0L;
					
					for(Pais p : paisAgregadoList) {
						USDAData usdaData = USDAData.findUniqueByCommodityPaisAnoTipo(tipoCafe.toUpperCase(), p.getUsdaNome(),ano.toString(),tipo);
						
						if(usdaData != null) {
							valor += usdaData.getQuantidade();
						}
					}
					columns.add(valor.toString());
					graficoItemValorEntity.addColumns(columns);
				}
			}
		}
		graficoItemValorEntity.setCountPaises(paisAgregadoList.size());
		graficoItemValorEntity.setCountTipoCafes(tipoCafes.length);
		return ok(Json.toJson(graficoItemValorEntity));
    }
	   
    public static Result consultarGraficoComtrade(String tipoDadoComtrade) {

    	Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] tipoArr = asFormUrlEncoded.get("tipo");
		String tipo = tipoArr[0];
		String[] fonteArr = asFormUrlEncoded.get("fonte");
		String fonte = fonteArr[0];
		String[] paisListArr = asFormUrlEncoded.get("paisList");
		String[] paisIdList = paisListArr[0].split(",");
		String[] continenteListArr = asFormUrlEncoded.get("continenteList");
		String[] continenteIdList = continenteListArr[0].split(",");
		String[] blocoEconomicoListArr = asFormUrlEncoded.get("blocoEconomicoList");
		String[] blocoEconomicoIdList = blocoEconomicoListArr[0].split(",");
		String[] tipoCafeArr = asFormUrlEncoded.get("tipoCafe");
		String[] tipoCafes = tipoCafeArr[0].split(",");
		
		String[] anoInicialArr = asFormUrlEncoded.get("anoInicial");
		String anoInicial = anoInicialArr[0];
		
		String[] anoFinalArr = asFormUrlEncoded.get("anoFinal");
		String anoFinal = anoFinalArr[0];
		
		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
		List<String> categories = new ArrayList<String>();
		Set<Pais> paisAgregadoList = new HashSet<Pais>();
		
		if(paisIdList != null && paisIdList.length > 0) {
			for(String idPais : paisIdList) {
				if(idPais.length() > 0) { 
					paisAgregadoList.add(Pais.findById(Long.valueOf(idPais)));
				}
			}			
		}
		
		if(continenteIdList != null && continenteIdList.length > 0) {
			
			for(String idContinente : continenteIdList) {
				if(idContinente.length() > 0) {
					paisAgregadoList.addAll(Pais.findByContinente(Long.valueOf(idContinente)));	
				}
			}
		}
		
		if(blocoEconomicoIdList != null && blocoEconomicoIdList.length > 0) {
			for(String idBlocoEconomico : blocoEconomicoIdList) {
				if(idBlocoEconomico.length() > 0) {
					List<RelPaisBlocoEconomico> rpbeList = RelPaisBlocoEconomico.findAllByBlocoEconomico(Long.valueOf(idBlocoEconomico));
					for(RelPaisBlocoEconomico rpbe : rpbeList) {
						Pais p = Pais.findById(rpbe.getPaisId());
						paisAgregadoList.add(p);
					}
				}
			}
		}
		
		Integer anoInicialInteger = Integer.valueOf(anoInicial);
		Integer anoFinalInteger = Integer.valueOf(anoFinal);
		Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
		Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
		
		for(Integer i = 0; i < tamanhoArrayAno; i++) {
			anoPeriodo[i] = anoInicialInteger + i;
		}
		
    	for(int i = 0; i < anoPeriodo.length; i++) {
			categories.add(anoPeriodo[i].toString());
		}    
		graficoItemValorEntity.setCategories(categories);
		
		if(tipo.equalsIgnoreCase("produção")) {
			tipo = "0";
		} else if(tipo.equalsIgnoreCase("exportação")) {
			tipo = "2";
		} else if(tipo.equalsIgnoreCase("importação")) {
			tipo = "1";
		}
		
		for(String tipoCafe : tipoCafes) {
			for(Pais p : paisAgregadoList) {
				
				List<String> columns = new ArrayList<String>();
				columns.add(p.getNome() + " - "+ tipoCafe);
				
				for(Integer ano : anoPeriodo) {
					String valor = "0";
					
					List<ComtradeDataSumarizacao> comtradeList = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYr(
								tipo.toString(), p.getComtradeId().toString(), "0", ano.toString());
						
					if(comtradeList != null && comtradeList.size() > 0) {
						
						ComtradeDataSumarizacao comtrade = comtradeList.get(0);
						if(comtrade.getYr().equals(comtrade.getPeriod())) {
							if(tipoDadoComtrade.equalsIgnoreCase("tradeValue")) {
								valor = comtrade.getTradeValue().toString();
							} else if(tipoDadoComtrade.equalsIgnoreCase("NetWeight")) {
								valor = comtrade.getNetWeight().toString();
							}
						}
					}
					
					columns.add(valor);
					graficoItemValorEntity.addColumns(columns);
					
				}
			}
		}
		graficoItemValorEntity.setCountPaises(paisAgregadoList.size());
		return ok(Json.toJson(graficoItemValorEntity));
    }
    
    public static Result consultarGraficoComtradeAgregado(String tipoDadoComtrade) {

    	Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] tipoArr = asFormUrlEncoded.get("tipo");
		String tipo = tipoArr[0];
		String[] fonteArr = asFormUrlEncoded.get("fonte");
		String fonte = fonteArr[0];
		String[] paisListArr = asFormUrlEncoded.get("paisList");
		String[] paisIdList = paisListArr[0].split(",");
		String[] continenteListArr = asFormUrlEncoded.get("continenteList");
		String[] continenteIdList = continenteListArr[0].split(",");
		String[] blocoEconomicoListArr = asFormUrlEncoded.get("blocoEconomicoList");
		String[] blocoEconomicoIdList = blocoEconomicoListArr[0].split(",");
		String[] tipoCafeArr = asFormUrlEncoded.get("tipoCafe");
		String[] tipoCafes = tipoCafeArr[0].split(",");
		
		String[] anoInicialArr = asFormUrlEncoded.get("anoInicial");
		String anoInicial = anoInicialArr[0];
		
		String[] anoFinalArr = asFormUrlEncoded.get("anoFinal");
		String anoFinal = anoFinalArr[0];
		
		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
		List<String> categories = new ArrayList<String>();
		Set<Pais> paisAgregadoList = new HashSet<Pais>();
		
		if(paisIdList != null && paisIdList.length > 0) {
			for(String idPais : paisIdList) {
				if(idPais.length() > 0) { 
					paisAgregadoList.add(Pais.findById(Long.valueOf(idPais)));
				}
			}			
		}
		
		if(continenteIdList != null && continenteIdList.length > 0) {
			
			for(String idContinente : continenteIdList) {
				if(idContinente.length() > 0) {
					paisAgregadoList.addAll(Pais.findByContinente(Long.valueOf(idContinente)));	
				}
			}
		}
		
		if(blocoEconomicoIdList != null && blocoEconomicoIdList.length > 0) {
			for(String idBlocoEconomico : blocoEconomicoIdList) {
				if(idBlocoEconomico.length() > 0) {
					List<RelPaisBlocoEconomico> rpbeList = RelPaisBlocoEconomico.findAllByBlocoEconomico(Long.valueOf(idBlocoEconomico));
					for(RelPaisBlocoEconomico rpbe : rpbeList) {
						Pais p = Pais.findById(rpbe.getPaisId());
						paisAgregadoList.add(p);
					}
				}
			}
		}
		
		Integer anoInicialInteger = Integer.valueOf(anoInicial);
		Integer anoFinalInteger = Integer.valueOf(anoFinal);
		Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
		Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
		
		for(Integer i = 0; i < tamanhoArrayAno; i++) {
			anoPeriodo[i] = anoInicialInteger + i;
		}
		
    	for(int i = 0; i < anoPeriodo.length; i++) {
			categories.add(anoPeriodo[i].toString());
		}    
		graficoItemValorEntity.setCategories(categories);
		
		if(tipo.equalsIgnoreCase("produção")) {
			tipo = "0";
		} else if(tipo.equalsIgnoreCase("exportação")) {
			tipo = "2";
		} else if(tipo.equalsIgnoreCase("importação")) {
			tipo = "1";
		}
		
		for(String tipoCafe : tipoCafes) {
			List<String> columns = new ArrayList<String>();
			columns.add("Países agregados - " + tipoCafe);
				
			for(Integer ano : anoPeriodo) {
				Long valor = 0L;
				
				for(Pais p : paisAgregadoList) {
					List<ComtradeDataSumarizacao> comtradeList = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYr(
								tipo.toString(), p.getComtradeId().toString(), "0", ano.toString());
						
					if(comtradeList != null && comtradeList.size() > 0) {
						
						ComtradeDataSumarizacao comtrade = comtradeList.get(0);
						if(comtrade.getYr().equals(comtrade.getPeriod())) {
							if(tipoDadoComtrade.equalsIgnoreCase("tradeValue")) {
								valor += comtrade.getTradeValue();
							} else if(tipoDadoComtrade.equalsIgnoreCase("NetWeight")) {
								valor += comtrade.getNetWeight();
							}
						}
					}
				}
				columns.add(valor.toString());
				graficoItemValorEntity.addColumns(columns);
				
			}
		}
		graficoItemValorEntity.setCountPaises(paisAgregadoList.size());
		return ok(Json.toJson(graficoItemValorEntity));
    }
    
    public static Result consultarTabela() {

    	Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] tipoArr = asFormUrlEncoded.get("tipo");
		String tipo = tipoArr[0];
		String[] fonteArr = asFormUrlEncoded.get("fonte");
		String fonte = fonteArr[0];
		String[] paisListArr = asFormUrlEncoded.get("paisList");
		String[] paisIdList = paisListArr[0].split(",");
		String[] continenteListArr = asFormUrlEncoded.get("continenteList");
		String[] continenteIdList = continenteListArr[0].split(",");
		String[] blocoEconomicoListArr = asFormUrlEncoded.get("blocoEconomicoList");
		String[] blocoEconomicoIdList = blocoEconomicoListArr[0].split(",");
		String[] tipoCafeArr = asFormUrlEncoded.get("tipoCafe");
		String[] tipoCafes = tipoCafeArr[0].split(",");
		
		String[] anoInicialArr = asFormUrlEncoded.get("anoInicial");
		String anoInicial = anoInicialArr[0];
		
		String[] anoFinalArr = asFormUrlEncoded.get("anoFinal");
		String anoFinal = anoFinalArr[0];
		
		Set<Pais> paisAgregadoList = new HashSet<Pais>();
		
		if(paisIdList != null && paisIdList.length > 0) {
			for(String idPais : paisIdList) {
				if(idPais.length() > 0) { 
					paisAgregadoList.add(Pais.findById(Long.valueOf(idPais)));
				}
			}			
		}
		
		if(continenteIdList != null && continenteIdList.length > 0) {
			
			for(String idContinente : continenteIdList) {
				if(idContinente.length() > 0) {
					paisAgregadoList.addAll(Pais.findByContinente(Long.valueOf(idContinente)));	
				}
			}
		}
		
		if(blocoEconomicoIdList != null && blocoEconomicoIdList.length > 0) {
			for(String idBlocoEconomico : blocoEconomicoIdList) {
				if(idBlocoEconomico.length() > 0) {
					List<RelPaisBlocoEconomico> rpbeList = RelPaisBlocoEconomico.findAllByBlocoEconomico(Long.valueOf(idBlocoEconomico));
					for(RelPaisBlocoEconomico rpbe : rpbeList) {
						Pais p = Pais.findById(rpbe.getPaisId());
						paisAgregadoList.add(p);
					}
				}
			}
		}
		
		if(fonte.equalsIgnoreCase("usda")) {
			
			if(tipo.equalsIgnoreCase("produção")) {
				tipo = "production";
			} else if(tipo.equalsIgnoreCase("exportação")) {
				tipo = "export";
			} else if(tipo.equalsIgnoreCase("importação")) {
				tipo = "import";
			}
			
			List<AbicsDataRelatorioUSDAResponse> responseList = new ArrayList<AbicsDataRelatorioUSDAResponse>();
			
			Integer anoInicialInteger = Integer.valueOf(anoInicial);
			Integer anoFinalInteger = Integer.valueOf(anoFinal);
			Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
			Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
			
			for(Integer i = 0; i < tamanhoArrayAno; i++) {
				anoPeriodo[i] = anoInicialInteger + i;
			}
			
			for(String tipoCafe : tipoCafes) {
				for(Pais p : paisAgregadoList) {
					for(Integer ano : anoPeriodo) {
						USDAData usdaData = USDAData.findUniqueByCommodityPaisAnoTipo(tipoCafe.toUpperCase(), p.getUsdaNome(),ano.toString(),tipo);

						if(usdaData != null && usdaData.getQuantidade() != 0L) {
							AbicsDataRelatorioUSDAResponse response = 
									new AbicsDataRelatorioUSDAResponse(tipo, tipoCafe, p.getUsdaNome(), ano.toString(), String.valueOf(usdaData.getQuantidade())); 
							responseList.add(response);	
						}
					}
				}
			}
			
			return ok(Json.toJson(responseList)); 
		}
		
		if(fonte.equalsIgnoreCase("comtrade")) {
			
			String tipoInt = "";
			
			if(tipo.equalsIgnoreCase("produção")) {
				tipoInt = "0";
			} else if(tipo.equalsIgnoreCase("exportação")) {
				tipoInt = "2";
			} else if(tipo.equalsIgnoreCase("importação")) {
				tipoInt = "1";
			}
			
			List<AbicsDataRelatorioComtradeResponse> responseList = new ArrayList<AbicsDataRelatorioComtradeResponse>();
			
			Integer anoInicialInteger = Integer.valueOf(anoInicial);
			Integer anoFinalInteger = Integer.valueOf(anoFinal);
			Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
			Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
			
			for(Integer i = 0; i < tamanhoArrayAno; i++) {
				anoPeriodo[i] = anoInicialInteger + i;
			}
			
			for(String tipoCafe : tipoCafes) {
				for(Pais p : paisAgregadoList) {
					for(Integer ano : anoPeriodo) {
						
						List<ComtradeDataSumarizacao> comtradeList = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYr(
								tipoInt, p.getComtradeId().toString(), "0", ano.toString());
						
						if(comtradeList != null && comtradeList.size() > 0) {
							
							ComtradeDataSumarizacao comtrade = comtradeList.get(0);
							if(comtrade.getYr().equals(comtrade.getPeriod())) {
								AbicsDataRelatorioComtradeResponse response = 
										new AbicsDataRelatorioComtradeResponse(tipo, tipoCafe, comtrade.getRtTitle(), ano.toString(), 
												String.valueOf(comtrade.getTradeValue()), String.valueOf(comtrade.getNetWeight())); 
								responseList.add(response);	
							}
						}
					}
				}
			}
			
			return ok(Json.toJson(responseList)); 
		}	
		
		return ok();
    }
    
    public static Result consultarTabelaAgregada() {

    	Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] tipoArr = asFormUrlEncoded.get("tipo");
		String tipo = tipoArr[0];
		String[] fonteArr = asFormUrlEncoded.get("fonte");
		String fonte = fonteArr[0];
		String[] paisListArr = asFormUrlEncoded.get("paisList");
		String[] paisIdList = paisListArr[0].split(",");
		String[] continenteListArr = asFormUrlEncoded.get("continenteList");
		String[] continenteIdList = continenteListArr[0].split(",");
		String[] blocoEconomicoListArr = asFormUrlEncoded.get("blocoEconomicoList");
		String[] blocoEconomicoIdList = blocoEconomicoListArr[0].split(",");
		String[] tipoCafeArr = asFormUrlEncoded.get("tipoCafe");
		String[] tipoCafes = tipoCafeArr[0].split(",");
		String[] agregadoUSDAArr = asFormUrlEncoded.get("agregadoUSDA");
		String agregadoUSDA = agregadoUSDAArr[0];
		
		String[] anoInicialArr = asFormUrlEncoded.get("anoInicial");
		String anoInicial = anoInicialArr[0];
		
		String[] anoFinalArr = asFormUrlEncoded.get("anoFinal");
		String anoFinal = anoFinalArr[0];
		
		Set<Pais> paisAgregadoList = new HashSet<Pais>();
		
		if(paisIdList != null && paisIdList.length > 0) {
			for(String idPais : paisIdList) {
				if(idPais.length() > 0) { 
					paisAgregadoList.add(Pais.findById(Long.valueOf(idPais)));
				}
			}			
		}
		
		if(continenteIdList != null && continenteIdList.length > 0) {
			
			for(String idContinente : continenteIdList) {
				if(idContinente.length() > 0) {
					paisAgregadoList.addAll(Pais.findByContinente(Long.valueOf(idContinente)));	
				}
			}
		}
		
		if(blocoEconomicoIdList != null && blocoEconomicoIdList.length > 0) {
			for(String idBlocoEconomico : blocoEconomicoIdList) {
				if(idBlocoEconomico.length() > 0) {
					List<RelPaisBlocoEconomico> rpbeList = RelPaisBlocoEconomico.findAllByBlocoEconomico(Long.valueOf(idBlocoEconomico));
					for(RelPaisBlocoEconomico rpbe : rpbeList) {
						Pais p = Pais.findById(rpbe.getPaisId());
						paisAgregadoList.add(p);
					}
				}
			}
		}
		
		if(fonte.equalsIgnoreCase("usda")) {
			
			if(tipo.equalsIgnoreCase("produção")) {
				tipo = "production";
			} else if(tipo.equalsIgnoreCase("exportação")) {
				tipo = "export";
			} else if(tipo.equalsIgnoreCase("importação")) {
				tipo = "import";
			}
			
			List<AbicsDataRelatorioUSDAResponse> responseList = new ArrayList<AbicsDataRelatorioUSDAResponse>();
			
			Integer anoInicialInteger = Integer.valueOf(anoInicial);
			Integer anoFinalInteger = Integer.valueOf(anoFinal);
			Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
			Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
			
			for(Integer i = 0; i < tamanhoArrayAno; i++) {
				anoPeriodo[i] = anoInicialInteger + i;
			}
			
			if(agregadoUSDA.equals("pais")) {
				for(String tipoCafe : tipoCafes) {
					for(Integer ano : anoPeriodo) {
						USDAData usdaData = null;
						Long quantidade = 0L;
						
						for(Pais p : paisAgregadoList) {
							usdaData = USDAData.findUniqueByCommodityPaisAnoTipo(tipoCafe, p.getUsdaNome(),ano.toString(),tipo);

							if(usdaData != null) {
								quantidade += usdaData.getQuantidade();
							}
						}
						AbicsDataRelatorioUSDAResponse response = 
								new AbicsDataRelatorioUSDAResponse(tipo, tipoCafe, "Países agregados", ano.toString(), String.valueOf(quantidade)); 
						responseList.add(response);
					}
				}
			} else if(agregadoUSDA.equals("cafe")) {
				for(Pais p : paisAgregadoList) {
					for(Integer ano : anoPeriodo) {
						USDAData usdaData = null;
						Long quantidade = 0L;
						
						for(String tipoCafe : tipoCafes) {
							usdaData = USDAData.findUniqueByCommodityPaisAnoTipo(tipoCafe, p.getUsdaNome(),ano.toString(),tipo);
							
							if(usdaData != null) {
								quantidade += usdaData.getQuantidade();
							}
						}
						if(usdaData != null && quantidade != 0L) {
							AbicsDataRelatorioUSDAResponse response = 
									new AbicsDataRelatorioUSDAResponse(tipo, "café agregado", usdaData.getPais(), ano.toString(), String.valueOf(quantidade)); 
							responseList.add(response);	
						}
					}
				}
			}
			return ok(Json.toJson(responseList)); 
		}
		
		if(fonte.equalsIgnoreCase("comtrade")) {
			
			String tipoInt = "";
			
			if(tipo.equalsIgnoreCase("produção")) {
				tipoInt = "0";
			} else if(tipo.equalsIgnoreCase("exportação")) {
				tipoInt = "2";
			} else if(tipo.equalsIgnoreCase("importação")) {
				tipoInt = "1";
			}
			
			List<AbicsDataRelatorioComtradeResponse> responseList = new ArrayList<AbicsDataRelatorioComtradeResponse>();
			
			Integer anoInicialInteger = Integer.valueOf(anoInicial);
			Integer anoFinalInteger = Integer.valueOf(anoFinal);
			Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
			Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
			
			for(Integer i = 0; i < tamanhoArrayAno; i++) {
				anoPeriodo[i] = anoInicialInteger + i;
			}
			
			for(String tipoCafe : tipoCafes) {
				for(Integer ano : anoPeriodo) {
					ComtradeDataSumarizacao comtrade = null;
					Long tradeValue = 0L;
					Long netWeight = 0L;
					
					for(Pais p : paisAgregadoList) {
						
						List<ComtradeDataSumarizacao> comtradeList = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYr(
								tipoInt, p.getComtradeId().toString(), "0", ano.toString());
						
						if(comtradeList != null && comtradeList.size() > 0) {
							comtrade = comtradeList.get(0);
							if(comtrade.getYr().equals(comtrade.getPeriod())) {
								tradeValue += comtrade.getTradeValue();
								netWeight += comtrade.getNetWeight();
							}
						}
					}
						
					AbicsDataRelatorioComtradeResponse response = 
							new AbicsDataRelatorioComtradeResponse(tipo, tipoCafe, "Países Agregados", ano.toString(), 
									String.valueOf(tradeValue), String.valueOf(netWeight)); 
					responseList.add(response);	
				}
			}
			
			return ok(Json.toJson(responseList)); 
		}	
		
		return ok();
    }
    
    public static Result imprimirRelatorio() {
    	
    	Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] tipoArr = asFormUrlEncoded.get("tipo");
		String tipo = tipoArr[0];
		String[] fonteArr = asFormUrlEncoded.get("fonte");
		String fonte = fonteArr[0];
		String[] paisListArr = asFormUrlEncoded.get("paisList");
		String[] paisIdList = paisListArr[0].split(",");
		String[] continenteListArr = asFormUrlEncoded.get("continenteList");
		String[] continenteIdList = continenteListArr[0].split(",");
		String[] blocoEconomicoListArr = asFormUrlEncoded.get("blocoEconomicoList");
		String[] blocoEconomicoIdList = blocoEconomicoListArr[0].split(",");
		String[] tipoCafeArr = asFormUrlEncoded.get("tipoCafe");
		String[] tipoCafes = tipoCafeArr[0].split(",");
		
		String[] anoInicialArr = asFormUrlEncoded.get("anoInicial");
		String anoInicial = anoInicialArr[0];
		
		String[] anoFinalArr = asFormUrlEncoded.get("anoFinal");
		String anoFinal = anoFinalArr[0];
		
		List<Pais> paisAgregadoList = new ArrayList<Pais>();
		
		if(paisIdList != null && paisIdList.length > 0) {
			for(String idPais : paisIdList) {
				if(idPais.length() > 0) { 
					paisAgregadoList.add(Pais.findById(Long.valueOf(idPais)));
				}
			}			
		}
		
		if(continenteIdList != null && continenteIdList.length > 0) {
			
			for(String idContinente : continenteIdList) {
				if(idContinente.length() > 0) {
					paisAgregadoList.addAll(Pais.findByContinente(Long.valueOf(idContinente)));	
				}
			}
		}
		
		if(blocoEconomicoIdList != null && blocoEconomicoIdList.length > 0) {
			for(String idBlocoEconomico : blocoEconomicoIdList) {
				if(idBlocoEconomico.length() > 0) {
					List<RelPaisBlocoEconomico> rpbeList = RelPaisBlocoEconomico.findAllByBlocoEconomico(Long.valueOf(idBlocoEconomico));
					for(RelPaisBlocoEconomico rpbe : rpbeList) {
						Pais p = Pais.findById(rpbe.getPaisId());
						paisAgregadoList.add(p);
					}
				}
			}
		}
		
		try {
			
			String excelFileName = AbicsLogConfig.getString(AbicsLogConfig.USER_REPORT_FOLDER) + getUsuarioLogado().getId() 
					+"/relatorioPaisAnual.xlsx";
			
			String sheetName1 = "";
			String sheetName2 = "Comtrade";
			
			if(fonte.equalsIgnoreCase("usda")) {
				sheetName1 = "USDA - quantidade";//name of sheet
				
			} else if(fonte.equalsIgnoreCase("comtrade")) {
				sheetName1 = "Comtrade - Net Weight";//name of sheet
				sheetName2 = "Comtrade - Trade Value";//name of sheet
			}
			
			HSSFWorkbook wb = new HSSFWorkbook();
	
			HSSFSheet sheetUSDA = wb.createSheet(sheetName1);
			HSSFSheet sheetComtrade = wb.createSheet(sheetName2);
			
			HSSFColor lightPink =  ExcelUtil.setColor(wb, (byte)(Integer.parseInt("255")) , (byte)(Integer.parseInt("227")) ,(byte)(Integer.parseInt("221")), new HSSFColor.PINK());
			CellStyle cellStyleTipos = ExcelUtil.createStyle(wb, IndexedColors.BROWN.getIndex(), lightPink.getIndex() , (short) 11, true, false, true);
			
			CellStyle cellStylePaisHeader = ExcelUtil.createStyle(wb, IndexedColors.WHITE.getIndex(), IndexedColors.BROWN.getIndex(), (short) 13, true, false, true);
			CellStyle cellStyleHeader = ExcelUtil.createStyle(wb, IndexedColors.BROWN.getIndex(), IndexedColors.WHITE.getIndex(), (short) 13, true, false, true);
			CellStyle cellStyleAnoPaisHeader = ExcelUtil.createStyle(wb, IndexedColors.BROWN.getIndex(), IndexedColors.WHITE.getIndex(), (short) 11, true, false, true);
			CellStyle cellStyleDados = ExcelUtil.createStyle(wb, IndexedColors.BLACK.getIndex(), IndexedColors.WHITE.getIndex(), (short) 11, false, false, true);
			
			HSSFRow row0USDA = sheetUSDA.createRow(0);
			HSSFCell cellCabecalho1 = row0USDA.createCell(0);
			cellCabecalho1.setCellValue("Fonte: " + fonte.toUpperCase() + " (Anual) - Período: " + anoInicial + " / " + anoFinal);
			cellCabecalho1.setCellStyle(cellStyleHeader);
			
			HSSFRow row0Comtrade = sheetComtrade.createRow(0);
			HSSFCell cellCabecalho2 = row0Comtrade.createCell(0);
			cellCabecalho2.setCellValue("Fonte: " + fonte.toUpperCase() + " (Anual) - Período: " + anoInicial + " / " + anoFinal);
			cellCabecalho2.setCellStyle(cellStyleHeader);

			HSSFRow row1USDATipo = sheetUSDA.createRow(1);
			HSSFCell cellData = row1USDATipo.createCell(0);
			cellData.setCellValue("PAÍS DESTINO");
			cellData.setCellStyle(cellStylePaisHeader);
			
			HSSFRow row1ComtradeTipo = sheetComtrade.createRow(1);
			HSSFCell cellDataComtrade = row1ComtradeTipo.createCell(0);
			cellDataComtrade.setCellValue("PAÍS DESTINO");
			cellDataComtrade.setCellStyle(cellStylePaisHeader);
			
			Integer anoInicialInteger = Integer.valueOf(anoInicial);
			Integer anoFinalInteger = Integer.valueOf(anoFinal);
			Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
			Integer[] anoPeriodo = new Integer[tamanhoArrayAno];

			for(Integer i = 0; i < tamanhoArrayAno; i++) {
				anoPeriodo[i] = anoInicialInteger + i;
			}
			
			
			HSSFRow row2USDAAno = sheetUSDA.createRow(2);
			HSSFRow row2ComtradeAno = sheetComtrade.createRow(2);
			
			HSSFRow row3USDATipoCafe = sheetUSDA.createRow(3);
			HSSFRow row3ComtradeTipoCafe = sheetComtrade.createRow(3);
			
			int tipoCafeCel = 1;
			int jIndex = 1;
			
			for(int i=0; i<anoPeriodo.length; i++) {
				HSSFCell cellAno = row2USDAAno.createCell(jIndex);
				cellAno.setCellValue(anoPeriodo[i]);
				cellAno.setCellStyle(cellStyleAnoPaisHeader);
				
				HSSFCell cellAno2 = row2ComtradeAno.createCell(jIndex);
				cellAno2.setCellValue(anoPeriodo[i]);
				cellAno2.setCellStyle(cellStyleAnoPaisHeader);
				
				for(int j=0; j<tipoCafes.length; j++) {
					HSSFCell celTipoCafe = row3USDATipoCafe.createCell(tipoCafeCel);
					celTipoCafe.setCellValue(tipoCafes[j]);
					celTipoCafe.setCellStyle(cellStyleTipos);
					
					HSSFCell celTipoCafe2 = row3ComtradeTipoCafe.createCell(tipoCafeCel);
					celTipoCafe2.setCellValue(tipoCafes[j]);
					celTipoCafe2.setCellStyle(cellStyleTipos);
					
					tipoCafeCel++;
				}
				if(tipoCafes.length > 1) {
					sheetUSDA.addMergedRegion(new CellRangeAddress(2,2,jIndex,(i+1)*tipoCafes.length));
					sheetComtrade.addMergedRegion(new CellRangeAddress(2,2,jIndex,(i+1)*tipoCafes.length));
				}
				jIndex += tipoCafes.length;
			}
			
			if(tipoCafes.length == 1 && anoPeriodo.length > 1) {
				sheetUSDA.addMergedRegion(new CellRangeAddress(3,3,1,anoPeriodo.length));
				sheetComtrade.addMergedRegion(new CellRangeAddress(3,3,1,anoPeriodo.length));
			}
			
			sheetUSDA.addMergedRegion(new CellRangeAddress(1,3,0,0));
			sheetUSDA.addMergedRegion(new CellRangeAddress(0,0,0,anoPeriodo.length*tipoCafes.length));
			
			if(tipoCafes.length > 1 || anoPeriodo.length > 1) {
				sheetUSDA.addMergedRegion(new CellRangeAddress(1,1,1,anoPeriodo.length*tipoCafes.length));
				sheetComtrade.addMergedRegion(new CellRangeAddress(1,1,1,anoPeriodo.length*tipoCafes.length));
			}
			
			sheetComtrade.addMergedRegion(new CellRangeAddress(1,3,0,0));
			sheetComtrade.addMergedRegion(new CellRangeAddress(0,0,0,anoPeriodo.length*tipoCafes.length));
			
			if(fonte.equalsIgnoreCase("usda")) {
				
				if(tipo.equalsIgnoreCase("produção")) {
					tipo = "production";
				} else if(tipo.equalsIgnoreCase("exportação")) {
					tipo = "export";
				} else if(tipo.equalsIgnoreCase("importação")) {
					tipo = "import";
				}
				
				HSSFCell cellTipo = row1USDATipo.createCell(1);
				cellTipo.setCellValue(tipo.toUpperCase() + " (Quantidade)");
				cellTipo.setCellStyle(cellStyleTipos);
				
				for(int i=0; i<paisAgregadoList.size(); i++) {
						
					HSSFRow row4USDAPais = sheetUSDA.createRow(i+4);
					HSSFCell cellPaises = row4USDAPais.createCell(0);
					cellPaises.setCellValue(paisAgregadoList.get(i).getNome());
					cellPaises.setCellStyle(cellStyleAnoPaisHeader);
						
					int indice = 1;
						
					for(int j=0; j<anoPeriodo.length; j++) {
						for(int k=0; k< tipoCafes.length; k++) {
							USDAData usdaData = USDAData.findUniqueByCommodityPaisAnoTipo(tipoCafes[k].toUpperCase(), paisAgregadoList.get(i).getUsdaNome(),anoPeriodo[j].toString(),tipo);

							HSSFCell cellValores = row4USDAPais.createCell(indice);
							cellValores.setCellStyle(cellStyleDados);
							
							if(usdaData != null && usdaData.getQuantidade() != 0L) {
								cellValores.setCellValue(usdaData.getQuantidade());
							} else {
								cellValores.setCellValue(0);
							}
						
							indice++;
						}
					}
				}
			}
			
			if(fonte.equalsIgnoreCase("comtrade")) {
				
				String tipoInt = "";
				
				if(tipo.equalsIgnoreCase("produção")) {
					tipoInt = "0";
				} else if(tipo.equalsIgnoreCase("exportação")) {
					tipoInt = "2";
				} else if(tipo.equalsIgnoreCase("importação")) {
					tipoInt = "1";
				}
				
				HSSFCell cellTipo = row1USDATipo.createCell(1);
				HSSFCell cellTipo2 = row1ComtradeTipo.createCell(1);
				
				cellTipo.setCellValue(tipo.toUpperCase() + " (Net Weight)");
				cellTipo.setCellStyle(cellStyleTipos);
				
				cellTipo2.setCellValue(tipo.toUpperCase() + " (Trade Value)");
				cellTipo2.setCellStyle(cellStyleTipos);
				
				for(int i=0; i<paisAgregadoList.size(); i++) {
					
					HSSFRow row4USDAPais = sheetUSDA.createRow(i+4);
					HSSFRow row4ComtradePais = sheetComtrade.createRow(i+4);
					
					HSSFCell cellPaises = row4USDAPais.createCell(0);
					HSSFCell cellPaises2 = row4ComtradePais.createCell(0);
					
					cellPaises.setCellValue(paisAgregadoList.get(i).getNome());
					cellPaises.setCellStyle(cellStyleAnoPaisHeader);
					
					cellPaises2.setCellValue(paisAgregadoList.get(i).getNome());
					cellPaises2.setCellStyle(cellStyleAnoPaisHeader);
						
					int indice = 1;
						
					for(int j=0; j<anoPeriodo.length; j++) {
						List<ComtradeDataSumarizacao> comtradeList = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYr(
								tipoInt, paisAgregadoList.get(i).getComtradeId().toString(), "0", anoPeriodo[j].toString());
						
						HSSFCell cellValores = row4USDAPais.createCell(indice);
						HSSFCell cellValores2 = row4ComtradePais.createCell(indice);
						
						cellValores.setCellStyle(cellStyleDados);
						cellValores2.setCellStyle(cellStyleDados);
						
						if(comtradeList != null && comtradeList.size() > 0) {
							ComtradeDataSumarizacao comtrade = comtradeList.get(0);
							cellValores.setCellValue(comtrade.getNetWeight().toString());
							cellValores2.setCellValue(comtrade.getTradeValue().toString());
						} else {
							cellValores.setCellValue("0");
							cellValores2.setCellValue("0");
						}
						
						indice++;
					}
				}
			}
			
			sheetUSDA.createFreezePane(1, 4, 1, 4);
			sheetComtrade.createFreezePane(1, 4, 1, 4);
			
			int numColunasTotal = anoPeriodo.length * tipoCafes.length + 1;
			for(int i=0; i< numColunasTotal; i++) {
				sheetUSDA.autoSizeColumn(i, true);
				sheetComtrade.autoSizeColumn(i, true);
			}
			
			if(fonte.equalsIgnoreCase("usda")) {
				wb.removeSheetAt(1);
			}
			
			FileOutputStream fileOut = new FileOutputStream(excelFileName);

			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
			
			byte[] contents = IOUtils.toByteArray(new FileInputStream(excelFileName));
			return ok(contents);
			
		} catch(Exception e) {
			System.out.println(e);
		}
		return ok();
	}
    
    public static Result imprimirRelatorioAgregado() {
    	
    	Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] tipoArr = asFormUrlEncoded.get("tipo");
		String tipo = tipoArr[0];
		String[] fonteArr = asFormUrlEncoded.get("fonte");
		String fonte = fonteArr[0];
		String[] paisListArr = asFormUrlEncoded.get("paisList");
		String[] paisIdList = paisListArr[0].split(",");
		String[] continenteListArr = asFormUrlEncoded.get("continenteList");
		String[] continenteIdList = continenteListArr[0].split(",");
		String[] blocoEconomicoListArr = asFormUrlEncoded.get("blocoEconomicoList");
		String[] blocoEconomicoIdList = blocoEconomicoListArr[0].split(",");
		String[] tipoCafeArr = asFormUrlEncoded.get("tipoCafe");
		String[] tipoCafes = tipoCafeArr[0].split(",");
		String[] agregadoUSDAArr = asFormUrlEncoded.get("agregadoUSDA");
		String agregadoUSDA = agregadoUSDAArr[0];
		
		String[] anoInicialArr = asFormUrlEncoded.get("anoInicial");
		String anoInicial = anoInicialArr[0];
		
		String[] anoFinalArr = asFormUrlEncoded.get("anoFinal");
		String anoFinal = anoFinalArr[0];
		
		List<Pais> paisAgregadoList = new ArrayList<Pais>();
		
		if(paisIdList != null && paisIdList.length > 0) {
			for(String idPais : paisIdList) {
				if(idPais.length() > 0) { 
					paisAgregadoList.add(Pais.findById(Long.valueOf(idPais)));
				}
			}			
		}
		
		if(continenteIdList != null && continenteIdList.length > 0) {
			
			for(String idContinente : continenteIdList) {
				if(idContinente.length() > 0) {
					paisAgregadoList.addAll(Pais.findByContinente(Long.valueOf(idContinente)));	
				}
			}
		}
		
		if(blocoEconomicoIdList != null && blocoEconomicoIdList.length > 0) {
			for(String idBlocoEconomico : blocoEconomicoIdList) {
				if(idBlocoEconomico.length() > 0) {
					List<RelPaisBlocoEconomico> rpbeList = RelPaisBlocoEconomico.findAllByBlocoEconomico(Long.valueOf(idBlocoEconomico));
					for(RelPaisBlocoEconomico rpbe : rpbeList) {
						Pais p = Pais.findById(rpbe.getPaisId());
						paisAgregadoList.add(p);
					}
				}
			}
		}
		
		try {
			
			String excelFileName = AbicsLogConfig.getString(AbicsLogConfig.USER_REPORT_FOLDER) + getUsuarioLogado().getId() 
					+"/relatorioPaisAnual.xlsx";
			
			String sheetName1 = "";
			String sheetName2 = "Comtrade";
			
			if(fonte.equalsIgnoreCase("usda")) {
				sheetName1 = "USDA - quantidade";//name of sheet
				
			} else if(fonte.equalsIgnoreCase("comtrade")) {
				sheetName1 = "Comtrade - Net Weight";//name of sheet
				sheetName2 = "Comtrade - Trade Value";//name of sheet
			}
			
			HSSFWorkbook wb = new HSSFWorkbook();
	
			HSSFSheet sheetUSDA = wb.createSheet(sheetName1);
			HSSFSheet sheetComtrade = wb.createSheet(sheetName2);
			
			HSSFColor lightPink =  ExcelUtil.setColor(wb, (byte)(Integer.parseInt("255")) , (byte)(Integer.parseInt("227")) ,(byte)(Integer.parseInt("221")), new HSSFColor.PINK());
			CellStyle cellStyleTipos = ExcelUtil.createStyle(wb, IndexedColors.BROWN.getIndex(), lightPink.getIndex() , (short) 11, true, false, true);
			
			CellStyle cellStylePaisHeader = ExcelUtil.createStyle(wb, IndexedColors.WHITE.getIndex(), IndexedColors.BROWN.getIndex(), (short) 13, true, false, true);
			CellStyle cellStyleHeader = ExcelUtil.createStyle(wb, IndexedColors.BROWN.getIndex(), IndexedColors.WHITE.getIndex(), (short) 13, true, false, true);
			CellStyle cellStyleAnoPaisHeader = ExcelUtil.createStyle(wb, IndexedColors.BROWN.getIndex(), IndexedColors.WHITE.getIndex(), (short) 11, true, false, true);
			CellStyle cellStyleDados = ExcelUtil.createStyle(wb, IndexedColors.BLACK.getIndex(), IndexedColors.WHITE.getIndex(), (short) 11, false, false, true);
			
			HSSFRow row0USDA = sheetUSDA.createRow(0);
			HSSFCell cellCabecalho1 = row0USDA.createCell(0);
			cellCabecalho1.setCellValue("Fonte: " + fonte.toUpperCase() + " (Anual) - Período: " + anoInicial + " / " + anoFinal);
			cellCabecalho1.setCellStyle(cellStyleHeader);
			
			HSSFRow row0Comtrade = sheetComtrade.createRow(0);
			HSSFCell cellCabecalho2 = row0Comtrade.createCell(0);
			cellCabecalho2.setCellValue("Fonte: " + fonte.toUpperCase() + " (Anual) - Período: " + anoInicial + " / " + anoFinal);
			cellCabecalho2.setCellStyle(cellStyleHeader);

			HSSFRow row1USDATipo = sheetUSDA.createRow(1);
			HSSFCell cellData = row1USDATipo.createCell(0);
			cellData.setCellValue("PAÍS DESTINO");
			cellData.setCellStyle(cellStylePaisHeader);
			
			HSSFRow row1ComtradeTipo = sheetComtrade.createRow(1);
			HSSFCell cellDataComtrade = row1ComtradeTipo.createCell(0);
			cellDataComtrade.setCellValue("PAÍS DESTINO");
			cellDataComtrade.setCellStyle(cellStylePaisHeader);
			
			Integer anoInicialInteger = Integer.valueOf(anoInicial);
			Integer anoFinalInteger = Integer.valueOf(anoFinal);
			Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
			Integer[] anoPeriodo = new Integer[tamanhoArrayAno];

			for(Integer i = 0; i < tamanhoArrayAno; i++) {
				anoPeriodo[i] = anoInicialInteger + i;
			}
			
			HSSFRow row2USDAAno = sheetUSDA.createRow(2);
			HSSFRow row2ComtradeAno = sheetComtrade.createRow(2);
			
			HSSFRow row3USDATipoCafe = sheetUSDA.createRow(3);
			HSSFRow row3ComtradeTipoCafe = sheetComtrade.createRow(3);
			
			int tipoCafeCel = 1;
			int jIndex = 1;
			
			for(int i=0; i<anoPeriodo.length; i++) {
				HSSFCell cellAno = row2USDAAno.createCell(jIndex);
				cellAno.setCellValue(anoPeriodo[i]);
				cellAno.setCellStyle(cellStyleAnoPaisHeader);
				
				HSSFCell cellAno2 = row2ComtradeAno.createCell(jIndex);
				cellAno2.setCellValue(anoPeriodo[i]);
				cellAno2.setCellStyle(cellStyleAnoPaisHeader);
				
				if(!agregadoUSDA.equalsIgnoreCase("cafe")) {
					for(int j=0; j<tipoCafes.length; j++) {
						HSSFCell celTipoCafe = row3USDATipoCafe.createCell(tipoCafeCel);
						celTipoCafe.setCellValue(tipoCafes[j]);
						celTipoCafe.setCellStyle(cellStyleTipos);
						
						HSSFCell celTipoCafe2 = row3ComtradeTipoCafe.createCell(tipoCafeCel);
						celTipoCafe2.setCellValue(tipoCafes[j]);
						celTipoCafe2.setCellStyle(cellStyleTipos);
						
						tipoCafeCel++;
					}
					
				} else {
					HSSFCell celTipoCafe = row3USDATipoCafe.createCell(tipoCafeCel);
					celTipoCafe.setCellValue("Café agregado");
					celTipoCafe.setCellStyle(cellStyleTipos);
				}
				
				if(tipoCafes.length > 1 && !agregadoUSDA.equalsIgnoreCase("cafe")) {
					sheetUSDA.addMergedRegion(new CellRangeAddress(2,2,jIndex,(i+1)*tipoCafes.length));
					sheetComtrade.addMergedRegion(new CellRangeAddress(2,2,jIndex,(i+1)*tipoCafes.length));
					jIndex += tipoCafes.length;
					
				} else {
					jIndex++;
				}
			}
			
			if(tipoCafes.length == 1 && anoPeriodo.length > 1) {
				sheetUSDA.addMergedRegion(new CellRangeAddress(3,3,1,anoPeriodo.length));
				sheetComtrade.addMergedRegion(new CellRangeAddress(3,3,1,anoPeriodo.length));
			}
			
			sheetUSDA.addMergedRegion(new CellRangeAddress(1,3,0,0));
			
			if(!agregadoUSDA.equalsIgnoreCase("cafe")) {
				sheetUSDA.addMergedRegion(new CellRangeAddress(0,0,0,anoPeriodo.length*tipoCafes.length));
			} else {
				sheetUSDA.addMergedRegion(new CellRangeAddress(0,0,0,anoPeriodo.length));
			}
			
			if(tipoCafes.length > 1 || anoPeriodo.length > 1) {
				if(!agregadoUSDA.equalsIgnoreCase("cafe")) {
					sheetUSDA.addMergedRegion(new CellRangeAddress(1,1,1,anoPeriodo.length*tipoCafes.length));
					sheetComtrade.addMergedRegion(new CellRangeAddress(1,1,1,anoPeriodo.length*tipoCafes.length));
				} else {
					if(anoPeriodo.length > 1) {
						sheetUSDA.addMergedRegion(new CellRangeAddress(3,3,1,anoPeriodo.length));
						sheetUSDA.addMergedRegion(new CellRangeAddress(1,1,1,anoPeriodo.length));
					}
				}
			}
			
			sheetComtrade.addMergedRegion(new CellRangeAddress(1,3,0,0));
			sheetComtrade.addMergedRegion(new CellRangeAddress(0,0,0,anoPeriodo.length*tipoCafes.length));
			
			if(fonte.equalsIgnoreCase("usda")) {
				
				if(tipo.equalsIgnoreCase("produção")) {
					tipo = "production";
				} else if(tipo.equalsIgnoreCase("exportação")) {
					tipo = "export";
				} else if(tipo.equalsIgnoreCase("importação")) {
					tipo = "import";
				}
				
				HSSFCell cellTipo = row1USDATipo.createCell(1);
				cellTipo.setCellValue(tipo.toUpperCase() + " (Quantidade)");
				cellTipo.setCellStyle(cellStyleTipos);
				
				if(agregadoUSDA.equals("pais")) {
					
					HSSFRow row4USDAPais = sheetUSDA.createRow(4);
					HSSFCell cellPaises = row4USDAPais.createCell(0);
					cellPaises.setCellValue("Países Agregados");
					cellPaises.setCellStyle(cellStyleAnoPaisHeader);
					
					List<Long> quantidadeList = new ArrayList<Long>();
					
					for(int i=0; i<tipoCafes.length; i++) {
						for(int j=0; j<anoPeriodo.length; j++) {
							USDAData usdaData = null;
							Long quantidade = 0L;
							
							for(int k=0; k<paisAgregadoList.size(); k++) {
								usdaData = USDAData.findUniqueByCommodityPaisAnoTipo(tipoCafes[i], paisAgregadoList.get(k).getUsdaNome(),anoPeriodo[j].toString(),tipo);

								if(usdaData != null) {
									quantidade += usdaData.getQuantidade();
								}
							}
							quantidadeList.add(quantidade);
						}
					}
					
					int indice = 1;
					
					for(int i=0; i<anoPeriodo.length; i++) {
						for(int j=0; j< tipoCafes.length; j++) {
							HSSFCell cellValores = row4USDAPais.createCell(indice);
							cellValores.setCellStyle(cellStyleDados);
							cellValores.setCellValue(quantidadeList.get(anoPeriodo.length*j + i));
							indice++;
						}
					}
					
				} else if(agregadoUSDA.equals("cafe")) {
					for(int i=0; i<paisAgregadoList.size(); i++) {
						
						HSSFRow row4USDAPais = sheetUSDA.createRow(i+4);
						HSSFCell cellPaises = row4USDAPais.createCell(0);
						cellPaises.setCellValue(paisAgregadoList.get(i).getNome());
						cellPaises.setCellStyle(cellStyleAnoPaisHeader);
						
						int indice = 1;
						
						for(int j=0; j<anoPeriodo.length; j++) {
							
							Long quantidade = 0L;
							
							for(int k=0; k< tipoCafes.length; k++) {
								USDAData usdaData = USDAData.findUniqueByCommodityPaisAnoTipo(tipoCafes[k].toUpperCase(), paisAgregadoList.get(i).getUsdaNome(),anoPeriodo[j].toString(),tipo);

								if(usdaData != null && usdaData.getQuantidade() != 0L) {
									quantidade += usdaData.getQuantidade();
								}
							}
							
							HSSFCell cellValores = row4USDAPais.createCell(indice);
							cellValores.setCellStyle(cellStyleDados);
							cellValores.setCellValue(quantidade);
							
							indice++;
						}
					}
				}
			}
			
			if(fonte.equalsIgnoreCase("comtrade")) {
				
				String tipoInt = "";
				
				if(tipo.equalsIgnoreCase("produção")) {
					tipoInt = "0";
				} else if(tipo.equalsIgnoreCase("exportação")) {
					tipoInt = "2";
				} else if(tipo.equalsIgnoreCase("importação")) {
					tipoInt = "1";
				}
				
				HSSFCell cellTipo = row1USDATipo.createCell(1);
				HSSFCell cellTipo2 = row1ComtradeTipo.createCell(1);
				
				cellTipo.setCellValue(tipo.toUpperCase() + " (Net Weight)");
				cellTipo.setCellStyle(cellStyleTipos);
				
				cellTipo2.setCellValue(tipo.toUpperCase() + " (Trade Value)");
				cellTipo2.setCellStyle(cellStyleTipos);
				
				HSSFRow row4USDAPais = sheetUSDA.createRow(4);
				HSSFCell cellPaises = row4USDAPais.createCell(0);
				cellPaises.setCellValue("Países Agregados");
				cellPaises.setCellStyle(cellStyleAnoPaisHeader);
				
				HSSFRow row4ComTradePais = sheetComtrade.createRow(4);
				HSSFCell cellPaises2 = row4ComTradePais.createCell(0);
				cellPaises2.setCellValue("Países Agregados");
				cellPaises2.setCellStyle(cellStyleAnoPaisHeader);
				
				for(int j=0; j<anoPeriodo.length; j++) {
					ComtradeDataSumarizacao comtrade = null;
					Long tradeValue = 0L;
					Long netWeight = 0L;
					
					for(int k=0; k<paisAgregadoList.size(); k++) {
						List<ComtradeDataSumarizacao> comtradeList = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYr(
								tipoInt, paisAgregadoList.get(k).getComtradeId().toString(), "0", anoPeriodo[j].toString());
						
						if(comtradeList != null && comtradeList.size() > 0) {
							comtrade = comtradeList.get(0);
							
							if(comtrade.getYr().equals(comtrade.getPeriod())) {
								tradeValue += comtrade.getTradeValue();
								netWeight += comtrade.getNetWeight();
							}
						}
					}
					
					HSSFCell cellValores = row4USDAPais.createCell(j+1);
					cellValores.setCellStyle(cellStyleDados);
					cellValores.setCellValue(netWeight);
					
					HSSFCell cellValores2 = row4ComTradePais.createCell(j+1);
					cellValores2.setCellStyle(cellStyleDados);
					cellValores2.setCellValue(tradeValue);
				}
			}	
			
			sheetUSDA.createFreezePane(1, 4, 1, 4);
			sheetComtrade.createFreezePane(1, 4, 1, 4);
			
			int numColunasTotal = anoPeriodo.length * tipoCafes.length + 1;
			
			for(int i=0; i< numColunasTotal; i++) {
				sheetUSDA.autoSizeColumn(i, true);
				sheetComtrade.autoSizeColumn(i, true);
			}
			
			if(fonte.equalsIgnoreCase("usda")) {
				wb.removeSheetAt(1);
			}
			
			FileOutputStream fileOut = new FileOutputStream(excelFileName);

			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
			
			byte[] contents = IOUtils.toByteArray(new FileInputStream(excelFileName));
			return ok(contents);
			
		} catch(Exception e) {
			System.out.println(e);
		}
		return ok();
	}
    
	
}