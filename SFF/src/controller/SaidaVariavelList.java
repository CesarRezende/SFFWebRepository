package controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import business.SaidaVariavelRepository;
import data.SaidaVariavel;

@ManagedBean
@ViewScoped
public class SaidaVariavelList extends GenericManageBean {

	private SaidaVariavelRepository repository;

	private List<SaidaVariavel> saidasVariaveis = new ArrayList<SaidaVariavel>();

	public SaidaVariavelList() {
		System.out.println("Construtor SaidaVariavelList");
		this.repository = new SaidaVariavelRepository();

			this.setSaidasVariaveis(repository.getSaidasVariaveis(this.usuario.getFamilia()));

	}

	public void insert(AjaxBehaviorEvent event) {
		
	}

	public void edit(AjaxBehaviorEvent event) {
	}

	// Getters & Setters
	public List<SaidaVariavel> getAllSaidaVariavel() {
		return this.repository.getAllSaidaVariavel();

	}

	public List<SaidaVariavel> getSaidasVariaveis() {
		return saidasVariaveis;
	}

	public void setSaidasVariaveis(List<SaidaVariavel> saidasVariaveis) {
		this.saidasVariaveis = saidasVariaveis;
	}




}
