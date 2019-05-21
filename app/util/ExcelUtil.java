package util;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.util.CellRangeAddress;

import entitys.report.RelatorioExcelAnual;
import entitys.report.RelatorioExcelMensalAcumulado;

/**
 * 
 * Classe responsavel para criacao de dados para o relatorio (.EXCEL)
 * 
 * 
 * */
public class ExcelUtil {

	public static HSSFSheet criarSheetComCabecalhoRelatorioAbicsData(HSSFWorkbook woorkBook, String titulo, int my_picture_id, 
			String tituloCelula, String periodoCelula, String dataAlfandegaCelula, CellStyle styleCabecalhoGeral1, 
			CellStyle styleCabecalhoGeral2, CellStyle styleCabecalhoGeral3, String tipoRelatorio) {
		
		HSSFSheet sheet = woorkBook.createSheet(titulo);
		
		HSSFPatriarch drawing = sheet.createDrawingPatriarch();
		ClientAnchor my_anchor = new HSSFClientAnchor();
		my_anchor.setCol1(1);
		my_anchor.setCol2(1);
		my_anchor.setRow1(0);
		my_anchor.setRow2(8);
		
		Picture pic = drawing.createPicture(my_anchor, my_picture_id);
		
		if(tipoRelatorio.equals("mes")) {
			pic.resize(0.8, 0.65);
			
		} else if(tipoRelatorio.equals("pais")){
			pic.resize(0.32, 0.65);
		} else if(tipoRelatorio.equals("empresa")){
			pic.resize(0.25, 0.65);
		} else if(tipoRelatorio.equals("produto")){
			pic.resize(0.25, 0.65);
		}
		
		HSSFRow row2 = sheet.createRow(0);
		HSSFCell cellTitulo = row2.createCell(3);
	    cellTitulo.setCellValue(tituloCelula);
		cellTitulo.setCellStyle(styleCabecalhoGeral1);
		
		HSSFCell cellBlank = row2.createCell(2);
		cellBlank.setCellStyle(styleCabecalhoGeral1);
		
		HSSFRow row3 = sheet.createRow(2);
		HSSFCell cellPeriodo = row3.createCell(3);
		cellPeriodo.setCellValue(periodoCelula);
		cellPeriodo.setCellStyle(styleCabecalhoGeral2);

		HSSFRow row4 = sheet.createRow(4);
		HSSFCell cellDataAlfandega = row4.createCell(3);
		cellDataAlfandega.setCellValue(dataAlfandegaCelula);
		cellDataAlfandega.setCellStyle(styleCabecalhoGeral3);
		
//		imagem
		sheet.addMergedRegion(new CellRangeAddress(0, 4, 1, 1));
		
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 22));
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 3, 22));
//		sheet.addMergedRegion(new CellRangeAddress(4, 5, 3, 22));
		
		return sheet;
	}
	
	
	public static HSSFSheet criarCelulaTipoSelecionadoMensalAcumuladoRelatorioAbicsData(HSSFSheet sheet, String tituloDestino, 
			String tituloPeriodo, Integer[] periodoArr, CellStyle cellTop, CellStyle cellMid, CellStyle cellTopAno) {
		
		HSSFRow row8 = sheet.createRow(6);
		HSSFRow row9 = sheet.createRow(7);
		HSSFRow row10 = sheet.createRow(8);
		
		HSSFCell cellTituloDestino = row9.createCell(1);
	    cellTituloDestino.setCellValue(tituloDestino.toUpperCase());
		cellTituloDestino.setCellStyle(cellTop);
		sheet.addMergedRegion(new CellRangeAddress(7, 8, 1, 1));
		
		for(int i = 0; i < 3; i++) {
			
			int modificador = i * 7;
			String tituloCelula = "";
			if(i == 0) {
				tituloCelula = "Peso Líquido (KG)";
			} else if(i == 1) {
				tituloCelula = "Equivalente em sacas 60kg";
			} else if(i == 2) {
				tituloCelula = "Receita Cambial US$";
			}
			
			HSSFCell cellPesoLiquido = row8.createCell(3 + modificador);
			cellPesoLiquido.setCellValue(tituloCelula);
			cellPesoLiquido.setCellStyle(cellTop);
			sheet.addMergedRegion(new CellRangeAddress(6, 6, 3 + modificador, 5 + modificador));
			
			HSSFCell cellPesoPeriodo = row9.createCell(3 + modificador);
			cellPesoPeriodo.setCellValue(tituloPeriodo);
			cellPesoPeriodo.setCellStyle(cellMid);
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 3 + modificador, 5 + modificador));
			
			HSSFCell cellVariacao = row8.createCell(7 + modificador);
			cellVariacao.setCellValue("Variação %");
			cellVariacao.setCellStyle(cellTop);
			sheet.addMergedRegion(new CellRangeAddress(6, 7, 7 + modificador, 8 + modificador));
			
			for(int j=0; j<periodoArr.length; j++) {
				HSSFCell cellPesoAno = row10.createCell((3 + modificador) + j);
				cellPesoAno.setCellValue(periodoArr[j]);
				cellPesoAno.setCellStyle(cellTopAno);
				
				if(j != periodoArr.length -1) {
					HSSFCell cellVariacaoAno = row10.createCell((7 + modificador) + j);
					cellVariacaoAno.setCellValue(periodoArr[0] + " x " + periodoArr[j+1]);
					cellVariacaoAno.setCellStyle(cellTopAno);
				}
			}
		}
		
//		sheet.createFreezePane(2, 11);
		
		return sheet;
	}
	
	public static HSSFSheet criarCelulaTipoSelecionadoMensalAcumulado2RelatorioAbicsData(HSSFSheet sheet, String tituloDestino, 
			String tituloPeriodo, Integer[] periodoArr, int tamanhoRelatorioAnterior, CellStyle cellTop, CellStyle cellMid, CellStyle cellTopAno) {
		
		HSSFRow row8 = sheet.createRow(tamanhoRelatorioAnterior + 12);
		HSSFRow row9 = sheet.createRow(tamanhoRelatorioAnterior + 13);
		HSSFRow row10 = sheet.createRow(tamanhoRelatorioAnterior + 14);
		
		HSSFCell cellTituloDestino = row9.createCell(1);
	    cellTituloDestino.setCellValue(tituloDestino.toUpperCase());
		cellTituloDestino.setCellStyle(cellTop);
		sheet.addMergedRegion(new CellRangeAddress(tamanhoRelatorioAnterior + 13, tamanhoRelatorioAnterior + 14, 1, 1));
		
		for(int i = 0; i < 3; i++) {
			
			int modificador = i * 7;
			String tituloCelula = "";
			if(i == 0) {
				tituloCelula = "Peso Líquido (KG)";
			} else if(i == 1) {
				tituloCelula = "Equivalente em sacas 60kg";
			} else if(i == 2) {
				tituloCelula = "Receita Cambial US$";
			}
			
			HSSFCell cellPesoLiquido = row8.createCell(3 + modificador);
			cellPesoLiquido.setCellValue(tituloCelula);
			cellPesoLiquido.setCellStyle(cellTop);
			sheet.addMergedRegion(new CellRangeAddress(tamanhoRelatorioAnterior + 12, tamanhoRelatorioAnterior + 12, 3 + modificador, 5 + modificador));
			
			HSSFCell cellPesoPeriodo = row9.createCell(3 + modificador);
			cellPesoPeriodo.setCellValue(tituloPeriodo);
			cellPesoPeriodo.setCellStyle(cellMid);
			sheet.addMergedRegion(new CellRangeAddress(tamanhoRelatorioAnterior + 13, tamanhoRelatorioAnterior + 13, 3 + modificador, 5 + modificador));
			
			HSSFCell cellVariacao = row8.createCell(7 + modificador);
			cellVariacao.setCellValue("Variação %");
			cellVariacao.setCellStyle(cellTop);
			sheet.addMergedRegion(new CellRangeAddress(tamanhoRelatorioAnterior + 12, tamanhoRelatorioAnterior + 13, 7 + modificador, 8 + modificador));
			
			for(int j=0; j<periodoArr.length; j++) {
				HSSFCell cellPesoAno = row10.createCell((3 + modificador) + j);
				cellPesoAno.setCellValue(periodoArr[j]);
				cellPesoAno.setCellStyle(cellTopAno);
				
				if(j != periodoArr.length -1) {
					HSSFCell cellVariacaoAno = row10.createCell((7 + modificador) + j);
					cellVariacaoAno.setCellValue(periodoArr[0] + " x " + periodoArr[j+1]);
					cellVariacaoAno.setCellStyle(cellTopAno);
				}
			}
		}
		
//		sheet.createFreezePane(2, 11);
		
		return sheet;
	}
	
	public static HSSFSheet criarCelulaTipoSelecionadoMensalRelatorioAbicsData(HSSFSheet sheet, String tituloDestino, 
			String tituloPeriodo, Integer[] periodoArr, CellStyle cellTop, CellStyle cellMid, CellStyle cellTopAno) {
		
		HSSFRow row8 = sheet.createRow(8);
		HSSFRow row9 = sheet.createRow(9);
		HSSFRow row10 = sheet.createRow(10);
		
		HSSFRow row26 = sheet.createRow(26);
		HSSFRow row27 = sheet.createRow(27);
		HSSFRow row28 = sheet.createRow(28);
		
		HSSFRow row44 = sheet.createRow(44);
		HSSFRow row45 = sheet.createRow(45);
		HSSFRow row46 = sheet.createRow(46);
		
		HSSFCell cellTituloDestino = row9.createCell(1);
	    cellTituloDestino.setCellValue(tituloDestino.toUpperCase());
		cellTituloDestino.setCellStyle(cellTop);
		sheet.addMergedRegion(new CellRangeAddress(9, 10, 1, 1));
		
		HSSFCell cellTituloDestino2 = row27.createCell(1);
		cellTituloDestino2.setCellValue(tituloDestino.toUpperCase());
		cellTituloDestino2.setCellStyle(cellTop);
		sheet.addMergedRegion(new CellRangeAddress(27, 28, 1, 1));
		
		HSSFCell cellTituloDestino3 = row45.createCell(1);
		cellTituloDestino3.setCellValue(tituloDestino.toUpperCase());
		cellTituloDestino3.setCellStyle(cellTop);
		sheet.addMergedRegion(new CellRangeAddress(45, 46, 1, 1));
		
		String tituloCelula = "";
		tituloCelula = "Peso Líquido (KG)";
		
		HSSFCell cellPesoLiquido = row8.createCell(3);
		cellPesoLiquido.setCellValue(tituloCelula);
		cellPesoLiquido.setCellStyle(cellTop);
		sheet.addMergedRegion(new CellRangeAddress(8, 8, 3, 5));
		
		HSSFCell cellPesoPeriodo = row9.createCell(3);
		cellPesoPeriodo.setCellValue(tituloPeriodo);
		cellPesoPeriodo.setCellStyle(cellMid);
		sheet.addMergedRegion(new CellRangeAddress(9, 9, 3, 5));
		
		HSSFCell cellVariacao1 = row8.createCell(7);
		cellVariacao1.setCellValue("Variação %");
		cellVariacao1.setCellStyle(cellTop);
		sheet.addMergedRegion(new CellRangeAddress(8, 9, 7, 8));
		
		tituloCelula = "Equivalente em sacas 60kg";
		
		HSSFCell cellSacas = row26.createCell(3);
		cellSacas.setCellValue(tituloCelula);
		cellSacas.setCellStyle(cellTop);
		sheet.addMergedRegion(new CellRangeAddress(26, 26, 3, 5));
		
		HSSFCell cellSacaPeriodo = row27.createCell(3);
		cellSacaPeriodo.setCellValue(tituloPeriodo);
		cellSacaPeriodo.setCellStyle(cellMid);
		sheet.addMergedRegion(new CellRangeAddress(27, 27, 3, 5));
		
		HSSFCell cellVariacao2 = row26.createCell(7);
		cellVariacao2.setCellValue("Variação %");
		cellVariacao2.setCellStyle(cellTop);
		sheet.addMergedRegion(new CellRangeAddress(26, 27, 7, 8));
		
		tituloCelula = "Receita Cambial US$";

		HSSFCell cellReceita = row44.createCell(3);
		cellReceita.setCellValue(tituloCelula);
		cellReceita.setCellStyle(cellTop);
		sheet.addMergedRegion(new CellRangeAddress(44, 44, 3, 5));
		
		HSSFCell cellReceitaPeriodo = row45.createCell(3);
		cellReceitaPeriodo.setCellValue(tituloPeriodo);
		cellReceitaPeriodo.setCellStyle(cellMid);
		sheet.addMergedRegion(new CellRangeAddress(45, 45, 3, 5));
		
		HSSFCell cellVariacao3 = row44.createCell(7);
		cellVariacao3.setCellValue("Variação %");
		cellVariacao3.setCellStyle(cellTop);
		sheet.addMergedRegion(new CellRangeAddress(44, 45, 7, 8));
		
			
		for(int j=0; j<periodoArr.length; j++) {
			HSSFCell cellPesoAno = row10.createCell((3) + j);
			cellPesoAno.setCellValue(periodoArr[j]);
			cellPesoAno.setCellStyle(cellTopAno);
			
			HSSFCell cellSacaAno = row28.createCell((3) + j);
			cellSacaAno.setCellValue(periodoArr[j]);
			cellSacaAno.setCellStyle(cellTopAno);
			
			HSSFCell cellReceitaAno = row46.createCell((3) + j);
			cellReceitaAno.setCellValue(periodoArr[j]);
			cellReceitaAno.setCellStyle(cellTopAno);
			
			if(j != periodoArr.length -1) {
				HSSFCell cellVariacaoAno = row10.createCell((7) + j);
				cellVariacaoAno.setCellValue(periodoArr[0] + " x " + periodoArr[j+1]);
				cellVariacaoAno.setCellStyle(cellTopAno);
				
				HSSFCell cellVariacaoAno2 = row28.createCell((7) + j);
				cellVariacaoAno2.setCellValue(periodoArr[0] + " x " + periodoArr[j+1]);
				cellVariacaoAno2.setCellStyle(cellTopAno);
				
				HSSFCell cellVariacaoAno3 = row46.createCell((7) + j);
				cellVariacaoAno3.setCellValue(periodoArr[0] + " x " + periodoArr[j+1]);
				cellVariacaoAno3.setCellStyle(cellTopAno);
			}
		}
		
//		sheet.createFreezePane(2, 11);
		
		return sheet;
	}
	
	public static HSSFSheet criarCelulaTipoSelecionadoAnoRelatorioAbicsData(HSSFSheet sheet, String tituloDestino, 
			String tituloPeriodo, Integer[] periodoArr, CellStyle cellTop, CellStyle cellMid, CellStyle cellTopAno) {
		
		HSSFRow row8 = sheet.createRow(6);
		HSSFRow row9 = sheet.createRow(7);
		HSSFRow row10 = sheet.createRow(8);
		
		HSSFCell cellTituloDestino = row9.createCell(1);
	    cellTituloDestino.setCellValue(tituloDestino.toUpperCase());
		cellTituloDestino.setCellStyle(cellTop);
		sheet.addMergedRegion(new CellRangeAddress(7, 8, 1, 1));
		
		for(int j = 0; j < 2; j++) {
			int modificador = j * 10;
			String tituloCelula = "";
			
			if(j == 0) {
				tituloCelula = "Peso Líquido ( x 1000 Kg )";
			} else if(j == 1) {
				tituloCelula = "Receita Cambial ( x 1000 US$ )";
			}
			
			HSSFCell cellPesoLiquido = row8.createCell(3 + modificador);
			cellPesoLiquido.setCellValue(tituloCelula);
			cellPesoLiquido.setCellStyle(cellTop);
			sheet.addMergedRegion(new CellRangeAddress(6, 6, 3 + modificador, 11 + modificador));
			
			HSSFCell cellPesoPeriodo = row9.createCell(3 + modificador);
			cellPesoPeriodo.setCellValue(tituloPeriodo);
			cellPesoPeriodo.setCellStyle(cellMid);
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 3 + modificador, 11 + modificador));
			
			int contadorInverso = periodoArr.length - 1;
			for(int i=0; i<periodoArr.length; i++) {
				HSSFCell cellPesoAno = row10.createCell((3 + modificador) + i);
				cellPesoAno.setCellValue(periodoArr[contadorInverso]);
				cellPesoAno.setCellStyle(cellTopAno);
				
				contadorInverso--;
			}
		}
		
