package br.com.suportecpl.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.suportecpl.model.Locacao;
import br.com.suportecpl.repository.LocacaoPU;

public class LocacaoService implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private LocacaoPU locacaoPU;
	
	public void adicionarLocacao(Locacao locacao){
		locacaoPU.salvarNovaLocacao(locacao);
	}
	public void removerLocacao(Locacao locacao) throws Exception{
		try{
			locacaoPU.excluirLocacao(locacao);
		}catch (Exception e) {
			// TODO: handle exception
			throw new Exception("Não foi possivel remover locação. Existe estoque da ferramenta cadastrada.");
		}
	}
	
	public Locacao locacaoPorEmpECod(String cnpj, String codigo){
		return locacaoPU.locacaoPorEmpECod(cnpj, codigo);
	}
}
