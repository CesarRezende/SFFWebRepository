package controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import business.UsuarioRepository;
import data.Usuario;

@ManagedBean
@ViewScoped
public class UsuarioList extends GenericManageBean {

	private UsuarioRepository repository;

	private List<Usuario> usr = new ArrayList<Usuario>();

	public UsuarioList() {
		System.out.println("Construtor UsuarioList");
		this.repository = new UsuarioRepository();
		
		if(this.usuario.isAdministrator())
			this.setUsuarios(this.getAllUsuario());
		else
			this.setUsuarios(this.repository.getAllUsuario(this.usuario.getFamilia()));
	}

	public void insert(AjaxBehaviorEvent event) {
		
	}

	public void edit(AjaxBehaviorEvent event) {
	}

	// Getters & Setters
	public List<Usuario> getAllUsuario() {
		return this.repository.getAllUsuario();

	}

	public void setUsuarios(List<Usuario> usr) {
		this.usr = usr;
	}

	public List<Usuario> getUsuarios() {
		return usr;
	}


}