//		sheet.createFreezePane(2, 11);
		
		return sheet;
	}
	
	public static HSSFSheet preencherDadosPaisMensalAcumulado(HSSFSheet sheet, List<RelatorioExcelMensalAcumulado> objetoList
			, List<Long> listaSomaAno, List<Long> listaSomaVariacao, CellStyle cellDadosPaises, CellStyle cellDadosGerais, CellStyle cellDadosSomatorio, String tipoRelatorio) {
		
		for(int i = 0; i < objetoList.size(); i++) {
			
			HSSFRow row11 = sheet.createRow(9 + i);
			HSSFCell cellTituloDestino = row11.createCell(1);
		    cellTituloDestino.setCellValue(" " + objetoList.get(i).getPais());
			cellTituloDestino.setCellStyle(cellDadosPaises);
			
			HSSFCell cellValorPesoAno1 = row11.createCell(3);
		    cellValorPesoAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno1().equals(0L)) {
		    	cellValorPesoAno1.setCellValue("-");
		    	
			} else {
				cellValorPesoAno1.setCellValue(objetoList.get(i).getPesoAno1());
			}
		    
		    HSSFCell cellValorPesoAno2 = row11.createCell(4);
		    cellValorPesoAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno2().equals(0L)) {
		    	cellValorPesoAno2.setCellValue("-");
				
			} else {
				cellValorPesoAno2.setCellValue(objetoList.get(i).getPesoAno2());
			}
		    
		    HSSFCell cellValorPesoAno3 = row11.createCell(5);
		    cellValorPesoAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno3().equals(0L)) {
		    	cellValorPesoAno3.setCellValue("-");
				
			} else {
				cellValorPesoAno3.setCellValue(objetoList.get(i).getPesoAno3());
			}
		    
		    HSSFCell cellValorVariacaoPesoAno1 = row11.createCell(7);
		    if(objetoList.get(i).getPesoVariacaoAno1() == -66893823023L) {
		    	cellValorVariacaoPesoAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoPesoAno1.setCellValue(objetoList.get(i).getPesoVariacaoAno1() + "%");
		    }
		    cellValorVariacaoPesoAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoPesoAno2 = row11.createCell(8);
		    if(objetoList.get(i).getPesoVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoPesoAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoPesoAno2.setCellValue(objetoList.get(i).getPesoVariacaoAno2() + "%");
		    }
		    cellValorVariacaoPesoAno2.setCellStyle(cellDadosGerais);
		    
		    
		    HSSFCell cellValorSacasAno1 = row11.createCell(10);
		    cellValorSacasAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getSaca60kgAno1().equals(0L)) {
		    	cellValorSacasAno1.setCellValue("-");
				
			} else {
				cellValorSacasAno1.setCellValue(objetoList.get(i).getSaca60kgAno1());
			}
		    
		    HSSFCell cellValorSacasAno2 = row11.createCell(11);
		    cellValorSacasAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getSaca60kgAno2().equals(0L)) {
		    	cellValorSacasAno2.setCellValue("-");
		    	
			} else {
				cellValorSacasAno2.setCellValue(objetoList.get(i).getSaca60kgAno2());
			}
		    
		    HSSFCell cellValorSacasAno3 = row11.createCell(12);
		    cellValorSacasAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getSaca60kgAno3().equals(0L)) {
		    	cellValorSacasAno3.setCellValue("-");
				
			} else {
				cellValorSacasAno3.setCellValue(objetoList.get(i).getSaca60kgAno3());
			}
		    
		    HSSFCell cellValorVariacaoSacasAno1 = row11.createCell(14);
		    if(objetoList.get(i).getSaca60kgVariacaoAno1() == -66893823023L) {
		    	cellValorVariacaoSacasAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoSacasAno1.setCellValue(objetoList.get(i).getSaca60kgVariacaoAno1() + "%");
		    }
		    cellValorVariacaoSacasAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoSacasAno2 = row11.createCell(15);
		    if(objetoList.get(i).getSaca60kgVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoSacasAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoSacasAno2.setCellValue(objetoList.get(i).getSaca60kgVariacaoAno2() + "%");
		    }
		    cellValorVariacaoSacasAno2.setCellStyle(cellDadosGerais);
		    
		    
		    HSSFCell cellValorReceitaAno1 = row11.createCell(17);
		    cellValorReceitaAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno1().equals(0L)) {
		    	cellValorReceitaAno1.setCellValue("-");
		    	
			} else {
				cellValorReceitaAno1.setCellValue(objetoList.get(i).getReceitaAno1());
			}
		    
		    HSSFCell cellValorReceitaAno2 = row11.createCell(18);
		    cellValorReceitaAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno2().equals(0L)) {
		    	cellValorReceitaAno2.setCellValue("-");
				
			} else {
				cellValorReceitaAno2.setCellValue(objetoList.get(i).getReceitaAno2());
			}
		    
		    HSSFCell cellValorReceitaAno3 = row11.createCell(19);
		    cellValorReceitaAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno3().equals(0L)) {
		    	cellValorReceitaAno3.setCellValue("-");
				
			} else {
				cellValorReceitaAno3.setCellValue(objetoList.get(i).getReceitaAno3());
			}
		    
		    HSSFCell cellValorVariacaoReceitaAno1 = row11.createCell(21);
		    if(objetoList.get(i).getReceitaVariacaoAno1() == -66893823023L) {
		    	cellValorVariacaoReceitaAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoReceitaAno1.setCellValue(objetoList.get(i).getReceitaVariacaoAno1() + "%");
		    }
		    cellValorVariacaoReceitaAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoReceitaAno2 = row11.createCell(22);
		    if(objetoList.get(i).getReceitaVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoReceitaAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoReceitaAno2.setCellValue(objetoList.get(i).getReceitaVariacaoAno2() + "%");
		    }
		    cellValorVariacaoReceitaAno2.setCellStyle(cellDadosGerais);
		    
//			sheet.addMergedRegion(new CellRangeAddress(11 + i, 11 + i, 1, 1));
		}
		
		HSSFRow rowLast = sheet.createRow(objetoList.size() + 9);
		HSSFCell cellSomatorioCabecalho = rowLast.createCell(1);
		cellSomatorioCabecalho.setCellValue("Total Acumulado");
		cellSomatorioCabecalho.setCellStyle(cellDadosSomatorio);
		
		sheet.addMergedRegion(new CellRangeAddress(objetoList.size() + 9, objetoList.size() + 10, 1, 1));
		
		int indexAnos = 0;
		int indexSomatorio = 3;
		
		for(int i=0; i< listaSomaAno.size(); i++) {
			HSSFCell cellSomatorioAno = rowLast.createCell(indexSomatorio);
			cellSomatorioAno.setCellValue(listaSomaAno.get(i));
			cellSomatorioAno.setCellStyle(cellDadosSomatorio);
			
			sheet.addMergedRegion(new CellRangeAddress(objetoList.size() + 9, objetoList.size() + 10, indexSomatorio, indexSomatorio));
			
			if(indexAnos == 2) {
				indexSomatorio += 5;
				indexAnos = 0;
				
			} else {
				indexSomatorio++;
				indexAnos++;
			}
		}
		
		int index = 7;
		for(int i=0; i< listaSomaVariacao.size(); i++) {
			HSSFCell cellSomatorioVariacao = rowLast.createCell(index);
			cellSomatorioVariacao.setCellValue(listaSomaVariacao.get(i) + "%");
			cellSomatorioVariacao.setCellStyle(cellDadosSomatorio);
			
			sheet.addMergedRegion(new CellRangeAddress(objetoList.size() + 9, objetoList.size() + 10, index, index));
			
			if(i%2 == 0) {
				index++;
			} else {
				index += 6;
			}
		}
		
