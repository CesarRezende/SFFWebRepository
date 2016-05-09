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
import data.EntradaVariavel;
import data.Usuario;

public class EntradaVariavelRepository extends GenericRepository {

	
	public EntradaVariavelRepository() {
		super();
	}
	
	public EntradaVariavelRepository(HttpServletRequest request) {
		super(request);
	}

	public List<EntradaVariavel> getAllEntradaVariavel() {

		return super.getAll(EntradaVariavel.class);

	}
	
	
	public List<EntradaVariavel> getEntradasVariaveis(Familia familia) {
		updateEntityManager();
		CriteriaBuilder criteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<EntradaVariavel> criteriaQuery = criteriaBuilder.createQuery(EntradaVariavel.class);
		Root<EntradaVariavel> root = criteriaQuery.from(EntradaVariavel.class);
		Join<EntradaVariavel, Usuario> joinUsr = root.join("usuario", JoinType.INNER);
		root.fetch("usuario", JoinType.INNER);
		criteriaQuery.select(root);
		
		criteriaQuery.where(criteriaBuilder.equal(joinUsr.<Familia>get("familia"), familia));
		
		criteriaQuery.orderBy(criteriaBuilder.asc(root.<String>get("status")),
							  criteriaBuilder.asc(root.<String>get("descricao")));
		
		TypedQuery<EntradaVariavel> query = this.entityManager.createQuery(criteriaQuery);
		
		List<EntradaVariavel> listEntity = query.getResultList();

		return listEntity;
	}
	
	public List<EntradaVariavel> getEntradasVariavelNotCanceled() {
		updateEntityManager();
		
		CriteriaBuilder criteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<EntradaVariavel> criteriaQuery = criteriaBuilder.createQuery(EntradaVariavel.class);
		Root<EntradaVariavel> root = criteriaQuery.from(EntradaVariavel.class);
		root.fetch("usuario", JoinType.INNER);
		root.fetch("movimentacoesfinanc", JoinType.LEFT);
		criteriaQuery.select(root);
		criteriaQuery.distinct(true);
		
		criteriaQuery.where(criteriaBuilder.equal(root.<Boolean>get("desativado"), false));

		TypedQuery<EntradaVariavel> query = this.entityManager.createQuery(criteriaQuery);
		

		return query.getResultList();


	}
	

	public void saveEntradaVariavel(EntradaVariavel entradaVariavel) {

		super.save(entradaVariavel);
	}
	public void removeEntradaVariavel(EntradaVariavel entradaVariavel) {

		super.remove(entradaVariavel);
	}

	public void updateEntradaVariavel(EntradaVariavel entradaVariavel) {

		super.update(entradaVariavel);
	}
	
	public boolean hasDependences(EntradaVariavel entradaVariavel){
		boolean hasDependences = false;
		
		Predicate condition = new Predicate() {
			   public boolean evaluate(Object movimObject) {
			        return ((MovimentacaoFinanceira)movimObject).getSituacao() == 'R';
			   }
			};
			@SuppressWarnings("unchecked")
			List<MovimentacaoFinanceira> result = (List<MovimentacaoFinanceira>) CollectionUtils.select( entradaVariavel.getMovimentacoesfinanc(), condition );
			
			if(result != null)
			hasDependences = result.size() > 0;
			
		return hasDependences;
	}
	

	public EntradaVariavel findEntradaVariavel(Long id) {
		updateEntityManager();
		
		CriteriaBuilder criteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<EntradaVariavel> criteriaQuery = criteriaBuilder.createQuery(EntradaVariavel.class);
		Root<EntradaVariavel> root = criteriaQuery.from(EntradaVariavel.class);
		root.fetch("usuario", JoinType.INNER);
		root.fetch("movimentacoesfinanc", JoinType.LEFT);
		criteriaQuery.select(root);
		
		criteriaQuery.where(criteriaBuilder.equal(root.<Long>get("id"), id));

		TypedQuery<EntradaVariavel> query = this.entityManager.createQuery(criteriaQuery);
		
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}

	}
	

}
