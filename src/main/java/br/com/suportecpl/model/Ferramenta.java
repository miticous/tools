package br.com.suportecpl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table
public class Ferramenta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long codigo;
	
	@NotEmpty
	@Column(name = "codigo_nh")
	private String codigoNh;

	@Column
	private String codigoAntigo;

	@Column
	private Long numeroNf;
	
	@Column
	@NotEmpty
	private String descricao;

	@Enumerated(EnumType.ORDINAL)
	private TipoObrigatoriedade obrigatorio;
	
	@Column
	private String aplicabilidade;

	@Column
	private boolean ativa = false;

	@Column
	private double preco;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "ferramenta")
	private List<Estoque> estoques;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Grupo grupo;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInclusao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAlteracao;

	@ManyToOne
	private Usuario usuarioInclusao;
	
	@ManyToOne
	private Usuario usuarioAlteracao;
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getCodigoNh() {
		return codigoNh;
	}

	public void setCodigoNh(String codigoNh) {
		this.codigoNh = codigoNh.toUpperCase();
	}

	public String getCodigoAntigo() {
		return codigoAntigo;
	}

	public void setCodigoAntigo(String codigoAntigo) {
		this.codigoAntigo = codigoAntigo.toUpperCase();
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.toUpperCase();
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public List<Estoque> getEstoques() {
		return estoques;
	}

	public void setEstoques(List<Estoque> estoques) {
		this.estoques = estoques;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	public String getAplicabilidade() {
		return aplicabilidade;
	}

	public void setAplicabilidade(String aplicabilidade) {
		this.aplicabilidade = aplicabilidade;
	}

	public TipoObrigatoriedade getObrigatorio() {
		return obrigatorio;
	}

	public void setObrigatorio(TipoObrigatoriedade obrigatorio) {
		this.obrigatorio = obrigatorio;
	}

	public Long getNumeroNf() {
		return numeroNf;
	}

	public void setNumeroNf(Long numeroNf) {
		this.numeroNf = numeroNf;
	}

	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public Usuario getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(Usuario usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	public Usuario getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ferramenta other = (Ferramenta) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(codigo);
	}
}
