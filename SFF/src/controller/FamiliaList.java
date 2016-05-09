package controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import business.FamiliaRepository;
import data.Familia;

@ManagedBean
@ViewScoped
public class FamiliaList extends GenericManageBean {

	private FamiliaRepository repository;

	private List<Familia> familia = new ArrayList<Familia>();

	public FamiliaList() {
		System.out.println("Construtor FamiliaList");
		this.repository = new FamiliaRepository();
		
		
		if(this.usuario.isAdministrator())
			this.setFamilias(this.getAllFamilia());
		else
			this.familia.add(repository.findFamilia(this.usuario.getFamilia().getId()));
	}

	public void insert(AjaxBehaviorEvent event) {
		
	}

	public void edit(AjaxBehaviorEvent event) {
	}

	// Getters & Setters
	public List<Familia> getAllFamilia() {
		return this.repository.getAllFamilia();

	}

	public void setFamilias(List<Familia> familia) {
		this.familia = familia;
	}

	public List<Familia> getFamilias() {
		return familia;
	}


}
