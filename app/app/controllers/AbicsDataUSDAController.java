package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.USDAData;

import org.apache.commons.lang3.ArrayUtils;

import play.libs.Json;
import play.mvc.Result;
import entitys.response.GraficoCategoriesColumnsResponse;

public class AbicsDataUSDAController extends AbstractController {

	public static Result findDistinctAno() {
		
		List<Long> distinctAnos = USDAData.distinctAnosMaior(1990, "desc");//USDAData.distinctAnos();
		return ok(Json.toJson(distinctAnos));
	}
	
	public static Result findDistinctPais() {
		
		List<String> distinctPais = USDAData.distinctPais();
		return ok(Json.toJson(distinctPais));
	}
	
	public static Result graficoUSDA(String tipo) {
		
		Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] paisListArr = asFormUrlEncoded.get("pais_list");
		String[] paisList = paisListArr[0].split(",");
		String[] yearListArr = asFormUrlEncoded.get("anos_list");
		String[] yearList = yearListArr[0].split(",");
		String[] commodityArr = asFormUrlEncoded.get("commodity");
		String commodity = commodityArr[0];
		
		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();

    	ArrayUtils.reverse(yearList);
    	
    	for(int i = 0; i < yearList.length; i++) {
			categories.add(yearList[i]);
		}    
		graficoItemValorEntity.setCategories(categories);

		for(String pais : paisList) {

			List<String> columns = new ArrayList<String>();
			columns.add(pais);
			
			for(String year : yearList) {
				
				USDAData usdaData = USDAData.findUniqueByCommodityPaisAnoTipo(commodity,pais,year,tipo);
			
					String valor = "0";
					if(usdaData != null) {
						
						valor = String.valueOf(usdaData.getQuantidade());
					}
					
					columns.add(valor);
	
				graficoItemValorEntity.addColumns(columns);
			}
		}

		return ok(Json.toJson(graficoItemValorEntity));
	}
}