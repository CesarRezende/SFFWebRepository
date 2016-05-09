package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;

/**
 * Entity implementation class for Entity: SaidasFixas
 *
 */
@Entity
@Audited
@Table(name="saida_fixa")
public class SaidaFixa  extends GenericEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Transient
	public
	static MovimentFinancComparator movimentFinancComparator = new MovimentFinancComparator();
	
	@Id @GeneratedValue
	private Long id;
	
	@Version
	private Long versao;
	
	private String descricao;
	
	private double valor;
	
	@Column(name="data_lancamento")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataLancamento;
	
	@OneToOne
	@JoinColumn(name="tipo_gastoid")
	private TipoGasto tipoGasto;
	
	@ManyToOne
	@JoinColumn(name="usuarioid")
	private Usuario usuario;
	
	@Column(name="tipo_dia")
	private TipoDia tipoDia;
	
	@Column(name="valor_dia")
	private int valorDia; 
	
	@OneToMany(mappedBy="saidaFixa", cascade=CascadeType.ALL ,orphanRemoval=true)
	private List<MovimentacaoFinanceira> movimentacoesfinanc = new ArrayList<MovimentacaoFinanceira>();
	
	private int ocorrencia; 
	
	private boolean jan = true;
	
	private boolean fev = true;
	
	private boolean mar = true;
	
	private boolean abr = true;
	
	private boolean mai = true;
	
	private boolean jun = true;
	
	private boolean jul = true;
	
	private boolean ago = true;
	
	private boolean set = true;
	
	private boolean out = true;
	
	private boolean nov = true;
	
	private boolean dez = true;
	
	private boolean desativado = false;
	
	@Transient
	private boolean newRecord = false;

	public SaidaFixa() {
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

	public static long getSerialversionuid() {
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

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public TipoDia getTipoDia() {
		return tipoDia;
	}

	public void setTipoDia(TipoDia tipoDia) {
		this.tipoDia = tipoDia;
	}

	public int getValorDia() {
		return valorDia;
	}

	public void setValorDia(int valorDia) {
		this.valorDia = valorDia;
	}

	public int getOcorrencia() {
		return ocorrencia;
	}

	public void setOcorrencia(int ocorrencia) {
		this.ocorrencia = ocorrencia;
	}
	
	public static enum TipoDia{ 
		DIA_MES("Dia Mês")
		,DIA_SEMANA("Dia Semana")
		,DIA_UTIL("Dia Útil")
		,ULTIMO_DIA_UTIL("Último Dia Útil");
	
	private String label;

    private TipoDia(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
		
}

	public boolean isJan() {
		return jan;
	}

	public void setJan(boolean jan) {
		this.jan = jan;
	}

	public boolean isFev() {
		return fev;
	}

	public void setFev(boolean fev) {
		this.fev = fev;
	}

	public boolean isMar() {
		return mar;
	}

	public void setMar(boolean mar) {
		this.mar = mar;
	}

	public boolean isAbr() {
		return abr;
	}

	public void setAbr(boolean abr) {
		this.abr = abr;
	}

	public boolean isMai() {
		return mai;
	}

	public void setMai(boolean mai) {
		this.mai = mai;
	}

	public boolean isJun() {
		return jun;
	}

	public void setJun(boolean jun) {
		this.jun = jun;
	}

	public boolean isJul() {
		return jul;
	}

	public void setJul(boolean jul) {
		this.jul = jul;
	}

	public boolean isAgo() {
		return ago;
	}

	public void setAgo(boolean ago) {
		this.ago = ago;
	}

	public boolean isSet() {
		return set;
	}

	public void setSet(boolean set) {
		this.set = set;
	}

	public boolean isOut() {
		return out;
	}

	public void setOut(boolean out) {
		this.out = out;
	}

	public boolean isNov() {
		return nov;
	}

	public void setNov(boolean nov) {
		this.nov = nov;
	}

	public boolean isDez() {
		return dez;
	}

	public void setDez(boolean dez) {
		this.dez = dez;
	}

	public List<MovimentacaoFinanceira> getMovimentacoesfinanc() {
		return movimentacoesfinanc;
	}

	public void setMovimentacoesfinanc(List<MovimentacaoFinanceira> movimentacoesfinanc) {
		this.movimentacoesfinanc = movimentacoesfinanc;
	}
	
	public boolean isDesativado() {
		return desativado;
	}

	public void setDesativado(boolean desativado) {
		this.desativado = desativado;
	}

	private static class MovimentFinancComparator implements Comparator<MovimentacaoFinanceira>{

		@Override
		public int compare(MovimentacaoFinanceira arg0, MovimentacaoFinanceira arg1) {
			
			int compare = 0;
			
			compare = arg0.getDataPrevista().compareTo(arg1.getDataPrevista());
			
			if(compare == 0){
				compare = arg0.getSituacao().compareTo(arg1.getSituacao());
			}
			
			return compare;
		}
		
	}
   
}
