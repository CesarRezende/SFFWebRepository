package controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import data.EntradaFixa;
import data.EntradaVariavel;
import data.MovimentacaoFinanceira;
import data.SaidaFixa;
import data.SaidaVariavel;
import data.TipoGasto;
import data.Usuario;
import business.EntradaFixaRepository;
import business.EntradaVariavelRepository;
import business.FamiliaRepository;
import business.GenericRepository;
import business.MovimentacaoFinanceiraRepository;
import business.SFFUtil;
import business.SaidaFixaRepository;
import business.SaidaVariavelRepository;
import business.TipoGastoRepository;
import business.UsuarioRepository;

@WebService(endpointInterface = "controller.WebServiceInterface")
@ManagedBean
public class WebServiceImpl implements WebServiceInterface {

	@Resource
	private WebServiceContext context;

	public WebServiceImpl() {
		System.out.println("Construtor WebServiceImpl");
	}

	@Override
	public String printMessage(@WebParam(name = "name") String name) {
		return "Hello " + name + "!";
	}

	@Override
	public String removeTipoGasto(Long id) {
		String returnMsg = "success";

		HttpServletRequest request = (HttpServletRequest) context
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);

		TipoGastoRepository tipoGatoRepository = new TipoGastoRepository(
				request);

		if (id > 0) {
			if (!tipoGatoRepository.hasDependences(id, true))
				tipoGatoRepository.removeTipoGasto(tipoGatoRepository
						.findTipoGasto(id));
			else
				returnMsg = "Não é possivel apagar, tipo de gasto contem dependencias";

		}

