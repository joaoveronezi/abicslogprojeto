package controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import models.ComtradeDataSumarizacao;
import models.ComtradeDataSumarizacao.TipoValorComtrade;
import models.ComtradePartner;
import play.libs.Json;
import play.mvc.Result;
import util.MonetaryUtil;
import entitys.response.GraficoCategoriesColumnsResponse;

public class AbicsDataComtradeController extends AbstractController {
	
	public static Result findAllReporter() {

		Long [] listaReporterImportado = new Long[]{156L, 276L, 360L,384L,392L,410L,643L,702L,76L,800L,804L,826L,842L};
		List<ComtradePartner> reporterList = new ArrayList<ComtradePartner>();
		
		for(Long idReporter : listaReporterImportado) {
			ComtradePartner reporter = ComtradePartner.findById(idReporter);
			reporterList.add(reporter);
		}
		return ok(Json.toJson(reporterList));
	}
	
	public static Result findAllPartner() {

		List<ComtradePartner> partnerList = ComtradePartner.findAll();
		return ok(Json.toJson(partnerList));
	}
	
	public static Result consultarComparativo(String tipoValorComtrade, String pfCode) {
		
//		$scope.tipoOrigemDestino = 0; // 0)) 1 origem > N destiino  1)) N origens > 1 destino
//		$scope.tipoData = 0; //0 anual 1 mensal
		String[] mesesAno = new String[]{"","jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"};
		Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] anosListArr = asFormUrlEncoded.get("anos_list");
		String[] mesesListArr = asFormUrlEncoded.get("meses_list");
		String[] anoMensalArr = asFormUrlEncoded.get("ano_mensal");
		String[] tipoOrigemDestinoArr = asFormUrlEncoded.get("tipo_origem_destino");
		String[] tipoDataArr = asFormUrlEncoded.get("tipo_data");
//		String[] tipoDataComtradeArr = asFormUrlEncoded.get("tipo_data_comtrade");
		String[] reporterSelecionadoListArr = asFormUrlEncoded.get("reporter_selecionado_list");
		String[] partnerSelecionadoListArr = asFormUrlEncoded.get("partner_selecionado_list");
		
		String[] anosList = anosListArr[0].split(",");
		String[] mesesList = mesesListArr[0].split(",");
		String tipoOrigemDestino = tipoOrigemDestinoArr[0];
		String tipoData = tipoDataArr[0];
		String anoMensal = anoMensalArr[0];
		String[] partnerList = partnerSelecionadoListArr[0].split(",");
		String[] reporterList = reporterSelecionadoListArr[0].split(",");
		ArrayUtils.reverse(anosList);
		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	ArrayList<String> categories = new ArrayList<String>();
    	String[] tipoDataAnoMes = null;
    	if(tipoData.equals("0"))  {
    		for(String ano : anosList) {
    			categories.add(ano);
    		}
    		tipoDataAnoMes = anosList;
    	} else {
    		for(String mes : mesesList) {
    			categories.add(mesesAno[Integer.valueOf(mes)]);
    		}    		
    		tipoDataAnoMes = mesesList;
    	}
    	
		if(tipoOrigemDestino.equals("0")) {
			for(String reporter : reporterList) {
				for(String partner : partnerList) {
					List<String> columns = new ArrayList<String>();
//					columns.add(partner);
					columns = montarColumnsComparativo(reporter, partner, tipoDataAnoMes, tipoData, pfCode, anoMensal, tipoValorComtrade, tipoOrigemDestino);
					graficoItemValorEntity.addColumns(columns);

				}
			}
		} else {
			for(String partner : partnerList) {
				for(String reporter : reporterList) {
					List<String> columns = new ArrayList<String>();
//					columns.add(reporter);
					columns = montarColumnsComparativo(reporter, partner, tipoDataAnoMes, tipoData, pfCode, anoMensal, tipoValorComtrade, tipoOrigemDestino);
					graficoItemValorEntity.addColumns(columns);
				}
			}
		}    	
		graficoItemValorEntity.setCategories(categories);

