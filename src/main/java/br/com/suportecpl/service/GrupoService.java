package br.com.suportecpl.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import br.com.suportecpl.model.Grupo;
import br.com.suportecpl.repository.GrupoPU;

public class GrupoService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private GrupoPU grupoPU;

	public Grupo inserirGrupo(Grupo grupo) {
		return grupoPU.gravarGrupo(grupo);
	}
	
	public List<Grupo> listarGrupos(){
		return grupoPU.todosGrupos();
	}
}
