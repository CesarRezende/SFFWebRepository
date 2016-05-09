package data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;

import business.SFFUtil;

/**
 * Entity implementation class for Entity: Usuario
 * 
 */

@Entity
@Audited
@Table(name = "usuario")
public class Usuario extends GenericEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	private String login;

	private String senha;

	private String nome;

	private boolean administrator;

	@Version
	private Long versao;

	private boolean desativado;

	@ManyToOne(optional = false)
	@JoinColumn(name = "familiaid")
	private Familia familia;
	
	@OneToMany(mappedBy="usuario")
	private List<MovimentacaoFinanceira> movimentacaoFinanceira;

	@OneToMany(mappedBy="usuario")
	private List<SaidaFixa> saidasFixas;
	
	public Usuario() {
		super();
	}

	public void encodeSenha() {
	 this.senha = SFFUtil.encodeSenha(this.senha);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isAdministrator() {
		return administrator;
	}

	public String getAdministratorDescr() {
		return administrator ? "Sim" : "Não";
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
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

	public Familia getFamilia() {
		return familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", login=" + login + ", senha=" + senha
				+ ", nome=" + nome + ", administrator=" + administrator
				+ ", versao=" + versao + ", desativado=" + desativado
				+ ", familia=" + familia + "]";
	}

	public List<MovimentacaoFinanceira> getMovimentacaoFinanceira() {
		return movimentacaoFinanceira;
	}

	public void setMovimentacaoFinanceira(
			List<MovimentacaoFinanceira> movimentacaoFinanceira) {
		this.movimentacaoFinanceira = movimentacaoFinanceira;
	}

}
