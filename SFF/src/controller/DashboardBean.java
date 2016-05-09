package controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.event.AjaxBehaviorEvent;

import org.json.JSONArray;

import business.MovimentacaoFinanceiraRepository;
import business.SFFUtil;
import data.AreaChart;
import data.MovimentacaoFinanceira;
import data.PieChart;

@ManagedBean
@ViewScoped
public class DashboardBean extends GenericManageBean {

	private MovimentacaoFinanceiraRepository repository;

	private String yearMonth = "";

	private HtmlInputText txtYearMonth;

	private Calendar initialDate;

	private Calendar finalDate;

	private Calendar today;

	private Calendar referenceDate;

	private Calendar previewMonthLastDay;

	private PieChart tipoGastoChart = new PieChart();

	private PieChart spendingsChart = new PieChart();

	private PieChart currentSpendingsChart = new PieChart();

	private AreaChart periodeChart = new AreaChart();

	private AreaChart tipoGastoPeriodeChart = new AreaChart();

	private double spendingsChartSpends = 0;

	private double spendingsChartGains = 0;

	private double currentSpendingsChartSpends = 0;

	private double currentSpendingsChartGains = 0;

	public DashboardBean() {
		System.out.println("Construtor DashboardBean");
		this.repository = new MovimentacaoFinanceiraRepository();
		this.renderGraphs();
	}

	public void changeYearMonth(AjaxBehaviorEvent event) {

		String value = (String) txtYearMonth.getValue();
		setSessionObject("YearMonthMF", value);
		this.renderGraphs();

	}

