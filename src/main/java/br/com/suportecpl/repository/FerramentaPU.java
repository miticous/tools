package br.com.suportecpl.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.management.Query;
import javax.persistence.EntityManager;

import br.com.suportecpl.model.Empresa;
import br.com.suportecpl.model.Ferramenta;

public class FerramentaPU implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Ferramenta ferramentaPorId(Long id) {
		return manager.find(Ferramenta.class, id);
	}

	public Ferramenta ferramentaPorCodigoNh(String codNh) {
		return manager.createQuery("from Ferramenta where codigo_nh = :codNh", Ferramenta.class)
				.setParameter("codNh", codNh).getSingleResult();
	}

	public void gravarFerramenta(Ferramenta ferramenta) {
		manager.getTransaction().begin();
		manager.merge(ferramenta);
		manager.getTransaction().commit();
	}

	public List<Ferramenta> todasFerramentas() {
		return manager.createQuery("from Ferramenta", Ferramenta.class).getResultList();
	}

	public List<Ferramenta> buscaFerramentaPorEmpDescCodigo(String parametro, Long codEmp) {
		return manager
				.createQuery(
						"select ferramenta from Ferramenta ferramenta join ferramenta.estoques estoque where estoque.empresa.codigo = :codEmp and (ferramenta.descricao LIKE concat('%', :parametro,'%') OR ferramenta.codigoNh LIKE concat('%', :parametro,'%'))",
						Ferramenta.class)
				.setParameter("codEmp", codEmp).setParameter("parametro", parametro).getResultList();
	}

	public void removerFerramenta(Ferramenta ferramenta) {
		try {
			manager.getTransaction().begin();
			manager.createNativeQuery("delete from estoque where ferramenta_codigo = " + ferramenta.getCodigo())
					.executeUpdate();
			manager.createNativeQuery("delete from ferramenta where codigo = " + ferramenta.getCodigo())
					.executeUpdate();
			manager.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public List<Ferramenta> ferramentasObrigatoriasSemEstoque() {
		String sql = "select f.* from ferramenta f where f.obrigatorio = 0 and (f.codigo not in (select ferramenta_codigo from estoque where empresa_codigo = 2)) or f.obrigatorio = 1 and (f.codigo not in (select ferramenta_codigo from estoque where empresa_codigo = 2) or f.codigo not in (select ferramenta_codigo from estoque where empresa_codigo = 3) or f.codigo not in (select ferramenta_codigo from estoque where empresa_codigo = 4) or f.codigo not in (select ferramenta_codigo from estoque where empresa_codigo = 5) or f.codigo not in (select ferramenta_codigo from estoque where empresa_codigo = 6)) order by f.codigo desc";

		return manager.createNativeQuery(sql, Ferramenta.class).getResultList();
	}

}
