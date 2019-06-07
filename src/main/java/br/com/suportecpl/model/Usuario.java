package br.com.suportecpl.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String username;

	@Column
	private String password;

	@Column
	private String nome;

	@ManyToOne
	private Permissao permissao;

	@Column
	private Long empresa;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "usuarioInclusao")
	private List<Ferramenta> ferramentasIncluidas;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "usuarioAlteracao")
	private List<Ferramenta> ferramentasAlteradas;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}

	public Long getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Long empresa) {
		this.empresa = empresa;
	}

	public List<Ferramenta> getFerramentasIncluidas() {
		return ferramentasIncluidas;
	}

	public void setFerramentasIncluidas(List<Ferramenta> ferramentasIncluidas) {
		this.ferramentasIncluidas = ferramentasIncluidas;
	}

	public List<Ferramenta> getFerramentasAlteradas() {
		return ferramentasAlteradas;
	}

	public void setFerramentasAlteradas(List<Ferramenta> ferramentasAlteradas) {
		this.ferramentasAlteradas = ferramentasAlteradas;
	}

}
