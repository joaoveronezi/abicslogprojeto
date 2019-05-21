package util;

import java.security.MessageDigest;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;

/**
 * 
 * Classe security 
 * 
 * */
public class SecurityUtil {

    public static String toMD5(String password)  {
        try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(password.getBytes());
	        byte byteData[] = md.digest();
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        return sb.toString();
	        } catch(Exception e) {
	                return null;
	    }
	}

    public static void main(String[] args) throws Exception {
	    String password = "XXXXXX"; 
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(password.getBytes());
	    byte byteData[] = md.digest();
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < byteData.length; i++) {
	     sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	    }
	         System.out.println(sb);
	}
}