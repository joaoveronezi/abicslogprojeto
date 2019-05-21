package controllers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import models.AbicsEmpresa;
import models.AbicsExportacaoEmpresa;
import models.AbicsExportacaoPais;
import models.AbicsExportacaoProduto;
import models.AbicsProduto;
import models.BlocoEconomico;
import models.Pais;
import models.RelPaisBlocoEconomico;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

import play.libs.Json;
import play.mvc.Result;
import util.AbicsLogConfig;
import util.ExcelUtil;
import util.FileUtil;
import entitys.report.RelatorioExcelAnual;
import entitys.report.RelatorioExcelMensalAcumulado;

public class TemplateAbicsDataController extends AbstractController {
	
	public static final int ANOS_PAIS_MENSAL_ACUMULADO = 3;
	public static final int ANOS_PAIS_ANUAL = 9;
	public static String[] meses = {"Meses", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
	
	public static Result gerarExcel() {
		
		Map<String, String[]> asFormUrlEncoded = request().body().asMultipartFormData().asFormUrlEncoded();
		
		String[] tipoRelatorioArr = asFormUrlEncoded.get("tipoRelatorio");
		String[] tipoRelatorioList = tipoRelatorioArr[0].split(",");
		
		String[] anosArr = asFormUrlEncoded.get("ano");
		String ano = anosArr[0];
		int anoInteger = Integer.parseInt(ano);
		String[] mesesArr = asFormUrlEncoded.get("mes");
		String mes = mesesArr[0];
		int mesInteger = Integer.parseInt(mes);
		
		int anoInicialMensalAcumulado = anoInteger - ANOS_PAIS_MENSAL_ACUMULADO + 1;
		
		Integer tamanhoArrayAnoMensalAcumulado = (anoInteger - anoInicialMensalAcumulado) + 1;
		Integer[] anoPeriodoMensalAcumulado = new Integer[tamanhoArrayAnoMensalAcumulado];
		
		for(Integer i = 0; i < tamanhoArrayAnoMensalAcumulado; i++) {
			anoPeriodoMensalAcumulado[i] = anoInteger - i;
		}
		
		int anoInicialAnual = anoInteger - ANOS_PAIS_ANUAL + 1;
		
		Integer tamanhoArrayAnual = (anoInteger - anoInicialAnual) + 1;
		Integer[] anoPeriodoAnual = new Integer[tamanhoArrayAnual];
		
		for(Integer i = 0; i < tamanhoArrayAnual; i++) {
			anoPeriodoAnual[i] = anoInteger - i;
		}
		
		List<RelatorioExcelMensalAcumulado> relatorioMensalList = new ArrayList<RelatorioExcelMensalAcumulado>();
		List<RelatorioExcelMensalAcumulado> relatorioAcumuladoList = new ArrayList<RelatorioExcelMensalAcumulado>();
		List<RelatorioExcelAnual> relatorioAnualList = new ArrayList<RelatorioExcelAnual>();
		
		List<Long> listaSomatorioAnoMensal = new ArrayList<Long>();
		List<Long> listaSomatorioAnoAcumulado = new ArrayList<Long>();
		List<BigDecimal> listaSomatorioAnoAnual = new ArrayList<BigDecimal>();
		
		List<Long> listaSomatorioVariacaoMensal = new ArrayList<Long>();
		List<Long> listaSomatorioVariacaoAcumulado = new ArrayList<Long>();
		
		try {

			String excelFileName = AbicsLogConfig.getString(AbicsLogConfig.USER_REPORT_FOLDER)
					+ getUsuarioLogado().getId() + "/relatorioAcumuladoMensalAnual.xlsx";

			FileUtil.criarPasta(AbicsLogConfig.getString(AbicsLogConfig.USER_REPORT_FOLDER));

			HSSFWorkbook woorkBook = new HSSFWorkbook();
			
			CellStyle styleCabecalhoGeral1 = ExcelUtil.createStyle(woorkBook, IndexedColors.BLACK.getIndex(), IndexedColors.WHITE.getIndex(), "Calibri", (short) 14, true, false, false, "left", "text");
			CellStyle styleCabecalhoGeral2 = ExcelUtil.createStyle(woorkBook, IndexedColors.BLACK.getIndex(), IndexedColors.WHITE.getIndex(), "Calibri", (short) 12, true, false, false, "left", "text");
			CellStyle styleCabecalhoGeral3 = ExcelUtil.createStyle(woorkBook, IndexedColors.BLACK.getIndex(), IndexedColors.WHITE.getIndex(), "Calibri", (short) 12, true, true, false, "left", "text");
			
			HSSFColor marromCabecalhoTop =  ExcelUtil.setColor(woorkBook, (byte)(Integer.parseInt("122")), (byte)(Integer.parseInt("21")),(byte)(Integer.parseInt("0")), new HSSFColor.BROWN());
			HSSFColor lightPink =  ExcelUtil.setColor(woorkBook, (byte)(Integer.parseInt("255")) , (byte)(Integer.parseInt("227")) ,(byte)(Integer.parseInt("221")), new HSSFColor.PINK());
			
			CellStyle cellCabecalhoTop = ExcelUtil.createStyle(woorkBook, IndexedColors.WHITE.getIndex(), marromCabecalhoTop.getIndex(), "Calibri", (short) 10, true, false, true, "center", "text");
			CellStyle cellCabecalhoMid = ExcelUtil.createStyle(woorkBook, marromCabecalhoTop.getIndex(),  IndexedColors.WHITE.getIndex(), "Calibri", (short) 10, true, false, true, "center", "text");
			CellStyle cellCabecalhoTopAno = ExcelUtil.createStyle(woorkBook, marromCabecalhoTop.getIndex(), lightPink.getIndex(), "Calibri", (short) 10, true, false, true, "center", "text");
			CellStyle cellCabecalhoBot = ExcelUtil.createStyle(woorkBook, marromCabecalhoTop.getIndex(), lightPink.getIndex(), "Calibri", (short) 10, true, false, true, "center", "numeric");

			CellStyle cellDadosPaises = ExcelUtil.createStyle(woorkBook, marromCabecalhoTop.getIndex(), IndexedColors.WHITE.getIndex(), "Calibri", (short) 10, true, false, true, "left", "numeric");
			CellStyle cellDadosGerais = ExcelUtil.createStyle(woorkBook, IndexedColors.BLACK.getIndex(), IndexedColors.WHITE.getIndex(), "Calibri", (short) 10, false, false, true, "right", "numeric");
			
			CellStyle cellDadosPaisesAnual = ExcelUtil.createStyle(woorkBook, marromCabecalhoTop.getIndex(), IndexedColors.WHITE.getIndex(), "Calibri", (short) 10, true, false, true, "left", "dot");
			CellStyle cellDadosGeraisAnual = ExcelUtil.createStyle(woorkBook, IndexedColors.BLACK.getIndex(), IndexedColors.WHITE.getIndex(), "Calibri", (short) 10, false, false, true, "right", "dot");
			CellStyle cellCabecalhoBotAnual = ExcelUtil.createStyle(woorkBook, marromCabecalhoTop.getIndex(), lightPink.getIndex(), "Calibri", (short) 10, true, false, true, "center", "dot");
			
			InputStream my_banner_image = new FileInputStream(AbicsLogConfig.getString(AbicsLogConfig.LOGO_ABICS_PNG));
			byte[] bytes = IOUtils.toByteArray(my_banner_image);
			int my_picture_id = woorkBook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
			my_banner_image.close();
			
			String tituloMensal = "";
			String tituloPeriodoMensal = "";
			String tituloAlfandegaMensal = "";
			String subTituloMensal = "";

			String tituloAcumulado = "";
			String tituloPeriodoAcumulado = "";
			String tituloAlfandegaAcumulado = "";
			String subTituloAcumulado = "";
			String subTituloAcumuladoUE = "";
			
			String tituloAno = "";
			String tituloPeriodoAno = "";
			String tituloAlfandegaAno = "";
			String subTituloAno = "";
			
			relatorioMensalList.clear();
			relatorioAcumuladoList.clear();
			
			List<Pais> listaPaisesObj = new ArrayList<Pais>();
			listaPaisesObj = Pais.findAllWithAbicsId();
			
			Long somaPesoAno1Meses = 0L;
			Long somaPesoAno2Meses = 0L;
			Long somaPesoAno3Meses = 0L;
			
			Long somaReceitaAno1Meses = 0L;
			Long somaReceitaAno2Meses = 0L;
			Long somaReceitaAno3Meses = 0L;
			
			Long somaSacaAno1Meses = 0L;
			Long somaSacaAno2Meses = 0L;
			Long somaSacaAno3Meses = 0L;
			
			for(int i=0; i<mesInteger; i++) {
				RelatorioExcelMensalAcumulado relatorioMensal = new RelatorioExcelMensalAcumulado();
				
				relatorioMensal.setPais(meses[i+1]);

				for(int j=0; j<ANOS_PAIS_MENSAL_ACUMULADO; j++) {
					Integer anosMensal = anoInteger - j;
					
					Long pesoAcumuladoAntigo = 0L;
					Long receitaAcumuladaAntigo = 0L;
					Long saca60kgAcumuladaAntigo = 0L;
					
					for(Pais pais: listaPaisesObj) {
						AbicsExportacaoPais abicsPaisMensal = AbicsExportacaoPais.findByAnoMesPaisId(anosMensal, i+1, pais.getId());
						
						if(abicsPaisMensal != null) {
							pesoAcumuladoAntigo += abicsPaisMensal.getPeso();
							receitaAcumuladaAntigo += abicsPaisMensal.getReceita();
							saca60kgAcumuladaAntigo += abicsPaisMensal.getSaca60kg();
						}
					}
					
					if(j == 0) {
						relatorioMensal.setPesoAno1(pesoAcumuladoAntigo);
						relatorioMensal.setReceitaAno1(receitaAcumuladaAntigo);
						relatorioMensal.setSaca60kgAno1(saca60kgAcumuladaAntigo);
						somaPesoAno1Meses += pesoAcumuladoAntigo;
						somaSacaAno1Meses += saca60kgAcumuladaAntigo;
						somaReceitaAno1Meses += receitaAcumuladaAntigo;
					} else if(j == 1) {
						relatorioMensal.setPesoAno2(pesoAcumuladoAntigo);
						relatorioMensal.setReceitaAno2(receitaAcumuladaAntigo);
						relatorioMensal.setSaca60kgAno2(saca60kgAcumuladaAntigo);
						somaPesoAno2Meses += pesoAcumuladoAntigo;
						somaSacaAno2Meses += saca60kgAcumuladaAntigo;
						somaReceitaAno2Meses += receitaAcumuladaAntigo;
					} else if(j == 2) {
						relatorioMensal.setPesoAno3(pesoAcumuladoAntigo);
						relatorioMensal.setReceitaAno3(receitaAcumuladaAntigo);
						relatorioMensal.setSaca60kgAno3(saca60kgAcumuladaAntigo);
						somaPesoAno3Meses += pesoAcumuladoAntigo;
						somaSacaAno3Meses += saca60kgAcumuladaAntigo;
						somaReceitaAno3Meses += receitaAcumuladaAntigo;
					}
					
					if(!anosMensal.equals(anoInteger)) {
						
						Long pesoAcumuladoNovo = 0L;
						Long receitaAcumuladaNovo = 0L;
						Long saca60kgAcumuladaNovo = 0L;
						
						for(Pais pais: listaPaisesObj) {
							AbicsExportacaoPais abicsPaisAcumulado = AbicsExportacaoPais.findByAnoMesPaisId(anoInteger, i+1, pais.getId());
							
							if(abicsPaisAcumulado != null) {
								pesoAcumuladoNovo += abicsPaisAcumulado.getPeso();
								receitaAcumuladaNovo += abicsPaisAcumulado.getReceita();
								saca60kgAcumuladaNovo += abicsPaisAcumulado.getSaca60kg();
							}
						}
						
						AbicsExportacaoPais abicsAcumuladoNovo = new AbicsExportacaoPais();
						
						abicsAcumuladoNovo.setPeso(pesoAcumuladoNovo);
						abicsAcumuladoNovo.setReceita(receitaAcumuladaNovo);
						abicsAcumuladoNovo.setSaca60kg(saca60kgAcumuladaNovo);
						
						AbicsExportacaoPais abicsAcumuladoAntigo = new AbicsExportacaoPais();
						
						abicsAcumuladoAntigo.setPeso(pesoAcumuladoAntigo);
						abicsAcumuladoAntigo.setReceita(receitaAcumuladaAntigo);
						abicsAcumuladoAntigo.setSaca60kg(saca60kgAcumuladaAntigo);
						
						abicsAcumuladoNovo = calcularVariacaoPais(abicsAcumuladoAntigo, abicsAcumuladoNovo);

						if(j == 1) { 
							relatorioMensal.setPesoVariacaoAno1(abicsAcumuladoNovo.getPeso());
							relatorioMensal.setReceitaVariacaoAno1(abicsAcumuladoNovo.getReceita());
							relatorioMensal.setSaca60kgVariacaoAno1(abicsAcumuladoNovo.getSaca60kg());
							
						} else if(j == 2) {
							relatorioMensal.setPesoVariacaoAno2(abicsAcumuladoNovo.getPeso());
							relatorioMensal.setReceitaVariacaoAno2(abicsAcumuladoNovo.getReceita());
							relatorioMensal.setSaca60kgVariacaoAno2(abicsAcumuladoNovo.getSaca60kg());
						}
					}
				}
				
				relatorioMensalList.add(relatorioMensal);
			}
			
			for(int i=mesInteger+1; i<=12; i++) {
				RelatorioExcelMensalAcumulado relatorio = new RelatorioExcelMensalAcumulado();
				relatorio.setPais(meses[i]);
				relatorioMensalList.add(relatorio);
			}
			
			//add na lista de total mensal
			listaSomatorioAnoAcumulado.add(somaPesoAno1Meses);
			listaSomatorioAnoAcumulado.add(somaPesoAno2Meses);
			listaSomatorioAnoAcumulado.add(somaPesoAno3Meses);
			
			listaSomatorioAnoAcumulado.add(somaSacaAno1Meses);
			listaSomatorioAnoAcumulado.add(somaSacaAno2Meses);
			listaSomatorioAnoAcumulado.add(somaSacaAno3Meses);
			
			listaSomatorioAnoAcumulado.add(somaReceitaAno1Meses);
			listaSomatorioAnoAcumulado.add(somaReceitaAno2Meses);
			listaSomatorioAnoAcumulado.add(somaReceitaAno3Meses);
			
			//add na lista de variação dos totais mensal
			AbicsExportacaoPais abicsAno1Meses = new AbicsExportacaoPais();
			abicsAno1Meses.setPeso(somaPesoAno1Meses);
			abicsAno1Meses.setReceita(somaReceitaAno1Meses);
			abicsAno1Meses.setSaca60kg(somaSacaAno1Meses);
			
			AbicsExportacaoPais abicsAno2Meses = new AbicsExportacaoPais();
			abicsAno2Meses.setPeso(somaPesoAno2Meses);
			abicsAno2Meses.setReceita(somaReceitaAno2Meses);
			abicsAno2Meses.setSaca60kg(somaSacaAno2Meses);
			
			AbicsExportacaoPais abicsAno3Meses = new AbicsExportacaoPais();
			abicsAno3Meses.setPeso(somaPesoAno3Meses);
			abicsAno3Meses.setReceita(somaReceitaAno3Meses);
			abicsAno3Meses.setSaca60kg(somaSacaAno3Meses);
			
			AbicsExportacaoPais abicsVariacaoMeses1 = calcularVariacaoPais(abicsAno2Meses, abicsAno1Meses);
			AbicsExportacaoPais abicsVariacaoMeses2 = calcularVariacaoPais(abicsAno3Meses, abicsAno1Meses);
			
			listaSomatorioVariacaoAcumulado.add(abicsVariacaoMeses1.getPeso());
			listaSomatorioVariacaoAcumulado.add(abicsVariacaoMeses2.getPeso());
			listaSomatorioVariacaoAcumulado.add(abicsVariacaoMeses1.getSaca60kg());
			listaSomatorioVariacaoAcumulado.add(abicsVariacaoMeses2.getSaca60kg());
			listaSomatorioVariacaoAcumulado.add(abicsVariacaoMeses1.getReceita());
			listaSomatorioVariacaoAcumulado.add(abicsVariacaoMeses2.getReceita());
			
			tituloMensal = "Brasil: Exportações Mensais de café solúvel";
			tituloPeriodoMensal = "Período: Janeiro a " + meses[mesInteger];
			tituloAlfandegaMensal = "Data de alfandega";
			subTituloMensal = "";
			
			HSSFSheet sheetPaisMesMensal = ExcelUtil.criarSheetComCabecalhoRelatorioAbicsData(woorkBook, "Mensal", my_picture_id, 
					tituloMensal, tituloPeriodoMensal, tituloAlfandegaMensal, styleCabecalhoGeral1, styleCabecalhoGeral2, styleCabecalhoGeral3, "mes");
			
			sheetPaisMesMensal = ExcelUtil.criarCelulaTipoSelecionadoMensalAcumuladoRelatorioAbicsData(sheetPaisMesMensal, 
					"MÊS", subTituloMensal, anoPeriodoMensalAcumulado, cellCabecalhoTop, cellCabecalhoMid, cellCabecalhoTopAno);
			
			sheetPaisMesMensal = ExcelUtil.preencherDadosPaisMensalAcumulado(sheetPaisMesMensal, relatorioMensalList, listaSomatorioAnoAcumulado, listaSomatorioVariacaoAcumulado, cellDadosPaises, cellDadosGerais, cellCabecalhoBot, "mensal");
			
			sheetPaisMesMensal.setDisplayGridlines(false);
			
			for(String tipoRelatorio: tipoRelatorioList) {
				
				relatorioMensalList.clear();
				relatorioAcumuladoList.clear();
				relatorioAnualList.clear();
				
				listaSomatorioAnoMensal.clear();
				listaSomatorioAnoAcumulado.clear();
				listaSomatorioAnoAnual.clear();
				
				listaSomatorioVariacaoMensal.clear();
				listaSomatorioVariacaoAcumulado.clear();
				
				Long somaPesoAno1Mensal = 0L;
				Long somaPesoAno2Mensal = 0L;
				Long somaPesoAno3Mensal = 0L;
				
				Long somaPesoAno1Acumulado = 0L;
				Long somaPesoAno2Acumulado = 0L;
				Long somaPesoAno3Acumulado = 0L;
				
				BigDecimal somaPesoAno1Anual = new BigDecimal(0);
				BigDecimal somaPesoAno2Anual = new BigDecimal(0);
				BigDecimal somaPesoAno3Anual = new BigDecimal(0);
				BigDecimal somaPesoAno4Anual = new BigDecimal(0);
				BigDecimal somaPesoAno5Anual = new BigDecimal(0);
				BigDecimal somaPesoAno6Anual = new BigDecimal(0);
				BigDecimal somaPesoAno7Anual = new BigDecimal(0);
				BigDecimal somaPesoAno8Anual = new BigDecimal(0);
				BigDecimal somaPesoAno9Anual = new BigDecimal(0);
				
				Long somaSacaAno1Mensal = 0L;
				Long somaSacaAno2Mensal = 0L;
				Long somaSacaAno3Mensal = 0L;
				
				Long somaSacaAno1Acumulado = 0L;
				Long somaSacaAno2Acumulado = 0L;
				Long somaSacaAno3Acumulado = 0L;
				
				Long somaReceitaAno1Mensal = 0L;
				Long somaReceitaAno2Mensal = 0L;
				Long somaReceitaAno3Mensal = 0L;
				
				Long somaReceitaAno1Acumulado = 0L;
				Long somaReceitaAno2Acumulado = 0L;
				Long somaReceitaAno3Acumulado = 0L;
				
				BigDecimal somaReceitaAno1Anual = new BigDecimal(0);
				BigDecimal somaReceitaAno2Anual = new BigDecimal(0);
				BigDecimal somaReceitaAno3Anual = new BigDecimal(0);
				BigDecimal somaReceitaAno4Anual = new BigDecimal(0);
				BigDecimal somaReceitaAno5Anual = new BigDecimal(0);
				BigDecimal somaReceitaAno6Anual = new BigDecimal(0);
				BigDecimal somaReceitaAno7Anual = new BigDecimal(0);
				BigDecimal somaReceitaAno8Anual = new BigDecimal(0);
				BigDecimal somaReceitaAno9Anual = new BigDecimal(0);
				
				List<RelatorioExcelMensalAcumulado> relAcumuladoUE = new ArrayList<RelatorioExcelMensalAcumulado>();
				List<RelatorioExcelMensalAcumulado> relMensalUE = new ArrayList<RelatorioExcelMensalAcumulado>();
				Integer[] anoPeriodoMensalAcumuladoUE = {};
				List<Long> listaSomatorioUE = new ArrayList<Long>();
				List<Long> listaSomatorioAnoAcumuladoUE = new ArrayList<Long>();
				List<Long> listaSomatorioVariacaoAcumuladoUE = new ArrayList<Long>();
				List<Long> listaSomatorioVariacaoUE = new ArrayList<Long>();
				
				if(tipoRelatorio.equalsIgnoreCase("país") || tipoRelatorio.equalsIgnoreCase("ue")) {
					
					listaPaisesObj.clear();
					listaPaisesObj = new ArrayList<Pais>();
					
					if(tipoRelatorio.equalsIgnoreCase("país")) {
						listaPaisesObj = Pais.findAllWithAbicsId();
						
					} else {
						Long ueId = BlocoEconomico.findIdBySigla("UE");
						List<RelPaisBlocoEconomico> listaRelPaisBloco = RelPaisBlocoEconomico.findAllByBlocoEconomico(ueId);
						
						for(RelPaisBlocoEconomico paisBloco: listaRelPaisBloco) {
							listaPaisesObj.add(Pais.findById(paisBloco.getPaisId()));
						}
					}
					
					for(Pais pais: listaPaisesObj) {
						
						RelatorioExcelMensalAcumulado relatorioMensal = new RelatorioExcelMensalAcumulado();
						RelatorioExcelMensalAcumulado relatorioAcumulado = new RelatorioExcelMensalAcumulado();
						RelatorioExcelAnual relatorioAnual = new RelatorioExcelAnual();
						
						for(int i=0; i<ANOS_PAIS_MENSAL_ACUMULADO; i++) {
							Integer anosMensal = anoInteger - i;
							
							AbicsExportacaoPais abicsPaisAntigo = AbicsExportacaoPais.findByAnoMesPaisId(anosMensal, mesInteger, pais.getId());
							
							relatorioMensal.setPais(pais.getNome().toUpperCase());
							relatorioAcumulado.setPais(pais.getNome().toUpperCase());
							
							if(abicsPaisAntigo != null) {
								if(i == 0) {
									relatorioMensal.setPesoAno1(abicsPaisAntigo.getPeso());
									relatorioMensal.setReceitaAno1(abicsPaisAntigo.getReceita());
									relatorioMensal.setSaca60kgAno1(abicsPaisAntigo.getSaca60kg());
									somaPesoAno1Mensal += abicsPaisAntigo.getPeso();
									somaSacaAno1Mensal += abicsPaisAntigo.getSaca60kg();
									somaReceitaAno1Mensal += abicsPaisAntigo.getReceita();
									
								} else if(i == 1) {
									relatorioMensal.setPesoAno2(abicsPaisAntigo.getPeso());
									relatorioMensal.setReceitaAno2(abicsPaisAntigo.getReceita());
									relatorioMensal.setSaca60kgAno2(abicsPaisAntigo.getSaca60kg());
									somaPesoAno2Mensal += abicsPaisAntigo.getPeso();
									somaSacaAno2Mensal += abicsPaisAntigo.getSaca60kg();
									somaReceitaAno2Mensal += abicsPaisAntigo.getReceita();
									
								} else if(i == 2) {
									relatorioMensal.setPesoAno3(abicsPaisAntigo.getPeso());
									relatorioMensal.setReceitaAno3(abicsPaisAntigo.getReceita());
									relatorioMensal.setSaca60kgAno3(abicsPaisAntigo.getSaca60kg());
									somaPesoAno3Mensal += abicsPaisAntigo.getPeso();
									somaSacaAno3Mensal += abicsPaisAntigo.getSaca60kg();
									somaReceitaAno3Mensal += abicsPaisAntigo.getReceita();
								}
							}
							
							Long pesoAcumuladoAntigo = 0L;
							Long receitaAcumuladaAntigo = 0L;
							Long saca60kgAcumuladaAntigo = 0L;
							
							for(int j=0; j<mesInteger; j++) {
								AbicsExportacaoPais abicsPaisAcumulado = AbicsExportacaoPais.findByAnoMesPaisId(anosMensal, j+1, pais.getId());
								
								if(abicsPaisAcumulado != null) {
									pesoAcumuladoAntigo += abicsPaisAcumulado.getPeso();
									receitaAcumuladaAntigo += abicsPaisAcumulado.getReceita();
									saca60kgAcumuladaAntigo += abicsPaisAcumulado.getSaca60kg();
								}
							}
							
							if(i == 0) {
								relatorioAcumulado.setPesoAno1(pesoAcumuladoAntigo);
								relatorioAcumulado.setReceitaAno1(receitaAcumuladaAntigo);
								relatorioAcumulado.setSaca60kgAno1(saca60kgAcumuladaAntigo);
								somaPesoAno1Acumulado += pesoAcumuladoAntigo;
								somaSacaAno1Acumulado += saca60kgAcumuladaAntigo;
								somaReceitaAno1Acumulado += receitaAcumuladaAntigo;
							} else if(i == 1) {
								relatorioAcumulado.setPesoAno2(pesoAcumuladoAntigo);
								relatorioAcumulado.setReceitaAno2(receitaAcumuladaAntigo);
								relatorioAcumulado.setSaca60kgAno2(saca60kgAcumuladaAntigo);
								somaPesoAno2Acumulado += pesoAcumuladoAntigo;
								somaSacaAno2Acumulado += saca60kgAcumuladaAntigo;
								somaReceitaAno2Acumulado += receitaAcumuladaAntigo;
							} else if(i == 2) {
								relatorioAcumulado.setPesoAno3(pesoAcumuladoAntigo);
								relatorioAcumulado.setReceitaAno3(receitaAcumuladaAntigo);
								relatorioAcumulado.setSaca60kgAno3(saca60kgAcumuladaAntigo);
								somaPesoAno3Acumulado += pesoAcumuladoAntigo;
								somaSacaAno3Acumulado += saca60kgAcumuladaAntigo;
								somaReceitaAno3Acumulado += receitaAcumuladaAntigo;
							}
							
							if(!anosMensal.equals(anoInteger)) {
								
								AbicsExportacaoPais abicsPais = AbicsExportacaoPais.findByAnoMesPaisId(anoInteger, mesInteger, pais.getId());
								
								if(abicsPais == null) {
									abicsPais = new AbicsExportacaoPais();
									abicsPais.setPeso(0L);
									abicsPais.setReceita(0L);
									abicsPais.setSaca60kg(0L);
								}
								
								abicsPais = calcularVariacaoPais(abicsPaisAntigo, abicsPais);
								
								Long pesoAcumuladoNovo = 0L;
								Long receitaAcumuladaNovo = 0L;
								Long saca60kgAcumuladaNovo = 0L;
								
								for(int j=0; j<mesInteger; j++) {
									AbicsExportacaoPais abicsPaisAcumulado = AbicsExportacaoPais.findByAnoMesPaisId(anoInteger, j+1, pais.getId());
									
									if(abicsPaisAcumulado != null) {
										pesoAcumuladoNovo += abicsPaisAcumulado.getPeso();
										receitaAcumuladaNovo += abicsPaisAcumulado.getReceita();
										saca60kgAcumuladaNovo += abicsPaisAcumulado.getSaca60kg();
									}
								}
								
								AbicsExportacaoPais abicsAcumuladoNovo = new AbicsExportacaoPais();
								
								abicsAcumuladoNovo.setPeso(pesoAcumuladoNovo);
								abicsAcumuladoNovo.setReceita(receitaAcumuladaNovo);
								abicsAcumuladoNovo.setSaca60kg(saca60kgAcumuladaNovo);
								
								AbicsExportacaoPais abicsAcumuladoAntigo = new AbicsExportacaoPais();
								
								abicsAcumuladoAntigo.setPeso(pesoAcumuladoAntigo);
								abicsAcumuladoAntigo.setReceita(receitaAcumuladaAntigo);
								abicsAcumuladoAntigo.setSaca60kg(saca60kgAcumuladaAntigo);
								
								abicsAcumuladoNovo = calcularVariacaoPais(abicsAcumuladoAntigo, abicsAcumuladoNovo);
								
								if(i == 1) { 
									relatorioMensal.setPesoVariacaoAno1(abicsPais.getPeso());
									relatorioMensal.setReceitaVariacaoAno1(abicsPais.getReceita());
									relatorioMensal.setSaca60kgVariacaoAno1(abicsPais.getSaca60kg());
									
									relatorioAcumulado.setPesoVariacaoAno1(abicsAcumuladoNovo.getPeso());
									relatorioAcumulado.setReceitaVariacaoAno1(abicsAcumuladoNovo.getReceita());
									relatorioAcumulado.setSaca60kgVariacaoAno1(abicsAcumuladoNovo.getSaca60kg());
									
								} else if(i == 2) {
									relatorioMensal.setPesoVariacaoAno2(abicsPais.getPeso());
									relatorioMensal.setReceitaVariacaoAno2(abicsPais.getReceita());
									relatorioMensal.setSaca60kgVariacaoAno2(abicsPais.getSaca60kg());
									
									relatorioAcumulado.setPesoVariacaoAno2(abicsAcumuladoNovo.getPeso());
									relatorioAcumulado.setReceitaVariacaoAno2(abicsAcumuladoNovo.getReceita());
									relatorioAcumulado.setSaca60kgVariacaoAno2(abicsAcumuladoNovo.getSaca60kg());
								}
							}
						}
						
						for(int i=0; i<ANOS_PAIS_ANUAL; i++) {
							Integer anosMensal = anoInicialAnual + i;
							
							BigDecimal pesoAnual = new BigDecimal(0);
							BigDecimal receitaAnual = new BigDecimal(0);
							
							relatorioAnual.setPais(pais.getNome().toUpperCase());
							
							int index = 12;
							
							if(anosMensal.equals(anoInteger)) {
								index = mesInteger;
							}
							
							for(int j=0; j<index; j++) {
								AbicsExportacaoPais abicsPaisAnual = AbicsExportacaoPais.findByAnoMesPaisId(anosMensal, j+1, pais.getId());
								
								if(abicsPaisAnual != null) {
									BigDecimal pesoObj = new BigDecimal(abicsPaisAnual.getPeso());
									BigDecimal receitaObj = new BigDecimal(abicsPaisAnual.getReceita());
									
									pesoAnual = pesoAnual.add(pesoObj.divide(new BigDecimal(1000), 1, RoundingMode.HALF_UP));
									receitaAnual = receitaAnual.add(receitaObj.divide(new BigDecimal(1000), 1, RoundingMode.HALF_UP));
								}
							}
							
							if(i == 0) {
								relatorioAnual.setPesoAno1(pesoAnual);
								relatorioAnual.setReceitaAno1(receitaAnual);
								somaPesoAno1Anual = somaPesoAno1Anual.add(pesoAnual);
								somaReceitaAno1Anual = somaReceitaAno1Anual.add(receitaAnual);
								
							} else if(i == 1) {
								relatorioAnual.setPesoAno2(pesoAnual);
								relatorioAnual.setReceitaAno2(receitaAnual);
								somaPesoAno2Anual = somaPesoAno2Anual.add(pesoAnual);
								somaReceitaAno2Anual = somaReceitaAno2Anual.add(receitaAnual);
								
							} else if(i == 2) {
								relatorioAnual.setPesoAno3(pesoAnual);
								relatorioAnual.setReceitaAno3(receitaAnual);
								somaPesoAno3Anual = somaPesoAno3Anual.add(pesoAnual);
								somaReceitaAno3Anual = somaReceitaAno3Anual.add(receitaAnual);
								
							} else if(i == 3) {
								relatorioAnual.setPesoAno4(pesoAnual);
								relatorioAnual.setReceitaAno4(receitaAnual);
								somaPesoAno4Anual = somaPesoAno4Anual.add(pesoAnual);
								somaReceitaAno4Anual = somaReceitaAno4Anual.add(receitaAnual);
								
							} else if(i == 4) {
								relatorioAnual.setPesoAno5(pesoAnual);
								relatorioAnual.setReceitaAno5(receitaAnual);
								somaPesoAno5Anual = somaPesoAno5Anual.add(pesoAnual);
								somaReceitaAno5Anual = somaReceitaAno5Anual.add(receitaAnual);
								
							} else if(i == 5) {
								relatorioAnual.setPesoAno6(pesoAnual);
								relatorioAnual.setReceitaAno6(receitaAnual);
								somaPesoAno6Anual = somaPesoAno6Anual.add(pesoAnual);
								somaReceitaAno6Anual = somaReceitaAno6Anual.add(receitaAnual);
								
							} else if(i == 6) {
								relatorioAnual.setPesoAno7(pesoAnual);
								relatorioAnual.setReceitaAno7(receitaAnual);
								somaPesoAno7Anual = somaPesoAno7Anual.add(pesoAnual);
								somaReceitaAno7Anual = somaReceitaAno7Anual.add(receitaAnual);
								
							} else if(i == 7) {
								relatorioAnual.setPesoAno8(pesoAnual);
								relatorioAnual.setReceitaAno8(receitaAnual);
								somaPesoAno8Anual = somaPesoAno8Anual.add(pesoAnual);
								somaReceitaAno8Anual = somaReceitaAno8Anual.add(receitaAnual);
								
							} else if(i == 8) {
								relatorioAnual.setPesoAno9(pesoAnual);
								relatorioAnual.setReceitaAno9(receitaAnual);
								somaPesoAno9Anual = somaPesoAno9Anual.add(pesoAnual);
								somaReceitaAno9Anual = somaReceitaAno9Anual.add(receitaAnual);
							}
						}
						
						if(!relatorioMensal.getPesoAno1().equals(0L) || !relatorioMensal.getPesoAno2().equals(0L) 
								|| !relatorioMensal.getPesoAno3().equals(0L)) {
							
							relatorioMensalList.add(relatorioMensal);
						}
						
						if(!relatorioAcumulado.getPesoAno1().equals(0L) || !relatorioAcumulado.getPesoAno2().equals(0L) 
								|| !relatorioAcumulado.getPesoAno3().equals(0L)) {
							
							relatorioAcumuladoList.add(relatorioAcumulado);
						}
						
						relatorioAnualList.add(relatorioAnual);
					}
					
					//add na lista de total mensal
					listaSomatorioAnoMensal.add(somaPesoAno1Mensal);
					listaSomatorioAnoMensal.add(somaPesoAno2Mensal);
					listaSomatorioAnoMensal.add(somaPesoAno3Mensal);
					
					listaSomatorioAnoMensal.add(somaSacaAno1Mensal);
					listaSomatorioAnoMensal.add(somaSacaAno2Mensal);
					listaSomatorioAnoMensal.add(somaSacaAno3Mensal);
					
					listaSomatorioAnoMensal.add(somaReceitaAno1Mensal);
					listaSomatorioAnoMensal.add(somaReceitaAno2Mensal);
					listaSomatorioAnoMensal.add(somaReceitaAno3Mensal);
					
					//add na lista de variação dos totais mensal
					AbicsExportacaoPais abicsAno1 = new AbicsExportacaoPais();
					abicsAno1.setPeso(somaPesoAno1Mensal);
					abicsAno1.setReceita(somaReceitaAno1Mensal);
					abicsAno1.setSaca60kg(somaSacaAno1Mensal);
					
					AbicsExportacaoPais abicsAno2 = new AbicsExportacaoPais();
					abicsAno2.setPeso(somaPesoAno2Mensal);
					abicsAno2.setReceita(somaReceitaAno2Mensal);
					abicsAno2.setSaca60kg(somaSacaAno2Mensal);
					
					AbicsExportacaoPais abicsAno3 = new AbicsExportacaoPais();
					abicsAno3.setPeso(somaPesoAno3Mensal);
					abicsAno3.setReceita(somaReceitaAno3Mensal);
					abicsAno3.setSaca60kg(somaSacaAno3Mensal);
					
					AbicsExportacaoPais abicsVariacaoMensal1 = calcularVariacaoPais(abicsAno2, abicsAno1);
					AbicsExportacaoPais abicsVariacaoMensal2 = calcularVariacaoPais(abicsAno3, abicsAno1);
					
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal1.getPeso());
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal2.getPeso());
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal1.getSaca60kg());
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal2.getSaca60kg());
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal1.getReceita());
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal2.getReceita());
					
					//add na lista de total acumulado
					listaSomatorioAnoAcumulado.add(somaPesoAno1Acumulado);
					listaSomatorioAnoAcumulado.add(somaPesoAno2Acumulado);
					listaSomatorioAnoAcumulado.add(somaPesoAno3Acumulado);
					
					listaSomatorioAnoAcumulado.add(somaSacaAno1Acumulado);
					listaSomatorioAnoAcumulado.add(somaSacaAno2Acumulado);
					listaSomatorioAnoAcumulado.add(somaSacaAno3Acumulado);
					
					listaSomatorioAnoAcumulado.add(somaReceitaAno1Acumulado);
					listaSomatorioAnoAcumulado.add(somaReceitaAno2Acumulado);
					listaSomatorioAnoAcumulado.add(somaReceitaAno3Acumulado);
					
					abicsAno1.setPeso(somaPesoAno1Acumulado);
					abicsAno1.setReceita(somaReceitaAno1Acumulado);
					abicsAno1.setSaca60kg(somaSacaAno1Acumulado);
					
					abicsAno2.setPeso(somaPesoAno2Acumulado);
					abicsAno2.setReceita(somaReceitaAno2Acumulado);
					abicsAno2.setSaca60kg(somaSacaAno2Acumulado);
					
					abicsAno3.setPeso(somaPesoAno3Acumulado);
					abicsAno3.setReceita(somaReceitaAno3Acumulado);
					abicsAno3.setSaca60kg(somaSacaAno3Acumulado);
					
					//add na lista de variação dos totais acumulado
					AbicsExportacaoPais abicsVariacaoAcumulado1 = calcularVariacaoPais(abicsAno2, abicsAno1);
					AbicsExportacaoPais abicsVariacaoAcumulado2 = calcularVariacaoPais(abicsAno3, abicsAno1);
					
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado1.getPeso());
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado2.getPeso());
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado1.getSaca60kg());
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado2.getSaca60kg());
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado1.getReceita());
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado2.getReceita());
					
					//add na lista de totais anual
					listaSomatorioAnoAnual.add(somaPesoAno1Anual);
					listaSomatorioAnoAnual.add(somaPesoAno2Anual);
					listaSomatorioAnoAnual.add(somaPesoAno3Anual);
					listaSomatorioAnoAnual.add(somaPesoAno4Anual);
					listaSomatorioAnoAnual.add(somaPesoAno5Anual);
					listaSomatorioAnoAnual.add(somaPesoAno6Anual);
					listaSomatorioAnoAnual.add(somaPesoAno7Anual);
					listaSomatorioAnoAnual.add(somaPesoAno8Anual);
					listaSomatorioAnoAnual.add(somaPesoAno9Anual);
					
					listaSomatorioAnoAnual.add(somaReceitaAno1Anual);
					listaSomatorioAnoAnual.add(somaReceitaAno2Anual);
					listaSomatorioAnoAnual.add(somaReceitaAno3Anual);
					listaSomatorioAnoAnual.add(somaReceitaAno4Anual);
					listaSomatorioAnoAnual.add(somaReceitaAno5Anual);
					listaSomatorioAnoAnual.add(somaReceitaAno6Anual);
					listaSomatorioAnoAnual.add(somaReceitaAno7Anual);
					listaSomatorioAnoAnual.add(somaReceitaAno8Anual);
					listaSomatorioAnoAnual.add(somaReceitaAno9Anual);
					
					if(tipoRelatorio.equalsIgnoreCase("país")) {
						tituloMensal = "Brasil: Exportações de café solúvel por destino";
						tituloPeriodoMensal = "Período: Janeiro a " + meses[mesInteger];
						tituloAlfandegaMensal = "Data de alfandega";
						subTituloMensal = meses[mesInteger];
						
						tituloAno = "Brasil: Exportações de café solúvel por destino";
						tituloPeriodoAno = "Janeiro/" + anoInicialAnual + " a " + meses[mesInteger] + "/" + anoInteger;
						tituloAlfandegaAno = "Data de alfandega";
						subTituloAno = "";
						
						tituloAcumulado = "Brasil: Exportações de café solúvel por destino";
						tituloPeriodoAcumulado = "Período: Janeiro a " + meses[mesInteger];
						tituloAlfandegaAcumulado = "Data de alfandega";
						subTituloAcumulado = "Janeiro a " + meses[mesInteger];
						
						
						Collections.sort(relatorioMensalList, new Comparator<RelatorioExcelMensalAcumulado>() {
					        @Override
					        public int compare(RelatorioExcelMensalAcumulado peso1, RelatorioExcelMensalAcumulado peso2)
					        {
					        	Long peso1Long = peso1.getPesoAno1();
					        	Long peso2Long = peso2.getPesoAno1();
					            return peso2Long.compareTo(peso1Long);
					        }
						});
						
						Collections.sort(relatorioAcumuladoList, new Comparator<RelatorioExcelMensalAcumulado>() {
					        @Override
					        public int compare(RelatorioExcelMensalAcumulado peso1, RelatorioExcelMensalAcumulado peso2)
					        {
					        	Long peso1Long = peso1.getPesoAno1();
					        	Long peso2Long = peso2.getPesoAno1();
					            return peso2Long.compareTo(peso1Long);
					        }
						});
						Collections.sort(relatorioAnualList, new Comparator<RelatorioExcelAnual>() {
					        @Override
					        public int compare(RelatorioExcelAnual peso1, RelatorioExcelAnual peso2)
					        {
					        	BigDecimal peso1BigDecimal = peso1.getPesoAno1();
					        	BigDecimal peso2BigDecimal = peso2.getPesoAno1();
					            return peso2BigDecimal.compareTo(peso1BigDecimal);
					        }
						});
						
						HSSFSheet sheetPaisMensal = ExcelUtil.criarSheetComCabecalhoRelatorioAbicsData(woorkBook, "Pais - Mensal", my_picture_id, 
								tituloMensal, tituloPeriodoMensal, tituloAlfandegaMensal, styleCabecalhoGeral1, styleCabecalhoGeral2, styleCabecalhoGeral3, "pais");
						HSSFSheet sheetPaisAcumulado = ExcelUtil.criarSheetComCabecalhoRelatorioAbicsData(woorkBook, "Pais - Acumulado", my_picture_id, 
								tituloAcumulado, tituloPeriodoAcumulado, tituloAlfandegaAcumulado, styleCabecalhoGeral1, styleCabecalhoGeral2, styleCabecalhoGeral3, "pais");
						HSSFSheet sheetPaisAno = ExcelUtil.criarSheetComCabecalhoRelatorioAbicsData(woorkBook, "Pais - Anual", my_picture_id, 
								tituloAno, tituloPeriodoAno, tituloAlfandegaAno, styleCabecalhoGeral1, styleCabecalhoGeral2, styleCabecalhoGeral3, "pais");
						
						sheetPaisMensal = ExcelUtil.criarCelulaTipoSelecionadoMensalAcumuladoRelatorioAbicsData(sheetPaisMensal, 
								"País - Destino", subTituloMensal, anoPeriodoMensalAcumulado, cellCabecalhoTop, cellCabecalhoMid, cellCabecalhoTopAno);
						sheetPaisAcumulado = ExcelUtil.criarCelulaTipoSelecionadoMensalAcumuladoRelatorioAbicsData(sheetPaisAcumulado, 
								"País - Acumulado", subTituloAcumulado, anoPeriodoMensalAcumulado, cellCabecalhoTop, cellCabecalhoMid, cellCabecalhoTopAno);
						sheetPaisAno = ExcelUtil.criarCelulaTipoSelecionadoAnoRelatorioAbicsData(sheetPaisAno, 
								"País - Destino", subTituloAno, anoPeriodoAnual, cellCabecalhoTop, cellCabecalhoMid, cellCabecalhoTopAno);
						
						sheetPaisMensal = ExcelUtil.preencherDadosPaisMensalAcumulado(sheetPaisMensal, relatorioMensalList, listaSomatorioAnoMensal, listaSomatorioVariacaoMensal, cellDadosPaises, cellDadosGerais, cellCabecalhoBot, "mensalAcumulado");
						sheetPaisAcumulado = ExcelUtil.preencherDadosPaisMensalAcumulado(sheetPaisAcumulado, relatorioAcumuladoList, listaSomatorioAnoAcumulado, listaSomatorioVariacaoAcumulado, cellDadosPaises, cellDadosGerais, cellCabecalhoBot, "mensalAcumulado");
						sheetPaisAno = ExcelUtil.preencherDadosPaisAnual(sheetPaisAno, relatorioAnualList, listaSomatorioAnoAnual, cellDadosPaisesAnual, cellDadosGeraisAnual, cellCabecalhoBotAnual, "mensalAcumulado");
						
//						sheetPaisMensal.setZoom(85);
//						sheetPaisAcumulado.setZoom(85);
//						sheetPaisAno.setZoom(85);
						
						sheetPaisMensal.setDisplayGridlines(false);
						sheetPaisAcumulado.setDisplayGridlines(false);
						sheetPaisAno.setDisplayGridlines(false);
						
					} else if(tipoRelatorio.equalsIgnoreCase("ue")) {
						
						tituloMensal = "Brasil: Exportações de café solúvel por destino";
						tituloPeriodoMensal = "Período: Janeiro a " + meses[mesInteger];
						subTituloMensal = meses[mesInteger];
						
						Collections.sort(relatorioMensalList, new Comparator<RelatorioExcelMensalAcumulado>() {
					        @Override
					        public int compare(RelatorioExcelMensalAcumulado peso1, RelatorioExcelMensalAcumulado peso2)
					        {
					        	Long peso1Long = peso1.getPesoAno1();
					        	Long peso2Long = peso2.getPesoAno1();
					            return peso2Long.compareTo(peso1Long);
					        }
						});
						
						Collections.sort(relatorioAcumuladoList, new Comparator<RelatorioExcelMensalAcumulado>() {
					        @Override
					        public int compare(RelatorioExcelMensalAcumulado peso1, RelatorioExcelMensalAcumulado peso2)
					        {
					        	Long peso1Long = peso1.getPesoAno1();
					        	Long peso2Long = peso2.getPesoAno1();
					            return peso2Long.compareTo(peso1Long);
					        }
						});
						
						subTituloAcumuladoUE = "Janeiro a " + meses[mesInteger];
						
						
						relAcumuladoUE.addAll(relatorioAcumuladoList);
						relMensalUE.addAll(relatorioMensalList);
						anoPeriodoMensalAcumuladoUE = anoPeriodoMensalAcumulado;
						listaSomatorioUE.addAll(listaSomatorioAnoMensal);
						listaSomatorioAnoAcumuladoUE.addAll(listaSomatorioAnoAcumulado);
						listaSomatorioVariacaoAcumuladoUE.addAll(listaSomatorioVariacaoAcumulado);
						listaSomatorioVariacaoUE.addAll(listaSomatorioVariacaoMensal);
						
						relatorioMensalList.clear();
						
						for(int i=0; i<mesInteger; i++) {
							RelatorioExcelMensalAcumulado relatorioMensal = new RelatorioExcelMensalAcumulado();
							
							relatorioMensal.setPais(meses[i+1]);

							for(int j=0; j<ANOS_PAIS_MENSAL_ACUMULADO; j++) {
								Integer anosMensal = anoInteger - j;
								
								Long pesoAcumuladoAntigo = 0L;
								Long receitaAcumuladaAntigo = 0L;
								Long saca60kgAcumuladaAntigo = 0L;
								
								for(Pais pais: listaPaisesObj) {
									AbicsExportacaoPais abicsPaisMensal = AbicsExportacaoPais.findByAnoMesPaisId(anosMensal, i+1, pais.getId());
									
									if(abicsPaisMensal != null) {
										pesoAcumuladoAntigo += abicsPaisMensal.getPeso();
										receitaAcumuladaAntigo += abicsPaisMensal.getReceita();
										saca60kgAcumuladaAntigo += abicsPaisMensal.getSaca60kg();
									}
								}
								
								if(j == 0) {
									relatorioMensal.setPesoAno1(pesoAcumuladoAntigo);
									relatorioMensal.setReceitaAno1(receitaAcumuladaAntigo);
									relatorioMensal.setSaca60kgAno1(saca60kgAcumuladaAntigo);
									somaPesoAno1Acumulado += pesoAcumuladoAntigo;
									somaSacaAno1Acumulado += saca60kgAcumuladaAntigo;
									somaReceitaAno1Acumulado += receitaAcumuladaAntigo;
								} else if(j == 1) {
									relatorioMensal.setPesoAno2(pesoAcumuladoAntigo);
									relatorioMensal.setReceitaAno2(receitaAcumuladaAntigo);
									relatorioMensal.setSaca60kgAno2(saca60kgAcumuladaAntigo);
									somaPesoAno2Acumulado += pesoAcumuladoAntigo;
									somaSacaAno2Acumulado += saca60kgAcumuladaAntigo;
									somaReceitaAno2Acumulado += receitaAcumuladaAntigo;
								} else if(j == 2) {
									relatorioMensal.setPesoAno3(pesoAcumuladoAntigo);
									relatorioMensal.setReceitaAno3(receitaAcumuladaAntigo);
									relatorioMensal.setSaca60kgAno3(saca60kgAcumuladaAntigo);
									somaPesoAno3Acumulado += pesoAcumuladoAntigo;
									somaSacaAno3Acumulado += saca60kgAcumuladaAntigo;
									somaReceitaAno3Acumulado += receitaAcumuladaAntigo;
								}
								
								if(!anosMensal.equals(anoInteger)) {
									
									Long pesoAcumuladoNovo = 0L;
									Long receitaAcumuladaNovo = 0L;
									Long saca60kgAcumuladaNovo = 0L;
									
									for(Pais pais: listaPaisesObj) {
										AbicsExportacaoPais abicsPaisAcumulado = AbicsExportacaoPais.findByAnoMesPaisId(anoInteger, i+1, pais.getId());
										
										if(abicsPaisAcumulado != null) {
											pesoAcumuladoNovo += abicsPaisAcumulado.getPeso();
											receitaAcumuladaNovo += abicsPaisAcumulado.getReceita();
											saca60kgAcumuladaNovo += abicsPaisAcumulado.getSaca60kg();
										}
									}
									
									AbicsExportacaoPais abicsAcumuladoNovo = new AbicsExportacaoPais();
									
									abicsAcumuladoNovo.setPeso(pesoAcumuladoNovo);
									abicsAcumuladoNovo.setReceita(receitaAcumuladaNovo);
									abicsAcumuladoNovo.setSaca60kg(saca60kgAcumuladaNovo);
									
									AbicsExportacaoPais abicsAcumuladoAntigo = new AbicsExportacaoPais();
									
									abicsAcumuladoAntigo.setPeso(pesoAcumuladoAntigo);
									abicsAcumuladoAntigo.setReceita(receitaAcumuladaAntigo);
									abicsAcumuladoAntigo.setSaca60kg(saca60kgAcumuladaAntigo);
									
									abicsAcumuladoNovo = calcularVariacaoPais(abicsAcumuladoAntigo, abicsAcumuladoNovo);
									
									if(j == 1) { 
										relatorioMensal.setPesoVariacaoAno1(abicsAcumuladoNovo.getPeso());
										relatorioMensal.setReceitaVariacaoAno1(abicsAcumuladoNovo.getReceita());
										relatorioMensal.setSaca60kgVariacaoAno1(abicsAcumuladoNovo.getSaca60kg());
										
									} else if(j == 2) {
										relatorioMensal.setPesoVariacaoAno2(abicsAcumuladoNovo.getPeso());
										relatorioMensal.setReceitaVariacaoAno2(abicsAcumuladoNovo.getReceita());
										relatorioMensal.setSaca60kgVariacaoAno2(abicsAcumuladoNovo.getSaca60kg());
									}
								}
							}
							
							relatorioMensalList.add(relatorioMensal);
						}
						
						for(int i=mesInteger+1; i<=12; i++) {
							RelatorioExcelMensalAcumulado relatorio = new RelatorioExcelMensalAcumulado();
							relatorio.setPais(meses[i]);
							relatorioMensalList.add(relatorio);
						}
						
						//add na lista de total acumulado
						listaSomatorioAnoAcumulado.add(somaPesoAno1Acumulado);
						listaSomatorioAnoAcumulado.add(somaPesoAno2Acumulado);
						listaSomatorioAnoAcumulado.add(somaPesoAno3Acumulado);
						
						listaSomatorioAnoAcumulado.add(somaSacaAno1Acumulado);
						listaSomatorioAnoAcumulado.add(somaSacaAno2Acumulado);
						listaSomatorioAnoAcumulado.add(somaSacaAno3Acumulado);
						
						listaSomatorioAnoAcumulado.add(somaReceitaAno1Acumulado);
						listaSomatorioAnoAcumulado.add(somaReceitaAno2Acumulado);
						listaSomatorioAnoAcumulado.add(somaReceitaAno3Acumulado);
						
						//add na lista de variação dos totais mensal
						AbicsExportacaoPais abicsAno4 = new AbicsExportacaoPais();
						AbicsExportacaoPais abicsAno5 = new AbicsExportacaoPais();
						AbicsExportacaoPais abicsAno6 = new AbicsExportacaoPais();
						
						abicsAno4.setPeso(somaPesoAno1Acumulado);
						abicsAno4.setReceita(somaReceitaAno1Acumulado);
						abicsAno4.setSaca60kg(somaSacaAno1Acumulado);
						
						abicsAno5.setPeso(somaPesoAno2Acumulado);
						abicsAno5.setReceita(somaReceitaAno2Acumulado);
						abicsAno5.setSaca60kg(somaSacaAno2Acumulado);
						
						abicsAno6.setPeso(somaPesoAno3Acumulado);
						abicsAno6.setReceita(somaReceitaAno3Acumulado);
						abicsAno6.setSaca60kg(somaSacaAno3Acumulado);
						
						//add na lista de variação dos totais acumulado
						abicsVariacaoAcumulado1 = calcularVariacaoPais(abicsAno5, abicsAno4);
						abicsVariacaoAcumulado2 = calcularVariacaoPais(abicsAno6, abicsAno4);
						
						listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado1.getPeso());
						listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado2.getPeso());
						listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado1.getSaca60kg());
						listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado2.getSaca60kg());
						listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado1.getReceita());
						listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado2.getReceita());
						
						//add na lista de totais anual
						listaSomatorioAnoAnual.clear();
						listaSomatorioAnoAnual.add(somaPesoAno1Anual);
						listaSomatorioAnoAnual.add(somaPesoAno2Anual);
						listaSomatorioAnoAnual.add(somaPesoAno3Anual);
						listaSomatorioAnoAnual.add(somaPesoAno4Anual);
						listaSomatorioAnoAnual.add(somaPesoAno5Anual);
						listaSomatorioAnoAnual.add(somaPesoAno6Anual);
						listaSomatorioAnoAnual.add(somaPesoAno7Anual);
						listaSomatorioAnoAnual.add(somaPesoAno8Anual);
						listaSomatorioAnoAnual.add(somaPesoAno9Anual);
						
						listaSomatorioAnoAnual.add(somaReceitaAno1Anual);
						listaSomatorioAnoAnual.add(somaReceitaAno2Anual);
						listaSomatorioAnoAnual.add(somaReceitaAno3Anual);
						listaSomatorioAnoAnual.add(somaReceitaAno4Anual);
						listaSomatorioAnoAnual.add(somaReceitaAno5Anual);
						listaSomatorioAnoAnual.add(somaReceitaAno6Anual);
						listaSomatorioAnoAnual.add(somaReceitaAno7Anual);
						listaSomatorioAnoAnual.add(somaReceitaAno8Anual);
						listaSomatorioAnoAnual.add(somaReceitaAno9Anual);
						
						tituloMensal = "Brasil: Exportações Mensais de café solúvel - U.E";
						tituloPeriodoMensal = "Período: Janeiro a " + meses[mesInteger];
						tituloAlfandegaMensal = "Data de alfandega";
						subTituloMensal = "";
						String subTituloMensalUE = meses[mesInteger];
						
						tituloAno = "Brasil: Exportações anuas de café solúvel por destino (União Européia)";
						tituloPeriodoAno = "Período: Janeiro/" + anoInicialAnual + " a " + meses[mesInteger] + "/" + anoInteger;
						tituloAlfandegaAno = "Data de alfandega";
						subTituloAno = "";
						
						
						HSSFSheet sheetPaisMes = ExcelUtil.criarSheetComCabecalhoRelatorioAbicsData(woorkBook, "UE mês, país mensal acumulado", my_picture_id, 
								tituloMensal, tituloPeriodoMensal, tituloAlfandegaMensal, styleCabecalhoGeral1, styleCabecalhoGeral2, styleCabecalhoGeral3, "mes");
						
