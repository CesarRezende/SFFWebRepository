package business;

import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import data.Usuario;

public class GenericRepository {
	protected EntityManager entityManager;
	protected HttpServletRequest request;
	protected Usuario usuario;

	public GenericRepository() {
		this.setEntityManager(this.getManager());
		this.setUsuario(getCurrentUsuario());
	}

	public GenericRepository(HttpServletRequest request) {
		this.request= request;
		this.setEntityManager(this.getManager());
		this.setUsuario(getCurrentUsuario());
	}
	
	protected <E> void save(E entity) {
		updateEntityManager();
		this.entityManager.persist(entity);
		this.entityManager.flush();

	}

	protected <E> void update(E entity) {
		updateEntityManager();
		this.entityManager.merge(entity);
		this.entityManager.flush();

	}

	protected <E> void remove(E entity) {
		updateEntityManager();
		this.entityManager.remove(entity);
		this.entityManager.flush();

	}

	protected <E> List<E> getAll(Class<E> e) {
		updateEntityManager();
		CriteriaBuilder criteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(e);
		Root<E> root = criteriaQuery.from(e);
		criteriaQuery.select(root);

		TypedQuery<E> query = this.entityManager.createQuery(criteriaQuery);
		List<E> listEntity = query.getResultList();

		return listEntity;
	}

	public void updateEntityManager() {
		this.setEntityManager(this.getManager());
	}
	
	public void getnewEntityManager() {
		FacesContext fc = FacesContext.getCurrentInstance();
		if(fc != null){
			ExternalContext ec = fc.getExternalContext();
			this.request = (HttpServletRequest) ec.getRequest();
			EntityManagerFactory factory = (EntityManagerFactory) request.getAttribute("entityManagerFactory");
			EntityManager entityManager = factory.createEntityManager();
			this.setEntityManager(entityManager);
			
		}else{
			EntityManagerFactory factory = (EntityManagerFactory) request.getSession().getAttribute("entityManagerFactory");
			EntityManager entityManager = factory.createEntityManager();
			this.setEntityManager(entityManager);
			
		}
		
		
	}
	
	private EntityManager getManager() {
		FacesContext fc = FacesContext.getCurrentInstance();
		if(fc != null){
			ExternalContext ec = fc.getExternalContext();
			this.request = (HttpServletRequest) ec.getRequest();
			return (EntityManager) request.getAttribute("entityManager");
		}else{
			if(request != null)
				return (EntityManager) request.getAttribute("entityManager");
			else
				return this.entityManager;
		}
		
	}
	
	public Usuario getCurrentUsuario() {
		FacesContext fc = FacesContext.getCurrentInstance();
		if(fc != null){
			ExternalContext ec = fc.getExternalContext();
			this.request = (HttpServletRequest) ec.getRequest();
			return (Usuario) request.getSession().getAttribute("usr");
		}else{
			if(request != null)
				return (Usuario) request.getAttribute("usr");
			return this.usuario;
		}
		
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Usuario getUsuario() {
		return this.usuario;
		
	}
}
