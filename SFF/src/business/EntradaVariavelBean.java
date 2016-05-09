package business;

import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import data.MovimentacaoFinanceira;
import data.EntradaVariavel;
import data.EntradaVariavel.Status;
import data.EntradaVariavel.TipoDia;
import data.Usuario;

public class EntradaVariavelBean implements IEntradaVariavel {

	public void saveEntradaVariavel(EntradaVariavel entradaVariavel,
			Usuario usuario, TipoDia tipoDia) throws ErrorMessageException{

		entradaVariavel.setUsuario(usuario);
		entradaVariavel.setTipoDia(tipoDia);

		this.saveEntradaVariavel(entradaVariavel);

	}
	private boolean hasMovimentosRealizados(EntradaVariavel entradaVariavel){
		
		for (Iterator<MovimentacaoFinanceira> iterator = entradaVariavel.getMovimentacoesfinanc().iterator(); iterator.hasNext();) {
			MovimentacaoFinanceira movimentFinanc = (MovimentacaoFinanceira) iterator.next();
			
			if(movimentFinanc.getSituacao().equals('R'))
				return true;
		}
		
		return false;
		
	}

	public void calcGastoTotal(EntradaVariavel entradaVariavel){

		entradaVariavel.setValorTotal(entradaVariavel.getValor() * entradaVariavel.getParcelas());
	}
	
	public void calcGastoTotalRestante(EntradaVariavel entradaVariavel){

		entradaVariavel.setValorTotalRestante(entradaVariavel.getValor() * (entradaVariavel.getParcelas() - entradaVariavel.getParcelasRecebidas()));
	}
	
	public void saveEntradaVariavel(EntradaVariavel entradaVariavel) throws ErrorMessageException{

		Calendar dataPrimeiraParc = Calendar.getInstance();
		dataPrimeiraParc.set(entradaVariavel.getAno(), entradaVariavel.getMes(),entradaVariavel.getValorDia());
		
		entradaVariavel.setDataPrimeiraParc(dataPrimeiraParc.getTime());
		
		if(hasMovimentosRealizados(entradaVariavel))
			throw new ErrorMessageException("Não é permitido alteração de Gasto Variavel com Movimentações Financeiras realizadas!");
		
		generateMovimentacoesFinanc(entradaVariavel);

		if (entradaVariavel.isNewRecord())
			new EntradaVariavelRepository().saveEntradaVariavel(entradaVariavel);
		else
			new EntradaVariavelRepository().updateEntradaVariavel(entradaVariavel);

	}

	public Status evaluateStatus(EntradaVariavel entradaVariavel) {
	int receved = 0;
	int opens = 0;
	
	for (Iterator<MovimentacaoFinanceira> iterator = entradaVariavel
			.getMovimentacoesfinanc().iterator(); iterator.hasNext();) {
		MovimentacaoFinanceira movimentFinanc = (MovimentacaoFinanceira) iterator
				.next();

		if (movimentFinanc.getSituacao().equals('P'))
			opens += 1;
			
		if (movimentFinanc.getSituacao().equals('R'))
			receved += 1;
			
	}
	
	entradaVariavel.setParcelasRecebidas(receved);

	if(receved > 0 && opens > 0){
		entradaVariavel.setStatus(Status.RECEBIDO_PARCIALMENTE);		
		return Status.RECEBIDO_PARCIALMENTE;
	}
	else if(receved > 0 && opens == 0){
		entradaVariavel.setStatus(Status.RECEBIDO);		
		return Status.RECEBIDO;
	}
	else{
		entradaVariavel.setStatus(Status.ABERTO);
		return Status.ABERTO;
	}

}


