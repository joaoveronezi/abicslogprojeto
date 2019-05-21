package controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.AbicsEmpresa;
import models.AbicsExportacaoEmpresa;
import models.AbicsExportacaoProduto;
import models.AbicsProduto;
import play.libs.Json;
import play.mvc.Result;
import entitys.response.GraficoCategoriesColumnsResponse;

public class AbicsDataEmpresaProdutoController extends AbstractController {

	private static final String[] mesesAno = new String[]{"jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"};
	
	public static Result findAllAbicsEmpresa() {

		List<AbicsEmpresa> list = AbicsEmpresa.findAll();
		
		for(AbicsEmpresa empresa : list) {
			
		}
		
		return ok(Json.toJson(list));
	}
	
	public static Result findAllAbicsProduto() {

		List<AbicsProduto> list = AbicsProduto.findAll();
		
		for(AbicsProduto produto : list) {
			
		}
		
		return ok(Json.toJson(list));
	}
    
	public static Result graficoAbicsEmpresa(String tipo) {
		
		Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] empresaListArr = asFormUrlEncoded.get("empresa_list");
		String[] empresaList = empresaListArr[0].split(",");
		String[] yearListArr = asFormUrlEncoded.get("anos_list");
		String[] yearList = yearListArr[0].split(",");
		
		String [] periodList = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();

    	for(int i = 0; i < periodList.length; i++) {
//			categories.add(periodList[i]);
//		}
//		for(String mes : mesesList) {
			categories.add(mesesAno[i]);
		}    
		graficoItemValorEntity.setCategories(categories);

		for(String empresa : empresaList) {

			AbicsEmpresa abicsEmpresa = AbicsEmpresa.findById(Long.valueOf(empresa));
			for(String year : yearList) {
				List<String> columns = new ArrayList<String>();
				columns.add(abicsEmpresa.getNome() + " - " + year);
				
//				AbicsPais paisAbics = AbicsPais.findById(comtradePartner.getAbicsPaisId());
//				
//				if(paisAbics != null) {
					
					for(String period : periodList) {
						int anoInt = Integer.parseInt(year);
						int mesInt = Integer.parseInt(period);
						AbicsExportacaoEmpresa exportEmpresa = AbicsExportacaoEmpresa.findByAnoMesEmpresaId(anoInt, mesInt, abicsEmpresa.getId());
						String valor = "0";
						if(exportEmpresa != null) {
							
							if(tipo.equals("saca60kg")) {
								valor = String.valueOf(exportEmpresa.getSaca60kg());
		//						columns.add(String.valueOf(exportPais.getSaca60kg()));
							} else if(tipo.equals("receita")) {
								valor = String.valueOf(exportEmpresa.getReceita());
		//						columns.add(String.valueOf(exportPais.getReceita()));
							} else if(tipo.equals("peso")) {
								valor = String.valueOf(exportEmpresa.getPeso());
		//						columns.add(String.valueOf(exportPais.getPeso()));
							}
						}
						
						columns.add(valor);
//					}
		
					graficoItemValorEntity.addColumns(columns);
				}
			}
		}

