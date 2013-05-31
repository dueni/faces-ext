/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dueni.insight2jsf.util;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;

/**
 * JsfUtil implements some tasks frequently used when using JSF.
 * 
 * @author Hanspeter D&uuml;nnenberger
 */
public abstract class JsfUtil {

	/**
	 * Return the managed bean that is resolved by the given name and is of the given type.
	 * 
	 * @param beanName
	 *          the name of the bean.
	 * @param T
	 *          expected type.
	 */
	public static <T extends Object> T resolveManagedBean(String beanName, Class<T> T) {
		return resolveManagedBean(FacesContext.getCurrentInstance(), beanName, T);
	}

	/**
	 * Return the managed bean that is resolved by the given name and is of the given type.
	 * 
	 * @param jsf
	 *          current FacesContext.
	 * @param beanName
	 *          the name of the bean.
	 * @param T
	 *          expected type.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> T resolveManagedBean(FacesContext jsf, String beanName,
			Class<T> T) {
		ELResolver elResolver = jsf.getApplication().getELResolver();
		ELContext elContext = jsf.getELContext();

		return (T)elResolver.getValue(elContext, null, beanName);
	}
}