	public void generateMovimentacoesFinanc(EntradaVariavel entradaVariavel) {

		// repository.inicialize(entradaVariavel);

		Calendar date = Calendar.getInstance();
		date.set(entradaVariavel.getAno(),entradaVariavel.getMes() -1, 1);

		List<MovimentacaoFinanceira> movimentFinancList = entradaVariavel
				.getMovimentacoesfinanc();

		Collections.sort(movimentFinancList,
				EntradaVariavel.movimentFinancComparator);

		// Deleta Movimentos da Lista
		for (Iterator<MovimentacaoFinanceira> iterator = movimentFinancList
				.iterator(); iterator.hasNext();) {
			iterator.next();
			iterator.remove();
		}

		// Insere Movimentos para as parcelas
		for (int parcela = 0; parcela < (entradaVariavel.getParcelas()); parcela++) {

			boolean createMovFinanc = true;
			MovimentacaoFinanceira newMovimentFinanc = new MovimentacaoFinanceira();
			MovimentacaoFinanceira currentMovimentFinanc = null;

			newMovimentFinanc.setDescricao(entradaVariavel.getDescricao() + " - "+ (parcela + 1) +"/"+ entradaVariavel.getParcelas());
			newMovimentFinanc.setTipoMovimentacao('C');
			newMovimentFinanc.setValor(entradaVariavel.getValor());
			newMovimentFinanc.setUsuario(entradaVariavel.getUsuario());
			newMovimentFinanc.setSituacao('P');
			newMovimentFinanc.setTipoGasto(null);
			Calendar dataLancamento = Calendar.getInstance();
			if (dataLancamento.get(Calendar.DST_OFFSET) > 0)
				dataLancamento.add(Calendar.HOUR_OF_DAY, -2);
			else
				dataLancamento.add(Calendar.HOUR_OF_DAY, -3);

			newMovimentFinanc.setDataLancamento(dataLancamento.getTime());
			newMovimentFinanc.setEntradaVariavel(entradaVariavel);

			switch (entradaVariavel.getTipoDia()) {
			case DIA_MES:
				Calendar diaMes = Calendar.getInstance();
				diaMes.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
						1, 0, 0, 0);

				if (entradaVariavel.getValorDia() <= diaMes
						.getActualMaximum(Calendar.DAY_OF_MONTH)) {
					diaMes.set(Calendar.DAY_OF_MONTH,
							entradaVariavel.getValorDia());

				} else {
					createMovFinanc = false;
				}

				if (diaMes.get(Calendar.DST_OFFSET) > 0)
					diaMes.add(Calendar.HOUR_OF_DAY, -2);
				else
					diaMes.add(Calendar.HOUR_OF_DAY, -3);
				newMovimentFinanc.setDataPrevista(diaMes.getTime());

				if (createMovFinanc)
					currentMovimentFinanc = contemEntradaVariavelMoviment(
							movimentFinancList, newMovimentFinanc, false);

				if (currentMovimentFinanc != null) {
					createMovFinanc = false;
					updateMovFinac(newMovimentFinanc, currentMovimentFinanc);
				}

				break;
			case DIA_SEMANA:

				Calendar diaSemana = Calendar.getInstance();
				diaSemana.set(date.get(Calendar.YEAR),
						date.get(Calendar.MONTH), 1, 0, 0, 0);

				while (diaSemana.get(Calendar.DAY_OF_WEEK) != entradaVariavel
						.getValorDia()) {
					diaSemana.add(Calendar.DAY_OF_MONTH, 1);
				}
				int day = diaSemana.get(Calendar.DAY_OF_MONTH)
						+ (7 * (entradaVariavel.getOcorrencia() - 1));

				if (day <= date.getActualMaximum(Calendar.DAY_OF_MONTH)) {
					diaSemana.set(date.get(Calendar.YEAR),
							date.get(Calendar.MONTH), day, 0, 0, 0);

					if (diaSemana.get(Calendar.DST_OFFSET) > 0)
						diaSemana.add(Calendar.HOUR_OF_DAY, -2);
					else
						diaSemana.add(Calendar.HOUR_OF_DAY, -3);

					newMovimentFinanc.setDataPrevista(diaSemana.getTime());

				} else {
					createMovFinanc = false;
				}

				if (createMovFinanc)
					currentMovimentFinanc = contemEntradaVariavelMoviment(
							movimentFinancList, newMovimentFinanc, false);

				if (currentMovimentFinanc != null) {
					createMovFinanc = false;
					updateMovFinac(newMovimentFinanc, currentMovimentFinanc);
				}

				break;

			case DIA_UTIL:
				Calendar diaUtil = Calendar.getInstance();
				diaUtil.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
						1, 0, 0);
				int diasUteis = entradaVariavel.getValorDia();

				while (diasUteis > 0) {
					if (diaUtil.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
							&& diaUtil.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
						diasUteis -= 1;

						if (diasUteis > 0)
							diaUtil.add(Calendar.DAY_OF_MONTH, 1);
					} else {
						diaUtil.add(Calendar.DAY_OF_MONTH, 1);
					}

				}

				if (diaUtil.get(Calendar.DST_OFFSET) > 0)
					diaUtil.add(Calendar.HOUR_OF_DAY, -2);
				else
					diaUtil.add(Calendar.HOUR_OF_DAY, -3);
				newMovimentFinanc.setDataPrevista(diaUtil.getTime());

				if (createMovFinanc)
					currentMovimentFinanc = contemEntradaVariavelMoviment(
							movimentFinancList, newMovimentFinanc, false);

				if (currentMovimentFinanc != null) {
					createMovFinanc = false;
					updateMovFinac(newMovimentFinanc, currentMovimentFinanc);
				}

				break;
			case ULTIMO_DIA_UTIL:
				Calendar ultimodiaUtil = Calendar.getInstance();
				ultimodiaUtil.set(date.get(Calendar.YEAR),
						date.get(Calendar.MONTH),
						date.getActualMaximum(Calendar.DAY_OF_MONTH), 0, 0);
				int diasUteisl = entradaVariavel.getValorDia();

				while (diasUteisl > 0) {
					if (ultimodiaUtil.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
							&& ultimodiaUtil.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
						diasUteisl -= 1;

						if (diasUteisl > 0)
							ultimodiaUtil.add(Calendar.DAY_OF_MONTH, -1);
					} else {
						ultimodiaUtil.add(Calendar.DAY_OF_MONTH, -1);
					}

				}

				if (ultimodiaUtil.get(Calendar.DST_OFFSET) > 0)
					ultimodiaUtil.add(Calendar.HOUR_OF_DAY, -2);
				else
					ultimodiaUtil.add(Calendar.HOUR_OF_DAY, -3);
				newMovimentFinanc.setDataPrevista(ultimodiaUtil.getTime());

				if (createMovFinanc)
					currentMovimentFinanc = contemEntradaVariavelMoviment(
							movimentFinancList, newMovimentFinanc, false);

				if (currentMovimentFinanc != null) {
					createMovFinanc = false;
					updateMovFinac(newMovimentFinanc, currentMovimentFinanc);
				}

				break;
			default:
				break;
			}

