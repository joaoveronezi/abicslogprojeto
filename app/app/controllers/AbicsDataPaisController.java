package controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.AbicsExportacaoPais;
import models.AbicsPais;
import play.libs.Json;
import play.mvc.Result;
import entitys.response.GraficoCategoriesColumnsResponse;

public class AbicsDataPaisController extends AbstractController {

	private static final String[] mesesAno = new String[]{"jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"};
	
	public static Result findAllPais() {
		
		List<AbicsPais> list = AbicsPais.findAll();
		return ok(Json.toJson(list));
	}
	
	public static Result grafico(String tipo) {
		
		Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
//		String[] partnerListArr = asFormUrlEncoded.get("partner_selecionado_list");
		String[] yearArr = asFormUrlEncoded.get("ano_selecionado_list");
		String[] yearList = yearArr[0].split(",");
		String[] partnerList = null;//new String[]{};//partnerListArr[0].split(",");
//		String[] showSelecionarPaisesArr = asFormUrlEncoded.get("show_selecionar_paises");
//		String showSelecionarPaises = showSelecionarPaisesArr[0];
		
		String [] periodList = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();

    	for(int i = 0; i < periodList.length; i++) {
//			categories.add(periodList[i]);
    		categories.add(mesesAno[i]);
		}
		graficoItemValorEntity.setCategories(categories);

//		if(showSelecionarPaises.equals("1")) {
			String[] partnerListArr = asFormUrlEncoded.get("pais_selecionado_list");
			partnerList = partnerListArr[0].split(",");
//			formData.append('partner_selecionado_list', $scope.partnerSelecionadoList);
//		} else {
//			String[] qteMaioresPaisesArr = asFormUrlEncoded.get("qte_maiores_paises");
//			String qteMaioresPaises = qteMaioresPaisesArr[0];
//			Set<String> idPartnerUnique = new HashSet<String>();
////			for(String partner : partnerList) {
////
////				ComtradePartner comtradePartner = ComtradePartner.findById(Long.valueOf(partner));
//				partnerList = new String[50];
//				for(String year : yearList) {
//					List<Long> comtradeList = AbicsExportacaoPais.getMaioresReceitaAno(year, Integer.valueOf(qteMaioresPaises));
//					if(comtradeList != null) {
//						for(int i = 0; i < comtradeList.size();i++) {
//							
//							ComtradePartner comtradePartner = ComtradePartner.findByPaisAbicsId(Long.valueOf(comtradeList.get(i).toString()));
//							if(idPartnerUnique.add(comtradePartner.getId().toString())) {
//								partnerList[i] = comtradePartner.getId().toString();
//							}
//						}
//					}
//				}
////			}
//			
////			formData.append('qte_maiores_paises', $scope.qteMaioresPaises);
//		}
		
		for(String partner : partnerList) {

			if(partner != null) {
				AbicsPais abicsPais = AbicsPais.findById(Long.valueOf(partner));
//				if(comtradePartner != null) {
					
					for(String year : yearList) {
						
						List<String> columns = new ArrayList<String>();
						columns.add(abicsPais.getNome() + " - " + year);
						
						AbicsPais paisAbics = AbicsPais.findById(Long.valueOf(partner));
						
						if(paisAbics != null) {
							
							for(String period : periodList) {
								int anoInt = Integer.parseInt(year);
								int mesInt = Integer.parseInt(period);
								AbicsExportacaoPais exportPais = AbicsExportacaoPais.findByAnoMesPaisId(anoInt, mesInt, paisAbics.getId());
								String valor = "0";
								if(exportPais != null) {
									
									if(tipo.equals("equivalenteSaca60kg")) {
										valor = String.valueOf(exportPais.getSaca60kg());
									} else if(tipo.equals("receita")) {
										valor = String.valueOf(exportPais.getReceita());
									} else if(tipo.equals("peso")) {
										valor = String.valueOf(exportPais.getPeso());
									}
								}
								
								columns.add(valor);
							}
							
							graficoItemValorEntity.addColumns(columns);
						}
					}
//				}
			}
		}

		return ok(Json.toJson(graficoItemValorEntity));
	}

	
	public static Result variacao(String tipo) {
		
		Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
//		String[] partnerListArr = asFormUrlEncoded.get("partner_selecionado_list");
		String[] yearArr = asFormUrlEncoded.get("ano_selecionado_list");
		String[] yearList = yearArr[0].split(",");
		String[] partnerList = null;//partnerListArr[0].split(",");
//		String[] showSelecionarPaisesArr = asFormUrlEncoded.get("show_selecionar_paises");
//		String showSelecionarPaises = showSelecionarPaisesArr[0];
		
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
		
//		if(showSelecionarPaises.equals("1")) {
			String[] partnerListArr = asFormUrlEncoded.get("pais_selecionado_list");
			partnerList = partnerListArr[0].split(",");
//			formData.append('partner_selecionado_list', $scope.partnerSelecionadoList);
//		} else {
//			String[] qteMaioresPaisesArr = asFormUrlEncoded.get("qte_maiores_paises");
//			String qteMaioresPaises = qteMaioresPaisesArr[0];
//			Set<String> idPartnerUnique = new HashSet<String>();
////			for(String partner : partnerList) {
////
////				ComtradePartner comtradePartner = ComtradePartner.findById(Long.valueOf(partner));
//				partnerList = new String[50];
//				for(String year : yearList) {
//					List<Long> comtradeList = AbicsExportacaoPais.getMaioresReceitaAno(year, Integer.valueOf(qteMaioresPaises));
//					if(comtradeList != null) {
//						for(int i = 0; i < comtradeList.size();i++) {
//							
//							ComtradePartner comtradePartner = ComtradePartner.findByPaisAbicsId(Long.valueOf(comtradeList.get(i).toString()));
//							if(idPartnerUnique.add(comtradePartner.getId().toString())) {
//								partnerList[i] = comtradePartner.getId().toString();
//							}
//						}
//					}
//				}
////			}
//			
////			formData.append('qte_maiores_paises', $scope.qteMaioresPaises);
//		}
		
		for(String partner : partnerList) {

			if(partner != null) {
//				ComtradePartner comtradePartner = ComtradePartner.findById(Long.valueOf(partner));
//				if(comtradePartner != null) {
					
					for(String dataInicialDataFinal : variacaoDatas) {
						
						String dataInicial = dataInicialDataFinal.split("x")[1];
						String dataFinal = dataInicialDataFinal.split("x")[0];
						
//						System.out.println(dataInicial + "dii");
//						System.out.println(dataFinal + " FIMFIFM");
						
						AbicsPais paisAbics = AbicsPais.findById(Long.valueOf(partner));
						
						if(paisAbics != null) {
							List<String> columns = new ArrayList<String>();
							columns.add(paisAbics.getNome() + " - " + dataFinal + "x" + dataInicial);
							for(String period : periodList) {
								
								int anoInicialInt = Integer.parseInt(dataInicial);
								int anoFinalInt = Integer.parseInt(dataFinal);
								int mesInt = Integer.parseInt(period);
								AbicsExportacaoPais exportInicialPais = AbicsExportacaoPais.findByAnoMesPaisId(anoInicialInt, mesInt, paisAbics.getId());
								AbicsExportacaoPais exportFinalPais = AbicsExportacaoPais.findByAnoMesPaisId(anoFinalInt, mesInt, paisAbics.getId());
								
								Double valorInicial = 1.0;
								Double valorFinal = 0.0;
								if(exportInicialPais != null) {
									
									if(tipo.equals("equivalenteSaca60kg")) {
										valorInicial = Double.valueOf(exportInicialPais.getSaca60kg());
									} else if(tipo.equals("receita")) {
										valorInicial = Double.valueOf(exportInicialPais.getReceita());
									} else if(tipo.equals("peso")) {
										valorInicial = Double.valueOf(exportInicialPais.getPeso());
									}
								}
		
								if(exportFinalPais != null) {
									
									if(tipo.equals("equivalenteSaca60kg")) {
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
//								System.out.println(period + " >> " + anoInicialInt + "/" + anoFinalInt + " : "+ valorInicial + "-" + valorFinal + 
//										" valor:: " + valor);
								columns.add(valor.toString());
							}
				
							graficoItemValorEntity.addColumns(columns);
						}
					}
//				}
			}
		}

		return ok(Json.toJson(graficoItemValorEntity));
	}    
}