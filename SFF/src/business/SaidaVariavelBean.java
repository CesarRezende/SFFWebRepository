package business;

import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import data.MovimentacaoFinanceira;
import data.SaidaVariavel;
import data.TipoGasto;
import data.SaidaVariavel.Status;
import data.SaidaVariavel.TipoDia;
import data.Usuario;

public class SaidaVariavelBean implements ISaidaVariavel {

	public void saveSaidaVariavel(SaidaVariavel saidaVariavel,
			TipoGasto tipoGasto, Usuario usuario, TipoDia tipoDia) throws ErrorMessageException{

		saidaVariavel.setTipoGasto(tipoGasto);
		saidaVariavel.setUsuario(usuario);
		saidaVariavel.setTipoDia(tipoDia);

		this.saveSaidaVariavel(saidaVariavel);

	}
	
	public void calcGastoTotal(SaidaVariavel saidaVariavel){

		saidaVariavel.setValorTotal(saidaVariavel.getValor() * saidaVariavel.getParcelas());
	}
	
	public void calcGastoTotalRestante(SaidaVariavel saidaVariavel){

		saidaVariavel.setValorTotalRestante(saidaVariavel.getValor() * (saidaVariavel.getParcelas() - saidaVariavel.getParcelasPagas()));
	}
	
	private boolean hasMovimentosRealizados(SaidaVariavel saidaVariavel){
		
		for (Iterator<MovimentacaoFinanceira> iterator = saidaVariavel.getMovimentacoesfinanc().iterator(); iterator.hasNext();) {
			MovimentacaoFinanceira movimentFinanc = (MovimentacaoFinanceira) iterator.next();
			
			if(movimentFinanc.getSituacao().equals('R'))
				return true;
		}
		
		return false;
		
	}

	public void saveSaidaVariavel(SaidaVariavel saidaVariavel) throws ErrorMessageException{

		Calendar dataPrimeiraParc = Calendar.getInstance();
		dataPrimeiraParc.set(saidaVariavel.getAno(), saidaVariavel.getMes(),saidaVariavel.getValorDia());
		
		saidaVariavel.setDataPrimeiraParc(dataPrimeiraParc.getTime());
		
		if(hasMovimentosRealizados(saidaVariavel))
			throw new ErrorMessageException("Não é permitido alteração de Gasto Variavel com Movimentações Financeiras realizadas!");
		
		generateMovimentacoesFinanc(saidaVariavel);

		if (saidaVariavel.isNewRecord())
			new SaidaVariavelRepository().saveSaidaVariavel(saidaVariavel);
		else
			new SaidaVariavelRepository().updateSaidaVariavel(saidaVariavel);

	}

public Status evaluateStatus(SaidaVariavel saidaVariavel) {
	int payeds = 0;
	int opens = 0;
	
	for (Iterator<MovimentacaoFinanceira> iterator = saidaVariavel
			.getMovimentacoesfinanc().iterator(); iterator.hasNext();) {
		MovimentacaoFinanceira movimentFinanc = (MovimentacaoFinanceira) iterator
				.next();

		if (movimentFinanc.getSituacao().equals('P'))
			opens += 1;
			
		if (movimentFinanc.getSituacao().equals('R'))
			payeds += 1;
			
	}
	
	saidaVariavel.setParcelasPagas(payeds);

	if(payeds > 0 && opens > 0){
		saidaVariavel.setStatus(Status.PAGO_PARCIALMENTE);		
		return Status.PAGO_PARCIALMENTE;
	}
	else if(payeds > 0 && opens == 0){
		saidaVariavel.setStatus(Status.PAGO);		
		return Status.PAGO;
	}
	else{
		saidaVariavel.setStatus(Status.ABERTO);
		return Status.ABERTO;
	}

}