//						HSSFSheet sheetPaisMensal = ExcelUtil.criarSheetComCabecalhoRelatorioAbicsData(woorkBook, "UE País mensal acumulado", my_picture_id, 
//								tituloMensal, tituloPeriodoMensal, tituloAlfandegaMensal, styleCabecalhoGeral1, styleCabecalhoGeral2, styleCabecalhoGeral3, "pais");
						
						HSSFSheet sheetPaisAno = ExcelUtil.criarSheetComCabecalhoRelatorioAbicsData(woorkBook, "UE - Anual", my_picture_id, 
								tituloAno, tituloPeriodoAno, tituloAlfandegaAno, styleCabecalhoGeral1, styleCabecalhoGeral2, styleCabecalhoGeral3, "mes");
						
//						sheetPaisMes = ExcelUtil.criarCelulaTipoSelecionadoMensalAcumuladoRelatorioAbicsData(sheetPaisMes, 
//								"MÊS", subTituloMensal, anoPeriodoMensalAcumulado, cellCabecalhoTop, cellCabecalhoMid, cellCabecalhoTopAno);
						sheetPaisAno = ExcelUtil.criarCelulaTipoSelecionadoAnoRelatorioAbicsData(sheetPaisAno, 
								"Pais - Destino", subTituloAno, anoPeriodoAnual, cellCabecalhoTop, cellCabecalhoMid, cellCabecalhoTopAno);
						
						sheetPaisMes = ExcelUtil.criarCelulaTipoSelecionadoMensalAcumuladoRelatorioAbicsData(sheetPaisMes, 
								"MÊS", subTituloMensal, anoPeriodoMensalAcumulado, cellCabecalhoTop, cellCabecalhoMid, cellCabecalhoTopAno);
						sheetPaisMes = ExcelUtil.criarCelulaTipoSelecionadoMensalAcumulado2RelatorioAbicsData(sheetPaisMes, 
								"País - Destino", subTituloMensalUE, anoPeriodoMensalAcumuladoUE, relMensalUE.size(), cellCabecalhoTop, cellCabecalhoMid, cellCabecalhoTopAno);
						sheetPaisMes = ExcelUtil.criarCelulaTipoSelecionadoMensalAcumulado2RelatorioAbicsData(sheetPaisMes, 
								"País - Destino", subTituloAcumuladoUE, anoPeriodoMensalAcumuladoUE, relMensalUE.size() + relatorioMensalList.size() + 8, cellCabecalhoTop, cellCabecalhoMid, cellCabecalhoTopAno);
						