//		for(int i=2; i<24; i++) {
//			sheet.autoSizeColumn((short) i, true);
//		}
		
		sheet.setColumnWidth(0, 500);
		
		if(tipoRelatorio.equals("mensal")) {
			sheet.setColumnWidth(1, 5000);
			
		} else if(tipoRelatorio.equals("produto")) {
			sheet.setColumnWidth(1, 16000);
			
		} else {
			sheet.setColumnWidth(1, 12500);
		}
		
		sheet.setColumnWidth(2, 500);
		sheet.setColumnWidth(3, 4300);
		sheet.setColumnWidth(4, 4300);
		sheet.setColumnWidth(5, 4300);
		sheet.setColumnWidth(6, 500);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 500);
		sheet.setColumnWidth(10, 4300);
		sheet.setColumnWidth(11, 4300);
		sheet.setColumnWidth(12, 4300);
		sheet.setColumnWidth(13, 500);
		sheet.setColumnWidth(14, 4000);
		sheet.setColumnWidth(15, 4000);
		sheet.setColumnWidth(16, 500);
		sheet.setColumnWidth(17, 4300);
		sheet.setColumnWidth(18, 4300);
		sheet.setColumnWidth(19, 4300);
		sheet.setColumnWidth(20, 500);
		sheet.setColumnWidth(21, 4000);
		sheet.setColumnWidth(22, 4000);
		
		return sheet;
	}
	
	public static HSSFSheet preencherDadosPaisAnual(HSSFSheet sheet, List<RelatorioExcelAnual> objetoList, List<BigDecimal> listaSomaAno, 
			CellStyle cellDadosPaises, CellStyle cellDadosGerais, CellStyle cellDadosSomatorio, String tipoRelatorio) {
		
		for(int i = 0; i < objetoList.size(); i++) {
			
			HSSFRow row11 = sheet.createRow(9 + i);
			HSSFCell cellTituloDestino = row11.createCell(1);
		    cellTituloDestino.setCellValue(objetoList.get(i).getPais());
			cellTituloDestino.setCellStyle(cellDadosPaises);
			
			HSSFCell cellValorPesoAno1 = row11.createCell(3);
		    cellValorPesoAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno1().equals(new BigDecimal(0))) {
		    	cellValorPesoAno1.setCellValue("-");
				
			} else {
				cellValorPesoAno1.setCellValue(objetoList.get(i).getPesoAno1().doubleValue());
			}
		    
		    HSSFCell cellValorPesoAno2 = row11.createCell(4);
		    cellValorPesoAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno2().equals(new BigDecimal(0))) {
		    	cellValorPesoAno2.setCellValue("-");
				
			} else {
				cellValorPesoAno2.setCellValue(objetoList.get(i).getPesoAno2().doubleValue());
			}
		    
		    HSSFCell cellValorPesoAno3 = row11.createCell(5);
		    cellValorPesoAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno3().equals(new BigDecimal(0))) {
		    	cellValorPesoAno3.setCellValue("-");
			
		    } else {
		    	cellValorPesoAno3.setCellValue(objetoList.get(i).getPesoAno3().doubleValue());
		    }
		    
		    HSSFCell cellValorPesoAno4 = row11.createCell(6);
		    cellValorPesoAno4.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno4().equals(new BigDecimal(0))) {
		    	cellValorPesoAno4.setCellValue("-");
			
		    } else {
		    	cellValorPesoAno4.setCellValue(objetoList.get(i).getPesoAno4().doubleValue());
		    }
		    
			HSSFCell cellValorPesoAno5 = row11.createCell(7);
			cellValorPesoAno5.setCellStyle(cellDadosGerais);
			
			if(objetoList.get(i).getPesoAno5().equals(new BigDecimal(0))) {
				cellValorPesoAno5.setCellValue("-");
			
			} else {
				cellValorPesoAno5.setCellValue(objetoList.get(i).getPesoAno5().doubleValue());
			}
			
		    HSSFCell cellValorPesoAno6 = row11.createCell(8);
		    cellValorPesoAno6.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno6().equals(new BigDecimal(0))) {
		    	cellValorPesoAno6.setCellValue("-");
			
		    } else {
		    	cellValorPesoAno6.setCellValue(objetoList.get(i).getPesoAno6().doubleValue());
		    }
		    
		    HSSFCell cellValorPesoAno7 = row11.createCell(9);
		    cellValorPesoAno7.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno7().equals(new BigDecimal(0))) {
		    	cellValorPesoAno7.setCellValue("-");
			
		    } else {
		    	cellValorPesoAno7.setCellValue(objetoList.get(i).getPesoAno7().doubleValue());
		    }
		    
		    HSSFCell cellValorPesoAno8 = row11.createCell(10);
		    cellValorPesoAno8.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno8().equals(new BigDecimal(0))) {
		    	cellValorPesoAno8.setCellValue("-");
			
		    } else {
		    	cellValorPesoAno8.setCellValue(objetoList.get(i).getPesoAno8().doubleValue());
		    }
		    
		    HSSFCell cellValorPesoAno9 = row11.createCell(11);
		    cellValorPesoAno9.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno9().equals(new BigDecimal(0))) {
		    	cellValorPesoAno9.setCellValue("-");
			
		    } else {
		    	cellValorPesoAno9.setCellValue(objetoList.get(i).getPesoAno9().doubleValue());
		    }
		    
		    HSSFCell cellValorReceitaAno1 = row11.createCell(13);
		    cellValorReceitaAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno1().equals(new BigDecimal(0))) {
		    	cellValorReceitaAno1.setCellValue("-");
			
		    } else {
		    	cellValorReceitaAno1.setCellValue(objetoList.get(i).getReceitaAno1().doubleValue());
		    }
		    
		    HSSFCell cellValorReceitaAno2 = row11.createCell(14);
		    cellValorReceitaAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno2().equals(new BigDecimal(0))) {
		    	cellValorReceitaAno2.setCellValue("-");
			
		    } else {
		    	cellValorReceitaAno2.setCellValue(objetoList.get(i).getReceitaAno2().doubleValue());
		    }
		    
		    HSSFCell cellValorReceitaAno3 = row11.createCell(15);
		    cellValorReceitaAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno3().equals(new BigDecimal(0))) {
		    	cellValorReceitaAno3.setCellValue("-");
			
		    } else {
		    	cellValorReceitaAno3.setCellValue(objetoList.get(i).getReceitaAno3().doubleValue());
		    }
		    
		    HSSFCell cellValorReceitaAno4 = row11.createCell(16);
		    cellValorReceitaAno4.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno4().equals(new BigDecimal(0))) {
		    	cellValorReceitaAno4.setCellValue("-");
			
		    } else {
		    	cellValorReceitaAno4.setCellValue(objetoList.get(i).getReceitaAno4().doubleValue());
		    }
		    
			HSSFCell cellValorReceitaAno5 = row11.createCell(17);
			cellValorReceitaAno5.setCellStyle(cellDadosGerais);
			
			if(objetoList.get(i).getReceitaAno5().equals(new BigDecimal(0))) {
				cellValorReceitaAno5.setCellValue("-");
			
			} else {
				cellValorReceitaAno5.setCellValue(objetoList.get(i).getReceitaAno5().doubleValue());
			}
			
		    HSSFCell cellValorReceitaAno6 = row11.createCell(18);
		    cellValorReceitaAno6.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno6().equals(new BigDecimal(0))) {
		    	cellValorReceitaAno6.setCellValue("-");
			
		    } else {
		    	cellValorReceitaAno6.setCellValue(objetoList.get(i).getReceitaAno6().doubleValue());
		    }
		    
		    HSSFCell cellValorReceitaAno7 = row11.createCell(19);
		    cellValorReceitaAno7.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno7().equals(new BigDecimal(0))) {
		    	cellValorReceitaAno7.setCellValue("-");
			
		    } else {
		    	cellValorReceitaAno7.setCellValue(objetoList.get(i).getReceitaAno7().doubleValue());
		    }
		    
		    HSSFCell cellValorReceitaAno8 = row11.createCell(20);
			cellValorReceitaAno8.setCellStyle(cellDadosGerais);
			
			if(objetoList.get(i).getReceitaAno8().equals(new BigDecimal(0))) {
				cellValorReceitaAno8.setCellValue("-");
			
			} else {
				cellValorReceitaAno8.setCellValue(objetoList.get(i).getReceitaAno8().doubleValue());
			}
			
		    HSSFCell cellValorReceitaAno9 = row11.createCell(21);
		    cellValorReceitaAno9.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno9().equals(new BigDecimal(0))) {
		    	cellValorReceitaAno9.setCellValue("-");
			
		    } else {
		    	cellValorReceitaAno9.setCellValue(objetoList.get(i).getReceitaAno9().doubleValue());
		    }
		    
//			sheet.addMergedRegion(new CellRangeAddress(11 + i, 11 + i, 1, 1));
		}
		
		HSSFRow rowLast = sheet.createRow(objetoList.size() + 9);
		HSSFCell cellSomatorioCabecalho = rowLast.createCell(1);
		cellSomatorioCabecalho.setCellValue("Total Acumulado");
		cellSomatorioCabecalho.setCellStyle(cellDadosSomatorio);
		
		sheet.addMergedRegion(new CellRangeAddress(objetoList.size() + 9, objetoList.size() + 10, 1, 1));
		
		int indexSomatorio = 3;
		for(int i=0; i< listaSomaAno.size(); i++) {
			HSSFCell cellSomatorioAno = rowLast.createCell(indexSomatorio);
			cellSomatorioAno.setCellValue(listaSomaAno.get(i).doubleValue());
			cellSomatorioAno.setCellStyle(cellDadosSomatorio);
			
			sheet.addMergedRegion(new CellRangeAddress(objetoList.size() + 9, objetoList.size() + 10, indexSomatorio, indexSomatorio));
			
			if((i+1)%9 == 0) {
				indexSomatorio++;;
			}
			
			indexSomatorio++;
		}
		
