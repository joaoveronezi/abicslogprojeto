package controllers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.AbicsExportacaoPais;
import models.BlocoEconomico;
import models.ComtradeDataSumarizacao;
import models.Continente;
import models.Pais;
import models.RelPaisBlocoEconomico;

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
import entitys.response.AbicsDataRelatorioAbicsResponse;
import entitys.response.AbicsDataRelatorioComtradeMesResponse;
import entitys.response.GraficoCategoriesColumnsResponse;

public class AbicsDataRelatorioBrasilController extends AbstractController {
	
	private static String mesList[] = new String[]{"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
	private  static Integer mesesTotais[] = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12};

	public static Result findAllPais() {
		
		List<Pais> list = Pais.findAll();
		List<Pais> listRetorno = new ArrayList<Pais>();
		
		for(Pais p : list) {
			
			if(p.getId() != 68L) {
				listRetorno.add(p);
			}
		}
		
		return ok(Json.toJson(listRetorno));
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
	
	public static Result consultarGraficoAbics(String tipoDadoAbics) {
		
    	Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] tipoArr = asFormUrlEncoded.get("tipo");
		String tipo = tipoArr[0];
		String[] paisListArr = asFormUrlEncoded.get("paisList");
		String[] paisIdList = paisListArr[0].split(",");
		String[] continenteListArr = asFormUrlEncoded.get("continenteList");
		String[] continenteIdList = continenteListArr[0].split(",");
		String[] blocoEconomicoListArr = asFormUrlEncoded.get("blocoEconomicoList");
		String[] blocoEconomicoIdList = blocoEconomicoListArr[0].split(",");
		String[] tipoCafeArr = asFormUrlEncoded.get("tipoCafe");
		String[] tipoCafes = tipoCafeArr[0].split(",");
		String[] tipoGraficoArr = asFormUrlEncoded.get("tipoGrafico");
		String tipoGrafico = tipoGraficoArr[0];
		
		String[] anoInicialArr = asFormUrlEncoded.get("anoInicial");
		String anoInicial = anoInicialArr[0];
		
		String[] anoFinalArr = asFormUrlEncoded.get("anoFinal");
		String anoFinal = anoFinalArr[0];
		
		String[] mesInicialArr = null;
		String mesInicial = "";
		mesInicialArr = asFormUrlEncoded.get("mesInicial");
		mesInicial = mesInicialArr[0];
		
		String[] mesFinalArr = null;
		String mesFinal = "";
		mesFinalArr = asFormUrlEncoded.get("mesFinal");
		mesFinal = mesFinalArr[0];
		
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
		
		Integer mesInicialInteger = Integer.valueOf(mesInicial);
		Integer mesFinalInteger = Integer.valueOf(mesFinal);
		Integer anoInicialInteger = Integer.valueOf(anoInicial);
		Integer anoFinalInteger = Integer.valueOf(anoFinal);
		Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
		Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
		Integer tamanhoArrayMes = 0;
		
		if(tipo.equalsIgnoreCase("producao")) {
			tipo = "production";
		} else if(tipo.equalsIgnoreCase("exportacao")) {
			tipo = "export";
		} else if(tipo.equalsIgnoreCase("importacao")) {
			tipo = "import";
		}
		
		if(tamanhoArrayAno == 1){
			tamanhoArrayMes = (mesFinalInteger - mesInicialInteger) + 1;
			anoPeriodo[0] = anoInicialInteger;
		} else {
			for(Integer i = 0; i < tamanhoArrayAno; i++) {
				anoPeriodo[i] = anoInicialInteger + i;
			}
		}
		
		if(tipoGrafico.equalsIgnoreCase("sequencial")) {
			if(anoPeriodo.length == 1) {
				Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
				
				for(int i=0; i<mesPeriodo.length; i++) {
					mesPeriodo[i] = mesInicialInteger + i;
					
	    			categories.add(mesPeriodo[i].toString() + "/" + anoPeriodo[0].toString());
				}
				for(String tipoCafe : tipoCafes) {
					for(Pais p : paisAgregadoList) {
						
						List<String> columns = new ArrayList<String>();
						columns.add(p.getNome() + " - "+ tipoCafe);
						
						for(Integer mes : mesPeriodo) {
							String valor = "0";
							AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(anoPeriodo[0], mes, p.getId());
							
							if(abicsExportacao != null) {
								
								String peso = abicsExportacao.getPeso() != null ? abicsExportacao.getPeso().toString() : "0";
								String receita = abicsExportacao.getReceita() != null ? abicsExportacao.getReceita().toString() : "0";
								String saca60kg = abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg().toString() : "0";
								
								if(tipoDadoAbics.equalsIgnoreCase("peso")) {
									valor = String.valueOf(peso);								
								} else if(tipoDadoAbics.equalsIgnoreCase("receita")) {
									valor = String.valueOf(receita);
								} else if(tipoDadoAbics.equalsIgnoreCase("saca60kg")) {
									valor = String.valueOf(saca60kg);
								}
							}
							columns.add(valor);
							graficoItemValorEntity.addColumns(columns);
						}
					}
				}
				graficoItemValorEntity.setCategories(categories);
			} else {
				for(Integer ano : anoPeriodo) {
					if(ano.equals(anoInicialInteger)) {
						tamanhoArrayMes = 12 - mesInicialInteger + 1;
						Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
						
						for(int i=0; i<tamanhoArrayMes; i++) {
							mesPeriodo[i] = mesInicialInteger + i;
							
							categories.add(mesPeriodo[i].toString() + "/" + ano.toString());
						}
					} else if(ano.equals(anoFinalInteger)) {
						tamanhoArrayMes = mesFinalInteger;
						Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
						for(int i=tamanhoArrayMes-1; i>=0; i--) {
							mesPeriodo[i] = mesFinalInteger - i;
							
							categories.add(mesPeriodo[i].toString() + "/" + ano.toString());
						}
					} else {
						tamanhoArrayMes = 12;
						Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
						for(int i=0; i<tamanhoArrayMes; i++) {
							mesPeriodo[i] = i+1;
							
							categories.add(mesPeriodo[i].toString() + "/" + ano.toString());
						}
					}
				}
				
				for(String tipoCafe : tipoCafes) {
					for(Pais p : paisAgregadoList) {
						List<String> columns = new ArrayList<String>();
						columns.add(p.getNome() + " - "+ tipoCafe);
						
						for(Integer ano : anoPeriodo) {
							if(ano.equals(anoInicialInteger)) {
								tamanhoArrayMes = 12 - mesInicialInteger + 1;
								Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
								
								for(int i=0; i<tamanhoArrayMes; i++) {
									mesPeriodo[i] = mesInicialInteger + i;							
									String valor = "0";
									AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(ano, mesPeriodo[i], p.getId());
									
									if(abicsExportacao != null) {
										
										String peso = abicsExportacao.getPeso() != null ? abicsExportacao.getPeso().toString() : "0";
										String receita = abicsExportacao.getReceita() != null ? abicsExportacao.getReceita().toString() : "0";
										String saca60kg = abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg().toString() : "0";
										
										if(tipoDadoAbics.equalsIgnoreCase("peso")) {
											valor = String.valueOf(peso);								
										} else if(tipoDadoAbics.equalsIgnoreCase("receita")) {
											valor = String.valueOf(receita);
										} else if(tipoDadoAbics.equalsIgnoreCase("saca60kg")) {
											valor = String.valueOf(saca60kg);
										}
									}
									columns.add(valor);
									graficoItemValorEntity.addColumns(columns);
								}
							} else if(ano.equals(anoFinalInteger)) {
								tamanhoArrayMes = mesFinalInteger;
								Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
								
								for(int i=tamanhoArrayMes-1; i>=0; i--) {
									mesPeriodo[i] = mesFinalInteger - i;
									String valor = "0";
									AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(ano, mesPeriodo[i], p.getId());
									
									if(abicsExportacao != null) {
										
										String peso = abicsExportacao.getPeso() != null ? abicsExportacao.getPeso().toString() : "0";
										String receita = abicsExportacao.getReceita() != null ? abicsExportacao.getReceita().toString() : "0";
										String saca60kg = abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg().toString() : "0";
										
										if(tipoDadoAbics.equalsIgnoreCase("peso")) {
											valor = String.valueOf(peso);								
										} else if(tipoDadoAbics.equalsIgnoreCase("receita")) {
											valor = String.valueOf(receita);
										} else if(tipoDadoAbics.equalsIgnoreCase("saca60kg")) {
											valor = String.valueOf(saca60kg);
										}
									}
									columns.add(valor);
									graficoItemValorEntity.addColumns(columns);
								}
							} else {
								tamanhoArrayMes = 12;
								Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
								
								for(int i=0; i<tamanhoArrayMes; i++) {
									mesPeriodo[i] = i+1;
									String valor = "0";
									AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(ano, mesPeriodo[i], p.getId());
									
									if(abicsExportacao != null) {
										
										String peso = abicsExportacao.getPeso() != null ? abicsExportacao.getPeso().toString() : "0";
										String receita = abicsExportacao.getReceita() != null ? abicsExportacao.getReceita().toString() : "0";
										String saca60kg = abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg().toString() : "0";
										
										if(tipoDadoAbics.equalsIgnoreCase("peso")) {
											valor = String.valueOf(peso);								
										} else if(tipoDadoAbics.equalsIgnoreCase("receita")) {
											valor = String.valueOf(receita);
										} else if(tipoDadoAbics.equalsIgnoreCase("saca60kg")) {
											valor = String.valueOf(saca60kg);
										}
									}
									columns.add(valor);
									graficoItemValorEntity.addColumns(columns);
								}
							}
						}
					}
				}
				graficoItemValorEntity.setCategories(categories);
			}
			graficoItemValorEntity.setCountPaises(paisAgregadoList.size());
			
		} else if(tipoGrafico.equalsIgnoreCase("anual")) {
			tamanhoArrayMes = mesFinalInteger - mesInicialInteger + 1;
			Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
			
			for(int i=0; i<mesPeriodo.length; i++) {
				mesPeriodo[i] = mesInicialInteger + i;
				
    			categories.add(mesPeriodo[i].toString());
			}
			
			for(String tipoCafe : tipoCafes) {
				for(Pais p : paisAgregadoList) {
					for(Integer ano : anoPeriodo) {
						List<String> columns = new ArrayList<String>();
						columns.add(p.getNome() + " (" + ano.toString() + ")");
						
						for(Integer mes : mesPeriodo) {
							String valor = "0";
							AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(ano, mes, p.getId());
							
							if(abicsExportacao != null) {
								
								String peso = abicsExportacao.getPeso() != null ? abicsExportacao.getPeso().toString() : "0";
								String receita = abicsExportacao.getReceita() != null ? abicsExportacao.getReceita().toString() : "0";
								String saca60kg = abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg().toString() : "0";
								
								if(tipoDadoAbics.equalsIgnoreCase("peso")) {
									valor = String.valueOf(peso);								
								} else if(tipoDadoAbics.equalsIgnoreCase("receita")) {
									valor = String.valueOf(receita);
								} else if(tipoDadoAbics.equalsIgnoreCase("saca60kg")) {
									valor = String.valueOf(saca60kg);
								}
							}
							columns.add(valor);
							graficoItemValorEntity.addColumns(columns);
						}
					}
				}
			}
			graficoItemValorEntity.setCategories(categories);
			graficoItemValorEntity.setCountPaises(1);
		}
		return ok(Json.toJson(graficoItemValorEntity));
    }
	
	public static Result consultarGraficoAbicsAgregado(String tipoDadoAbics) {
		
    	Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] tipoArr = asFormUrlEncoded.get("tipo");
		String tipo = tipoArr[0];
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
		
		String[] mesInicialArr = null;
		String mesInicial = "";
		mesInicialArr = asFormUrlEncoded.get("mesInicial");
		mesInicial = mesInicialArr[0];
		
		String[] mesFinalArr = null;
		String mesFinal = "";
		mesFinalArr = asFormUrlEncoded.get("mesFinal");
		mesFinal = mesFinalArr[0];
		
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
		
		Integer mesInicialInteger = Integer.valueOf(mesInicial);
		Integer mesFinalInteger = Integer.valueOf(mesFinal);
		Integer anoInicialInteger = Integer.valueOf(anoInicial);
		Integer anoFinalInteger = Integer.valueOf(anoFinal);
		Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
		Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
		Integer tamanhoArrayMes = 0;
		
