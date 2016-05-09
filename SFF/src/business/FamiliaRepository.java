package business;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import data.Familia;

public class FamiliaRepository extends GenericRepository {

	
	public FamiliaRepository() {
		super();
	}
	
	public FamiliaRepository(HttpServletRequest request) {
		super(request);
	}

	public List<Familia> getAllFamilia() {

		return super.getAll(Familia.class);

	}
	

	public void saveFamilia(Familia familia) {

		super.save(familia);
	}
	public void removeFamilia(Familia familia) {

		super.remove(familia);
	}

	public void updateFamilia(Familia familia) {

		super.update(familia);
	}
	
	public boolean hasDependences(Long familiaId, boolean newTransaction){
		boolean hasDependences = false;
		Long result = null;
		if(newTransaction){
			
			getnewEntityManager();
			this.entityManager.getTransaction().begin();
			
			try{
				TypedQuery<Long> query = this.entityManager.createQuery("Select Count(u.id) From Usuario u where u.familia.id = :familia",Long.class);
				query.setParameter("familia", familiaId);
				result = query.getSingleResult();
				this.entityManager.getTransaction().commit();
			}catch(Exception e){
				e.printStackTrace();
				this.entityManager.getTransaction().rollback();
			}finally{				
				entityManager.close();
			}
			
			
			hasDependences = (result != null && result > 0);
			
		}else{
			updateEntityManager();
			
			TypedQuery<Long> query = this.entityManager.createQuery("Select Count(m.id) From MovimentacaoFinanceira m  where m.familia.id = :familia",Long.class);
			query.setParameter("familia", familiaId);
			result = query.getSingleResult();
			
			hasDependences = (result != null && result > 0);
			
		}
		
		
		return hasDependences;
	}
	

	public Familia findFamilia(Long id) {
		updateEntityManager();
		return this.entityManager.find(Familia.class, id);

	}
	

}
