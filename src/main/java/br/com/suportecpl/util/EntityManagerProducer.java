package br.com.suportecpl.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class EntityManagerProducer {
	
	private EntityManagerFactory siteFactory;
	
	public EntityManagerProducer() {
		// TODO Auto-generated constructor stub
		this.siteFactory = Persistence.createEntityManagerFactory("EM_CONN_SUPORTECPL");
	}

	@RequestScoped
	@Produces
	@Default
	public EntityManager createSiteEntityManager() {
		return this.siteFactory.createEntityManager();
	}
	

	public void closeEntityManager(@Disposes EntityManager manager) {
		if (manager.isOpen())
			manager.close();
	}
}