//		for(int i=2; i<23; i++) {
//			sheet.autoSizeColumn((short) i, true);
//		}
		
		sheet.setColumnWidth(0, 500);
		if(tipoRelatorio.equals("mensal")) {
			sheet.setColumnWidth(1, 5000);
			
		} else if(tipoRelatorio.equals("produto")) {
			sheet.setColumnWidth(1, 16000);
			
		} else {
			sheet.setColumnWidth(1, 12500);
		}
		
		sheet.setColumnWidth(2, 500);
		sheet.setColumnWidth(3, 4300);
		sheet.setColumnWidth(4, 4300);
		sheet.setColumnWidth(5, 4300);
		sheet.setColumnWidth(6, 4300);
		sheet.setColumnWidth(7, 4300);
		sheet.setColumnWidth(8, 4300);
		sheet.setColumnWidth(9, 4300);
		sheet.setColumnWidth(10, 4300);
		sheet.setColumnWidth(11, 4300);
		sheet.setColumnWidth(12, 500);
		sheet.setColumnWidth(13, 4300);
		sheet.setColumnWidth(14, 4300);
		sheet.setColumnWidth(15, 4300);
		sheet.setColumnWidth(16, 4300);
		sheet.setColumnWidth(17, 4300);
		sheet.setColumnWidth(18, 4300);
		sheet.setColumnWidth(19, 4300);
		sheet.setColumnWidth(20, 4300);
		sheet.setColumnWidth(21, 4300);
		
		return sheet;
	}
	
	public static HSSFSheet preencherDadosMensal(HSSFSheet sheet, List<RelatorioExcelMensalAcumulado> objetoList
			, List<Long> listaSomaAno, List<Long> listaSomaVariacao, CellStyle cellDadosPaises, CellStyle cellDadosGerais, CellStyle cellDadosSomatorio) {
		
		for(int i = 0; i < objetoList.size(); i++) {
			
			HSSFRow row11 = sheet.createRow(11 + i);
			HSSFCell cellTituloDestino = row11.createCell(1);
		    cellTituloDestino.setCellValue(objetoList.get(i).getPais());
			cellTituloDestino.setCellStyle(cellDadosPaises);
			
			HSSFRow row29 = sheet.createRow(29 + i);
			HSSFCell cellTituloDestino2 = row29.createCell(1);
			cellTituloDestino2.setCellValue(objetoList.get(i).getPais());
			cellTituloDestino2.setCellStyle(cellDadosPaises);
			
			HSSFRow row47 = sheet.createRow(47 + i);
			HSSFCell cellTituloDestino3 = row47.createCell(1);
			cellTituloDestino3.setCellValue(objetoList.get(i).getPais());
			cellTituloDestino3.setCellStyle(cellDadosPaises);
			
			HSSFCell cellValorPesoAno1 = row11.createCell(3);
		    cellValorPesoAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno1().equals(0L)) {
		    	cellValorPesoAno1.setCellValue("-");
		    	
			} else {
				cellValorPesoAno1.setCellValue(objetoList.get(i).getPesoAno1());
			}
		    
		    HSSFCell cellValorSacaAno1 = row29.createCell(3);
		    cellValorSacaAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getSaca60kgAno1().equals(0L)) {
		    	cellValorSacaAno1.setCellValue("-");
		    	
			} else {
				cellValorSacaAno1.setCellValue(objetoList.get(i).getSaca60kgAno1());
			}
		    
		    HSSFCell cellValorReceitaAno1 = row47.createCell(3);
		    cellValorReceitaAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno1().equals(0L)) {
		    	cellValorReceitaAno1.setCellValue("-");
		    	
			} else {
				cellValorReceitaAno1.setCellValue(objetoList.get(i).getReceitaAno1());
			}
		    
		    
		    HSSFCell cellValorPesoAno2 = row11.createCell(4);
		    cellValorPesoAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno2().equals(0L)) {
		    	cellValorPesoAno2.setCellValue("-");
				
			} else {
				cellValorPesoAno2.setCellValue(objetoList.get(i).getPesoAno2());
			}
		    
		    HSSFCell cellValorSacaAno2 = row29.createCell(4);
		    cellValorSacaAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getSaca60kgAno2().equals(0L)) {
		    	cellValorSacaAno2.setCellValue("-");
				
			} else {
				cellValorSacaAno2.setCellValue(objetoList.get(i).getSaca60kgAno2());
			}
		    
		    HSSFCell cellValorReceitaAno2 = row47.createCell(4);
		    cellValorReceitaAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno2().equals(0L)) {
		    	cellValorReceitaAno2.setCellValue("-");
				
			} else {
				cellValorReceitaAno2.setCellValue(objetoList.get(i).getReceitaAno2());
			}
		    
		    
		    HSSFCell cellValorPesoAno3 = row11.createCell(5);
		    cellValorPesoAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno3().equals(0L)) {
		    	cellValorPesoAno3.setCellValue("-");
				
			} else {
				cellValorPesoAno3.setCellValue(objetoList.get(i).getPesoAno3());
			}
		    
		    HSSFCell cellValorSacaAno3 = row29.createCell(5);
		    cellValorSacaAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getSaca60kgAno3().equals(0L)) {
		    	cellValorSacaAno3.setCellValue("-");
				
			} else {
				cellValorSacaAno3.setCellValue(objetoList.get(i).getSaca60kgAno3());
			}
		    
		    HSSFCell cellValorReceitaAno3 = row47.createCell(5);
		    cellValorReceitaAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno3().equals(0L)) {
		    	cellValorReceitaAno3.setCellValue("-");
				
			} else {
				cellValorReceitaAno3.setCellValue(objetoList.get(i).getReceitaAno3());
			}
		    
		    
		    HSSFCell cellValorVariacaoPesoAno1 = row11.createCell(7);
		    if(objetoList.get(i).getPesoVariacaoAno1() == -66893823023L) {
			    cellValorVariacaoPesoAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoPesoAno1.setCellValue(objetoList.get(i).getPesoVariacaoAno1() + "%");
		    }
		    cellValorVariacaoPesoAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoSacaAno1 = row29.createCell(7);
		    if(objetoList.get(i).getSaca60kgVariacaoAno1() == -66893823023L) {
			    cellValorVariacaoSacaAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoSacaAno1.setCellValue(objetoList.get(i).getSaca60kgVariacaoAno1() + "%");
		    }
		    cellValorVariacaoSacaAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoReceitaAno1 = row47.createCell(7);
		    if(objetoList.get(i).getReceitaVariacaoAno1() == -66893823023L) {
			    cellValorVariacaoReceitaAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoReceitaAno1.setCellValue(objetoList.get(i).getReceitaVariacaoAno1() + "%");
		    }
		    cellValorVariacaoReceitaAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoPesoAno2 = row11.createCell(8);
		    if(objetoList.get(i).getPesoVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoPesoAno2.setCellValue("");
		    	
		    } else {
		    	cellValorVariacaoPesoAno2.setCellValue(objetoList.get(i).getPesoVariacaoAno2() + "%");
		    }
		    cellValorVariacaoPesoAno2.setCellStyle(cellDadosGerais);

		    HSSFCell cellValorVariacaoSacaAno2 = row29.createCell(8);
		    if(objetoList.get(i).getSaca60kgVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoSacaAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoSacaAno2.setCellValue(objetoList.get(i).getSaca60kgVariacaoAno2() + "%");
		    }
		    cellValorVariacaoSacaAno2.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoReceitaAno2 = row47.createCell(8);
		    if(objetoList.get(i).getReceitaVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoReceitaAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoReceitaAno2.setCellValue(objetoList.get(i).getReceitaVariacaoAno2() + "%");
		    }
		    cellValorVariacaoReceitaAno2.setCellStyle(cellDadosGerais);
		    
		    
