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
import data.EntradaFixa;
import data.Usuario;

public class EntradaFixaRepository extends GenericRepository {

	
	public EntradaFixaRepository() {
		super();
	}
	
	public EntradaFixaRepository(HttpServletRequest request) {
		super(request);
	}

	public List<EntradaFixa> getAllEntradaFixa() {

		return super.getAll(EntradaFixa.class);

	}
	
	
	public List<EntradaFixa> getEntradasFixas(Familia familia) {
		updateEntityManager();
		CriteriaBuilder criteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<EntradaFixa> criteriaQuery = criteriaBuilder.createQuery(EntradaFixa.class);
		Root<EntradaFixa> root = criteriaQuery.from(EntradaFixa.class);
		Join<EntradaFixa, Usuario> joinUsr = root.join("usuario", JoinType.INNER);
		root.fetch("usuario", JoinType.INNER);
		criteriaQuery.select(root);
		
		criteriaQuery.where(criteriaBuilder.equal(joinUsr.<Familia>get("familia"), familia));
		
		criteriaQuery.orderBy(criteriaBuilder.asc(root.<String>get("descricao")));

		TypedQuery<EntradaFixa> query = this.entityManager.createQuery(criteriaQuery);
		List<EntradaFixa> listEntity = query.getResultList();

		return listEntity;
	}
	
	public List<EntradaFixa> getEntradasFixasNotCanceled() {
		updateEntityManager();
		
		CriteriaBuilder criteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<EntradaFixa> criteriaQuery = criteriaBuilder.createQuery(EntradaFixa.class);
		Root<EntradaFixa> root = criteriaQuery.from(EntradaFixa.class);
		root.fetch("usuario", JoinType.INNER);
		root.fetch("movimentacoesfinanc", JoinType.LEFT);
		criteriaQuery.select(root);
		criteriaQuery.distinct(true);
		
		criteriaQuery.where(criteriaBuilder.equal(root.<Boolean>get("desativado"), false));

		TypedQuery<EntradaFixa> query = this.entityManager.createQuery(criteriaQuery);
		

		return query.getResultList();


	}
	

	public void saveEntradaFixa(EntradaFixa entradaFixa) {

		super.save(entradaFixa);
	}
	public void removeEntradaFixa(EntradaFixa entradaFixa) {

		super.remove(entradaFixa);
	}

	public void updateEntradaFixa(EntradaFixa entradaFixa) {

		super.update(entradaFixa);
	}
	
	public boolean hasDependences(EntradaFixa entradaFixa){
		boolean hasDependences = false;
		
		Predicate condition = new Predicate() {
			   public boolean evaluate(Object movimObject) {
			        return ((MovimentacaoFinanceira)movimObject).getSituacao() == 'R';
			   }
			};
			@SuppressWarnings("unchecked")
			List<MovimentacaoFinanceira> result = (List<MovimentacaoFinanceira>) CollectionUtils.select( entradaFixa.getMovimentacoesfinanc(), condition );
			
			if(result != null)
			hasDependences = result.size() > 0;
			
		return hasDependences;
	}
	

	public EntradaFixa findEntradaFixa(Long id) {
		updateEntityManager();
		
		CriteriaBuilder criteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<EntradaFixa> criteriaQuery = criteriaBuilder.createQuery(EntradaFixa.class);
		Root<EntradaFixa> root = criteriaQuery.from(EntradaFixa.class);
		root.fetch("usuario", JoinType.INNER);
		root.fetch("movimentacoesfinanc", JoinType.LEFT);
		criteriaQuery.select(root);
		
		criteriaQuery.where(criteriaBuilder.equal(root.<Long>get("id"), id));

		TypedQuery<EntradaFixa> query = this.entityManager.createQuery(criteriaQuery);
		
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}

	}
	

}
