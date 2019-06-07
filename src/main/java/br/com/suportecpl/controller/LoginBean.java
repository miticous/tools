package br.com.suportecpl.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.com.suportecpl.model.Usuario;
import br.com.suportecpl.service.UsuarioService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

@SessionScoped
@Named
public class LoginBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioService usuarioService;
	
	private String username;
	private String password;
	private Usuario usuario;
	
	@PostConstruct
	public void init(){
		usuario = new Usuario();
	}
	
	public void efetuarLogin(){
		try{
			if(usuarioService.validarLogin(username, password)){
				usuario = usuarioService.obterUsuarioPorUsername(username);
				definirIdUsuarioSessao(usuario.getId());
				FacesContext.getCurrentInstance().getExternalContext().redirect("/ferramentas.xhtml");
			}else
				RequestContext.getCurrentInstance().execute("alert('Usuario e/ou senha inválidos. Contate TI')");
			
		}catch (Exception e) {
			// TODO: handle exception
			RequestContext.getCurrentInstance().execute("alert('" + e.getMessage() + "')");
		}
		
	}
	
	public void efetuarLogout() throws IOException{
		try{
			ExternalContext fc = FacesContext.getCurrentInstance().getExternalContext();
			fc.invalidateSession();
			fc.redirect("/login.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
			
		}
		

	}
	
	public void definirIdUsuarioSessao(Long idUser){
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		session.setAttribute("USERID", idUser);
	}

	public Long obterIdUsuarioSessao() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpServletRequest request = (HttpServletRequest) req;
		HttpSession session = (HttpSession) request.getSession();
		return (Long) session.getAttribute("USERID");
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
