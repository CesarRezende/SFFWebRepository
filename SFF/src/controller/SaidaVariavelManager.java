package controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import business.ErrorMessageException;
import business.ISaidaVariavel;
import business.SaidaVariavelBean;
import business.SaidaVariavelRepository;
import business.TipoGastoRepository;
import data.SaidaVariavel;
import data.SaidaVariavel.Status;
import data.SaidaVariavel.TipoDia;
import data.TipoGasto;

@ManagedBean
@ViewScoped
public class SaidaVariavelManager extends GenericManageBean {

	private SaidaVariavelRepository repository;
	private SaidaVariavel saidaVariavel;
	private TipoDia selectedTipoDia;
	private List<TipoGasto> tiposGasto = new ArrayList<TipoGasto>();

	private Long selectTipoGastoId = new Long(0);

	public SaidaVariavelManager() {
		System.out.println("Construtor SaidaVariavelManager");
		this.repository = new SaidaVariavelRepository();

		this.tiposGasto.addAll(new TipoGastoRepository()
				.getAllTipoGasto(this.usuario.getFamilia()));

		String saidaVariavelId = (String) this.getRequestParameter("id");

		if (saidaVariavelId != null) {
			long id = 0;
			try {
				id = Long.parseLong(saidaVariavelId);
			} catch (Exception e) {
			}

			this.saidaVariavel = this.repository.findSaidaVariavel(id);

		}

		if (this.saidaVariavel == null) {
			this.saidaVariavel = new SaidaVariavel();
			this.saidaVariavel.setNewRecord(true);
			this.selectTipoGastoId = new Long(1);
			this.saidaVariavel.setStatus(Status.ABERTO);
			Calendar todayDate = Calendar.getInstance();
			if (todayDate.get(Calendar.DST_OFFSET) > 0)
				todayDate.add(Calendar.HOUR_OF_DAY, -2);
			else
				todayDate.add(Calendar.HOUR_OF_DAY, -3);
			this.saidaVariavel.setDataLancamento(todayDate.getTime());

		} else {
			if (this.saidaVariavel.getTipoGasto() != null)
				this.selectTipoGastoId = this.saidaVariavel.getTipoGasto()
						.getId();
			else
				this.selectTipoGastoId = 0L;

			this.selectedTipoDia = this.saidaVariavel.getTipoDia();
		}
	}

	public void saveSaidaVariavel(AjaxBehaviorEvent event) {

		TipoGasto selectedTipoGasto = new TipoGastoRepository()
				.findTipoGasto(this.selectTipoGastoId);
		ISaidaVariavel iSaidaVariavel = new SaidaVariavelBean();
		try {
			iSaidaVariavel.saveSaidaVariavel(this.saidaVariavel,
					selectedTipoGasto, this.usuario, this.selectedTipoDia);
		} catch (ErrorMessageException eme) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, eme.getMessage(), ""));
		}

	}

	public void calcValues(AjaxBehaviorEvent event) {
		ISaidaVariavel iSaidaVariavel = new SaidaVariavelBean();
		iSaidaVariavel.calcGastoTotal(saidaVariavel);
		iSaidaVariavel.calcGastoTotalRestante(saidaVariavel);
	}

	public void onTipoDiaChange(AjaxBehaviorEvent event) {

		if (!this.selectedTipoDia.equals(TipoDia.DIA_SEMANA))
			this.saidaVariavel.setOcorrencia(0);
		else
			this.saidaVariavel.setOcorrencia(9);
	}

	// Getters & Setters
	public TipoDia[] getTiposDia() {
		return TipoDia.values();
	}

	public Status[] getStatus() {
		return Status.values();
	}

	public void setSaidaVariavel(SaidaVariavel saidaVariavel) {
		this.saidaVariavel = saidaVariavel;
	}

	public SaidaVariavel getSaidaVariavel() {
		return saidaVariavel;
	}

	public TipoDia getSelectedTipoDia() {
		return selectedTipoDia;
	}

	public void setSelectedTipoDia(TipoDia selectedTipoDia) {
		this.selectedTipoDia = selectedTipoDia;
	}

	public SaidaVariavelRepository getRepository() {
		return repository;
	}

	public void setRepository(SaidaVariavelRepository repository) {
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
