package business;

import data.EntradaVariavel;
import data.Usuario;
import data.EntradaVariavel.TipoDia;

public interface IEntradaVariavel {

	public void generateMovimentacoesFinanc(EntradaVariavel entradaVariavel);
	
	public void saveEntradaVariavel(EntradaVariavel entradaVariavel) throws ErrorMessageException;
	
	public void saveEntradaVariavel(EntradaVariavel entradaVariavel, Usuario usuario, TipoDia tipoDia  ) throws ErrorMessageException;
	
	public void calcGastoTotal(EntradaVariavel entradaVariavel);
	
	public void calcGastoTotalRestante(EntradaVariavel entradaVariavel);
}
