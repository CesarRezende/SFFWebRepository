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
import data.SaidaFixa;
import data.Usuario;

public class SaidaFixaRepository extends GenericRepository {

	
	public SaidaFixaRepository() {
		super();
	}
	
	public SaidaFixaRepository(HttpServletRequest request) {
		super(request);
	}

	public List<SaidaFixa> getAllSaidaFixa() {

		return super.getAll(SaidaFixa.class);

	}
	
	
	public List<SaidaFixa> getSaidasFixas(Familia familia) {
		updateEntityManager();
		CriteriaBuilder criteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<SaidaFixa> criteriaQuery = criteriaBuilder.createQuery(SaidaFixa.class);
		Root<SaidaFixa> root = criteriaQuery.from(SaidaFixa.class);
		Join<SaidaFixa, Usuario> joinUsr = root.join("usuario", JoinType.INNER);
		root.fetch("usuario", JoinType.INNER);
		root.fetch("tipoGasto", JoinType.LEFT);
		criteriaQuery.select(root);
		
		criteriaQuery.where(criteriaBuilder.equal(joinUsr.<Familia>get("familia"), familia));
		
		criteriaQuery.orderBy(criteriaBuilder.asc(root.<String>get("descricao")));

		TypedQuery<SaidaFixa> query = this.entityManager.createQuery(criteriaQuery);
		List<SaidaFixa> listEntity = query.getResultList();

		return listEntity;
	}
	
	public List<SaidaFixa> getSaidasFixasNotCanceled() {
		updateEntityManager();
		
		CriteriaBuilder criteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<SaidaFixa> criteriaQuery = criteriaBuilder.createQuery(SaidaFixa.class);
		Root<SaidaFixa> root = criteriaQuery.from(SaidaFixa.class);
		root.fetch("usuario", JoinType.INNER);
		root.fetch("tipoGasto", JoinType.LEFT);
		root.fetch("movimentacoesfinanc", JoinType.LEFT);
		criteriaQuery.select(root);
		criteriaQuery.distinct(true);
		
		criteriaQuery.where(criteriaBuilder.equal(root.<Boolean>get("desativado"), false));

		TypedQuery<SaidaFixa> query = this.entityManager.createQuery(criteriaQuery);
		

		return query.getResultList();


	}
	

	public void saveSaidaFixa(SaidaFixa saidaFixa) {

		super.save(saidaFixa);
	}
	public void removeSaidaFixa(SaidaFixa saidaFixa) {

		super.remove(saidaFixa);
	}

	public void updateSaidaFixa(SaidaFixa saidaFixa) {

		super.update(saidaFixa);
	}
	
	public boolean hasDependences(SaidaFixa saidaFixa){
		boolean hasDependences = false;
		
		Predicate condition = new Predicate() {
			   public boolean evaluate(Object movimObject) {
			        return ((MovimentacaoFinanceira)movimObject).getSituacao() == 'R';
			   }
			};
			@SuppressWarnings("unchecked")
			List<MovimentacaoFinanceira> result = (List<MovimentacaoFinanceira>) CollectionUtils.select( saidaFixa.getMovimentacoesfinanc(), condition );
			
			if(result != null)
			hasDependences = result.size() > 0;
			
		return hasDependences;
	}
	

	public SaidaFixa findSaidaFixa(Long id) {
		updateEntityManager();
		
		CriteriaBuilder criteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<SaidaFixa> criteriaQuery = criteriaBuilder.createQuery(SaidaFixa.class);
		Root<SaidaFixa> root = criteriaQuery.from(SaidaFixa.class);
		root.fetch("usuario", JoinType.INNER);
		root.fetch("tipoGasto", JoinType.LEFT);
		root.fetch("movimentacoesfinanc", JoinType.LEFT);
		criteriaQuery.select(root);
		
		criteriaQuery.where(criteriaBuilder.equal(root.<Long>get("id"), id));

		TypedQuery<SaidaFixa> query = this.entityManager.createQuery(criteriaQuery);
		
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}

	}
	

}
