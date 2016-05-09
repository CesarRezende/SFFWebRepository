package controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import business.SFFUtil;
import business.UsuarioRepository;
import data.Usuario;


@ManagedBean
@SessionScoped
public class LoginBean {

	private String usuario;
	private String senha;

	public String entrar() {

		Usuario usr = new UsuarioRepository().findUsuario(this.usuario);
		
		if (usr != null && usr.getSenha().equals(SFFUtil.encodeSenha(this.senha))) {
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			session.setAttribute("autenticado", true);
			session.setAttribute("usr", usr);

			return "/dashboard";
		} else {

			return "/index";
		}

	}

	public String sair() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		session.removeAttribute("autenticado");

		return "/index";

	}
	
	// Getters & Setters
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
