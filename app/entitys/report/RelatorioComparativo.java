package entitys.report;

import java.awt.Image;


public class RelatorioComparativo {

	private String cotacaoNome;
	
	private String valorCIF;
	
	private String valorCIFDolar;
	
	private String valorCIFEuro;
	
	private String valorCIFTonelada;
	
	private String valorCIFDolarTonelada;
	
	private String valorCIFEuroTonelada;

	private String caminhoFlag;
	
	private Image caminhoFlagImage;
	
	public String getCotacaoNome() {
		return cotacaoNome;
	}

	public void setCotacaoNome(String cotacaoNome) {
		this.cotacaoNome = cotacaoNome;
	}

	public String getValorCIF() {
		return valorCIF;
	}

	public void setValorCIF(String valorCIF) {
		this.valorCIF = valorCIF;
	}

	public String getValorCIFDolar() {
		return valorCIFDolar;
	}

	public void setValorCIFDolar(String valorCIFDolar) {
		this.valorCIFDolar = valorCIFDolar;
	}

	public String getValorCIFEuro() {
		return valorCIFEuro;
	}

	public void setValorCIFEuro(String valorCIFEuro) {
		this.valorCIFEuro = valorCIFEuro;
	}

	public String getValorCIFTonelada() {
		return valorCIFTonelada;
	}

	public void setValorCIFTonelada(String valorCIFTonelada) {
		this.valorCIFTonelada = valorCIFTonelada;
	}

	public String getValorCIFDolarTonelada() {
		return valorCIFDolarTonelada;
	}

	public void setValorCIFDolarTonelada(String valorCIFDolarTonelada) {
		this.valorCIFDolarTonelada = valorCIFDolarTonelada;
	}

	public String getValorCIFEuroTonelada() {
		return valorCIFEuroTonelada;
	}

	public void setValorCIFEuroTonelada(String valorCIFEuroTonelada) {
		this.valorCIFEuroTonelada = valorCIFEuroTonelada;
	}

	public String getCaminhoFlag() {
		return caminhoFlag;
	}

	public void setCaminhoFlag(String caminhoFlag) {
		this.caminhoFlag = caminhoFlag;
	}
	
	public Image getCaminhoFlagImage() {
		return caminhoFlagImage;
	}

	public void setCaminhoFlagImage(Image caminhoFlagImage) {
		this.caminhoFlagImage = caminhoFlagImage;
	}
}