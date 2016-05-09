package data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.persistence.*;

import org.hibernate.envers.Audited;

/**
 * Entity implementation class for Entity: MovimentacaoFinanceira
 *
 */
@Entity
@Audited
@Table(name="movimentacao_financeira")
public class MovimentacaoFinanceira  extends GenericEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue
	private Long id;
	
	@Version
	private Long versao;
	
	private String descricao;
	
	@Column(name="tipo_movimentacao")
	private Character tipoMovimentacao;
	
	private double valor;
	
	private double juros;
	
	private double multa;
	
	private double desconto;
	
	private Character situacao; 
	
	@Column(name="data_prevista")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPrevista;
	
	@Column(name="data_realizada")
	@Temporal(TemporalType.TIMESTAMP )
	private Date dataRealizada;
	
	@Column(name="data_lancamento")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataLancamento;
	
	@OneToOne
	@JoinColumn(name="tipo_gastoid")
	private TipoGasto tipoGasto;
	
	@ManyToOne
	@JoinColumn(name="usuarioid")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name="saidafixaid")
	private SaidaFixa saidaFixa;
	
	@ManyToOne
	@JoinColumn(name="saidavariavelid")
	private SaidaVariavel saidaVariavel;
	
	@ManyToOne
	@JoinColumn(name="entradavariavelid")
	private EntradaVariavel entradaVariavel;
	
	@ManyToOne
	@JoinColumn(name="entradafixaid")
	private EntradaFixa entradaFixa;
	
	private boolean manual = false;
	
	@Transient
	private boolean newRecord = false;

	public MovimentacaoFinanceira() {
		super();
		this.setTipoGasto(new TipoGasto());
	}

	//Getters & Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	public boolean isNewRecord() {
		return newRecord;
	}

	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}

	@Override
	public boolean isDuplicatedEntity() {
		// TODO Auto-generated method stub
		return false;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

	public Character getTipoMovimentacao() {
		return tipoMovimentacao;
	}

	public void setTipoMovimentacao(Character tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public double getJuros() {
		return juros;
	}

	public void setJuros(double juros) {
		this.juros = juros;
	}

	public double getMulta() {
		return multa;
	}

	public void setMulta(double multa) {
		this.multa = multa;
	}

	public double getDesconto() {
		return desconto;
	}

	public void setDesconto(double desconto) {
		this.desconto = desconto;
	}
	
	public double getValorTotal() {
		double result = this.valor - this.desconto + this.multa + this.juros; 
		return result;
	}

	public Date getDataPrevista() {
		return dataPrevista;
	}

	public void setDataPrevista(Date dataPrevista) {
		this.dataPrevista = dataPrevista;
	}

	public Date getDataRealizada() {
		return dataRealizada;
	}

	public String getFormattedDataRealizada() {
		String returnValue = "Sem Data";
		SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy",new Locale("pt", "BR"));
		
		if(dataRealizada != null){
			Calendar c = Calendar.getInstance();
			c.setTime(dataRealizada);
			
			if(c.get(Calendar.DST_OFFSET) > 0)
				c.add(Calendar.HOUR_OF_DAY, 2);
			else
				c.add(Calendar.HOUR_OF_DAY, 3);
			
			
			returnValue = dataFormatada.format(c.getTime());
		}
		return returnValue;
	}
	
	public void setDataRealizada(Date dataRealizada) {
		this.dataRealizada = dataRealizada;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public TipoGasto getTipoGasto() {
		return tipoGasto;
	}

	public void setTipoGasto(TipoGasto tipoGasto) {
		this.tipoGasto = tipoGasto;
	}

	public Character getSituacao() {
		return situacao;
	}

	public void setSituacao(Character situacao) {
		this.situacao = situacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public SaidaFixa getSaidaFixa() {
		return saidaFixa;
	}

	public void setSaidaFixa(SaidaFixa saidaFixa) {
		this.saidaFixa = saidaFixa;
	}

	public boolean isManual() {
		return manual;
	}

	public void setManual(boolean manual) {
		this.manual = manual;
	}

	public EntradaFixa getEntradaFixa() {
		return entradaFixa;
	}

	public void setEntradaFixa(EntradaFixa entradaFixa) {
		this.entradaFixa = entradaFixa;
	}

	public SaidaVariavel getSaidaVariavel() {
		return saidaVariavel;
	}

	public void setSaidaVariavel(SaidaVariavel saidaVariavel) {
		this.saidaVariavel = saidaVariavel;
	}

	public EntradaVariavel getEntradaVariavel() {
		return entradaVariavel;
	}

	public void setEntradaVariavel(EntradaVariavel entradaVariavel) {
		this.entradaVariavel = entradaVariavel;
	}



	
   
}
