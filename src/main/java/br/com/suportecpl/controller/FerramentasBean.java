package br.com.suportecpl.controller;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.util.IOUtils;
import org.eclipse.persistence.platform.xml.SAXDocumentBuilder;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.lowagie.text.Document;
import com.sun.jmx.snmp.Timestamp;

import br.com.suportecpl.model.Empresa;
import br.com.suportecpl.model.Estoque;
import br.com.suportecpl.model.Ferramenta;
import br.com.suportecpl.model.Fornecedor;
import br.com.suportecpl.model.Grupo;
import br.com.suportecpl.model.Locacao;
import br.com.suportecpl.model.LogSistema;
import br.com.suportecpl.model.TipoAcoes;
import br.com.suportecpl.model.TipoObrigatoriedade;
import br.com.suportecpl.model.Usuario;
import br.com.suportecpl.repository.FerramentaPU;
import br.com.suportecpl.service.EmpresaService;
import br.com.suportecpl.service.EstoqueService;
import br.com.suportecpl.service.FerramentaService;
import br.com.suportecpl.service.FornecedorService;
import br.com.suportecpl.service.GrupoService;
import br.com.suportecpl.service.LocacaoService;
import br.com.suportecpl.service.LogSistemaService;
import br.com.suportecpl.service.UsuarioService;
import br.com.suportecpl.util.FacesMessages;

