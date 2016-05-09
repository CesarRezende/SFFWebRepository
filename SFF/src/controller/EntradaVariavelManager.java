package controller;

import java.util.Calendar;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import business.ErrorMessageException;
import business.IEntradaVariavel;
import business.EntradaVariavelBean;
import business.EntradaVariavelRepository;
import data.EntradaVariavel;
import data.EntradaVariavel.Status;
import data.EntradaVariavel.TipoDia;

@ManagedBean
@ViewScoped
public class EntradaVariavelManager extends GenericManageBean{

	private EntradaVariavelRepository repository;
	private EntradaVariavel entradaVariavel;
	private TipoDia selectedTipoDia;

	public EntradaVariavelManager() {
		System.out.println("Construtor EntradaVariavelManager");
		this.repository = new EntradaVariavelRepository();
		
		String entradaVariavelId  = (String)this.getRequestParameter("id");
		
		if(entradaVariavelId != null){
			long id = 0;
			try{
				id = Long.parseLong(entradaVariavelId);
			}catch(Exception e){}
			
			this.entradaVariavel = this.repository.findEntradaVariavel(id);
			
		}
		
		
		if(this.entradaVariavel == null){
			this.entradaVariavel = new EntradaVariavel();
			this.entradaVariavel.setNewRecord(true);
			this.entradaVariavel.setStatus(Status.ABERTO);
			Calendar todayDate = Calendar.getInstance();
			if(todayDate.get(Calendar.DST_OFFSET) > 0)
				todayDate.add(Calendar.HOUR_OF_DAY, -2);
			else
				todayDate.add(Calendar.HOUR_OF_DAY, -3);
			this.entradaVariavel.setDataLancamento(todayDate.getTime());
			
		}else{
			this.selectedTipoDia = this.entradaVariavel.getTipoDia();
		}
	}


	public void calcValues(AjaxBehaviorEvent event) {
		IEntradaVariavel iEntradaVariavel = new EntradaVariavelBean();
		iEntradaVariavel.calcGastoTotal(entradaVariavel);
		iEntradaVariavel.calcGastoTotalRestante(entradaVariavel);
	}
	
	public void saveEntradaVariavel(AjaxBehaviorEvent event) {

		IEntradaVariavel iEntradaVariavel = new EntradaVariavelBean(); 
		try {
			iEntradaVariavel.saveEntradaVariavel(this.entradaVariavel, this.usuario, this.selectedTipoDia);
		} catch (ErrorMessageException eme) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, eme.getMessage(), "")); 
		}
		
	}



	public void onTipoDiaChange(AjaxBehaviorEvent event) {

		if(!this.selectedTipoDia.equals(TipoDia.DIA_SEMANA))
			this.entradaVariavel.setOcorrencia(0);
		else
			this.entradaVariavel.setOcorrencia(9);
	}
	
	//Getters & Setters
	public TipoDia[] getTiposDia() {
        return TipoDia.values();
    }
	
	public Status[] getStatus() {
        return Status.values();
    }
	
	public void setEntradaVariavel(EntradaVariavel entradaVariavel) {
		this.entradaVariavel = entradaVariavel;
	}

	public EntradaVariavel getEntradaVariavel() {
		return entradaVariavel;
	}



	public TipoDia getSelectedTipoDia() {
		return selectedTipoDia;
	}



	public void setSelectedTipoDia(TipoDia selectedTipoDia) {
		this.selectedTipoDia = selectedTipoDia;
	}



	public EntradaVariavelRepository getRepository() {
		return repository;
	}



	public void setRepository(EntradaVariavelRepository repository) {
		this.repository = repository;
	}

}
