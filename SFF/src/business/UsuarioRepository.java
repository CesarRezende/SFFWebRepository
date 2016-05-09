package business;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import data.Familia;
import data.Usuario;

public class UsuarioRepository extends GenericRepository {

	public UsuarioRepository() {
		super();
	}

	public UsuarioRepository(HttpServletRequest request) {
		super(request);
	}

	public List<Usuario> getAllUsuario() {

		return super.getAll(Usuario.class);

	}
	
	public List<Usuario> getAllUsuario(Familia familia) {
		updateEntityManager();
		CriteriaBuilder criteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
		Root<Usuario> root = criteriaQuery.from(Usuario.class);
		criteriaQuery.select(root);
		Predicate familiaClause = criteriaBuilder.equal(root.<Familia>get("familia"), familia);
		
		criteriaQuery.where(familiaClause);

		TypedQuery<Usuario> query = this.entityManager.createQuery(criteriaQuery);
		List<Usuario> listEntity = query.getResultList();

		return listEntity;
	}
	

	public void saveUsuario(Usuario usuario) {

		super.save(usuario);
	}

	public void removeUsuario(Usuario usuario) {

		super.remove(usuario);
	}

	public void updateUsuario(Usuario usuario) {

		super.update(usuario);
	}

	public boolean hasDependences(Long usuarioId, boolean newTransaction) {
		boolean hasDependences = false;
		Long result = null;
		if (newTransaction) {

			getnewEntityManager();
			this.entityManager.getTransaction().begin();

			try {
				TypedQuery<Long> query = this.entityManager
						.createQuery(
								"Select Count(m.id) From MovimentacaoFinanceira m  where m.usuario.id = :usuario",
								Long.class);
				query.setParameter("usuario", usuarioId);
				result = query.getSingleResult();
				this.entityManager.getTransaction().commit();
			} catch (Exception e) {
				this.entityManager.getTransaction().rollback();
			} finally {
				entityManager.close();
			}

			hasDependences = (result != null && result > 0);

		} else {
			updateEntityManager();

			TypedQuery<Long> query = this.entityManager
					.createQuery(
							"Select Count(m.id) From MovimentacaoFinanceira m  where m.usuario.id = :usuario",
							Long.class);
			query.setParameter("usuario", usuarioId);
			result = query.getSingleResult();

			hasDependences = (result != null && result > 0);

		}

		return hasDependences;
	}

	public Usuario findUsuario(Long id) {
		updateEntityManager();
		return this.entityManager.find(Usuario.class, id);

	}

	public Usuario findUsuario(String login) {
		Usuario usuario = null;

		updateEntityManager();
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);
		Root<Usuario> root = cq.from(Usuario.class);
		cq.select(root);

		cq.where(cb.equal(root.<String> get("login"), login));
		TypedQuery<Usuario> query = this.entityManager.createQuery(cq);
		try {
			usuario = query.getSingleResult();
		} catch (NoResultException nre) {
		
		}

		return usuario;
	}

}
