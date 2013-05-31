/**
 * Copyright 2010 by dueni.ch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.dueni.insight2jsf.bean;

import ch.dueni.insight2jsf.util.JsfUtil;
import java.util.HashMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * <code>Bindings</code> bean allows simple component reference mapping for e.g. when one component
 * needs the clientId of another component on the page.
 * <p>
 * <b>Warning:</b> never inject or assign this bean to another bean with any scope different than
 * "request". Component binding must only be used with request scoped beans!
 * </p>
 * <p>
 * Usage example:
 * </p>
 * 
 * <pre>
 * <code>
 * <h:panelGroup>
 *   <my:compositeComp ... reRender="#{bindings.firstCountry.clientId}" ... />
 * 
 *   <h:outputLabel value="The selected country:" />
 * 
 *   <h:outputText value="#{autoComplete.firstCountry}" id="firstCountry" binding="#{bindings.firstCountry}" />
 * </h:panelGroup>
 * </code>
 * </pre>
 * 
 * </p>
 * 
 * @author Hanspeter D&uuml;nnenberger
 * 
 */
@ManagedBean(name = "bindings")
@RequestScoped
public class Bindings extends HashMap<String, UIComponent> {

	static final long serialVersionUID = 1L;

	/**
	 * ViewRoot reference to allow clearing the bindings map when ViewRoot changed - might use future
	 * ViewChangedEvent once JSF 2.1(?) provides it.
	 */
	private transient UIViewRoot viewRoot;

	/**
	 * cached FacesContext.
	 */
	private transient FacesContext facesContext;

	/**
	 * Return the current Bindings instance for easy access from other beans. Never assign this
	 * Bindings instance to any bean attribute on beans that are not request scoped.
	 * 
	 * @return the current Bindings instance for easy access from other beans.
	 */
	public static Bindings getInstance() {
		return JsfUtil.resolveManagedBean("bindings", Bindings.class);
	}

	/**
	 * Return the current Bindings instance for easy access from other beans. Never assign this
	 * Bindings instance to any bean attribute on beans that are not request scoped.
	 * 
	 * @param jsf
	 *          current FacesContext.
	 * @return the current Bindings instance for easy access from other beans.
	 */
	public static Bindings getInstance(FacesContext jsf) {
		return JsfUtil.resolveManagedBean(jsf, "bindings", Bindings.class);
	}

	/**
	 * Put a binding to the bindings map.
	 * 
	 * @param key
	 *          the name of the binding.
	 * @param value
	 *          the bound UIComponent.
	 * @return previously binded UIComponent if one was already bound.
	 */
	@Override
	public UIComponent put(String key, UIComponent value) {
		clearIfViewRootChanged();
		return super.put(key, value);
	}

	/**
	 * Return a bounded UIComponent.
	 * 
	 * @param key
	 *          the name of the binding.
	 * @return the bound UIComponent.
	 */
	@Override
	public UIComponent get(Object key) {
		clearIfViewRootChanged();
		return super.get(key);
	}

	/**
	 * Clear bindings Map if ViewRoot instance has changed.
	 */
	private void clearIfViewRootChanged() {
		UIViewRoot currentViewRoot = getFacesContext().getViewRoot();
		if (viewRoot != currentViewRoot) {
			clear();
			viewRoot = currentViewRoot;
		}
	}

	/**
	 * Return cached current instance of FacesContext.
	 * 
	 * @return cached current instance of FacesContext.
	 */
	private FacesContext getFacesContext() {
		if (facesContext == null || facesContext.isReleased()) {
			facesContext = FacesContext.getCurrentInstance();
		}
		return facesContext;
	}
}
