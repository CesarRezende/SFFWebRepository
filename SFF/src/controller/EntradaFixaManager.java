package controller;

import java.util.Calendar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import business.IEntradaFixa;
import business.EntradaFixaBean;
import business.EntradaFixaRepository;
import data.EntradaFixa;
import data.EntradaFixa.TipoDia;

@ManagedBean
@ViewScoped
public class EntradaFixaManager extends GenericManageBean{

	
	private EntradaFixaRepository repository;
	private EntradaFixa entradaFixa;
	private TipoDia selectedTipoDia;

	public EntradaFixaManager() {
		System.out.println("Construtor EntradaFixaManager");
		this.repository = new EntradaFixaRepository();
		
		String entradaFixaId  = (String)this.getRequestParameter("id");
		
		if(entradaFixaId != null){
			long id = 0;
			try{
				id = Long.parseLong(entradaFixaId);
			}catch(Exception e){}
			
			this.entradaFixa = this.repository.findEntradaFixa(id);
			
		}
		
		
		if(this.entradaFixa == null){
			this.entradaFixa = new EntradaFixa();
			this.entradaFixa.setNewRecord(true);
			Calendar todayDate = Calendar.getInstance();
			if(todayDate.get(Calendar.DST_OFFSET) > 0)
				todayDate.add(Calendar.HOUR_OF_DAY, -2);
			else
				todayDate.add(Calendar.HOUR_OF_DAY, -3);
			this.entradaFixa.setDataLancamento(todayDate.getTime());
			this.entradaFixa.setJan(true);
			this.entradaFixa.setFev(true);
			this.entradaFixa.setMar(true);
			this.entradaFixa.setAbr(true);
			this.entradaFixa.setMai(true);
			this.entradaFixa.setJun(true);
			this.entradaFixa.setJul(true);
			this.entradaFixa.setAgo(true);
			this.entradaFixa.setSet(true);
			this.entradaFixa.setOut(true);
			this.entradaFixa.setNov(true);
			this.entradaFixa.setDez(true);
			
		}else{
			this.selectedTipoDia = this.entradaFixa.getTipoDia();
		}
	}


	
	public void saveEntradaFixa(AjaxBehaviorEvent event) {

		IEntradaFixa iEntradaFixa = new EntradaFixaBean(); 
		iEntradaFixa.saveEntradaFixa(this.entradaFixa, this.usuario, this.selectedTipoDia);
		
	}
	
	public void disableEntradaFixa(AjaxBehaviorEvent event) {
		IEntradaFixa iEntradaFixa = new EntradaFixaBean(); 
		iEntradaFixa.disableEntradaFixa(this.entradaFixa, this.usuario, this.selectedTipoDia);
		
	}
	

	public void onTipoDiaChange(AjaxBehaviorEvent event) {

		if(!this.selectedTipoDia.equals(TipoDia.DIA_SEMANA))
			this.entradaFixa.setOcorrencia(0);
		else
			this.entradaFixa.setOcorrencia(9);
	}
	
	//Getters & Setters
	public TipoDia[] getTiposDia() {
        return TipoDia.values();
    }
	
	public void setEntradaFixa(EntradaFixa entradaFixa) {
		this.entradaFixa = entradaFixa;
	}

	public EntradaFixa getEntradaFixa() {
		return entradaFixa;
	}



	public TipoDia getSelectedTipoDia() {
		return selectedTipoDia;
	}



	public void setSelectedTipoDia(TipoDia selectedTipoDia) {
		this.selectedTipoDia = selectedTipoDia;
	}



	public EntradaFixaRepository getRepository() {
		return repository;
	}



	public void setRepository(EntradaFixaRepository repository) {
		this.repository = repository;
	}


}
