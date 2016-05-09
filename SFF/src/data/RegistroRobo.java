package data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name="registro_robos")
public class RegistroRobo {
	
	@Id @GeneratedValue
	private Long id;
	
	@Version
	private Long versao;
	
	private int ano;
	
	private int mes;
	
	@Column(name="data_execucao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataExecucao;
	
	@Column(name="data_termino")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataTermino;
	
	@Column(name="robo_nome")
	private String roboNome;
	
	private String obs;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public Date getDataExecucao() {
		return dataExecucao;
	}

	public void setDataExecucao(Date dataExecucao) {
		this.dataExecucao = dataExecucao;
	}

	public String getRoboNome() {
		return roboNome;
	}

	public void setRoboNome(String roboNome) {
		this.roboNome = roboNome;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Date getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}
	
	
	

}
