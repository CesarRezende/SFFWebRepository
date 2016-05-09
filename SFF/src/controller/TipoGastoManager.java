package controller;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import business.TipoGastoRepository;
import data.TipoGasto;

@ManagedBean
@ViewScoped
public class TipoGastoManager extends GenericManageBean{

	
	private TipoGastoRepository repository;
	private TipoGasto tipoGasto;

	public TipoGastoManager() {
		System.out.println("Construtor TipoGastoManager");
		this.repository = new TipoGastoRepository();
		
		String tipoGastoId  = (String)this.getRequestParameter("id");
		
		if(tipoGastoId != null){
			long id = 0;
			try{
				id = Long.parseLong(tipoGastoId);
			}catch(Exception e){}
			
			this.tipoGasto = this.repository.findTipoGasto(id);
		}
		
		
		if(this.tipoGasto == null){
			this.tipoGasto = new TipoGasto();
			this.tipoGasto.setNewRecord(true);
			this.tipoGasto.setFamilia(this.usuario.getFamilia());
			
		}
	}


	
	public void saveTipoGasto(AjaxBehaviorEvent event) {

		if(this.tipoGasto.isNewRecord())
			repository.saveTipoGasto(this.tipoGasto);
		else
			repository.updateTipoGasto(this.tipoGasto);
		
	}

	
	//Getters & Setters
	public void setTipoGasto(TipoGasto tipoGasto) {
		this.tipoGasto = tipoGasto;
	}

	public TipoGasto getTipoGasto() {
		return tipoGasto;
	}

}
