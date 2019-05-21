package controllers;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import models.Pais;
import models.RelPaisAcordoComercial;

import org.apache.commons.io.IOUtils;

import play.libs.Json;
import play.mvc.Result;
import util.AbicsLogConfig;

public class AbicsDataController extends AbstractController {

	public static Result findAcordoComercial(String data, Long paisId) {
		
		String mes = data.split("-")[0];
		String ano = data.split("-")[1];
		
		List<RelPaisAcordoComercial> list = RelPaisAcordoComercial.findByMesAnoPais(mes, ano, paisId);
		return ok(Json.toJson(list));
	}
	
	public static Result findPaisAcordoComercial(String data) {
		
		String mes = data.split("-")[0];
		String ano = data.split("-")[1];

		List<Pais> paisResponse = new ArrayList<Pais>();
		List<String> paisIdList = RelPaisAcordoComercial.distinctPais(mes, ano);
		
		for(String idPais : paisIdList) {
			paisResponse.add(Pais.findById(Long.valueOf(idPais)));
		}
		return ok(Json.toJson(paisResponse));
	}
	
	public static Result downloadPlanilhaAcordoComercial(String data) {

		try {
			String mes = data.split("-")[0];
			String ano = data.split("-")[1];

//			String excelFileName = "/home/fabricio/Desktop/abics_data/abics/09-2016-Abics-Tabelas de Acordo.xlsx";//
			String excelFileName = AbicsLogConfig.getString(AbicsLogConfig.ABICS_DATA_FOLDER) + data + ".xlsx";
			
//			FileOutputStream fileOut = new FileOutputStream(excelFileName);
//	
//			//write this workbook to an Outputstream.
//			wb.write(fileOut);
//			fileOut.flush();
//			fileOut.close();
			
	//		FileUtil.salvarImagem(content, excelFileName);
			
			byte[] contents = IOUtils.toByteArray(new FileInputStream(excelFileName));
			return ok(contents);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
//		assets/data
		return ok();
	}
	
	
}