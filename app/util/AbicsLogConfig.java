package util;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * 
 * Configuration class. Load the abicslog.properties onstartup
 * Contains the keys for read the properties from the file
 * Contains the cookies names 
 * For all the application 
 * 
 * **/
public class AbicsLogConfig {

	/**configuration file**/
	private static Config CONFIGURATION_FILE = ConfigFactory.load("abicslog.properties");
	
	public static String USER_REPORT_FOLDER="USER_REPORT_FOLDER";
//	public static String APLICACAO_REPORT_FOLDER="APLICACAO_REPORT_FOLDER";
	
	
	
	
	
	
	/**Chaves mapeadas no arquivo abicslog.properties**/
	public static String REPORT_FOLDER="REPORT_FOLDER";
	public static String ABICS_DATA_FOLDER="ABICS_DATA_FOLDER";
	public static String LOGO_ABICS="LOGO_ABICS";
	public static String LOGO_ABICS_PNG="LOGO_ABICS_PNG";

//	public static String IMAGES_FILE_TEMP_CAMBIO="IMAGES_FILE_TEMP_CAMBIO";
//	public static String IMAGES_FILE_TEMP_VALOR_MERCADORIA="IMAGES_FILE_TEMP_VALOR_MERCADORIA";
//	public static String FILE_PDF_TEMP="FILE_PDF_TEMP";
	public static String EMAIL_FOLDER="EMAIL_FOLDER";
//	public static String FOLDER_VIDEOS="FOLDER_VIDEOS";
//	public static String FOLDER_TEMP_IMAGES_REPORT="FOLDER_TEMP_IMAGES_REPORT";
	//public static String FILE_PDF_VALOR_TOTAL_IMPORTACAO="FILE_PDF_VALOR_TOTAL_IMPORTACAO";
	//public static String FILE_PDF_CAMBIO_VALOR_MERCADORIA="FILE_PDF_CAMBIO_VALOR_MERCADORIA";
//	public static String FOLDER_TEMP_DAILY_REPORTS="FOLDER_TEMP_DAILY_REPORTS";
	
	public static String COOKIE_USUARIO_ID = "___ID";

	/**
	 * Retorna o valor da @param key do arquivo abicslog.properties
	 * 
	 * **/
	public static String getString(String key) {
	
		return CONFIGURATION_FILE.getString(key);
	}
}
