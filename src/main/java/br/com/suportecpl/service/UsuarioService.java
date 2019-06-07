package br.com.suportecpl.service;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Base64;

import javax.inject.Inject;

import br.com.suportecpl.model.Usuario;
import br.com.suportecpl.repository.UsuarioPU;

public class UsuarioService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioPU usuarioPU;

	public Usuario obterUsuarioPorId(Long id) {
		return usuarioPU.usuarioPorId(id);
	}

	public Usuario obterUsuarioPorUsername(String username) {
		return usuarioPU.usuarioPorUserName(username);
	}

	public boolean validarLogin(String username, String password) throws Exception {
		try {
			if (password.matches(obterUsuarioPorUsername(username).getPassword())) {
				return true;
			} else
				return false;
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("Usuário não existe");
		}

	}
}