//			sheet.addMergedRegion(new CellRangeAddress(11 + i, 11 + i, 1, 1));
//			sheet.addMergedRegion(new CellRangeAddress(29 + i, 29 + i, 1, 1));
//			sheet.addMergedRegion(new CellRangeAddress(47 + i, 47 + i, 1, 1));
		}
		
		HSSFRow rowLast1 = sheet.createRow(23);
		HSSFCell cellSomatorioCabecalho1 = rowLast1.createCell(1);
		cellSomatorioCabecalho1.setCellValue("Total Acumulado");
		cellSomatorioCabecalho1.setCellStyle(cellDadosSomatorio);
		
		HSSFRow rowLast2 = sheet.createRow(41);
		HSSFCell cellSomatorioCabecalho2 = rowLast2.createCell(1);
		cellSomatorioCabecalho2.setCellValue("Total Acumulado");
		cellSomatorioCabecalho2.setCellStyle(cellDadosSomatorio);
		
		HSSFRow rowLast3 = sheet.createRow(59);
		HSSFCell cellSomatorioCabecalho3 = rowLast3.createCell(1);
		cellSomatorioCabecalho3.setCellValue("Total Acumulado");
		cellSomatorioCabecalho3.setCellStyle(cellDadosSomatorio);
		
		sheet.addMergedRegion(new CellRangeAddress(23, 24, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(41, 42, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(59, 60, 1, 1));
		
		HSSFCell cellSomatorioAno1 = rowLast1.createCell(3);
		cellSomatorioAno1.setCellValue(listaSomaAno.get(0));
		cellSomatorioAno1.setCellStyle(cellDadosSomatorio);
		sheet.addMergedRegion(new CellRangeAddress(23, 24, 3, 3));
		
		HSSFCell cellSomatorioAno2 = rowLast1.createCell(4);
		cellSomatorioAno2.setCellValue(listaSomaAno.get(1));
		cellSomatorioAno2.setCellStyle(cellDadosSomatorio);
		sheet.addMergedRegion(new CellRangeAddress(23, 24, 4, 4));
		
		HSSFCell cellSomatorioAno3 = rowLast1.createCell(5);
		cellSomatorioAno3.setCellValue(listaSomaAno.get(2));
		cellSomatorioAno3.setCellStyle(cellDadosSomatorio);
		sheet.addMergedRegion(new CellRangeAddress(23, 24, 5, 5));
		
		HSSFCell cellSomatorioAno4 = rowLast2.createCell(3);
		cellSomatorioAno4.setCellValue(listaSomaAno.get(3));
		cellSomatorioAno4.setCellStyle(cellDadosSomatorio);
		sheet.addMergedRegion(new CellRangeAddress(41, 42, 3, 3));
		
		HSSFCell cellSomatorioAno5 = rowLast2.createCell(4);
		cellSomatorioAno5.setCellValue(listaSomaAno.get(4));
		cellSomatorioAno5.setCellStyle(cellDadosSomatorio);
		sheet.addMergedRegion(new CellRangeAddress(41, 42, 4, 4));
		
		HSSFCell cellSomatorioAno6 = rowLast2.createCell(5);
		cellSomatorioAno6.setCellValue(listaSomaAno.get(5));
		cellSomatorioAno6.setCellStyle(cellDadosSomatorio);
		sheet.addMergedRegion(new CellRangeAddress(41, 42, 5, 5));
		
		HSSFCell cellSomatorioAno7 = rowLast3.createCell(3);
		cellSomatorioAno7.setCellValue(listaSomaAno.get(6));
		cellSomatorioAno7.setCellStyle(cellDadosSomatorio);
		sheet.addMergedRegion(new CellRangeAddress(59, 60, 3, 3));
		
		HSSFCell cellSomatorioAno8 = rowLast3.createCell(4);
		cellSomatorioAno8.setCellValue(listaSomaAno.get(7));
		cellSomatorioAno8.setCellStyle(cellDadosSomatorio);
		sheet.addMergedRegion(new CellRangeAddress(59, 60, 4, 4));
		
		HSSFCell cellSomatorioAno9 = rowLast3.createCell(5);
		cellSomatorioAno9.setCellValue(listaSomaAno.get(8));
		cellSomatorioAno9.setCellStyle(cellDadosSomatorio);
		sheet.addMergedRegion(new CellRangeAddress(59, 60, 5, 5));
		
		
		HSSFCell cellSomatorioVariacao1 = rowLast1.createCell(7);
		cellSomatorioVariacao1.setCellValue(listaSomaVariacao.get(0) + "%");
		cellSomatorioVariacao1.setCellStyle(cellDadosSomatorio);
		sheet.addMergedRegion(new CellRangeAddress(23, 24, 7, 7));
		
		HSSFCell cellSomatorioVariacao2 = rowLast1.createCell(8);
		cellSomatorioVariacao2.setCellValue(listaSomaVariacao.get(1) + "%");
		cellSomatorioVariacao2.setCellStyle(cellDadosSomatorio);
		sheet.addMergedRegion(new CellRangeAddress(23, 24, 8, 8));
		
		HSSFCell cellSomatorioVariacao3 = rowLast2.createCell(7);
		cellSomatorioVariacao3.setCellValue(listaSomaVariacao.get(2) + "%");
		cellSomatorioVariacao3.setCellStyle(cellDadosSomatorio);
		sheet.addMergedRegion(new CellRangeAddress(41, 42, 7, 7));
		
		HSSFCell cellSomatorioVariacao4 = rowLast2.createCell(8);
		cellSomatorioVariacao4.setCellValue(listaSomaVariacao.get(3) + "%");
		cellSomatorioVariacao4.setCellStyle(cellDadosSomatorio);
		sheet.addMergedRegion(new CellRangeAddress(41, 42, 8, 8));
		
		HSSFCell cellSomatorioVariacao5 = rowLast3.createCell(7);
		cellSomatorioVariacao5.setCellValue(listaSomaVariacao.get(4) + "%");
		cellSomatorioVariacao5.setCellStyle(cellDadosSomatorio);
		sheet.addMergedRegion(new CellRangeAddress(59, 60, 7, 7));
		
		HSSFCell cellSomatorioVariacao6 = rowLast3.createCell(8);
		cellSomatorioVariacao6.setCellValue(listaSomaVariacao.get(5) + "%");
		cellSomatorioVariacao6.setCellStyle(cellDadosSomatorio);
		sheet.addMergedRegion(new CellRangeAddress(59, 60, 8, 8));
		
//		for(int i=2; i<24; i++) {
//			sheet.autoSizeColumn((short) i, true);
//		}
		
		sheet.setColumnWidth(0, 500);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 500);
		sheet.setColumnWidth(3, 4500);
		sheet.setColumnWidth(4, 4500);
		sheet.setColumnWidth(5, 4500);
		sheet.setColumnWidth(6, 500);
		sheet.setColumnWidth(7, 5000);
		sheet.setColumnWidth(8, 5000);
		
		return sheet;
	}
	
	public static HSSFSheet preencherDadosEmpresaMensalAcumulado(HSSFSheet sheet, List<RelatorioExcelMensalAcumulado> objetoList
			, List<Long> listaSomaAno, List<Long> listaSomaVariacao, List<RelatorioExcelMensalAcumulado> objetoList2
			, List<Long> listaSomaAno2, List<Long> listaSomaVariacao2, CellStyle cellDadosPaises, CellStyle cellDadosGerais, CellStyle cellDadosSomatorio) {
		
		//Mensal
		for(int i = 0; i < objetoList.size(); i++) {
			
			HSSFRow row11 = sheet.createRow(9 + i);
			HSSFCell cellTituloDestino = row11.createCell(1);
		    cellTituloDestino.setCellValue(" " + objetoList.get(i).getPais());
			cellTituloDestino.setCellStyle(cellDadosPaises);
			
			HSSFCell cellValorPesoAno1 = row11.createCell(3);
		    cellValorPesoAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno1().equals(0L)) {
		    	cellValorPesoAno1.setCellValue("-");
		    	
			} else {
				cellValorPesoAno1.setCellValue(objetoList.get(i).getPesoAno1());
			}
		    
		    HSSFCell cellValorPesoAno2 = row11.createCell(4);
		    cellValorPesoAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno2().equals(0L)) {
		    	cellValorPesoAno2.setCellValue("-");
				
			} else {
				cellValorPesoAno2.setCellValue(objetoList.get(i).getPesoAno2());
			}
		    
		    HSSFCell cellValorPesoAno3 = row11.createCell(5);
		    cellValorPesoAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno3().equals(0L)) {
		    	cellValorPesoAno3.setCellValue("-");
				
			} else {
				cellValorPesoAno3.setCellValue(objetoList.get(i).getPesoAno3());
			}

		    HSSFCell cellValorVariacaoPesoAno1 = row11.createCell(7);
		    if(objetoList.get(i).getPesoVariacaoAno1() == -66893823023L) {
			    cellValorVariacaoPesoAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoPesoAno1.setCellValue(objetoList.get(i).getPesoVariacaoAno1() + "%");
		    }
		    cellValorVariacaoPesoAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoPesoAno2 = row11.createCell(8);
		    cellValorVariacaoPesoAno2.setCellValue(objetoList.get(i).getPesoVariacaoAno2() + "%");
		    cellValorVariacaoPesoAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoPesoAno2.setCellValue("");
		    	
		    } else {
		    	cellValorVariacaoPesoAno2.setCellValue(objetoList.get(i).getPesoVariacaoAno2() + "%");
		    }
		    
		    HSSFCell cellValorSacasAno1 = row11.createCell(10);
		    cellValorSacasAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getSaca60kgAno1().equals(0L)) {
		    	cellValorSacasAno1.setCellValue("-");
				
			} else {
				cellValorSacasAno1.setCellValue(objetoList.get(i).getSaca60kgAno1());
			}
		    
		    HSSFCell cellValorSacasAno2 = row11.createCell(11);
		    cellValorSacasAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getSaca60kgAno2().equals(0L)) {
		    	cellValorSacasAno2.setCellValue("-");
		    	
			} else {
				cellValorSacasAno2.setCellValue(objetoList.get(i).getSaca60kgAno2());
			}
		    
		    HSSFCell cellValorSacasAno3 = row11.createCell(12);
		    cellValorSacasAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getSaca60kgAno3().equals(0L)) {
		    	cellValorSacasAno3.setCellValue("-");
				
			} else {
				cellValorSacasAno3.setCellValue(objetoList.get(i).getSaca60kgAno3());
			}
		    
		    HSSFCell cellValorVariacaoSacasAno1 = row11.createCell(14);
		    if(objetoList.get(i).getSaca60kgVariacaoAno1() == -66893823023L) {
		    	cellValorVariacaoSacasAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoSacasAno1.setCellValue(objetoList.get(i).getSaca60kgVariacaoAno1() + "%");
		    }
		    cellValorVariacaoSacasAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoSacasAno2 = row11.createCell(15);
		    if(objetoList.get(i).getSaca60kgVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoSacasAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoSacasAno2.setCellValue(objetoList.get(i).getSaca60kgVariacaoAno2() + "%");
		    }
		    
		    cellValorVariacaoSacasAno2.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorReceitaAno1 = row11.createCell(17);
		    cellValorReceitaAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno1().equals(0L)) {
		    	cellValorReceitaAno1.setCellValue("-");
		    	
			} else {
				cellValorReceitaAno1.setCellValue(objetoList.get(i).getReceitaAno1());
			}
		    
		    HSSFCell cellValorReceitaAno2 = row11.createCell(18);
		    cellValorReceitaAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno2().equals(0L)) {
		    	cellValorReceitaAno2.setCellValue("-");
				
			} else {
				cellValorReceitaAno2.setCellValue(objetoList.get(i).getReceitaAno2());
			}
		    
		    HSSFCell cellValorReceitaAno3 = row11.createCell(19);
		    cellValorReceitaAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno3().equals(0L)) {
		    	cellValorReceitaAno3.setCellValue("-");
				
			} else {
				cellValorReceitaAno3.setCellValue(objetoList.get(i).getReceitaAno3());
			}
		    
		    HSSFCell cellValorVariacaoReceitaAno1 = row11.createCell(21);
		    if(objetoList.get(i).getReceitaVariacaoAno1() == -66893823023L) {
		    	cellValorVariacaoReceitaAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoReceitaAno1.setCellValue(objetoList.get(i).getReceitaVariacaoAno1() + "%");
		    }
		    cellValorVariacaoReceitaAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoReceitaAno2 = row11.createCell(22);
		    if(objetoList.get(i).getReceitaVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoReceitaAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoReceitaAno2.setCellValue(objetoList.get(i).getReceitaVariacaoAno2() + "%");
		    }
		    cellValorVariacaoReceitaAno2.setCellStyle(cellDadosGerais);
		    
//			sheet.addMergedRegion(new CellRangeAddress(11 + i, 11 + i, 1, 1));
		}
		
		HSSFRow rowLast = sheet.createRow(objetoList.size() + 9);
		HSSFCell cellSomatorioCabecalho = rowLast.createCell(1);
		cellSomatorioCabecalho.setCellValue("Total Acumulado");
		cellSomatorioCabecalho.setCellStyle(cellDadosSomatorio);
		
		sheet.addMergedRegion(new CellRangeAddress(objetoList.size() + 9, objetoList.size() + 10, 1, 1));
		
		int indexAnos = 0;
		int indexSomatorio = 3;
		
		for(int i=0; i< listaSomaAno.size(); i++) {
			HSSFCell cellSomatorioAno = rowLast.createCell(indexSomatorio);
			cellSomatorioAno.setCellValue(listaSomaAno.get(i));
			cellSomatorioAno.setCellStyle(cellDadosSomatorio);
			
			sheet.addMergedRegion(new CellRangeAddress(objetoList.size() + 9, objetoList.size() + 10, indexSomatorio, indexSomatorio));
			
			if(indexAnos == 2) {
				indexSomatorio += 5;
				indexAnos = 0;
				
			} else {
				indexSomatorio++;
				indexAnos++;
			}
		}
		
		int index = 7;
		for(int i=0; i< listaSomaVariacao.size(); i++) {
			HSSFCell cellSomatorioVariacao = rowLast.createCell(index);
			cellSomatorioVariacao.setCellValue(listaSomaVariacao.get(i) + "%");
			cellSomatorioVariacao.setCellStyle(cellDadosSomatorio);
			
			sheet.addMergedRegion(new CellRangeAddress(objetoList.size() + 9, objetoList.size() + 10, index, index));
			
			if(i%2 == 0) {
				index++;
			} else {
				index += 6;
			}
		}
		
