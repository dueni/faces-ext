/**
 * Copyright 2009 by dueni.ch
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

package ch.dueni.jsf.ext.multivalidator;

import java.util.ArrayList;
import java.util.Collection;

import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 * <code>MultiValidator</code>
 * 
 * @author Hanspeter D&uuml;nnenberger
 */
public class MultiValidator extends UIInput {

	public static final String COMPONENT_TYPE = "ch.dueni.jsf.component.MultiValidator";

	private static final String COMPONENT_FAMILY = "ch.dueni.jsf.component.MulitValidator";

	private String forIds;

	/**
	 * The MethodExpression for the validate method.
	 */
	private MethodExpression validateMethod;

	private transient boolean rendered = false;

	/**
	 * Default constructor.
	 */
	public MultiValidator() {
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public String getRendererType() {
		return null;
	}

	/**
	 * Override {@link #validate(FacesContext)} to handle the multi validations.
	 * 
	 * @see javax.faces.component.UIInput#validate(javax.faces.context.FacesContext)
	 */
	@Override
	public void validate(FacesContext context) {
		if (forIds == null) {
			throw new IllegalArgumentException("Tag attribute 'forIds' must not be null.");
		}

		Collection<UIComponent> components = new ArrayList<UIComponent>();
		Collection<Object> values = new ArrayList<Object>();

		String[] forIdsArray = forIds.split(",");
		for (String forId : forIdsArray) {
			UIComponent cmp = findComponent(forId);
			components.add(cmp);
			EditableValueHolder e = (EditableValueHolder) cmp;
			values.add(e.getValue());
		}

		try {
			validateMethod.invoke(context.getELContext(), new Object[] { context, components, values });
		} catch (Throwable th) {
			if (th instanceof ValidatorException) {
				handleValidatorException(context, components, (ValidatorException) th);
			} else if (th.getCause() instanceof ValidatorException) {
				handleValidatorException(context, components, (ValidatorException) th.getCause());
			}
		}
	}

	/**
	 * Handle {@link ValidatorException} by adding the contained
	 * {@link FacesMessage} to all involved {@link UIComponent}s.
	 * 
	 * @param fc
	 *          current FacesContext.
	 * @param components
	 *          the Collection of UIComponent involved in multi validation.
	 * @param ve
	 *          the ValueException to handle.
	 */
	private void handleValidatorException(FacesContext fc, Collection<UIComponent> components,
			ValidatorException ve) {
		FacesMessage msg = ve.getFacesMessage();
		for (UIComponent cmp : components) {
			fc.addMessage(cmp.getClientId(fc), msg);
			if (cmp instanceof EditableValueHolder) {
				((EditableValueHolder) cmp).setValid(false);
			}
		}
	}

	/**
	 * Returns false always except during processValidators() - MultiValidator
	 * must not be rendered and must not take any space in PanelGrid - but for
	 * {@link #processValidators(FacesContext)} this must be true.
	 * 
	 * @see javax.faces.component.UIComponentBase#isRendered()
	 */
	@Override
	public boolean isRendered() {
		return rendered;
	}

	/**
	 * Returns false - this invisible component should not have any children.
	 * 
	 * @see javax.faces.component.UIComponentBase#getRendersChildren()
	 */
	@Override
	public boolean getRendersChildren() {
		return false;
	}

	/**
	 * @see javax.faces.component.UIInput#restoreState(javax.faces.context.FacesContext,
	 *      java.lang.Object)
	 */
	@Override
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		forIds = (String) values[1];
		validateMethod = (MethodExpression) restoreAttachedState(context, values[2]);
	}

	/**
	 * @see javax.faces.component.UIInput#saveState(javax.faces.context.FacesContext)
	 */
	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = forIds;
		values[2] = saveAttachedState(context, validateMethod);
		return values;
	}

	/**
	 * Since this hidden component returns always false for isRendered() for all
	 * the other phases, during processValidators we need to set rendered to true
	 * and then let the super class do it's work.
	 * 
	 * @see javax.faces.component.UIInput#processValidators(javax.faces.context.FacesContext)
	 */
	@Override
	public void processValidators(FacesContext context) {
		rendered = true;
		super.processValidators(context);
		rendered = false;
	}

	/**
	 * Return a comma separated list of referenced component id's of the
	 * components that are involved in the cross validation.
	 * 
	 * @return a comma separated list of referenced component id's.
	 */
	public String getForIds() {
		return forIds;
	}

	/**
	 * Set the comma separated list of referenced component id's.
	 * 
	 * @param forIds
	 *          comma separated list of referenced UIComponent id's.
	 */
	public void setForIds(String forIds) {
		this.forIds = forIds;
	}

	/**
	 * Return the MethodExpression used to invoke the multi-validation.
	 * 
	 * @return the MethodExpression used to invoke the multi-validation.
	 * @see #setValidateMethod(MethodExpression)
	 */
	public MethodExpression getValidateMethod() {
		return validateMethod;
	}

	/**
	 * Set the validateMethod which must comply to the signature
	 * 
	 * <code>public void multiValidate(FacesContext context, Collection<UIComponent> components, Collection<Object> values) throws ValidatorException ;</code>
	 * to perform cross validation.
	 * 
	 * @param validateMethod
	 *          the validate MethodExpression.
	 */
	public void setValidateMethod(MethodExpression validateMethod) {
		this.validateMethod = validateMethod;
	}

}
