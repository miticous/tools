package br.com.suportecpl.service;

import java.beans.IntrospectionException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.suportecpl.controller.FerramentasBean;
import br.com.suportecpl.controller.LoginBean;
import br.com.suportecpl.model.Estoque;
import br.com.suportecpl.model.Ferramenta;
import br.com.suportecpl.model.Fornecedor;
import br.com.suportecpl.model.LogSistema;
import br.com.suportecpl.model.TipoAcoes;
import br.com.suportecpl.repository.EstoquePU;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class EstoqueService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private EstoquePU estoquePU;
	@Inject
	private EmpresaService empService;
	@Inject
	private FerramentaService feService;
	@Inject
	private FornecedorService fornecedorService;
	@Inject
	private LocacaoService locacaoService;
	@Inject
	private UsuarioService uService;

	private InputStream iso;
	private StringBuilder string;
	private String cnpjEmp;

	public void salvarEstoque(Estoque estoque) {
		estoquePU.salvarEstoque(estoque);
	}

	public Estoque estoquePorId(Long codEst) {
		return estoquePU.estoquePorId(codEst);
	}

	public Estoque estoquePorCnpjECod(String cnpj, String codProd) {
		return estoquePU.estoquePorEmpECod(cnpj, codProd);
	}

	public List<Estoque> estoquePorNf(Long nf) {
		return estoquePU.estoquePorNfNum(nf);
	}

	public void iniciarImportacaoXml(InputStream is) throws Exception {
		iso = is;
		cnpjEmp = "";
		geraString();
		List<String> targets = new ArrayList<>();
		targets = processaXml("prod", "cProd");
		cnpjEmp = targets.get(targets.size() - 1);
		Document doc = geraXml();
		String nfe = doc.getElementsByTagName("nNF").item(0).getFirstChild().getNodeValue();
		targets.remove(targets.size() - 1);
		try {
			if (estoquePorNf(Long.valueOf(nfe)).isEmpty()) {
				int i = 0;
				for (String s : targets) {
					try {
						Ferramenta fer = new Ferramenta();
						fer = feService.buscaFerramentaPorCodigoNh(s);
						gerenciaEstoque(fer, i);
						i++;
					} catch (NoResultException e) {
						// TODO: handle exception
						criarFerramenta(i);
						i++;
					}
				}
			} else
				throw new Exception(
						"Não foi possível importar este xml. Ele já pode ter sido importado ou está inválido. Verifique a NF "
								+ nfe);
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception(e.getMessage());
		}
	}

	public void criarFerramenta(int i) {
		try {
			Document doc = geraXml();
			Ferramenta fer = new Ferramenta();
			fer.setCodigoNh(doc.getElementsByTagName("cProd").item(i).getFirstChild().getNodeValue());
			fer.setDescricao(doc.getElementsByTagName("xProd").item(i).getFirstChild().getNodeValue());
			fer.setPreco(Double.valueOf(doc.getElementsByTagName("vUnCom").item(i).getFirstChild().getNodeValue()));
			fer.setNumeroNf(Long.valueOf(doc.getElementsByTagName("nNF").item(0).getFirstChild().getNodeValue()));
			fer.setAtiva(false);
			fer.setDataAlteracao(new Date());
			fer.setDataInclusao(new Date());
			fer.setUsuarioAlteracao(uService.obterUsuarioPorUsername("SYSTEM"));
			fer.setUsuarioInclusao(uService.obterUsuarioPorUsername("SYSTEM"));
			List<Estoque> ests = new ArrayList<>();
			Estoque est = new Estoque();
			est = criarEstoque(i);
			est.setFerramenta(fer);
			ests.add(est);
			fer.setEstoques(ests);
			feService.inserirFerramenta(fer);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void gerenciaEstoque(Ferramenta fer, int i)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		Document doc = geraXml();
		try {
			Estoque est = new Estoque();
			est = estoquePorCnpjECod(cnpjEmp, fer.getCodigoNh());
			est.setDataUltimaAlt(new Date());
			Long quant = 0L;
			quant = est.getQuantidade() + Long.valueOf(
					doc.getElementsByTagName("qCom").item(i).getFirstChild().getNodeValue().replace(".0000", ""));
			est.setQuantidade(quant);
			est.setUltimaNf(Long.valueOf(doc.getElementsByTagName("nNF").item(0).getFirstChild().getNodeValue()));
			salvarEstoque(est);
		} catch (NoResultException e) {
			// TODO: handle exception
			Estoque est = new Estoque();
			est = criarEstoque(i);
			est.setUltimaNf(Long.valueOf(doc.getElementsByTagName("nNF").item(0).getFirstChild().getNodeValue()));
			est.setFerramenta(fer);
			salvarEstoque(est);
		}
	}

	public Estoque criarEstoque(int i) {
		Estoque est = new Estoque();
		try {
			Document doc = geraXml();
			est.setEmpresa(empService.empresaPorCnpj(cnpjEmp));
			est.setQuantidade(Long.valueOf(
					doc.getElementsByTagName("qCom").item(i).getFirstChild().getNodeValue().replace(".0000", "")));
			est.setDataUltimaAlt(new Date());
			est.setLocacao(locacaoService.locacaoPorEmpECod(cnpjEmp, "PENDENTE"));
			est.setFornecedor(fornecedorService
					.buscaPorCnpj(doc.getElementsByTagName("CNPJ").item(0).getFirstChild().getNodeValue()));
			return est;
		} catch (Exception e) {
			// TODO: handle exception
			est.setFornecedor(criarFornecedor(i));
			return est;
		}
	}

	public Fornecedor criarFornecedor(int i) {
		try {
			Fornecedor forn = new Fornecedor();
			Document doc = geraXml();
			forn.setCnpj(doc.getElementsByTagName("CNPJ").item(0).getFirstChild().getNodeValue());
			forn.setDescricao(doc.getElementsByTagName("xNome").item(0).getFirstChild().getNodeValue());
			return forn;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public List<String> processaXml(String area, String target) {
		Document doc = geraXml();
		List<String> targets = new ArrayList<>();

		for (int i = 0; i < doc.getElementsByTagName(area).getLength(); i++) {
			targets.add(doc.getElementsByTagName(target).item(i).getFirstChild().getNodeValue());
		}
		targets.add(doc.getElementsByTagName("CNPJ").item(1).getFirstChild().getNodeValue());
		return targets;
	}

	public Document geraXml() {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource src = new InputSource();
			src.setCharacterStream(new StringReader(string.toString()));
			org.w3c.dom.Document doc = builder.parse(src);
			return doc;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public void geraString() throws IOException {
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(iso));
		StringBuilder sb = new StringBuilder();
		String inline = "";
		while ((inline = inputReader.readLine()) != null) {
			sb.append(inline);
		}
		string = new StringBuilder();
		string = sb;
	}
}
