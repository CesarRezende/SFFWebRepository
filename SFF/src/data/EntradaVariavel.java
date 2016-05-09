package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;

/**
 * Entity implementation class for Entity: EntradaVariavels
 * 
 */
@Entity
@Audited
@Table(name = "entrada_variavel")
public class EntradaVariavel extends GenericEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Transient
	public static MovimentFinancComparator movimentFinancComparator = new MovimentFinancComparator();

	@Id
	@GeneratedValue
	private Long id;

	@Version
	private Long versao;

	private String descricao;

	private double valor;
	
	@Column(name = "valor_total")
	private double valorTotal;
	
	@Column(name = "valor_total_restante")
	private double valorTotalRestante;

	@Column(name = "data_lancamento")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataLancamento;

	@ManyToOne
	@JoinColumn(name = "usuarioid")
	private Usuario usuario;

	@Column(name = "tipo_dia")
	private TipoDia tipoDia;

	@Column(name = "valor_dia")
	private int valorDia;

	private int mes;

	private int ano;

	private int ocorrencia;

	private int parcelas;
	
	@Column(name = "parcelas_recebidas")
	private int parcelasRecebidas;
	
	private Status status;

	@Column(name = "data_primeiraparc")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPrimeiraParc;
	
	@OneToMany(mappedBy = "entradaVariavel", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MovimentacaoFinanceira> movimentacoesfinanc = new ArrayList<MovimentacaoFinanceira>();

	@Transient
	private boolean newRecord = false;

	public EntradaVariavel() {
		super();
	}
	
	public static enum Status {
		ABERTO("(A) Aberto"), RECEBIDO("(F) Recebido"), RECEBIDO_PARCIALMENTE("(A) Recebido Parcial");

		private String label;

		private Status(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}
	}
	
	// Getters & Setters



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

	public static enum TipoDia {
		DIA_MES("Dia Mês"), DIA_SEMANA("Dia Semana"), DIA_UTIL("Dia Útil"), ULTIMO_DIA_UTIL(
				"Último Dia Útil");

		private String label;

		private TipoDia(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

	}

	public List<MovimentacaoFinanceira> getMovimentacoesfinanc() {
		return movimentacoesfinanc;
	}

	public void setMovimentacoesfinanc(
			List<MovimentacaoFinanceira> movimentacoesfinanc) {
		this.movimentacoesfinanc = movimentacoesfinanc;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	private static class MovimentFinancComparator implements
			Comparator<MovimentacaoFinanceira> {

		@Override
		public int compare(MovimentacaoFinanceira arg0,
				MovimentacaoFinanceira arg1) {

			int compare = 0;

			compare = arg0.getDataPrevista().compareTo(arg1.getDataPrevista());

			if (compare == 0) {
				compare = arg0.getSituacao().compareTo(arg1.getSituacao());
			}

			return compare;
		}

	}

	public static MovimentFinancComparator getMovimentFinancComparator() {
		return movimentFinancComparator;
	}

	public static void setMovimentFinancComparator(
			MovimentFinancComparator movimentFinancComparator) {
		EntradaVariavel.movimentFinancComparator = movimentFinancComparator;
	}

	public int getParcelas() {
		return parcelas;
	}

	public void setParcelas(int parcelas) {
		this.parcelas = parcelas;
	}

	public Date getDataPrimeiraParc() {
		return dataPrimeiraParc;
	}

	public void setDataPrimeiraParc(Date dataPrimeiraParc) {
		this.dataPrimeiraParc = dataPrimeiraParc;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getParcelasRecebidas() {
		return parcelasRecebidas;
	}

	public void setParcelasRecebidas(int parcelasRecebidas) {
		this.parcelasRecebidas = parcelasRecebidas;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public double getValorTotalRestante() {
		return valorTotalRestante;
	}

	public void setValorTotalRestante(double valorTotalRestante) {
		this.valorTotalRestante = valorTotalRestante;
	}

}