//						sheetPaisMes = ExcelUtil.preencherDadosPaisMensalAcumulado(sheetPaisMes, relatorioMensalList, listaSomatorioAnoAcumuladoUE, listaSomatorioVariacaoAcumuladoUE, cellDadosPaises, cellDadosGerais, cellCabecalhoBot, "mensal");
//						sheetPaisMensal = ExcelUtil.preencherDadosEmpresaMensalAcumulado(sheetPaisMensal, relMensalUE, listaSomatorioUE, listaSomatorioVariacaoUE, relAcumuladoUE, listaSomatorioAnoAcumuladoUE, listaSomatorioVariacaoAcumuladoUE, cellDadosPaises, cellDadosGerais, cellCabecalhoBot);
						sheetPaisMes = ExcelUtil.preencherDadosUEMesMensalAcumulado(sheetPaisMes, relMensalUE, listaSomatorioUE, listaSomatorioVariacaoUE, relAcumuladoUE, listaSomatorioAnoAcumuladoUE, listaSomatorioVariacaoAcumuladoUE, relatorioMensalList, listaSomatorioAnoAcumuladoUE, listaSomatorioVariacaoAcumuladoUE, cellDadosPaises, cellDadosGerais, cellCabecalhoBot);
						sheetPaisAno = ExcelUtil.preencherDadosPaisAnual(sheetPaisAno, relatorioAnualList, listaSomatorioAnoAnual, cellDadosPaisesAnual, cellDadosGeraisAnual, cellCabecalhoBotAnual, "mensal");
						
						sheetPaisMes.setDisplayGridlines(false);