	private void renderGraphs() {
		this.yearMonth = (String) getSessionObject("YearMonthMF");
		this.today = Calendar.getInstance();
		this.initialDate = Calendar.getInstance();
		this.referenceDate = Calendar.getInstance();
		this.finalDate = Calendar.getInstance();
		this.previewMonthLastDay = Calendar.getInstance();

		if (this.yearMonth == null || this.yearMonth.equals("")) {
			this.yearMonth = String.format("%02d",
					(today.get(Calendar.MONTH) + 1))
					+ "/"
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

		this.renderTipoGastoChart();
		this.renderSpendingsChart();
		this.renderCurrentSpendingsChart();
		renderPeriodeChart();
		renderTipoGastoPeriodeChart();
	}

	private void renderTipoGastoChart() {
		this.tipoGastoChart.setData(this.repository.TipoGastoChart(initialDate,
				finalDate, today));
		this.tipoGastoChart.setTitle("Tipo de Gasto");

	}

	private void renderSpendingsChart() {
		String spendingsChartData = "[]";
		double previewsBalance = this.repository.getPreviewsBalance(
				previewMonthLastDay, finalDate, today);
		double monthsSpendings = this.repository.getMonthsSpendings(
				initialDate, finalDate, today);
		double monthsGains = this.repository.getMonthsGains(initialDate,
				finalDate, today);
		double balance = monthsGains + previewsBalance - monthsSpendings;
		double spendingsPerc = round((monthsSpendings
				/ (monthsGains + previewsBalance) * 100), 2);

		if (balance >= 0) {
			this.spendingsChartSpends = monthsSpendings;
			this.spendingsChartGains = monthsGains + previewsBalance;

			spendingsChartData = "[[\"Gastos\", "
					+ Double.toString(spendingsPerc) + "],[\"Ganhos\", "
					+ Double.toString(100.00 - spendingsPerc) + "]]";

			this.spendingsChart.setData(spendingsChartData);
			this.spendingsChart.setTitle("Crédito Previsto Restante");
			this.spendingsChart
					.setSlices("{0: {color: '#dc3912'}, 1: {color: '#3366cc'}}");

		} else {

			this.spendingsChartSpends = round(balance, 2);
			spendingsPerc = round(
					(monthsSpendings / (monthsGains + previewsBalance)), 2);
			long iPart;

			iPart = (long) spendingsPerc;
			spendingsPerc = (spendingsPerc - iPart) * 100;

			spendingsChartData = "[[\"Endivid.\", "
					+ Double.toString(spendingsPerc)
					+ "],[\"Ganhos já gastos\", "
					+ Double.toString(100.00 - spendingsPerc) + "]]";

			this.spendingsChart.setData(spendingsChartData);
			this.spendingsChart.setTitle("Nivel de Endividamento Previsto");
			this.spendingsChart
					.setSlices("{0: {color: '#dc3912'}, 1: {color: '#2f0404'}}");
		}

	}

	private void renderCurrentSpendingsChart() {
		String CurrentSpendingsChartData = "[]";
		double previewsBalance = this.repository.getPreviewsBalance(
				previewMonthLastDay, finalDate, today);
		double monthsSpendings = this.repository.getCurrentMonthsSpendings(
				initialDate, finalDate, today);
		double monthsGains = this.repository.getCurrentMonthsGains(initialDate,
				finalDate, today);
		double balance = monthsGains + previewsBalance - monthsSpendings;
		double currentSpendingsPerc = round((monthsSpendings
				/ (monthsGains + previewsBalance) * 100), 2);

		if (balance >= 0) {
			this.currentSpendingsChartSpends = monthsSpendings;
			this.currentSpendingsChartGains = monthsGains + previewsBalance;

			CurrentSpendingsChartData = "[[\"Gastos\", "
					+ Double.toString(currentSpendingsPerc) + "],[\"Ganhos\", "
					+ Double.toString(100.00 - currentSpendingsPerc) + "]]";

			this.currentSpendingsChart.setData(CurrentSpendingsChartData);
			this.currentSpendingsChart.setTitle("Crédito Realizado Restante");
			this.currentSpendingsChart
					.setSlices("{0: {color: '#dc3912'}, 1: {color: '#3366cc'}}");

		} else {
			this.currentSpendingsChartSpends = round(balance, 2);
			currentSpendingsPerc = round(
					(monthsSpendings / (monthsGains + previewsBalance)), 2);
			long iPart;

			iPart = (long) currentSpendingsPerc;
			currentSpendingsPerc = (currentSpendingsPerc - iPart) * 100;

			CurrentSpendingsChartData = "[[\"Endivid.\", "
					+ Double.toString(currentSpendingsPerc)
					+ "],[\"Ganhos já gastos\", "
					+ Double.toString(100.00 - currentSpendingsPerc) + "]]";

			this.currentSpendingsChart.setData(CurrentSpendingsChartData);
			this.currentSpendingsChart
					.setTitle("Nivel de Endividamento Realizado");
			this.currentSpendingsChart
					.setSlices("{0: {color: '#dc3912'}, 1: {color: '#2f0404'}}");

		}

	}

	private void renderPeriodeChart() {
		int monthsRange = 10;
		double previewsBalance = 0;
		double totalSpendings = 0;
		double totalGains = 0;
		double totalEndradasVariaveis = 0;
		double totalEntradasFixas = 0;
		double totalSaidasVariaveis = 0;
		double totalSaidasFixas = 0;
		String chartData = "[\n['Mês','Entradas Variáveis', 'Entradas Fixas', 'Entradas Totais','Saídas Variáveis', 'Saídas Fixas', 'Saídas Totais' ],";

		Calendar referenceDateTemp = (Calendar) referenceDate.clone();
		referenceDateTemp.add(Calendar.MONTH, -2);
		Calendar initialDateTemp = (Calendar) referenceDate.clone();
		Calendar previewMonthLastDayTemp = (Calendar) referenceDate.clone();
		Calendar finalDateTemp = (Calendar) referenceDate.clone();

		for (int i = 0; i <= monthsRange; i++) {
			totalSpendings = 0;
			totalGains = 0;
			totalEndradasVariaveis = 0;
			totalEntradasFixas = 0;
			totalSaidasVariaveis = 0;
			totalSaidasFixas = 0;

			initialDateTemp = SFFUtil.createInitialDate(
					referenceDateTemp.get(Calendar.YEAR),
					referenceDateTemp.get(Calendar.MONTH) + 1);

			finalDateTemp = SFFUtil.createFinalDate(
					referenceDateTemp.get(Calendar.YEAR),
					referenceDateTemp.get(Calendar.MONTH) + 1);

			previewMonthLastDayTemp = SFFUtil.createPreviewMonthLastDay(
					referenceDateTemp.get(Calendar.YEAR),
					referenceDateTemp.get(Calendar.MONTH) + 1);

			chartData += ("\n['" + SFFUtil.getMonthDescr(
					(int) referenceDateTemp.get(Calendar.MONTH), true));

			chartData += "'";

			previewsBalance = this.repository.getPreviewsBalance(
					previewMonthLastDayTemp, finalDateTemp, today);
			Object[] monthsSpendings = this.repository
					.getMonthsSpendingsGroupByType(initialDateTemp,
							finalDateTemp, today);
			Object[] monthsGains = this.repository.getMonthsGainsGroupByType(
					initialDateTemp, finalDateTemp, today);

			if (previewsBalance < 0)
				totalSpendings += previewsBalance;
			else
				totalGains += previewsBalance;

			if (monthsGains[0] != null) {
				totalGains += ((Double) monthsGains[0]);
				totalEndradasVariaveis += ((Double) monthsGains[0]);
			}

			if (monthsGains[1] != null) {
				totalGains += ((Double) monthsGains[1]);
				totalEntradasFixas += ((Double) monthsGains[1]);
			}

			if (monthsGains[2] != null) {
				totalGains += ((Double) monthsGains[2]);
			}

			if (monthsSpendings[0] != null) {
				totalSpendings += ((Double) monthsSpendings[0]);
				totalSaidasVariaveis += ((Double) monthsSpendings[0]);
			}

			if (monthsSpendings[1] != null) {
				totalSpendings += ((Double) monthsSpendings[1]);
				totalSaidasFixas += ((Double) monthsSpendings[1]);
			}

			if (monthsSpendings[1] != null) {
				totalSpendings += ((Double) monthsSpendings[2]);
			}

			chartData += "," + round(totalEndradasVariaveis, 2);
			chartData += "," + round(totalEntradasFixas, 2);
			chartData += "," + round(totalGains, 2);

			chartData += "," + round(totalSaidasVariaveis, 2);
			chartData += "," + round(totalSaidasFixas, 2);
			chartData += "," + round(totalSpendings, 2);

			chartData += "],";

			referenceDateTemp.add(Calendar.MONTH, 1);
		}
		chartData += "\n]";

		this.periodeChart.setData(chartData);
		this.periodeChart.setTitle("Evolução de Ganhos e Gastos");

	}

	private void renderTipoGastoPeriodeChart() {
		int monthsRange = 7;
		Calendar referenceDateTemp = (Calendar) referenceDate.clone();
		referenceDateTemp.add(Calendar.MONTH, -5);
		Calendar initialDateTemp = (Calendar) referenceDate.clone();
		Calendar finalDateTemp = (Calendar) referenceDate.clone();
		ArrayList tipoGastoPeriodeList = new ArrayList();
		ArrayList tipoGastoList = new ArrayList(monthsRange);
		ArrayList chartList = new ArrayList();
		ArrayList<String> monthList = new ArrayList<String>(monthsRange);

		for (int i = 0; i <= monthsRange; i++) {

			initialDateTemp = SFFUtil.createInitialDate(
					referenceDateTemp.get(Calendar.YEAR),
					referenceDateTemp.get(Calendar.MONTH) + 1);

			finalDateTemp = SFFUtil.createFinalDate(
					referenceDateTemp.get(Calendar.YEAR),
					referenceDateTemp.get(Calendar.MONTH) + 1);

			ArrayList list = this.repository.tipoGastoPeriodeChart(initialDateTemp,
					finalDateTemp, today);

			tipoGastoPeriodeList.add(list);
			
			monthList.add(SFFUtil.getMonthDescr(referenceDateTemp.get(Calendar.MONTH), true));
			referenceDateTemp.add(Calendar.MONTH, 1);
		}

		if (tipoGastoPeriodeList.size() > 0) {

			for (Object list : tipoGastoPeriodeList) {
				List tipoGastoPeriodeMonthList = (List) list;

				for (Object objectItem : tipoGastoPeriodeMonthList) {
					Object[] item = (Object[]) objectItem;

					if (tipoGastoList.contains(item[0]))
						continue;
					else
						tipoGastoList.add(item[0]);

				}
			}
			List<Object> chartInnerList = new ArrayList<Object>();
			if(tipoGastoList.size() > 0){
				
			chartInnerList.add("Mês");
			chartInnerList.addAll(tipoGastoList);
			chartList.add(chartInnerList);
			}
			
			int count = 0;
			for (Object list : tipoGastoPeriodeList) {
				List tipoGastoPeriodeMonthList = (List) list;
				chartInnerList = inicializeList(tipoGastoList.size() + 1);

				chartInnerList.set(0, monthList.get(count));
				for (Object objectItem : tipoGastoPeriodeMonthList) {
					Object[] item = (Object[]) objectItem;

					int index = tipoGastoList.indexOf(item[0]);
					chartInnerList.set(index + 1, item[1]);

				}
				
				for (int i = 0; i < chartInnerList.size(); i++) {
					if(chartInnerList.get(i) == null)
						chartInnerList.set(i, 0.0);
				}
				
				
				chartList.add(chartInnerList);
				count++;
			}
		}

		JSONArray mJSONArray = new JSONArray(chartList);
		this.tipoGastoPeriodeChart.setData(mJSONArray.toString());

		this.tipoGastoPeriodeChart.setTitle("Evolução de Tipo de Gasto");

	}
	
	private List<Object> inicializeList(int size){
		List<Object> list = new ArrayList<Object>(size);
		for(int i = 0; i < size; i++)
			list.add(0.0);
		
		return list;
	}
	

	private static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public List<MovimentacaoFinanceira> getAllMovimentacaoFinanceira() {
		return this.repository.getAllMovimentacaoFinanceira();

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

	public Calendar getpreviewMonthLastDay() {
		return previewMonthLastDay;
	}

	public void setpreviewMonthLastDay(Calendar previewMonthLastDay) {
		this.previewMonthLastDay = previewMonthLastDay;
	}

	public PieChart getTipoGastoChart() {
		return tipoGastoChart;
	}

	public void setTipoGastoChart(PieChart tipoGastoChart) {
		this.tipoGastoChart = tipoGastoChart;
	}

	public PieChart getSpendingsChart() {
		return spendingsChart;
	}

	public void setSpendingsChart(PieChart spendingsChart) {
		this.spendingsChart = spendingsChart;
	}

	public PieChart getCurrentSpendingsChart() {
		return currentSpendingsChart;
	}

	public void setCurrentSpendingsChart(PieChart currentSpendingsChart) {
		this.currentSpendingsChart = currentSpendingsChart;
	}

	public Calendar getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(Calendar referenceDate) {
		this.referenceDate = referenceDate;
	}

	public double getSpendingsChartSpends() {
		return spendingsChartSpends;
	}

	public void setSpendingsChartSpends(double spendingsChartSpends) {
		this.spendingsChartSpends = spendingsChartSpends;
	}

	public double getSpendingsChartGains() {
		return spendingsChartGains;
	}

	public void setSpendingsChartGains(double spendingsChartGains) {
		this.spendingsChartGains = spendingsChartGains;
	}

	public double getCurrentSpendingsChartSpends() {
		return currentSpendingsChartSpends;
	}

	public void setCurrentSpendingsChartSpends(
			double currentSpendingsChartSpends) {
		this.currentSpendingsChartSpends = currentSpendingsChartSpends;
	}

	public double getCurrentSpendingsChartGains() {
		return currentSpendingsChartGains;
	}

	public void setCurrentSpendingsChartGains(double currentSpendingsChartGains) {
		this.currentSpendingsChartGains = currentSpendingsChartGains;
	}

	public AreaChart getPeriodeChart() {
		return periodeChart;
	}

	public void setPeriodeChart(AreaChart periodeChart) {
		this.periodeChart = periodeChart;
	}

	public AreaChart getTipoGastoPeriodeChart() {
		return tipoGastoPeriodeChart;
	}

	public void setTipoGastoPeriodeChart(AreaChart tipoGastoPeriodeChart) {
		this.tipoGastoPeriodeChart = tipoGastoPeriodeChart;
	}

}
