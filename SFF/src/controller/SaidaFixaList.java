package controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import business.SaidaFixaRepository;
import data.SaidaFixa;

@ManagedBean
@ViewScoped
public class SaidaFixaList extends GenericManageBean {

	private SaidaFixaRepository repository;

	private List<SaidaFixa> saidasFixas = new ArrayList<SaidaFixa>();

	public SaidaFixaList() {
		System.out.println("Construtor SaidaFixaList");
		this.repository = new SaidaFixaRepository();

			this.setSaidasFixas(repository.getSaidasFixas(this.usuario.getFamilia()));

	}

	public void insert(AjaxBehaviorEvent event) {
		
	}

	public void edit(AjaxBehaviorEvent event) {
	}

	// Getters & Setters
	public List<SaidaFixa> getAllSaidaFixa() {
		return this.repository.getAllSaidaFixa();

	}

	public List<SaidaFixa> getSaidasFixas() {
		return saidasFixas;
	}

	public void setSaidasFixas(List<SaidaFixa> saidasFixas) {
		this.saidasFixas = saidasFixas;
	}




}
