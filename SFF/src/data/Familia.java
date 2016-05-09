package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;

/**
 * Entity implementation class for Entity: Familia
 *
 */
@Entity
@Audited
@Table(name="familia")
public class Familia  extends GenericEntity implements Serializable {


	@Id @GeneratedValue
	private Long id;
	
	@Version
	private Long versao;
	
	private String descricao;
	
	private static final long serialVersionUID = 1L;
	
	private boolean desativado;
	
	@Transient
	private static FamiliaComparator comparator = new FamiliaComparator();;
	
	@Transient
	private boolean newRecord = false;
	
	@OneToMany(mappedBy="familia")
	private List<Usuario> usuarios  = new ArrayList<Usuario>();

	@OneToMany(mappedBy="familia")
	private List<TipoGasto> tipoGastos  = new ArrayList<TipoGasto>();
	
	public Familia() {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Familia [id=" + id + ", descricao=" + descricao + "]";
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

	public static FamiliaComparator getComparator() {
		return comparator;
	}


	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}


	private static class FamiliaComparator implements Comparator<Familia>{

		@Override
		public int compare(Familia arg0, Familia arg1) {
			
			return arg0.getDescricao().compareTo(arg1.getDescricao());
		}
		
	}
	
   
}
