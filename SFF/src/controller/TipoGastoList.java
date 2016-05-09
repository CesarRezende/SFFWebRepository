package controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import business.TipoGastoRepository;
import data.TipoGasto;

@ManagedBean
@ViewScoped
public class TipoGastoList extends GenericManageBean {

	private TipoGastoRepository repository;

	private List<TipoGasto> tipoGastos = new ArrayList<TipoGasto>();

	public TipoGastoList() {
		System.out.println("Construtor TipoGastoList");
		this.repository = new TipoGastoRepository();
		this.setTipoGastos(this.repository.getAllTipoGasto(this.usuario.getFamilia()));
	}

	public void insert(AjaxBehaviorEvent event) {
		
	}

	public void edit(AjaxBehaviorEvent event) {
	}

	// Getters & Setters
	public List<TipoGasto> getAllTipoGasto() {
		return this.repository.getAllTipoGasto();

	}

	public void setTipoGastos(List<TipoGasto> tipoGastos) {
		this.tipoGastos = tipoGastos;
	}

	public List<TipoGasto> getTipoGastos() {
		return tipoGastos;
	}


}
