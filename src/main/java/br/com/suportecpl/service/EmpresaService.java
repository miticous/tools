package br.com.suportecpl.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.suportecpl.model.Empresa;
import br.com.suportecpl.repository.EmpresaPU;

public class EmpresaService implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EmpresaPU empresaPU;
	
	public void inserirEmpresa(Empresa empresa){
		empresaPU.gravarEmpresa(empresa);
	}
	
	public List<Empresa> buscarEmpresas(){
		return empresaPU.todasEmpresas();
	}
	
	public Empresa empresaPorId(Long idEmpresa){
		return empresaPU.empresaPorId(idEmpresa);
	}
	
	public Empresa empresaPorCnpj(String cnpj){
		return empresaPU.empresaPorCnpj(cnpj);
	}
}
