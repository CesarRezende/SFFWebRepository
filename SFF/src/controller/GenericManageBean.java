package controller;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import data.Usuario;

public class GenericManageBean {
	

	protected Usuario usuario;
	
	public GenericManageBean(){
		this.usuario = (Usuario)this.getSessionObject("usr");
	}
	
	protected  Object getSessionObject(String atributeName){
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpSession session = (HttpSession) ec.getSession(false);
		
		return session.getAttribute(atributeName);
	}

	
	protected  void setSessionObject(String atributeName,Object atribute){
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpSession session = (HttpSession) ec.getSession(false);
		
		session.setAttribute(atributeName, atribute);
	}
	
	protected  String getRequestParameter(String requestParamenter){
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) ec.getRequest();
		
		return request.getParameter(requestParamenter);
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	
}
