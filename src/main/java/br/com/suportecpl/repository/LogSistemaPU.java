package br.com.suportecpl.repository;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.suportecpl.model.LogSistema;

public class LogSistemaPU implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public void salvarLogSistema(LogSistema logSistema){
		manager.getTransaction().begin();
		manager.merge(logSistema);
		manager.getTransaction().commit();
	}
}
