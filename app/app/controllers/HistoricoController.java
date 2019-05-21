package controllers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import models.Cambio;
import models.Cotacao;
import models.RelValorMercadoriaCotacao;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import util.AbicsLogConfig;
import util.DataUtil;
import util.ExcelUtil;
import util.FileUtil;
import util.MonetaryUtil;
import util.ReportUtil;
import entitys.response.GraficoCategoriesColumnsResponse;

public class HistoricoController extends AbstractController {

	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");
	private final static Calendar calendar31Agosto = Calendar.getInstance();
	
	public static Result relatorioCompleto() {
		
		calendar31Agosto.set(2016, 7, 31);
		
    	DynamicForm requestData = Form.form().bindFromRequest();
    	Map<String, String> parametrosRequestMap = requestData.data();
    	String dataInicialStr = parametrosRequestMap.get("data_inicial");
    	String dataFinalStr = parametrosRequestMap.get("data_final");
    	String aplicarLogisticaStr = parametrosRequestMap.get("aplicar_logistica");
    	String tipoMoedaStr = parametrosRequestMap.get("tipo_moeda");

		Boolean aplicarLogistica = Boolean.valueOf(aplicarLogisticaStr);
		Long dataInicialUnix = Long.valueOf(dataInicialStr); //0L;
		Long dataFinalUnix = Long.valueOf(dataFinalStr);//0L;
		List<Cotacao> cotacoes = Cotacao.findAtivo(1);//new ArrayList<Cotacao>();
		
		
		Calendar calendarInicial = Calendar.getInstance();
		calendarInicial.setTimeInMillis(dataInicialUnix);
		
		Calendar calendarFinal = Calendar.getInstance();
		calendarFinal.setTimeInMillis(dataFinalUnix);
		
		Date firstDate = calendarInicial.getTime();
     	Date secondDate = calendarFinal.getTime();

        long diff = secondDate.getTime() - firstDate.getTime();
		Long qteDiasMes =  diff / (24 * 60 * 60 * 1000);

		GraficoCategoriesColumnsResponse graficoItemValorEntity = new GraficoCategoriesColumnsResponse();
    	List<String> categories = new ArrayList<String>();
    	
    	Calendar calendarCategories = Calendar.getInstance();
    	calendarCategories.setTimeInMillis(dataInicialUnix);
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
			    if(valorMercadoriaCotacao != null && valorMercadoriaCotacao.getValorCustoImportacaoReal() != null) {
			    	
			    	Cambio cambio = Cambio.findByData(valorMercadoriaCotacao.getData());
			    	if(aplicarLogistica) {
			    		if(tipoMoedaStr.equals("real")) {
			    			valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoReal();	
			    		} else if(tipoMoedaStr.equals("dolar")) {
			    			valorMercadoria = valorMercadoriaCotacao.getValorCustoImportacaoDolar();
			    		}
			    	} else {
			    		if(tipoMoedaStr.equals("real")) {
			    			valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaReal();	
			    		} else if(tipoMoedaStr.equals("dolar")) {
			    			valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaDolar();
			    		}
			    	}
			    }
			    
				if(cotacao.getFlagEspiritoSanto() == 1) {
					BigDecimal novoValor = new BigDecimal(0.0);
		    		String valueOld = mediaEspiritoSanto.get(i);
		    		novoValor = MonetaryUtil.add(new BigDecimal(valueOld), MonetaryUtil.divide(valorMercadoria, new BigDecimal(2)));
		    		mediaEspiritoSanto.set(i, String.valueOf(novoValor));
				} else {
					if(calendarCategories.getTime().after(calendar31Agosto.getTime()) && cotacao.getId() == 7L) {
						continue;
					}
			        
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
	
	public static Result imprimirGrafico() {
		
		Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		String[] strings = asFormUrlEncoded.get("grafico_byte_array");
		String[] data_inicial = asFormUrlEncoded.get("data_inicial");
		String[] data_final = asFormUrlEncoded.get("data_final");
		String[] aplicar_logisticaArr = asFormUrlEncoded.get("aplicar_logistica");
		String[] tipo_moedaArr = asFormUrlEncoded.get("tipo_moeda");
		String aplicarLogistica = "Com custo logístico.";
		String tipoMoeda = "Real";
		if(aplicar_logisticaArr != null && aplicar_logisticaArr[0] != null && !aplicar_logisticaArr[0].isEmpty()) {
			
			if(aplicar_logisticaArr[0].equalsIgnoreCase("false")) {
				aplicarLogistica = "Sem custo logístico";
			}
		}
		
		if(tipo_moedaArr != null && tipo_moedaArr[0] != null && !tipo_moedaArr[0].isEmpty()) {
			if(tipo_moedaArr[0].equalsIgnoreCase("dolar")) {
				tipoMoeda = "Dolar";
			}
		}
		
		String content = strings[0];
		content = content.replace("data:image/png;base64,", "");
		
		String caminhoPdf = AbicsLogConfig.getString(AbicsLogConfig.USER_REPORT_FOLDER) + getUsuarioLogado().getId() + "/graficoHistorico.pdf";
		String imagemGraficoPNG = AbicsLogConfig.getString(AbicsLogConfig.USER_REPORT_FOLDER)+ getUsuarioLogado().getId() + "/imagemGrafico.png";
		FileUtil.salvarImagem(content, imagemGraficoPNG);
		
		
		Long dataInicialUnixtime = Long.valueOf(data_inicial[0]);
		Long dataFinalUnixtime = Long.valueOf(data_final[0]);
		dataInicialUnixtime = dataInicialUnixtime / 1000;
		dataFinalUnixtime = dataFinalUnixtime / 1000;
		
		InputStream inputStream = ReportUtil.gerarRelatorioHistorico(DataUtil.formatDateToBR(DataUtil.fromUnixTimeSemLocale(dataInicialUnixtime)), 
				DataUtil.formatDateToBR(DataUtil.fromUnixTimeSemLocale(dataFinalUnixtime)), aplicarLogistica, tipoMoeda, imagemGraficoPNG, caminhoPdf);

		try {
			byte[] contents = IOUtils.toByteArray(inputStream);
			return ok(contents);
		} catch (IOException e) {
			e.printStackTrace();
			byte [] contents = new byte []{};
//			return ok(contents);
			return ok();
		}
	}
	
	public static Result imprimirRelatorio() {
		
		try {
			
	    	DynamicForm requestData = Form.form().bindFromRequest();
	    	Map<String, String> parametrosRequestMap = requestData.data();
	    	String dataInicialStr = parametrosRequestMap.get("data_inicial");
	    	String dataFinalStr = parametrosRequestMap.get("data_final");
	    	String aplicarLogisticaStr = parametrosRequestMap.get("aplicar_logistica");
			String tipoMoedaStr = parametrosRequestMap.get("tipo_moeda");
			String simboloMoeda = "";
			
			if(tipoMoedaStr.equals("real")) {
				simboloMoeda = "R$";
			} else {
				simboloMoeda = "US$";
			}
			
			List<Cotacao> cotacaoList = Cotacao.findAllNacional(0);
			
			Boolean aplicarLogistica = Boolean.valueOf(aplicarLogisticaStr);
			Long dataInicialUnix = Long.valueOf(dataInicialStr); //0L;
			Long dataFinalUnix = Long.valueOf(dataFinalStr);//0L;
			
			Calendar calendarInicial = Calendar.getInstance();
			calendarInicial.setTimeInMillis(dataInicialUnix);
			
			Calendar calendarFinal = Calendar.getInstance();
			calendarFinal.setTimeInMillis(dataFinalUnix);
			
			Date firstDate = calendarInicial.getTime();
	     	Date secondDate = calendarFinal.getTime();

	        long diff = secondDate.getTime() - firstDate.getTime();
			Long qteDiasMes =  diff / (24 * 60 * 60 * 1000);
			
			String excelFileName = AbicsLogConfig.getString(AbicsLogConfig.USER_REPORT_FOLDER) + getUsuarioLogado().getId() 
					+"/relatorioComparativo.xlsx";
	
			String sheetName = "Relatorio Comparativo";//name of sheet
	
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet(sheetName);
			CellStyle cellStyleBlue = ExcelUtil.createStyle(wb, IndexedColors.GREY_25_PERCENT.getIndex());
			CellStyle cellStyleGreen = ExcelUtil.createStyle(wb, IndexedColors.LIGHT_GREEN.getIndex());
			CellStyle cellStyleYellow = ExcelUtil.createStyle(wb, IndexedColors.LIGHT_YELLOW.getIndex());
			CellStyle cellStyleHeaderBlue = ExcelUtil.createStyleHeader(wb, IndexedColors.GREY_25_PERCENT.getIndex());
			CellStyle cellStyleHeaderGreen = ExcelUtil.createStyleHeader(wb, IndexedColors.LIGHT_GREEN.getIndex());
			CellStyle cellStyleHeaderYellow = ExcelUtil.createStyleHeader(wb, IndexedColors.LIGHT_YELLOW.getIndex());
			CellStyle cellStyleWhite = ExcelUtil.createStyle(wb, IndexedColors.WHITE.getIndex());
			
			HSSFRow row0 = sheet.createRow(0);
			HSSFCell cellOrigem = row0.createCell(0);
			cellOrigem.setCellValue("Origem");
			cellOrigem.setCellStyle(cellStyleHeaderBlue);
			HSSFCell cellVietnam = row0.createCell(1);
			cellVietnam.setCellValue("Vietnam");
			cellVietnam.setCellStyle(cellStyleHeaderGreen);
			HSSFCell cellIndonesia = row0.createCell(4);
			cellIndonesia.setCellValue("Indonesia");
			cellIndonesia.setCellStyle(cellStyleHeaderYellow);
			HSSFCell cellLondres = row0.createCell(7);
			cellLondres.setCellValue("Bolsa de Londres");
			cellLondres.setCellStyle(cellStyleHeaderGreen);
//			HSSFCell cellUganda = row0.createCell(7);
//			cellUganda.setCellValue("Uganda");
//			cellUganda.setCellStyle(cellStyleHeaderGreen);
//			HSSFCell cellLondres = row0.createCell(10);
//			cellLondres.setCellValue("Bolsa de Londres");
//			cellLondres.setCellStyle(cellStyleHeaderYellow);
			
			HSSFRow row1 = sheet.createRow(1);
			HSSFCell cellData = row1.createCell(0);
			cellData.setCellValue("Data");
			cellData.setCellStyle(cellStyleHeaderBlue);
			
			HSSFCell cellPrecoVietnam = row1.createCell(1);
			cellPrecoVietnam.setCellValue("Preço\n "+simboloMoeda+"/sc");
			cellPrecoVietnam.setCellStyle(cellStyleHeaderGreen);
			HSSFCell cellFreteVietnam = row1.createCell(2);
			cellFreteVietnam.setCellValue("Fr. Maritimo +\n Seguro");
			cellFreteVietnam.setCellStyle(cellStyleHeaderGreen);
			HSSFCell cellFOBVietnam = row1.createCell(3);
			cellFOBVietnam.setCellValue("Fob Porto\n Santos\n "+simboloMoeda+"/sc");
			cellFOBVietnam.setCellStyle(cellStyleHeaderGreen);
			
			HSSFCell cellPrecoIndonesia = row1.createCell(4);
			cellPrecoIndonesia.setCellValue("Preço \n "+simboloMoeda+"/sc");
			cellPrecoIndonesia.setCellStyle(cellStyleHeaderYellow);
			HSSFCell cellFreteIndonesia = row1.createCell(5);
			cellFreteIndonesia.setCellValue("Fr. Maritimo +\n Seguro");
			cellFreteIndonesia.setCellStyle(cellStyleHeaderYellow);
			HSSFCell cellFOBIndonesia = row1.createCell(6);
			cellFOBIndonesia.setCellValue("Fob Porto\n Santos\n "+simboloMoeda+"/sc");
			cellFOBIndonesia.setCellStyle(cellStyleHeaderYellow);
			
			HSSFCell cellPrecoLondres = row1.createCell(7);
			cellPrecoLondres.setCellValue("Preço\n "+simboloMoeda+"/sc");
			cellPrecoLondres.setCellStyle(cellStyleHeaderGreen);
			HSSFCell cellFreteLondres = row1.createCell(8);
			cellFreteLondres.setCellValue("Fr. Maritimo +\n Seguro");
			cellFreteLondres.setCellStyle(cellStyleHeaderGreen);
			HSSFCell cellFOBLondres = row1.createCell(9);
			cellFOBLondres.setCellValue("Fob Porto\n Santos\n "+simboloMoeda+"/sc");
			cellFOBLondres.setCellStyle(cellStyleHeaderGreen);
			
//			HSSFCell cellPrecoCostaMarfim = row1.createCell(7);
//			cellPrecoCostaMarfim.setCellValue("Preço\n "+simboloMoeda+"/sc");
//			cellPrecoCostaMarfim.setCellStyle(cellStyleHeaderGreen);
//			HSSFCell cellFreteCostaMarfim = row1.createCell(8);
//			cellFreteCostaMarfim.setCellValue("Fr. Maritimo +\n Seguro");
//			cellFreteCostaMarfim.setCellStyle(cellStyleHeaderGreen);
//			HSSFCell cellFOBCostaMarfim = row1.createCell(9);
//			cellFOBCostaMarfim.setCellValue("Fob Porto\n Santos\n "+simboloMoeda+"/sc");
//			cellFOBCostaMarfim.setCellStyle(cellStyleHeaderGreen);
//			
//			HSSFCell cellPrecoLondres = row1.createCell(10);
//			cellPrecoLondres.setCellValue("Preço\n "+simboloMoeda+"/sc");
//			cellPrecoLondres.setCellStyle(cellStyleHeaderYellow);
//			HSSFCell cellFreteLondres = row1.createCell(11);
//			cellFreteLondres.setCellValue("Fr. Maritimo +\n Seguro");
//			cellFreteLondres.setCellStyle(cellStyleHeaderYellow);
//			HSSFCell cellFOBLondres = row1.createCell(12);
//			cellFOBLondres.setCellValue("Fob Porto\n Santos\n "+simboloMoeda+"/sc");
//			cellFOBLondres.setCellStyle(cellStyleHeaderYellow);
			
			sheet.addMergedRegion(new CellRangeAddress(0,0,1,3));
			sheet.addMergedRegion(new CellRangeAddress(0,0,4,6));
			sheet.addMergedRegion(new CellRangeAddress(0,0,7,9));
			sheet.addMergedRegion(new CellRangeAddress(0,0,10,12));

			sheet.addMergedRegion(new CellRangeAddress(1,2,0,0));
			sheet.addMergedRegion(new CellRangeAddress(1,2,1,1));
			sheet.addMergedRegion(new CellRangeAddress(1,2,2,2));
			sheet.addMergedRegion(new CellRangeAddress(1,2,3,3));
			
			sheet.addMergedRegion(new CellRangeAddress(1,2,4,4));
			sheet.addMergedRegion(new CellRangeAddress(1,2,5,5));
			sheet.addMergedRegion(new CellRangeAddress(1,2,6,6));
			
			sheet.addMergedRegion(new CellRangeAddress(1,2,7,7));
			sheet.addMergedRegion(new CellRangeAddress(1,2,8,8));
			sheet.addMergedRegion(new CellRangeAddress(1,2,9,9));

//			sheet.addMergedRegion(new CellRangeAddress(1,2,10,10));
//			sheet.addMergedRegion(new CellRangeAddress(1,2,11,11));
//			sheet.addMergedRegion(new CellRangeAddress(1,2,12,12));
			
	    	Calendar calendarCategories = Calendar.getInstance();
	    	calendarCategories.setTimeInMillis(dataInicialUnix);
	        
	    	int corLinha = 0;
	    	
	        for(int i = 0; i < qteDiasMes; i++) {
	        	
	        	HSSFRow rowDataValor = sheet.createRow(i + 3);
	        	int numeroCelula = 1;
	        	
	        	calendarCategories.add(Calendar.DAY_OF_YEAR, 1);
	        	calendarCategories.set(Calendar.HOUR_OF_DAY, 0);  
	        	calendarCategories.set(Calendar.MINUTE, 0);  
	        	calendarCategories.set(Calendar.SECOND, 0);  
	        	calendarCategories.set(Calendar.MILLISECOND, 0);  
	        	Long unixtimeWithoutLocale = calendarCategories.getTimeInMillis() / 1000;
	        	
				HSSFCell celData = rowDataValor.createCell(0);
				celData.setCellValue(DataUtil.sdfDataBR.format(calendarCategories.getTime()));
				
				if(corLinha % 2 == 0) {
					celData.setCellStyle(cellStyleBlue);
				}
				corLinha++;
				
	        	for(Cotacao c : cotacaoList) {
	        		
				    RelValorMercadoriaCotacao valorMercadoriaCotacao = RelValorMercadoriaCotacao.findByDataCotacao(unixtimeWithoutLocale, c.getId()); 
				    if(valorMercadoriaCotacao != null) {
			    		
				    	BigDecimal valorMercadoria = new BigDecimal(0.0);
				    	BigDecimal valorLogistica = new BigDecimal(0.0);
				    	BigDecimal valorCustoImportacao = new BigDecimal(0.0);
				    	
			    		if(tipoMoedaStr.equals("real")) {
		    				valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaReal();	
		    				valorLogistica = MonetaryUtil.subtract(valorMercadoriaCotacao.getValorCustoImportacaoReal(), 
		    						valorMercadoriaCotacao.getValorMercadoriaReal());
		    				valorCustoImportacao = valorMercadoriaCotacao.getValorCustoImportacaoReal();
			    		} else if(tipoMoedaStr.equals("dolar")) {
			    			valorMercadoria = valorMercadoriaCotacao.getValorMercadoriaDolar();
			    			valorLogistica = MonetaryUtil.subtract(valorMercadoriaCotacao.getValorCustoImportacaoDolar(), 
									valorMercadoriaCotacao.getValorMercadoriaDolar());
			    			valorCustoImportacao = valorMercadoriaCotacao.getValorCustoImportacaoDolar();
			    		}
				    	
						HSSFCell celPrecoFOB = rowDataValor.createCell(numeroCelula);
						celPrecoFOB.setCellValue(valorMercadoria.setScale(2,RoundingMode.HALF_UP).toString().replace(".", ","));
						HSSFCell celFrete = rowDataValor.createCell(++numeroCelula);
//						BigDecimal valorLogistica = MonetaryUtil.subtract(valorMercadoriaCotacao.getValorCustoImportacaoReal(), 
//								valorMercadoriaCotacao.getValorMercadoriaReal());
						valorLogistica = valorLogistica.setScale(2,RoundingMode.HALF_UP);
						celFrete.setCellValue(valorLogistica.toString().replace(".", ","));
						HSSFCell celPrecoCIF = rowDataValor.createCell(++numeroCelula);
						celPrecoCIF.setCellValue(valorCustoImportacao.setScale(2,RoundingMode.HALF_UP).toString().replace(".", ","));
						++numeroCelula;

						if(c.getId().equals(1L) || c.getId().equals(6L)) {
							celPrecoFOB.setCellStyle(cellStyleGreen);
							celFrete.setCellStyle(cellStyleGreen);
							celPrecoCIF.setCellStyle(cellStyleGreen);
						} else if(c.getId().equals(2L)) {
							celPrecoFOB.setCellStyle(cellStyleYellow);
							celFrete.setCellStyle(cellStyleYellow);
							celPrecoCIF.setCellStyle(cellStyleYellow);							
						}
						
//						if(c.getId().equals(1L) || c.getId().equals(3L)) {
//							celPrecoFOB.setCellStyle(cellStyleGreen);
//							celFrete.setCellStyle(cellStyleGreen);
//							celPrecoCIF.setCellStyle(cellStyleGreen);
//						} else if(c.getId().equals(2L) || c.getId().equals(6L)) {
//							celPrecoFOB.setCellStyle(cellStyleYellow);
//							celFrete.setCellStyle(cellStyleYellow);
//							celPrecoCIF.setCellStyle(cellStyleYellow);							
//						}

						if(corLinha % 2 == 0) {
							celPrecoFOB.setCellStyle(cellStyleWhite);
							celFrete.setCellStyle(cellStyleWhite);
							celPrecoCIF.setCellStyle(cellStyleWhite);
						}
				    }
	        	}
	        }

			FileOutputStream fileOut = new FileOutputStream(excelFileName);

			//write this workbook to an Outputstream.
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
			
//			FileUtil.salvarImagem(content, excelFileName);
			
			byte[] contents = IOUtils.toByteArray(new FileInputStream(excelFileName));
			return ok(contents);
		} catch(Exception e) {
			
		}
		return ok();
	}
	
//	private static CellStyle createStyleHeader(HSSFWorkbook wb, short foregroundColor) {
//		
//		Font fontBold = wb.createFont();//Create font
//	    fontBold.setBoldweight(Font.BOLDWEIGHT_BOLD);//Make font bold
//	    
//        CellStyle cellStyle = wb.createCellStyle();
//        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
//        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
//        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
//        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
//        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
//        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
//        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
//        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        cellStyle.setFillForegroundColor(foregroundColor);
//        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
//        cellStyle.setFont(fontBold);
//
//        return cellStyle;
//	}
//	
//	private static CellStyle createStyle(HSSFWorkbook wb, short foregroundColor) {
//		
//        CellStyle cellStyle = wb.createCellStyle();
//        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
//        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
//        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
//        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
//        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
//        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
//        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
//        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        cellStyle.setFillForegroundColor(foregroundColor);
//        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
//
//        return cellStyle;
//	}
    
}