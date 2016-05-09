package business;

import data.SaidaVariavel;
import data.TipoGasto;
import data.Usuario;
import data.SaidaVariavel.TipoDia;

public interface ISaidaVariavel {

	public void generateMovimentacoesFinanc(SaidaVariavel saidaVariavel);
	
	public void saveSaidaVariavel(SaidaVariavel saidaVariavel) throws ErrorMessageException;
	
	public void saveSaidaVariavel(SaidaVariavel saidaVariavel,TipoGasto tipoGasto, Usuario usuario, TipoDia tipoDia  ) throws ErrorMessageException;
	
	public void calcGastoTotal(SaidaVariavel saidaVariavel);
	
	public void calcGastoTotalRestante(SaidaVariavel saidaVariavel);
}