		return returnMsg;

	}

	@Override
	public String removeFamilia(Long id) {
		String returnMsg = "success";

		HttpServletRequest request = (HttpServletRequest) context
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);

		FamiliaRepository tipoGatoRepository = new FamiliaRepository(request);

		if (id > 0) {
			if (!tipoGatoRepository.hasDependences(id, true))
				tipoGatoRepository.removeFamilia(tipoGatoRepository
						.findFamilia(id));
			else
				returnMsg = "Não é possivel apagar, familia contem dependencias";

		}

		return returnMsg;

	}

	@Override
	public String removeUsuario(Long id) {
		String returnMsg = "success";

		HttpServletRequest request = (HttpServletRequest) context
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);

		UsuarioRepository tipoGatoRepository = new UsuarioRepository(request);

		if (id > 0) {
			if (!tipoGatoRepository.hasDependences(id, true))
				tipoGatoRepository.removeUsuario(tipoGatoRepository
						.findUsuario(id));
			else
				returnMsg = "Não é possivel apagar, usuario contem dependencias";

		}

		return returnMsg;

	}

	private String removeMovFinanc(Long id,
			MovimentacaoFinanceiraRepository movFinancRepository) {
		String returnMsg = "success";

		MovimentacaoFinanceira movimentFinanc = movFinancRepository
				.findMovimentacaoFinanceira(id);

		if (movimentFinanc.getSaidaVariavel() == null
				&& movimentFinanc.getEntradaVariavel() == null)
			movFinancRepository.removeMovimentacaoFinanceira(movimentFinanc);
		else
			returnMsg = "Não é possivel apagar, movimentação contem dependencias";

		return returnMsg;

	}

	@Override
	public String removeMovFinanc(Long id) {
		HttpServletRequest request = (HttpServletRequest) context
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);

		MovimentacaoFinanceiraRepository movFinancRepository = new MovimentacaoFinanceiraRepository(
				request);

		return removeMovFinanc(id, movFinancRepository);
	}

	@Override
	public String removeMovFinancExternal(String userId, String password,
			String id) {

		HttpServletRequest request = (HttpServletRequest) context
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);

		MovimentacaoFinanceiraRepository movFinancRepository = new MovimentacaoFinanceiraRepository(
				request);

		if (!putUsrIntoRepository(request, movFinancRepository, userId,
				password)) {
			return "false|Usuario não autenticado";
		}
		String returnValue = removeMovFinanc(Long.valueOf(id),
				movFinancRepository);
		if (returnValue.equals("success"))
			return "true|" + returnValue;
		else
			return "false|" + returnValue;
	}

	@Override
	public String removeSaidaFixa(Long id) {
		String returnMsg = "success";

		HttpServletRequest request = (HttpServletRequest) context
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);

		SaidaFixaRepository saidaFixaRepository = new SaidaFixaRepository(
				request);
		SaidaFixa saidaFixa = saidaFixaRepository.findSaidaFixa(id);
		if (id > 0) {
			if (!saidaFixaRepository.hasDependences(saidaFixa))
				saidaFixaRepository.removeSaidaFixa(saidaFixa);
			else
				returnMsg = "Não é possivel apagar, Saida Fixa contem dependencias.\n Se não for utilizar essa saida fixa novamente desative a mesma.";

		}

		return returnMsg;

	}

	@Override
	public String removeEntradaFixa(Long id) {
		String returnMsg = "success";

		HttpServletRequest request = (HttpServletRequest) context
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);

		EntradaFixaRepository entradaFixaRepository = new EntradaFixaRepository(
				request);
		EntradaFixa entradaFixa = entradaFixaRepository.findEntradaFixa(id);
		if (id > 0) {
			if (!entradaFixaRepository.hasDependences(entradaFixa))
				entradaFixaRepository.removeEntradaFixa(entradaFixa);
			else
				returnMsg = "Não é possivel apagar, Entrada Fixa contem dependencias.\n Se não for utilizar essa entrada fixa novamente desative a mesma.";

		}

		return returnMsg;

	}

	@Override
	public String removeSaidaVariavel(Long id) {
		String returnMsg = "success";

		HttpServletRequest request = (HttpServletRequest) context
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);

		SaidaVariavelRepository saidaVariavelRepository = new SaidaVariavelRepository(
				request);
		SaidaVariavel saidaVariavel = saidaVariavelRepository
				.findSaidaVariavel(id);
		if (id > 0) {
			if (!saidaVariavelRepository.hasDependences(saidaVariavel))
				saidaVariavelRepository.removeSaidaVariavel(saidaVariavel);
			else
				returnMsg = "Não é possivel apagar, Saida Variavel contem dependencias.";

		}

		return returnMsg;

	}

	@Override
	public String removeEntradaVariavel(Long id) {
		String returnMsg = "success";

		HttpServletRequest request = (HttpServletRequest) context
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);

		EntradaVariavelRepository entradaVariavelRepository = new EntradaVariavelRepository(
				request);
		EntradaVariavel entradaVariavel = entradaVariavelRepository
				.findEntradaVariavel(id);
		if (id > 0) {
			if (!entradaVariavelRepository.hasDependences(entradaVariavel))
				entradaVariavelRepository
						.removeEntradaVariavel(entradaVariavel);
			else
				returnMsg = "Não é possivel apagar, Entrada Variavel contem dependencias.";

		}

		return returnMsg;

	}

	@Override
	public boolean hasConnection() {

		return true;
	}

	private boolean putUsrIntoRepository(HttpServletRequest request,
			GenericRepository repository, String userId, String password) {

		Usuario usr = new UsuarioRepository(request).findUsuario(userId);

		if (usr != null && validateUser(usr, password)) {
			repository.setUsuario(usr);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getMovimentacaoFinanceiraFromMonth(String yearMonth,
			String userId, String password) {

		try {

			String yearMonthStr = yearMonth;
			String returnValue = "{}";
			java.util.List<MovimentacaoFinanceira> movFinacList = null;

			Calendar today = Calendar.getInstance();
			Calendar initialDate = Calendar.getInstance();
			Calendar finalDate = Calendar.getInstance();

			HttpServletRequest request = (HttpServletRequest) context
					.getMessageContext().get(MessageContext.SERVLET_REQUEST);

			MovimentacaoFinanceiraRepository repository = new MovimentacaoFinanceiraRepository(
					request);

			if (!putUsrIntoRepository(request, repository, userId, password)) {
				return "false|Usuario não autenticado";
			}

			String[] yearMonthArray = new String[] {
					yearMonthStr.substring(4, 6), yearMonthStr.substring(0, 4) };

			initialDate = SFFUtil.createInitialDate(
					Integer.parseInt(yearMonthArray[1]),
					Integer.parseInt(yearMonthArray[0]));

			finalDate = SFFUtil.createFinalDate(
					Integer.parseInt(yearMonthArray[1]),
					Integer.parseInt(yearMonthArray[0]));

			movFinacList = repository.getListMovimentacaoFinanceira(
					initialDate, finalDate, today);

			if (movFinacList != null)
				returnValue = SFFUtil.getJsonObjectList(movFinacList,
						MovimentacaoFinanceira.class, true);

			return "true|" + returnValue;
		} catch (Exception e) {
			return "false|Erro Interno no Servidor";
		}
	}

	private boolean validateUser(Usuario usr, String password) {
		boolean validUser = false;

		validUser = (usr != null && usr.getSenha().equals(
				SFFUtil.encodeSenha(password)));

		return validUser;
	}

	@Override
	public boolean validateUser(String userId, String password) {
		boolean validUser = false;

		HttpServletRequest request = (HttpServletRequest) context
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);

		Usuario usr = new UsuarioRepository(request).findUsuario(userId);

		validUser = (usr != null && usr.getSenha().equals(
				SFFUtil.encodeSenha(password)));

		return validUser;
	}

	@Override
	public String getAllTipoGasto(String userId, String password) {
		try {
			String returnValue = "{}";

			java.util.List<TipoGasto> tipoGastoList = null;

			HttpServletRequest request = (HttpServletRequest) context
					.getMessageContext().get(MessageContext.SERVLET_REQUEST);

			TipoGastoRepository repository = new TipoGastoRepository(request);

			if (!putUsrIntoRepository(request, repository, userId, password)) {
				return "false|Usuario não autenticado";
			}

			tipoGastoList = repository.getAllTipoGasto(repository.getUsuario()
					.getFamilia());

			if (tipoGastoList != null)
				returnValue = SFFUtil.getJsonObjectList(tipoGastoList,
						TipoGasto.class, false);

			return "true|" + returnValue;
		} catch (Exception e) {
			return "false|Erro Interno no Servidor";
		}
	}

	@Override
	public String editMovimentacaoFinanceira(String userId, String password,
			String id, String descricao, String tipoMovimentacao, String valor,
			String juros, String multa, String desconto, String situacao,
			String dataPrevista, String dataRealizada, String tipoGastoId) {

		try {
			String returnValue = "sucesso";

			HttpServletRequest request = (HttpServletRequest) context
					.getMessageContext().get(MessageContext.SERVLET_REQUEST);

			MovimentacaoFinanceiraRepository movFianncRepository = new MovimentacaoFinanceiraRepository(
					request);

			if (!putUsrIntoRepository(request, movFianncRepository, userId,
					password)) {
				return "false|Usuario não autenticado";
			}

			MovimentacaoFinanceira movFiananc = movFianncRepository
					.findMovimentacaoFinanceira(Long.parseLong(id));
			movFiananc.setDescricao(descricao);
			movFiananc.setTipoMovimentacao(tipoMovimentacao.charAt(0));

			movFiananc.setValor(SFFUtil.parseDouble(valor));
			movFiananc.setJuros(SFFUtil.parseDouble(juros));
			movFiananc.setMulta(SFFUtil.parseDouble(multa));
			movFiananc.setDesconto(SFFUtil.parseDouble(desconto));

			movFiananc.setSituacao(situacao.charAt(0));

			Calendar dataPrev = Calendar.getInstance();
			dataPrev.setTime(SFFUtil.parseDate(dataPrevista,
					SFFUtil.DateFormat.MEDIUM));
			if (dataPrev.get(Calendar.DST_OFFSET) > 0)
				dataPrev.add(Calendar.HOUR_OF_DAY, -2);
			else
				dataPrev.add(Calendar.HOUR_OF_DAY, -3);
			movFiananc.setDataPrevista(dataPrev.getTime());

			if (dataRealizada != null && !dataRealizada.equals("")
					&& !dataRealizada.equals("Sem Data")) {

				Calendar dataRealz = Calendar.getInstance();
				dataRealz.setTime(SFFUtil.parseDate(dataRealizada,
						SFFUtil.DateFormat.MEDIUM));
				if (dataRealz.get(Calendar.DST_OFFSET) > 0)
					dataRealz.add(Calendar.HOUR_OF_DAY, -2);
				else
					dataRealz.add(Calendar.HOUR_OF_DAY, -3);

				movFiananc.setDataRealizada(dataRealz.getTime());
			} else {
				movFiananc.setDataRealizada(null);
			}

			if (movFiananc.getTipoMovimentacao().equals('D')) {

				TipoGastoRepository tipoGastoRepository = new TipoGastoRepository(
						request);

				if (!putUsrIntoRepository(request, tipoGastoRepository, userId,
						password)) {
					return "false|Usuario não autenticado";
				}

				TipoGasto tipoGasto = tipoGastoRepository.findTipoGasto(Long
						.parseLong(tipoGastoId));

				movFiananc.setTipoGasto(tipoGasto);
			} else {
				movFiananc.setTipoGasto(null);
			}

			movFianncRepository.updateMovimentacaoFinanceira(movFiananc, request);

			return "true|" + returnValue;
		} catch (Exception e) {
			return "false|Erro Interno no Servidor";
		}

	}

	public String insertMovimentacaoFinanceira(String userId, String password,
			String id, String descricao, String tipoMovimentacao, String valor,
			String juros, String multa, String desconto, String situacao,
			String dataPrevista, String dataRealizada, String tipoGastoId) {

		try {
			String returnValue = "sucesso";

			HttpServletRequest request = (HttpServletRequest) context
					.getMessageContext().get(MessageContext.SERVLET_REQUEST);

			MovimentacaoFinanceiraRepository movFinancRepository = new MovimentacaoFinanceiraRepository(
					request);

			if (!putUsrIntoRepository(request, movFinancRepository, userId,
					password)) {
				return "false|Usuario não autenticado";
			}

			MovimentacaoFinanceira movFiananc = new MovimentacaoFinanceira();
			movFiananc.setManual(true);
			movFiananc.setDataLancamento(Calendar.getInstance().getTime());

			movFiananc.setDescricao(descricao);
			movFiananc.setTipoMovimentacao(tipoMovimentacao.charAt(0));

			movFiananc.setValor(SFFUtil.parseDouble(valor));
			movFiananc.setJuros(SFFUtil.parseDouble(juros));
			movFiananc.setMulta(SFFUtil.parseDouble(multa));
			movFiananc.setDesconto(SFFUtil.parseDouble(desconto));
			movFiananc.setUsuario(movFinancRepository.getUsuario());

			movFiananc.setSituacao(situacao.charAt(0));

			Calendar dataPrev = Calendar.getInstance();
			dataPrev.setTime(SFFUtil.parseDate(dataPrevista,
					SFFUtil.DateFormat.MEDIUM));
			if (dataPrev.get(Calendar.DST_OFFSET) > 0)
				dataPrev.add(Calendar.HOUR_OF_DAY, -2);
			else
				dataPrev.add(Calendar.HOUR_OF_DAY, -3);
			movFiananc.setDataPrevista(dataPrev.getTime());

			if (dataRealizada != null && !dataRealizada.equals("")
					&& !dataRealizada.equals("Sem Data")) {

				Calendar dataRealz = Calendar.getInstance();
				dataRealz.setTime(SFFUtil.parseDate(dataRealizada,
						SFFUtil.DateFormat.MEDIUM));
				if (dataRealz.get(Calendar.DST_OFFSET) > 0)
					dataRealz.add(Calendar.HOUR_OF_DAY, -2);
				else
					dataRealz.add(Calendar.HOUR_OF_DAY, -3);

				movFiananc.setDataRealizada(dataRealz.getTime());
			} else {
				movFiananc.setDataRealizada(null);
			}

			if (movFiananc.getTipoMovimentacao().equals('D')) {

				TipoGastoRepository tipoGastoRepository = new TipoGastoRepository(
						request);

				if (!putUsrIntoRepository(request, tipoGastoRepository, userId,
						password)) {
					return "false|Usuario não autenticado";
				}

				TipoGasto tipoGasto = tipoGastoRepository.findTipoGasto(Long
						.parseLong(tipoGastoId));

				movFiananc.setTipoGasto(tipoGasto);
			} else {
				movFiananc.setTipoGasto(null);
			}

			movFinancRepository.saveMovimentacaoFinanceira(movFiananc);

			returnValue = "{id:\'" + movFiananc.getId().toString() + "\'}";

			return "true|" + returnValue;
		} catch (Exception e) {
			return "false|Erro Interno no Servidor";
		}

	}

	@Override
	public String fulfillMovFinanc(Long id) {
		HttpServletRequest request = (HttpServletRequest) context
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);

		MovimentacaoFinanceiraRepository movFinancRepository = new MovimentacaoFinanceiraRepository(
				request);

		return fulfillMovFinanc(id, movFinancRepository, null);
	}

	@Override
	public String fulfillMovFinancExternal(String userId, String password,
			String id) {
		HttpServletRequest request = (HttpServletRequest) context
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);

		MovimentacaoFinanceiraRepository movFinancRepository = new MovimentacaoFinanceiraRepository(
				request);

		if (!putUsrIntoRepository(request, movFinancRepository, userId,
				password)) {
			return "false|Usuario não autenticado";
		}
		String returnValue = fulfillMovFinanc(Long.valueOf(id),
				movFinancRepository, request);
		if (returnValue.equals("success"))
			return "true|" + returnValue;
		else
			return "false|" + returnValue;
	}

	private String fulfillMovFinanc(Long id,
			MovimentacaoFinanceiraRepository movFinancRepository,
			HttpServletRequest request) {
		String returnMsg = "success";

		MovimentacaoFinanceira movimentFinanc = movFinancRepository
				.findMovimentacaoFinanceira(id);

		Calendar dataRealizada = Calendar.getInstance();
		dataRealizada.set(dataRealizada.get(Calendar.YEAR),
				dataRealizada.get(Calendar.MONTH),
				dataRealizada.get(Calendar.DAY_OF_MONTH));
		// if (dataRealizada.get(Calendar.DST_OFFSET) > 0)
		// dataRealizada.add(Calendar.HOUR_OF_DAY, -2);
		// else
		// dataRealizada.add(Calendar.HOUR_OF_DAY, -3);

		movimentFinanc.setSituacao('R');
		movimentFinanc.setDataRealizada(dataRealizada.getTime());

		movFinancRepository.updateMovimentacaoFinanceira(movimentFinanc, request);

		return returnMsg;

	}

	@Override
	public String getTipoGastoChart(String yearMonth, String userId,
			String password) {

		try {

			String yearMonthStr = yearMonth;
			String returnValue = "{}";
			java.util.List<MovimentacaoFinanceira> movFinacList = null;

			Calendar today = Calendar.getInstance();
			Calendar initialDate = Calendar.getInstance();
			Calendar finalDate = Calendar.getInstance();

			HttpServletRequest request = (HttpServletRequest) context
					.getMessageContext().get(MessageContext.SERVLET_REQUEST);

			MovimentacaoFinanceiraRepository repository = new MovimentacaoFinanceiraRepository(
					request);

			if (!putUsrIntoRepository(request, repository, userId, password)) {
				return "false|Usuario não autenticado";
			}

			String[] yearMonthArray = new String[] {
					yearMonthStr.substring(4, 6), yearMonthStr.substring(0, 4) };

			initialDate = SFFUtil.createInitialDate(
					Integer.parseInt(yearMonthArray[1]),
					Integer.parseInt(yearMonthArray[0]));

			finalDate = SFFUtil.createFinalDate(
					Integer.parseInt(yearMonthArray[1]),
					Integer.parseInt(yearMonthArray[0]));

			returnValue = "{title: \"Tipo de Gasto\", ";
			returnValue += "data: "
					+ repository.TipoGastoChart(initialDate, finalDate, today);
			returnValue += "}";

			return "true|" + returnValue;
		} catch (Exception e) {
			return "false|Erro Interno no Servidor";
		}
	}

	@Override
	public String getSpendingsChart(String yearMonth, String userId,
			String password) {

		try {

			String yearMonthStr = yearMonth;
			String returnValue = "{}";
			String chartData = "";
			String chartValues = "";
			String chartTitle = "";
			java.util.List<MovimentacaoFinanceira> movFinacList = null;

			Calendar today = Calendar.getInstance();
			Calendar initialDate = Calendar.getInstance();
			Calendar finalDate = Calendar.getInstance();
			Calendar previewMonthLastDay = Calendar.getInstance();

			HttpServletRequest request = (HttpServletRequest) context
					.getMessageContext().get(MessageContext.SERVLET_REQUEST);

			MovimentacaoFinanceiraRepository repository = new MovimentacaoFinanceiraRepository(
					request);

			if (!putUsrIntoRepository(request, repository, userId, password)) {
				return "false|Usuario não autenticado";
			}

			String[] yearMonthArray = new String[] {
					yearMonthStr.substring(4, 6), yearMonthStr.substring(0, 4) };

			initialDate = SFFUtil.createInitialDate(
					Integer.parseInt(yearMonthArray[1]),
					Integer.parseInt(yearMonthArray[0]));

			finalDate = SFFUtil.createFinalDate(
					Integer.parseInt(yearMonthArray[1]),
					Integer.parseInt(yearMonthArray[0]));

			previewMonthLastDay = SFFUtil.createPreviewMonthLastDay(
					Integer.parseInt(yearMonthArray[1]),
					Integer.parseInt(yearMonthArray[0]));

			double spendingsChartSpends = 0;
			double spendingsChartGains = 0;
			double previewsBalance = repository.getPreviewsBalance(
					previewMonthLastDay, finalDate, today);
			double monthsSpendings = repository.getMonthsSpendings(initialDate,
					finalDate, today);
			double monthsGains = repository.getMonthsGains(initialDate,
					finalDate, today);
			double balance = monthsGains + previewsBalance - monthsSpendings;
			double spendingsPerc = round((monthsSpendings
					/ (monthsGains + previewsBalance) * 100), 2);

			if (balance >= 0) {
				spendingsChartSpends = monthsSpendings;
				spendingsChartGains = monthsGains + previewsBalance;

				chartTitle = "Crédito Previsto Restante";

				chartData += "[[\"Gastos\", " + Double.toString(spendingsPerc)
						+ "],[\"Ganhos\", "
						+ Double.toString(100.00 - spendingsPerc) + "]]";
				
				chartValues += "[[\"Gastos\", " + Double.toString(spendingsChartSpends)
						+ "],[\"Ganhos\", "
						+ Double.toString(spendingsChartGains) + "]]";

			} else {

				spendingsChartSpends = round(balance, 2);
				spendingsPerc = round(
						(monthsSpendings / (monthsGains + previewsBalance)), 2);
				long iPart;

				iPart = (long) spendingsPerc;
				spendingsPerc = (spendingsPerc - iPart) * 100;

				chartData = "[[\"Endividamento\", "
						+ Double.toString(spendingsPerc)
						+ "],[\"Ganhos já gastos\", "
						+ Double.toString(100.00 - spendingsPerc) + "]]";

				chartValues += "[[\"Gastos\", " + Double.toString(spendingsChartSpends)
						+ "],[\"Ganhos já gastos\", "
						+ Double.toString((monthsGains + previewsBalance)) + "]]";

				chartTitle = "Nivel de Endividamento Previsto";
			}

			returnValue = "{title: \"" + chartTitle + "\", ";
			returnValue += "data: " + chartData;
			returnValue += ",values: " + chartValues;
			returnValue += "}";

			return "true|" + returnValue;
		} catch (Exception e) {
			return "false|Erro Interno no Servidor";
		}
	}

	private static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@Override
	public String getCurrentSpendingsChart(String yearMonth, String userId,
			String password) {
		try {

			String yearMonthStr = yearMonth;
			String returnValue = "{}";
			String chartData = "";
			String chartValues = "";
			String chartTitle = "";
			java.util.List<MovimentacaoFinanceira> movFinacList = null;

			Calendar today = Calendar.getInstance();
			Calendar initialDate = Calendar.getInstance();
			Calendar finalDate = Calendar.getInstance();
			Calendar previewMonthLastDay = Calendar.getInstance();

			HttpServletRequest request = (HttpServletRequest) context
					.getMessageContext().get(MessageContext.SERVLET_REQUEST);

			MovimentacaoFinanceiraRepository repository = new MovimentacaoFinanceiraRepository(
					request);

			if (!putUsrIntoRepository(request, repository, userId, password)) {
				return "false|Usuario não autenticado";
			}

			String[] yearMonthArray = new String[] {
					yearMonthStr.substring(4, 6), yearMonthStr.substring(0, 4) };

			initialDate = SFFUtil.createInitialDate(
					Integer.parseInt(yearMonthArray[1]),
					Integer.parseInt(yearMonthArray[0]));

			finalDate = SFFUtil.createFinalDate(
					Integer.parseInt(yearMonthArray[1]),
					Integer.parseInt(yearMonthArray[0]));

			previewMonthLastDay = SFFUtil.createPreviewMonthLastDay(
					Integer.parseInt(yearMonthArray[1]),
					Integer.parseInt(yearMonthArray[0]));

			double spendingsChartSpends = 0;
			double spendingsChartGains = 0;
			double previewsBalance = repository.getPreviewsBalance(
					previewMonthLastDay, finalDate, today);
			double monthsSpendings = repository.getCurrentMonthsSpendings(initialDate,
					finalDate, today);
			double monthsGains = repository.getCurrentMonthsGains(initialDate,
					finalDate, today);
			double balance = monthsGains + previewsBalance - monthsSpendings;
			double spendingsPerc = round((monthsSpendings
					/ (monthsGains + previewsBalance) * 100), 2);

			if (balance >= 0) {
				spendingsChartSpends = monthsSpendings;
				spendingsChartGains = monthsGains + previewsBalance;

				chartTitle = "Crédito Realizado Restante";

				chartData += "[[\"Gastos\", " + Double.toString(spendingsPerc)
						+ "],[\"Ganhos\", "
						+ Double.toString(100.00 - spendingsPerc) + "]]";
				
				chartValues += "[[\"Gastos\", " + Double.toString(spendingsChartSpends)
						+ "],[\"Ganhos\", "
						+ Double.toString(spendingsChartGains) + "]]";

			} else {

				spendingsChartSpends = round(balance, 2);
				spendingsPerc = round(
						(monthsSpendings / (monthsGains + previewsBalance)), 2);
				long iPart;

				iPart = (long) spendingsPerc;
				spendingsPerc = (spendingsPerc - iPart) * 100;

				chartData = "[[\"Endividamento\", "
						+ Double.toString(spendingsPerc)
						+ "],[\"Ganhos já gastos\", "
						+ Double.toString(100.00 - spendingsPerc) + "]]";

				chartValues += "[[\"Gastos\", " + Double.toString(spendingsChartSpends)
						+ "],[\"Ganhos já gastos\", "
						+ Double.toString((monthsGains + previewsBalance)) + "]]";

				chartTitle = "Nivel de Endividamento Realizado";
			}

			returnValue = "{title: \"" + chartTitle + "\", ";
			returnValue += "data: " + chartData;
			returnValue += ",values: " + chartValues;
			returnValue += "}";

			return "true|" + returnValue;
		} catch (Exception e) {
			return "false|Erro Interno no Servidor";
		}
	}

}
