package controller;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import business.ISaidaFixa;
import business.SaidaFixaBean;
import business.SaidaFixaRepository;
import business.TipoGastoRepository;
import data.SaidaFixa;
import data.SaidaFixa.TipoDia;
import data.TipoGasto;

@ManagedBean
@ViewScoped
public class SaidaFixaManager extends GenericManageBean{

	
	private SaidaFixaRepository repository;
	private SaidaFixa saidaFixa;
	private TipoDia selectedTipoDia;
	
	private List<TipoGasto> tiposGasto = new ArrayList<TipoGasto>();
	
	private Long selectTipoGastoId = new Long(0);

	public SaidaFixaManager() {
		System.out.println("Construtor SaidaFixaManager");
		this.repository = new SaidaFixaRepository();
		
		this.tiposGasto.addAll(new TipoGastoRepository().getAllTipoGasto(this.usuario.getFamilia()));
		
		String saidaFixaId  = (String)this.getRequestParameter("id");
		
		if(saidaFixaId != null){
			long id = 0;
			try{
				id = Long.parseLong(saidaFixaId);
			}catch(Exception e){}
			
			this.saidaFixa = this.repository.findSaidaFixa(id);
			
		}
		
		
		if(this.saidaFixa == null){
			this.saidaFixa = new SaidaFixa();
			this.saidaFixa.setNewRecord(true);
			this.selectTipoGastoId = new Long(1);
			Calendar todayDate = Calendar.getInstance();
			if(todayDate.get(Calendar.DST_OFFSET) > 0)
				todayDate.add(Calendar.HOUR_OF_DAY, -2);
			else
				todayDate.add(Calendar.HOUR_OF_DAY, -3);
			this.saidaFixa.setDataLancamento(todayDate.getTime());
			this.saidaFixa.setJan(true);
			this.saidaFixa.setFev(true);
			this.saidaFixa.setMar(true);
			this.saidaFixa.setAbr(true);
			this.saidaFixa.setMai(true);
			this.saidaFixa.setJun(true);
			this.saidaFixa.setJul(true);
			this.saidaFixa.setAgo(true);
			this.saidaFixa.setSet(true);
			this.saidaFixa.setOut(true);
			this.saidaFixa.setNov(true);
			this.saidaFixa.setDez(true);
			
		}else{
			if(this.saidaFixa.getTipoGasto() != null)
				this.selectTipoGastoId = this.saidaFixa.getTipoGasto().getId();
			else
				this.selectTipoGastoId = 0L;
			
			this.selectedTipoDia = this.saidaFixa.getTipoDia();
		}
	}


	
	public void saveSaidaFixa(AjaxBehaviorEvent event) {

		TipoGasto selectedTipoGasto =  new TipoGastoRepository().findTipoGasto(this.selectTipoGastoId);
		ISaidaFixa iSaidaFixa = new SaidaFixaBean(); 
		iSaidaFixa.saveSaidaFixa(this.saidaFixa, selectedTipoGasto, this.usuario, this.selectedTipoDia);
		
	}
	
	public void disableSaidaFixa(AjaxBehaviorEvent event) {
		TipoGasto selectedTipoGasto =  new TipoGastoRepository().findTipoGasto(this.selectTipoGastoId);
		ISaidaFixa iSaidaFixa = new SaidaFixaBean(); 
		iSaidaFixa.disableSaidaFixa(this.saidaFixa, selectedTipoGasto, this.usuario, this.selectedTipoDia);
		
	}
	

	public void onTipoDiaChange(AjaxBehaviorEvent event) {

		if(!this.selectedTipoDia.equals(TipoDia.DIA_SEMANA))
			this.saidaFixa.setOcorrencia(0);
		else
			this.saidaFixa.setOcorrencia(9);
	}
	
	//Getters & Setters
	public TipoDia[] getTiposDia() {
        return TipoDia.values();
    }
	
	public void setSaidaFixa(SaidaFixa saidaFixa) {
		this.saidaFixa = saidaFixa;
	}

	public SaidaFixa getSaidaFixa() {
		return saidaFixa;
	}



	public TipoDia getSelectedTipoDia() {
		return selectedTipoDia;
	}



	public void setSelectedTipoDia(TipoDia selectedTipoDia) {
		this.selectedTipoDia = selectedTipoDia;
	}



	public SaidaFixaRepository getRepository() {
		return repository;
	}



	public void setRepository(SaidaFixaRepository repository) {
		this.repository = repository;
	}



	public List<TipoGasto> getTiposGasto() {
		return tiposGasto;
	}



	public void setTiposGasto(List<TipoGasto> tiposGasto) {
		this.tiposGasto = tiposGasto;
	}



	public Long getSelectTipoGastoId() {
		return selectTipoGastoId;
	}



	public void setSelectTipoGastoId(Long selectTipoGastoId) {
		this.selectTipoGastoId = selectTipoGastoId;
	}

}
