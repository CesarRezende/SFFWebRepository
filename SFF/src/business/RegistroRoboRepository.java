package business;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import data.Familia;
import data.RegistroRobo;
import data.TipoGasto;

public class RegistroRoboRepository extends GenericRepository {

	public RegistroRoboRepository() {
		super();
	}

	public RegistroRoboRepository(HttpServletRequest request) {
		super(request);
	}

	public List<RegistroRobo> getAllRegistroRobo() {

		return super.getAll(RegistroRobo.class);

	}

	public boolean existsRegistroRobo(String roboName, int year,
			int month) {
		updateEntityManager();

		TypedQuery<Long> query = this.entityManager
				.createQuery(
						"Select "+
						"Count(rb.id) "+
						"From RegistroRobo rb  "+
						"WHERE "+
						"rb.roboNome = :roboname "+ 
						"AND rb.ano = :year  " +
						"AND rb.mes = :month ",
						Long.class);
		query.setParameter("roboname", roboName);
		query.setParameter("year", year);
		query.setParameter("month", month);
		Long result = query.getSingleResult();

		return result > 0;
	}

	public void saveRegistroRobo(RegistroRobo registroRobo) {

		super.save(registroRobo);
	}

	public void removeRegistroRobo(RegistroRobo registroRobo) {

		super.remove(registroRobo);
	}

	public void updateRegistroRobo(RegistroRobo registroRobo) {

		super.update(registroRobo);
	}

	public RegistroRobo findRegistroRobo(Long id) {
		updateEntityManager();
		return this.entityManager.find(RegistroRobo.class, id);

	}
}
