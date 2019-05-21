package entitys.response;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class AbstractResponse {
	
	public enum IconEnum {

		THUMBS_UP("thumbs-up"),
		WARNING_SIGN("warning-sign"),
		REMOVE("remove");
		
		private String descricao;
		
		private IconEnum(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}
	
	public enum TypeEnum {

		DANGER("danger"),
		WARNING("warning"),
		SUCCESS("success");
		
		private String descricao;
		
		private TypeEnum(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}
	
	public AbstractResponse(String icon, String type, String message) {
		
		this.icon = icon;
		this.type = type;
		this.message = message;
	}
	
	private String icon;
	
	private String type;
	
	private String message;

	public String getType() {
		return type;
	}

//	public void setType(String type) {
//		this.type = type;
//	}

	public String getMessage() {
		return message;
	}

//	public void setMessage(String message) {
//		this.message = message;
//	}

	public String getIcon() {
		return icon;
	}

//	public void setIcon(String icon) {
//		this.icon = icon;
//	}
	
}
