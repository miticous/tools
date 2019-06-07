package br.com.suportecpl.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.suportecpl.model.Grupo;

import javax.persistence.Entity;

public class GrupoPU implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Grupo grupoPorNome(String nome) {
		return manager.createQuery("from Grupo where descricao = :descricao", Grupo.class)
				.setParameter("descricao", nome).getSingleResult();
	}

	public Grupo gravarGrupo(Grupo grupo) {
		manager.getTransaction().begin();
		grupo = manager.merge(grupo);
		manager.getTransaction().commit();
		return grupo;
	}
	
	public List<Grupo> todosGrupos(){
		return manager.createQuery("from Grupo", Grupo.class).getResultList();
	}
}
