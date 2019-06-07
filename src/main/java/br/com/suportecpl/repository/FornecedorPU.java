package br.com.suportecpl.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.suportecpl.model.Fornecedor;

public class FornecedorPU implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public void salvarFornecedor(Fornecedor fornecedor) {
		manager.getTransaction().begin();
		manager.merge(fornecedor);
		manager.getTransaction().commit();
	}

	public Fornecedor fornecedorPorId(Long id) {
		return manager.find(Fornecedor.class, id);
	}

	public Fornecedor fornecedorPorCnpj(String cnpj) {
		return manager.createQuery("from Fornecedor where cnpj = :cnpj", Fornecedor.class).setParameter("cnpj", cnpj)
				.getSingleResult();
	}

	public List<Fornecedor> todosFornecedores() {
		return manager.createQuery("from Fornecedor", Fornecedor.class).getResultList();
	}
}