		if(tipo.equalsIgnoreCase("producao")) {
			tipo = "production";
		} else if(tipo.equalsIgnoreCase("exportacao")) {
			tipo = "export";
		} else if(tipo.equalsIgnoreCase("importacao")) {
			tipo = "import";
		}
		
		if(tamanhoArrayAno == 1){
			tamanhoArrayMes = (mesFinalInteger - mesInicialInteger) + 1;
			anoPeriodo[0] = anoInicialInteger;
		} else {
			for(Integer i = 0; i < tamanhoArrayAno; i++) {
				anoPeriodo[i] = anoInicialInteger + i;
			}
		}
		
		if(anoPeriodo.length == 1) {
			Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
			
			for(int i=0; i<mesPeriodo.length; i++) {
				mesPeriodo[i] = mesInicialInteger + i;
				
    			categories.add(mesPeriodo[i].toString() + "/" + anoPeriodo[0].toString());
			}
			for(String tipoCafe : tipoCafes) {
				List<String> columns = new ArrayList<String>();
				columns.add("Paíes Agregados" + " - "+ tipoCafe);
				
				for(Integer mes : mesPeriodo) {
					Long valor = 0L;
					
					for(Pais p : paisAgregadoList) {
						AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(anoPeriodo[0], mes, p.getId());
						
						if(abicsExportacao != null) {
							
							Long peso = abicsExportacao != null ? abicsExportacao.getPeso() : 0L;
							Long receita = abicsExportacao.getReceita() != null ? abicsExportacao.getReceita() : 0L;
							Long saca60kg = abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg() : 0L;
							
							if(tipoDadoAbics.equalsIgnoreCase("peso")) {
								valor += peso;								
							} else if(tipoDadoAbics.equalsIgnoreCase("receita")) {
								valor += receita;
							} else if(tipoDadoAbics.equalsIgnoreCase("saca60kg")) {
								valor += saca60kg;
							}
						}
					}
					columns.add(valor.toString());
					graficoItemValorEntity.addColumns(columns);
				}
			}
			graficoItemValorEntity.setCategories(categories);
		} else {
			for(Integer ano : anoPeriodo) {
				if(ano.equals(anoInicialInteger)) {
					tamanhoArrayMes = 12 - mesInicialInteger + 1;
					Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
					
					for(int i=0; i<tamanhoArrayMes; i++) {
						mesPeriodo[i] = mesInicialInteger + i;
						
						categories.add(mesPeriodo[i].toString() + "/" + ano.toString());
					}
				} else if(ano.equals(anoFinalInteger)) {
					tamanhoArrayMes = mesFinalInteger;
					Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
					for(int i=tamanhoArrayMes-1; i>=0; i--) {
						mesPeriodo[i] = mesFinalInteger - i;
						
						categories.add(mesPeriodo[i].toString() + "/" + ano.toString());
					}
				} else {
					tamanhoArrayMes = 12;
					Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
					for(int i=0; i<tamanhoArrayMes; i++) {
						mesPeriodo[i] = i+1;
						
						categories.add(mesPeriodo[i].toString() + "/" + ano.toString());
					}
				}
			}
			
			for(String tipoCafe : tipoCafes) {
				List<String> columns = new ArrayList<String>();
				columns.add("Países agregados " + " - " + tipoCafe);
					
				for(Integer ano : anoPeriodo) {
					if(ano.equals(anoInicialInteger)) {
						tamanhoArrayMes = 12 - mesInicialInteger + 1;
						Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
						for(int i=0; i<tamanhoArrayMes; i++) {
							Long valor = 0L;
							
							for(Pais p : paisAgregadoList) {
								mesPeriodo[i] = mesInicialInteger + i;							
								AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(ano, mesPeriodo[i], p.getId());
								
								if(abicsExportacao != null) {
									Long peso = abicsExportacao.getPeso() != null ? abicsExportacao.getPeso() : 0L;
									Long receita = abicsExportacao.getReceita() != null ? abicsExportacao.getReceita() : 0L;
									Long saca60kg = abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg() : 0L;
									
									if(tipoDadoAbics.equalsIgnoreCase("peso")) {
										valor += peso;								
									} else if(tipoDadoAbics.equalsIgnoreCase("receita")) {
										valor += receita;
									} else if(tipoDadoAbics.equalsIgnoreCase("saca60kg")) {
										valor += saca60kg;
									}
								}
							}
							columns.add(valor.toString());
							graficoItemValorEntity.addColumns(columns);
						}
					} else if(ano.equals(anoFinalInteger)) {
						tamanhoArrayMes = mesFinalInteger;
						Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
							
						for(int i=tamanhoArrayMes-1; i>=0; i--) {
							Long valor = 0L;
							for(Pais p : paisAgregadoList) {
								mesPeriodo[i] = mesFinalInteger - i;
								AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(ano, mesPeriodo[i], p.getId());
									
								if(abicsExportacao != null) {
									
									Long peso = abicsExportacao.getPeso() != null ? abicsExportacao.getPeso() : 0L;
									Long receita = abicsExportacao.getReceita() != null ? abicsExportacao.getReceita() : 0L;
									Long saca60kg = abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg() : 0L;
									
									if(tipoDadoAbics.equalsIgnoreCase("peso")) {
										valor += peso;								
									} else if(tipoDadoAbics.equalsIgnoreCase("receita")) {
										valor += receita;
									} else if(tipoDadoAbics.equalsIgnoreCase("saca60kg")) {
										valor += saca60kg;
									}
								}
							}
							columns.add(valor.toString());
							graficoItemValorEntity.addColumns(columns);
						}
					} else {
						tamanhoArrayMes = 12;
						Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
						
						for(int i=0; i<tamanhoArrayMes; i++) {
							Long valor = 0L;
							for(Pais p : paisAgregadoList) {
								mesPeriodo[i] = i+1;
								AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(ano, mesPeriodo[i], p.getId());
								
								if(abicsExportacao != null) {
									
									Long peso = abicsExportacao.getPeso() != null ? abicsExportacao.getPeso() : 0L;
									Long receita = abicsExportacao.getReceita() != null ? abicsExportacao.getReceita() : 0L;
									Long saca60kg = abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg() : 0L;
									
									if(tipoDadoAbics.equalsIgnoreCase("peso")) {
										valor += peso;								
									} else if(tipoDadoAbics.equalsIgnoreCase("receita")) {
										valor += receita;
									} else if(tipoDadoAbics.equalsIgnoreCase("saca60kg")) {
										valor += saca60kg;
									}
								}
							}
							columns.add(valor.toString());
							graficoItemValorEntity.addColumns(columns);
						}
					}
				}
			}
			graficoItemValorEntity.setCategories(categories);
		}
		
