package controller;


import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import business.FamiliaRepository;
import business.UsuarioRepository;
import data.Familia;
import data.Usuario;

@ManagedBean
@ViewScoped
public class UsuarioManager extends GenericManageBean{

	
	private UsuarioRepository repository;
	private Usuario usr;
	private List<Familia> familias = new ArrayList<Familia>();
	private Long selectFamiliaId = new Long(1);
	private String previewsSenha = "";
	private FamiliaRepository familiaRepository = new FamiliaRepository();

	public UsuarioManager() {
		System.out.println("Construtor UsuarioManager");
		this.repository = new UsuarioRepository();
		
		if(this.usuario.isAdministrator())
			this.familias = familiaRepository.getAllFamilia();
		else
			this.familias.add(familiaRepository.findFamilia(this.usuario.getFamilia().getId()));
				
		String usuarioId  = (String)this.getRequestParameter("id");
		
		if(usuarioId != null){
			long id = 0;
			try{
				id = Long.parseLong(usuarioId);
			}catch(Exception e){}
			
			this.usr = this.repository.findUsuario(id);
		}
		
		
		if(this.usr == null){
			this.usr = new Usuario();
			this.usr.setNewRecord(true);
			
		}else{
			if(this.usr.getFamilia() != null)
				this.selectFamiliaId = this.usr.getFamilia().getId();
			this.previewsSenha = this.usr.getSenha(); 
		}
	}


	
	public void saveUsuario(AjaxBehaviorEvent event) {

		this.usr.setFamilia(new FamiliaRepository().findFamilia(this.selectFamiliaId));
		
		if(this.usr.isNewRecord()){
			this.usr.encodeSenha();
			repository.saveUsuario(this.usr);
		}
		else{
			if(!this.previewsSenha.equals(this.usr.getSenha()))
				this.usr.encodeSenha();
			
			repository.updateUsuario(this.usr);
		}
	}


	
	//Getters & Setters
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}



	public List<Familia> getFamilias() {
		return familias;
	}



	public void setFamilias(List<Familia> familias) {
		this.familias = familias;
	}



	public Long getSelectFamiliaId() {
		return selectFamiliaId;
	}



	public void setSelectFamiliaId(Long selectFamiliaId) {
		this.selectFamiliaId = selectFamiliaId;
	}



	public Usuario getUsr() {
		return usr;
	}



	public void setUsr(Usuario usr) {
		this.usr = usr;
	}

}
