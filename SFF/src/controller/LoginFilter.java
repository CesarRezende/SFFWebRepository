package controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(servletNames = { "Faces Servlet" }, filterName="LoginFilter")
public class LoginFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();

		if (session.getAttribute("autenticado") != null
				|| req.getServletPath().endsWith("index.xhtml")) {

			chain.doFilter(request, response);
		}else{
			HttpServletResponse resp = (HttpServletResponse) response;
			resp.sendRedirect("index.xhtml");
			
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {


	}

}