		return ok(Json.toJson(graficoItemValorEntity));
	}
	
	public static Result variacaoAbicsEmpresa(String tipo) {
		
		Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] empresaListArr = asFormUrlEncoded.get("empresa_list");
		String[] empresaList = empresaListArr[0].split(",");
		String[] yearListArr = asFormUrlEncoded.get("anos_list");
		String[] yearList = yearListArr[0].split(",");
		
		String [] periodList = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();

    	for(int i = 0; i < periodList.length; i++) {
//			categories.add(periodList[i]);
    		categories.add(mesesAno[i]);
		}
		graficoItemValorEntity.setCategories(categories);

		Set<String> variacaoDatas = new HashSet<String>();
		
		if(yearList.length > 1) {
			for(int i = 0; i <= yearList.length; i++) {
				for(int j = i+1; j < yearList.length; j++) {
					String yearInicio = yearList[i];
					String yearFim = yearList[j];
					variacaoDatas.add(yearInicio + "x" + yearFim);
//					System.out.println(yearInicio + "x" + yearFim);
				}
			}
		}
		
		for(String empresa : empresaList) {

			AbicsEmpresa abicsEmpresa = AbicsEmpresa.findById(Long.valueOf(empresa));
			for(String dataInicialDataFinal : variacaoDatas) {
				
				String dataInicial = dataInicialDataFinal.split("x")[1];
				String dataFinal = dataInicialDataFinal.split("x")[0];
				
				List<String> columns = new ArrayList<String>();
				columns.add(abicsEmpresa.getNome() + " - " + dataFinal + "x" + dataInicial);
				
//				columns.add(abicsEmpresa.getNome() + " - " + year);
				
//				AbicsPais paisAbics = AbicsPais.findById(comtradePartner.getAbicsPaisId());
//				
//				if(paisAbics != null) {
					
					for(String period : periodList) {
						int anoInicialInt = Integer.parseInt(dataInicial);
						int anoFinalInt = Integer.parseInt(dataFinal);
						int mesInt = Integer.parseInt(period);
						AbicsExportacaoEmpresa exportInicialEmpresa = AbicsExportacaoEmpresa.findByAnoMesEmpresaId(anoInicialInt, mesInt, abicsEmpresa.getId());
						AbicsExportacaoEmpresa exportFinalEmpresa = AbicsExportacaoEmpresa.findByAnoMesEmpresaId(anoFinalInt, mesInt, abicsEmpresa.getId());
						
//						AbicsExportacaoPais exportInicialPais = AbicsExportacaoPais.findByAnoMesPaisId(anoInicialInt, mesInt, paisAbics.getId());
//						AbicsExportacaoPais exportFinalPais = AbicsExportacaoPais.findByAnoMesPaisId(anoFinalInt, mesInt, paisAbics.getId());
						
						Double valorInicial = 1.0;
						Double valorFinal = 0.0;
						
//						int anoInt = Integer.parseInt(year);
//						int mesInt = Integer.parseInt(period);
						
//						String valor = "0";
						if(exportInicialEmpresa != null) {
							
							if(tipo.equals("saca60kg")) {
								valorInicial = Double.valueOf(exportInicialEmpresa.getSaca60kg());
							} else if(tipo.equals("receita")) {
								valorInicial = Double.valueOf(exportInicialEmpresa.getReceita());
							} else if(tipo.equals("peso")) {
								valorInicial = Double.valueOf(exportInicialEmpresa.getPeso());
							}
						}
						
						if(exportFinalEmpresa != null) {
							
							if(tipo.equals("saca60kg")) {
								valorFinal = Double.valueOf(exportFinalEmpresa.getSaca60kg());
							} else if(tipo.equals("receita")) {
								valorFinal = Double.valueOf(exportFinalEmpresa.getReceita());
							} else if(tipo.equals("peso")) {
								valorFinal = Double.valueOf(exportFinalEmpresa.getPeso());
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
//						System.out.println(period + " >> " + anoInicialInt + "/" + anoFinalInt + " : "+ valorInicial + "-" + valorFinal + 
//								" valor:: " + valor);
						columns.add(valor.toString());
//					}
		
					graficoItemValorEntity.addColumns(columns);
				}
			}
		}
		
		return ok(Json.toJson(graficoItemValorEntity));
	}
	
	public static Result graficoAbicsProduto(String tipo) {
	
		Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] produtoListArr = asFormUrlEncoded.get("produto_list");
		String[] produtoList = produtoListArr[0].split(",");
		String[] yearListArr = asFormUrlEncoded.get("anos_list");
		String[] yearList = yearListArr[0].split(",");
		
		String [] periodList = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();

    	for(int i = 0; i < periodList.length; i++) {
//			categories.add(periodList[i]);
    		categories.add(mesesAno[i]);
		}
		graficoItemValorEntity.setCategories(categories);
		
		for(String produto : produtoList) {

			AbicsProduto abicsProduto = AbicsProduto.findById(Long.valueOf(produto));
			for(String year : yearList) {
				
				List<String> columns = new ArrayList<String>();
				columns.add(abicsProduto.getNome() + " - " + year);
				
//				AbicsPais paisAbics = AbicsPais.findById(comtradePartner.getAbicsPaisId());
//				
//				if(paisAbics != null) {
					
					for(String period : periodList) {
						int anoInt = Integer.parseInt(year);
						int mesInt = Integer.parseInt(period);
						AbicsExportacaoProduto exportProduto = AbicsExportacaoProduto.findByAnoMesProdutoId(anoInt, mesInt, abicsProduto.getId());
						String valor = "0";
						if(exportProduto != null) {
							
							if(tipo.equals("saca60kg")) {
								valor = String.valueOf(exportProduto.getSaca60kg());
		//						columns.add(String.valueOf(exportPais.getSaca60kg()));
							} else if(tipo.equals("receita")) {
								valor = String.valueOf(exportProduto.getReceita());
		//						columns.add(String.valueOf(exportPais.getReceita()));
							} else if(tipo.equals("peso")) {
								valor = String.valueOf(exportProduto.getPeso());
		//						columns.add(String.valueOf(exportPais.getPeso()));
							}
						}
						
						columns.add(valor);
//					}
		
					graficoItemValorEntity.addColumns(columns);
				}
			}
		}

		return ok(Json.toJson(graficoItemValorEntity));
	}
	
	public static Result variacaoAbicsProduto(String tipo) {
		
		Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] produtoListArr = asFormUrlEncoded.get("produto_list");
		String[] produtoList = produtoListArr[0].split(",");
		String[] yearListArr = asFormUrlEncoded.get("anos_list");
		String[] yearList = yearListArr[0].split(",");
		
		String [] periodList = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();

    	for(int i = 0; i < periodList.length; i++) {
//			categories.add(periodList[i]);
    		categories.add(mesesAno[i]);
		}
		graficoItemValorEntity.setCategories(categories);

		Set<String> variacaoDatas = new HashSet<String>();
		
		if(yearList.length > 1) {
			for(int i = 0; i <= yearList.length; i++) {
				for(int j = i+1; j < yearList.length; j++) {
					String yearInicio = yearList[i];
					String yearFim = yearList[j];
					variacaoDatas.add(yearInicio + "x" + yearFim);
//					System.out.println(yearInicio + "x" + yearFim);
				}
			}
		}
		
		for(String produto : produtoList) {

			AbicsProduto abicsProduto = AbicsProduto.findById(Long.valueOf(produto));
			for(String dataInicialDataFinal : variacaoDatas) {
				
				String dataInicial = dataInicialDataFinal.split("x")[1];
				String dataFinal = dataInicialDataFinal.split("x")[0];
				
				List<String> columns = new ArrayList<String>();
				columns.add(abicsProduto.getNome() + " - " + dataFinal + "x" + dataInicial);
				
//				columns.add(abicsEmpresa.getNome() + " - " + year);
				
//				AbicsPais paisAbics = AbicsPais.findById(comtradePartner.getAbicsPaisId());
//				
//				if(paisAbics != null) {
					
					for(String period : periodList) {
						int anoInicialInt = Integer.parseInt(dataInicial);
						int anoFinalInt = Integer.parseInt(dataFinal);
						int mesInt = Integer.parseInt(period);
						AbicsExportacaoProduto exportInicialEmpresa = AbicsExportacaoProduto.findByAnoMesProdutoId(anoInicialInt, mesInt, abicsProduto.getId());
						AbicsExportacaoProduto exportFinalEmpresa = AbicsExportacaoProduto.findByAnoMesProdutoId(anoFinalInt, mesInt, abicsProduto.getId());
						
//						AbicsExportacaoPais exportInicialPais = AbicsExportacaoPais.findByAnoMesPaisId(anoInicialInt, mesInt, paisAbics.getId());
//						AbicsExportacaoPais exportFinalPais = AbicsExportacaoPais.findByAnoMesPaisId(anoFinalInt, mesInt, paisAbics.getId());
						
						Double valorInicial = 1.0;
						Double valorFinal = 0.0;
						
//						int anoInt = Integer.parseInt(year);
//						int mesInt = Integer.parseInt(period);
						
//						String valor = "0";
						if(exportInicialEmpresa != null) {
							
							if(tipo.equals("saca60kg")) {
								valorInicial = Double.valueOf(exportInicialEmpresa.getSaca60kg());
							} else if(tipo.equals("receita")) {
								valorInicial = Double.valueOf(exportInicialEmpresa.getReceita());
							} else if(tipo.equals("peso")) {
								valorInicial = Double.valueOf(exportInicialEmpresa.getPeso());
							}
						}
						
						if(exportFinalEmpresa != null) {
							
							if(tipo.equals("saca60kg")) {
								valorFinal = Double.valueOf(exportFinalEmpresa.getSaca60kg());
							} else if(tipo.equals("receita")) {
								valorFinal = Double.valueOf(exportFinalEmpresa.getReceita());
							} else if(tipo.equals("peso")) {
								valorFinal = Double.valueOf(exportFinalEmpresa.getPeso());
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
						}//						System.out.println(period + " >> " + anoInicialInt + "/" + anoFinalInt + " : "+ valorInicial + "-" + valorFinal + 
//								" valor:: " + valor);
						columns.add(valor.toString());
//					}
		
					graficoItemValorEntity.addColumns(columns);
				}
			}
		}
		
		return ok(Json.toJson(graficoItemValorEntity));
	}
}