package util;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import entitys.report.GraficoReportObject;

/**
 * 
 * Classe responsavel para criacao de dados para o relatorio (.jasper)
 * 
 * 
 * */
public class ReportUtil {

	public static InputStream gerarRelatorioHistorico(String dataInicial, String dataFinal, String aplicarLogistica, String tipoMoeda, 
			String caminhoImagem, String caminhoExportar) {
		
		ImageIcon i = new ImageIcon(AbicsLogConfig.getString(AbicsLogConfig.LOGO_ABICS));
		Image logo = i.getImage();
				
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("logo_abics", logo);
		map.put("data_inicial", dataInicial);
		map.put("data_final", dataFinal);
		map.put("aplicar_logistica", aplicarLogistica);
		map.put("tipo_moeda", tipoMoeda);
		map.put("imagemGrafico", caminhoImagem);

		try {
			
			String compiledFile = AbicsLogConfig.getString(AbicsLogConfig.REPORT_FOLDER) + "relatorioHistorico.jasper";
			JasperCompileManager.compileReportToFile(AbicsLogConfig.getString(AbicsLogConfig.REPORT_FOLDER) + "relatorioHistorico.jrxml", compiledFile);
			List<GraficoReportObject> list = new ArrayList<GraficoReportObject>();
			GraficoReportObject e = new GraficoReportObject(); 
			list.add(e);
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(list, false);
			JasperPrint jrprint = JasperFillManager.fillReport(compiledFile, map, ds);
			JasperExportManager.exportReportToPdfFile(jrprint, caminhoExportar);
			
			return new FileInputStream(caminhoExportar);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