//		for(int i=2; i<24; i++) {
//			sheet.autoSizeColumn((short) i, true);
//		}
		
		//acumulado
		for(int i = 0; i < objetoList2.size(); i++) {
			
			HSSFRow row11 = sheet.createRow(objetoList.size() + 15 + i);
			HSSFCell cellTituloDestino = row11.createCell(1);
		    cellTituloDestino.setCellValue(" " + objetoList2.get(i).getPais());
			cellTituloDestino.setCellStyle(cellDadosPaises);
			
			HSSFCell cellValorPesoAno1 = row11.createCell(3);
		    cellValorPesoAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getPesoAno1().equals(0L)) {
		    	cellValorPesoAno1.setCellValue("-");
		    	
			} else {
				cellValorPesoAno1.setCellValue(objetoList2.get(i).getPesoAno1());
			}
		    
		    HSSFCell cellValorPesoAno2 = row11.createCell(4);
		    cellValorPesoAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getPesoAno2().equals(0L)) {
		    	cellValorPesoAno2.setCellValue("-");
				
			} else {
				cellValorPesoAno2.setCellValue(objetoList2.get(i).getPesoAno2());
			}
		    
		    HSSFCell cellValorPesoAno3 = row11.createCell(5);
		    cellValorPesoAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getPesoAno3().equals(0L)) {
		    	cellValorPesoAno3.setCellValue("-");
				
			} else {
				cellValorPesoAno3.setCellValue(objetoList2.get(i).getPesoAno3());
			}
		    
		    HSSFCell cellValorVariacaoPesoAno1 = row11.createCell(7);
		    if(objetoList2.get(i).getPesoVariacaoAno1() == -66893823023L) {
			    cellValorVariacaoPesoAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoPesoAno1.setCellValue(objetoList2.get(i).getPesoVariacaoAno1() + "%");
		    }
		    cellValorVariacaoPesoAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoPesoAno2 = row11.createCell(8);
		    if(objetoList2.get(i).getPesoVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoPesoAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoPesoAno2.setCellValue(objetoList2.get(i).getPesoVariacaoAno2() + "%");
		    }
		    cellValorVariacaoPesoAno2.setCellStyle(cellDadosGerais);
		    
		    
		    HSSFCell cellValorSacasAno1 = row11.createCell(10);
		    cellValorSacasAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getSaca60kgAno1().equals(0L)) {
		    	cellValorSacasAno1.setCellValue("-");
				
			} else {
				cellValorSacasAno1.setCellValue(objetoList2.get(i).getSaca60kgAno1());
			}
		    
		    HSSFCell cellValorSacasAno2 = row11.createCell(11);
		    cellValorSacasAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getSaca60kgAno2().equals(0L)) {
		    	cellValorSacasAno2.setCellValue("-");
		    	
			} else {
				cellValorSacasAno2.setCellValue(objetoList2.get(i).getSaca60kgAno2());
			}
		    
		    HSSFCell cellValorSacasAno3 = row11.createCell(12);
		    cellValorSacasAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getSaca60kgAno3().equals(0L)) {
		    	cellValorSacasAno3.setCellValue("-");
				
			} else {
				cellValorSacasAno3.setCellValue(objetoList2.get(i).getSaca60kgAno3());
			}
		    
		    HSSFCell cellValorVariacaoSacasAno1 = row11.createCell(14);
		    if(objetoList2.get(i).getSaca60kgVariacaoAno1() == -66893823023L) {
		    	cellValorVariacaoSacasAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoSacasAno1.setCellValue(objetoList2.get(i).getSaca60kgVariacaoAno1() + "%");
		    }
		    cellValorVariacaoSacasAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoSacasAno2 = row11.createCell(15);
		    if(objetoList2.get(i).getSaca60kgVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoSacasAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoSacasAno2.setCellValue(objetoList2.get(i).getSaca60kgVariacaoAno2() + "%");
		    }
		    cellValorVariacaoSacasAno2.setCellStyle(cellDadosGerais);
		    
		    
		    HSSFCell cellValorReceitaAno1 = row11.createCell(17);
		    cellValorReceitaAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getReceitaAno1().equals(0L)) {
		    	cellValorReceitaAno1.setCellValue("-");
		    	
			} else {
				cellValorReceitaAno1.setCellValue(objetoList2.get(i).getReceitaAno1());
			}
		    
		    HSSFCell cellValorReceitaAno2 = row11.createCell(18);
		    cellValorReceitaAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getReceitaAno2().equals(0L)) {
		    	cellValorReceitaAno2.setCellValue("-");
				
			} else {
				cellValorReceitaAno2.setCellValue(objetoList2.get(i).getReceitaAno2());
			}
		    
		    HSSFCell cellValorReceitaAno3 = row11.createCell(19);
		    cellValorReceitaAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getReceitaAno3().equals(0L)) {
		    	cellValorReceitaAno3.setCellValue("-");
				
			} else {
				cellValorReceitaAno3.setCellValue(objetoList2.get(i).getReceitaAno3());
			}
		    
		    HSSFCell cellValorVariacaoReceitaAno1 = row11.createCell(21);
		    if(objetoList2.get(i).getReceitaVariacaoAno1() == -66893823023L) {
		    	cellValorVariacaoReceitaAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoReceitaAno1.setCellValue(objetoList2.get(i).getReceitaVariacaoAno1() + "%");
		    }
		    cellValorVariacaoReceitaAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoReceitaAno2 = row11.createCell(22);
		    if(objetoList2.get(i).getReceitaVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoReceitaAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoReceitaAno2.setCellValue(objetoList2.get(i).getReceitaVariacaoAno2() + "%");
		    }
		    cellValorVariacaoReceitaAno2.setCellStyle(cellDadosGerais);
		    
//			sheet.addMergedRegion(new CellRangeAddress(objetoList.size() + 20 + i, objetoList.size() + 20 + i, 1, 1));
		}
		
		int linha = objetoList2.size() + objetoList.size() + 15;
		
		HSSFRow rowLast2 = sheet.createRow(linha);
		HSSFCell cellSomatorioCabecalho2 = rowLast2.createCell(1);
		cellSomatorioCabecalho2.setCellValue("Total Acumulado");
		cellSomatorioCabecalho2.setCellStyle(cellDadosSomatorio);
		
		sheet.addMergedRegion(new CellRangeAddress(linha, linha + 1, 1, 1));
		
		indexAnos = 0;
		indexSomatorio = 3;
		
		for(int i=0; i< listaSomaAno2.size(); i++) {
			HSSFCell cellSomatorioAno = rowLast2.createCell(indexSomatorio);
			cellSomatorioAno.setCellValue(listaSomaAno2.get(i));
			cellSomatorioAno.setCellStyle(cellDadosSomatorio);
			
			sheet.addMergedRegion(new CellRangeAddress(linha, linha+1, indexSomatorio, indexSomatorio));
			
			if(indexAnos == 2) {
				indexSomatorio += 5;
				indexAnos = 0;
				
			} else {
				indexSomatorio++;
				indexAnos++;
			}
		}
		
		index = 7;
		for(int i=0; i< listaSomaVariacao2.size(); i++) {
			HSSFCell cellSomatorioVariacao = rowLast2.createCell(index);
			cellSomatorioVariacao.setCellValue(listaSomaVariacao2.get(i) + "%");
			cellSomatorioVariacao.setCellStyle(cellDadosSomatorio);
			
			sheet.addMergedRegion(new CellRangeAddress(linha, linha + 1, index, index));
			
			if(i%2 == 0) {
				index++;
			} else {
				index += 6;
			}
		}
		
//		for(int i=2; i<24; i++) {
//			sheet.autoSizeColumn((short) i, true);
//		}
		
		sheet.setColumnWidth(0, 500);
		sheet.setColumnWidth(1, 16000);
		sheet.setColumnWidth(2, 500);
		sheet.setColumnWidth(3, 4300);
		sheet.setColumnWidth(4, 4300);
		sheet.setColumnWidth(5, 4300);
		sheet.setColumnWidth(6, 500);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 500);
		sheet.setColumnWidth(10, 4300);
		sheet.setColumnWidth(11, 4300);
		sheet.setColumnWidth(12, 4300);
		sheet.setColumnWidth(13, 500);
		sheet.setColumnWidth(14, 4000);
		sheet.setColumnWidth(15, 4000);
		sheet.setColumnWidth(16, 500);
		sheet.setColumnWidth(17, 4300);
		sheet.setColumnWidth(18, 4300);
		sheet.setColumnWidth(19, 4300);
		sheet.setColumnWidth(20, 500);
		sheet.setColumnWidth(21, 4000);
		sheet.setColumnWidth(22, 4000);
		
		return sheet;
	}
	
	public static HSSFSheet preencherDadosUEMesMensalAcumulado(HSSFSheet sheet, List<RelatorioExcelMensalAcumulado> objetoList
			, List<Long> listaSomaAno, List<Long> listaSomaVariacao, List<RelatorioExcelMensalAcumulado> objetoList2
			, List<Long> listaSomaAno2, List<Long> listaSomaVariacao2, List<RelatorioExcelMensalAcumulado> objetoList3
			, List<Long> listaSomaAno3, List<Long> listaSomaVariacao3, CellStyle cellDadosPaises, CellStyle cellDadosGerais, CellStyle cellDadosSomatorio) {
		
		//Mês
		HSSFRow row10 = sheet.createRow(5);
		HSSFCell cellIdentificacaoTipo = row10.createCell(3);
		cellIdentificacaoTipo.setCellValue("Mensal");
		cellIdentificacaoTipo.setCellStyle(cellDadosPaises);
		
		for(int i = 0; i < objetoList3.size(); i++) {
			HSSFRow row11 = sheet.createRow(9 + i);
			HSSFCell cellTituloDestino = row11.createCell(1);
		    cellTituloDestino.setCellValue(" " + objetoList3.get(i).getPais());
			cellTituloDestino.setCellStyle(cellDadosPaises);
			
			HSSFCell cellValorPesoAno1 = row11.createCell(3);
		    cellValorPesoAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList3.get(i).getPesoAno1().equals(0L)) {
		    	cellValorPesoAno1.setCellValue("-");
		    	
			} else {
				cellValorPesoAno1.setCellValue(objetoList3.get(i).getPesoAno1());
			}
		    
		    HSSFCell cellValorPesoAno2 = row11.createCell(4);
		    cellValorPesoAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList3.get(i).getPesoAno2().equals(0L)) {
		    	cellValorPesoAno2.setCellValue("-");
				
			} else {
				cellValorPesoAno2.setCellValue(objetoList3.get(i).getPesoAno2());
			}
		    
		    HSSFCell cellValorPesoAno3 = row11.createCell(5);
		    cellValorPesoAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList3.get(i).getPesoAno3().equals(0L)) {
		    	cellValorPesoAno3.setCellValue("-");
				
			} else {
				cellValorPesoAno3.setCellValue(objetoList3.get(i).getPesoAno3());
			}

		    HSSFCell cellValorVariacaoPesoAno1 = row11.createCell(7);
		    if(objetoList3.get(i).getPesoVariacaoAno1() == -66893823023L) {
			    cellValorVariacaoPesoAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoPesoAno1.setCellValue(objetoList3.get(i).getPesoVariacaoAno1() + "%");
		    }
		    cellValorVariacaoPesoAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoPesoAno2 = row11.createCell(8);
		    cellValorVariacaoPesoAno2.setCellValue(objetoList3.get(i).getPesoVariacaoAno2() + "%");
		    cellValorVariacaoPesoAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList3.get(i).getPesoVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoPesoAno2.setCellValue("");
		    	
		    } else {
		    	cellValorVariacaoPesoAno2.setCellValue(objetoList3.get(i).getPesoVariacaoAno2() + "%");
		    }
		    
		    HSSFCell cellValorSacasAno1 = row11.createCell(10);
		    cellValorSacasAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList3.get(i).getSaca60kgAno1().equals(0L)) {
		    	cellValorSacasAno1.setCellValue("-");
				
			} else {
				cellValorSacasAno1.setCellValue(objetoList3.get(i).getSaca60kgAno1());
			}
		    
		    HSSFCell cellValorSacasAno2 = row11.createCell(11);
		    cellValorSacasAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList3.get(i).getSaca60kgAno2().equals(0L)) {
		    	cellValorSacasAno2.setCellValue("-");
		    	
			} else {
				cellValorSacasAno2.setCellValue(objetoList3.get(i).getSaca60kgAno2());
			}
		    
		    HSSFCell cellValorSacasAno3 = row11.createCell(12);
		    cellValorSacasAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList3.get(i).getSaca60kgAno3().equals(0L)) {
		    	cellValorSacasAno3.setCellValue("-");
				
			} else {
				cellValorSacasAno3.setCellValue(objetoList3.get(i).getSaca60kgAno3());
			}
		    
		    HSSFCell cellValorVariacaoSacasAno1 = row11.createCell(14);
		    if(objetoList3.get(i).getSaca60kgVariacaoAno1() == -66893823023L) {
		    	cellValorVariacaoSacasAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoSacasAno1.setCellValue(objetoList3.get(i).getSaca60kgVariacaoAno1() + "%");
		    }
		    cellValorVariacaoSacasAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoSacasAno2 = row11.createCell(15);
		    if(objetoList3.get(i).getSaca60kgVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoSacasAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoSacasAno2.setCellValue(objetoList3.get(i).getSaca60kgVariacaoAno2() + "%");
		    }
		    
		    cellValorVariacaoSacasAno2.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorReceitaAno1 = row11.createCell(17);
		    cellValorReceitaAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList3.get(i).getReceitaAno1().equals(0L)) {
		    	cellValorReceitaAno1.setCellValue("-");
		    	
			} else {
				cellValorReceitaAno1.setCellValue(objetoList3.get(i).getReceitaAno1());
			}
		    
		    HSSFCell cellValorReceitaAno2 = row11.createCell(18);
		    cellValorReceitaAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList3.get(i).getReceitaAno2().equals(0L)) {
		    	cellValorReceitaAno2.setCellValue("-");
				
			} else {
				cellValorReceitaAno2.setCellValue(objetoList3.get(i).getReceitaAno2());
			}
		    
		    HSSFCell cellValorReceitaAno3 = row11.createCell(19);
		    cellValorReceitaAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList3.get(i).getReceitaAno3().equals(0L)) {
		    	cellValorReceitaAno3.setCellValue("-");
				
			} else {
				cellValorReceitaAno3.setCellValue(objetoList3.get(i).getReceitaAno3());
			}
		    
		    HSSFCell cellValorVariacaoReceitaAno1 = row11.createCell(21);
		    if(objetoList3.get(i).getReceitaVariacaoAno1() == -66893823023L) {
		    	cellValorVariacaoReceitaAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoReceitaAno1.setCellValue(objetoList3.get(i).getReceitaVariacaoAno1() + "%");
		    }
		    cellValorVariacaoReceitaAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoReceitaAno2 = row11.createCell(22);
		    if(objetoList3.get(i).getReceitaVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoReceitaAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoReceitaAno2.setCellValue(objetoList3.get(i).getReceitaVariacaoAno2() + "%");
		    }
		    cellValorVariacaoReceitaAno2.setCellStyle(cellDadosGerais);
		    