		graficoItemValorEntity.setCountPaises(paisAgregadoList.size());
		return ok(Json.toJson(graficoItemValorEntity));
    }
	
	public static Result consultarGraficoComtrade(String tipoDadoComtrade) {

    	Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] tipoArr = asFormUrlEncoded.get("tipo");
		String tipo = tipoArr[0];
		String[] paisListArr = asFormUrlEncoded.get("paisList");
		String[] paisIdList = paisListArr[0].split(",");
		String[] continenteListArr = asFormUrlEncoded.get("continenteList");
		String[] continenteIdList = continenteListArr[0].split(",");
		String[] blocoEconomicoListArr = asFormUrlEncoded.get("blocoEconomicoList");
		String[] blocoEconomicoIdList = blocoEconomicoListArr[0].split(",");
		String[] tipoCafeArr = asFormUrlEncoded.get("tipoCafe");
		String[] tipoCafes = tipoCafeArr[0].split(",");
		String[] tipoGraficoArr = asFormUrlEncoded.get("tipoGrafico");
		String tipoGrafico = tipoGraficoArr[0];
		
		String[] anoInicialArr = asFormUrlEncoded.get("anoInicial");
		String anoInicial = anoInicialArr[0];
		
		String[] anoFinalArr = asFormUrlEncoded.get("anoFinal");
		String anoFinal = anoFinalArr[0];
		
		String[] mesInicialArr = null;//asFormUrlEncoded.get("mesInicial");
		String mesInicial = "";
		mesInicialArr = asFormUrlEncoded.get("mesInicial");
		mesInicial = mesInicialArr[0];
		
		String[] mesFinalArr = null;
		String mesFinal = "";
		mesFinalArr = asFormUrlEncoded.get("mesFinal");
		mesFinal = mesFinalArr[0];
		
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
		
		Integer mesInicialInteger = Integer.valueOf(mesInicial);
		Integer mesFinalInteger = Integer.valueOf(mesFinal);
		Integer anoInicialInteger = Integer.valueOf(anoInicial);
		Integer anoFinalInteger = Integer.valueOf(anoFinal);
		Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
		Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
		Integer tamanhoArrayMes = 0;
		
		if(tipo.equalsIgnoreCase("producao")) {
			tipo = "0";
		} else if(tipo.equalsIgnoreCase("exportacao")) {
			tipo = "2";
		} else if(tipo.equalsIgnoreCase("importacao")) {
			tipo = "1";
		}
		
		if(tamanhoArrayAno == 1){
			tamanhoArrayMes = (mesFinalInteger - mesInicialInteger) + 1;
			anoPeriodo[0] = anoInicialInteger;
		} else {
			for(Integer i = 0; i < tamanhoArrayAno; i++) {
				anoPeriodo[i] = anoInicialInteger + i;
			}
		}

		if(tipoGrafico.equalsIgnoreCase("sequencial")) {
			if(anoPeriodo.length == 1) {
				Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
				
				for(int i=0; i<mesPeriodo.length; i++) {
					mesPeriodo[i] = mesInicialInteger + i;
					
	    			categories.add(mesPeriodo[i].toString() + "/" + anoPeriodo[0].toString());
				}
				for(String tipoCafe : tipoCafes) {
					for(Pais p : paisAgregadoList) {
						
						List<String> columns = new ArrayList<String>();
						columns.add(p.getNome() + " - "+ tipoCafe);
						
						for(Integer mes : mesPeriodo) {
							String valor = "0";
							
							ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
									tipo.toString(), "76", p.getComtradeId().toString(), anoPeriodo[0].toString(), mes.toString());
							
							if(comtrade != null) {
								
								if(tipoDadoComtrade.equalsIgnoreCase("tradeValue")) {
									valor = comtrade.getTradeValue().toString();
								} else if(tipoDadoComtrade.equalsIgnoreCase("NetWeight")) {
									valor = comtrade.getNetWeight().toString();
								}
								
							}
							columns.add(valor);
							graficoItemValorEntity.addColumns(columns);
						}
					}
				}
				graficoItemValorEntity.setCategories(categories);
				
			} else {
				for(Integer ano : anoPeriodo) {
					if(ano.equals(anoInicialInteger)) {
						tamanhoArrayMes = 12 - mesInicialInteger + 1;
						Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
						
						for(int i=0; i<tamanhoArrayMes; i++) {
							mesPeriodo[i] = mesInicialInteger + i;
							
							categories.add(mesPeriodo[i].toString() + "/" + ano.toString());
						}
					} else if(ano.equals(anoFinalInteger)) {
						tamanhoArrayMes = mesFinalInteger;
						Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
						for(int i=tamanhoArrayMes-1; i>=0; i--) {
							mesPeriodo[i] = mesFinalInteger - i;
							
							categories.add(mesPeriodo[i].toString() + "/" + ano.toString());
						}
					} else {
						tamanhoArrayMes = 12;
						Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
						for(int i=0; i<tamanhoArrayMes; i++) {
							mesPeriodo[i] = i+1;
							
							categories.add(mesPeriodo[i].toString() + "/" + ano.toString());
						}
					}
				}
				
				for(String tipoCafe : tipoCafes) {
					for(Pais p : paisAgregadoList) {
						List<String> columns = new ArrayList<String>();
						columns.add(p.getNome() + " - "+ tipoCafe);
						
						for(Integer ano : anoPeriodo) {
							if(ano.equals(anoInicialInteger)) {
								tamanhoArrayMes = 12 - mesInicialInteger + 1;
								Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
								
								for(int i=0; i<tamanhoArrayMes; i++) {
									mesPeriodo[i] = mesInicialInteger + i;							
									String valor = "0";
									
									ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
											tipo.toString(), "76", p.getComtradeId().toString(), ano.toString(), mesPeriodo[i].toString());
									
									if(comtrade != null) {
										
										if(tipoDadoComtrade.equalsIgnoreCase("tradeValue")) {
											valor = comtrade.getTradeValue().toString();
										} else if(tipoDadoComtrade.equalsIgnoreCase("NetWeight")) {
											valor = comtrade.getNetWeight().toString();
										}
										
									}
									columns.add(valor);
									graficoItemValorEntity.addColumns(columns);
								}
							} else if(ano.equals(anoFinalInteger)) {
								tamanhoArrayMes = mesFinalInteger;
								Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
								
								for(int i=tamanhoArrayMes-1; i>=0; i--) {
									mesPeriodo[i] = mesFinalInteger - i;
									String valor = "0";
										
									ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
											tipo.toString(), "76", p.getComtradeId().toString(), ano.toString(), mesPeriodo[i].toString());
									
									if(comtrade != null) {
										
										if(tipoDadoComtrade.equalsIgnoreCase("tradeValue")) {
											valor = comtrade.getTradeValue().toString();
										} else if(tipoDadoComtrade.equalsIgnoreCase("NetWeight")) {
											valor = comtrade.getNetWeight().toString();
										}
										
									}
									columns.add(valor);
									graficoItemValorEntity.addColumns(columns);
								}
							} else {
								tamanhoArrayMes = 12;
								Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
								
								for(int i=0; i<tamanhoArrayMes; i++) {
									mesPeriodo[i] = i+1;
									String valor = "0";
										
									ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
											tipo.toString(), "76", p.getComtradeId().toString(), ano.toString(), mesPeriodo[i].toString());
									
									if(comtrade != null) {
										
										if(tipoDadoComtrade.equalsIgnoreCase("tradeValue")) {
											valor = comtrade.getTradeValue().toString();
										} else if(tipoDadoComtrade.equalsIgnoreCase("NetWeight")) {
											valor = comtrade.getNetWeight().toString();
										}
										
									}
									columns.add(valor);
									graficoItemValorEntity.addColumns(columns);
								}
							}
						}
					}
				}
				graficoItemValorEntity.setCategories(categories);
			}
			graficoItemValorEntity.setCountPaises(paisAgregadoList.size());
			
		} else if(tipoGrafico.equalsIgnoreCase("anual")) {
			tamanhoArrayMes = mesFinalInteger - mesInicialInteger + 1;
			Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
			
			for(int i=0; i<mesPeriodo.length; i++) {
				mesPeriodo[i] = mesInicialInteger + i;
				
    			categories.add(mesPeriodo[i].toString());
			}
			
			for(String tipoCafe : tipoCafes) {
				for(Pais p : paisAgregadoList) {
					for(Integer ano : anoPeriodo) {
						List<String> columns = new ArrayList<String>();
						columns.add(p.getNome() + " (" + ano.toString() + ")");
						
						for(Integer mes : mesPeriodo) {
							String valor = "0";
						
							ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
									tipo.toString(), "76", p.getComtradeId().toString(), ano.toString(), mes.toString());
							
							if(comtrade != null) {
								if(tipoDadoComtrade.equalsIgnoreCase("tradeValue")) {
									valor = comtrade.getTradeValue().toString();
								} else if(tipoDadoComtrade.equalsIgnoreCase("NetWeight")) {
									valor = comtrade.getNetWeight().toString();
								}
							}
							columns.add(valor);
							graficoItemValorEntity.addColumns(columns);
						}
					}
				}
			}
			graficoItemValorEntity.setCategories(categories);
			graficoItemValorEntity.setCountPaises(1);
		}
		
		return ok(Json.toJson(graficoItemValorEntity));
    }
	
	public static Result consultarGraficoComtradeAgregado(String tipoDadoComtrade) {

    	Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] tipoArr = asFormUrlEncoded.get("tipo");
		String tipo = tipoArr[0];
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
		
		String[] mesInicialArr = null;//asFormUrlEncoded.get("mesInicial");
		String mesInicial = "";
		mesInicialArr = asFormUrlEncoded.get("mesInicial");
		mesInicial = mesInicialArr[0];

		String[] mesFinalArr = null;
		String mesFinal = "";
		mesFinalArr = asFormUrlEncoded.get("mesFinal");
		mesFinal = mesFinalArr[0];
		
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
		
		Integer mesInicialInteger = Integer.valueOf(mesInicial);
		Integer mesFinalInteger = Integer.valueOf(mesFinal);
		Integer anoInicialInteger = Integer.valueOf(anoInicial);
		Integer anoFinalInteger = Integer.valueOf(anoFinal);
		Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
		Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
		Integer tamanhoArrayMes = 0;
		
		if(tipo.equalsIgnoreCase("producao")) {
			tipo = "0";
		} else if(tipo.equalsIgnoreCase("exportacao")) {
			tipo = "2";
		} else if(tipo.equalsIgnoreCase("importacao")) {
			tipo = "1";
		}
		
		if(tamanhoArrayAno == 1){
			tamanhoArrayMes = (mesFinalInteger - mesInicialInteger) + 1;
			anoPeriodo[0] = anoInicialInteger;
		} else {
			for(Integer i = 0; i < tamanhoArrayAno; i++) {
				anoPeriodo[i] = anoInicialInteger + i;
			}
		}
		
		if(anoPeriodo.length == 1) {
			Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
			
			for(int i=0; i<mesPeriodo.length; i++) {
				mesPeriodo[i] = mesInicialInteger + i;
				
    			categories.add(mesPeriodo[i].toString() + "/" + anoPeriodo[0].toString());
			}
			for(String tipoCafe : tipoCafes) {
					
				List<String> columns = new ArrayList<String>();
				columns.add("Países agregados" + " - " + tipoCafe);
					
				for(Integer mes : mesPeriodo) {
					Long valor = 0L;
					for(Pais p : paisAgregadoList) {
						
						ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
								tipo.toString(), "76", p.getComtradeId().toString(), anoPeriodo[0].toString(), mes.toString());
					
						if(comtrade != null) {
							
							if(tipoDadoComtrade.equalsIgnoreCase("tradeValue")) {
								valor += comtrade.getTradeValue();
							} else if(tipoDadoComtrade.equalsIgnoreCase("NetWeight")) {
								valor += comtrade.getNetWeight();
							}
							
						}
					}
					columns.add(valor.toString());
					graficoItemValorEntity.addColumns(columns);
				}
			}
			graficoItemValorEntity.setCategories(categories);
		} else {
			for(Integer ano : anoPeriodo) {
				
				if(ano.equals(anoInicialInteger)) {
					tamanhoArrayMes = 12 - mesInicialInteger + 1;
					Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
					
					for(int i=0; i<tamanhoArrayMes; i++) {
						mesPeriodo[i] = mesInicialInteger + i;
						
						categories.add(mesPeriodo[i].toString() + "/" + ano.toString());
					}
				} else if(ano.equals(anoFinalInteger)) {
					tamanhoArrayMes = mesFinalInteger;
					Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
					for(int i=tamanhoArrayMes-1; i>=0; i--) {
						mesPeriodo[i] = mesFinalInteger - i;
						
						categories.add(mesPeriodo[i].toString() + "/" + ano.toString());
					}
				} else {
					tamanhoArrayMes = 12;
					Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
					for(int i=0; i<tamanhoArrayMes; i++) {
						mesPeriodo[i] = i+1;
						
						categories.add(mesPeriodo[i].toString() + "/" + ano.toString());
					}
				}
			}
			
			for(String tipoCafe : tipoCafes) {
				List<String> columns = new ArrayList<String>();
				columns.add("Países agregados - " + tipoCafe);
					for(Integer ano : anoPeriodo) {
						if(ano.equals(anoInicialInteger)) {
							tamanhoArrayMes = 12 - mesInicialInteger + 1;
							Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
							
							for(int i=0; i<tamanhoArrayMes; i++) {
								Long valor = 0L;
								
								for(Pais p : paisAgregadoList) {
									mesPeriodo[i] = mesInicialInteger + i;							
									ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
											tipo.toString(), "76", p.getComtradeId().toString(), ano.toString(), mesPeriodo[i].toString());
									
									if(comtrade != null) {
										
										if(tipoDadoComtrade.equalsIgnoreCase("tradeValue")) {
											valor += comtrade.getTradeValue();
										} else if(tipoDadoComtrade.equalsIgnoreCase("NetWeight")) {
											valor += comtrade.getNetWeight();
										}
										
									}
								}
								columns.add(valor.toString());
								graficoItemValorEntity.addColumns(columns);
							}
						} else if(ano.equals(anoFinalInteger)) {
							tamanhoArrayMes = mesFinalInteger;
							Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
							
							for(int i=tamanhoArrayMes-1; i>=0; i--) {
								Long valor = 0L;
								for(Pais p : paisAgregadoList) {
									mesPeriodo[i] = mesFinalInteger - i;
										
									ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
											tipo.toString(), "76", p.getComtradeId().toString(), ano.toString(), mesPeriodo[i].toString());
									
									if(comtrade != null) {
										
										if(tipoDadoComtrade.equalsIgnoreCase("tradeValue")) {
											valor += comtrade.getTradeValue();
										} else if(tipoDadoComtrade.equalsIgnoreCase("NetWeight")) {
											valor += comtrade.getNetWeight();
										}
										
									}
								}
								columns.add(valor.toString());
								graficoItemValorEntity.addColumns(columns);
							}
						} else {
							tamanhoArrayMes = 12;
							Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
							
							for(int i=0; i<tamanhoArrayMes; i++) {
								Long valor = 0L;
								for(Pais p : paisAgregadoList) {
								
									mesPeriodo[i] = i+1;
									ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
											tipo.toString(), "76", p.getComtradeId().toString(), ano.toString(), mesPeriodo[i].toString());
									
									if(comtrade != null) {
										
										if(tipoDadoComtrade.equalsIgnoreCase("tradeValue")) {
											valor += comtrade.getTradeValue();
										} else if(tipoDadoComtrade.equalsIgnoreCase("NetWeight")) {
											valor += comtrade.getNetWeight();
										}
										
									}
								}
								columns.add(valor.toString());
								graficoItemValorEntity.addColumns(columns);
						}
					}
				}
			}
			graficoItemValorEntity.setCategories(categories);
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
		String[] tipoGraficoArr = asFormUrlEncoded.get("tipoGrafico");
		String tipoGrafico = tipoGraficoArr[0];
		
		String[] mesInicialArr = null;//asFormUrlEncoded.get("mesInicial");
		String mesInicial = "";
		mesInicialArr = asFormUrlEncoded.get("mesInicial");
		mesInicial = mesInicialArr[0];
		
		String[] mesFinalArr = null;
		String mesFinal = "";
		mesFinalArr = asFormUrlEncoded.get("mesFinal");
		mesFinal = mesFinalArr[0];
		
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
		
		if(fonte.equalsIgnoreCase("abics")) {
			
			List<AbicsDataRelatorioAbicsResponse> responseList = new ArrayList<AbicsDataRelatorioAbicsResponse>();
			
			Integer mesInicialInteger = Integer.valueOf(mesInicial);
			Integer mesFinalInteger = Integer.valueOf(mesFinal);
			Integer anoInicialInteger = Integer.valueOf(anoInicial);
			Integer anoFinalInteger = Integer.valueOf(anoFinal);
			Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
			Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
			Integer tamanhoArrayMes = 0;
			
			if(tamanhoArrayAno == 1){
				tamanhoArrayMes = (mesFinalInteger - mesInicialInteger) + 1;
				anoPeriodo[0] = anoInicialInteger;
			} else {
				for(Integer i = 0; i < tamanhoArrayAno; i++) {
					anoPeriodo[i] = anoInicialInteger + i;
				}
			}
			
			if(tipoGrafico.equals("sequencial")) {
				for(String tipoCafe : tipoCafes) {
					for(Pais p : paisAgregadoList) {
						if(anoPeriodo.length == 1) {
							Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
							
							for(int i=0; i<mesPeriodo.length; i++) {
								mesPeriodo[i] = mesInicialInteger + i;
								
								AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(anoPeriodo[0], mesPeriodo[i], p.getId());
								
								if(abicsExportacao != null) {
									
									String peso = abicsExportacao.getPeso() != null ? abicsExportacao.getPeso().toString() : "0";
									String receita = abicsExportacao.getReceita() != null ? abicsExportacao.getReceita().toString() : "0";
									String saca60kg = abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg().toString() : "0";
									
									AbicsDataRelatorioAbicsResponse response = 
											new AbicsDataRelatorioAbicsResponse(tipo, tipoCafe, p.getNome(), mesPeriodo[i].toString(), anoPeriodo[0].toString(),
													peso, receita, saca60kg);
									responseList.add(response);
								}
							}
						} else {
							for(Integer ano : anoPeriodo) {
								if(ano.equals(anoInicialInteger)) {
									tamanhoArrayMes = 12 - mesInicialInteger + 1;
									Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
									
									for(int i=0; i<tamanhoArrayMes; i++) {
										mesPeriodo[i] = mesInicialInteger + i;
										
										AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(ano, mesPeriodo[i], p.getId());
										if(abicsExportacao != null) {

											String peso = abicsExportacao.getPeso() != null ? abicsExportacao.getPeso().toString() : "0";
											String receita = abicsExportacao.getReceita() != null ? abicsExportacao.getReceita().toString() : "0";
											String saca60kg = abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg().toString() : "0";
											
											AbicsDataRelatorioAbicsResponse response = 
											new AbicsDataRelatorioAbicsResponse(tipo, tipoCafe, p.getNome(), mesPeriodo[i].toString(), ano.toString(),
													peso, receita, saca60kg);
											
											responseList.add(response);
										}
									}
								}
								else if(ano.equals(anoFinalInteger)) {
									tamanhoArrayMes = mesFinalInteger;
									Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
									for(int i=tamanhoArrayMes-1; i>=0; i--) {
										mesPeriodo[i] = mesFinalInteger - i;
										
										AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(ano, mesPeriodo[i], p.getId());
										
										if(abicsExportacao != null) {
											
											String peso = abicsExportacao.getPeso() != null ? abicsExportacao.getPeso().toString() : "0";
											String receita = abicsExportacao.getReceita() != null ? abicsExportacao.getReceita().toString() : "0";
											String saca60kg = abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg().toString() : "0";
											
											AbicsDataRelatorioAbicsResponse response = 
											new AbicsDataRelatorioAbicsResponse(tipo, tipoCafe, p.getNome(), mesPeriodo[i].toString(), ano.toString(),
													peso, receita, saca60kg);
											responseList.add(response);
										}
									}
								} else {
									tamanhoArrayMes = 12;
									Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
									for(int i=0; i<tamanhoArrayMes; i++) {
										mesPeriodo[i] = i+1;
										
										AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(ano, mesPeriodo[i], p.getId());
										
										if(abicsExportacao != null) {
											
											String peso = abicsExportacao.getPeso() != null ? abicsExportacao.getPeso().toString() : "0";
											String receita = abicsExportacao.getReceita() != null ? abicsExportacao.getReceita().toString() : "0";
											String saca60kg = abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg().toString() : "0";
											
											AbicsDataRelatorioAbicsResponse response = 
											new AbicsDataRelatorioAbicsResponse(tipo, tipoCafe, p.getNome(), mesPeriodo[i].toString(), ano.toString(),
													peso, receita, saca60kg);
											responseList.add(response);
										}
									}
								}
							}
						}
					}
				}
				
			} else if(tipoGrafico.equalsIgnoreCase("anual")) {
				for(String tipoCafe : tipoCafes) {
					for(Pais p : paisAgregadoList) {
						for(Integer ano : anoPeriodo) {
							tamanhoArrayMes = mesFinalInteger - mesInicialInteger + 1;
							Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
						
							for(int i=0; i<mesPeriodo.length; i++) {
								mesPeriodo[i] = mesInicialInteger + i;
								
								AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(ano, mesPeriodo[i], p.getId());
								
								if(abicsExportacao != null) {
									
									String peso = abicsExportacao.getPeso() != null ? abicsExportacao.getPeso().toString() : "0";
									String receita = abicsExportacao.getReceita() != null ? abicsExportacao.getReceita().toString() : "0";
									String saca60kg = abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg().toString() : "0";
									
									AbicsDataRelatorioAbicsResponse response = 
											new AbicsDataRelatorioAbicsResponse(tipo, tipoCafe, p.getNome(), mesPeriodo[i].toString(), ano.toString(),
													peso, receita, saca60kg);
									responseList.add(response);
								}
							}
						}
					}
				}
			}
			
			return ok(Json.toJson(responseList));
		}
		
		if(fonte.equalsIgnoreCase("comtrade")) {
			String tipoInt = "";
			
			if(tipo.equalsIgnoreCase("producao")) {
				tipoInt = "0";
			} else if(tipo.equalsIgnoreCase("exportacao")) {
				tipoInt = "2";
			} else if(tipo.equalsIgnoreCase("importacao")) {
				tipoInt = "1";
			}
			
			List<AbicsDataRelatorioComtradeMesResponse> responseList = new ArrayList<AbicsDataRelatorioComtradeMesResponse>();

			Integer mesInicialInteger = Integer.valueOf(mesInicial);
			Integer mesFinalInteger = Integer.valueOf(mesFinal);
			Integer anoInicialInteger = Integer.valueOf(anoInicial);
			Integer anoFinalInteger = Integer.valueOf(anoFinal);
			Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
			Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
			Integer tamanhoArrayMes = 0;
			
			if(tamanhoArrayAno == 1){
				tamanhoArrayMes = (mesFinalInteger - mesInicialInteger) + 1;
				anoPeriodo[0] = anoInicialInteger;
			} else {
				for(Integer i = 0; i < tamanhoArrayAno; i++) {
					anoPeriodo[i] = anoInicialInteger + i;
				}
			}
			
			if(tipoGrafico.equals("sequencial")) {
				for(String tipoCafe : tipoCafes) {
					for(Pais p : paisAgregadoList) {
						if(p.getComtradeId().toString().equals("0")) {
							continue;
						}
						
						if(anoPeriodo.length == 1) {
							Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
							
							for(int i=0; i<mesPeriodo.length; i++) {
								mesPeriodo[i] = mesInicialInteger + i;
								
								ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
										tipoInt.toString(), "76", p.getComtradeId().toString(), anoPeriodo[0].toString(), mesPeriodo[i].toString());
								
								if(comtrade != null) {
									
									AbicsDataRelatorioComtradeMesResponse response = 
											new AbicsDataRelatorioComtradeMesResponse(tipo, tipoCafe, comtrade.getPtTitle(), mesPeriodo[i].toString(), anoPeriodo[0].toString(), 
													String.valueOf(comtrade.getTradeValue()), String.valueOf(comtrade.getNetWeight())); 
									responseList.add(response);	
								}
							}
						} else {
							for(Integer ano : anoPeriodo) {
								if(ano.equals(anoInicialInteger)) {
									tamanhoArrayMes = 12 - mesInicialInteger + 1;
									Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
									
									for(int i=0; i<tamanhoArrayMes; i++) {
										mesPeriodo[i] = mesInicialInteger + i;
										
										ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
												tipoInt.toString(), "76", p.getComtradeId().toString(), ano.toString(), mesPeriodo[i].toString());
										
										if(comtrade != null) {
											AbicsDataRelatorioComtradeMesResponse response = 
													new AbicsDataRelatorioComtradeMesResponse(tipo, tipoCafe, comtrade.getPtTitle(), mesPeriodo[i].toString(), ano.toString(), 
															String.valueOf(comtrade.getTradeValue()), String.valueOf(comtrade.getNetWeight())); 
											responseList.add(response);	
										}
									}
								}
								else if(ano.equals(anoFinalInteger)) {
									tamanhoArrayMes = mesFinalInteger;
									Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
									for(int i=tamanhoArrayMes-1; i>=0; i--) {
										mesPeriodo[i] = mesFinalInteger - i;
										
										ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
												tipoInt.toString(), "76", p.getComtradeId().toString(), ano.toString(), mesPeriodo[i].toString());
										
										if(comtrade != null) {
											
											AbicsDataRelatorioComtradeMesResponse response = 
													new AbicsDataRelatorioComtradeMesResponse(tipo, tipoCafe, comtrade.getPtTitle(), mesPeriodo[i].toString(), ano.toString(), 
															String.valueOf(comtrade.getTradeValue()), String.valueOf(comtrade.getNetWeight())); 
											responseList.add(response);	
										}
									}
								} else {
									tamanhoArrayMes = 12;
									Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
									for(int i=0; i<tamanhoArrayMes; i++) {
										mesPeriodo[i] = i+1;
										
										ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
												tipoInt.toString(), "76", p.getComtradeId().toString(), ano.toString(), mesPeriodo[i].toString());
										
										if(comtrade != null) {
											
											AbicsDataRelatorioComtradeMesResponse response = 
													new AbicsDataRelatorioComtradeMesResponse(tipo, tipoCafe, comtrade.getPtTitle(), mesPeriodo[i].toString(), ano.toString(), 
															String.valueOf(comtrade.getTradeValue()), String.valueOf(comtrade.getNetWeight())); 
											responseList.add(response);	
										}
									}
								}
							}
						}
					}
				}
			} else if(tipoGrafico.equalsIgnoreCase("anual")) {
				for(String tipoCafe : tipoCafes) {
					for(Pais p : paisAgregadoList) {
						for(Integer ano : anoPeriodo) {
							tamanhoArrayMes = mesFinalInteger - mesInicialInteger + 1;
							Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
						
							for(int i=0; i<mesPeriodo.length; i++) {
								mesPeriodo[i] = mesInicialInteger + i;
								
								ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
										tipoInt.toString(), "76", p.getComtradeId().toString(), ano.toString(), mesPeriodo[i].toString());
								
								if(comtrade != null) {
									
									AbicsDataRelatorioComtradeMesResponse response = 
											new AbicsDataRelatorioComtradeMesResponse(tipo, tipoCafe, comtrade.getPtTitle(), mesPeriodo[i].toString(), ano.toString(), 
													String.valueOf(comtrade.getTradeValue()), String.valueOf(comtrade.getNetWeight())); 
									responseList.add(response);	
								}
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
		
		String[] mesInicialArr = null;//asFormUrlEncoded.get("mesInicial");
		String mesInicial = "";
		mesInicialArr = asFormUrlEncoded.get("mesInicial");
		mesInicial = mesInicialArr[0];
		
		String[] mesFinalArr = null;
		String mesFinal = "";
		mesFinalArr = asFormUrlEncoded.get("mesFinal");
		mesFinal = mesFinalArr[0];
		
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
		
		if(fonte.equalsIgnoreCase("abics")) {
			
			List<AbicsDataRelatorioAbicsResponse> responseList = new ArrayList<AbicsDataRelatorioAbicsResponse>();
			
			Integer mesInicialInteger = Integer.valueOf(mesInicial);
			Integer mesFinalInteger = Integer.valueOf(mesFinal);
			Integer anoInicialInteger = Integer.valueOf(anoInicial);
			Integer anoFinalInteger = Integer.valueOf(anoFinal);
			Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
			Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
			Integer tamanhoArrayMes = 0;
			
			if(tamanhoArrayAno == 1){
				tamanhoArrayMes = (mesFinalInteger - mesInicialInteger) + 1;
				anoPeriodo[0] = anoInicialInteger;
			} else {
				for(Integer i = 0; i < tamanhoArrayAno; i++) {
					anoPeriodo[i] = anoInicialInteger + i;
				}
			}
			
			for(String tipoCafe : tipoCafes) {
				if(anoPeriodo.length == 1) {
					Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
						
					for(int i=0; i<mesPeriodo.length; i++) {
						Long peso = 0L;
						Long receita = 0L;
						Long saca60kg = 0L;
						
						for(Pais p : paisAgregadoList) {
							mesPeriodo[i] = mesInicialInteger + i;
							
							AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(anoPeriodo[0], mesPeriodo[i], p.getId());
							
							if(abicsExportacao != null) {
								
								peso = abicsExportacao.getPeso() != null ? peso + abicsExportacao.getPeso() : 0L;
								receita = abicsExportacao.getReceita() != null ? receita + abicsExportacao.getReceita() : 0L;
								saca60kg = abicsExportacao.getSaca60kg() != null ? saca60kg + abicsExportacao.getSaca60kg() : 0L;
							}
						}
						AbicsDataRelatorioAbicsResponse response = 
								new AbicsDataRelatorioAbicsResponse(tipo, tipoCafe, "Países agregados", mesPeriodo[i].toString(), anoPeriodo[0].toString(),
										peso.toString(), receita.toString(), saca60kg.toString());
						responseList.add(response);
					}
				} else {
					for(Integer ano : anoPeriodo) {
						if(ano.equals(anoInicialInteger)) {
							tamanhoArrayMes = 12 - mesInicialInteger + 1;
							Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
								
							for(int i=0; i<tamanhoArrayMes; i++) {
								Long peso = 0L;
								Long receita = 0L;
								Long saca60kg = 0L;
								
								for(Pais p : paisAgregadoList) {
									mesPeriodo[i] = mesInicialInteger + i;
									
									AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(ano, mesPeriodo[i], p.getId());
									if(abicsExportacao != null) {

										peso = abicsExportacao.getPeso() != null ? peso + abicsExportacao.getPeso() : 0L;
										receita = abicsExportacao.getReceita() != null ? receita + abicsExportacao.getReceita() : 0L;
										saca60kg = abicsExportacao.getSaca60kg() != null ? saca60kg + abicsExportacao.getSaca60kg() : 0L;
									}
								}
								AbicsDataRelatorioAbicsResponse response = 
										new AbicsDataRelatorioAbicsResponse(tipo, tipoCafe, "Países agregados", mesPeriodo[i].toString(), ano.toString(),
												peso.toString(), receita.toString(), saca60kg.toString());
										
								responseList.add(response);
							}
						} else if(ano.equals(anoFinalInteger)) {
							tamanhoArrayMes = mesFinalInteger;
							Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
							for(int i=tamanhoArrayMes-1; i>=0; i--) {
								Long peso = 0L;
								Long receita = 0L;
								Long saca60kg = 0L;
								
								for(Pais p : paisAgregadoList) {
									mesPeriodo[i] = mesFinalInteger - i;
									
									AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(ano, mesPeriodo[i], p.getId());
									
									if(abicsExportacao != null) {
										
										peso = abicsExportacao.getPeso() != null ? peso + abicsExportacao.getPeso() : 0L;
										receita = abicsExportacao.getReceita() != null ? receita + abicsExportacao.getReceita() : 0L;
										saca60kg = abicsExportacao.getSaca60kg() != null ? saca60kg + abicsExportacao.getSaca60kg() : 0L;
									}
								}
								AbicsDataRelatorioAbicsResponse response = 
										new AbicsDataRelatorioAbicsResponse(tipo, tipoCafe, "Países agregados", mesPeriodo[i].toString(), ano.toString(),
												peso.toString(), receita.toString(), saca60kg.toString());
								responseList.add(response);
							} 
						} else {
							tamanhoArrayMes = 12;
							Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
							for(int i=0; i<tamanhoArrayMes; i++) {
								Long peso = 0L;
								Long receita = 0L;
								Long saca60kg = 0L;
								
								for(Pais p : paisAgregadoList) {
									mesPeriodo[i] = i+1;
									
									AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(ano, mesPeriodo[i], p.getId());
									
									if(abicsExportacao != null) {
										
										peso = abicsExportacao.getPeso() != null ? peso + abicsExportacao.getPeso() : 0L;
										receita = abicsExportacao.getReceita() != null ? receita + abicsExportacao.getReceita() : 0L;
										saca60kg = abicsExportacao.getSaca60kg() != null ? saca60kg + abicsExportacao.getSaca60kg() : 0L;
									}
								}
								AbicsDataRelatorioAbicsResponse response = 
										new AbicsDataRelatorioAbicsResponse(tipo, tipoCafe, "Países agregados", mesPeriodo[i].toString(), ano.toString(),
												peso.toString(), receita.toString(), saca60kg.toString());
								responseList.add(response);
							}
						}
					}
				}
			}
			return ok(Json.toJson(responseList));
		}
		
		if(fonte.equalsIgnoreCase("comtrade")) {
			String tipoInt = "";
			
			if(tipo.equalsIgnoreCase("producao")) {
				tipoInt = "0";
			} else if(tipo.equalsIgnoreCase("exportacao")) {
				tipoInt = "2";
			} else if(tipo.equalsIgnoreCase("importacao")) {
				tipoInt = "1";
			}
			
			List<AbicsDataRelatorioComtradeMesResponse> responseList = new ArrayList<AbicsDataRelatorioComtradeMesResponse>();

			Integer mesInicialInteger = Integer.valueOf(mesInicial);
			Integer mesFinalInteger = Integer.valueOf(mesFinal);
			Integer anoInicialInteger = Integer.valueOf(anoInicial);
			Integer anoFinalInteger = Integer.valueOf(anoFinal);
			Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
			Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
			Integer tamanhoArrayMes = 0;
			
			if(tamanhoArrayAno == 1){
				tamanhoArrayMes = (mesFinalInteger - mesInicialInteger) + 1;
				anoPeriodo[0] = anoInicialInteger;
			} else {
				for(Integer i = 0; i < tamanhoArrayAno; i++) {
					anoPeriodo[i] = anoInicialInteger + i;
				}
			}
			for(String tipoCafe : tipoCafes) {
				if(anoPeriodo.length == 1) {
					Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
					
					for(int i=0; i<mesPeriodo.length; i++) {
						ComtradeDataSumarizacao comtrade = null;
						Long tradeValue = 0L;
						Long netWeight = 0L;
						
						for(Pais p : paisAgregadoList) {
							if(p.getComtradeId().toString().equals("0")) {
								continue;
							}
							
							mesPeriodo[i] = mesInicialInteger + i;
							
							comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
									tipoInt.toString(), "76", p.getComtradeId().toString(), anoPeriodo[0].toString(), mesPeriodo[i].toString());
							
							if(comtrade != null) {
								tradeValue += comtrade.getTradeValue();
								netWeight += comtrade.getNetWeight();
							}
							
						}
						AbicsDataRelatorioComtradeMesResponse response = 
								new AbicsDataRelatorioComtradeMesResponse(tipo, tipoCafe, "Países agregados", mesPeriodo[i].toString(), anoPeriodo[0].toString(), 
										String.valueOf(tradeValue), String.valueOf(netWeight)); 
						responseList.add(response);	
					} 
				} else {
					for(Integer ano : anoPeriodo) {
						if(ano.equals(anoInicialInteger)) {
							tamanhoArrayMes = 12 - mesInicialInteger + 1;
							Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
							
							for(int i=0; i<tamanhoArrayMes; i++) {
								ComtradeDataSumarizacao comtrade = null;
								
								Long tradeValue = 0L;
								Long netWeight = 0L;
								
								for(Pais p : paisAgregadoList) {
									mesPeriodo[i] = mesInicialInteger + i;
									
									comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
											tipoInt.toString(), "76", p.getComtradeId().toString(), ano.toString(), mesPeriodo[i].toString());
									
									if(comtrade != null) {
										tradeValue += comtrade.getTradeValue();
										netWeight += comtrade.getNetWeight();
									}
								}
								AbicsDataRelatorioComtradeMesResponse response = 
										new AbicsDataRelatorioComtradeMesResponse(tipo, tipoCafe, "Países agregados", mesPeriodo[i].toString(), ano.toString(), 
												String.valueOf(tradeValue), String.valueOf(netWeight)); 
								responseList.add(response);	
							}
						}
						else if(ano.equals(anoFinalInteger)) {
							tamanhoArrayMes = mesFinalInteger;
							Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
							for(int i=tamanhoArrayMes-1; i>=0; i--) {
								ComtradeDataSumarizacao comtrade = null;
								
								Long tradeValue = 0L;
								Long netWeight = 0L;
								
								for(Pais p : paisAgregadoList) {
									mesPeriodo[i] = mesFinalInteger - i;
									
									comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
											tipoInt.toString(), "76", p.getComtradeId().toString(), ano.toString(), mesPeriodo[i].toString());
									
									if(comtrade != null) {
										tradeValue += comtrade.getTradeValue();
										netWeight += comtrade.getNetWeight();
									}
								}
								
								AbicsDataRelatorioComtradeMesResponse response = 
										new AbicsDataRelatorioComtradeMesResponse(tipo, tipoCafe, "Países agregados", mesPeriodo[i].toString(), ano.toString(), 
												String.valueOf(tradeValue), String.valueOf(netWeight)); 
								responseList.add(response);	
							}
						} else {
							tamanhoArrayMes = 12;
							Integer[] mesPeriodo = new Integer[tamanhoArrayMes];
							for(int i=0; i<tamanhoArrayMes; i++) {
								mesPeriodo[i] = i+1;
								ComtradeDataSumarizacao comtrade = null;
								
								Long tradeValue = 0L;
								Long netWeight = 0L;
								
								for(Pais p : paisAgregadoList) {
									comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
											tipoInt.toString(), "76", p.getComtradeId().toString(), ano.toString(), mesPeriodo[i].toString());
									
									if(comtrade != null) {
										tradeValue += comtrade.getTradeValue();
										netWeight += comtrade.getNetWeight();
									}
								}
								
								AbicsDataRelatorioComtradeMesResponse response = 
										new AbicsDataRelatorioComtradeMesResponse(tipo, tipoCafe, "Países agregados", mesPeriodo[i].toString(), ano.toString(), 
												String.valueOf(tradeValue), String.valueOf(netWeight)); 
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
		
		String[] mesInicialArr = null;//asFormUrlEncoded.get("mesInicial");
		String mesInicial = "";
		mesInicialArr = asFormUrlEncoded.get("mesInicial");
		mesInicial = mesInicialArr[0];
		
		String[] mesFinalArr = null;
		String mesFinal = "";
		mesFinalArr = asFormUrlEncoded.get("mesFinal");
		mesFinal = mesFinalArr[0];
		
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
					+"/relatorioPaisMensal.xlsx";
			
			String sheetName1 = "";
			String sheetName2 = "";
			String sheetName3 = "Abics";
			
			String tipoInt = "";
			
			if(fonte.equalsIgnoreCase("abics")) {
				sheetName1 = "Abics - Peso";//name of sheet
				sheetName2 = "Abics - Receita";
				sheetName3 = "Abics - Saca 60kg";
				
			} else if(fonte.equalsIgnoreCase("comtrade")) {
				sheetName1 = "Comtrade - Net Weight";//name of sheet
				sheetName2 = "Comtrade - Trade Value";//name of sheet
				
				if(tipo.equalsIgnoreCase("producao")) {
					tipoInt = "0";
				} else if(tipo.equalsIgnoreCase("exportacao")) {
					tipoInt = "2";
				} else if(tipo.equalsIgnoreCase("importacao")) {
					tipoInt = "1";
				}
			}
			
			HSSFWorkbook wb = new HSSFWorkbook();
	
			HSSFSheet sheetComtrade1 = wb.createSheet(sheetName1);
			HSSFSheet sheetComtrade2 = wb.createSheet(sheetName2);
			HSSFSheet sheetAbics = wb.createSheet(sheetName3);
			
			HSSFColor lightPink =  ExcelUtil.setColor(wb, (byte)(Integer.parseInt("255")) , (byte)(Integer.parseInt("227")) ,(byte)(Integer.parseInt("221")), new HSSFColor.PINK());
			CellStyle cellStyleTipos = ExcelUtil.createStyle(wb, IndexedColors.BROWN.getIndex(), lightPink.getIndex() , (short) 11, true, false, true);
			
			CellStyle cellStylePaisHeader = ExcelUtil.createStyle(wb, IndexedColors.WHITE.getIndex(), IndexedColors.BROWN.getIndex(), (short) 13, true, false, true);
			CellStyle cellStyleHeader = ExcelUtil.createStyle(wb, IndexedColors.BROWN.getIndex(), IndexedColors.WHITE.getIndex(), (short) 13, true, false, true);
			CellStyle cellStyleAnoPaisHeader = ExcelUtil.createStyle(wb, IndexedColors.BROWN.getIndex(), IndexedColors.WHITE.getIndex(), (short) 11, true, false, true);
			CellStyle cellStyleDados = ExcelUtil.createStyle(wb, IndexedColors.BLACK.getIndex(), IndexedColors.WHITE.getIndex(), (short) 11, false, false, true);
			
			HSSFRow row0Comtrade1 = sheetComtrade1.createRow(0);
			HSSFCell cellCabecalho1 = row0Comtrade1.createCell(0);
			cellCabecalho1.setCellValue("Fonte: " + fonte.toUpperCase() + " (Mensal) - Período: " + mesInicial + "/" + anoInicial 
					+ " a " + mesFinal + "/" + anoFinal);
			cellCabecalho1.setCellStyle(cellStyleHeader);
			
			HSSFRow row0Comtrade2 = sheetComtrade2.createRow(0);
			HSSFCell cellCabecalho2 = row0Comtrade2.createCell(0);
			cellCabecalho2.setCellValue("Fonte: " + fonte.toUpperCase() + " (Mensal) - Período: " + mesInicial + "/" + anoInicial 
					+ " a " + mesFinal + "/" + anoFinal);
			cellCabecalho2.setCellStyle(cellStyleHeader);
			
			HSSFRow row0Abics = sheetAbics.createRow(0);
			HSSFCell cellCabecalho3 = row0Abics.createCell(0);
			cellCabecalho3.setCellValue("Fonte: " + fonte.toUpperCase() + " (Mensal) - Período: " + mesInicial + "/" + anoInicial 
					+ " a " + mesFinal + "/" + anoFinal);
			cellCabecalho3.setCellStyle(cellStyleHeader);
			
			HSSFRow row1ComtradeTipo1 = sheetComtrade1.createRow(1);
			HSSFRow row1ComtradeTipo2 = sheetComtrade2.createRow(1);
			HSSFRow row1Abics = sheetAbics.createRow(1);
			
			HSSFCell cellTipo = row1ComtradeTipo1.createCell(0);
			HSSFCell cellTipo2 = row1ComtradeTipo2.createCell(0);
			HSSFCell cellTipo3 = row1Abics.createCell(0);
			
			if(fonte.equalsIgnoreCase("comtrade")) {
				cellTipo.setCellValue(tipo.toUpperCase() + " (Net Weight)");
				cellTipo2.setCellValue(tipo.toUpperCase() + " (Trade Value)");
				cellTipo3.setCellValue("Vazio");
			} else {
				cellTipo.setCellValue(tipo.toUpperCase() + " (Peso)");
				cellTipo2.setCellValue(tipo.toUpperCase() + " (Receita)");
				cellTipo3.setCellValue(tipo.toUpperCase() + " (Saca 60kg)");
			}
			
			sheetComtrade1.addMergedRegion(new CellRangeAddress(0,0,0,12));
			sheetComtrade1.addMergedRegion(new CellRangeAddress(1,1,0,12));
			
			sheetComtrade2.addMergedRegion(new CellRangeAddress(0,0,0,12));
			sheetComtrade2.addMergedRegion(new CellRangeAddress(1,1,0,12));
			
			sheetAbics.addMergedRegion(new CellRangeAddress(0,0,0,12));
			sheetAbics.addMergedRegion(new CellRangeAddress(1,1,0,12));
			
			cellTipo.setCellStyle(cellStyleTipos);
			cellTipo2.setCellStyle(cellStyleTipos);
			cellTipo3.setCellStyle(cellStyleTipos);
			
			Integer anoInicialInteger = Integer.valueOf(anoInicial);
			Integer anoFinalInteger = Integer.valueOf(anoFinal);
			Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
			Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
			
			for(Integer i = 0; i < tamanhoArrayAno; i++) {
				anoPeriodo[i] = anoInicialInteger + i;
			}

			Integer mesInicialInteger = Integer.valueOf(mesInicial);
			Integer mesFinalInteger = Integer.valueOf(mesFinal);
			Integer tamanhoArrayMes = 0;
			
			int indiceMes = 3;
			int indiceAno = 2;

			for(int i=0; i<tamanhoArrayAno; i++){
				HSSFRow row2ComtradePaisAno1 = sheetComtrade1.createRow(indiceAno);
				HSSFRow row2ComtradePaisAno2 = sheetComtrade2.createRow(indiceAno);
				HSSFRow row2AbicsPaisAno = sheetAbics.createRow(indiceAno);
				
				HSSFCell cellPais = row2ComtradePaisAno1.createCell(0);
				cellPais.setCellValue("PAÍS ORIGEM");
				cellPais.setCellStyle(cellStylePaisHeader);
				
				HSSFCell cellPais2 = row2ComtradePaisAno2.createCell(0);
				cellPais2.setCellValue("PAÍS ORIGEM");
				cellPais2.setCellStyle(cellStylePaisHeader);
				
				HSSFCell cellPais3 = row2AbicsPaisAno.createCell(0);
				cellPais3.setCellValue("PAÍS ORIGEM");
				cellPais3.setCellStyle(cellStylePaisHeader);
				
				HSSFCell cellAno = row2ComtradePaisAno1.createCell(1);
				cellAno.setCellValue(anoPeriodo[i]);
				cellAno.setCellStyle(cellStyleAnoPaisHeader);
				
				HSSFCell cellAno2 = row2ComtradePaisAno2.createCell(1);
				cellAno2.setCellValue(anoPeriodo[i]);
				cellAno2.setCellStyle(cellStyleAnoPaisHeader);
				
				HSSFCell cellAno3 = row2AbicsPaisAno.createCell(1);
				cellAno3.setCellValue(anoPeriodo[i]);
				cellAno3.setCellStyle(cellStyleAnoPaisHeader);
				
				HSSFRow row3ComtradeMes1 = sheetComtrade1.createRow(indiceMes);
				HSSFRow row3ComtradeMes2 = sheetComtrade2.createRow(indiceMes);
				HSSFRow row3AbicsMes = sheetAbics.createRow(indiceMes);
				
				for(int j=0; j< mesesTotais.length; j++) {
					HSSFCell cellMes = row3ComtradeMes1.createCell(j+1);
					cellMes.setCellValue(mesList[j]);
					cellMes.setCellStyle(cellStyleTipos);
					
					HSSFCell cellMes2 = row3ComtradeMes2.createCell(j+1);
					cellMes2.setCellValue(mesList[j]);
					cellMes2.setCellStyle(cellStyleTipos);
					
					HSSFCell cellMes3 = row3AbicsMes.createCell(j+1);
					cellMes3.setCellValue(mesList[j]);
					cellMes3.setCellStyle(cellStyleTipos);
				}
				
				sheetComtrade1.addMergedRegion(new CellRangeAddress(indiceAno,indiceAno+1,0,0));
				sheetComtrade1.addMergedRegion(new CellRangeAddress(indiceAno,indiceAno,1,12));
				
				sheetComtrade2.addMergedRegion(new CellRangeAddress(indiceAno,indiceAno+1,0,0));
				sheetComtrade2.addMergedRegion(new CellRangeAddress(indiceAno,indiceAno,1,12));
				
				sheetAbics.addMergedRegion(new CellRangeAddress(indiceAno,indiceAno+1,0,0));
				sheetAbics.addMergedRegion(new CellRangeAddress(indiceAno,indiceAno,1,12));
				
				indiceMes += paisAgregadoList.size() + 4;
				indiceAno += paisAgregadoList.size() + 4;
			}
			
			for(int i=0; i< paisAgregadoList.size(); i++) {
				
				int indiceLinhaPais = 4;
				
				for(int j=0; j< anoPeriodo.length; j++) {
				
					HSSFRow row4ComtradeValores1 = sheetComtrade1.createRow(i + j*paisAgregadoList.size() + indiceLinhaPais);
					HSSFRow row4ComtradeValores2 = sheetComtrade2.createRow(i + j*paisAgregadoList.size() + indiceLinhaPais);
					HSSFRow row4AbicsValores = sheetAbics.createRow(i + j*paisAgregadoList.size() + indiceLinhaPais);
					
					HSSFCell cellPais = row4ComtradeValores1.createCell(0);
					cellPais.setCellValue(paisAgregadoList.get(i).getNome());
					cellPais.setCellStyle(cellStyleAnoPaisHeader);
					
					HSSFCell cellPais2 = row4ComtradeValores2.createCell(0);
					cellPais2.setCellValue(paisAgregadoList.get(i).getNome());
					cellPais2.setCellStyle(cellStyleAnoPaisHeader);
					
					HSSFCell cellPais3 = row4AbicsValores.createCell(0);
					cellPais3.setCellValue(paisAgregadoList.get(i).getNome());
					cellPais3.setCellStyle(cellStyleAnoPaisHeader);
					
					indiceLinhaPais += 4;
					
					Integer[] mesPeriodo = null;
					
					if(anoPeriodo.length == 1) {
						tamanhoArrayMes = (mesFinalInteger - mesInicialInteger) + 1;
					
					} else {
						if(anoPeriodo[j].equals(anoInicialInteger)) {
							tamanhoArrayMes = 12 - mesInicialInteger + 1;
							
						} else if(anoPeriodo[j].equals(anoFinalInteger)) {
							tamanhoArrayMes = mesFinalInteger;
						
						} else if(!anoPeriodo[j].equals(anoInicialInteger) && !anoPeriodo[j].equals(anoFinalInteger)) {
							tamanhoArrayMes = 12;
						}
					}
					
					mesPeriodo = new Integer[tamanhoArrayMes];
					
					if(anoPeriodo[j].equals(anoFinalInteger) && anoPeriodo.length != 1) {
						for(int k=tamanhoArrayMes-1; k>=0; k--) {
							mesPeriodo[k] = mesFinalInteger - k;
							
							String valor1 = "";
							String valor2 = "";
							String valor3 = "";
							
							if(fonte.equalsIgnoreCase("comtrade")) {
								ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
										tipoInt, "76", paisAgregadoList.get(i).getComtradeId().toString(), anoPeriodo[j].toString(), mesPeriodo[k].toString());
								
								if(comtrade != null) {
									valor1 = comtrade.getNetWeight() != null ? comtrade.getNetWeight().toString() : "0";
									valor2 = comtrade.getTradeValue() != null ? comtrade.getTradeValue().toString() : "0";
								} else {
									valor1 = "0";
									valor2 = "0";
								}
								
							} else if(fonte.equalsIgnoreCase("abics")) {
								AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(anoPeriodo[j], mesPeriodo[k], paisAgregadoList.get(i).getId());
								if(abicsExportacao != null) {
									valor1 = abicsExportacao.getPeso() != null ? abicsExportacao.getPeso().toString() : "0";
									valor2 = abicsExportacao.getReceita() != null ? abicsExportacao.getReceita().toString() : "0";
									valor3 = abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg().toString() : "0";
								} else {
									valor1 = "0";
									valor2 = "0";
									valor3 = "0";
								}
							}
							
							HSSFCell cellValor = row4ComtradeValores1.createCell(mesFinalInteger-k);
							cellValor.setCellValue(valor1);
							cellValor.setCellStyle(cellStyleDados);
							
							HSSFCell cellValor2 = row4ComtradeValores2.createCell(mesFinalInteger-k);
							cellValor2.setCellValue(valor2);
							cellValor2.setCellStyle(cellStyleDados);
							
							HSSFCell cellValor3 = row4AbicsValores.createCell(mesFinalInteger-k);
							cellValor3.setCellValue(valor3);
							cellValor3.setCellStyle(cellStyleDados);
						}
					} else {
						for(int k=0; k<tamanhoArrayMes; k++) {
							HSSFCell cellValor = null;
							HSSFCell cellValor2 = null;
							HSSFCell cellValor3 = null;
							
							if(anoPeriodo[j].equals(anoInicialInteger) || anoPeriodo.length == 1) {
								mesPeriodo[k] = mesInicialInteger + k;
							} else {
								mesPeriodo[k] = k+1;
								cellValor = row4ComtradeValores1.createCell(k+1);
								cellValor2 = row4ComtradeValores2.createCell(k+1);
								cellValor3 = row4AbicsValores.createCell(k+1);
							}
							
							String valor1 = "";
							String valor2 = "";
							String valor3 = "";
							
							if(fonte.equalsIgnoreCase("comtrade")) {
								ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
										tipoInt, "76", paisAgregadoList.get(i).getComtradeId().toString(), anoPeriodo[j].toString(), mesPeriodo[k].toString());
								
								if(comtrade != null) {
									valor1 = comtrade.getNetWeight() != null ? comtrade.getNetWeight().toString() : "0";
									valor2 = comtrade.getTradeValue() != null ? comtrade.getTradeValue().toString() : "0";
								} else {
									valor1 = "0";
									valor2 = "0";
								}
								
							} else if(fonte.equalsIgnoreCase("abics")) {
								AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(anoPeriodo[j], mesPeriodo[k], paisAgregadoList.get(i).getId());
								if(abicsExportacao != null) {
									valor1 = abicsExportacao.getPeso() != null ? abicsExportacao.getPeso().toString() : "0";
									valor2 = abicsExportacao.getReceita() != null ? abicsExportacao.getReceita().toString() : "0";
									valor3 = abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg().toString() : "0";
								} else {
									valor1 = "0";
									valor2 = "0";
									valor3 = "0";
								}
							}
							
							if(cellValor == null) {
								cellValor = row4ComtradeValores1.createCell(mesInicialInteger+k);
								cellValor2 = row4ComtradeValores2.createCell(mesInicialInteger+k);
								cellValor3 = row4AbicsValores.createCell(mesInicialInteger+k);
							}
							
							cellValor.setCellValue(valor1);
							cellValor2.setCellValue(valor2);
							cellValor3.setCellValue(valor3);
							
							cellValor.setCellStyle(cellStyleDados);
							cellValor2.setCellStyle(cellStyleDados);
							cellValor3.setCellStyle(cellStyleDados);
						}
					}
				}
			}
			
			int numColunasTotal = anoPeriodo.length * tipoCafes.length + 1;
			for(int i=0; i< numColunasTotal; i++) {
				sheetComtrade1.autoSizeColumn(i, true);
				sheetComtrade2.autoSizeColumn(i, true);
				sheetAbics.autoSizeColumn(i, true);
			}
			
			if(fonte.equalsIgnoreCase("comtrade")) {
				wb.removeSheetAt(2);
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
		
		String[] mesInicialArr = null;//asFormUrlEncoded.get("mesInicial");
		String mesInicial = "";
		mesInicialArr = asFormUrlEncoded.get("mesInicial");
		mesInicial = mesInicialArr[0];
		
		String[] mesFinalArr = null;
		String mesFinal = "";
		mesFinalArr = asFormUrlEncoded.get("mesFinal");
		mesFinal = mesFinalArr[0];
		
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
					+"/relatorioPaisMensal.xlsx";
			
			String sheetName1 = "";
			String sheetName2 = "";
			String sheetName3 = "Abics";
			
			String tipoInt = "";
			
			if(fonte.equalsIgnoreCase("abics")) {
				sheetName1 = "Abics - Peso";//name of sheet
				sheetName2 = "Abics - Receita";
				sheetName3 = "Abics - Saca 60kg";
				
			} else if(fonte.equalsIgnoreCase("comtrade")) {
				sheetName1 = "Comtrade - Net Weight";//name of sheet
				sheetName2 = "Comtrade - Trade Value";//name of sheet
				
				if(tipo.equalsIgnoreCase("producao")) {
					tipoInt = "0";
				} else if(tipo.equalsIgnoreCase("exportacao")) {
					tipoInt = "2";
				} else if(tipo.equalsIgnoreCase("importacao")) {
					tipoInt = "1";
				}
			}
			
			HSSFWorkbook wb = new HSSFWorkbook();
	
			HSSFSheet sheetComtrade1 = wb.createSheet(sheetName1);
			HSSFSheet sheetComtrade2 = wb.createSheet(sheetName2);
			HSSFSheet sheetAbics = wb.createSheet(sheetName3);
			
			HSSFColor lightPink =  ExcelUtil.setColor(wb, (byte)(Integer.parseInt("255")) , (byte)(Integer.parseInt("227")) ,(byte)(Integer.parseInt("221")), new HSSFColor.PINK());
			CellStyle cellStyleTipos = ExcelUtil.createStyle(wb, IndexedColors.BROWN.getIndex(), lightPink.getIndex() , (short) 11, true, false, true);
			
			CellStyle cellStylePaisHeader = ExcelUtil.createStyle(wb, IndexedColors.WHITE.getIndex(), IndexedColors.BROWN.getIndex(), (short) 13, true, false, true);
			CellStyle cellStyleHeader = ExcelUtil.createStyle(wb, IndexedColors.BROWN.getIndex(), IndexedColors.WHITE.getIndex(), (short) 13, true, false, true);
			CellStyle cellStyleAnoPaisHeader = ExcelUtil.createStyle(wb, IndexedColors.BROWN.getIndex(), IndexedColors.WHITE.getIndex(), (short) 11, true, false, true);
			CellStyle cellStyleDados = ExcelUtil.createStyle(wb, IndexedColors.BLACK.getIndex(), IndexedColors.WHITE.getIndex(), (short) 11, false, false, true);
			
			HSSFRow row0Comtrade1 = sheetComtrade1.createRow(0);
			HSSFCell cellCabecalho1 = row0Comtrade1.createCell(0);
			cellCabecalho1.setCellValue("Fonte: " + fonte.toUpperCase() + " (Mensal) - Período: " + mesInicial + "/" + anoInicial 
					+ " a " + mesFinal + "/" + anoFinal);
			cellCabecalho1.setCellStyle(cellStyleHeader);
			
			HSSFRow row0Comtrade2 = sheetComtrade2.createRow(0);
			HSSFCell cellCabecalho2 = row0Comtrade2.createCell(0);
			cellCabecalho2.setCellValue("Fonte: " + fonte.toUpperCase() + " (Mensal) - Período: " + mesInicial + "/" + anoInicial 
					+ " a " + mesFinal + "/" + anoFinal);
			cellCabecalho2.setCellStyle(cellStyleHeader);
			
			HSSFRow row0Abics = sheetAbics.createRow(0);
			HSSFCell cellCabecalho3 = row0Abics.createCell(0);
			cellCabecalho3.setCellValue("Fonte: " + fonte.toUpperCase() + " (Mensal) - Período: " + mesInicial + "/" + anoInicial 
					+ " a " + mesFinal + "/" + anoFinal);
			cellCabecalho3.setCellStyle(cellStyleHeader);
			
			HSSFRow row1ComtradeTipo1 = sheetComtrade1.createRow(1);
			HSSFRow row1ComtradeTipo2 = sheetComtrade2.createRow(1);
			HSSFRow row1Abics = sheetAbics.createRow(1);
			
			HSSFCell cellTipo = row1ComtradeTipo1.createCell(0);
			HSSFCell cellTipo2 = row1ComtradeTipo2.createCell(0);
			HSSFCell cellTipo3 = row1Abics.createCell(0);
			
			if(fonte.equalsIgnoreCase("comtrade")) {
				cellTipo.setCellValue(tipo.toUpperCase() + " (Net Weight)");
				cellTipo2.setCellValue(tipo.toUpperCase() + " (Trade Value)");
				cellTipo3.setCellValue("Vazio");
			} else {
				cellTipo.setCellValue(tipo.toUpperCase() + " (Peso)");
				cellTipo2.setCellValue(tipo.toUpperCase() + " (Receita)");
				cellTipo3.setCellValue(tipo.toUpperCase() + " (Saca 60kg)");
			}
			
			sheetComtrade1.addMergedRegion(new CellRangeAddress(0,0,0,12));
			sheetComtrade1.addMergedRegion(new CellRangeAddress(1,1,0,12));
			
			sheetComtrade2.addMergedRegion(new CellRangeAddress(0,0,0,12));
			sheetComtrade2.addMergedRegion(new CellRangeAddress(1,1,0,12));
			
			sheetAbics.addMergedRegion(new CellRangeAddress(0,0,0,12));
			sheetAbics.addMergedRegion(new CellRangeAddress(1,1,0,12));
			
			cellTipo.setCellStyle(cellStyleTipos);
			cellTipo2.setCellStyle(cellStyleTipos);
			cellTipo3.setCellStyle(cellStyleTipos);
			
			Integer anoInicialInteger = Integer.valueOf(anoInicial);
			Integer anoFinalInteger = Integer.valueOf(anoFinal);
			Integer tamanhoArrayAno = (anoFinalInteger - anoInicialInteger) + 1;
			Integer[] anoPeriodo = new Integer[tamanhoArrayAno];
			
			for(Integer i = 0; i < tamanhoArrayAno; i++) {
				anoPeriodo[i] = anoInicialInteger + i;
			}

			Integer mesInicialInteger = Integer.valueOf(mesInicial);
			Integer mesFinalInteger = Integer.valueOf(mesFinal);
			Integer tamanhoArrayMes = 0;
			
			int indiceMes = 3;
			int indiceAno = 2;

			for(int i=0; i<tamanhoArrayAno; i++){
				HSSFRow row2ComtradePaisAno1 = sheetComtrade1.createRow(indiceAno);
				HSSFRow row2ComtradePaisAno2 = sheetComtrade2.createRow(indiceAno);
				HSSFRow row2AbicsPaisAno = sheetAbics.createRow(indiceAno);
				
				HSSFCell cellPais = row2ComtradePaisAno1.createCell(0);
				cellPais.setCellValue("PAÍS ORIGEM");
				cellPais.setCellStyle(cellStylePaisHeader);
				
				HSSFCell cellPais2 = row2ComtradePaisAno2.createCell(0);
				cellPais2.setCellValue("PAÍS ORIGEM");
				cellPais2.setCellStyle(cellStylePaisHeader);
				
				HSSFCell cellPais3 = row2AbicsPaisAno.createCell(0);
				cellPais3.setCellValue("PAÍS ORIGEM");
				cellPais3.setCellStyle(cellStylePaisHeader);
				
				HSSFCell cellAno = row2ComtradePaisAno1.createCell(1);
				cellAno.setCellValue(anoPeriodo[i]);
				cellAno.setCellStyle(cellStyleAnoPaisHeader);
				
				HSSFCell cellAno2 = row2ComtradePaisAno2.createCell(1);
				cellAno2.setCellValue(anoPeriodo[i]);
				cellAno2.setCellStyle(cellStyleAnoPaisHeader);
				
				HSSFCell cellAno3 = row2AbicsPaisAno.createCell(1);
				cellAno3.setCellValue(anoPeriodo[i]);
				cellAno3.setCellStyle(cellStyleAnoPaisHeader);
				
				HSSFRow row3ComtradeMes1 = sheetComtrade1.createRow(indiceMes);
				HSSFRow row3ComtradeMes2 = sheetComtrade2.createRow(indiceMes);
				HSSFRow row3AbicsMes = sheetAbics.createRow(indiceMes);
				
				for(int j=0; j< mesesTotais.length; j++) {
					HSSFCell cellMes = row3ComtradeMes1.createCell(j+1);
					cellMes.setCellValue(mesList[j]);
					cellMes.setCellStyle(cellStyleTipos);
					
					HSSFCell cellMes2 = row3ComtradeMes2.createCell(j+1);
					cellMes2.setCellValue(mesList[j]);
					cellMes2.setCellStyle(cellStyleTipos);
					
					HSSFCell cellMes3 = row3AbicsMes.createCell(j+1);
					cellMes3.setCellValue(mesList[j]);
					cellMes3.setCellStyle(cellStyleTipos);
				}
				
				sheetComtrade1.addMergedRegion(new CellRangeAddress(indiceAno,indiceAno+1,0,0));
				sheetComtrade1.addMergedRegion(new CellRangeAddress(indiceAno,indiceAno,1,12));
				
				sheetComtrade2.addMergedRegion(new CellRangeAddress(indiceAno,indiceAno+1,0,0));
				sheetComtrade2.addMergedRegion(new CellRangeAddress(indiceAno,indiceAno,1,12));
				
				sheetAbics.addMergedRegion(new CellRangeAddress(indiceAno,indiceAno+1,0,0));
				sheetAbics.addMergedRegion(new CellRangeAddress(indiceAno,indiceAno,1,12));
				
				indiceMes += paisAgregadoList.size() + 4;
				indiceAno += paisAgregadoList.size() + 4;
			}
			
			int indiceLinhaPais = 4;
				
			for(int j=0; j< anoPeriodo.length; j++) {
				HSSFRow row4ComtradeValores1 = sheetComtrade1.createRow(j*paisAgregadoList.size() + indiceLinhaPais);
				HSSFRow row4ComtradeValores2 = sheetComtrade2.createRow(j*paisAgregadoList.size() + indiceLinhaPais);
				HSSFRow row4AbicsValores = sheetAbics.createRow(j*paisAgregadoList.size() + indiceLinhaPais);
				
				HSSFCell cellPais = row4ComtradeValores1.createCell(0);
				cellPais.setCellValue("Países agregados");
				cellPais.setCellStyle(cellStyleAnoPaisHeader);
				
				HSSFCell cellPais2 = row4ComtradeValores2.createCell(0);
				cellPais2.setCellValue("Países agregados");
				cellPais2.setCellStyle(cellStyleAnoPaisHeader);
				
				HSSFCell cellPais3 = row4AbicsValores.createCell(0);
				cellPais3.setCellValue("Países agregados");
				cellPais3.setCellStyle(cellStyleAnoPaisHeader);
				
				indiceLinhaPais += 4;
				
				Integer[] mesPeriodo = null;
				
				if(anoPeriodo.length == 1) {
					tamanhoArrayMes = (mesFinalInteger - mesInicialInteger) + 1;
				
				} else {
					if(anoPeriodo[j].equals(anoInicialInteger)) {
						tamanhoArrayMes = 12 - mesInicialInteger + 1;
						
					} else if(anoPeriodo[j].equals(anoFinalInteger)) {
						tamanhoArrayMes = mesFinalInteger;
					
					} else if(!anoPeriodo[j].equals(anoInicialInteger) && !anoPeriodo[j].equals(anoFinalInteger)) {
						tamanhoArrayMes = 12;
					}
				}
					
				mesPeriodo = new Integer[tamanhoArrayMes];
					
				if(anoPeriodo[j].equals(anoFinalInteger) && anoPeriodo.length != 1) {
					for(int k=tamanhoArrayMes-1; k>=0; k--) {
						Long valor1 = 0L;
						Long valor2 = 0L;
						Long valor3 = 0L;
						
						for(int i=0; i< paisAgregadoList.size(); i++) {
							mesPeriodo[k] = mesFinalInteger - k;
							
							if(fonte.equalsIgnoreCase("comtrade")) {
								ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
										tipoInt, "76", paisAgregadoList.get(i).getComtradeId().toString(), anoPeriodo[j].toString(), mesPeriodo[k].toString());
								
								if(comtrade != null) {
									valor1 += comtrade.getNetWeight() != null ? comtrade.getNetWeight() : 0L;
									valor2 += comtrade.getTradeValue() != null ? comtrade.getTradeValue() : 0L;
								}
								
							} else if(fonte.equalsIgnoreCase("abics")) {
								AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(anoPeriodo[j], mesPeriodo[k], paisAgregadoList.get(i).getId());
								if(abicsExportacao != null) {
									valor1 += abicsExportacao.getPeso() != null ? abicsExportacao.getPeso() : 0L;
									valor2 += abicsExportacao.getReceita() != null ? abicsExportacao.getReceita() : 0L;
									valor3 += abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg() : 0L;
								}
							}
							
							HSSFCell cellValor = row4ComtradeValores1.createCell(mesFinalInteger-k);
							cellValor.setCellValue(valor1);
							cellValor.setCellStyle(cellStyleDados);
							
							HSSFCell cellValor2 = row4ComtradeValores2.createCell(mesFinalInteger-k);
							cellValor2.setCellValue(valor2);
							cellValor2.setCellStyle(cellStyleDados);
							
							HSSFCell cellValor3 = row4AbicsValores.createCell(mesFinalInteger-k);
							cellValor3.setCellValue(valor3);
							cellValor3.setCellStyle(cellStyleDados);
						}
					}
				} else {
					for(int k=0; k<tamanhoArrayMes; k++) {
						Long valor1 = 0L;
						Long valor2 = 0L;
						Long valor3 = 0L;
						
						for(int i=0; i< paisAgregadoList.size(); i++) {
							HSSFCell cellValor = null;
							HSSFCell cellValor2 = null;
							HSSFCell cellValor3 = null;
							
							if(anoPeriodo[j].equals(anoInicialInteger) || anoPeriodo.length == 1) {
								mesPeriodo[k] = mesInicialInteger + k;
							} else {
								mesPeriodo[k] = k+1;
								cellValor = row4ComtradeValores1.createCell(k+1);
								cellValor2 = row4ComtradeValores2.createCell(k+1);
								cellValor3 = row4AbicsValores.createCell(k+1);
							}
							
							if(fonte.equalsIgnoreCase("comtrade")) {
								ComtradeDataSumarizacao comtrade = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
										tipoInt, "76", paisAgregadoList.get(i).getComtradeId().toString(), anoPeriodo[j].toString(), mesPeriodo[k].toString());
								
								if(comtrade != null) {
									valor1 += comtrade.getNetWeight() != null ? comtrade.getNetWeight() : 0L;
									valor2 += comtrade.getTradeValue() != null ? comtrade.getTradeValue() : 0L;
								}
								
							} else if(fonte.equalsIgnoreCase("abics")) {
								AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(anoPeriodo[j], mesPeriodo[k], paisAgregadoList.get(i).getId());
								if(abicsExportacao != null) {
									valor1 += abicsExportacao.getPeso() != null ? abicsExportacao.getPeso() : 0L;
									valor2 += abicsExportacao.getReceita() != null ? abicsExportacao.getReceita() : 0L;
									valor3 += abicsExportacao.getSaca60kg() != null ? abicsExportacao.getSaca60kg() : 0L;
								}
							}
							
							if(cellValor == null) {
								cellValor = row4ComtradeValores1.createCell(mesInicialInteger+k);
								cellValor2 = row4ComtradeValores2.createCell(mesInicialInteger+k);
								cellValor3 = row4AbicsValores.createCell(mesInicialInteger+k);
							}
							
							cellValor.setCellValue(valor1);
							cellValor2.setCellValue(valor2);
							cellValor3.setCellValue(valor3);
							
							cellValor.setCellStyle(cellStyleDados);
							cellValor2.setCellStyle(cellStyleDados);
							cellValor3.setCellStyle(cellStyleDados);
						}
					}
				}
			}
			
			int numColunasTotal = anoPeriodo.length * tipoCafes.length + 1;
			for(int i=0; i< numColunasTotal; i++) {
				sheetComtrade1.autoSizeColumn(i, true);
				sheetComtrade2.autoSizeColumn(i, true);
				sheetAbics.autoSizeColumn(i, true);
			}
			
			if(fonte.equalsIgnoreCase("comtrade")) {
				wb.removeSheetAt(2);
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
    
    public static Result variacaoAbics(String tipo) {
		
		Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] paisListArr = asFormUrlEncoded.get("pais_list");
		String[] paisList = paisListArr[0].split(",");
		String[] yearListArr = asFormUrlEncoded.get("anos_list");
		String[] yearList = yearListArr[0].split(",");
		String[] periodListArr = asFormUrlEncoded.get("period_list");
		String [] periodList = periodListArr[0].split(",");

		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();

    	for(int i = 0; i < periodList.length; i++) {
    		categories.add(mesList[Integer.parseInt(periodList[i]) -1]);
		}
    	
		graficoItemValorEntity.setCategories(categories);

		Set<String> variacaoDatas = new HashSet<String>();
		
		if(yearList.length > 1) {
			for(int i = 0; i < yearList.length -1; i++) {
				String yearInicio = yearList[yearList.length -1];
				String yearFim = yearList[i];
				variacaoDatas.add(yearInicio + "x" + yearFim);
			}
		}
		
		for(String pais : paisList) {
			Pais paisBd = Pais.findById(Long.valueOf(pais));
			for(String dataInicialDataFinal : variacaoDatas) {
				
				String dataInicial = dataInicialDataFinal.split("x")[1];
				String dataFinal = dataInicialDataFinal.split("x")[0];
				
				List<String> columns = new ArrayList<String>();
				columns.add(paisBd.getNome() + " - " + dataFinal + "x" + dataInicial);
				
				for(String period : periodList) {
					int anoInicialInt = Integer.parseInt(dataInicial);
					int anoFinalInt = Integer.parseInt(dataFinal);
					int mesInt = Integer.parseInt(period);
					AbicsExportacaoPais exportInicialPais = AbicsExportacaoPais.findByAnoMesPaisId(anoInicialInt, mesInt, paisBd.getId());
					AbicsExportacaoPais exportFinalPais = AbicsExportacaoPais.findByAnoMesPaisId(anoFinalInt, mesInt, paisBd.getId());
					
					Double valorInicial = 1.0;
					Double valorFinal = 0.0;
					
					if(exportInicialPais != null) {
						
						if(tipo.equals("saca60kg")) {
							valorInicial = Double.valueOf(exportInicialPais.getSaca60kg());
						} else if(tipo.equals("receita")) {
							valorInicial = Double.valueOf(exportInicialPais.getReceita());
						} else if(tipo.equals("peso")) {
							valorInicial = Double.valueOf(exportInicialPais.getPeso());
						}
					}
					
					if(exportFinalPais != null) {
						
						if(tipo.equals("saca60kg")) {
							valorFinal = Double.valueOf(exportFinalPais.getSaca60kg());
						} else if(tipo.equals("receita")) {
							valorFinal = Double.valueOf(exportFinalPais.getReceita());
						} else if(tipo.equals("peso")) {
							valorFinal = Double.valueOf(exportFinalPais.getPeso());
						}
					}
					
					Double valor = (((double) (valorFinal / valorInicial)) * 100);
					if(!valor.equals(0.0)) {
						valor = valor - 100;
					} 
					if(valorFinal.equals(0.0) && !valorInicial.equals(1.0)) {
						valor = -100.0;
					} else if(!valorFinal.equals(0.0) && valorInicial.equals(1.0)) {
						valor = 100.0;
					}
					columns.add(valor.toString());
					graficoItemValorEntity.addColumns(columns);
				}
			}
		}
		
		return ok(Json.toJson(graficoItemValorEntity));
	}
    
    public static Result variacaoComtrade(String tipo) {
		
		Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] paisListArr = asFormUrlEncoded.get("pais_list");
		String[] paisList = paisListArr[0].split(",");
		String[] yearListArr = asFormUrlEncoded.get("anos_list");
		String[] yearList = yearListArr[0].split(",");
		String[] periodListArr = asFormUrlEncoded.get("period_list");
		String [] periodList = periodListArr[0].split(",");

		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();

    	for(int i = 0; i < periodList.length; i++) {
    		categories.add(mesList[Integer.parseInt(periodList[i]) -1]);
		}
    	
		graficoItemValorEntity.setCategories(categories);

		Set<String> variacaoDatas = new HashSet<String>();
		
		if(yearList.length > 1) {
			for(int i = 0; i < yearList.length -1; i++) {
				String yearInicio = yearList[yearList.length -1];
				String yearFim = yearList[i];
				variacaoDatas.add(yearInicio + "x" + yearFim);
			}
		}
		
		for(String pais : paisList) {
			Pais paisBd = Pais.findById(Long.valueOf(pais));
			for(String dataInicialDataFinal : variacaoDatas) {
				
				String dataInicial = dataInicialDataFinal.split("x")[1];
				String dataFinal = dataInicialDataFinal.split("x")[0];
				
				List<String> columns = new ArrayList<String>();
				columns.add(paisBd.getNome() + " - " + dataFinal + "x" + dataInicial);
				
				for(String period : periodList) {
					Integer anoInicialInt = Integer.parseInt(dataInicial);
					Integer anoFinalInt = Integer.parseInt(dataFinal);
					Integer mesInt = Integer.parseInt(period);
					
					ComtradeDataSumarizacao exportInicialPais = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
							"2", "76", paisBd.getComtradeId().toString(), anoInicialInt.toString(), mesInt.toString());
					ComtradeDataSumarizacao exportFinalPais = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
							"2", "76", paisBd.getComtradeId().toString(), anoFinalInt.toString(), mesInt.toString());
					
					Double valorInicial = 1.0;
					Double valorFinal = 0.0;
					
					if(exportInicialPais != null) {
						
						if(tipo.equals("netweight")) {
							valorInicial = Double.valueOf(exportInicialPais.getNetWeight());
						} else if(tipo.equals("tradevalue")) {
							valorInicial = Double.valueOf(exportInicialPais.getTradeValue());
						}
					}
					
					if(exportFinalPais != null) {
						
						if(tipo.equals("netweight")) {
							valorFinal = Double.valueOf(exportFinalPais.getNetWeight());
						} else if(tipo.equals("tradevalue")) {
							valorFinal = Double.valueOf(exportFinalPais.getTradeValue());
						}
					}
					
					Double valor = (((double) (valorFinal / valorInicial)) * 100);
					if(!valor.equals(0.0)) {
						valor = valor - 100;
					} 
					if(valorFinal.equals(0.0) && !valorInicial.equals(1.0)) {
						valor = -100.0;
					} else if(!valorFinal.equals(0.0) && valorInicial.equals(1.0)) {
						valor = 100.0;
					}
					columns.add(valor.toString());
					graficoItemValorEntity.addColumns(columns);
				}
			}
		}
		
		return ok(Json.toJson(graficoItemValorEntity));
	}
    
}