			// Adiciona a Lista de Movimentos
			if (createMovFinanc)
				movimentFinancList.add(newMovimentFinanc);

			date.add(Calendar.MONTH, 1);

		}

	}

	private MovimentacaoFinanceira updateMovFinac(
			MovimentacaoFinanceira newMovimentFinanc,
			MovimentacaoFinanceira currentMovimentFinanc) {

		currentMovimentFinanc.setDescricao(newMovimentFinanc.getDescricao());
		currentMovimentFinanc.setDataLancamento(newMovimentFinanc
				.getDataLancamento());

		if (currentMovimentFinanc.getSituacao().equals("P")) {
			currentMovimentFinanc.setValor(newMovimentFinanc.getValor());
			currentMovimentFinanc.setDataPrevista(newMovimentFinanc
					.getDataPrevista());
			currentMovimentFinanc.setUsuario(newMovimentFinanc.getUsuario());
		}

		return currentMovimentFinanc;
	}

	private MovimentacaoFinanceira contemEntradaVariavelMoviment(
			List<MovimentacaoFinanceira> movimentFinancList,
			MovimentacaoFinanceira newMovimentFinanc, boolean verifyWeek) {

		Collections.sort(movimentFinancList,
				EntradaVariavel.movimentFinancComparator);

		if (movimentFinancList.size() > 0) {
			MovimentacaoFinanceira lastMovFinac = null;
			int count = movimentFinancList.size();
			while (count > 0) {
				count -= 1;
				lastMovFinac = movimentFinancList.get(count);

				Calendar lastMovDataPrevista = Calendar.getInstance();
				lastMovDataPrevista.setTime(lastMovFinac.getDataPrevista());
				lastMovDataPrevista.add(Calendar.HOUR_OF_DAY, 3);

				Calendar newMovDataPrevista = Calendar.getInstance();
				newMovDataPrevista.setTime(newMovimentFinanc.getDataPrevista());
				newMovDataPrevista.add(Calendar.HOUR_OF_DAY, 3);

				if (lastMovDataPrevista.get(Calendar.YEAR) < newMovDataPrevista
						.get(Calendar.YEAR)
						|| (lastMovDataPrevista.get(Calendar.YEAR) == newMovDataPrevista
								.get(Calendar.YEAR) && lastMovDataPrevista
								.get(Calendar.MONTH) < newMovDataPrevista
								.get(Calendar.MONTH))) {
					lastMovFinac = null;
					break;
				}
				// newMovimentFinanc.getDescricao().equals(lastMovFinac.getDescricao())
				// &&
				// lastMovDataPrevista.getTime().toString().equals(newMovDataPrevista.getTime().toString())
				if (!verifyWeek)
					if (lastMovDataPrevista.get(Calendar.YEAR) == newMovDataPrevista
							.get(Calendar.YEAR)
							&& lastMovDataPrevista.get(Calendar.MONTH) == newMovDataPrevista
									.get(Calendar.MONTH)) {
						break;
					}

				if (verifyWeek)
					if (lastMovDataPrevista.get(Calendar.YEAR) == newMovDataPrevista
							.get(Calendar.YEAR)
							&& lastMovDataPrevista.get(Calendar.MONTH) == newMovDataPrevista
									.get(Calendar.MONTH)
							&& lastMovDataPrevista.get(Calendar.WEEK_OF_MONTH) == newMovDataPrevista
									.get(Calendar.WEEK_OF_MONTH)) {
						break;
					}

				lastMovFinac = null;

			}

			return lastMovFinac;

		}

		return null;

	}

}
