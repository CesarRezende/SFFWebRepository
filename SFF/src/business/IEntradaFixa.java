package business;

import data.EntradaFixa;
import data.Usuario;
import data.EntradaFixa.TipoDia;

public interface IEntradaFixa {

	public void generateMovimentacoesFinanc(EntradaFixa entradaFixa, boolean deleteMovFinancPrevisto);
	
	public void saveEntradaFixa(EntradaFixa entradaFixa);
	
	public void saveEntradaFixa(EntradaFixa entradaFixa, Usuario usuario, TipoDia tipoDia  );
	
	public void disableEntradaFixa(EntradaFixa entradaFixa, Usuario usuario, TipoDia tipoDia);
	
}
