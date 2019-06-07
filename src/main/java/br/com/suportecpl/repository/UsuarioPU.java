package br.com.suportecpl.repository;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Id;

import br.com.suportecpl.model.Usuario;

public class UsuarioPU implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Usuario usuarioPorId(Long id) {
		return manager.find(Usuario.class, id);
	}

	public Usuario usuarioPorUserName(String username) {
		return manager.createQuery("from Usuario where username = :username", Usuario.class)
				.setParameter("username", username).getSingleResult();
	}
}
