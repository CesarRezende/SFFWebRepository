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
import javax.servlet.http.HttpSession;

@WebFilter(servletNames = { "webservices" }, filterName = "WSFilter")
public class WSFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		filterChain.doFilter(request, response);

		HttpServletRequest HttpRequest = (HttpServletRequest) request;
		HttpSession session = HttpRequest.getSession(true);

		if (session.getAttribute("autenticado") == null)
			session.setMaxInactiveInterval(1);

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

}
