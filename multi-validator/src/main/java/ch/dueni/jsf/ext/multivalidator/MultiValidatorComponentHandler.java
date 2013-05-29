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

import java.util.Collection;

import javax.el.MethodExpression;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;

/**
 * <code>MultiValidatorComponentHandler</code> applies an instance of
 * {@link MultiValidator} to the parent {@link UIComponent} which must be of
 * type {@link EditableValueHolder}.
 * <p>
 * MultiValidator has two mandatory tag attributes:
 * </p>
 * <dl>
 * <dt>forIds</dt>
 * <dd>A comma separated list of {@link UIComponent} id's that are to be
 * involved in validation.</dd>
 * <dt>validator</dt>
 * <dd>The validate method to be invoked - the method must provide the following
 * signature
 * <code>public void multiValidate(FacesContext context, Collection<UIComponent> components, Collection<Object> values) throws ValidatorException ;</code>
 * .</dd>
 * </dl>
 * 
 * @author Hanspeter D&uuml;nnenberger
 * @author Amar Pandav
 */
public class MultiValidatorComponentHandler extends ComponentHandler {

	public MultiValidatorComponentHandler(ComponentConfig config) {
		super(config);
	}

	/**
	 * Set the method-attributes on data table like filterOnAction,
	 * filterOffAction.
	 * 
	 * @param ctx
	 *          facelet-context
	 * @param instance
	 *          component-instance
	 */
	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {
		super.setAttributes(ctx, instance);

		MultiValidator multiValidator = (MultiValidator) instance;

		/** A comma separated list of UIComponent id's to be involved in validation. */
		TagAttribute forIds = this.getRequiredAttribute("forIds");
		String strForIds = forIds.getValue(ctx);
		if (strForIds == null || strForIds.trim().length() == 0) {
			throw new IllegalArgumentException("Mandatory attribute: 'forIds' must be not null or empty!");
		}
		multiValidator.setForIds(strForIds);

		/** The validator method to be called for validation. */
		TagAttribute validator = this.getRequiredAttribute("validator");
		MethodExpression validateMethod = validator.getMethodExpression(ctx, null, new Class[] {
				FacesContext.class, Collection.class, Collection.class });
		multiValidator.setValidateMethod(validateMethod);

	}

}