//						sheetPaisMensal.setDisplayGridlines(false);
						sheetPaisAno.setDisplayGridlines(false);
					}
					
				} else if(tipoRelatorio.equalsIgnoreCase("produto")) {
					
					List<AbicsProduto> listaProdutosObj = AbicsProduto.findAll();
					
					for(AbicsProduto produto: listaProdutosObj) {
						
						RelatorioExcelMensalAcumulado relatorioMensal = new RelatorioExcelMensalAcumulado();
						RelatorioExcelMensalAcumulado relatorioAcumulado = new RelatorioExcelMensalAcumulado();
						RelatorioExcelAnual relatorioAnual = new RelatorioExcelAnual();
						
						for(int i=0; i<ANOS_PAIS_MENSAL_ACUMULADO; i++) {
							Integer anosMensal = anoInteger - i;
							
							AbicsExportacaoProduto abicsProdutoAntigo = AbicsExportacaoProduto.findByAnoMesProdutoId(anosMensal, mesInteger, produto.getId());
							
							relatorioMensal.setPais(produto.getNome().toUpperCase());
							relatorioAcumulado.setPais(produto.getNome().toUpperCase());
							
							if(abicsProdutoAntigo != null) {
								if(i == 0) {
									relatorioMensal.setPesoAno1(abicsProdutoAntigo.getPeso());
									relatorioMensal.setReceitaAno1(abicsProdutoAntigo.getReceita());
									relatorioMensal.setSaca60kgAno1(abicsProdutoAntigo.getSaca60kg());
									somaPesoAno1Mensal += abicsProdutoAntigo.getPeso();
									somaSacaAno1Mensal += abicsProdutoAntigo.getSaca60kg();
									somaReceitaAno1Mensal += abicsProdutoAntigo.getReceita();
									
								} else if(i == 1) {
									relatorioMensal.setPesoAno2(abicsProdutoAntigo.getPeso());
									relatorioMensal.setReceitaAno2(abicsProdutoAntigo.getReceita());
									relatorioMensal.setSaca60kgAno2(abicsProdutoAntigo.getSaca60kg());
									somaPesoAno2Mensal += abicsProdutoAntigo.getPeso();
									somaSacaAno2Mensal += abicsProdutoAntigo.getSaca60kg();
									somaReceitaAno2Mensal += abicsProdutoAntigo.getReceita();
									
								} else if(i == 2) {
									relatorioMensal.setPesoAno3(abicsProdutoAntigo.getPeso());
									relatorioMensal.setReceitaAno3(abicsProdutoAntigo.getReceita());
									relatorioMensal.setSaca60kgAno3(abicsProdutoAntigo.getSaca60kg());
									somaPesoAno3Mensal += abicsProdutoAntigo.getPeso();
									somaSacaAno3Mensal += abicsProdutoAntigo.getSaca60kg();
									somaReceitaAno3Mensal += abicsProdutoAntigo.getReceita();
								}
							}
							
							Long pesoAcumuladoAntigo = 0L;
							Long receitaAcumuladaAntigo = 0L;
							Long saca60kgAcumuladaAntigo = 0L;
							
							for(int j=0; j<mesInteger; j++) {
								AbicsExportacaoProduto abicsProdutoAcumulado = AbicsExportacaoProduto.findByAnoMesProdutoId(anosMensal, j+1, produto.getId());
								
								if(abicsProdutoAcumulado != null) {
									pesoAcumuladoAntigo += abicsProdutoAcumulado.getPeso();
									receitaAcumuladaAntigo += abicsProdutoAcumulado.getReceita();
									saca60kgAcumuladaAntigo += abicsProdutoAcumulado.getSaca60kg();
								}
							}
							
							if(i == 0) {
								relatorioAcumulado.setPesoAno1(pesoAcumuladoAntigo);
								relatorioAcumulado.setReceitaAno1(receitaAcumuladaAntigo);
								relatorioAcumulado.setSaca60kgAno1(saca60kgAcumuladaAntigo);
								somaPesoAno1Acumulado += pesoAcumuladoAntigo;
								somaSacaAno1Acumulado += saca60kgAcumuladaAntigo;
								somaReceitaAno1Acumulado += receitaAcumuladaAntigo;
							} else if(i == 1) {
								relatorioAcumulado.setPesoAno2(pesoAcumuladoAntigo);
								relatorioAcumulado.setReceitaAno2(receitaAcumuladaAntigo);
								relatorioAcumulado.setSaca60kgAno2(saca60kgAcumuladaAntigo);
								somaPesoAno2Acumulado += pesoAcumuladoAntigo;
								somaSacaAno2Acumulado += saca60kgAcumuladaAntigo;
								somaReceitaAno2Acumulado += receitaAcumuladaAntigo;
							} else if(i == 2) {
								relatorioAcumulado.setPesoAno3(pesoAcumuladoAntigo);
								relatorioAcumulado.setReceitaAno3(receitaAcumuladaAntigo);
								relatorioAcumulado.setSaca60kgAno3(saca60kgAcumuladaAntigo);
								somaPesoAno3Acumulado += pesoAcumuladoAntigo;
								somaSacaAno3Acumulado += saca60kgAcumuladaAntigo;
								somaReceitaAno3Acumulado += receitaAcumuladaAntigo;
							}
							
							if(!anosMensal.equals(anoInteger)) {
								
								AbicsExportacaoProduto abicsProduto = AbicsExportacaoProduto.findByAnoMesProdutoId(anoInteger, mesInteger, produto.getId());
								
								if(abicsProduto == null) {
									abicsProduto = new AbicsExportacaoProduto();
									abicsProduto.setPeso(0L);
									abicsProduto.setReceita(0L);
									abicsProduto.setSaca60kg(0L);
								}
								
								abicsProduto = calcularVariacaoProduto(abicsProdutoAntigo, abicsProduto);
								
								Long pesoAcumuladoNovo = 0L;
								Long receitaAcumuladaNovo = 0L;
								Long saca60kgAcumuladaNovo = 0L;
								
								for(int j=0; j<mesInteger; j++) {
									AbicsExportacaoProduto abicsProdutoAcumulado = AbicsExportacaoProduto.findByAnoMesProdutoId(anoInteger, j+1, produto.getId());
									
									if(abicsProdutoAcumulado != null) {
										pesoAcumuladoNovo += abicsProdutoAcumulado.getPeso();
										receitaAcumuladaNovo += abicsProdutoAcumulado.getReceita();
										saca60kgAcumuladaNovo += abicsProdutoAcumulado.getSaca60kg();
									}
								}
								
								AbicsExportacaoProduto abicsAcumuladoNovo = new AbicsExportacaoProduto();
								
								abicsAcumuladoNovo.setPeso(pesoAcumuladoNovo);
								abicsAcumuladoNovo.setReceita(receitaAcumuladaNovo);
								abicsAcumuladoNovo.setSaca60kg(saca60kgAcumuladaNovo);
								
								AbicsExportacaoProduto abicsAcumuladoAntigo = new AbicsExportacaoProduto();
								
								abicsAcumuladoAntigo.setPeso(pesoAcumuladoAntigo);
								abicsAcumuladoAntigo.setReceita(receitaAcumuladaAntigo);
								abicsAcumuladoAntigo.setSaca60kg(saca60kgAcumuladaAntigo);
								
								abicsAcumuladoNovo = calcularVariacaoProduto(abicsAcumuladoAntigo, abicsAcumuladoNovo);
								
								if(i == 1) { 
									relatorioMensal.setPesoVariacaoAno1(abicsProduto.getPeso());
									relatorioMensal.setReceitaVariacaoAno1(abicsProduto.getReceita());
									relatorioMensal.setSaca60kgVariacaoAno1(abicsProduto.getSaca60kg());
									
									relatorioAcumulado.setPesoVariacaoAno1(abicsAcumuladoNovo.getPeso());
									relatorioAcumulado.setReceitaVariacaoAno1(abicsAcumuladoNovo.getReceita());
									relatorioAcumulado.setSaca60kgVariacaoAno1(abicsAcumuladoNovo.getSaca60kg());
									
								} else if(i == 2) {
									relatorioMensal.setPesoVariacaoAno2(abicsProduto.getPeso());
									relatorioMensal.setReceitaVariacaoAno2(abicsProduto.getReceita());
									relatorioMensal.setSaca60kgVariacaoAno2(abicsProduto.getSaca60kg());
									
									relatorioAcumulado.setPesoVariacaoAno2(abicsAcumuladoNovo.getPeso());
									relatorioAcumulado.setReceitaVariacaoAno2(abicsAcumuladoNovo.getReceita());
									relatorioAcumulado.setSaca60kgVariacaoAno2(abicsAcumuladoNovo.getSaca60kg());
								}
							}
						}
						
						for(int i=0; i<ANOS_PAIS_ANUAL; i++) {
							Integer anosMensal = anoInicialAnual + i;
							
							BigDecimal pesoAnual = new BigDecimal(0);
							BigDecimal receitaAnual = new BigDecimal(0);
							
							relatorioAnual.setPais(produto.getNome().toUpperCase());
							
							int index = 12;
							
							if(anosMensal.equals(anoInteger)) {
								index = mesInteger;
							}
							
							for(int j=0; j<index; j++) {
								AbicsExportacaoProduto abicsProdutoAnual = AbicsExportacaoProduto.findByAnoMesProdutoId(anosMensal, j+1, produto.getId());
								
								if(abicsProdutoAnual != null) {
									
									pesoAnual = pesoAnual.add(new BigDecimal(abicsProdutoAnual.getPeso()).divide(new BigDecimal(1000), 1, RoundingMode.HALF_UP));
									receitaAnual = receitaAnual.add(new BigDecimal(abicsProdutoAnual.getReceita()).divide(new BigDecimal(1000), 1, RoundingMode.HALF_UP));
								}
							}
							
							if(i == 0) {
								relatorioAnual.setPesoAno1(pesoAnual);
								relatorioAnual.setReceitaAno1(receitaAnual);
								somaPesoAno1Anual = somaPesoAno1Anual.add(pesoAnual);
								somaReceitaAno1Anual = somaReceitaAno1Anual.add(receitaAnual);
								
							} else if(i == 1) {
								relatorioAnual.setPesoAno2(pesoAnual);
								relatorioAnual.setReceitaAno2(receitaAnual);
								somaPesoAno2Anual = somaPesoAno2Anual.add(pesoAnual);
								somaReceitaAno2Anual = somaReceitaAno2Anual.add(receitaAnual);
								
							} else if(i == 2) {
								relatorioAnual.setPesoAno3(pesoAnual);
								relatorioAnual.setReceitaAno3(receitaAnual);
								somaPesoAno3Anual = somaPesoAno3Anual.add(pesoAnual);
								somaReceitaAno3Anual = somaReceitaAno3Anual.add(receitaAnual);
								
							} else if(i == 3) {
								relatorioAnual.setPesoAno4(pesoAnual);
								relatorioAnual.setReceitaAno4(receitaAnual);
								somaPesoAno4Anual = somaPesoAno4Anual.add(pesoAnual);
								somaReceitaAno4Anual = somaReceitaAno4Anual.add(receitaAnual);
								
							} else if(i == 4) {
								relatorioAnual.setPesoAno5(pesoAnual);
								relatorioAnual.setReceitaAno5(receitaAnual);
								somaPesoAno5Anual = somaPesoAno5Anual.add(pesoAnual);
								somaReceitaAno5Anual = somaReceitaAno5Anual.add(receitaAnual);
								
							} else if(i == 5) {
								relatorioAnual.setPesoAno6(pesoAnual);
								relatorioAnual.setReceitaAno6(receitaAnual);
								somaPesoAno6Anual = somaPesoAno6Anual.add(pesoAnual);
								somaReceitaAno6Anual = somaReceitaAno6Anual.add(receitaAnual);
								
							} else if(i == 6) {
								relatorioAnual.setPesoAno7(pesoAnual);
								relatorioAnual.setReceitaAno7(receitaAnual);
								somaPesoAno7Anual = somaPesoAno7Anual.add(pesoAnual);
								somaReceitaAno7Anual = somaReceitaAno7Anual.add(receitaAnual);
								
							} else if(i == 7) {
								relatorioAnual.setPesoAno8(pesoAnual);
								relatorioAnual.setReceitaAno8(receitaAnual);
								somaPesoAno8Anual = somaPesoAno8Anual.add(pesoAnual);
								somaReceitaAno8Anual = somaReceitaAno8Anual.add(receitaAnual);
								
							} else if(i == 8) {
								relatorioAnual.setPesoAno9(pesoAnual);
								relatorioAnual.setReceitaAno9(receitaAnual);
								somaPesoAno9Anual = somaPesoAno9Anual.add(pesoAnual);
								somaReceitaAno9Anual = somaReceitaAno9Anual.add(receitaAnual);
							}
						}
						
						if(!relatorioMensal.getPesoAno1().equals(0L) || !relatorioMensal.getPesoAno2().equals(0L)
								|| !relatorioMensal.getPesoAno3().equals(0L)) {
							
							relatorioMensalList.add(relatorioMensal);
						}
						
						if(!relatorioAcumulado.getPesoAno1().equals(0L) || !relatorioAcumulado.getPesoAno2().equals(0L)
								|| !relatorioAcumulado.getPesoAno3().equals(0L)) {
							
							relatorioAcumuladoList.add(relatorioAcumulado);
						}
						
						relatorioAnualList.add(relatorioAnual);
					}
					
					//add na lista de total mensal
					listaSomatorioAnoMensal.add(somaPesoAno1Mensal);
					listaSomatorioAnoMensal.add(somaPesoAno2Mensal);
					listaSomatorioAnoMensal.add(somaPesoAno3Mensal);
					
					listaSomatorioAnoMensal.add(somaSacaAno1Mensal);
					listaSomatorioAnoMensal.add(somaSacaAno2Mensal);
					listaSomatorioAnoMensal.add(somaSacaAno3Mensal);
					
					listaSomatorioAnoMensal.add(somaReceitaAno1Mensal);
					listaSomatorioAnoMensal.add(somaReceitaAno2Mensal);
					listaSomatorioAnoMensal.add(somaReceitaAno3Mensal);
					
					//add na lista de variação dos totais mensal
					AbicsExportacaoProduto abicsAno1 = new AbicsExportacaoProduto();
					abicsAno1.setPeso(somaPesoAno1Mensal);
					abicsAno1.setReceita(somaReceitaAno1Mensal);
					abicsAno1.setSaca60kg(somaSacaAno1Mensal);
					
					AbicsExportacaoProduto abicsAno2 = new AbicsExportacaoProduto();
					abicsAno2.setPeso(somaPesoAno2Mensal);
					abicsAno2.setReceita(somaReceitaAno2Mensal);
					abicsAno2.setSaca60kg(somaSacaAno2Mensal);
					
					AbicsExportacaoProduto abicsAno3 = new AbicsExportacaoProduto();
					abicsAno3.setPeso(somaPesoAno3Mensal);
					abicsAno3.setReceita(somaReceitaAno3Mensal);
					abicsAno3.setSaca60kg(somaSacaAno3Mensal);
					
					AbicsExportacaoProduto abicsVariacaoMensal1 = calcularVariacaoProduto(abicsAno2, abicsAno1);
					AbicsExportacaoProduto abicsVariacaoMensal2 = calcularVariacaoProduto(abicsAno3, abicsAno1);
					
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal1.getPeso());
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal2.getPeso());
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal1.getSaca60kg());
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal2.getSaca60kg());
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal1.getReceita());
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal2.getReceita());
					
					//add na lista de total acumulado
					listaSomatorioAnoAcumulado.add(somaPesoAno1Acumulado);
					listaSomatorioAnoAcumulado.add(somaPesoAno2Acumulado);
					listaSomatorioAnoAcumulado.add(somaPesoAno3Acumulado);
					
					listaSomatorioAnoAcumulado.add(somaSacaAno1Acumulado);
					listaSomatorioAnoAcumulado.add(somaSacaAno2Acumulado);
					listaSomatorioAnoAcumulado.add(somaSacaAno3Acumulado);
					
					listaSomatorioAnoAcumulado.add(somaReceitaAno1Acumulado);
					listaSomatorioAnoAcumulado.add(somaReceitaAno2Acumulado);
					listaSomatorioAnoAcumulado.add(somaReceitaAno3Acumulado);
					
					abicsAno1.setPeso(somaPesoAno1Acumulado);
					abicsAno1.setReceita(somaReceitaAno1Acumulado);
					abicsAno1.setSaca60kg(somaSacaAno1Acumulado);
					
					abicsAno2.setPeso(somaPesoAno2Acumulado);
					abicsAno2.setReceita(somaReceitaAno2Acumulado);
					abicsAno2.setSaca60kg(somaSacaAno2Acumulado);
					
					abicsAno3.setPeso(somaPesoAno3Acumulado);
					abicsAno3.setReceita(somaReceitaAno3Acumulado);
					abicsAno3.setSaca60kg(somaSacaAno3Acumulado);
					
					//add na lista de variação dos totais acumulado
					AbicsExportacaoProduto abicsVariacaoAcumulado1 = calcularVariacaoProduto(abicsAno2, abicsAno1);
					AbicsExportacaoProduto abicsVariacaoAcumulado2 = calcularVariacaoProduto(abicsAno3, abicsAno1);
					
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado1.getPeso());
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado2.getPeso());
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado1.getSaca60kg());
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado2.getSaca60kg());
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado1.getReceita());
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado2.getReceita());
					
					//add na lista de totais anual
					listaSomatorioAnoAnual.add(somaPesoAno1Anual);
					listaSomatorioAnoAnual.add(somaPesoAno2Anual);
					listaSomatorioAnoAnual.add(somaPesoAno3Anual);
					listaSomatorioAnoAnual.add(somaPesoAno4Anual);
					listaSomatorioAnoAnual.add(somaPesoAno5Anual);
					listaSomatorioAnoAnual.add(somaPesoAno6Anual);
					listaSomatorioAnoAnual.add(somaPesoAno7Anual);
					listaSomatorioAnoAnual.add(somaPesoAno8Anual);
					listaSomatorioAnoAnual.add(somaPesoAno9Anual);
					
					listaSomatorioAnoAnual.add(somaReceitaAno1Anual);
					listaSomatorioAnoAnual.add(somaReceitaAno2Anual);
					listaSomatorioAnoAnual.add(somaReceitaAno3Anual);
					listaSomatorioAnoAnual.add(somaReceitaAno4Anual);
					listaSomatorioAnoAnual.add(somaReceitaAno5Anual);
					listaSomatorioAnoAnual.add(somaReceitaAno6Anual);
					listaSomatorioAnoAnual.add(somaReceitaAno7Anual);
					listaSomatorioAnoAnual.add(somaReceitaAno8Anual);
					listaSomatorioAnoAnual.add(somaReceitaAno9Anual);
					
					tituloMensal = "Brasil: Exportações de café solúvel por destino";
					tituloPeriodoMensal = "Período: Janeiro a " + meses[mesInteger];
					tituloAlfandegaMensal = "Data de alfandega";
					subTituloMensal = meses[mesInteger];
					
					tituloAno = "Brasil: Exportações de café solúvel por destino";
					tituloPeriodoAno = "Janeiro/" + anoInicialAnual + " a " + meses[mesInteger] + "/" + anoInteger;
					tituloAlfandegaAno = "Data de alfandega";
					subTituloAno = "";
					
					tituloAcumulado = "Brasil: Exportações de café solúvel por destino";
					tituloPeriodoAcumulado = "Período: Janeiro a " + meses[mesInteger];
					tituloAlfandegaAcumulado = "Data de alfandega";
					subTituloAcumulado = "Período: Janeiro a " + meses[mesInteger];
					
					Collections.sort(relatorioMensalList, new Comparator<RelatorioExcelMensalAcumulado>() {
				        @Override
				        public int compare(RelatorioExcelMensalAcumulado peso1, RelatorioExcelMensalAcumulado peso2)
				        {
				        	Long peso1Long = peso1.getPesoAno1();
				        	Long peso2Long = peso2.getPesoAno1();
				            return peso2Long.compareTo(peso1Long);
				        }
					});
					Collections.sort(relatorioAcumuladoList, new Comparator<RelatorioExcelMensalAcumulado>() {
				        @Override
				        public int compare(RelatorioExcelMensalAcumulado peso1, RelatorioExcelMensalAcumulado peso2)
				        {
				        	Long peso1Long = peso1.getPesoAno1();
				        	Long peso2Long = peso2.getPesoAno1();
				            return peso2Long.compareTo(peso1Long);
				        }
					});
					Collections.sort(relatorioAnualList, new Comparator<RelatorioExcelAnual>() {
				        @Override
				        public int compare(RelatorioExcelAnual peso1, RelatorioExcelAnual peso2)
				        {
				        	BigDecimal peso1BigDecimal = peso1.getPesoAno1();
				        	BigDecimal peso2BigDecimal = peso2.getPesoAno1();
				            return peso2BigDecimal.compareTo(peso1BigDecimal);
				        }
					});
					
					HSSFSheet sheetPaisMensal = ExcelUtil.criarSheetComCabecalhoRelatorioAbicsData(woorkBook, "Por produto - Mensal", my_picture_id, 
							tituloMensal, tituloPeriodoMensal, tituloAlfandegaMensal, styleCabecalhoGeral1, styleCabecalhoGeral2, styleCabecalhoGeral3, "produto");
					HSSFSheet sheetPaisAcumulado = ExcelUtil.criarSheetComCabecalhoRelatorioAbicsData(woorkBook, "Por produto - Acumulado", my_picture_id, 
							tituloAcumulado, tituloPeriodoAcumulado, tituloAlfandegaAcumulado, styleCabecalhoGeral1, styleCabecalhoGeral2, styleCabecalhoGeral3, "produto");
					HSSFSheet sheetPaisAno = ExcelUtil.criarSheetComCabecalhoRelatorioAbicsData(woorkBook, "Por produto - Anual", my_picture_id, 
							tituloAno, tituloPeriodoAno, tituloAlfandegaAno, styleCabecalhoGeral1, styleCabecalhoGeral2, styleCabecalhoGeral3, "produto");
					
					sheetPaisMensal = ExcelUtil.criarCelulaTipoSelecionadoMensalAcumuladoRelatorioAbicsData(sheetPaisMensal, 
							"Por produto", subTituloMensal, anoPeriodoMensalAcumulado, cellCabecalhoTop, cellCabecalhoMid, cellCabecalhoTopAno);
					sheetPaisAcumulado = ExcelUtil.criarCelulaTipoSelecionadoMensalAcumuladoRelatorioAbicsData(sheetPaisAcumulado, 
							"Por produto", subTituloAcumulado, anoPeriodoMensalAcumulado, cellCabecalhoTop, cellCabecalhoMid, cellCabecalhoTopAno);
				
					sheetPaisAno = ExcelUtil.criarCelulaTipoSelecionadoAnoRelatorioAbicsData(sheetPaisAno, 
							"Por produto", subTituloAno, anoPeriodoAnual, cellCabecalhoTop, cellCabecalhoMid, cellCabecalhoTopAno);
					
					sheetPaisMensal = ExcelUtil.preencherDadosPaisMensalAcumulado(sheetPaisMensal, relatorioMensalList, listaSomatorioAnoMensal, listaSomatorioVariacaoMensal, cellDadosPaises, cellDadosGerais, cellCabecalhoBot, "produto");
					sheetPaisAcumulado = ExcelUtil.preencherDadosPaisMensalAcumulado(sheetPaisAcumulado, relatorioAcumuladoList, listaSomatorioAnoAcumulado, listaSomatorioVariacaoAcumulado, cellDadosPaises, cellDadosGerais, cellCabecalhoBot, "produto");
					sheetPaisAno = ExcelUtil.preencherDadosPaisAnual(sheetPaisAno, relatorioAnualList, listaSomatorioAnoAnual, cellDadosPaisesAnual, cellDadosGeraisAnual, cellCabecalhoBotAnual, "produto");
				
