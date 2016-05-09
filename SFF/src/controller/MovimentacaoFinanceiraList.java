package controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.event.AjaxBehaviorEvent;

import business.MovimentacaoFinanceiraRepository;
import business.SFFUtil;
import data.MovimentacaoFinanceira;

@ManagedBean
@ViewScoped
public class MovimentacaoFinanceiraList extends GenericManageBean {

	private MovimentacaoFinanceiraRepository repository;

	private String yearMonth = "";

	private HtmlInputText txtYearMonth;
	
	private double balance = 0;
	
	private String balanceType = "C";

	private List<MovimentacaoFinanceira> movimentacoesFinanceira = new ArrayList<MovimentacaoFinanceira>();

	private Calendar initialDate;

	private Calendar finalDate;

	private Calendar today;
	
	private Calendar previewMonthLastDay;
	
	private Calendar referenceDate;
	
	private String alertMsg = "";

	public MovimentacaoFinanceiraList() {
		System.out.println("Construtor MovimentacaoFinanceiraList");
		this.repository = new MovimentacaoFinanceiraRepository();
		this.getMovimentacaoFinanceiraFromMonth();
	}

	public void insert(AjaxBehaviorEvent event) {
	}

	public void edit(AjaxBehaviorEvent event) {

	}

	public void changeYearMonth(AjaxBehaviorEvent event) {

		String value = (String) txtYearMonth.getValue();
		setSessionObject("YearMonthMF", value);
		this.getMovimentacaoFinanceiraFromMonth();

	}


	private void getMovimentacaoFinanceiraFromMonth() {
		this.yearMonth = (String) getSessionObject("YearMonthMF");
		this.today = Calendar.getInstance();
		this.referenceDate = Calendar.getInstance();
		this.initialDate = Calendar.getInstance();
		this.finalDate = Calendar.getInstance();
		this.previewMonthLastDay = Calendar.getInstance();

		if (this.yearMonth == null || this.yearMonth.equals("")) {
			this.yearMonth = String.format("%02d",(today.get(Calendar.MONTH) + 1)) + "/"
					+ today.get(Calendar.YEAR);
		}
		String[] yearMonthArray = this.yearMonth.split("/");

		this.referenceDate = SFFUtil.createReferenceDate(
				Integer.parseInt(yearMonthArray[1]),
				Integer.parseInt(yearMonthArray[0]));

		this.initialDate = SFFUtil.createInitialDate(
				Integer.parseInt(yearMonthArray[1]),
				Integer.parseInt(yearMonthArray[0]));

		this.finalDate = SFFUtil.createFinalDate(
				Integer.parseInt(yearMonthArray[1]),
				Integer.parseInt(yearMonthArray[0]));

		this.previewMonthLastDay = SFFUtil.createPreviewMonthLastDay(
				Integer.parseInt(yearMonthArray[1]),
				Integer.parseInt(yearMonthArray[0]));
		

		this.setMovimentacoesFinanceira(this.repository
				.getListMovimentacaoFinanceira(initialDate, finalDate,
						today));
		
		this.setBalance(this.repository.getPreviewsBalance(previewMonthLastDay,finalDate, today));
		
		if(this.balance >= 0)
			this.balanceType = "C";
		else{
			this.balanceType = "D";
			this.balance *= -1;
		}

	}

	
	public List<MovimentacaoFinanceira> getAllMovimentacaoFinanceira() {
		return this.repository.getAllMovimentacaoFinanceira();

	}

	public void setMovimentacoesFinanceira(
			List<MovimentacaoFinanceira> movimentacoesFinanceira) {
		this.movimentacoesFinanceira = movimentacoesFinanceira;
	}

	public List<MovimentacaoFinanceira> getMovimentacoesFinanceira() {
		return movimentacoesFinanceira;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public HtmlInputText getTxtYearMonth() {
		return txtYearMonth;
	}

	public void setTxtYearMonth(HtmlInputText txtYearMonth) {
		this.txtYearMonth = txtYearMonth;
	}

	public Calendar getinitialDate() {
		return initialDate;
	}

	public void setinitialDate(Calendar initialDate) {
		this.initialDate = initialDate;
	}

	public Calendar getfinalDate() {
		return finalDate;
	}

	public void setfinalDate(Calendar finalDate) {
		this.finalDate = finalDate;
	}

	public Calendar getToday() {
		return today;
	}

	public void setToday(Calendar today) {
		this.today = today;
	}


	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Calendar getPreviewMonthLastDay() {
		return previewMonthLastDay;
	}

	public void setPreviewMonthLastDay(Calendar previewMonthLastDay) {
		this.previewMonthLastDay = previewMonthLastDay;
	}

	public String getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}

	public Calendar getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(Calendar referenceDate) {
		this.referenceDate = referenceDate;
	}

	public String getAlertMsg() {
		return alertMsg;
	}

	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}

}
