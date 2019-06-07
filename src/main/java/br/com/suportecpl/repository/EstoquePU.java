package br.com.suportecpl.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.suportecpl.model.Empresa;
import br.com.suportecpl.model.Estoque;

public class EstoquePU implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Estoque> todosEstoques() {
		return manager.createQuery("from Estoque", Estoque.class).getResultList();
	}

	public Estoque estoquePorId(Long codEst) {
		return manager.find(Estoque.class, codEst);
	}

	public void salvarEstoque(Estoque estoque) {
		manager.getTransaction().begin();
		manager.merge(estoque);
		manager.getTransaction().commit();
	}

	public List<Estoque> estoquePorEmpresa(Long empId) {
		return manager.createQuery("from Estoque where empresa.codigo =  :empId", Estoque.class)
				.setParameter("empId", empId).getResultList();
	}

	public Estoque estoquePorEmpECod(String cnpj, String cod) {
		return manager
				.createQuery("from Estoque where empresa.cnpj = :emp and ferramenta.codigoNh = :cod", Estoque.class)
				.setParameter("emp", cnpj).setParameter("cod", cod).getSingleResult();
	}

	public List<Estoque> estoquePorNfNum(Long nf) {
		return manager.createQuery("from Estoque where ultimaNf =  :nf", Estoque.class).setParameter("nf", nf)
				.getResultList();
	}

}
