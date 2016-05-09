package controller;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.AjaxBehaviorEvent;

import business.EntradaVariavelBean;
import business.MovimentacaoFinanceiraRepository;
import business.SaidaVariavelBean;
import business.TipoGastoRepository;
import data.MovimentacaoFinanceira;
import data.TipoGasto;

@ManagedBean
@ViewScoped
public class MovimentacaoFinanceiraManager extends GenericManageBean{

	
	private MovimentacaoFinanceiraRepository repository;
	private MovimentacaoFinanceira movimentacaoFinanceira;
	private List<TipoGasto> tiposGasto;
	private Long selectTipoGastoId = new Long(0);
	private HtmlInputText txtDataRealizada;
	private HtmlSelectOneMenu txtTipoGasto;
	
	

	public MovimentacaoFinanceiraManager() {
		System.out.println("Construtor MovimentacaoFinanceiraManager");
		this.repository = new MovimentacaoFinanceiraRepository();
		
		this.tiposGasto = new ArrayList<TipoGasto>();
		TipoGasto tg = new TipoGasto();
		tg.setId(0L);
		tg.setDescricao("");
		this.tiposGasto.add(tg);
		this.tiposGasto.addAll(new TipoGastoRepository().getAllTipoGasto(this.usuario.getFamilia()));
		Collections.sort(this.tiposGasto, TipoGasto.getComparator()); 
		
		String movimentacaoFinanceiraId  = (String)this.getRequestParameter("id");
		
		if(movimentacaoFinanceiraId != null){
			long id = 0;
			try{
				id = Long.parseLong(movimentacaoFinanceiraId);
			}catch(Exception e){}
			
			this.movimentacaoFinanceira = this.repository.findMovimentacaoFinanceira(id);
			
			
		}
		
	
		if(this.movimentacaoFinanceira == null){
			this.movimentacaoFinanceira = new MovimentacaoFinanceira();
			this.movimentacaoFinanceira.setNewRecord(true);
			this.movimentacaoFinanceira.setManual(true);
			this.movimentacaoFinanceira.setTipoMovimentacao(new Character('D'));
			this.movimentacaoFinanceira.setSituacao(new Character('P'));
			this.selectTipoGastoId = new Long(1);
			Calendar todayDate = Calendar.getInstance();
			if(todayDate.get(Calendar.DST_OFFSET) > 0)
				todayDate.add(Calendar.HOUR_OF_DAY, -2);
			else
				todayDate.add(Calendar.HOUR_OF_DAY, -3);
			
			this.movimentacaoFinanceira.setDataLancamento(todayDate.getTime());
		}else{
			if(this.movimentacaoFinanceira.getTipoGasto() != null)
				this.selectTipoGastoId = this.movimentacaoFinanceira.getTipoGasto().getId();
			else
				this.selectTipoGastoId = 0L;
			
		}
	}

	public void requireDataRealizada(AjaxBehaviorEvent event) {
		if(this.movimentacaoFinanceira.getSituacao().equals('R')){
			this.txtDataRealizada.setRequired(true);
			this.txtDataRealizada.setDisabled(false);
		}
		else{
			this.txtDataRealizada.setRequired(false);
			this.txtDataRealizada.setDisabled(true);
			this.movimentacaoFinanceira.setDataRealizada(null);
		}
		
		
	}
	
	public void enableTipoGastoField(AjaxBehaviorEvent event) {
		if(this.movimentacaoFinanceira.getTipoMovimentacao().equals('D')){
			this.txtTipoGasto.setRequired(true);
			this.txtTipoGasto.setDisabled(false);
			this.selectTipoGastoId = new Long(1);
		}
		else{
			this.txtTipoGasto.setRequired(false);
			this.txtTipoGasto.setDisabled(true);
			this.selectTipoGastoId = new Long(0);
		}
		
		
	}
	
	public void saveMovimentacaoFinanceira(AjaxBehaviorEvent event) {

		TipoGasto selectedTipoGasto =  new TipoGastoRepository().findTipoGasto(this.selectTipoGastoId);
		this.movimentacaoFinanceira.setUsuario(this.usuario);
		
		if(this.movimentacaoFinanceira.isNewRecord()){
			this.movimentacaoFinanceira.setTipoGasto(selectedTipoGasto);
			repository.saveMovimentacaoFinanceira(this.movimentacaoFinanceira);
		}
		else{
			this.movimentacaoFinanceira.setTipoGasto(selectedTipoGasto);
			
			if(this.movimentacaoFinanceira.getSaidaVariavel() != null){
				new SaidaVariavelBean().evaluateStatus(this.movimentacaoFinanceira.getSaidaVariavel());
				new SaidaVariavelBean().calcGastoTotalRestante(this.movimentacaoFinanceira.getSaidaVariavel());
			}
			
			if(this.movimentacaoFinanceira.getEntradaVariavel() != null){
				new EntradaVariavelBean().evaluateStatus(this.movimentacaoFinanceira.getEntradaVariavel());
				new EntradaVariavelBean().calcGastoTotalRestante(this.movimentacaoFinanceira.getEntradaVariavel());
			}
			
			repository.updateMovimentacaoFinanceira(this.movimentacaoFinanceira);
		}
	}

	
	//Getters & Setters
	public void setMovimentacaoFinanceira(MovimentacaoFinanceira movimentacaoFinanceira) {
		this.movimentacaoFinanceira = movimentacaoFinanceira;
	}

	public MovimentacaoFinanceira getMovimentacaoFinanceira() {
		return movimentacaoFinanceira;
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

	public HtmlInputText getTxtDataRealizada() {
		return txtDataRealizada;
	}

	public void setTxtDataRealizada(HtmlInputText txtDataRealizada) {
		this.txtDataRealizada = txtDataRealizada;
	}

	public HtmlSelectOneMenu gettxtTipoGasto() {
		return txtTipoGasto;
	}

	public void settxtTipoGasto(HtmlSelectOneMenu txtTipoGasto) {
		this.txtTipoGasto = txtTipoGasto;
	}

}
