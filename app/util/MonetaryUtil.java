package util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

//import javafx.scene.transform.Scale;

//import org.joda.money.Money;

/**
 * Contem as operacoes monetarias
 * Utilizando BigDecimal com precision de 0,2
 * 
 * **/
public class MonetaryUtil {

	public static BigDecimal multiply(BigDecimal valor1, BigDecimal valor2) {
		
		valor1 = valor1.setScale(3 ,RoundingMode.HALF_UP);
		valor2 = valor2.setScale(3 ,RoundingMode.HALF_UP);
		
		BigDecimal retorno = valor1.multiply(valor2);
		return retorno.setScale(3 ,RoundingMode.HALF_UP);
	}
	
	public static BigDecimal add(BigDecimal valor1, BigDecimal valor2) {
		return valor1.add(valor2);
	}
	
	public static BigDecimal add(String valor1, String valor2) {
		return new BigDecimal(valor1).add(new BigDecimal(valor2));
	}
	
	public static BigDecimal subtract(BigDecimal valor1, BigDecimal valor2) {
		return valor1.subtract(valor2);
	}
	
	public static BigDecimal divide(BigDecimal valor1, BigDecimal valor2) {
		return valor1.divide(valor2, 3, RoundingMode.HALF_UP);
	}
	
}