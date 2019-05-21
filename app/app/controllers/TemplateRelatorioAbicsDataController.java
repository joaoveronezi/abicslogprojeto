package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import models.AbicsEmpresa;
import models.AbicsExportacao;
import models.AbicsExportacaoEmpresa;
import models.AbicsExportacaoPais;
import models.AbicsExportacaoProduto;
import models.AbicsPais;
import models.AbicsProduto;
import models.AbicsProduto1190;
import models.AbicsProduto1200;
import models.Pais;

import org.apache.commons.io.IOUtils;

import play.libs.Json;
import play.mvc.Result;
import entitys.response.AbicsDataRelatorioAnualExcelResponse;
import entitys.response.AbicsDataRelatorioExcelResponse;
import entitys.response.TipoRelatorioAbicsDataEnum;

public class TemplateRelatorioAbicsDataController extends AbstractController {

	
	public static String[] meses = {"Meses", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
	public static Integer[] mesesInteger = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
	
	public static Result findData(Integer tipoRelatorio, Integer anoParametro, Integer mesParametro) {
		
		List<AbicsDataRelatorioExcelResponse> listResponse = new ArrayList<AbicsDataRelatorioExcelResponse>();
		
		if(tipoRelatorio == TipoRelatorioAbicsDataEnum.DESEMPENHO_MENSAL.getTipo()) {
			
			Double totalAcumuladoPrimeiroPesoVariacao = 0.0;
			Double totalAcumuladoSegundoPesoVariacao = 0.0;
			Double totalAcumuladoPrimeiroSacaVariacao = 0.0;
			Double totalAcumuladoSegundoSacaVariacao = 0.0;
			Double totalAcumuladoPrimeiroReceitaVariacao = 0.0;
			Double totalAcumuladoSegundoReceitaVariacao = 0.0;
			
			Long totalAcumuladoPrimeiroPeso = 0L;
			Long totalAcumuladoPrimeiroSaca = 0L;
			Long totalAcumuladoPrimeiroReceita = 0L;
			Long totalAcumuladoSegundoPeso = 0L;
			Long totalAcumuladoSegundoSaca = 0L;
			Long totalAcumuladoSegundoReceita = 0L;
			Long totalAcumuladoTerceiroPeso = 0L;
			Long totalAcumuladoTerceiroSaca = 0L;
			Long totalAcumuladoTerceiroReceita = 0L;
			
			for(int mes = 1; mes <= mesParametro; mes++) {
				
				AbicsExportacao primeiroAnoMes = AbicsExportacao.findByAnoMes(anoParametro, mes);
				AbicsExportacao segundoAnoMes = AbicsExportacao.findByAnoMes(anoParametro - 1, mes);
				AbicsExportacao terceiroAnoMes = AbicsExportacao.findByAnoMes(anoParametro - 2, mes);
				
				if(primeiroAnoMes == null && segundoAnoMes == null && terceiroAnoMes == null) {
					continue;
				}
				
				if(primeiroAnoMes == null) {
					primeiroAnoMes = new AbicsExportacao();
					primeiroAnoMes.setPeso(0L);
					primeiroAnoMes.setSaca60kg(0L);
					primeiroAnoMes.setReceita(0L);
				}

				if(segundoAnoMes == null) {
					segundoAnoMes = new AbicsExportacao();
					segundoAnoMes.setPeso(0L);
					segundoAnoMes.setSaca60kg(0L);
					segundoAnoMes.setReceita(0L);
				}
				
				if(terceiroAnoMes == null) {
					terceiroAnoMes = new AbicsExportacao();
					terceiroAnoMes.setPeso(0L);
					terceiroAnoMes.setSaca60kg(0L);
					terceiroAnoMes.setReceita(0L);
				}
				
				Double pesoPrimeiraVariacao = calcularVariacao(primeiroAnoMes.getPeso(), segundoAnoMes.getPeso());
				Double pesoSegundaVariacao = calcularVariacao(primeiroAnoMes.getPeso(), terceiroAnoMes.getPeso());
				
				Double sacaPrimeiraVariacao = calcularVariacao(primeiroAnoMes.getSaca60kg(), segundoAnoMes.getSaca60kg());
				Double sacaSegundaVariacao = calcularVariacao(primeiroAnoMes.getSaca60kg(), terceiroAnoMes.getSaca60kg());
				
				Double receitaPrimeiraVariacao = calcularVariacao(primeiroAnoMes.getReceita(), segundoAnoMes.getReceita());
				Double receitaSegundaVariacao = calcularVariacao(primeiroAnoMes.getReceita(), terceiroAnoMes.getReceita());
				
				AbicsDataRelatorioExcelResponse data = new AbicsDataRelatorioExcelResponse(Long.valueOf(mes) * -1,
						meses[mes], String.valueOf(primeiroAnoMes.getPeso()), 
						String.valueOf(segundoAnoMes.getPeso()), String.valueOf(terceiroAnoMes.getPeso()),
						pesoPrimeiraVariacao.toString() + "%", pesoSegundaVariacao.toString() + "%",
						String.valueOf(primeiroAnoMes.getSaca60kg()), 
						String.valueOf(segundoAnoMes.getSaca60kg()), String.valueOf(terceiroAnoMes.getSaca60kg()),
						sacaPrimeiraVariacao.toString() + "%", sacaSegundaVariacao.toString() + "%",
						String.valueOf(primeiroAnoMes.getReceita()), 
						String.valueOf(segundoAnoMes.getReceita()), String.valueOf(terceiroAnoMes.getReceita()),
						receitaPrimeiraVariacao.toString() + "%", receitaSegundaVariacao.toString() + "%");
				
				listResponse.add(data);
				
				totalAcumuladoPrimeiroPeso += primeiroAnoMes.getPeso();
				totalAcumuladoPrimeiroSaca += primeiroAnoMes.getSaca60kg();
				totalAcumuladoPrimeiroReceita += primeiroAnoMes.getReceita();
				
				totalAcumuladoSegundoPeso += segundoAnoMes.getPeso();
				totalAcumuladoSegundoSaca += segundoAnoMes.getSaca60kg();
				totalAcumuladoSegundoReceita += segundoAnoMes.getReceita();
				
				totalAcumuladoTerceiroPeso += terceiroAnoMes.getPeso();
				totalAcumuladoTerceiroSaca += terceiroAnoMes.getSaca60kg();
				totalAcumuladoTerceiroReceita += terceiroAnoMes.getReceita();
				
			}
			totalAcumuladoPrimeiroPesoVariacao = calcularVariacao(totalAcumuladoPrimeiroPeso, totalAcumuladoSegundoPeso);
			totalAcumuladoSegundoPesoVariacao =  calcularVariacao(totalAcumuladoPrimeiroPeso, totalAcumuladoTerceiroPeso);

			totalAcumuladoPrimeiroSacaVariacao = calcularVariacao(totalAcumuladoPrimeiroSaca, totalAcumuladoSegundoSaca);
			totalAcumuladoSegundoSacaVariacao =  calcularVariacao(totalAcumuladoPrimeiroSaca, totalAcumuladoTerceiroSaca);
					
			totalAcumuladoPrimeiroReceitaVariacao = calcularVariacao(totalAcumuladoPrimeiroReceita, totalAcumuladoSegundoReceita);
			totalAcumuladoSegundoReceitaVariacao =  calcularVariacao(totalAcumuladoPrimeiroReceita, totalAcumuladoTerceiroReceita);
			
			AbicsDataRelatorioExcelResponse data = new AbicsDataRelatorioExcelResponse(-999L,
					"Total Acumulado", String.valueOf(totalAcumuladoPrimeiroPeso), 
					String.valueOf(totalAcumuladoSegundoPeso), String.valueOf(totalAcumuladoTerceiroPeso),
					totalAcumuladoPrimeiroPesoVariacao.toString() + "%", totalAcumuladoSegundoPesoVariacao.toString() + "%",
					String.valueOf(totalAcumuladoPrimeiroSaca), 
					String.valueOf(totalAcumuladoSegundoSaca), String.valueOf(totalAcumuladoTerceiroSaca),
					totalAcumuladoPrimeiroSacaVariacao.toString() + "%", totalAcumuladoSegundoSacaVariacao.toString() + "%",
					String.valueOf(totalAcumuladoPrimeiroReceita), 
					String.valueOf(totalAcumuladoSegundoReceita), String.valueOf(totalAcumuladoTerceiroReceita),
					totalAcumuladoPrimeiroReceitaVariacao.toString() + "%", totalAcumuladoSegundoReceitaVariacao.toString() + "%");
			
			listResponse.add(data);
			
			//exportador mensal
		} else if(tipoRelatorio == TipoRelatorioAbicsDataEnum.EXPORTADOR_MENSAL.getTipo()) {
			
			List<AbicsEmpresa> empresas = AbicsEmpresa.findAll();
			Long totalAcumuladoPrimeiroPeso = 0L;
			Long totalAcumuladoPrimeiroSaca = 0L;
			Long totalAcumuladoPrimeiroReceita = 0L;

			Long totalAcumuladoSegundoPeso = 0L;
			Long totalAcumuladoSegundoSaca = 0L;
			Long totalAcumuladoSegundoReceita = 0L;
			
			Long totalAcumuladoTerceiroPeso = 0L;
			Long totalAcumuladoTerceiroSaca = 0L;
			Long totalAcumuladoTerceiroReceita = 0L;
			
			Double totalAcumuladoPrimeiroPesoVariacao = 0.0;
			Double totalAcumuladoSegundoPesoVariacao = 0.0;
			Double totalAcumuladoPrimeiroSacaVariacao = 0.0;
			Double totalAcumuladoSegundoSacaVariacao = 0.0;
			Double totalAcumuladoPrimeiroReceitaVariacao = 0.0;
			Double totalAcumuladoSegundoReceitaVariacao = 0.0;
			
			for(AbicsEmpresa empresa : empresas) {
				
				AbicsExportacaoEmpresa primeiroAnoMesEmpresa = AbicsExportacaoEmpresa.findByAnoMesEmpresaId(anoParametro, mesParametro, empresa.getId());
				AbicsExportacaoEmpresa segundoAnoMesEmpresa = AbicsExportacaoEmpresa.findByAnoMesEmpresaId(anoParametro - 1, mesParametro, empresa.getId());
				AbicsExportacaoEmpresa terceiroAnoMesEmpresa = AbicsExportacaoEmpresa.findByAnoMesEmpresaId(anoParametro - 2, mesParametro, empresa.getId());
				
				if(primeiroAnoMesEmpresa == null && segundoAnoMesEmpresa == null && terceiroAnoMesEmpresa == null) {
					continue;
				}
				
				if(primeiroAnoMesEmpresa == null) {
					primeiroAnoMesEmpresa = new AbicsExportacaoEmpresa();
					primeiroAnoMesEmpresa.setPeso(0L);
					primeiroAnoMesEmpresa.setSaca60kg(0L);
					primeiroAnoMesEmpresa.setReceita(0L);
				}

				if(segundoAnoMesEmpresa == null) {
					segundoAnoMesEmpresa = new AbicsExportacaoEmpresa();
					segundoAnoMesEmpresa.setPeso(0L);
					segundoAnoMesEmpresa.setSaca60kg(0L);
					segundoAnoMesEmpresa.setReceita(0L);
				}
				
				if(terceiroAnoMesEmpresa == null) {
					terceiroAnoMesEmpresa = new AbicsExportacaoEmpresa();
					terceiroAnoMesEmpresa.setPeso(0L);
					terceiroAnoMesEmpresa.setSaca60kg(0L);
					terceiroAnoMesEmpresa.setReceita(0L);
				}
				
				Double pesoPrimeiraVariacao = calcularVariacao(primeiroAnoMesEmpresa.getPeso(), segundoAnoMesEmpresa.getPeso());
				Double pesoSegundaVariacao = calcularVariacao(primeiroAnoMesEmpresa.getPeso(), terceiroAnoMesEmpresa.getPeso());
				
				Double sacaPrimeiraVariacao = calcularVariacao(primeiroAnoMesEmpresa.getSaca60kg(), segundoAnoMesEmpresa.getSaca60kg());
				Double sacaSegundaVariacao = calcularVariacao(primeiroAnoMesEmpresa.getSaca60kg(), terceiroAnoMesEmpresa.getSaca60kg());
				
				Double receitaPrimeiraVariacao = calcularVariacao(primeiroAnoMesEmpresa.getReceita(), segundoAnoMesEmpresa.getReceita());
				Double receitaSegundaVariacao = calcularVariacao(primeiroAnoMesEmpresa.getReceita(), terceiroAnoMesEmpresa.getReceita());
				
				AbicsDataRelatorioExcelResponse data = new AbicsDataRelatorioExcelResponse(primeiroAnoMesEmpresa.getPeso(),
						empresa.getNome(), String.valueOf(primeiroAnoMesEmpresa.getPeso()), 
						String.valueOf(segundoAnoMesEmpresa.getPeso()), String.valueOf(terceiroAnoMesEmpresa.getPeso()),
						pesoPrimeiraVariacao.toString() + "%", pesoSegundaVariacao.toString() + "%",
						String.valueOf(primeiroAnoMesEmpresa.getSaca60kg()), 
						String.valueOf(segundoAnoMesEmpresa.getSaca60kg()), String.valueOf(terceiroAnoMesEmpresa.getSaca60kg()),
						sacaPrimeiraVariacao.toString() + "%", sacaSegundaVariacao.toString() + "%",
						String.valueOf(primeiroAnoMesEmpresa.getReceita()), 
						String.valueOf(segundoAnoMesEmpresa.getReceita()), String.valueOf(terceiroAnoMesEmpresa.getReceita()),
						receitaPrimeiraVariacao.toString() + "%", receitaSegundaVariacao.toString() + "%");
				
				listResponse.add(data);
				
				totalAcumuladoPrimeiroPeso += primeiroAnoMesEmpresa.getPeso();
				totalAcumuladoPrimeiroSaca += primeiroAnoMesEmpresa.getSaca60kg();
				totalAcumuladoPrimeiroReceita += primeiroAnoMesEmpresa.getReceita();
				
				totalAcumuladoSegundoPeso += segundoAnoMesEmpresa.getPeso();
				totalAcumuladoSegundoSaca += segundoAnoMesEmpresa.getSaca60kg();
				totalAcumuladoSegundoReceita += segundoAnoMesEmpresa.getReceita();
				
				totalAcumuladoTerceiroPeso += terceiroAnoMesEmpresa.getPeso();
				totalAcumuladoTerceiroSaca += terceiroAnoMesEmpresa.getSaca60kg();
				totalAcumuladoTerceiroReceita += terceiroAnoMesEmpresa.getReceita();
			}
			
			totalAcumuladoPrimeiroPesoVariacao = calcularVariacao(totalAcumuladoPrimeiroPeso, totalAcumuladoSegundoPeso);
			totalAcumuladoSegundoPesoVariacao =  calcularVariacao(totalAcumuladoPrimeiroPeso, totalAcumuladoTerceiroPeso);

			totalAcumuladoPrimeiroSacaVariacao = calcularVariacao(totalAcumuladoPrimeiroSaca, totalAcumuladoSegundoSaca);
			totalAcumuladoSegundoSacaVariacao =  calcularVariacao(totalAcumuladoPrimeiroSaca, totalAcumuladoTerceiroSaca);
					
			totalAcumuladoPrimeiroReceitaVariacao = calcularVariacao(totalAcumuladoPrimeiroReceita, totalAcumuladoSegundoReceita);
			totalAcumuladoSegundoReceitaVariacao =  calcularVariacao(totalAcumuladoPrimeiroReceita, totalAcumuladoTerceiroReceita);
			
			AbicsDataRelatorioExcelResponse data = new AbicsDataRelatorioExcelResponse(-999L,
					"Total Acumulado", String.valueOf(totalAcumuladoPrimeiroPeso), 
					String.valueOf(totalAcumuladoSegundoPeso), String.valueOf(totalAcumuladoTerceiroPeso),
					totalAcumuladoPrimeiroPesoVariacao.toString() + "%", totalAcumuladoSegundoPesoVariacao.toString() + "%",
					String.valueOf(totalAcumuladoPrimeiroSaca), 
					String.valueOf(totalAcumuladoSegundoSaca), String.valueOf(totalAcumuladoTerceiroSaca),
					totalAcumuladoPrimeiroSacaVariacao.toString() + "%", totalAcumuladoSegundoSacaVariacao.toString() + "%",
					String.valueOf(totalAcumuladoPrimeiroReceita), 
					String.valueOf(totalAcumuladoSegundoReceita), String.valueOf(totalAcumuladoTerceiroReceita),
					totalAcumuladoPrimeiroReceitaVariacao.toString() + "%", totalAcumuladoSegundoReceitaVariacao.toString() + "%");
			
			listResponse.add(data);
			
			//exportador acumulado
		} else if(tipoRelatorio == TipoRelatorioAbicsDataEnum.EXPORTADOR_ACUMULADO.getTipo()) {
			
			List<AbicsEmpresa> empresas = AbicsEmpresa.findAll();
//			empresas = empresas.subList(0, 5);
			
			//Ultima celula, total acumulado de todas empresas
			Long totalAcumuladoPrimeiroPeso = 0L;
			Long totalAcumuladoPrimeiroSaca = 0L;
			Long totalAcumuladoPrimeiroReceita = 0L;
			Long totalAcumuladoSegundoPeso = 0L;
			Long totalAcumuladoSegundoSaca = 0L;
			Long totalAcumuladoSegundoReceita = 0L;
			Long totalAcumuladoTerceiroPeso = 0L;
			Long totalAcumuladoTerceiroSaca = 0L;
			Long totalAcumuladoTerceiroReceita = 0L;
			
			for(AbicsEmpresa empresa : empresas) {
				
				Long pesoPrimeiroAcumuladoEmpresaTotal = 0L;
				Long sacaPrimeiroAcumuladoEmpresaTotal = 0L;
				Long receitaPrimeiroAcumuladoEmpresaTotal = 0L;
				
				Long pesoSegundoAcumuladoEmpresaTotal = 0L;
				Long sacaSegundoAcumuladoEmpresaTotal = 0L;
				Long receitaSegundoAcumuladoEmpresaTotal = 0L;
				
				Long pesoTerceiroAcumuladoEmpresaTotal = 0L;
				Long sacaTerceiroAcumuladoEmpresaTotal = 0L;
				Long receitaTerceiroAcumuladoEmpresaTotal = 0L;
				
				Double totalAcumuladoPrimeiroPesoVariacao = 0.0;
				Double totalAcumuladoSegundoPesoVariacao = 0.0;
				Double totalAcumuladoPrimeiroSacaVariacao = 0.0;
				Double totalAcumuladoSegundoSacaVariacao = 0.0;
				Double totalAcumuladoPrimeiroReceitaVariacao = 0.0;
				Double totalAcumuladoSegundoReceitaVariacao = 0.0;
				
				for(int i = 0; i < 3; i++) {
					
					Integer anoAtual = anoParametro - i;
					Long pesoTotalAno = 0L;
					Long sacaTotalAno = 0L;
					Long receitaTotalAno = 0L;
					
					for(int mes = 1; mes <= mesParametro; mes++) {
						
						AbicsExportacaoEmpresa primeiroAnoMesEmpresa = AbicsExportacaoEmpresa.findByAnoMesEmpresaId(anoAtual, mes, empresa.getId());
					
						if(primeiroAnoMesEmpresa == null) {
							primeiroAnoMesEmpresa = new AbicsExportacaoEmpresa();
							primeiroAnoMesEmpresa.setPeso(0L);
							primeiroAnoMesEmpresa.setSaca60kg(0L);
							primeiroAnoMesEmpresa.setReceita(0L);
						}
						
						pesoTotalAno += primeiroAnoMesEmpresa.getPeso();
						sacaTotalAno += primeiroAnoMesEmpresa.getSaca60kg();
						receitaTotalAno += primeiroAnoMesEmpresa.getReceita();
						
					}
					if(i == 0) {
						pesoPrimeiroAcumuladoEmpresaTotal = pesoTotalAno;
						sacaPrimeiroAcumuladoEmpresaTotal = sacaTotalAno;
						receitaPrimeiroAcumuladoEmpresaTotal = receitaTotalAno;
					} else if(i == 1) {
						pesoSegundoAcumuladoEmpresaTotal = pesoTotalAno;
						sacaSegundoAcumuladoEmpresaTotal = sacaTotalAno;
						receitaSegundoAcumuladoEmpresaTotal = receitaTotalAno;
					} else if(i == 2) {
						pesoTerceiroAcumuladoEmpresaTotal = pesoTotalAno;
						sacaTerceiroAcumuladoEmpresaTotal = sacaTotalAno;
						receitaTerceiroAcumuladoEmpresaTotal = receitaTotalAno;
					}
				}
				
				if(pesoPrimeiroAcumuladoEmpresaTotal == 0L && pesoSegundoAcumuladoEmpresaTotal == 0L && pesoTerceiroAcumuladoEmpresaTotal == 0L) {
					continue;
				}
				
				totalAcumuladoPrimeiroPesoVariacao = calcularVariacao(pesoPrimeiroAcumuladoEmpresaTotal, pesoSegundoAcumuladoEmpresaTotal);
				totalAcumuladoSegundoPesoVariacao =  calcularVariacao(pesoPrimeiroAcumuladoEmpresaTotal, pesoTerceiroAcumuladoEmpresaTotal);
				
				totalAcumuladoPrimeiroSacaVariacao = calcularVariacao(sacaPrimeiroAcumuladoEmpresaTotal, sacaSegundoAcumuladoEmpresaTotal);
				totalAcumuladoSegundoSacaVariacao =  calcularVariacao(sacaPrimeiroAcumuladoEmpresaTotal, sacaTerceiroAcumuladoEmpresaTotal);
				
				totalAcumuladoPrimeiroReceitaVariacao = calcularVariacao(receitaPrimeiroAcumuladoEmpresaTotal, receitaSegundoAcumuladoEmpresaTotal);
				totalAcumuladoSegundoReceitaVariacao =  calcularVariacao(receitaPrimeiroAcumuladoEmpresaTotal, receitaTerceiroAcumuladoEmpresaTotal);
				
				AbicsDataRelatorioExcelResponse data = new AbicsDataRelatorioExcelResponse(pesoPrimeiroAcumuladoEmpresaTotal,
						empresa.getNome(), String.valueOf(pesoPrimeiroAcumuladoEmpresaTotal), 
						String.valueOf(pesoSegundoAcumuladoEmpresaTotal), String.valueOf(pesoTerceiroAcumuladoEmpresaTotal),
						totalAcumuladoPrimeiroPesoVariacao.toString() + "%", totalAcumuladoSegundoPesoVariacao.toString() + "%",
						String.valueOf(sacaPrimeiroAcumuladoEmpresaTotal), 
						String.valueOf(sacaSegundoAcumuladoEmpresaTotal), String.valueOf(sacaTerceiroAcumuladoEmpresaTotal),
						totalAcumuladoPrimeiroSacaVariacao.toString() + "%", totalAcumuladoSegundoSacaVariacao.toString() + "%",
						String.valueOf(receitaPrimeiroAcumuladoEmpresaTotal), 
						String.valueOf(receitaSegundoAcumuladoEmpresaTotal), String.valueOf(receitaTerceiroAcumuladoEmpresaTotal),
						totalAcumuladoPrimeiroReceitaVariacao.toString() + "%", totalAcumuladoSegundoReceitaVariacao.toString() + "%");
				
				listResponse.add(data);
				
				totalAcumuladoPrimeiroPeso += pesoPrimeiroAcumuladoEmpresaTotal;
				totalAcumuladoPrimeiroSaca += sacaPrimeiroAcumuladoEmpresaTotal;
				totalAcumuladoPrimeiroReceita += receitaPrimeiroAcumuladoEmpresaTotal;
				totalAcumuladoSegundoPeso += pesoSegundoAcumuladoEmpresaTotal;
				totalAcumuladoSegundoSaca += sacaSegundoAcumuladoEmpresaTotal;
				totalAcumuladoSegundoReceita += receitaSegundoAcumuladoEmpresaTotal;
				totalAcumuladoTerceiroPeso += pesoTerceiroAcumuladoEmpresaTotal;
				totalAcumuladoTerceiroSaca += sacaTerceiroAcumuladoEmpresaTotal;
				totalAcumuladoTerceiroReceita += receitaTerceiroAcumuladoEmpresaTotal;
			}
			
			Double totalAcumuladoPrimeiroPesoVariacao = calcularVariacao(totalAcumuladoPrimeiroPeso, totalAcumuladoSegundoPeso);
			Double totalAcumuladoSegundoPesoVariacao =  calcularVariacao(totalAcumuladoPrimeiroPeso, totalAcumuladoTerceiroPeso);
			Double totalAcumuladoPrimeiroSacaVariacao = calcularVariacao(totalAcumuladoPrimeiroSaca, totalAcumuladoSegundoSaca);
			Double totalAcumuladoSegundoSacaVariacao =  calcularVariacao(totalAcumuladoPrimeiroSaca, totalAcumuladoTerceiroSaca);
			Double totalAcumuladoPrimeiroReceitaVariacao = calcularVariacao(totalAcumuladoPrimeiroReceita, totalAcumuladoSegundoReceita);
			Double totalAcumuladoSegundoReceitaVariacao =  calcularVariacao(totalAcumuladoPrimeiroReceita, totalAcumuladoTerceiroReceita);
			
			AbicsDataRelatorioExcelResponse data = new AbicsDataRelatorioExcelResponse(-999L,
					"Total Acumulado", String.valueOf(totalAcumuladoPrimeiroPeso), 
					String.valueOf(totalAcumuladoSegundoPeso), String.valueOf(totalAcumuladoTerceiroPeso),
					totalAcumuladoPrimeiroPesoVariacao.toString() + "%", totalAcumuladoSegundoPesoVariacao.toString() + "%",
					String.valueOf(totalAcumuladoPrimeiroSaca), 
					String.valueOf(totalAcumuladoSegundoSaca), String.valueOf(totalAcumuladoTerceiroSaca),
					totalAcumuladoPrimeiroSacaVariacao.toString() + "%", totalAcumuladoSegundoSacaVariacao.toString() + "%",
					String.valueOf(totalAcumuladoPrimeiroReceita), 
					String.valueOf(totalAcumuladoSegundoReceita), String.valueOf(totalAcumuladoTerceiroReceita),
					totalAcumuladoPrimeiroReceitaVariacao.toString() + "%", totalAcumuladoSegundoReceitaVariacao.toString() + "%");
			
			listResponse.add(data);
			
			//pais mensal destino
		} else if(tipoRelatorio == TipoRelatorioAbicsDataEnum.PAIS_MENSAL.getTipo()) {

			Long totalAcumuladoPrimeiroPeso = 0L;
			Long totalAcumuladoPrimeiroSaca = 0L;
			Long totalAcumuladoPrimeiroReceita = 0L;

			Long totalAcumuladoSegundoPeso = 0L;
			Long totalAcumuladoSegundoSaca = 0L;
			Long totalAcumuladoSegundoReceita = 0L;
			
			Long totalAcumuladoTerceiroPeso = 0L;
			Long totalAcumuladoTerceiroSaca = 0L;
			Long totalAcumuladoTerceiroReceita = 0L;
			
			Double totalAcumuladoPrimeiroPesoVariacao = 0.0;
			Double totalAcumuladoSegundoPesoVariacao = 0.0;
			Double totalAcumuladoPrimeiroSacaVariacao = 0.0;
			Double totalAcumuladoSegundoSacaVariacao = 0.0;
			Double totalAcumuladoPrimeiroReceitaVariacao = 0.0;
			Double totalAcumuladoSegundoReceitaVariacao = 0.0;
			
			List<AbicsPais> paisesAbics = AbicsPais.findAll();
			
			for(AbicsPais paisAbics : paisesAbics) {
				
				Pais pais = Pais.findByAbicsId(paisAbics.getId());
				
				if(pais == null) {
					continue;
				}
				
				AbicsExportacaoPais primeiroAnoMesEmpresa = AbicsExportacaoPais.findByAnoMesPaisId(anoParametro, mesParametro, pais.getId());
				AbicsExportacaoPais segundoAnoMesEmpresa = AbicsExportacaoPais.findByAnoMesPaisId(anoParametro - 1, mesParametro, pais.getId());
				AbicsExportacaoPais terceiroAnoMesEmpresa = AbicsExportacaoPais.findByAnoMesPaisId(anoParametro - 2, mesParametro, pais.getId());

				if(primeiroAnoMesEmpresa == null && segundoAnoMesEmpresa == null && terceiroAnoMesEmpresa == null) {
					continue;
				}
				
				if(primeiroAnoMesEmpresa == null) {
					primeiroAnoMesEmpresa = new AbicsExportacaoPais();
					primeiroAnoMesEmpresa.setPeso(0L);
					primeiroAnoMesEmpresa.setSaca60kg(0L);
					primeiroAnoMesEmpresa.setReceita(0L);
				}

				if(segundoAnoMesEmpresa == null) {
					segundoAnoMesEmpresa = new AbicsExportacaoPais();
					segundoAnoMesEmpresa.setPeso(0L);
					segundoAnoMesEmpresa.setSaca60kg(0L);
					segundoAnoMesEmpresa.setReceita(0L);
				}
				
				if(terceiroAnoMesEmpresa == null) {
					terceiroAnoMesEmpresa = new AbicsExportacaoPais();
					terceiroAnoMesEmpresa.setPeso(0L);
					terceiroAnoMesEmpresa.setSaca60kg(0L);
					terceiroAnoMesEmpresa.setReceita(0L);
				}
				
				Double pesoPrimeiraVariacao = calcularVariacao(primeiroAnoMesEmpresa.getPeso(), segundoAnoMesEmpresa.getPeso());
				Double pesoSegundaVariacao = calcularVariacao(primeiroAnoMesEmpresa.getPeso(), terceiroAnoMesEmpresa.getPeso());
				
				Double sacaPrimeiraVariacao = calcularVariacao(primeiroAnoMesEmpresa.getSaca60kg(), segundoAnoMesEmpresa.getSaca60kg());
				Double sacaSegundaVariacao = calcularVariacao(primeiroAnoMesEmpresa.getSaca60kg(), terceiroAnoMesEmpresa.getSaca60kg());
				
				Double receitaPrimeiraVariacao = calcularVariacao(primeiroAnoMesEmpresa.getReceita(), segundoAnoMesEmpresa.getReceita());
				Double receitaSegundaVariacao = calcularVariacao(primeiroAnoMesEmpresa.getReceita(), terceiroAnoMesEmpresa.getReceita());
				
				AbicsDataRelatorioExcelResponse data = new AbicsDataRelatorioExcelResponse(primeiroAnoMesEmpresa.getPeso(),
						pais.getNome(), String.valueOf(primeiroAnoMesEmpresa.getPeso()), 
						String.valueOf(segundoAnoMesEmpresa.getPeso()), String.valueOf(terceiroAnoMesEmpresa.getPeso()),
						pesoPrimeiraVariacao.toString() + "%", pesoSegundaVariacao.toString() + "%",
						String.valueOf(primeiroAnoMesEmpresa.getSaca60kg()), 
						String.valueOf(segundoAnoMesEmpresa.getSaca60kg()), String.valueOf(terceiroAnoMesEmpresa.getSaca60kg()),
						sacaPrimeiraVariacao.toString() + "%", sacaSegundaVariacao.toString() + "%",
						String.valueOf(primeiroAnoMesEmpresa.getReceita()), 
								String.valueOf(segundoAnoMesEmpresa.getReceita()), String.valueOf(terceiroAnoMesEmpresa.getReceita()),
						receitaPrimeiraVariacao.toString() + "%", receitaSegundaVariacao.toString() + "%");
				
				listResponse.add(data);
				
				totalAcumuladoPrimeiroPeso += primeiroAnoMesEmpresa.getPeso();
				totalAcumuladoPrimeiroSaca += primeiroAnoMesEmpresa.getSaca60kg();
				totalAcumuladoPrimeiroReceita += primeiroAnoMesEmpresa.getReceita();
				
				totalAcumuladoSegundoPeso += segundoAnoMesEmpresa.getPeso();
				totalAcumuladoSegundoSaca += segundoAnoMesEmpresa.getSaca60kg();
				totalAcumuladoSegundoReceita += segundoAnoMesEmpresa.getReceita();
				
				totalAcumuladoTerceiroPeso += terceiroAnoMesEmpresa.getPeso();
				totalAcumuladoTerceiroSaca += terceiroAnoMesEmpresa.getSaca60kg();
				totalAcumuladoTerceiroReceita += terceiroAnoMesEmpresa.getReceita();
			}
			
			totalAcumuladoPrimeiroPesoVariacao = calcularVariacao(totalAcumuladoPrimeiroPeso, totalAcumuladoSegundoPeso);
			totalAcumuladoSegundoPesoVariacao =  calcularVariacao(totalAcumuladoPrimeiroPeso, totalAcumuladoTerceiroPeso);

			totalAcumuladoPrimeiroSacaVariacao = calcularVariacao(totalAcumuladoPrimeiroSaca, totalAcumuladoSegundoSaca);
			totalAcumuladoSegundoSacaVariacao =  calcularVariacao(totalAcumuladoPrimeiroSaca, totalAcumuladoTerceiroSaca);
					
			totalAcumuladoPrimeiroReceitaVariacao = calcularVariacao(totalAcumuladoPrimeiroReceita, totalAcumuladoSegundoReceita);
			totalAcumuladoSegundoReceitaVariacao =  calcularVariacao(totalAcumuladoPrimeiroReceita, totalAcumuladoTerceiroReceita);
			
			AbicsDataRelatorioExcelResponse totalAcumulado = new AbicsDataRelatorioExcelResponse(-999L,
					"Total Acumulado", String.valueOf(totalAcumuladoPrimeiroPeso), 
					String.valueOf(totalAcumuladoSegundoPeso), String.valueOf(totalAcumuladoTerceiroPeso),
					totalAcumuladoPrimeiroPesoVariacao.toString() + "%", totalAcumuladoSegundoPesoVariacao.toString() + "%",
					String.valueOf(totalAcumuladoPrimeiroSaca), 
					String.valueOf(totalAcumuladoSegundoSaca), String.valueOf(totalAcumuladoTerceiroSaca),
					totalAcumuladoPrimeiroSacaVariacao.toString() + "%", totalAcumuladoSegundoSacaVariacao.toString() + "%",
					String.valueOf(totalAcumuladoPrimeiroReceita), 
					String.valueOf(totalAcumuladoSegundoReceita), String.valueOf(totalAcumuladoTerceiroReceita),
					totalAcumuladoPrimeiroReceitaVariacao.toString() + "%", totalAcumuladoSegundoReceitaVariacao.toString() + "%");
			
			listResponse.add(totalAcumulado);
			
			//pais acumulado ano
		} else if(tipoRelatorio == TipoRelatorioAbicsDataEnum.PAIS_ACUMULADO.getTipo()) {

			List<AbicsPais> paisesAbics = AbicsPais.findAll();
			
			for(AbicsPais paisAbics : paisesAbics) {
				
				Pais pais = Pais.findByAbicsId(paisAbics.getId());
				
				if(pais == null) {
					continue;
				}
				
				Long pesoPrimeiroAcumuladoPaisTotal = 0L;
				Long sacaPrimeiroAcumuladoPaisTotal = 0L;
				Long receitaPrimeiroAcumuladoPaisTotal = 0L;
				
				Long pesoSegundoAcumuladoPaisTotal = 0L;
				Long sacaSegundoAcumuladoPaisTotal = 0L;
				Long receitaSegundoAcumuladoPaisTotal = 0L;
				
				Long pesoTerceiroAcumuladoPaisTotal = 0L;
				Long sacaTerceiroAcumuladoPaisTotal = 0L;
				Long receitaTerceiroAcumuladoPaisTotal = 0L;
				
				Double totalAcumuladoPrimeiroPesoVariacao = 0.0;
				Double totalAcumuladoSegundoPesoVariacao = 0.0;
				Double totalAcumuladoPrimeiroSacaVariacao = 0.0;
				Double totalAcumuladoSegundoSacaVariacao = 0.0;
				Double totalAcumuladoPrimeiroReceitaVariacao = 0.0;
				Double totalAcumuladoSegundoReceitaVariacao = 0.0;
				
				for(int i = 0; i < 3; i++) {

					Integer anoAtual = anoParametro - i;
					Long pesoTotalAno = 0L;
					Long sacaTotalAno = 0L;
					Long receitaTotalAno = 0L;
					
					for(int mes = 1; mes <= mesParametro; mes++) {
						
						AbicsExportacaoPais primeiroAnoMesEmpresa = AbicsExportacaoPais.findByAnoMesPaisId(anoAtual, mes, pais.getId());
						
						if(primeiroAnoMesEmpresa == null) {
							primeiroAnoMesEmpresa = new AbicsExportacaoPais();
							primeiroAnoMesEmpresa.setPeso(0L);
							primeiroAnoMesEmpresa.setSaca60kg(0L);
							primeiroAnoMesEmpresa.setReceita(0L);
						}
						
						pesoTotalAno += primeiroAnoMesEmpresa.getPeso();
						sacaTotalAno += primeiroAnoMesEmpresa.getSaca60kg();
						receitaTotalAno += primeiroAnoMesEmpresa.getReceita();
					}
					
					if(i == 0) {
						pesoPrimeiroAcumuladoPaisTotal = pesoTotalAno;
						sacaPrimeiroAcumuladoPaisTotal = sacaTotalAno;
						receitaPrimeiroAcumuladoPaisTotal = receitaTotalAno;
					} else if(i == 1) {
						pesoSegundoAcumuladoPaisTotal = pesoTotalAno;
						sacaSegundoAcumuladoPaisTotal = sacaTotalAno;
						receitaSegundoAcumuladoPaisTotal = receitaTotalAno;
					} else if(i == 2) {
						pesoTerceiroAcumuladoPaisTotal = pesoTotalAno;
						sacaTerceiroAcumuladoPaisTotal = sacaTotalAno;
						receitaTerceiroAcumuladoPaisTotal = receitaTotalAno;
					}
				}
				
				if(pesoPrimeiroAcumuladoPaisTotal == 0L && pesoSegundoAcumuladoPaisTotal == 0L && pesoTerceiroAcumuladoPaisTotal == 0L) {
					continue;
				}
				
				totalAcumuladoPrimeiroPesoVariacao = calcularVariacao(pesoPrimeiroAcumuladoPaisTotal, pesoSegundoAcumuladoPaisTotal);
				totalAcumuladoSegundoPesoVariacao =  calcularVariacao(pesoPrimeiroAcumuladoPaisTotal, pesoTerceiroAcumuladoPaisTotal);
				
				totalAcumuladoPrimeiroSacaVariacao = calcularVariacao(sacaPrimeiroAcumuladoPaisTotal, sacaSegundoAcumuladoPaisTotal);
				totalAcumuladoSegundoSacaVariacao =  calcularVariacao(sacaPrimeiroAcumuladoPaisTotal, sacaTerceiroAcumuladoPaisTotal);
				
				totalAcumuladoPrimeiroReceitaVariacao = calcularVariacao(receitaPrimeiroAcumuladoPaisTotal, receitaSegundoAcumuladoPaisTotal);
				totalAcumuladoSegundoReceitaVariacao =  calcularVariacao(receitaPrimeiroAcumuladoPaisTotal, receitaTerceiroAcumuladoPaisTotal);
				
				AbicsDataRelatorioExcelResponse data = new AbicsDataRelatorioExcelResponse(pesoPrimeiroAcumuladoPaisTotal,
						pais.getNome(), String.valueOf(pesoPrimeiroAcumuladoPaisTotal), 
						String.valueOf(pesoSegundoAcumuladoPaisTotal), String.valueOf(pesoTerceiroAcumuladoPaisTotal),
						totalAcumuladoPrimeiroPesoVariacao.toString() + "%", totalAcumuladoSegundoPesoVariacao.toString() + "%",
						String.valueOf(sacaPrimeiroAcumuladoPaisTotal), 
						String.valueOf(sacaSegundoAcumuladoPaisTotal), String.valueOf(sacaTerceiroAcumuladoPaisTotal),
						totalAcumuladoPrimeiroSacaVariacao.toString() + "%", totalAcumuladoSegundoSacaVariacao.toString() + "%",
						String.valueOf(receitaPrimeiroAcumuladoPaisTotal), 
						String.valueOf(receitaSegundoAcumuladoPaisTotal), String.valueOf(receitaTerceiroAcumuladoPaisTotal),
						totalAcumuladoPrimeiroReceitaVariacao.toString() + "%", totalAcumuladoSegundoReceitaVariacao.toString() + "%");
				
				listResponse.add(data);
			}
			
		} else if(tipoRelatorio == TipoRelatorioAbicsDataEnum.PRODUTO_MENSAL.getTipo()) {
			
			List<AbicsProduto> produtos = AbicsProduto.findAll();
			Long totalAcumuladoPrimeiroPeso = 0L;
			Long totalAcumuladoPrimeiroSaca = 0L;
			Long totalAcumuladoPrimeiroReceita = 0L;

			Long totalAcumuladoSegundoPeso = 0L;
			Long totalAcumuladoSegundoSaca = 0L;
			Long totalAcumuladoSegundoReceita = 0L;
			
			Long totalAcumuladoTerceiroPeso = 0L;
			Long totalAcumuladoTerceiroSaca = 0L;
			Long totalAcumuladoTerceiroReceita = 0L;
			
			Double totalAcumuladoPrimeiroPesoVariacao = 0.0;
			Double totalAcumuladoSegundoPesoVariacao = 0.0;
			Double totalAcumuladoPrimeiroSacaVariacao = 0.0;
			Double totalAcumuladoSegundoSacaVariacao = 0.0;
			Double totalAcumuladoPrimeiroReceitaVariacao = 0.0;
			Double totalAcumuladoSegundoReceitaVariacao = 0.0;
			
			for(AbicsProduto produto : produtos) {
				
				AbicsExportacaoProduto primeiroAnoMesProduto = AbicsExportacaoProduto.findByAnoMesProdutoId(anoParametro, mesParametro, produto.getId());
				AbicsExportacaoProduto segundoAnoMesProduto = AbicsExportacaoProduto.findByAnoMesProdutoId(anoParametro - 1, mesParametro, produto.getId());
				AbicsExportacaoProduto terceiroAnoMesProduto = AbicsExportacaoProduto.findByAnoMesProdutoId(anoParametro - 2, mesParametro, produto.getId());
				
				if(primeiroAnoMesProduto == null && segundoAnoMesProduto == null && terceiroAnoMesProduto == null) {
					continue;
				}
				
				if(primeiroAnoMesProduto == null) {
					primeiroAnoMesProduto = new AbicsExportacaoProduto();
					primeiroAnoMesProduto.setPeso(0L);
					primeiroAnoMesProduto.setSaca60kg(0L);
					primeiroAnoMesProduto.setReceita(0L);
				}

				if(segundoAnoMesProduto == null) {
					segundoAnoMesProduto = new AbicsExportacaoProduto();
					segundoAnoMesProduto.setPeso(0L);
					segundoAnoMesProduto.setSaca60kg(0L);
					segundoAnoMesProduto.setReceita(0L);
				}
				
				if(terceiroAnoMesProduto == null) {
					terceiroAnoMesProduto = new AbicsExportacaoProduto();
					terceiroAnoMesProduto.setPeso(0L);
					terceiroAnoMesProduto.setSaca60kg(0L);
					terceiroAnoMesProduto.setReceita(0L);
				}
				
				Double pesoPrimeiraVariacao = calcularVariacao(primeiroAnoMesProduto.getPeso(), segundoAnoMesProduto.getPeso());
				Double pesoSegundaVariacao = calcularVariacao(primeiroAnoMesProduto.getPeso(), terceiroAnoMesProduto.getPeso());
				
				Double sacaPrimeiraVariacao = calcularVariacao(primeiroAnoMesProduto.getSaca60kg(), segundoAnoMesProduto.getSaca60kg());
				Double sacaSegundaVariacao = calcularVariacao(primeiroAnoMesProduto.getSaca60kg(), terceiroAnoMesProduto.getSaca60kg());
				
				Double receitaPrimeiraVariacao = calcularVariacao(primeiroAnoMesProduto.getReceita(), segundoAnoMesProduto.getReceita());
				Double receitaSegundaVariacao = calcularVariacao(primeiroAnoMesProduto.getReceita(), terceiroAnoMesProduto.getReceita());
				
				AbicsDataRelatorioExcelResponse data = new AbicsDataRelatorioExcelResponse(primeiroAnoMesProduto.getPeso(),
						produto.getNome(), String.valueOf(primeiroAnoMesProduto.getPeso()), 
						String.valueOf(segundoAnoMesProduto.getPeso()), String.valueOf(terceiroAnoMesProduto.getPeso()),
						pesoPrimeiraVariacao.toString() + "%", pesoSegundaVariacao.toString() + "%",
						String.valueOf(primeiroAnoMesProduto.getSaca60kg()), 
						String.valueOf(segundoAnoMesProduto.getSaca60kg()), String.valueOf(terceiroAnoMesProduto.getSaca60kg()),
						sacaPrimeiraVariacao.toString() + "%", sacaSegundaVariacao.toString() + "%",
						String.valueOf(primeiroAnoMesProduto.getReceita()), 
						String.valueOf(segundoAnoMesProduto.getReceita()), String.valueOf(terceiroAnoMesProduto.getReceita()),
						receitaPrimeiraVariacao.toString() + "%", receitaSegundaVariacao.toString() + "%");
				
				listResponse.add(data);
				
				totalAcumuladoPrimeiroPeso += primeiroAnoMesProduto.getPeso();
				totalAcumuladoPrimeiroSaca += primeiroAnoMesProduto.getSaca60kg();
				totalAcumuladoPrimeiroReceita += primeiroAnoMesProduto.getReceita();
				
				totalAcumuladoSegundoPeso += segundoAnoMesProduto.getPeso();
				totalAcumuladoSegundoSaca += segundoAnoMesProduto.getSaca60kg();
				totalAcumuladoSegundoReceita += segundoAnoMesProduto.getReceita();
				
				totalAcumuladoTerceiroPeso += terceiroAnoMesProduto.getPeso();
				totalAcumuladoTerceiroSaca += terceiroAnoMesProduto.getSaca60kg();
				totalAcumuladoTerceiroReceita += terceiroAnoMesProduto.getReceita();
			}
			
			totalAcumuladoPrimeiroPesoVariacao = calcularVariacao(totalAcumuladoPrimeiroPeso, totalAcumuladoSegundoPeso);
			totalAcumuladoSegundoPesoVariacao =  calcularVariacao(totalAcumuladoPrimeiroPeso, totalAcumuladoTerceiroPeso);

			totalAcumuladoPrimeiroSacaVariacao = calcularVariacao(totalAcumuladoPrimeiroSaca, totalAcumuladoSegundoSaca);
			totalAcumuladoSegundoSacaVariacao =  calcularVariacao(totalAcumuladoPrimeiroSaca, totalAcumuladoTerceiroSaca);
					
			totalAcumuladoPrimeiroReceitaVariacao = calcularVariacao(totalAcumuladoPrimeiroReceita, totalAcumuladoSegundoReceita);
			totalAcumuladoSegundoReceitaVariacao =  calcularVariacao(totalAcumuladoPrimeiroReceita, totalAcumuladoTerceiroReceita);
			
			AbicsDataRelatorioExcelResponse data = new AbicsDataRelatorioExcelResponse(-999L,
					"Total Acumulado", String.valueOf(totalAcumuladoPrimeiroPeso), 
					String.valueOf(totalAcumuladoSegundoPeso), String.valueOf(totalAcumuladoTerceiroPeso),
					totalAcumuladoPrimeiroPesoVariacao.toString() + "%", totalAcumuladoSegundoPesoVariacao.toString() + "%",
					String.valueOf(totalAcumuladoPrimeiroSaca), 
					String.valueOf(totalAcumuladoSegundoSaca), String.valueOf(totalAcumuladoTerceiroSaca),
					totalAcumuladoPrimeiroSacaVariacao.toString() + "%", totalAcumuladoSegundoSacaVariacao.toString() + "%",
					String.valueOf(totalAcumuladoPrimeiroReceita), 
					String.valueOf(totalAcumuladoSegundoReceita), String.valueOf(totalAcumuladoTerceiroReceita),
					totalAcumuladoPrimeiroReceitaVariacao.toString() + "%", totalAcumuladoSegundoReceitaVariacao.toString() + "%");
			
			listResponse.add(data);
			
			//produto acumulado
		} else if(tipoRelatorio == TipoRelatorioAbicsDataEnum.PRODUTO_ACUMULADO.getTipo()) {
			
			List<AbicsProduto> produtos = AbicsProduto.findAll();
			
			//Ultima celula, total acumulado de todas empresas
			Long totalAcumuladoPrimeiroPeso = 0L;
			Long totalAcumuladoPrimeiroSaca = 0L;
			Long totalAcumuladoPrimeiroReceita = 0L;
			Long totalAcumuladoSegundoPeso = 0L;
			Long totalAcumuladoSegundoSaca = 0L;
			Long totalAcumuladoSegundoReceita = 0L;
			Long totalAcumuladoTerceiroPeso = 0L;
			Long totalAcumuladoTerceiroSaca = 0L;
			Long totalAcumuladoTerceiroReceita = 0L;
			
			for(AbicsProduto produto : produtos) {
				
				Long pesoPrimeiroAcumuladoEmpresaTotal = 0L;
				Long sacaPrimeiroAcumuladoEmpresaTotal = 0L;
				Long receitaPrimeiroAcumuladoEmpresaTotal = 0L;
				
				Long pesoSegundoAcumuladoEmpresaTotal = 0L;
				Long sacaSegundoAcumuladoEmpresaTotal = 0L;
				Long receitaSegundoAcumuladoEmpresaTotal = 0L;
				
				Long pesoTerceiroAcumuladoEmpresaTotal = 0L;
				Long sacaTerceiroAcumuladoEmpresaTotal = 0L;
				Long receitaTerceiroAcumuladoEmpresaTotal = 0L;
				
				Double totalAcumuladoPrimeiroPesoVariacao = 0.0;
				Double totalAcumuladoSegundoPesoVariacao = 0.0;
				Double totalAcumuladoPrimeiroSacaVariacao = 0.0;
				Double totalAcumuladoSegundoSacaVariacao = 0.0;
				Double totalAcumuladoPrimeiroReceitaVariacao = 0.0;
				Double totalAcumuladoSegundoReceitaVariacao = 0.0;
				
				for(int i = 0; i < 3; i++) {
					
					Integer anoAtual = anoParametro - i;
					Long pesoTotalAno = 0L;
					Long sacaTotalAno = 0L;
					Long receitaTotalAno = 0L;
					
					for(int mes = 1; mes <= mesParametro; mes++) {
						
						AbicsExportacaoProduto primeiroAnoMesProduto = AbicsExportacaoProduto.findByAnoMesProdutoId(anoAtual, mes, produto.getId());
					
						if(primeiroAnoMesProduto == null) {
							primeiroAnoMesProduto = new AbicsExportacaoProduto();
							primeiroAnoMesProduto.setPeso(0L);
							primeiroAnoMesProduto.setSaca60kg(0L);
							primeiroAnoMesProduto.setReceita(0L);
						}
						
						pesoTotalAno += primeiroAnoMesProduto.getPeso();
						sacaTotalAno += primeiroAnoMesProduto.getSaca60kg();
						receitaTotalAno += primeiroAnoMesProduto.getReceita();
						
					}
					if(i == 0) {
						pesoPrimeiroAcumuladoEmpresaTotal = pesoTotalAno;
						sacaPrimeiroAcumuladoEmpresaTotal = sacaTotalAno;
						receitaPrimeiroAcumuladoEmpresaTotal = receitaTotalAno;
					} else if(i == 1) {
						pesoSegundoAcumuladoEmpresaTotal = pesoTotalAno;
						sacaSegundoAcumuladoEmpresaTotal = sacaTotalAno;
						receitaSegundoAcumuladoEmpresaTotal = receitaTotalAno;
					} else if(i == 2) {
						pesoTerceiroAcumuladoEmpresaTotal = pesoTotalAno;
						sacaTerceiroAcumuladoEmpresaTotal = sacaTotalAno;
						receitaTerceiroAcumuladoEmpresaTotal = receitaTotalAno;
					}
				}
				
				if(pesoPrimeiroAcumuladoEmpresaTotal == 0L && pesoSegundoAcumuladoEmpresaTotal == 0L && pesoTerceiroAcumuladoEmpresaTotal == 0L) {
					continue;
				}
				
				totalAcumuladoPrimeiroPesoVariacao = calcularVariacao(pesoPrimeiroAcumuladoEmpresaTotal, pesoSegundoAcumuladoEmpresaTotal);
				totalAcumuladoSegundoPesoVariacao =  calcularVariacao(pesoPrimeiroAcumuladoEmpresaTotal, pesoTerceiroAcumuladoEmpresaTotal);
				
				totalAcumuladoPrimeiroSacaVariacao = calcularVariacao(sacaPrimeiroAcumuladoEmpresaTotal, sacaSegundoAcumuladoEmpresaTotal);
				totalAcumuladoSegundoSacaVariacao =  calcularVariacao(sacaPrimeiroAcumuladoEmpresaTotal, sacaTerceiroAcumuladoEmpresaTotal);
				
				totalAcumuladoPrimeiroReceitaVariacao = calcularVariacao(receitaPrimeiroAcumuladoEmpresaTotal, receitaSegundoAcumuladoEmpresaTotal);
				totalAcumuladoSegundoReceitaVariacao =  calcularVariacao(receitaPrimeiroAcumuladoEmpresaTotal, receitaTerceiroAcumuladoEmpresaTotal);
				
				AbicsDataRelatorioExcelResponse data = new AbicsDataRelatorioExcelResponse(pesoPrimeiroAcumuladoEmpresaTotal,
						produto.getNome(), String.valueOf(pesoPrimeiroAcumuladoEmpresaTotal), 
						String.valueOf(pesoSegundoAcumuladoEmpresaTotal), String.valueOf(pesoTerceiroAcumuladoEmpresaTotal),
						totalAcumuladoPrimeiroPesoVariacao.toString() + "%", totalAcumuladoSegundoPesoVariacao.toString() + "%",
						String.valueOf(sacaPrimeiroAcumuladoEmpresaTotal), 
						String.valueOf(sacaSegundoAcumuladoEmpresaTotal), String.valueOf(sacaTerceiroAcumuladoEmpresaTotal),
						totalAcumuladoPrimeiroSacaVariacao.toString() + "%", totalAcumuladoSegundoSacaVariacao.toString() + "%",
						String.valueOf(receitaPrimeiroAcumuladoEmpresaTotal), 
						String.valueOf(receitaSegundoAcumuladoEmpresaTotal), String.valueOf(receitaTerceiroAcumuladoEmpresaTotal),
						totalAcumuladoPrimeiroReceitaVariacao.toString() + "%", totalAcumuladoSegundoReceitaVariacao.toString() + "%");
				
				listResponse.add(data);
				
				totalAcumuladoPrimeiroPeso += pesoPrimeiroAcumuladoEmpresaTotal;
				totalAcumuladoPrimeiroSaca += sacaPrimeiroAcumuladoEmpresaTotal;
				totalAcumuladoPrimeiroReceita += receitaPrimeiroAcumuladoEmpresaTotal;
				totalAcumuladoSegundoPeso += pesoSegundoAcumuladoEmpresaTotal;
				totalAcumuladoSegundoSaca += sacaSegundoAcumuladoEmpresaTotal;
				totalAcumuladoSegundoReceita += receitaSegundoAcumuladoEmpresaTotal;
				totalAcumuladoTerceiroPeso += pesoTerceiroAcumuladoEmpresaTotal;
				totalAcumuladoTerceiroSaca += sacaTerceiroAcumuladoEmpresaTotal;
				totalAcumuladoTerceiroReceita += receitaTerceiroAcumuladoEmpresaTotal;
			}
			
			Double totalAcumuladoPrimeiroPesoVariacao = calcularVariacao(totalAcumuladoPrimeiroPeso, totalAcumuladoSegundoPeso);
			Double totalAcumuladoSegundoPesoVariacao =  calcularVariacao(totalAcumuladoPrimeiroPeso, totalAcumuladoTerceiroPeso);
			Double totalAcumuladoPrimeiroSacaVariacao = calcularVariacao(totalAcumuladoPrimeiroSaca, totalAcumuladoSegundoSaca);
			Double totalAcumuladoSegundoSacaVariacao =  calcularVariacao(totalAcumuladoPrimeiroSaca, totalAcumuladoTerceiroSaca);
			Double totalAcumuladoPrimeiroReceitaVariacao = calcularVariacao(totalAcumuladoPrimeiroReceita, totalAcumuladoSegundoReceita);
			Double totalAcumuladoSegundoReceitaVariacao =  calcularVariacao(totalAcumuladoPrimeiroReceita, totalAcumuladoTerceiroReceita);
			
			AbicsDataRelatorioExcelResponse data = new AbicsDataRelatorioExcelResponse(-999L,
					"Total Acumulado", String.valueOf(totalAcumuladoPrimeiroPeso), 
					String.valueOf(totalAcumuladoSegundoPeso), String.valueOf(totalAcumuladoTerceiroPeso),
					totalAcumuladoPrimeiroPesoVariacao.toString() + "%", totalAcumuladoSegundoPesoVariacao.toString() + "%",
					String.valueOf(totalAcumuladoPrimeiroSaca), 
					String.valueOf(totalAcumuladoSegundoSaca), String.valueOf(totalAcumuladoTerceiroSaca),
					totalAcumuladoPrimeiroSacaVariacao.toString() + "%", totalAcumuladoSegundoSacaVariacao.toString() + "%",
					String.valueOf(totalAcumuladoPrimeiroReceita), 
					String.valueOf(totalAcumuladoSegundoReceita), String.valueOf(totalAcumuladoTerceiroReceita),
					totalAcumuladoPrimeiroReceitaVariacao.toString() + "%", totalAcumuladoSegundoReceitaVariacao.toString() + "%");
			
			listResponse.add(data);
		}
		
		
		
		listResponse.sort(Comparator.comparing(AbicsDataRelatorioExcelResponse::getPesoComparator));
		Collections.reverse(listResponse);
		
		for(AbicsDataRelatorioExcelResponse response : listResponse) {
			
			if(response.getPesoPrimeiroAno().equals("0")) {
				response.setPesoPrimeiroAno("-");
			}

			if(response.getPesoSegundoAno().equals("0")) {
				response.setPesoSegundoAno("-");
			}
			
			if(response.getPesoTerceiroAno().equals("0")) {
				response.setPesoTerceiroAno("-");
			}

			if(response.getSacaPrimeiroAno().equals("0")) {
				response.setSacaPrimeiroAno("-");
			}

			if(response.getSacaSegundoAno().equals("0")) {
				response.setSacaSegundoAno("-");
			}
			
			if(response.getSacaTerceiroAno().equals("0")) {
				response.setSacaTerceiroAno("-");
			}
			
			if(response.getReceitaPrimeiroAno().equals("0")) {
				response.setReceitaPrimeiroAno("-");
			}

			if(response.getReceitaSegundoAno().equals("0")) {
				response.setReceitaSegundoAno("-");
			}
			
			if(response.getReceitaTerceiroAno().equals("0")) {
				response.setReceitaTerceiroAno("-");
			}
		}
		
		return ok(Json.toJson(listResponse));
	}

	private static Double calcularVariacao(Long valorInicial, Long valorFinal) {
		
		Double valorCalculado = 0.0; 
		
		if(valorInicial == null) {
			valorInicial = 0L;
		}

		if(valorFinal == null) {
			valorFinal = 0L;
		}
		
		if(valorInicial == 0 && valorFinal == 0) {
			valorCalculado = 0.0;	
		} else if(valorInicial == 0 && valorFinal != 0) {
			valorCalculado = -100.0;
		} else if(valorInicial != 0 && valorFinal == 0) {
			valorCalculado = 100.0;
		} else if(valorInicial != 0 && valorFinal != 0) {
			valorCalculado = ((double) (valorInicial) / (double) (valorFinal) - 1 ) * 100;
		}
		
		BigDecimal bd = new BigDecimal(valorCalculado);
	    bd = bd.setScale(3, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static Result findDataAnual(Integer tipoRelatorio, Integer primeiroAnoParametro, Integer ultimoAnoParametro, Integer mesParametro) {
		
		List<AbicsDataRelatorioAnualExcelResponse> listResponse = new ArrayList<AbicsDataRelatorioAnualExcelResponse>();
		
		Long pesosTotalAcumuladoAnoList [] = new Long [10];
		Long receitasTotalAcumuladoAnoList [] = new Long [10];
		
		for(int f = 0; f < 10; f++) {
			pesosTotalAcumuladoAnoList[f] = 0L;
			receitasTotalAcumuladoAnoList[f] = 0L;
		}
		
		if(tipoRelatorio == TipoRelatorioAbicsDataEnum.PAIS_ANUAL.getTipo()) {
			
			List<AbicsPais> abicsPaisList = AbicsPais.findAll();
			
			for(AbicsPais paisAbics : abicsPaisList) {
				
				Pais pais = Pais.findByAbicsId(paisAbics.getId());
				
				if(pais == null) {
					continue;
				}
				
				List<Long> pesosTotalAnoList = new ArrayList<Long>();
				List<Long> receitasTotalAnoList = new ArrayList<Long>();

				for(int anoContador = ultimoAnoParametro; anoContador <= primeiroAnoParametro; anoContador++) {
					Long pesoTotalAno = 0L;
					Long receitaTotalAno = 0L;
					
					for(int mesContador = mesParametro; mesContador <= 12; mesContador++) {
						AbicsExportacaoPais abicsExportacao = AbicsExportacaoPais.findByAnoMesPaisId(anoContador, mesContador, pais.getId());
						if(abicsExportacao != null && abicsExportacao.getPeso() != null) {
							pesoTotalAno += abicsExportacao.getPeso();
						}
						
						if(abicsExportacao != null && abicsExportacao.getReceita() != null) {
							receitaTotalAno += abicsExportacao.getReceita();
						}
					}
					pesosTotalAnoList.add(pesoTotalAno);
					receitasTotalAnoList.add(receitaTotalAno);
					
				}
				
				boolean possuiValorDiferenteZero = false;
				//remove aqueles que possuem todos anos com 0
				for(int i = 0; i < pesosTotalAnoList.size(); i++) {
					
					if(pesosTotalAnoList.get(i) != 0) {
						possuiValorDiferenteZero = true;
						break;
					}
				}
				
				if(possuiValorDiferenteZero) {
					
					//retira o comparator, que seja do ano mais velho para o mais novo (2017 - 2009)
					//para que não seja comparado algum valor zerado 
					
					Long valorPesoComparator = 0L;
					int tamanhoLista = pesosTotalAnoList.size() - 1;
					
					for(int m = tamanhoLista; m > 0; m--) {
						valorPesoComparator = pesosTotalAnoList.get(m);
						if(valorPesoComparator != 0) {
							break;
						}
					}
					
					AbicsDataRelatorioAnualExcelResponse data = new AbicsDataRelatorioAnualExcelResponse(valorPesoComparator,
							pais.getNome(), String.valueOf(pesosTotalAnoList.get(0)), String.valueOf(pesosTotalAnoList.get(1)), 
							String.valueOf(pesosTotalAnoList.get(2)), String.valueOf(pesosTotalAnoList.get(3)), String.valueOf(pesosTotalAnoList.get(4)), 
							String.valueOf(pesosTotalAnoList.get(5)), String.valueOf(pesosTotalAnoList.get(6)), String.valueOf(pesosTotalAnoList.get(7)),
							String.valueOf(receitasTotalAnoList.get(0)), String.valueOf(receitasTotalAnoList.get(1)), 
							String.valueOf(receitasTotalAnoList.get(2)), String.valueOf(receitasTotalAnoList.get(3)), String.valueOf(receitasTotalAnoList.get(4)), 
							String.valueOf(receitasTotalAnoList.get(5)), String.valueOf(receitasTotalAnoList.get(6)), String.valueOf(receitasTotalAnoList.get(7)));
					listResponse.add(data);
					
					
					//Total acumulado
					for(int k = 0; k < pesosTotalAnoList.size(); k++) {
						Long pesoTotalAcumulado = pesosTotalAcumuladoAnoList[k] + pesosTotalAnoList.get(k);
						Long receitaTotalAcumulado = receitasTotalAcumuladoAnoList[k] + receitasTotalAnoList.get(k);
						
						pesosTotalAcumuladoAnoList[k] = pesoTotalAcumulado;
						receitasTotalAcumuladoAnoList[k] = receitaTotalAcumulado;
					}
				}
			}
		} else if(tipoRelatorio == TipoRelatorioAbicsDataEnum.PRODUTO_ANUAL.getTipo()
				|| tipoRelatorio == TipoRelatorioAbicsDataEnum.PAIS_NCM_21011190.getTipo()
				|| tipoRelatorio == TipoRelatorioAbicsDataEnum.PAIS_NCM_21011200.getTipo()) {
			
			
			if(tipoRelatorio == TipoRelatorioAbicsDataEnum.PRODUTO_ANUAL.getTipo()) {
				
				List<AbicsProduto> produtos = new ArrayList<AbicsProduto>();
				produtos = AbicsProduto.findAll();
			
				for(AbicsProduto produto : produtos) {
					
					List<Long> pesosTotalAnoList = new ArrayList<Long>();
					List<Long> receitasTotalAnoList = new ArrayList<Long>();
	
					for(int anoContador = ultimoAnoParametro; anoContador <= primeiroAnoParametro; anoContador++) {
						Long pesoTotalAno = 0L;
						Long receitaTotalAno = 0L;
						
						for(int mesContador = mesParametro; mesContador <= 12; mesContador++) {
							AbicsExportacaoProduto abicsExportacao = AbicsExportacaoProduto.findByAnoMesProdutoId(anoContador, mesContador, produto.getId());
							if(abicsExportacao != null && abicsExportacao.getPeso() != null) {
								pesoTotalAno += abicsExportacao.getPeso();
							}
							
							if(abicsExportacao != null && abicsExportacao.getReceita() != null) {
								receitaTotalAno += abicsExportacao.getReceita();
							}
						}
						pesosTotalAnoList.add(pesoTotalAno);
						receitasTotalAnoList.add(receitaTotalAno);
						
					}
					
					boolean possuiValorDiferenteZero = false;
					//remove aqueles que possuem todos anos com 0
					for(int i = 0; i < pesosTotalAnoList.size(); i++) {
						
						if(pesosTotalAnoList.get(i) != 0) {
							possuiValorDiferenteZero = true;
							break;
						}
					}
					
					if(possuiValorDiferenteZero) {
						
						//retira o comparator, que seja do ano mais velho para o mais novo (2017 - 2009)
						//para que não seja comparado algum valor zerado 
						
						Long valorPesoComparator = 0L;
						int tamanhoLista = pesosTotalAnoList.size() - 1;
						
						for(int m = tamanhoLista; m > 0; m--) {
							valorPesoComparator = pesosTotalAnoList.get(m);
							if(valorPesoComparator != 0) {
								break;
							}
						}
						
						AbicsDataRelatorioAnualExcelResponse data = new AbicsDataRelatorioAnualExcelResponse(valorPesoComparator,
								produto.getNome(), String.valueOf(pesosTotalAnoList.get(0)), String.valueOf(pesosTotalAnoList.get(1)), 
								String.valueOf(pesosTotalAnoList.get(2)), String.valueOf(pesosTotalAnoList.get(3)), String.valueOf(pesosTotalAnoList.get(4)), 
								String.valueOf(pesosTotalAnoList.get(5)), String.valueOf(pesosTotalAnoList.get(6)), String.valueOf(pesosTotalAnoList.get(7)),
								String.valueOf(receitasTotalAnoList.get(0)), String.valueOf(receitasTotalAnoList.get(1)), 
								String.valueOf(receitasTotalAnoList.get(2)), String.valueOf(receitasTotalAnoList.get(3)), String.valueOf(receitasTotalAnoList.get(4)), 
								String.valueOf(receitasTotalAnoList.get(5)), String.valueOf(receitasTotalAnoList.get(6)), String.valueOf(receitasTotalAnoList.get(7)));
						listResponse.add(data);
						
						
						//Total acumulado
						for(int k = 0; k < pesosTotalAnoList.size(); k++) {
							Long pesoTotalAcumulado = pesosTotalAcumuladoAnoList[k] + pesosTotalAnoList.get(k);
							Long receitaTotalAcumulado = receitasTotalAcumuladoAnoList[k] + receitasTotalAnoList.get(k);
							
							pesosTotalAcumuladoAnoList[k] = pesoTotalAcumulado;
							receitasTotalAcumuladoAnoList[k] = receitaTotalAcumulado;
						}
					}
				}
			
			} else if(tipoRelatorio == TipoRelatorioAbicsDataEnum.PAIS_NCM_21011190.getTipo()
					|| tipoRelatorio == TipoRelatorioAbicsDataEnum.PAIS_NCM_21011200.getTipo()) {
				
				List<AbicsPais> abicsPaisList = AbicsPais.findAll();
				
				for(AbicsPais paisAbics : abicsPaisList) {
					
					Pais pais = Pais.findByAbicsId(paisAbics.getId());
					
					if(pais == null) {
						continue;
					}
					
					List<Double> pesosTotalAnoList = new ArrayList<Double>();
					List<Double> receitasTotalAnoList = new ArrayList<Double>();

					for(int anoContador = ultimoAnoParametro; anoContador <= primeiroAnoParametro; anoContador++) {
						Double pesoTotalAno = 0.0;
						Double receitaTotalAno = 0.0;
						
						if(tipoRelatorio == TipoRelatorioAbicsDataEnum.PAIS_NCM_21011190.getTipo()) {

							AbicsProduto1190 abicsExportacao = AbicsProduto1190.findByAnoPaisId(anoContador, pais.getId());
							if(abicsExportacao != null && abicsExportacao.getPeso() != null && !abicsExportacao.getPeso().isEmpty()) {
								String temp = abicsExportacao.getPeso().replace(",", "");
								pesoTotalAno += Double.valueOf(temp);
							}
							
							if(abicsExportacao != null && abicsExportacao.getReceita() != null && !abicsExportacao.getReceita().isEmpty()) {
								String temp = abicsExportacao.getReceita().replace(",", "");
								receitaTotalAno += Double.valueOf(temp);
							}

						} else if(tipoRelatorio == TipoRelatorioAbicsDataEnum.PAIS_NCM_21011200.getTipo()) {
							
							AbicsProduto1200 abicsExportacao = AbicsProduto1200.findByAnoPaisId(anoContador, pais.getId());
							if(abicsExportacao != null && abicsExportacao.getPeso() != null && !abicsExportacao.getPeso().isEmpty()) {
								String temp = abicsExportacao.getPeso().replace(",", "");
								pesoTotalAno += Double.valueOf(temp);
							}
							
							if(abicsExportacao != null && abicsExportacao.getReceita() != null && !abicsExportacao.getReceita().isEmpty()) {
								String temp = abicsExportacao.getReceita().replace(",", "");
								receitaTotalAno += Double.valueOf(temp);
							}
						}
							
						pesosTotalAnoList.add(pesoTotalAno);
						receitasTotalAnoList.add(receitaTotalAno);
						
					}
					
					boolean possuiValorDiferenteZero = false;
					//remove aqueles que possuem todos anos com 0
					for(int i = 0; i < pesosTotalAnoList.size(); i++) {
						
						if(pesosTotalAnoList.get(i) != 0) {
							possuiValorDiferenteZero = true;
							break;
						}
					}
					
					if(possuiValorDiferenteZero) {
						
						//retira o comparator, que seja do ano mais velho para o mais novo (2017 - 2009)
						//para que não seja comparado algum valor zerado 
						
						Double valorPesoComparator = 0.0;
						int tamanhoLista = pesosTotalAnoList.size() - 1;
						
						for(int m = tamanhoLista; m > 0; m--) {
							valorPesoComparator = pesosTotalAnoList.get(m);
							if(valorPesoComparator != 0) {
								break;
							}
						}
						
						String valorPesoComparatorString = String.valueOf(valorPesoComparator);
						valorPesoComparatorString = valorPesoComparatorString.replace(".", "");
						Long valorPesoComparatorLong = Long.valueOf(valorPesoComparatorString);
						
						AbicsDataRelatorioAnualExcelResponse data = new AbicsDataRelatorioAnualExcelResponse(valorPesoComparatorLong,
								pais.getNome(), String.valueOf(pesosTotalAnoList.get(0)), String.valueOf(pesosTotalAnoList.get(1)), 
								String.valueOf(pesosTotalAnoList.get(2)), String.valueOf(pesosTotalAnoList.get(3)), String.valueOf(pesosTotalAnoList.get(4)), 
								String.valueOf(pesosTotalAnoList.get(5)), String.valueOf(pesosTotalAnoList.get(6)), String.valueOf(pesosTotalAnoList.get(7)),
								String.valueOf(receitasTotalAnoList.get(0)), String.valueOf(receitasTotalAnoList.get(1)), 
								String.valueOf(receitasTotalAnoList.get(2)), String.valueOf(receitasTotalAnoList.get(3)), String.valueOf(receitasTotalAnoList.get(4)), 
								String.valueOf(receitasTotalAnoList.get(5)), String.valueOf(receitasTotalAnoList.get(6)), String.valueOf(receitasTotalAnoList.get(7)));
						listResponse.add(data);
						
						
						//Total acumulado
						for(int k = 0; k < pesosTotalAnoList.size(); k++) {
							Double pesoTotalAcumulado = pesosTotalAcumuladoAnoList[k] + pesosTotalAnoList.get(k);
							Double receitaTotalAcumulado = receitasTotalAcumuladoAnoList[k] + receitasTotalAnoList.get(k);
							
							pesosTotalAcumuladoAnoList[k] = pesoTotalAcumulado.longValue();
							receitasTotalAcumuladoAnoList[k] = receitaTotalAcumulado.longValue();
						}
					}
				}
				
			}
		}
		
		listResponse.sort(Comparator.comparing(AbicsDataRelatorioAnualExcelResponse::getPesoComparator));
		Collections.reverse(listResponse);
		
		AbicsDataRelatorioAnualExcelResponse dataTotalAcumulado = new AbicsDataRelatorioAnualExcelResponse(-999L,
				"Total Acumulado", String.valueOf(pesosTotalAcumuladoAnoList[0]), String.valueOf(pesosTotalAcumuladoAnoList[1]), 
				String.valueOf(pesosTotalAcumuladoAnoList[2]), String.valueOf(pesosTotalAcumuladoAnoList[3]), String.valueOf(pesosTotalAcumuladoAnoList[4]), 
				String.valueOf(pesosTotalAcumuladoAnoList[5]), String.valueOf(pesosTotalAcumuladoAnoList[6]), String.valueOf(pesosTotalAcumuladoAnoList[7]),
				String.valueOf(receitasTotalAcumuladoAnoList[0]), String.valueOf(receitasTotalAcumuladoAnoList[1]), 
				String.valueOf(receitasTotalAcumuladoAnoList[2]), String.valueOf(receitasTotalAcumuladoAnoList[3]), String.valueOf(receitasTotalAcumuladoAnoList[4]), 
				String.valueOf(receitasTotalAcumuladoAnoList[5]), String.valueOf(receitasTotalAcumuladoAnoList[6]), String.valueOf(receitasTotalAcumuladoAnoList[7]));
		listResponse.add(dataTotalAcumulado);
		
		for(AbicsDataRelatorioAnualExcelResponse response : listResponse) {
			
			if(response.getPesoAno1().equals("0")) {
				response.setPesoAno1("-");
			}
			if(response.getPesoAno2().equals("0")) {
				response.setPesoAno2("-");
			}
			if(response.getPesoAno3().equals("0")) {
				response.setPesoAno3("-");
			}
			if(response.getPesoAno4().equals("0")) {
				response.setPesoAno4("-");
			}
			if(response.getPesoAno5().equals("0")) {
				response.setPesoAno5("-");
			}
			if(response.getPesoAno6().equals("0")) {
				response.setPesoAno6("-");
			}
			if(response.getPesoAno7().equals("0")) {
				response.setPesoAno7("-");
			}
			if(response.getPesoAno8().equals("0")) {
				response.setPesoAno8("-");
			}
		}
		
		return ok(Json.toJson(listResponse));
	}
	
}
