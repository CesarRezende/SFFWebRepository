package business;

import data.SaidaFixa;
import data.TipoGasto;
import data.Usuario;
import data.SaidaFixa.TipoDia;

public interface ISaidaFixa {

	public void generateMovimentacoesFinanc(SaidaFixa saidaFixa, boolean deleteMovFinancPrevisto);
	
	public void saveSaidaFixa(SaidaFixa saidaFixa);
	
	public void saveSaidaFixa(SaidaFixa saidaFixa,TipoGasto tipoGasto, Usuario usuario, TipoDia tipoDia  );
	
	public void disableSaidaFixa(SaidaFixa saidaFixa,TipoGasto tipoGasto, Usuario usuario, TipoDia tipoDia);
	
}
