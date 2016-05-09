package business;

import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import data.MovimentacaoFinanceira;
import data.SaidaFixa;
import data.TipoGasto;
import data.SaidaFixa.TipoDia;
import data.Usuario;

public class SaidaFixaBean implements ISaidaFixa{

	public void saveSaidaFixa(SaidaFixa saidaFixa,TipoGasto tipoGasto, Usuario usuario, TipoDia tipoDia ) {

		saidaFixa.setTipoGasto(tipoGasto);
		saidaFixa.setUsuario(usuario);
		saidaFixa.setTipoDia(tipoDia);
		
		this.saveSaidaFixa(saidaFixa);
		
	}
	
public void disableSaidaFixa(SaidaFixa saidaFixa,TipoGasto tipoGasto, Usuario usuario, TipoDia tipoDia) {
		
		if(!saidaFixa.isDesativado()){
			
			List<MovimentacaoFinanceira> movimentFinancList = saidaFixa.getMovimentacoesfinanc();
			
			//Deleta Movimentações Previstas
			for (Iterator<MovimentacaoFinanceira> iterator = movimentFinancList.iterator(); iterator.hasNext(); ) {
				MovimentacaoFinanceira moventFinanc = iterator.next();
				  if(moventFinanc.getSituacao().equals('P')){
				    iterator.remove();
				  }
				}
			saidaFixa.setUsuario(usuario);
			saidaFixa.setDesativado(true);
			new SaidaFixaRepository().updateSaidaFixa(saidaFixa);
			
		}else{
			//Reativa Saida Fixa e Recria seus Movimentos
			saidaFixa.setDesativado(false);
			saveSaidaFixa(saidaFixa,tipoGasto, usuario, tipoDia );
			
		}
		
		
	}

	
	public void saveSaidaFixa(SaidaFixa saidaFixa) {
				
		generateMovimentacoesFinanc(saidaFixa, true);
		
		if(saidaFixa.isNewRecord())
			new SaidaFixaRepository().saveSaidaFixa(saidaFixa);
		else
			new SaidaFixaRepository().updateSaidaFixa(saidaFixa);
		
	}

