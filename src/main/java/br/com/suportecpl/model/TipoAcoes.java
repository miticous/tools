package br.com.suportecpl.model;

public enum TipoAcoes {
	ADICIONAR_FERRAMENTA("Adicionar Ferramenta"),
	ADICIONAR_ESTOQUE("Adicionar Estoque"),
	ADICIONAR_LOCACAO("Adicionar Locação"),
	REMOVER_FERRAMENTA("Remover Ferramenta"),
	REMOVER_LOCACAO("Remover Locação"),
	EDITAR_FERRAMENTA("Editar Ferramenta"),
	EDITAR_ESTOQUE("Editar Estoque"),
	ADICIONAR_ESTOQUE_XML("Adicionar estoque XML");
	
	
	private String descricao;
	
	TipoAcoes(String descricao){
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
