package data;

import java.io.Serializable;
import java.util.Comparator;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

/**
 * Entity implementation class for Entity: TipoGasto
 *
 */
@Entity
@Audited
@Table(name="tipo_gasto")
public class TipoGasto  extends GenericEntity implements Serializable {


	@Id @GeneratedValue
	private Long id;
	
	@Version
	private Long versao;
	
	private String descricao;
	
	private static final long serialVersionUID = 1L;
	
	private boolean desativado;
	
	@Transient
	private static TipoGastoComparator comparator = new TipoGastoComparator();
	
	@Transient
	private boolean newRecord = false;
	
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(optional=false)
	@JoinColumn(name="familiaid")
	private Familia familia;

	public TipoGasto() {
		super();
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

	@Override
	public String toString() {
		return "TipoGasto [id=" + id + ", descricao=" + descricao + "]";
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

	public boolean isDesativado() {
		return desativado;
	}

	public void setDesativado(boolean desativado) {
		this.desativado = desativado;
	}

	public static TipoGastoComparator getComparator() {
		return comparator;
	}


	public Familia getFamilia() {
		return familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
	}


	private static class TipoGastoComparator implements Comparator<TipoGasto>{

		@Override
		public int compare(TipoGasto arg0, TipoGasto arg1) {
			
			return arg0.getDescricao().compareTo(arg1.getDescricao());
		}
		
	}
	
   
}
