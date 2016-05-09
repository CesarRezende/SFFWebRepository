package controller;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.WebParam;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

	@WebService
	@SOAPBinding(style = Style.RPC)
	public interface WebServiceInterface {
	 
	    @WebMethod
	    String printMessage(@WebParam(name="name")String name);
	 
	    @WebMethod
	    public String removeTipoGasto(@WebParam(name="id")Long id);
	    
	    @WebMethod
	    public String removeMovFinanc(@WebParam(name="id")Long id);
	    
	    @WebMethod
	    public String removeMovFinancExternal(@WebParam(name="userId")String userId, @WebParam(name="password")String password,@WebParam(name="id")String id);
	    
	    @WebMethod
	    public String removeFamilia(@WebParam(name="id")Long id);
	    
	    @WebMethod
	    public String removeUsuario(@WebParam(name="id")Long id);
	    
	    @WebMethod
	    public String removeSaidaFixa(@WebParam(name="id")Long id);

	    @WebMethod
	    public String removeEntradaFixa(@WebParam(name="id")Long id);

	    @WebMethod
	    public String removeSaidaVariavel(@WebParam(name="id")Long id);
	    
	    @WebMethod
	    public String removeEntradaVariavel(@WebParam(name="id")Long id);
	    
	    @WebMethod
	    public boolean hasConnection();
	    
	    
	    @WebMethod
	    public boolean validateUser(@WebParam(name="userId")String userId,@WebParam(name="password")String password);
	    
	    @WebMethod
	    public String getMovimentacaoFinanceiraFromMonth(@WebParam(name="yearMonth")String yearMonth, @WebParam(name="userId")String usuarioId, @WebParam(name="password")String password);
	    
	    @WebMethod
	    public String editMovimentacaoFinanceira(@WebParam(name="userId")String usuarioId, @WebParam(name="password")String password, @WebParam(name="id")String id, @WebParam(name="descricao")String descricao, @WebParam(name="tipoMovimentacao")String tipoMovimentacao, @WebParam(name="valor")String valor, @WebParam(name="juros")String juros, @WebParam(name="multa")String multa, @WebParam(name="desconto")String desconto, @WebParam(name="situacao")String situacao, @WebParam(name="dataPrevista")String dataPrevista, @WebParam(name="dataRealizada")String dataRealizada, @WebParam(name="tipoGastoId")String tipoGastoId );
	    
	    @WebMethod
	    public String insertMovimentacaoFinanceira(@WebParam(name="userId")String usuarioId, @WebParam(name="password")String password, @WebParam(name="id")String id, @WebParam(name="descricao")String descricao, @WebParam(name="tipoMovimentacao")String tipoMovimentacao, @WebParam(name="valor")String valor, @WebParam(name="juros")String juros, @WebParam(name="multa")String multa, @WebParam(name="desconto")String desconto, @WebParam(name="situacao")String situacao, @WebParam(name="dataPrevista")String dataPrevista, @WebParam(name="dataRealizada")String dataRealizada, @WebParam(name="tipoGastoId")String tipoGastoId );
	    
	    @WebMethod
	    public String getAllTipoGasto(@WebParam(name="userId")String usuarioId, @WebParam(name="password")String password);
	    
	    @WebMethod
	    public String fulfillMovFinanc(@WebParam(name="id")Long id);
	    
	    @WebMethod
	    public String fulfillMovFinancExternal(@WebParam(name="userId")String userId, @WebParam(name="password")String password,@WebParam(name="id")String id);
	    
	    @WebMethod
	    public String getTipoGastoChart(@WebParam(name="yearMonth")String yearMonth, @WebParam(name="userId")String userId, @WebParam(name="password")String password);
	    
	    @WebMethod
	    public String getSpendingsChart(@WebParam(name="yearMonth")String yearMonth, @WebParam(name="userId")String userId, @WebParam(name="password")String password);
	    
	    @WebMethod
	    public String getCurrentSpendingsChart(@WebParam(name="yearMonth")String yearMonth, @WebParam(name="userId")String userId, @WebParam(name="password")String password);
	    
	}