	public void generateMovimentacoesFinanc(SaidaFixa saidaFixa, boolean deleteMovFinancPrevisto){


		//repository.inicialize(saidaFixa);
		
		Calendar date = Calendar.getInstance();
		
		
		List<MovimentacaoFinanceira>  movimentFinancList = saidaFixa.getMovimentacoesfinanc();

		Collections.sort(movimentFinancList, SaidaFixa.movimentFinancComparator);
		
		//Deleta Movimentos Previstos da Lista
		if(deleteMovFinancPrevisto)
			for (Iterator<MovimentacaoFinanceira> iterator = movimentFinancList.iterator(); iterator.hasNext(); ) {
				MovimentacaoFinanceira moventFinanc = iterator.next();
				  if(moventFinanc.getSituacao().equals('P')){
					  
					  Calendar lastMovDataPrevista = Calendar.getInstance();
					  lastMovDataPrevista.setTime(moventFinanc.getDataPrevista());
					  
					  if(lastMovDataPrevista.get(Calendar.YEAR) > date.get(Calendar.YEAR) ||
							  (lastMovDataPrevista.get(Calendar.YEAR) == date.get(Calendar.YEAR)
							    && lastMovDataPrevista.get(Calendar.MONTH) >= date.get(Calendar.MONTH)))
				    iterator.remove();
				  }
				}
		
		//Insere Movimentos para os próximos 12 meses
		for(int months = 0; months < 12; months++){
			
			//Gera Movimentos para o Mês
			if(generateMovimenforMonth(date.get(Calendar.MONTH),saidaFixa)){
				
				//Gera Movimentos para todas ocorrencia do dia da semana no Mês
				if(saidaFixa.getTipoDia().equals(TipoDia.DIA_SEMANA) && saidaFixa.getOcorrencia() == 9){
					
					for(int semana = 1; semana <= 5; semana++){
						
						boolean createMovFinanc = true;
						MovimentacaoFinanceira newMovimentFinanc = new MovimentacaoFinanceira();
						MovimentacaoFinanceira currentMovimentFinanc = null;
						newMovimentFinanc.setDescricao(saidaFixa.getDescricao());
						newMovimentFinanc.setTipoMovimentacao('D');
						newMovimentFinanc.setValor(saidaFixa.getValor());
						newMovimentFinanc.setUsuario(saidaFixa.getUsuario());
						newMovimentFinanc.setTipoGasto(saidaFixa.getTipoGasto());
						newMovimentFinanc.setSituacao('P');
						Calendar dataLancamento = Calendar.getInstance();
						if(dataLancamento.get(Calendar.DST_OFFSET) > 0)
							dataLancamento.add(Calendar.HOUR_OF_DAY, -2);
						else
							dataLancamento.add(Calendar.HOUR_OF_DAY, -3);
						
						newMovimentFinanc.setDataLancamento(dataLancamento.getTime());
						newMovimentFinanc.setSaidaFixa(saidaFixa);
						
						Calendar diaSemana = Calendar.getInstance();
						diaSemana.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),1,0,0,0);
						
						
						while(diaSemana.get(Calendar.DAY_OF_WEEK) != saidaFixa.getValorDia()){
							diaSemana.add(Calendar.DAY_OF_MONTH, 1);
						}
						int day = diaSemana.get(Calendar.DAY_OF_MONTH) +  (7 * (semana -1));
						
						
						if(day <= date.getActualMaximum(Calendar.DAY_OF_MONTH)){
							diaSemana.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),day,0,0,0);
							
							if(diaSemana.get(Calendar.DST_OFFSET) > 0)
								diaSemana.add(Calendar.HOUR_OF_DAY, -2);
							else
								diaSemana.add(Calendar.HOUR_OF_DAY, -3);
							
							newMovimentFinanc.setDataPrevista(diaSemana.getTime());
						
						}else{
							createMovFinanc = false;
						}
						
						if(createMovFinanc)
							currentMovimentFinanc = contemSaidaFixaMoviment(movimentFinancList ,newMovimentFinanc, true);
						
						if(currentMovimentFinanc != null){
							createMovFinanc = false;
							updateMovFinac(newMovimentFinanc, currentMovimentFinanc);
						}
						
						//Adiciona a Lista de Movimentos
						if(createMovFinanc)
							movimentFinancList.add(newMovimentFinanc);
						
					}
					
				}
				//Gera Unico Movimentos para o Mês
				else{
					
					boolean createMovFinanc = true;
					MovimentacaoFinanceira newMovimentFinanc = new MovimentacaoFinanceira();
					MovimentacaoFinanceira currentMovimentFinanc = null;

					
					newMovimentFinanc.setDescricao(saidaFixa.getDescricao());
					newMovimentFinanc.setTipoMovimentacao('D');
					newMovimentFinanc.setValor(saidaFixa.getValor());
					newMovimentFinanc.setUsuario(saidaFixa.getUsuario());
					newMovimentFinanc.setTipoGasto(saidaFixa.getTipoGasto());
					newMovimentFinanc.setSituacao('P');
					Calendar dataLancamento = Calendar.getInstance();
					if(dataLancamento.get(Calendar.DST_OFFSET) > 0)
						dataLancamento.add(Calendar.HOUR_OF_DAY, -2);
					else
						dataLancamento.add(Calendar.HOUR_OF_DAY, -3);
					
					newMovimentFinanc.setDataLancamento(dataLancamento.getTime());
					newMovimentFinanc.setSaidaFixa(saidaFixa);
					
					
					switch (saidaFixa.getTipoDia()) {
					case DIA_MES:
						Calendar diaMes = Calendar.getInstance();
						diaMes.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),1,0,0,0);
						
						if(saidaFixa.getValorDia() <= diaMes.getActualMaximum(Calendar.DAY_OF_MONTH)){
							diaMes.set(Calendar.DAY_OF_MONTH, saidaFixa.getValorDia());
							
						}
						else{
							createMovFinanc = false;							
						}

						if(diaMes.get(Calendar.DST_OFFSET) > 0)
							diaMes.add(Calendar.HOUR_OF_DAY, -2);
						else
							diaMes.add(Calendar.HOUR_OF_DAY, -3);
						newMovimentFinanc.setDataPrevista(diaMes.getTime());
						
						
						if(createMovFinanc)
							currentMovimentFinanc = contemSaidaFixaMoviment(movimentFinancList ,newMovimentFinanc, false);
						
						if(currentMovimentFinanc != null){
							createMovFinanc = false;
							updateMovFinac(newMovimentFinanc, currentMovimentFinanc);
						}
						
						break;
					case DIA_SEMANA:
						
						Calendar diaSemana = Calendar.getInstance();
						diaSemana.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),1,0,0,0);
						
						
						while(diaSemana.get(Calendar.DAY_OF_WEEK) != saidaFixa.getValorDia()){
							diaSemana.add(Calendar.DAY_OF_MONTH, 1);
						}
						int day = diaSemana.get(Calendar.DAY_OF_MONTH) +  (7 * (saidaFixa.getOcorrencia()-1));
						
						if(day <= date.getActualMaximum(Calendar.DAY_OF_MONTH)){
							diaSemana.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),day,0,0,0);

							if(diaSemana.get(Calendar.DST_OFFSET) > 0)
								diaSemana.add(Calendar.HOUR_OF_DAY, -2);
							else
								diaSemana.add(Calendar.HOUR_OF_DAY, -3);
							
							newMovimentFinanc.setDataPrevista(diaSemana.getTime());
						
						}else{
							createMovFinanc = false;
						}
						
						if(createMovFinanc)
							currentMovimentFinanc = contemSaidaFixaMoviment(movimentFinancList ,newMovimentFinanc, false);
						
						if(currentMovimentFinanc != null){
							createMovFinanc = false;					
							updateMovFinac(newMovimentFinanc, currentMovimentFinanc);
						}
						
						break;
					
					case DIA_UTIL:
						Calendar diaUtil = Calendar.getInstance();
						diaUtil.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), 1,0,0);
						int diasUteis = saidaFixa.getValorDia();
						
						while(diasUteis > 0){
							if(diaUtil.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && diaUtil.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
								diasUteis -= 1;
								
								if(diasUteis > 0)
								diaUtil.add(Calendar.DAY_OF_MONTH, 1);
							}else{
								diaUtil.add(Calendar.DAY_OF_MONTH, 1);
							}
							
						}
						
						if(diaUtil.get(Calendar.DST_OFFSET) > 0)
							diaUtil.add(Calendar.HOUR_OF_DAY, -2);
						else
							diaUtil.add(Calendar.HOUR_OF_DAY, -3);
						newMovimentFinanc.setDataPrevista(diaUtil.getTime());
						
						if(createMovFinanc)
							currentMovimentFinanc = contemSaidaFixaMoviment(movimentFinancList ,newMovimentFinanc, false);
						
						if(currentMovimentFinanc != null){
							createMovFinanc = false;
							updateMovFinac(newMovimentFinanc, currentMovimentFinanc);
						}
						
						break;
					case ULTIMO_DIA_UTIL:
						Calendar ultimodiaUtil = Calendar.getInstance();
						ultimodiaUtil.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.getActualMaximum(Calendar.DAY_OF_MONTH),0,0);
						int diasUteisl = saidaFixa.getValorDia();
						
						while(diasUteisl > 0){
							if(ultimodiaUtil.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && ultimodiaUtil.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
								diasUteisl -= 1;
								
								if(diasUteisl > 0)
									ultimodiaUtil.add(Calendar.DAY_OF_MONTH, -1);
							}else{
								ultimodiaUtil.add(Calendar.DAY_OF_MONTH, -1);
							}
							
						}
						
						if(ultimodiaUtil.get(Calendar.DST_OFFSET) > 0)
							ultimodiaUtil.add(Calendar.HOUR_OF_DAY, -2);
						else
							ultimodiaUtil.add(Calendar.HOUR_OF_DAY, -3);
						newMovimentFinanc.setDataPrevista(ultimodiaUtil.getTime());
						
						if(createMovFinanc)
							currentMovimentFinanc = contemSaidaFixaMoviment(movimentFinancList ,newMovimentFinanc, false);
						
						if(currentMovimentFinanc != null){
							createMovFinanc = false;
							updateMovFinac(newMovimentFinanc, currentMovimentFinanc);
						}
						
						break;
					default:
						break;
					}
					
					//Adiciona a Lista de Movimentos
					if(createMovFinanc)
						movimentFinancList.add(newMovimentFinanc);
				}
				
				
			}
			date.add(Calendar.MONTH, 1);
			
		}
		
		
	}
	
	private MovimentacaoFinanceira updateMovFinac(MovimentacaoFinanceira newMovimentFinanc, MovimentacaoFinanceira currentMovimentFinanc ){
		
		currentMovimentFinanc.setDescricao(newMovimentFinanc.getDescricao());
		currentMovimentFinanc.setTipoGasto(newMovimentFinanc.getTipoGasto());
		currentMovimentFinanc.setDataLancamento(newMovimentFinanc.getDataLancamento());
		
		if(currentMovimentFinanc.getSituacao().equals("P")){		
			currentMovimentFinanc.setValor(newMovimentFinanc.getValor());
			currentMovimentFinanc.setDataPrevista(newMovimentFinanc.getDataPrevista());
			currentMovimentFinanc.setUsuario(newMovimentFinanc.getUsuario());
		}
		
		return currentMovimentFinanc;
	}
	
	private MovimentacaoFinanceira contemSaidaFixaMoviment(List<MovimentacaoFinanceira> movimentFinancList ,MovimentacaoFinanceira newMovimentFinanc, boolean verifyWeek){
		
		Collections.sort(movimentFinancList, SaidaFixa.movimentFinancComparator);
		
		if(movimentFinancList.size() > 0){
			MovimentacaoFinanceira lastMovFinac = null;
			int count = movimentFinancList.size();
			while(count > 0){
				count -= 1;
				lastMovFinac = movimentFinancList.get(count);
				
				Calendar lastMovDataPrevista = Calendar.getInstance();
				lastMovDataPrevista.setTime(lastMovFinac.getDataPrevista());
				lastMovDataPrevista.add(Calendar.HOUR_OF_DAY, 3);
				
				Calendar newMovDataPrevista = Calendar.getInstance();
				newMovDataPrevista.setTime(newMovimentFinanc.getDataPrevista());
				newMovDataPrevista.add(Calendar.HOUR_OF_DAY, 3);
				
				if(lastMovDataPrevista.get(Calendar.YEAR) < newMovDataPrevista.get(Calendar.YEAR) ||
				   (lastMovDataPrevista.get(Calendar.YEAR) == newMovDataPrevista.get(Calendar.YEAR)
				    && lastMovDataPrevista.get(Calendar.MONTH) < newMovDataPrevista.get(Calendar.MONTH))
				   ){
					lastMovFinac = null;
					break;
				}
				//newMovimentFinanc.getDescricao().equals(lastMovFinac.getDescricao())
				//&& lastMovDataPrevista.getTime().toString().equals(newMovDataPrevista.getTime().toString())
				if(!verifyWeek)
					if(lastMovDataPrevista.get(Calendar.YEAR) == newMovDataPrevista.get(Calendar.YEAR)
						    && lastMovDataPrevista.get(Calendar.MONTH) == newMovDataPrevista.get(Calendar.MONTH)){
						break;
					}
				
				if(verifyWeek)
					if(lastMovDataPrevista.get(Calendar.YEAR) == newMovDataPrevista.get(Calendar.YEAR)
						    && lastMovDataPrevista.get(Calendar.MONTH) == newMovDataPrevista.get(Calendar.MONTH)
						    && lastMovDataPrevista.get(Calendar.WEEK_OF_MONTH) == newMovDataPrevista.get(Calendar.WEEK_OF_MONTH)){
						break;
					}
				
				lastMovFinac = null;
				
			}
			
			
			return lastMovFinac;
			
		}
		
		return null;
		
	}
	
	private boolean generateMovimenforMonth(int month, SaidaFixa saidaFixa ){
		boolean generateMovimen = false;
		
		switch (month) {
		case Calendar.JANUARY:
			generateMovimen = saidaFixa.isJan();
			break;
		case Calendar.FEBRUARY:
			generateMovimen = saidaFixa.isFev();
			break;
		case Calendar.MARCH:
			generateMovimen = saidaFixa.isMar();
			break;
		case Calendar.APRIL:
			generateMovimen = saidaFixa.isAbr();
			break;
		case Calendar.MAY:
			generateMovimen = saidaFixa.isMai();
			break;
		case Calendar.JUNE:
			generateMovimen = saidaFixa.isJun();
			break;
		case Calendar.JULY:
			generateMovimen = saidaFixa.isJul();
			break;
		case Calendar.AUGUST:
			generateMovimen = saidaFixa.isAgo();
			break;
		case Calendar.SEPTEMBER:
			generateMovimen = saidaFixa.isSet();
			break;
		case Calendar.OCTOBER:
			generateMovimen = saidaFixa.isOut();
			break;
		case Calendar.NOVEMBER:
			generateMovimen = saidaFixa.isNov();
			break;
		case Calendar.DECEMBER:
			generateMovimen = saidaFixa.isDez();
			break;
		default:
			break;
		}
		
		return generateMovimen;
	}
	
}
