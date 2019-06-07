package br.com.suportecpl.model;

public enum TipoObrigatoriedade {
	MATRIZ("Matriz", "0"),
	MATRIZEFILIAL("Matriz e Filial", "1"),
	RECOMENDADO("Recomendável", "2");
	
	private String descricao;
	private String codigoServico;
	
	TipoObrigatoriedade(String descricao, String codigoServico){
		this.descricao = descricao;
		this.codigoServico = codigoServico;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCodigoServico() {
		return codigoServico;
	}

	public void setCodigoServico(String codigoServico) {
		this.codigoServico = codigoServico;
	}
}