//					sheet.addMergedRegion(new CellRangeAddress(11 + i, 11 + i, 1, 1));
		}
		
		HSSFRow rowLast = sheet.createRow(objetoList3.size() + 9);
		HSSFCell cellSomatorioCabecalho = rowLast.createCell(1);
		cellSomatorioCabecalho.setCellValue("Total Acumulado");
		cellSomatorioCabecalho.setCellStyle(cellDadosSomatorio);
		
		sheet.addMergedRegion(new CellRangeAddress(objetoList3.size() + 9, objetoList3.size() + 10, 1, 1));
		
		int indexAnos = 0;
		int indexSomatorio = 3;
		
		for(int i=0; i< listaSomaAno3.size(); i++) {
			HSSFCell cellSomatorioAno = rowLast.createCell(indexSomatorio);
			cellSomatorioAno.setCellValue(listaSomaAno3.get(i));
			cellSomatorioAno.setCellStyle(cellDadosSomatorio);
			
			sheet.addMergedRegion(new CellRangeAddress(objetoList3.size() + 9, objetoList3.size() + 10, indexSomatorio, indexSomatorio));
			
			if(indexAnos == 2) {
				indexSomatorio += 5;
				indexAnos = 0;
				
			} else {
				indexSomatorio++;
				indexAnos++;
			}
		}
		
		int index = 7;
		for(int i=0; i< listaSomaVariacao3.size(); i++) {
			HSSFCell cellSomatorioVariacao = rowLast.createCell(index);
			cellSomatorioVariacao.setCellValue(listaSomaVariacao3.get(i) + "%");
			cellSomatorioVariacao.setCellStyle(cellDadosSomatorio);
			
			sheet.addMergedRegion(new CellRangeAddress(objetoList3.size() + 9, objetoList3.size() + 10, index, index));
			
			if(i%2 == 0) {
				index++;
			} else {
				index += 6;
			}
		}
		
		HSSFRow row10a = sheet.createRow(12 + objetoList3.size());
		HSSFCell cellIdentificacaoTipo2 = row10a.createCell(3);
		cellIdentificacaoTipo2.setCellValue("País mensal");
		cellIdentificacaoTipo2.setCellStyle(cellDadosPaises);
		
		//Mensal
		for(int i = 0; i < objetoList.size(); i++) {
			
			HSSFRow row11 = sheet.createRow(objetoList3.size() + 16 + i);
			HSSFCell cellTituloDestino = row11.createCell(1);
		    cellTituloDestino.setCellValue(" " + objetoList.get(i).getPais());
			cellTituloDestino.setCellStyle(cellDadosPaises);
			
			HSSFCell cellValorPesoAno1 = row11.createCell(3);
		    cellValorPesoAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno1().equals(0L)) {
		    	cellValorPesoAno1.setCellValue("-");
		    	
			} else {
				cellValorPesoAno1.setCellValue(objetoList.get(i).getPesoAno1());
			}
		    
		    HSSFCell cellValorPesoAno2 = row11.createCell(4);
		    cellValorPesoAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno2().equals(0L)) {
		    	cellValorPesoAno2.setCellValue("-");
				
			} else {
				cellValorPesoAno2.setCellValue(objetoList.get(i).getPesoAno2());
			}
		    
		    HSSFCell cellValorPesoAno3 = row11.createCell(5);
		    cellValorPesoAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoAno3().equals(0L)) {
		    	cellValorPesoAno3.setCellValue("-");
				
			} else {
				cellValorPesoAno3.setCellValue(objetoList.get(i).getPesoAno3());
			}

		    HSSFCell cellValorVariacaoPesoAno1 = row11.createCell(7);
		    if(objetoList.get(i).getPesoVariacaoAno1() == -66893823023L) {
			    cellValorVariacaoPesoAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoPesoAno1.setCellValue(objetoList.get(i).getPesoVariacaoAno1() + "%");
		    }
		    cellValorVariacaoPesoAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoPesoAno2 = row11.createCell(8);
		    cellValorVariacaoPesoAno2.setCellValue(objetoList.get(i).getPesoVariacaoAno2() + "%");
		    cellValorVariacaoPesoAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getPesoVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoPesoAno2.setCellValue("");
		    	
		    } else {
		    	cellValorVariacaoPesoAno2.setCellValue(objetoList.get(i).getPesoVariacaoAno2() + "%");
		    }
		    
		    HSSFCell cellValorSacasAno1 = row11.createCell(10);
		    cellValorSacasAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getSaca60kgAno1().equals(0L)) {
		    	cellValorSacasAno1.setCellValue("-");
				
			} else {
				cellValorSacasAno1.setCellValue(objetoList.get(i).getSaca60kgAno1());
			}
		    
		    HSSFCell cellValorSacasAno2 = row11.createCell(11);
		    cellValorSacasAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getSaca60kgAno2().equals(0L)) {
		    	cellValorSacasAno2.setCellValue("-");
		    	
			} else {
				cellValorSacasAno2.setCellValue(objetoList.get(i).getSaca60kgAno2());
			}
		    
		    HSSFCell cellValorSacasAno3 = row11.createCell(12);
		    cellValorSacasAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getSaca60kgAno3().equals(0L)) {
		    	cellValorSacasAno3.setCellValue("-");
				
			} else {
				cellValorSacasAno3.setCellValue(objetoList.get(i).getSaca60kgAno3());
			}
		    
		    HSSFCell cellValorVariacaoSacasAno1 = row11.createCell(14);
		    if(objetoList.get(i).getSaca60kgVariacaoAno1() == -66893823023L) {
		    	cellValorVariacaoSacasAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoSacasAno1.setCellValue(objetoList.get(i).getSaca60kgVariacaoAno1() + "%");
		    }
		    cellValorVariacaoSacasAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoSacasAno2 = row11.createCell(15);
		    if(objetoList.get(i).getSaca60kgVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoSacasAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoSacasAno2.setCellValue(objetoList.get(i).getSaca60kgVariacaoAno2() + "%");
		    }
		    
		    cellValorVariacaoSacasAno2.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorReceitaAno1 = row11.createCell(17);
		    cellValorReceitaAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno1().equals(0L)) {
		    	cellValorReceitaAno1.setCellValue("-");
		    	
			} else {
				cellValorReceitaAno1.setCellValue(objetoList.get(i).getReceitaAno1());
			}
		    
		    HSSFCell cellValorReceitaAno2 = row11.createCell(18);
		    cellValorReceitaAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno2().equals(0L)) {
		    	cellValorReceitaAno2.setCellValue("-");
				
			} else {
				cellValorReceitaAno2.setCellValue(objetoList.get(i).getReceitaAno2());
			}
		    
		    HSSFCell cellValorReceitaAno3 = row11.createCell(19);
		    cellValorReceitaAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList.get(i).getReceitaAno3().equals(0L)) {
		    	cellValorReceitaAno3.setCellValue("-");
				
			} else {
				cellValorReceitaAno3.setCellValue(objetoList.get(i).getReceitaAno3());
			}
		    
		    HSSFCell cellValorVariacaoReceitaAno1 = row11.createCell(21);
		    if(objetoList.get(i).getReceitaVariacaoAno1() == -66893823023L) {
		    	cellValorVariacaoReceitaAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoReceitaAno1.setCellValue(objetoList.get(i).getReceitaVariacaoAno1() + "%");
		    }
		    cellValorVariacaoReceitaAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoReceitaAno2 = row11.createCell(22);
		    if(objetoList.get(i).getReceitaVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoReceitaAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoReceitaAno2.setCellValue(objetoList.get(i).getReceitaVariacaoAno2() + "%");
		    }
		    cellValorVariacaoReceitaAno2.setCellStyle(cellDadosGerais);
		    
//			sheet.addMergedRegion(new CellRangeAddress(11 + i, 11 + i, 1, 1));
		}
		
		HSSFRow rowLast3 = sheet.createRow(objetoList.size() + objetoList3.size() + 16);
		HSSFCell cellSomatorioCabecalho3 = rowLast3.createCell(1);
		cellSomatorioCabecalho3.setCellValue("Total Acumulado");
		cellSomatorioCabecalho3.setCellStyle(cellDadosSomatorio);
		
		sheet.addMergedRegion(new CellRangeAddress(objetoList.size() + objetoList3.size() + 16, objetoList.size() + objetoList3.size() + 17, 1, 1));
		
		indexAnos = 0;
		indexSomatorio = 3;
		
		for(int i=0; i< listaSomaAno.size(); i++) {
			HSSFCell cellSomatorioAno = rowLast3.createCell(indexSomatorio);
			cellSomatorioAno.setCellValue(listaSomaAno.get(i));
			cellSomatorioAno.setCellStyle(cellDadosSomatorio);
			
			sheet.addMergedRegion(new CellRangeAddress(objetoList.size() + objetoList3.size() + 16, objetoList.size() + objetoList3.size() + 17, indexSomatorio, indexSomatorio));
			
			if(indexAnos == 2) {
				indexSomatorio += 5;
				indexAnos = 0;
				
			} else {
				indexSomatorio++;
				indexAnos++;
			}
		}
		
		index = 7;
		for(int i=0; i< listaSomaVariacao.size(); i++) {
			HSSFCell cellSomatorioVariacao = rowLast3.createCell(index);
			cellSomatorioVariacao.setCellValue(listaSomaVariacao.get(i) + "%");
			cellSomatorioVariacao.setCellStyle(cellDadosSomatorio);
			
			sheet.addMergedRegion(new CellRangeAddress(objetoList.size() + objetoList3.size() + 16, objetoList.size() + objetoList3.size() + 17, index, index));
			
			if(i%2 == 0) {
				index++;
			} else {
				index += 6;
			}
		}
		
//		for(int i=2; i<24; i++) {
//			sheet.autoSizeColumn((short) i, true);
//		}
		
		HSSFRow row10b = sheet.createRow(19 + objetoList3.size() + objetoList.size());
		HSSFCell cellIdentificacaoTipo3 = row10b.createCell(3);
		cellIdentificacaoTipo3.setCellValue("País acumulado");
		cellIdentificacaoTipo3.setCellStyle(cellDadosPaises);
		