	public void generateMovimentacoesFinanc(SaidaVariavel saidaVariavel) {

		// repository.inicialize(saidaVariavel);

		Calendar date = Calendar.getInstance();
		date.set(saidaVariavel.getAno(),saidaVariavel.getMes() -1, 1);

		List<MovimentacaoFinanceira> movimentFinancList = saidaVariavel
				.getMovimentacoesfinanc();

		Collections.sort(movimentFinancList,
				SaidaVariavel.movimentFinancComparator);

		// Deleta Movimentos da Lista
		for (Iterator<MovimentacaoFinanceira> iterator = movimentFinancList
				.iterator(); iterator.hasNext();) {
			iterator.next();
			iterator.remove();
		}

		// Insere Movimentos para as parcelas
		for (int parcela = 0; parcela < (saidaVariavel.getParcelas()); parcela++) {

			boolean createMovFinanc = true;
			MovimentacaoFinanceira newMovimentFinanc = new MovimentacaoFinanceira();
			MovimentacaoFinanceira currentMovimentFinanc = null;

			newMovimentFinanc.setDescricao(saidaVariavel.getDescricao() + " - "+ (parcela + 1) +"/"+ saidaVariavel.getParcelas());
			newMovimentFinanc.setTipoMovimentacao('D');
			newMovimentFinanc.setValor(saidaVariavel.getValor());
			newMovimentFinanc.setUsuario(saidaVariavel.getUsuario());
			newMovimentFinanc.setTipoGasto(saidaVariavel.getTipoGasto());
			newMovimentFinanc.setSituacao('P');
			Calendar dataLancamento = Calendar.getInstance();
			if (dataLancamento.get(Calendar.DST_OFFSET) > 0)
				dataLancamento.add(Calendar.HOUR_OF_DAY, -2);
			else
				dataLancamento.add(Calendar.HOUR_OF_DAY, -3);

			newMovimentFinanc.setDataLancamento(dataLancamento.getTime());
			newMovimentFinanc.setSaidaVariavel(saidaVariavel);

			switch (saidaVariavel.getTipoDia()) {
			case DIA_MES:
				Calendar diaMes = Calendar.getInstance();
				diaMes.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
						1, 0, 0, 0);

				if (saidaVariavel.getValorDia() <= diaMes
						.getActualMaximum(Calendar.DAY_OF_MONTH)) {
					diaMes.set(Calendar.DAY_OF_MONTH,
							saidaVariavel.getValorDia());

				} else {
					createMovFinanc = false;
				}

				if (diaMes.get(Calendar.DST_OFFSET) > 0)
					diaMes.add(Calendar.HOUR_OF_DAY, -2);
				else
					diaMes.add(Calendar.HOUR_OF_DAY, -3);
				newMovimentFinanc.setDataPrevista(diaMes.getTime());

				if (createMovFinanc)
					currentMovimentFinanc = contemSaidaVariavelMoviment(
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

				while (diaSemana.get(Calendar.DAY_OF_WEEK) != saidaVariavel
						.getValorDia()) {
					diaSemana.add(Calendar.DAY_OF_MONTH, 1);
				}
				int day = diaSemana.get(Calendar.DAY_OF_MONTH)
						+ (7 * (saidaVariavel.getOcorrencia() - 1));

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
					currentMovimentFinanc = contemSaidaVariavelMoviment(
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
				int diasUteis = saidaVariavel.getValorDia();

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
					currentMovimentFinanc = contemSaidaVariavelMoviment(
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
				int diasUteisl = saidaVariavel.getValorDia();

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
					currentMovimentFinanc = contemSaidaVariavelMoviment(
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
		currentMovimentFinanc.setTipoGasto(newMovimentFinanc.getTipoGasto());
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

	private MovimentacaoFinanceira contemSaidaVariavelMoviment(
			List<MovimentacaoFinanceira> movimentFinancList,
			MovimentacaoFinanceira newMovimentFinanc, boolean verifyWeek) {

		Collections.sort(movimentFinancList,
				SaidaVariavel.movimentFinancComparator);

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
