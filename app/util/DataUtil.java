package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * 
 * Classe utilitaria para manipulacao de datas
 * 
 * **/
public class DataUtil {

	/**
	 * YYYY-M-dd
	 * **/
	public static final SimpleDateFormat sdfYearMonthDay = new SimpleDateFormat("YYYY-M-dd");
	
	private static final ZoneId fusoHorarioDeSaoPaulo = ZoneId.of("America/Sao_Paulo");
	private static final Locale localeBrasil = new Locale("pt", "br");
	public static final SimpleDateFormat sdfDataBR = new SimpleDateFormat("dd/MM/yy");
	public static final SimpleDateFormat sdfDataBRWithDot = new SimpleDateFormat("dd.MM.yy");
	
	
	public static final SimpleDateFormat sdfDataBRCompleto = new SimpleDateFormat("dd MMM yy HH:mm");
	
	public static final SimpleDateFormat sdfFolder = new SimpleDateFormat("dd_MM_yyyy"); 

	/**
	 * Retorna as datas entre a data inicial e a data final. O parametro
	 * includeFinalDate serve para incluir a data final
	 * 
	 * @param startdate
	 *            - dataInicial
	 * @param enddate
	 *            - dataFinal
	 * @param includeFinalDate
	 *            - incluir ou não a data final na lista de datas
	 * @return Lista de data incluindo, ou não, a data final
	 * 
	 * **/
	public static List<Date> getDaysBetweenDatesIncludeFinalDate(Date startdate, Date enddate, boolean includeFinalDate) {
		List<Date> dates = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startdate);

		while (calendar.getTime().before(enddate)) {
			Date result = calendar.getTime();
			dates.add(result);
			calendar.add(Calendar.DATE, 1);
		}

		if (includeFinalDate) {

			Date result = calendar.getTime();
			dates.add(result);
		}

		return dates;
	}

	public static List<Date> getFirstWeekDayBetweenDatesIncludeFinalDate(Date startdate, Date enddate, boolean includeFinalDate) {
		List<Date> dates = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startdate);

		while (calendar.getTime().before(enddate)) {
			Date result = calendar.getTime();
			
			if(!result.after(enddate)) {
				dates.add(result);
			}
			calendar.add(Calendar.DATE, 7);
		}

		if (includeFinalDate) {

			Date result = calendar.getTime();
			dates.add(result);
		}

		return dates;
	}
	
	/**
	 * Retorna a data no SimpleDateFormat dd/MM/yyyy
	 * 
	 * @param data
	 *            no formato
	 * @return data no formato BR dd/MM/yyyy
	 * 
	 * **/
	public static String formatDateToBR(Date date) {

		return sdfDataBR.format(date).toString();
	}

	/**
	 * Retorna a semana do ano de determinada data;
	 * 
	 * @param data
	 * @return semana do ano
	 * 
	 * **/
	public static int getWeekFromDate(Date data) {

		LocalDate dateN = data.toInstant().atZone(fusoHorarioDeSaoPaulo)
				.toLocalDate();
		TemporalField woy = WeekFields.of(localeBrasil).weekOfWeekBasedYear();

		int weeks = dateN.get(woy);
		return weeks;
	}

	/**
	 * 
	 * Transforma uma data em unixtime
	 * 
	 * @return date.getTime() / 1000;
	 * **/
	public static Long dateToUnixtime(Date date) {
		return date.getTime() / 1000;
	}

	/**
	 * @return new Date().getTime() / 1000;
	 * **/
	public static Long nowUnixtimeWithoutLocale() {
		return new Date().getTime() / 1000;
	}

	/**
	 * @return new Date(unixTime*1000);
	 * **/
	public static Date fromUnixTimeSemLocale(Long unixTime) {

		Date hoje = new Date(unixTime * 1000);
		return hoje;
	}
}