//		//acumulado
		for(int i = 0; i < objetoList2.size(); i++) {
			
			HSSFRow row11 = sheet.createRow(objetoList.size() + objetoList3.size() + 23 + i);
			HSSFCell cellTituloDestino = row11.createCell(1);
		    cellTituloDestino.setCellValue(" " + objetoList2.get(i).getPais());
			cellTituloDestino.setCellStyle(cellDadosPaises);
			
			HSSFCell cellValorPesoAno1 = row11.createCell(3);
		    cellValorPesoAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getPesoAno1().equals(0L)) {
		    	cellValorPesoAno1.setCellValue("-");
		    	
			} else {
				cellValorPesoAno1.setCellValue(objetoList2.get(i).getPesoAno1());
			}
		    
		    HSSFCell cellValorPesoAno2 = row11.createCell(4);
		    cellValorPesoAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getPesoAno2().equals(0L)) {
		    	cellValorPesoAno2.setCellValue("-");
				
			} else {
				cellValorPesoAno2.setCellValue(objetoList2.get(i).getPesoAno2());
			}
		    
		    HSSFCell cellValorPesoAno3 = row11.createCell(5);
		    cellValorPesoAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getPesoAno3().equals(0L)) {
		    	cellValorPesoAno3.setCellValue("-");
				
			} else {
				cellValorPesoAno3.setCellValue(objetoList2.get(i).getPesoAno3());
			}
		    
		    HSSFCell cellValorVariacaoPesoAno1 = row11.createCell(7);
		    if(objetoList2.get(i).getPesoVariacaoAno1() == -66893823023L) {
			    cellValorVariacaoPesoAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoPesoAno1.setCellValue(objetoList2.get(i).getPesoVariacaoAno1() + "%");
		    }
		    cellValorVariacaoPesoAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoPesoAno2 = row11.createCell(8);
		    if(objetoList2.get(i).getPesoVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoPesoAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoPesoAno2.setCellValue(objetoList2.get(i).getPesoVariacaoAno2() + "%");
		    }
		    cellValorVariacaoPesoAno2.setCellStyle(cellDadosGerais);
		    
		    
		    HSSFCell cellValorSacasAno1 = row11.createCell(10);
		    cellValorSacasAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getSaca60kgAno1().equals(0L)) {
		    	cellValorSacasAno1.setCellValue("-");
				
			} else {
				cellValorSacasAno1.setCellValue(objetoList2.get(i).getSaca60kgAno1());
			}
		    
		    HSSFCell cellValorSacasAno2 = row11.createCell(11);
		    cellValorSacasAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getSaca60kgAno2().equals(0L)) {
		    	cellValorSacasAno2.setCellValue("-");
		    	
			} else {
				cellValorSacasAno2.setCellValue(objetoList2.get(i).getSaca60kgAno2());
			}
		    
		    HSSFCell cellValorSacasAno3 = row11.createCell(12);
		    cellValorSacasAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getSaca60kgAno3().equals(0L)) {
		    	cellValorSacasAno3.setCellValue("-");
				
			} else {
				cellValorSacasAno3.setCellValue(objetoList2.get(i).getSaca60kgAno3());
			}
		    
		    HSSFCell cellValorVariacaoSacasAno1 = row11.createCell(14);
		    if(objetoList2.get(i).getSaca60kgVariacaoAno1() == -66893823023L) {
		    	cellValorVariacaoSacasAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoSacasAno1.setCellValue(objetoList2.get(i).getSaca60kgVariacaoAno1() + "%");
		    }
		    cellValorVariacaoSacasAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoSacasAno2 = row11.createCell(15);
		    if(objetoList2.get(i).getSaca60kgVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoSacasAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoSacasAno2.setCellValue(objetoList2.get(i).getSaca60kgVariacaoAno2() + "%");
		    }
		    cellValorVariacaoSacasAno2.setCellStyle(cellDadosGerais);
		    
		    
		    HSSFCell cellValorReceitaAno1 = row11.createCell(17);
		    cellValorReceitaAno1.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getReceitaAno1().equals(0L)) {
		    	cellValorReceitaAno1.setCellValue("-");
		    	
			} else {
				cellValorReceitaAno1.setCellValue(objetoList2.get(i).getReceitaAno1());
			}
		    
		    HSSFCell cellValorReceitaAno2 = row11.createCell(18);
		    cellValorReceitaAno2.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getReceitaAno2().equals(0L)) {
		    	cellValorReceitaAno2.setCellValue("-");
				
			} else {
				cellValorReceitaAno2.setCellValue(objetoList2.get(i).getReceitaAno2());
			}
		    
		    HSSFCell cellValorReceitaAno3 = row11.createCell(19);
		    cellValorReceitaAno3.setCellStyle(cellDadosGerais);
		    
		    if(objetoList2.get(i).getReceitaAno3().equals(0L)) {
		    	cellValorReceitaAno3.setCellValue("-");
				
			} else {
				cellValorReceitaAno3.setCellValue(objetoList2.get(i).getReceitaAno3());
			}
		    
		    HSSFCell cellValorVariacaoReceitaAno1 = row11.createCell(21);
		    if(objetoList2.get(i).getReceitaVariacaoAno1() == -66893823023L) {
		    	cellValorVariacaoReceitaAno1.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoReceitaAno1.setCellValue(objetoList2.get(i).getReceitaVariacaoAno1() + "%");
		    }
		    cellValorVariacaoReceitaAno1.setCellStyle(cellDadosGerais);
		    
		    HSSFCell cellValorVariacaoReceitaAno2 = row11.createCell(22);
		    if(objetoList2.get(i).getSaca60kgVariacaoAno2() == -66893823023L) {
		    	cellValorVariacaoSacasAno2.setCellValue("");
			    
		    } else {
		    	cellValorVariacaoSacasAno2.setCellValue(objetoList2.get(i).getSaca60kgVariacaoAno2() + "%");
		    }
		    cellValorVariacaoReceitaAno2.setCellStyle(cellDadosGerais);
		    
//			sheet.addMergedRegion(new CellRangeAddress(objetoList.size() + 20 + i, objetoList.size() + 20 + i, 1, 1));
		}
		
		int linha = objetoList2.size() + objetoList.size() + objetoList3.size() + 23;
		
		HSSFRow rowLast2 = sheet.createRow(linha);
		HSSFCell cellSomatorioCabecalho2 = rowLast2.createCell(1);
		cellSomatorioCabecalho2.setCellValue("Total Acumulado");
		cellSomatorioCabecalho2.setCellStyle(cellDadosSomatorio);
		
		sheet.addMergedRegion(new CellRangeAddress(linha, linha + 1, 1, 1));
		
		indexAnos = 0;
		indexSomatorio = 3;
		
		for(int i=0; i< listaSomaAno2.size(); i++) {
			HSSFCell cellSomatorioAno = rowLast2.createCell(indexSomatorio);
			cellSomatorioAno.setCellValue(listaSomaAno2.get(i));
			cellSomatorioAno.setCellStyle(cellDadosSomatorio);
			
			sheet.addMergedRegion(new CellRangeAddress(linha, linha+1, indexSomatorio, indexSomatorio));
			
			if(indexAnos == 2) {
				indexSomatorio += 5;
				indexAnos = 0;
				
			} else {
				indexSomatorio++;
				indexAnos++;
			}
		}
		
		index = 7;
		for(int i=0; i< listaSomaVariacao2.size(); i++) {
			HSSFCell cellSomatorioVariacao = rowLast2.createCell(index);
			cellSomatorioVariacao.setCellValue(listaSomaVariacao2.get(i) + "%");
			cellSomatorioVariacao.setCellStyle(cellDadosSomatorio);
			
			sheet.addMergedRegion(new CellRangeAddress(linha, linha + 1, index, index));
			
			if(i%2 == 0) {
				index++;
			} else {
				index += 6;
			}
		}
		
//		for(int i=2; i<24; i++) {
//			sheet.autoSizeColumn((short) i, true);
//		}
		
		sheet.setColumnWidth(0, 500);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 500);
		sheet.setColumnWidth(3, 4300);
		sheet.setColumnWidth(4, 4300);
		sheet.setColumnWidth(5, 4300);
		sheet.setColumnWidth(6, 500);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 500);
		sheet.setColumnWidth(10, 4300);
		sheet.setColumnWidth(11, 4300);
		sheet.setColumnWidth(12, 4300);
		sheet.setColumnWidth(13, 500);
		sheet.setColumnWidth(14, 4000);
		sheet.setColumnWidth(15, 4000);
		sheet.setColumnWidth(16, 500);
		sheet.setColumnWidth(17, 4300);
		sheet.setColumnWidth(18, 4300);
		sheet.setColumnWidth(19, 4300);
		sheet.setColumnWidth(20, 500);
		sheet.setColumnWidth(21, 4000);
		sheet.setColumnWidth(22, 4000);
		
		return sheet;
	}
	
	public static HSSFColor setColor(HSSFWorkbook workbook, byte r, byte g,
			byte b, HSSFColor corSubstituir) {
		HSSFPalette palette = workbook.getCustomPalette();
		HSSFColor hssfColor = null;
		try {
			hssfColor = palette.findColor(r, g, b);
			if (hssfColor == null) {
				palette.setColorAtIndex(corSubstituir.getIndex(), r, g, b);
				hssfColor = palette.getColor(corSubstituir.getIndex());
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return hssfColor;
	}
	
	public static CellStyle createStyle(HSSFWorkbook wb, short foregroundColor, short backgroundColor, String fontName, short fontSize, 
			boolean bold, boolean italico, boolean border, String textAlign, String typeText) {

		Font font = wb.createFont();
		font.setFontName(fontName);

		if(bold) {
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}
		if(italico) {
			font.setItalic(true);
		}
		
		font.setColor(foregroundColor);
		font.setFontHeightInPoints(fontSize);

		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(font);
		
		if(border) {
			cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(IndexedColors.BROWN.getIndex());
			cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(IndexedColors.BROWN.getIndex());
			cellStyle.setBorderRight(CellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(IndexedColors.BROWN.getIndex());
			cellStyle.setBorderTop(CellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(IndexedColors.BROWN.getIndex());
		}
		
		if(textAlign.equalsIgnoreCase("center")) {
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			
		} else if(textAlign.equalsIgnoreCase("right")) {
			cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
			
		} else if(textAlign.equalsIgnoreCase("left")) {
			cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		}
		
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setFillForegroundColor(backgroundColor);
		cellStyle.setFillBackgroundColor(backgroundColor);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		if(typeText.equalsIgnoreCase("numeric")) {
		    HSSFDataFormat hssfDataFormat = wb.createDataFormat(); 
		    cellStyle.setDataFormat(hssfDataFormat.getFormat("#,##0"));
		    
		} else if(typeText.equalsIgnoreCase("dot")) {
			HSSFDataFormat hssfDataFormat = wb.createDataFormat(); 
		    cellStyle.setDataFormat(hssfDataFormat.getFormat("#,##0.00"));
		}

		return cellStyle;
	}
	
	public static CellStyle createStyle(HSSFWorkbook wb, short foregroundColor, short backgroundColor, short fontSize, 
			boolean bold, boolean italico, boolean border) {

		Font font = wb.createFont();

		if(bold) {
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		}
		if(italico) {
			font.setItalic(true);
		}
		
		font.setColor(foregroundColor);
		font.setFontHeightInPoints(fontSize);

		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(font);
		
		if(border) {
			cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(IndexedColors.BROWN.getIndex());
			cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
			cellStyle.setLeftBorderColor(IndexedColors.BROWN.getIndex());
			cellStyle.setBorderRight(CellStyle.BORDER_THIN);
			cellStyle.setRightBorderColor(IndexedColors.BROWN.getIndex());
			cellStyle.setBorderTop(CellStyle.BORDER_THIN);
			cellStyle.setTopBorderColor(IndexedColors.BROWN.getIndex());
		}
		
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setFillForegroundColor(backgroundColor);
		cellStyle.setFillBackgroundColor(backgroundColor);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

		return cellStyle;
	}
	
	public static CellStyle createStyleHeader(HSSFWorkbook wb, short foregroundColor) {
		
		Font fontBold = wb.createFont();//Create font
	    fontBold.setBoldweight(Font.BOLDWEIGHT_BOLD);//Make font bold
	    
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setFillForegroundColor(foregroundColor);
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle.setFont(fontBold);

        return cellStyle;
	}
	
	public static CellStyle createStyle(HSSFWorkbook wb, short foregroundColor) {
		
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setFillForegroundColor(foregroundColor);
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        return cellStyle;
	}
}
