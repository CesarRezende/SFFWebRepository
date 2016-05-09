package controller;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import business.FamiliaRepository;
import data.Familia;

@ManagedBean
@ViewScoped
public class FamiliaManager extends GenericManageBean{

	
	private FamiliaRepository repository;
	private Familia familia;

	public FamiliaManager() {
		System.out.println("Construtor FamiliaManager");
		this.repository = new FamiliaRepository();
		
		String familiaId  = (String)this.getRequestParameter("id");
		
		if(familiaId != null){
			long id = 0;
			try{
				id = Long.parseLong(familiaId);
			}catch(Exception e){}
			
			this.familia = this.repository.findFamilia(id);
		}
		
		
		if(this.familia == null){
			this.familia = new Familia();
			this.familia.setNewRecord(true);
			
		}
	}


	
	public void saveFamilia(AjaxBehaviorEvent event) {

		if(this.familia.isNewRecord())
			repository.saveFamilia(this.familia);
		else
			repository.updateFamilia(this.familia);
		
	}

	
	//Getters & Setters
	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

	public Familia getFamilia() {
		return familia;
	}

}
