package br.com.suportecpl.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.suportecpl.model.Empresa;

public class EmpresaPU implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Empresa> todasEmpresas() {
		return manager.createQuery("from Empresa", Empresa.class).getResultList();
	}

	public void gravarEmpresa(Empresa empresa) {
		manager.getTransaction().begin();
		manager.merge(empresa);
		manager.getTransaction().commit();
	}

	public Empresa empresaPorId(Long id) {
		return manager.find(Empresa.class, id);
	}

	public Empresa empresaPorCnpj(String cnpj) {
		return manager.createQuery("from Empresa where cnpj = :cnpj", Empresa.class).setParameter("cnpj", cnpj)
				.getSingleResult();
	}
}
