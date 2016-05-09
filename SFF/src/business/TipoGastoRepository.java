package business;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import data.Familia;
import data.TipoGasto;

public class TipoGastoRepository extends GenericRepository {

	
	public TipoGastoRepository() {
		super();
	}
	
	public TipoGastoRepository(HttpServletRequest request) {
		super(request);
	}

	public List<TipoGasto> getAllTipoGasto() {

		return super.getAll(TipoGasto.class);

	}
	
	public  List<TipoGasto> getAllTipoGasto(Familia familia) {
		updateEntityManager();
		CriteriaBuilder criteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<TipoGasto> criteriaQuery = criteriaBuilder.createQuery(TipoGasto.class);
		Root<TipoGasto> root = criteriaQuery.from(TipoGasto.class);
		criteriaQuery.select(root);
		
		criteriaQuery.where(criteriaBuilder.equal(root.<Familia>get("familia"), familia));

		TypedQuery<TipoGasto> query = this.entityManager.createQuery(criteriaQuery);
		List<TipoGasto> listEntity = query.getResultList();

		return listEntity;
	}

	public void saveTipoGasto(TipoGasto tipoGasto) {

		super.save(tipoGasto);
	}
	public void removeTipoGasto(TipoGasto tipoGasto) {

		super.remove(tipoGasto);
	}

	public void updateTipoGasto(TipoGasto tipoGasto) {

		super.update(tipoGasto);
	}
	
	public boolean hasDependences(Long tipoGastoId, boolean newTransaction){
		boolean hasDependences = false;
		Long result = null;
		if(newTransaction){
			
			getnewEntityManager();
			this.entityManager.getTransaction().begin();
			
			try{
				TypedQuery<Long> query = this.entityManager.createQuery("Select Count(m.id) From MovimentacaoFinanceira m  where m.tipoGasto.id = :tipogasto",Long.class);
				query.setParameter("tipogasto", tipoGastoId);
				result = query.getSingleResult();
				this.entityManager.getTransaction().commit();
			}catch(Exception e){
				this.entityManager.getTransaction().rollback();
			}finally{				
				entityManager.close();
			}
			
			
			hasDependences = (result != null && result > 0);
			
		}else{
			updateEntityManager();
			
			TypedQuery<Long> query = this.entityManager.createQuery("Select Count(m.id) From MovimentacaoFinanceira m  where m.tipoGasto.id = :tipogasto",Long.class);
			query.setParameter("tipogasto", tipoGastoId);
			result = query.getSingleResult();
			
			hasDependences = (result != null && result > 0);
			
		}
		
		
		return hasDependences;
	}
	

	public TipoGasto findTipoGasto(Long id) {
		updateEntityManager();
		return this.entityManager.find(TipoGasto.class, id);

	}
	

}
