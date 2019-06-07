package br.com.suportecpl.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.w3c.dom.Document;

import br.com.suportecpl.model.Empresa;
import br.com.suportecpl.model.Estoque;
import br.com.suportecpl.model.Ferramenta;
import br.com.suportecpl.model.TipoObrigatoriedade;
import br.com.suportecpl.repository.EstoquePU;
import br.com.suportecpl.repository.FerramentaPU;

public class FerramentaService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private FerramentaPU ferramentaPU;
	@Inject
	private EstoquePU estPU;
	@Inject
	private UsuarioService usuarioService;

	public void inserirFerramenta(Ferramenta ferramenta) {
		try {
			ferramentaPU.gravarFerramenta(ferramenta);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// VERIFICA SE JA EXISTE ANTES DE INSERIR
	public void inserirFerramentaComVerificacao(Ferramenta ferramenta) throws Exception {
		try {
			if (verificaCodigoNhExistente(ferramenta.getCodigoNh())) {
				ferramentaPU.gravarFerramenta(ferramenta);
			} else
				throw new Exception("Ferramenta ja cadastrada");
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("Ferramenta ja cadastrada");
		}

	}

	public List<Ferramenta> buscaFerramentaPorEmp(Long codEmp) {
		List<Ferramenta> ferramentas = new ArrayList<>();
		for (Estoque e : estPU.estoquePorEmpresa(codEmp)) {
			ferramentas.add(e.getFerramenta());
		}
		return ferramentas;
	}

	public boolean verificaCodigoNhExistente(String codigoNh) {
		try {
			Ferramenta ferramenta = new Ferramenta();
			ferramenta = ferramentaPU.ferramentaPorCodigoNh(codigoNh);
			if (ferramenta != null) {
				return false;
			} else
				return true;
		} catch (NoResultException e) {
			// TODO: handle exception
			return true;
		}
	}

	// Verifica se ja existe estoque para a ferramenta na empresa escolhida
	public void verificaEstoqueFerramentaExistente(Ferramenta ferramenta, Long codEmp) throws Exception {
		boolean igual = false;
		ferramenta = buscaFerramentaPorCodigoNh(ferramenta.getCodigoNh());
		for (Estoque e : ferramenta.getEstoques()) {
			if (e.getEmpresa().getCodigo() == codEmp) {
				igual = true;
			}
		}
		if (igual) {
			throw new Exception("Estoque já cadastrado para essa empresa");
		}
	}

	public Ferramenta buscaFerramentaPorCodigoNh(String codigoNh) {
			return ferramentaPU.ferramentaPorCodigoNh(codigoNh);
	}

	public Ferramenta buscaFerramentaPorId(Long idFerramenta) {
		try {
			return ferramentaPU.ferramentaPorId(idFerramenta);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public List<Ferramenta> todasFerramentas() {
		return ferramentaPU.todasFerramentas();
	}

	public void removerFerramenta(Ferramenta ferramenta) throws Exception {
		try {
			ferramentaPU.removerFerramenta(ferramenta);
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("Não foi possível excluir ferramenta. Motivo: " + e.getMessage());
		}
	}

	public List<Ferramenta> filtrarObrigatoriosSemEstoque() {
		return ferramentaPU.ferramentasObrigatoriasSemEstoque();
	}
	
}