		return ok(Json.toJson(graficoItemValorEntity));
	}

	private static List<String> montarColumnsComparativo(String reporter, String partner, String[] tipoDataAnoMes,
		String tipoData, String pfCode, String anoMensal, String tipoValorComtrade, String tipoOrigemDestino) {
	
		List<String> columns = new ArrayList<String>();
		ComtradePartner comtradePartner = null;
		if(tipoOrigemDestino.equals("0")) {
			comtradePartner = ComtradePartner.findById(Long.valueOf(partner));
			columns.add(comtradePartner.getText());
		} else {
			comtradePartner = ComtradePartner.findById(Long.valueOf(reporter));
			columns.add(comtradePartner.getText());
		}
		
		for(String anoMes : tipoDataAnoMes) {
			String valorColumns = "0.0";
			ComtradeDataSumarizacao sumarizacao = null;
			if(tipoData.equals("0")) {
				List<ComtradeDataSumarizacao> sumarizacaoList = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYr(
						pfCode, reporter, partner, anoMes);
				if(sumarizacaoList != null && !sumarizacaoList.isEmpty()){
					sumarizacao = sumarizacaoList.get(0);
					
					if(sumarizacao != null) {
						if(sumarizacao.getYr().equals(sumarizacao.getPeriod())) {
							if(tipoValorComtrade.equalsIgnoreCase(TipoValorComtrade.NETWEIGHT.getDescricao())
									 && sumarizacao.getNetWeight() != null) {
								valorColumns = String.valueOf(sumarizacao.getNetWeight());
							} else if(tipoValorComtrade.equalsIgnoreCase(TipoValorComtrade.TRADEVALUE.getDescricao())
									&& sumarizacao.getTradeValue() != null) {
								valorColumns = String.valueOf(sumarizacao.getTradeValue());
							} else if(tipoValorComtrade.equalsIgnoreCase(TipoValorComtrade.SACA60KG.getDescricao())
									&& sumarizacao.getTradeValue() != null && sumarizacao.getNetWeight() != null) {
								BigDecimal preco = MonetaryUtil.divide(new BigDecimal(sumarizacao.getTradeValue()), 
										new BigDecimal((sumarizacao.getNetWeight())));
								BigDecimal precoSacaDolar = MonetaryUtil.multiply(preco, new BigDecimal(60));
								valorColumns = String.valueOf(precoSacaDolar);
							}
						}
					}
				}
			} else {
				sumarizacao = ComtradeDataSumarizacao.findByTradeCodeAndReportCodeAndPartnerAndYrAndPeriod(
						pfCode, reporter, partner, anoMensal, anoMes);
				if(sumarizacao != null) {
					if(tipoValorComtrade.equalsIgnoreCase(TipoValorComtrade.NETWEIGHT.getDescricao())
							 && sumarizacao.getNetWeight() != null) {
						valorColumns = String.valueOf(sumarizacao.getNetWeight());
					} else if(tipoValorComtrade.equalsIgnoreCase(TipoValorComtrade.TRADEVALUE.getDescricao())
							&& sumarizacao.getTradeValue() != null) {
						valorColumns = String.valueOf(sumarizacao.getTradeValue());
					} else if(tipoValorComtrade.equalsIgnoreCase(TipoValorComtrade.SACA60KG.getDescricao())
							&& sumarizacao.getTradeValue() != null && sumarizacao.getNetWeight() != null) {
						BigDecimal preco = MonetaryUtil.divide(new BigDecimal(sumarizacao.getTradeValue()), 
								new BigDecimal((sumarizacao.getNetWeight())));
						BigDecimal precoSacaDolar = MonetaryUtil.multiply(preco, new BigDecimal(60));
						valorColumns = String.valueOf(precoSacaDolar);
					}
				}
			}
			
			columns.add(valorColumns);
		}
		return columns;
	}
	
}