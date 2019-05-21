package entitys.report;

import java.util.List;

import models.Item;

public class CategoriaReport {

	private String data;
	
	private String nome;
	
	private String valor;

	private String valorPorcentagem;
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getValorPorcentagem() {
		return valorPorcentagem;
	}

	public void setValorPorcentagem(String valorPorcentagem) {
		this.valorPorcentagem = valorPorcentagem;
	}
	
}