//					sheetPaisMensal.setZoom(85);
//					sheetPaisAcumulado.setZoom(85);
//					sheetPaisAno.setZoom(85);
					
					sheetPaisMensal.setDisplayGridlines(false);
					sheetPaisAcumulado.setDisplayGridlines(false);
					sheetPaisAno.setDisplayGridlines(false);
					
				} else if(tipoRelatorio.equalsIgnoreCase("empresa")) {
					
					List<AbicsEmpresa> listaEmpresasObj = AbicsEmpresa.findAll();
					
					for(AbicsEmpresa empresa: listaEmpresasObj) {
						
						RelatorioExcelMensalAcumulado relatorioMensal = new RelatorioExcelMensalAcumulado();
						RelatorioExcelMensalAcumulado relatorioAcumulado = new RelatorioExcelMensalAcumulado();
						
						for(int i=0; i<ANOS_PAIS_MENSAL_ACUMULADO; i++) {
							Integer anosMensal = anoInteger - i;
							
							AbicsExportacaoEmpresa abicsEmpresaAntigo = AbicsExportacaoEmpresa.findByAnoMesEmpresaId(anosMensal, mesInteger, empresa.getId());
							
							relatorioMensal.setPais(empresa.getNome().toUpperCase());
							relatorioAcumulado.setPais(empresa.getNome().toUpperCase());
							
							if(abicsEmpresaAntigo != null) {
								if(i == 0) {
									relatorioMensal.setPesoAno1(abicsEmpresaAntigo.getPeso());
									relatorioMensal.setReceitaAno1(abicsEmpresaAntigo.getReceita());
									relatorioMensal.setSaca60kgAno1(abicsEmpresaAntigo.getSaca60kg());
									somaPesoAno1Mensal += abicsEmpresaAntigo.getPeso();
									somaSacaAno1Mensal += abicsEmpresaAntigo.getSaca60kg();
									somaReceitaAno1Mensal += abicsEmpresaAntigo.getReceita();
									
								} else if(i == 1) {
									relatorioMensal.setPesoAno2(abicsEmpresaAntigo.getPeso());
									relatorioMensal.setReceitaAno2(abicsEmpresaAntigo.getReceita());
									relatorioMensal.setSaca60kgAno2(abicsEmpresaAntigo.getSaca60kg());
									somaPesoAno2Mensal += abicsEmpresaAntigo.getPeso();
									somaSacaAno2Mensal += abicsEmpresaAntigo.getSaca60kg();
									somaReceitaAno2Mensal += abicsEmpresaAntigo.getReceita();
									
								} else if(i == 2) {
									relatorioMensal.setPesoAno3(abicsEmpresaAntigo.getPeso());
									relatorioMensal.setReceitaAno3(abicsEmpresaAntigo.getReceita());
									relatorioMensal.setSaca60kgAno3(abicsEmpresaAntigo.getSaca60kg());
									somaPesoAno3Mensal += abicsEmpresaAntigo.getPeso();
									somaSacaAno3Mensal += abicsEmpresaAntigo.getSaca60kg();
									somaReceitaAno3Mensal += abicsEmpresaAntigo.getReceita();
								}
							}
							
							Long pesoAcumuladoAntigo = 0L;
							Long receitaAcumuladaAntigo = 0L;
							Long saca60kgAcumuladaAntigo = 0L;
							
							for(int j=0; j<mesInteger; j++) {
								AbicsExportacaoEmpresa abicsEmpresaAcumulado = AbicsExportacaoEmpresa.findByAnoMesEmpresaId(anosMensal, j+1, empresa.getId());
								
								if(abicsEmpresaAcumulado != null) {
									pesoAcumuladoAntigo += abicsEmpresaAcumulado.getPeso();
									receitaAcumuladaAntigo += abicsEmpresaAcumulado.getReceita();
									saca60kgAcumuladaAntigo += abicsEmpresaAcumulado.getSaca60kg();
								}
							}
							
							if(i == 0) {
								relatorioAcumulado.setPesoAno1(pesoAcumuladoAntigo);
								relatorioAcumulado.setReceitaAno1(receitaAcumuladaAntigo);
								relatorioAcumulado.setSaca60kgAno1(saca60kgAcumuladaAntigo);
								somaPesoAno1Acumulado += pesoAcumuladoAntigo;
								somaSacaAno1Acumulado += saca60kgAcumuladaAntigo;
								somaReceitaAno1Acumulado += receitaAcumuladaAntigo;
							} else if(i == 1) {
								relatorioAcumulado.setPesoAno2(pesoAcumuladoAntigo);
								relatorioAcumulado.setReceitaAno2(receitaAcumuladaAntigo);
								relatorioAcumulado.setSaca60kgAno2(saca60kgAcumuladaAntigo);
								somaPesoAno2Acumulado += pesoAcumuladoAntigo;
								somaSacaAno2Acumulado += saca60kgAcumuladaAntigo;
								somaReceitaAno2Acumulado += receitaAcumuladaAntigo;
							} else if(i == 2) {
								relatorioAcumulado.setPesoAno3(pesoAcumuladoAntigo);
								relatorioAcumulado.setReceitaAno3(receitaAcumuladaAntigo);
								relatorioAcumulado.setSaca60kgAno3(saca60kgAcumuladaAntigo);
								somaPesoAno3Acumulado += pesoAcumuladoAntigo;
								somaSacaAno3Acumulado += saca60kgAcumuladaAntigo;
								somaReceitaAno3Acumulado += receitaAcumuladaAntigo;
							}
							
							if(!anosMensal.equals(anoInteger)) {
								
								AbicsExportacaoEmpresa abicsEmpresa = AbicsExportacaoEmpresa.findByAnoMesEmpresaId(anoInteger, mesInteger, empresa.getId());
								
								if(abicsEmpresa == null) {
									abicsEmpresa = new AbicsExportacaoEmpresa();
									abicsEmpresa.setPeso(0L);
									abicsEmpresa.setReceita(0L);
									abicsEmpresa.setSaca60kg(0L);
								}
								
								abicsEmpresa = calcularVariacaoEmpresa(abicsEmpresaAntigo, abicsEmpresa);
								
								Long pesoAcumuladoNovo = 0L;
								Long receitaAcumuladaNovo = 0L;
								Long saca60kgAcumuladaNovo = 0L;
								
								for(int j=0; j<mesInteger; j++) {
									AbicsExportacaoEmpresa abicsEmpresaAcumulado = AbicsExportacaoEmpresa.findByAnoMesEmpresaId(anoInteger, j+1, empresa.getId());
									
									if(abicsEmpresaAcumulado != null) {
										pesoAcumuladoNovo += abicsEmpresaAcumulado.getPeso();
										receitaAcumuladaNovo += abicsEmpresaAcumulado.getReceita();
										saca60kgAcumuladaNovo += abicsEmpresaAcumulado.getSaca60kg();
									}
								}
								
								AbicsExportacaoEmpresa abicsAcumuladoNovo = new AbicsExportacaoEmpresa();
								
								abicsAcumuladoNovo.setPeso(pesoAcumuladoNovo);
								abicsAcumuladoNovo.setReceita(receitaAcumuladaNovo);
								abicsAcumuladoNovo.setSaca60kg(saca60kgAcumuladaNovo);
								
								AbicsExportacaoEmpresa abicsAcumuladoAntigo = new AbicsExportacaoEmpresa();
								
								abicsAcumuladoAntigo.setPeso(pesoAcumuladoAntigo);
								abicsAcumuladoAntigo.setReceita(receitaAcumuladaAntigo);
								abicsAcumuladoAntigo.setSaca60kg(saca60kgAcumuladaAntigo);
								
								abicsAcumuladoNovo = calcularVariacaoEmpresa(abicsAcumuladoAntigo, abicsAcumuladoNovo);
								
								if(i == 1) { 
									relatorioMensal.setPesoVariacaoAno1(abicsEmpresa.getPeso());
									relatorioMensal.setReceitaVariacaoAno1(abicsEmpresa.getReceita());
									relatorioMensal.setSaca60kgVariacaoAno1(abicsEmpresa.getSaca60kg());
									
									relatorioAcumulado.setPesoVariacaoAno1(abicsAcumuladoNovo.getPeso());
									relatorioAcumulado.setReceitaVariacaoAno1(abicsAcumuladoNovo.getReceita());
									relatorioAcumulado.setSaca60kgVariacaoAno1(abicsAcumuladoNovo.getSaca60kg());
									
								} else if(i == 2) {
									relatorioMensal.setPesoVariacaoAno2(abicsEmpresa.getPeso());
									relatorioMensal.setReceitaVariacaoAno2(abicsEmpresa.getReceita());
									relatorioMensal.setSaca60kgVariacaoAno2(abicsEmpresa.getSaca60kg());
									
									relatorioAcumulado.setPesoVariacaoAno2(abicsAcumuladoNovo.getPeso());
									relatorioAcumulado.setReceitaVariacaoAno2(abicsAcumuladoNovo.getReceita());
									relatorioAcumulado.setSaca60kgVariacaoAno2(abicsAcumuladoNovo.getSaca60kg());
								}
							}
						}
						
						if(!relatorioMensal.getPesoAno1().equals(0L) || !relatorioMensal.getPesoAno2().equals(0L)
								|| !relatorioMensal.getPesoAno3().equals(0L)) {
							
							relatorioMensalList.add(relatorioMensal);
						}
						
						if(!relatorioAcumulado.getPesoAno1().equals(0L) || !relatorioAcumulado.getPesoAno2().equals(0L)
								|| !relatorioAcumulado.getPesoAno3().equals(0L)) {
							
							relatorioAcumuladoList.add(relatorioAcumulado);
						}
					}
					
					//add na lista de total mensal
					listaSomatorioAnoMensal.add(somaPesoAno1Mensal);
					listaSomatorioAnoMensal.add(somaPesoAno2Mensal);
					listaSomatorioAnoMensal.add(somaPesoAno3Mensal);
					
					listaSomatorioAnoMensal.add(somaSacaAno1Mensal);
					listaSomatorioAnoMensal.add(somaSacaAno2Mensal);
					listaSomatorioAnoMensal.add(somaSacaAno3Mensal);
					
					listaSomatorioAnoMensal.add(somaReceitaAno1Mensal);
					listaSomatorioAnoMensal.add(somaReceitaAno2Mensal);
					listaSomatorioAnoMensal.add(somaReceitaAno3Mensal);
					
					//add na lista de variação dos totais mensal
					AbicsExportacaoEmpresa abicsAno1 = new AbicsExportacaoEmpresa();
					abicsAno1.setPeso(somaPesoAno1Mensal);
					abicsAno1.setReceita(somaReceitaAno1Mensal);
					abicsAno1.setSaca60kg(somaSacaAno1Mensal);
					
					AbicsExportacaoEmpresa abicsAno2 = new AbicsExportacaoEmpresa();
					abicsAno2.setPeso(somaPesoAno2Mensal);
					abicsAno2.setReceita(somaReceitaAno2Mensal);
					abicsAno2.setSaca60kg(somaSacaAno2Mensal);
					
					AbicsExportacaoEmpresa abicsAno3 = new AbicsExportacaoEmpresa();
					abicsAno3.setPeso(somaPesoAno3Mensal);
					abicsAno3.setReceita(somaReceitaAno3Mensal);
					abicsAno3.setSaca60kg(somaSacaAno3Mensal);
					
					AbicsExportacaoEmpresa abicsVariacaoMensal1 = calcularVariacaoEmpresa(abicsAno2, abicsAno1);
					AbicsExportacaoEmpresa abicsVariacaoMensal2 = calcularVariacaoEmpresa(abicsAno3, abicsAno1);
					
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal1.getPeso());
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal2.getPeso());
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal1.getSaca60kg());
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal2.getSaca60kg());
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal1.getReceita());
					listaSomatorioVariacaoMensal.add(abicsVariacaoMensal2.getReceita());
					
					//add na lista de total acumulado
					listaSomatorioAnoAcumulado.add(somaPesoAno1Acumulado);
					listaSomatorioAnoAcumulado.add(somaPesoAno2Acumulado);
					listaSomatorioAnoAcumulado.add(somaPesoAno3Acumulado);
					
					listaSomatorioAnoAcumulado.add(somaSacaAno1Acumulado);
					listaSomatorioAnoAcumulado.add(somaSacaAno2Acumulado);
					listaSomatorioAnoAcumulado.add(somaSacaAno3Acumulado);
					
					listaSomatorioAnoAcumulado.add(somaReceitaAno1Acumulado);
					listaSomatorioAnoAcumulado.add(somaReceitaAno2Acumulado);
					listaSomatorioAnoAcumulado.add(somaReceitaAno3Acumulado);
					
					abicsAno1.setPeso(somaPesoAno1Acumulado);
					abicsAno1.setReceita(somaReceitaAno1Acumulado);
					abicsAno1.setSaca60kg(somaSacaAno1Acumulado);
					
					abicsAno2.setPeso(somaPesoAno2Acumulado);
					abicsAno2.setReceita(somaReceitaAno2Acumulado);
					abicsAno2.setSaca60kg(somaSacaAno2Acumulado);
					
					abicsAno3.setPeso(somaPesoAno3Acumulado);
					abicsAno3.setReceita(somaReceitaAno3Acumulado);
					abicsAno3.setSaca60kg(somaSacaAno3Acumulado);
					
					//add na lista de variação dos totais acumulado
					AbicsExportacaoEmpresa abicsVariacaoAcumulado1 = calcularVariacaoEmpresa(abicsAno2, abicsAno1);
					AbicsExportacaoEmpresa abicsVariacaoAcumulado2 = calcularVariacaoEmpresa(abicsAno3, abicsAno1);
					
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado1.getPeso());
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado2.getPeso());
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado1.getSaca60kg());
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado2.getSaca60kg());
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado1.getReceita());
					listaSomatorioVariacaoAcumulado.add(abicsVariacaoAcumulado2.getReceita());
					
					tituloMensal = "Brasil: Exportações de café solúvel por destino";
					tituloPeriodoMensal = "Período: Janeiro a " + meses[mesInteger];
					tituloAlfandegaMensal = "Data de alfandega";
					subTituloMensal = meses[mesInteger];
					
					Collections.sort(relatorioMensalList, new Comparator<RelatorioExcelMensalAcumulado>() {
				        @Override
				        public int compare(RelatorioExcelMensalAcumulado peso1, RelatorioExcelMensalAcumulado peso2)
				        {
				        	Long peso1Long = peso1.getPesoAno1();
				        	Long peso2Long = peso2.getPesoAno1();
				            return peso2Long.compareTo(peso1Long);
				        }
					});
					Collections.sort(relatorioAcumuladoList, new Comparator<RelatorioExcelMensalAcumulado>() {
				        @Override
				        public int compare(RelatorioExcelMensalAcumulado peso1, RelatorioExcelMensalAcumulado peso2)
				        {
				        	Long peso1Long = peso1.getPesoAno1();
				        	Long peso2Long = peso2.getPesoAno1();
				            return peso2Long.compareTo(peso1Long);
				        }
					});
					
					HSSFSheet sheetPaisMensalAcumulado = ExcelUtil.criarSheetComCabecalhoRelatorioAbicsData(woorkBook, "Exportador mensal e acumulado", my_picture_id, 
							tituloMensal, tituloPeriodoMensal, tituloAlfandegaMensal, styleCabecalhoGeral1, styleCabecalhoGeral2, styleCabecalhoGeral3, "empresa");
					
					sheetPaisMensalAcumulado = ExcelUtil.criarCelulaTipoSelecionadoMensalAcumuladoRelatorioAbicsData(sheetPaisMensalAcumulado, 
							"EMPRESA", subTituloMensal, anoPeriodoMensalAcumulado, cellCabecalhoTop, cellCabecalhoMid, cellCabecalhoTopAno);
					
					subTituloMensal = "Janeiro a " + meses[mesInteger];
					
					sheetPaisMensalAcumulado = ExcelUtil.criarCelulaTipoSelecionadoMensalAcumulado2RelatorioAbicsData(sheetPaisMensalAcumulado, 
							"EMPRESA", subTituloMensal, anoPeriodoMensalAcumulado, relatorioMensalList.size(), cellCabecalhoTop, cellCabecalhoMid, cellCabecalhoTopAno);
				
					sheetPaisMensalAcumulado = ExcelUtil.preencherDadosEmpresaMensalAcumulado(sheetPaisMensalAcumulado, relatorioMensalList, listaSomatorioAnoMensal, listaSomatorioVariacaoMensal, relatorioAcumuladoList, listaSomatorioAnoAcumulado, listaSomatorioVariacaoAcumulado, cellDadosPaises, cellDadosGerais, cellCabecalhoBot);
				
					sheetPaisMensalAcumulado.setDisplayGridlines(false);
				}
			}
			
			FileOutputStream fileOut = new FileOutputStream(excelFileName);

			woorkBook.write(fileOut);
			fileOut.flush();
			fileOut.close();

			byte[] contents = IOUtils.toByteArray(new FileInputStream(
					excelFileName));
			return ok(contents);

		} catch (Exception e) {
			e.printStackTrace();
			return ok(Json.toJson("0"));
		}
	}
	
	public static AbicsExportacaoPais calcularVariacaoPais(AbicsExportacaoPais abicsAntigo, AbicsExportacaoPais abicsNovo) {
		
		Double valorInicialPeso = 0.0;
		Double valorFinalPeso = 0.0;
		
		Double valorInicialReceita = 0.0;
		Double valorFinalReceita = 0.0;
		
		Double valorInicialSaca60kg = 0.0;
		Double valorFinalSaca60kg = 0.0;
		
		if(abicsNovo != null) {
			valorFinalPeso = Double.valueOf(abicsNovo.getPeso());
			valorFinalReceita = Double.valueOf(abicsNovo.getReceita());
			valorFinalSaca60kg = Double.valueOf(abicsNovo.getSaca60kg());
		}
		
		if(abicsAntigo != null) {
			valorInicialPeso = Double.valueOf(abicsAntigo.getPeso());
			valorInicialReceita = Double.valueOf(abicsAntigo.getReceita());
			valorInicialSaca60kg = Double.valueOf(abicsAntigo.getSaca60kg());
		}
		
		Double valorPeso = 0.0;
		
		if(valorFinalPeso.equals(0.0) && !valorInicialPeso.equals(0.0)) {
			valorPeso = -100.0;
			
		} else if(!valorFinalPeso.equals(0.0) && !valorInicialPeso.equals(0.0)) {
			valorPeso = (((double) (valorFinalPeso / valorInicialPeso)) * 100) -100;
			
		} else if(!valorFinalPeso.equals(0.0) && valorInicialPeso.equals(0.0)) {
			valorPeso = -66893823023.0;
			
		} else if(valorFinalPeso.equals(0.0) && valorInicialPeso.equals(0.0)) {
			valorPeso = -66893823023.0;
		}
		
		Double valorReceita = 0.0;
		
		if(valorFinalReceita.equals(0.0) && !valorInicialReceita.equals(0.0)) {
			valorReceita = -100.0;
			
		} else if(!valorFinalReceita.equals(0.0) && !valorInicialReceita.equals(0.0)) {
			valorReceita = (((double) (valorFinalReceita / valorInicialReceita)) * 100) -100;
			
		} else if(!valorFinalReceita.equals(0.0) && valorInicialReceita.equals(0.0)) {
			valorReceita = -66893823023.0;
			
		} else if(valorFinalReceita.equals(0.0) && valorInicialReceita.equals(0.0)) {
			valorReceita = -66893823023.0;
		}
		
		Double valorSaca60kg = 0.0;
		
		if(valorFinalSaca60kg.equals(0.0) && !valorInicialSaca60kg.equals(0.0)) {
			valorSaca60kg = -100.0;
			
		} else if(!valorFinalSaca60kg.equals(0.0) && !valorInicialSaca60kg.equals(0.0)) {
			valorSaca60kg = (((double) (valorFinalSaca60kg / valorInicialSaca60kg)) * 100) -100;
			
		} else if(!valorFinalSaca60kg.equals(0.0) && valorInicialSaca60kg.equals(0.0)) {
			valorSaca60kg = -66893823023.0;
			
		} else if(valorFinalSaca60kg.equals(0.0) && valorInicialSaca60kg.equals(0.0)) {
			valorSaca60kg = -66893823023.0;
		}
		
		AbicsExportacaoPais variacao = new AbicsExportacaoPais();
		
		variacao.setPeso(valorPeso.longValue());
		variacao.setReceita(valorReceita.longValue());
		variacao.setSaca60kg(valorSaca60kg.longValue());
		
		return variacao;
	
	}
	
	public static AbicsExportacaoProduto calcularVariacaoProduto(AbicsExportacaoProduto abicsAntigo, AbicsExportacaoProduto abicsNovo) {
		
		Double valorInicialPeso = 0.0;
		Double valorFinalPeso = 0.0;
		
		Double valorInicialReceita = 0.0;
		Double valorFinalReceita = 0.0;
		
		Double valorInicialSaca60kg = 0.0;
		Double valorFinalSaca60kg = 0.0;
		
		if(abicsNovo != null) {
			valorFinalPeso = Double.valueOf(abicsNovo.getPeso());
			valorFinalReceita = Double.valueOf(abicsNovo.getReceita());
			valorFinalSaca60kg = Double.valueOf(abicsNovo.getSaca60kg());
		}
		
		if(abicsAntigo != null) {
			valorInicialPeso = Double.valueOf(abicsAntigo.getPeso());
			valorInicialReceita = Double.valueOf(abicsAntigo.getReceita());
			valorInicialSaca60kg = Double.valueOf(abicsAntigo.getSaca60kg());
		}
		
		Double valorPeso = 0.0;
		
		if(valorFinalPeso.equals(0.0) && !valorInicialPeso.equals(0.0)) {
			valorPeso = -100.0;
			
		} else if(!valorFinalPeso.equals(0.0) && !valorInicialPeso.equals(0.0)) {
			valorPeso = (((double) (valorFinalPeso / valorInicialPeso)) * 100) -100;
			
		} else if(!valorFinalPeso.equals(0.0) && valorInicialPeso.equals(0.0)) {
			valorPeso = -66893823023.0;
			
		} else if(valorFinalPeso.equals(0.0) && valorInicialPeso.equals(0.0)) {
			valorPeso = -66893823023.0;
		}
		
		Double valorReceita = 0.0;
		
		if(valorFinalReceita.equals(0.0) && !valorInicialReceita.equals(0.0)) {
			valorReceita = -100.0;
			
		} else if(!valorFinalReceita.equals(0.0) && !valorInicialReceita.equals(0.0)) {
			valorReceita = (((double) (valorFinalReceita / valorInicialReceita)) * 100) -100;
			
		} else if(!valorFinalReceita.equals(0.0) && valorInicialReceita.equals(0.0)) {
			valorReceita = -66893823023.0;
			
		} else if(valorFinalReceita.equals(0.0) && valorInicialReceita.equals(0.0)) {
			valorReceita = -66893823023.0;
		}
		
		Double valorSaca60kg = 0.0;
		
		if(valorFinalSaca60kg.equals(0.0) && !valorInicialSaca60kg.equals(0.0)) {
			valorSaca60kg = -100.0;
			
		} else if(!valorFinalSaca60kg.equals(0.0) && !valorInicialSaca60kg.equals(0.0)) {
			valorSaca60kg = (((double) (valorFinalSaca60kg / valorInicialSaca60kg)) * 100) -100;
			
		} else if(!valorFinalSaca60kg.equals(0.0) && valorInicialSaca60kg.equals(0.0)) {
			valorSaca60kg = -66893823023.0;
			
		} else if(valorFinalSaca60kg.equals(0.0) && valorInicialSaca60kg.equals(0.0)) {
			valorSaca60kg = -66893823023.0;
		}
		
		AbicsExportacaoProduto variacao = new AbicsExportacaoProduto();
		
		variacao.setPeso(valorPeso.longValue());
		variacao.setReceita(valorReceita.longValue());
		variacao.setSaca60kg(valorSaca60kg.longValue());
		
		return variacao;
	
	}
	
	public static AbicsExportacaoEmpresa calcularVariacaoEmpresa(AbicsExportacaoEmpresa abicsAntigo, AbicsExportacaoEmpresa abicsNovo) {
		
		Double valorInicialPeso = 0.0;
		Double valorFinalPeso = 0.0;
		
		Double valorInicialReceita = 0.0;
		Double valorFinalReceita = 0.0;
		
		Double valorInicialSaca60kg = 0.0;
		Double valorFinalSaca60kg = 0.0;
		
		if(abicsNovo != null) {
			valorFinalPeso = Double.valueOf(abicsNovo.getPeso());
			valorFinalReceita = Double.valueOf(abicsNovo.getReceita());
			valorFinalSaca60kg = Double.valueOf(abicsNovo.getSaca60kg());
		}
		
		if(abicsAntigo != null) {
			valorInicialPeso = Double.valueOf(abicsAntigo.getPeso());
			valorInicialReceita = Double.valueOf(abicsAntigo.getReceita());
			valorInicialSaca60kg = Double.valueOf(abicsAntigo.getSaca60kg());
		}
		
		Double valorPeso = 0.0;
		
		if(valorFinalPeso.equals(0.0) && !valorInicialPeso.equals(0.0)) {
			valorPeso = -100.0;
			
		} else if(!valorFinalPeso.equals(0.0) && !valorInicialPeso.equals(0.0)) {
			valorPeso = (((double) (valorFinalPeso / valorInicialPeso)) * 100) -100;
			
		} else if(!valorFinalPeso.equals(0.0) && valorInicialPeso.equals(0.0)) {
			valorPeso = -66893823023.0;
			
		} else if(valorFinalPeso.equals(0.0) && valorInicialPeso.equals(0.0)) {
			valorPeso = -66893823023.0;
		}
		
		Double valorReceita = 0.0;
		
		if(valorFinalReceita.equals(0.0) && !valorInicialReceita.equals(0.0)) {
			valorReceita = -100.0;
			
		} else if(!valorFinalReceita.equals(0.0) && !valorInicialReceita.equals(0.0)) {
			valorReceita = (((double) (valorFinalReceita / valorInicialReceita)) * 100) -100;
			
		} else if(!valorFinalReceita.equals(0.0) && valorInicialReceita.equals(0.0)) {
			valorReceita = -66893823023.0;
			
		} else if(valorFinalReceita.equals(0.0) && valorInicialReceita.equals(0.0)) {
			valorReceita = -66893823023.0;
		}
		
		Double valorSaca60kg = 0.0;
		
		if(valorFinalSaca60kg.equals(0.0) && !valorInicialSaca60kg.equals(0.0)) {
			valorSaca60kg = -100.0;
			
		} else if(!valorFinalSaca60kg.equals(0.0) && !valorInicialSaca60kg.equals(0.0)) {
			valorSaca60kg = (((double) (valorFinalSaca60kg / valorInicialSaca60kg)) * 100) -100;
			
		} else if(!valorFinalSaca60kg.equals(0.0) && valorInicialSaca60kg.equals(0.0)) {
			valorSaca60kg = -66893823023.0;
			
		} else if(valorFinalSaca60kg.equals(0.0) && valorInicialSaca60kg.equals(0.0)) {
			valorSaca60kg = -66893823023.0;
		}
		
		AbicsExportacaoEmpresa variacao = new AbicsExportacaoEmpresa();
		
		variacao.setPeso(valorPeso.longValue());
		variacao.setReceita(valorReceita.longValue());
		variacao.setSaca60kg(valorSaca60kg.longValue());
		
		return variacao;
	}

}
