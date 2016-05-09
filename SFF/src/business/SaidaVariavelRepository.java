package business;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import data.Familia;
import data.MovimentacaoFinanceira;
import data.SaidaVariavel;
import data.Usuario;

public class SaidaVariavelRepository extends GenericRepository {

	
	public SaidaVariavelRepository() {
		super();
	}
	
	public SaidaVariavelRepository(HttpServletRequest request) {
		super(request);
	}

	public List<SaidaVariavel> getAllSaidaVariavel() {

		return super.getAll(SaidaVariavel.class);

	}
	
	
	public List<SaidaVariavel> getSaidasVariaveis(Familia familia) {
		updateEntityManager();
		CriteriaBuilder criteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<SaidaVariavel> criteriaQuery = criteriaBuilder.createQuery(SaidaVariavel.class);
		Root<SaidaVariavel> root = criteriaQuery.from(SaidaVariavel.class);
		Join<SaidaVariavel, Usuario> joinUsr = root.join("usuario", JoinType.INNER);
		root.fetch("usuario", JoinType.INNER);
		root.fetch("tipoGasto", JoinType.LEFT);
		criteriaQuery.select(root);
		
		criteriaQuery.where(criteriaBuilder.equal(joinUsr.<Familia>get("familia"), familia));
		
		criteriaQuery.orderBy(criteriaBuilder.asc(root.<String>get("status")),
							  criteriaBuilder.asc(root.<String>get("descricao")));
		
		TypedQuery<SaidaVariavel> query = this.entityManager.createQuery(criteriaQuery);
		
		List<SaidaVariavel> listEntity = query.getResultList();

		return listEntity;
	}
	
	public List<SaidaVariavel> getSaidasVariavelNotCanceled() {
		updateEntityManager();
		
		CriteriaBuilder criteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<SaidaVariavel> criteriaQuery = criteriaBuilder.createQuery(SaidaVariavel.class);
		Root<SaidaVariavel> root = criteriaQuery.from(SaidaVariavel.class);
		root.fetch("usuario", JoinType.INNER);
		root.fetch("tipoGasto", JoinType.LEFT);
		root.fetch("movimentacoesfinanc", JoinType.LEFT);
		criteriaQuery.select(root);
		criteriaQuery.distinct(true);
		
		criteriaQuery.where(criteriaBuilder.equal(root.<Boolean>get("desativado"), false));

		TypedQuery<SaidaVariavel> query = this.entityManager.createQuery(criteriaQuery);
		

		return query.getResultList();


	}
	

	public void saveSaidaVariavel(SaidaVariavel saidaVariavel) {

		super.save(saidaVariavel);
	}
	public void removeSaidaVariavel(SaidaVariavel saidaVariavel) {

		super.remove(saidaVariavel);
	}

	public void updateSaidaVariavel(SaidaVariavel saidaVariavel) {

		super.update(saidaVariavel);
	}
	
	public boolean hasDependences(SaidaVariavel saidaVariavel){
		boolean hasDependences = false;
		
		Predicate condition = new Predicate() {
			   public boolean evaluate(Object movimObject) {
			        return ((MovimentacaoFinanceira)movimObject).getSituacao() == 'R';
			   }
			};
			@SuppressWarnings("unchecked")
			List<MovimentacaoFinanceira> result = (List<MovimentacaoFinanceira>) CollectionUtils.select( saidaVariavel.getMovimentacoesfinanc(), condition );
			
			if(result != null)
			hasDependences = result.size() > 0;
			
		return hasDependences;
	}
	

	public SaidaVariavel findSaidaVariavel(Long id) {
		updateEntityManager();
		
		CriteriaBuilder criteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<SaidaVariavel> criteriaQuery = criteriaBuilder.createQuery(SaidaVariavel.class);
		Root<SaidaVariavel> root = criteriaQuery.from(SaidaVariavel.class);
		root.fetch("usuario", JoinType.INNER);
		root.fetch("tipoGasto", JoinType.LEFT);
		root.fetch("movimentacoesfinanc", JoinType.LEFT);
		criteriaQuery.select(root);
		
		criteriaQuery.where(criteriaBuilder.equal(root.<Long>get("id"), id));

		TypedQuery<SaidaVariavel> query = this.entityManager.createQuery(criteriaQuery);
		
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}

	}
	

}