@SessionScoped
@Named
public class FerramentasBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private FerramentaService feService;
	@Inject
	private FerramentaPU ferramentaPU;
	@Inject
	private LocacaoService locacaoService;
	@Inject
	private EmpresaService empresaService;
	@Inject
	private UsuarioService usuarioService;
	@Inject
	private FacesMessages message;
	@Inject
	private EstoqueService estoqueService;
	@Inject
	private GrupoService grupoService;
	@Inject
	private LogSistemaService logSistemaService;
	@Inject
	private FornecedorService fornecedorService;

	private UploadedFile file;
	private Empresa empresa;
	private Estoque estoque;
	private Locacao locacao;
	private Ferramenta ferramenta;
	private Usuario usuario;

	private List<Fornecedor> fornecedores;
	private List<Grupo> grupos;
	private List<Locacao> locacoes;
	private List<Empresa> empresas;
	private List<Ferramenta> ferramentas;

	@PostConstruct
	public void init() {
		usuario = new Usuario();
		ferramenta = new Ferramenta();
		locacao = new Locacao();
		estoque = new Estoque();
		ferramentas = new ArrayList<>();
	}

	public void limpaObjetos() {
		ferramenta = new Ferramenta();
		estoque = new Estoque();
		empresas = new ArrayList<>();
		locacao = new Locacao();
		locacoes = new ArrayList<>();
		empresa = new Empresa();
		grupos = new ArrayList<>();
	}

	public Usuario getUsuario() {
		return usuarioService.obterUsuarioPorId(new LoginBean().obterIdUsuarioSessao());
	}

	public void listaFerramentasPorEmpresa() throws IOException {
		validaAcesso();
		limpaObjetos();
		try {
			ferramentas = new ArrayList<>();
			// ferramentas = feService.buscaFerramentaPorEmp((long) 2);
			// CODIGO TEMPORARIO PARA LISTA DE FERRAMENTAS, VISUALIZACAO ADMIN
			ferramentas = feService.todasFerramentas();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void listaFerramentasPorEmpNomeCod(String parametro) {
		ferramentas = ferramentaPU.buscaFerramentaPorEmpDescCodigo(parametro, (long) 2);
	}

	public void listaFerramentasPorStatus() {

	}

	public void prepararNovaLocacao() {
		locacao = new Locacao();
		empresa = new Empresa();
		empresas = empresaService.buscarEmpresas();
	}

	public void adicionarLocacao() {
		try {
			locacaoService.adicionarLocacao(locacao);
			geraLog(locacao, TipoAcoes.ADICIONAR_LOCACAO, "LOCACAO");
			FacesContext.getCurrentInstance().getExternalContext().redirect("/ferramentas.xhtml");
		} catch (Exception e) {
			// TODO: handle exception
			message.error(
					"Não foi possível adicionar locação. Esta locação já existe para essa empresa ou não pôde ser salva");
		}

	}

	public void atualizarFerramentaSelecionada(Ferramenta fer) {
		ferramenta = fer;
	}

	public void prepararParaAdicionarFerramenta() {
		ferramenta = new Ferramenta();
		estoque = new Estoque();
		grupos = grupoService.listarGrupos();
		empresas = empresaService.buscarEmpresas();
	}

	public void atualizaListaDeLocacoesPorEmp() {
		try {
			for (Empresa e : empresas) {
				if (estoque.getEmpresa().getCodigo() == e.getCodigo()) {
					locacoes = e.getLocacoes();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			for (Empresa e2 : empresas) {
				if (e2.getCodigo() == empresa.getCodigo()) {
					locacoes = e2.getLocacoes();
				}
			}
		}
	}

	public void adicionarFerramenta() throws Exception {
		try {
			ferramenta.setUsuarioInclusao(getUsuario());
			ferramenta.setUsuarioAlteracao(getUsuario());
			ferramenta.setDataInclusao(new Date());
			ferramenta.setDataAlteracao(new Date());
			geraLog(ferramenta, TipoAcoes.ADICIONAR_FERRAMENTA, "FERRAMENTA");
			feService.inserirFerramenta(ferramenta);
			ferramenta = new Ferramenta();
			FacesContext.getCurrentInstance().getExternalContext().redirect("/ferramentas.xhtml");
		} catch (NullPointerException e) {
			// TODO: handle exception
			feService.inserirFerramenta(ferramenta);
			ferramenta = new Ferramenta();
		} catch (Exception e) {
			// TODO: handle exception
			message.error(e.getMessage());
		}
	}

	public void preparaEdicaoFerramenta() {
		ferramenta = feService.buscaFerramentaPorId(ferramenta.getCodigo());
		grupos = grupoService.listarGrupos();
	}

	public void salvarEdicaoFerramenta() {
		try {
			ferramenta.setUsuarioAlteracao(getUsuario());
			ferramenta.setDataAlteracao(new Date());
			geraLog(ferramenta, TipoAcoes.EDITAR_FERRAMENTA, "FERRAMENTA");
			feService.inserirFerramenta(ferramenta);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void removerFerramenta() {
		try {
			feService.removerFerramenta(ferramenta);
			geraLog(ferramenta, TipoAcoes.REMOVER_FERRAMENTA, "FERRAMENTA");
			limpaObjetos();
		} catch (Exception e) {
			// TODO: handle exception
			message.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public void prepararParaRemoverLocacao() {
		limpaObjetos();
		prepararNovaLocacao();
	}

	public void removerLocacao() throws IOException {
		try {
			locacaoService.removerLocacao(locacao);
			geraLog(locacao, TipoAcoes.REMOVER_LOCACAO, "LOCACAO");
			FacesContext.getCurrentInstance().getExternalContext().redirect("/ferramentas.xhtml");
		} catch (Exception e) {
			// TODO: handle exception
			RequestContext.getCurrentInstance().execute("alert('" + e.getMessage() + "')");
		}
	}

	public void prepararAdicionarEstoqueFerramenta() {
		locacao = new Locacao();
		estoque = new Estoque();
		empresas = empresaService.buscarEmpresas();
	}

	public void prepararEdicaoEstoqueFerramenta() {
		preparaEdicaoFerramenta();
		estoque = new Estoque();
		empresas = new ArrayList<>();
		for (Estoque est : ferramenta.getEstoques()) {
			empresas.add(est.getEmpresa());
		}

	}

	public void adicionarEstoqueFerramenta() {
		try {
			ferramenta.setUsuarioAlteracao(getUsuario());
			ferramenta.setDataAlteracao(new Date());
			feService.verificaEstoqueFerramentaExistente(ferramenta, estoque.getEmpresa().getCodigo());
			estoque.setFerramenta(ferramenta);
			ferramenta.getEstoques().add(estoque);
			feService.inserirFerramenta(ferramenta);
			geraLog(estoque, TipoAcoes.ADICIONAR_ESTOQUE, "ESTOQUE");
			FacesContext.getCurrentInstance().getExternalContext().redirect("/ferramentas.xhtml");
		} catch (Exception e) {
			// TODO: handle exception
			RequestContext.getCurrentInstance().execute("alert('" + e + "')");
		}
	}

	public void selecionarEstoqueFerramentaEdicao() {
		for (Estoque e : ferramenta.getEstoques()) {
			if (e.getEmpresa().getCodigo() == estoque.getEmpresa().getCodigo()) {
				estoque = e;
			}
		}
		atualizaListaDeLocacoesPorEmp();
	}

	public void filtrarObrigatoriosSemEstoque() {
		ferramentas = feService.filtrarObrigatoriosSemEstoque();
	}

	public void validaAcesso() throws IOException {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpServletRequest request = (HttpServletRequest) req;
		HttpSession session = (HttpSession) request.getSession();
		Long id = (Long) session.getAttribute("USERID");
		if (id == null) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/login.xhtml");
		} else if (!getUsuario().getPermissao().getDescricao().matches("ADMINISTRADOR"))
			FacesContext.getCurrentInstance().getExternalContext().redirect("/index.xhtml");
	}

	public void salvarEdicaoEstoqueFerramenta() {
		try {
			geraLog(estoque, TipoAcoes.EDITAR_ESTOQUE, "ESTOQUE");
			estoqueService.salvarEstoque(estoque);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// GERA LOG DE ACORDO COM A ACAO
	public void geraLog(Object objeto, TipoAcoes tipoAcao, String tabela)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		try {
			LogSistema log = new LogSistema();
			log.setAcao(logSistemaService.gerarAcao(objeto, tipoAcao));
			log.setDataEvento(new Date());
			log.setUsuario(getUsuario().getUsername());
			log.setTabela(tabela);
			logSistemaService.salvarLog(log);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException, ParserConfigurationException, SAXException {
		File file = new File(
				"C:\\Users\\" + System.getProperty("user.name") + "\\tools\\xml_old\\" + event.getFile().getFileName());

		try {
			estoqueService.iniciarImportacaoXml(event.getFile().getInputstream());
			OutputStream os = new FileOutputStream(file);
			IOUtils.copy(event.getFile().getInputstream(), os);
			os.flush();
			os.close();
			message.info("O arquivo " + event.getFile().getFileName() + " foi importado e processado com sucesso.");
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			new File("C:\\Users\\" + System.getProperty("user.name") + "\\tools\\xml_old\\").mkdir();
			handleFileUpload(event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			message.info(e.getMessage());
		}
	}
	
	public void alterarImagemFerramenta(FileUploadEvent event) throws IOException, ParserConfigurationException, SAXException {
		String formato = event.getFile().getFileName();
		formato = formato.substring(formato.indexOf("."), formato.length());

		File file = new File(
				"C:\\Users\\" + System.getProperty("user.name") + "\\tools\\tool-img\\" + ferramenta.getCodigoNh() + formato);

		try {
			OutputStream os = new FileOutputStream(file);
			IOUtils.copy(event.getFile().getInputstream(), os);
			os.flush();
			os.close();
			message.info("O arquivo " + event.getFile().getFileName() + " foi importado e processado com sucesso.");
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			new File("C:\\Users\\" + System.getProperty("user.name") + "\\tools\\tool-img\\").mkdirs();
			alterarImagemFerramenta(event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.info(e.getMessage());
		}
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public List<Ferramenta> getFerramentas() {
		return ferramentas;
	}

	public void setFerramentas(List<Ferramenta> ferramentas) {
		this.ferramentas = ferramentas;
	}

	public Ferramenta getFerramenta() {
		return ferramenta;
	}

	public void setFerramenta(Ferramenta ferramenta) {
		this.ferramenta = ferramenta;
	}

	public Locacao getLocacao() {
		return locacao;
	}

	public void setLocacao(Locacao locacao) {
		this.locacao = locacao;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public Estoque getEstoque() {
		return estoque;
	}

	public void setEstoque(Estoque estoque) {
		this.estoque = estoque;
	}

	public List<Locacao> getLocacoes() {
		return locacoes;
	}

	public void setLocacoes(List<Locacao> locacoes) {
		this.locacoes = locacoes;
	}

	public TipoObrigatoriedade[] getTipoObrigatoriedade() {
		return TipoObrigatoriedade.values();
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	public List<Fornecedor> getFornecedores() {
		fornecedores = fornecedorService.obterFornecedores();
		return fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

}
