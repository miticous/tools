package br.com.suportecpl.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.w3c.dom.Document;

import br.com.suportecpl.model.Fornecedor;
import br.com.suportecpl.repository.FornecedorPU;

public class FornecedorService implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private FornecedorPU fornecedorPU;
	
	public Fornecedor buscaPorCnpj(String cnpj){
		return fornecedorPU.fornecedorPorCnpj(cnpj);
	}
	
	public Fornecedor buscaPorId(Long id){
		return fornecedorPU.fornecedorPorId(id);
	}
	
	public void gravarFornecedor(Fornecedor fornecedor){
		fornecedorPU.salvarFornecedor(fornecedor);
	}
	
	public List<Fornecedor> obterFornecedores(){
		return fornecedorPU.todosFornecedores();
	}
	
	public Fornecedor criaFornecedor(Document doc){
		Fornecedor fornecedor = new Fornecedor();
		System.out.println(doc.getElementsByTagName("CNPJ").item(0).getFirstChild().getNodeValue());
		fornecedor.setCnpj(doc.getElementsByTagName("CNPJ").item(0).getFirstChild().getNodeValue());
		System.out.println(doc.getElementsByTagName("xNome").item(0).getFirstChild().getNodeValue());
		fornecedor.setDescricao(doc.getElementsByTagName("xNome").item(0).getFirstChild().getNodeValue());
		return fornecedor;
	}
}
