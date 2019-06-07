package br.com.suportecpl.repository;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.suportecpl.model.Locacao;

public class LocacaoPU implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public void salvarNovaLocacao(Locacao locacao) {
		manager.getTransaction().begin();
		manager.merge(locacao);
		manager.getTransaction().commit();
	}

	public void excluirLocacao(Locacao locacao) {
		locacao = manager.find(Locacao.class, locacao.getCodigo());
		manager.getTransaction().begin();
		manager.remove(locacao);
		manager.getTransaction().commit();
	}

	public Locacao locacaoPorEmpECod(String cnpj, String codigo) {
		return manager.createQuery("from Locacao where codigoLoc = :codigo and empresa.cnpj = :cnpj", Locacao.class)
				.setParameter("codigo", codigo).setParameter("cnpj", cnpj).getSingleResult();
	}
}
