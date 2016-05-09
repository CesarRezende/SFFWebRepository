package controller;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(servletNames = { "Faces Servlet", "webservices" },filterName="JPAFilter")
public class JPAFilter implements Filter{

	private EntityManagerFactory factory;
	
	@Override
	public void destroy() {
		this.factory.close();
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		
		EntityManager entityManager = this.factory.createEntityManager();
		request.setAttribute("entityManager", entityManager);
		HttpServletRequest HttpRequest = (HttpServletRequest)request;
		HttpRequest.getSession().setAttribute("entityManagerFactory", this.factory);

		entityManager.getTransaction().begin();
		
		filterChain.doFilter(request, response);
		
		try{
			
			entityManager.getTransaction().commit();
			
		}catch(RuntimeException re){
			entityManager.getTransaction().rollback();
			re.printStackTrace();
			throw re;
			
		}catch(Exception e){
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			
		}finally{
			entityManager.close();
			
		}
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		this.factory = Persistence.createEntityManagerFactory("SFF");
		

	}

}
