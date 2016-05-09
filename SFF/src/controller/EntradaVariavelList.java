package controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import business.EntradaVariavelRepository;
import data.EntradaVariavel;

@ManagedBean
@ViewScoped
public class EntradaVariavelList extends GenericManageBean {

	private EntradaVariavelRepository repository;

	private List<EntradaVariavel> entradasVariaveis = new ArrayList<EntradaVariavel>();

	public EntradaVariavelList() {
		System.out.println("Construtor EntradaVariavelList");
		this.repository = new EntradaVariavelRepository();

			this.setEntradasVariaveis(repository.getEntradasVariaveis(this.usuario.getFamilia()));

	}

	public void insert(AjaxBehaviorEvent event) {
		
	}

	public void edit(AjaxBehaviorEvent event) {
	}

	// Getters & Setters
	public List<EntradaVariavel> getAllEntradaVariavel() {
		return this.repository.getAllEntradaVariavel();

	}

	public List<EntradaVariavel> getEntradasVariaveis() {
		return entradasVariaveis;
	}

	public void setEntradasVariaveis(List<EntradaVariavel> entradasVariaveis) {
		this.entradasVariaveis = entradasVariaveis;
	}




}
