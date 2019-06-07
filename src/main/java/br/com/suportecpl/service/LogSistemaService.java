package br.com.suportecpl.service;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.suportecpl.model.Estoque;
import br.com.suportecpl.model.Ferramenta;
import br.com.suportecpl.model.Locacao;
import br.com.suportecpl.model.LogSistema;
import br.com.suportecpl.model.TipoAcoes;
import br.com.suportecpl.repository.LogSistemaPU;

public class LogSistemaService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private FerramentaService feService;
	@Inject
	private EstoqueService esService;
	@Inject
	private LogSistemaPU logSistemaPU;

	private Object objetoOld;
	private Long codigo = null;

	public void salvarLog(LogSistema log) {
		logSistemaPU.salvarLogSistema(log);
	}

	public String gerarAcao(Object objeto, TipoAcoes tipoAcoes)
			throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		codigo = null;
		objetoOld = new Object();
		BeanInfo info = null;
		switch (tipoAcoes) {
		case EDITAR_FERRAMENTA:
			info = Introspector.getBeanInfo(Ferramenta.class);
			obterCodigoObjeto(objeto, info);
			objetoOld = feService.buscaFerramentaPorId(codigo);
			return "//A ferramenta código " + codigo + " foi alterada.\n #DADOS_ALTERADOS#\n"
					+ buscarModificacoes(objeto, info);
		case EDITAR_ESTOQUE:
			info = Introspector.getBeanInfo(Estoque.class);
			obterCodigoObjeto(objeto, info);
			objetoOld = esService.estoquePorId(codigo);
			return "//O estoque código " + codigo + " foi alterada.\n #DADOS_ALTERADOS#\n"
					+ buscarModificacoes(objeto, info);
		case ADICIONAR_FERRAMENTA:
			Ferramenta fer2 = Ferramenta.class.cast(objeto);
			return "++++ Nova ferramenta adicionada. Código: " + fer2.getCodigoNh();
		case ADICIONAR_ESTOQUE:
			Estoque est = Estoque.class.cast(objeto);
			return "++++ Novo estoque adicionado á Ferramenta " + est.getFerramenta().getCodigoNh()
					+ ". Quantidade: " + est.getQuantidade() + " || Empresa: " + est.getEmpresa().getDescricao();
		case ADICIONAR_LOCACAO:
			Locacao loc = Locacao.class.cast(objeto);
			return "++++ Nova locação criada. Código: " + loc.getCodigoLoc() + " || Empresa: " + loc.getEmpresa().getDescricao();
		case REMOVER_FERRAMENTA:
			Ferramenta fer = Ferramenta.class.cast(objeto);
			return "---- Ferramenta com código " + fer.getCodigoNh() + " removida.";
		case REMOVER_LOCACAO:
			Locacao locacao = Locacao.class.cast(objeto);
			return "---- Locação com código " + locacao.getCodigoLoc() + " removida. || Empresa: " + locacao.getEmpresa().getDescricao();
		default:
			return null;
		}

	}

	public String buscarModificacoes(Object objeto, BeanInfo info) {
		try {
			String modificacoes = "";
			for (PropertyDescriptor prop : info.getPropertyDescriptors()) {
				if (!String.valueOf(prop.getReadMethod().invoke(objetoOld)).replaceAll("[\\[\\]]", "")
						.matches(String.valueOf(prop.getReadMethod().invoke(objeto)).replaceAll("[\\[\\]]", ""))) {
					modificacoes += ">> " + prop.getName() + " <<" + " Valor antigo: "
							+ prop.getReadMethod().invoke(objetoOld) + ". Valor novo: "
							+ prop.getReadMethod().invoke(objeto) + ". \n";
				}
			}
			System.out.println(modificacoes);
			return modificacoes;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			return "ERRO DESCONHECIDO, contate TI";
		}
	}

	public void obterCodigoObjeto(Object objeto, BeanInfo info)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for (PropertyDescriptor pro : info.getPropertyDescriptors()) {
			if (pro.getName().matches("codigo")) {
				codigo = (Long) pro.getReadMethod().invoke(objeto);
			}
		}
	}
}
