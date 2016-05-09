package business;

import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import data.MovimentacaoFinanceira;
import data.EntradaFixa;
import data.EntradaFixa.TipoDia;
import data.Usuario;

public class EntradaFixaBean implements IEntradaFixa{

	public void saveEntradaFixa(EntradaFixa entradaFixa, Usuario usuario, TipoDia tipoDia ) {

		entradaFixa.setUsuario(usuario);
		entradaFixa.setTipoDia(tipoDia);
		
		this.saveEntradaFixa(entradaFixa);
		
	}
	
public void disableEntradaFixa(EntradaFixa entradaFixa, Usuario usuario, TipoDia tipoDia) {
		
		if(!entradaFixa.isDesativado()){
			
			List<MovimentacaoFinanceira> movimentFinancList = entradaFixa.getMovimentacoesfinanc();
			
			//Deleta Movimentações Previstas
			for (Iterator<MovimentacaoFinanceira> iterator = movimentFinancList.iterator(); iterator.hasNext(); ) {
				MovimentacaoFinanceira moventFinanc = iterator.next();
				  if(moventFinanc.getSituacao().equals('P')){
				    iterator.remove();
				  }
				}
			entradaFixa.setUsuario(usuario);
			entradaFixa.setDesativado(true);
			new EntradaFixaRepository().updateEntradaFixa(entradaFixa);
			
		}else{
			//Reativa Entrada Fixa e Recria seus Movimentos
			entradaFixa.setDesativado(false);
			saveEntradaFixa(entradaFixa, usuario, tipoDia );
			
		}
		
		
	}

	
	public void saveEntradaFixa(EntradaFixa entradaFixa) {
				
		generateMovimentacoesFinanc(entradaFixa, true);
		
		if(entradaFixa.isNewRecord())
			new EntradaFixaRepository().saveEntradaFixa(entradaFixa);
		else
			new EntradaFixaRepository().updateEntradaFixa(entradaFixa);
		
	}

