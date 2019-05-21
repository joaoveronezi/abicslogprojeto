package entitys.response;

import java.util.List;

import models.ComtradeDataSumarizacao;

public class ComtradeDataSumarizacaoResponse {
	
	private String ptCode;
	
	private String ptTitle;
	
	private List<ComtradeDataSumarizacao> comtradeDataSumarizacaoList;

	public String getPtCode() {
		return ptCode;
	}

	public void setPtCode(String ptCode) {
		this.ptCode = ptCode;
	}

	public String getPtTitle() {
		return ptTitle;
	}

	public void setPtTitle(String ptTitle) {
		this.ptTitle = ptTitle;
	}

	public List<ComtradeDataSumarizacao> getComtradeDataSumarizacaoList() {
		return comtradeDataSumarizacaoList;
	}

	public void setComtradeDataSumarizacaoList(
			List<ComtradeDataSumarizacao> comtradeDataSumarizacaoList) {
		this.comtradeDataSumarizacaoList = comtradeDataSumarizacaoList;
	}
}
