package controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import business.EntradaFixaRepository;
import data.EntradaFixa;

@ManagedBean
@ViewScoped
public class EntradaFixaList extends GenericManageBean {

	private EntradaFixaRepository repository;

	private List<EntradaFixa> entradasFixas = new ArrayList<EntradaFixa>();

	public EntradaFixaList() {
		System.out.println("Construtor EntradaFixaList");
		this.repository = new EntradaFixaRepository();

			this.setEntradasFixas(repository.getEntradasFixas(this.usuario.getFamilia()));

	}

	public void insert(AjaxBehaviorEvent event) {
		
	}

	public void edit(AjaxBehaviorEvent event) {
	}

	// Getters & Setters
	public List<EntradaFixa> getAllEntradaFixa() {
		return this.repository.getAllEntradaFixa();

	}

	public List<EntradaFixa> getEntradasFixas() {
		return entradasFixas;
	}

	public void setEntradasFixas(List<EntradaFixa> entradasFixas) {
		this.entradasFixas = entradasFixas;
	}




}