	public void generateMovimentacoesFinanc(EntradaFixa entradaFixa, boolean deleteMovFinancPrevisto){


		//repository.inicialize(entradaFixa);
		
		Calendar date = Calendar.getInstance();
		
		
		List<MovimentacaoFinanceira>  movimentFinancList = entradaFixa.getMovimentacoesfinanc();

		Collections.sort(movimentFinancList, EntradaFixa.movimentFinancComparator);
		
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
			if(generateMovimenforMonth(date.get(Calendar.MONTH),entradaFixa)){
				
				//Gera Movimentos para todas ocorrencia do dia da semana no Mês
				if(entradaFixa.getTipoDia().equals(TipoDia.DIA_SEMANA) && entradaFixa.getOcorrencia() == 9){
					
					for(int semana = 1; semana <= 5; semana++){
						
						boolean createMovFinanc = true;
						MovimentacaoFinanceira newMovimentFinanc = new MovimentacaoFinanceira();
						MovimentacaoFinanceira currentMovimentFinanc = null;
						newMovimentFinanc.setDescricao(entradaFixa.getDescricao());
						newMovimentFinanc.setTipoMovimentacao('C');
						newMovimentFinanc.setValor(entradaFixa.getValor());
						newMovimentFinanc.setUsuario(entradaFixa.getUsuario());
						newMovimentFinanc.setSituacao('P');
						newMovimentFinanc.setTipoGasto(null);
						Calendar dataLancamento = Calendar.getInstance();
						if(dataLancamento.get(Calendar.DST_OFFSET) > 0)
							dataLancamento.add(Calendar.HOUR_OF_DAY, -2);
						else
							dataLancamento.add(Calendar.HOUR_OF_DAY, -3);
						
						newMovimentFinanc.setDataLancamento(dataLancamento.getTime());
						newMovimentFinanc.setEntradaFixa(entradaFixa);
						
						Calendar diaSemana = Calendar.getInstance();
						diaSemana.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),1,0,0,0);
						
						
						while(diaSemana.get(Calendar.DAY_OF_WEEK) != entradaFixa.getValorDia()){
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
							currentMovimentFinanc = contemEntradaFixaMoviment(movimentFinancList ,newMovimentFinanc, true);
						
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

					
					newMovimentFinanc.setDescricao(entradaFixa.getDescricao());
					newMovimentFinanc.setTipoMovimentacao('C');
					newMovimentFinanc.setValor(entradaFixa.getValor());
					newMovimentFinanc.setUsuario(entradaFixa.getUsuario());
					newMovimentFinanc.setSituacao('P');
					newMovimentFinanc.setTipoGasto(null);
					Calendar dataLancamento = Calendar.getInstance();
					if(dataLancamento.get(Calendar.DST_OFFSET) > 0)
						dataLancamento.add(Calendar.HOUR_OF_DAY, -2);
					else
						dataLancamento.add(Calendar.HOUR_OF_DAY, -3);
					
					newMovimentFinanc.setDataLancamento(dataLancamento.getTime());
					newMovimentFinanc.setEntradaFixa(entradaFixa);
					
					
					switch (entradaFixa.getTipoDia()) {
					case DIA_MES:
						Calendar diaMes = Calendar.getInstance();
						diaMes.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),1,0,0,0);
						
						if(entradaFixa.getValorDia() <= diaMes.getActualMaximum(Calendar.DAY_OF_MONTH)){
							diaMes.set(Calendar.DAY_OF_MONTH, entradaFixa.getValorDia());
							
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
							currentMovimentFinanc = contemEntradaFixaMoviment(movimentFinancList ,newMovimentFinanc, false);
						
						if(currentMovimentFinanc != null){
							createMovFinanc = false;
							updateMovFinac(newMovimentFinanc, currentMovimentFinanc);
						}
						
						break;
					case DIA_SEMANA:
						
						Calendar diaSemana = Calendar.getInstance();
						diaSemana.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),1,0,0,0);
						
						
						while(diaSemana.get(Calendar.DAY_OF_WEEK) != entradaFixa.getValorDia()){
							diaSemana.add(Calendar.DAY_OF_MONTH, 1);
						}
						int day = diaSemana.get(Calendar.DAY_OF_MONTH) +  (7 * (entradaFixa.getOcorrencia()-1));
						
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
							currentMovimentFinanc = contemEntradaFixaMoviment(movimentFinancList ,newMovimentFinanc, false);
						
						if(currentMovimentFinanc != null){
							createMovFinanc = false;					
							updateMovFinac(newMovimentFinanc, currentMovimentFinanc);
						}
						
						break;
					
					case DIA_UTIL:
						Calendar diaUtil = Calendar.getInstance();
						diaUtil.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), 1,0,0);
						int diasUteis = entradaFixa.getValorDia();
						
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
							currentMovimentFinanc = contemEntradaFixaMoviment(movimentFinancList ,newMovimentFinanc, false);
						
						if(currentMovimentFinanc != null){
							createMovFinanc = false;
							updateMovFinac(newMovimentFinanc, currentMovimentFinanc);
						}
						
						break;
					case ULTIMO_DIA_UTIL:
						Calendar ultimodiaUtil = Calendar.getInstance();
						ultimodiaUtil.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.getActualMaximum(Calendar.DAY_OF_MONTH),0,0);
						int diasUteisl = entradaFixa.getValorDia();
						
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
							currentMovimentFinanc = contemEntradaFixaMoviment(movimentFinancList ,newMovimentFinanc, false);
						
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
	
	private MovimentacaoFinanceira contemEntradaFixaMoviment(List<MovimentacaoFinanceira> movimentFinancList ,MovimentacaoFinanceira newMovimentFinanc, boolean verifyWeek){
		
		Collections.sort(movimentFinancList, EntradaFixa.movimentFinancComparator);
		
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
	
	private boolean generateMovimenforMonth(int month, EntradaFixa entradaFixa ){
		boolean generateMovimen = false;
		
		switch (month) {
		case Calendar.JANUARY:
			generateMovimen = entradaFixa.isJan();
			break;
		case Calendar.FEBRUARY:
			generateMovimen = entradaFixa.isFev();
			break;
		case Calendar.MARCH:
			generateMovimen = entradaFixa.isMar();
			break;
		case Calendar.APRIL:
			generateMovimen = entradaFixa.isAbr();
			break;
		case Calendar.MAY:
			generateMovimen = entradaFixa.isMai();
			break;
		case Calendar.JUNE:
			generateMovimen = entradaFixa.isJun();
			break;
		case Calendar.JULY:
			generateMovimen = entradaFixa.isJul();
			break;
		case Calendar.AUGUST:
			generateMovimen = entradaFixa.isAgo();
			break;
		case Calendar.SEPTEMBER:
			generateMovimen = entradaFixa.isSet();
			break;
		case Calendar.OCTOBER:
			generateMovimen = entradaFixa.isOut();
			break;
		case Calendar.NOVEMBER:
			generateMovimen = entradaFixa.isNov();
			break;
		case Calendar.DECEMBER:
			generateMovimen = entradaFixa.isDez();
			break;
		default:
			break;
		}
		
		return generateMovimen;
	}
	
}
