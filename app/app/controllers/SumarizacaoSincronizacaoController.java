package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Cambio;
import models.Cotacao;
import models.RelValorMercadoriaCotacao;
import models.SumarizacaoSincronizacao;
import play.libs.Json;
import play.mvc.Result;
import entitys.response.SumarizacaoSincronizacaoResponse;

public class SumarizacaoSincronizacaoController extends AbstractController {

	public static Result findAll() {

    	Calendar calYesterday = Calendar.getInstance();
		calYesterday.setTime(new Date());
        calYesterday.set(Calendar.HOUR_OF_DAY, 0);
        calYesterday.set(Calendar.MINUTE, 0);
        calYesterday.set(Calendar.SECOND, 0);
        calYesterday.set(Calendar.MILLISECOND, 0);
        calYesterday.add(Calendar.DATE, -1);
        Long dataOntem = calYesterday.getTimeInMillis() / 1000;

		List<SumarizacaoSincronizacao> list =  SumarizacaoSincronizacao.findAllUltimaSincronizacao();// findAllByData(data);
		List<SumarizacaoSincronizacaoResponse> responseList = new ArrayList<SumarizacaoSincronizacaoResponse>();

		for(SumarizacaoSincronizacao ss : list) {

			int status = 0;
			if(ss.getData().equals(dataOntem)) {
				status = 1;
			}

			SumarizacaoSincronizacaoResponse ssResponse = null;

			if(ss.getCambioId() != null) {

				Cambio cambio = Cambio.findById(ss.getCambioId());
				ssResponse = new SumarizacaoSincronizacaoResponse(ss.getData(), status, cambio, null, null, null, null);
			} else if(ss.getRelValorMercadoriaCotacaoId() != null) {

				RelValorMercadoriaCotacao rel = RelValorMercadoriaCotacao.findById(ss.getRelValorMercadoriaCotacaoId());
				Cambio cambio = Cambio.findByData(rel.getData());
				Cotacao cotacao = Cotacao.findById(rel.getCotacaoId());
				ssResponse = new SumarizacaoSincronizacaoResponse(ss.getData(), status, null, rel, cotacao, cambio.getValorEuro(), cambio.getValorDolar());
			}
			responseList.add(ssResponse);
		}

		return ok(Json.toJson(responseList));
	}

}
