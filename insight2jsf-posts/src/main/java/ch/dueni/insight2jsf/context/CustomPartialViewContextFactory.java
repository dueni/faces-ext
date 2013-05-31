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
package ch.dueni.insight2jsf.context;

import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextFactory;

/**
 * <code>CustomPartialViewContextFactory</code> is needed to wrap the standard
 * {@link PartialViewContext} with {@link CustomPartialViewContext}.
 * <p>
 * To activate CustomPartialViewContextFactory add the following xml fragment to your
 * faces-config.xml file.
 * <pre>
 * <factory>
 *  <partial-view-context-factory>
 *     ch.dueni.jsftips.context.CustomPartialViewContextFactory
 *  </partial-view-context-factory>
 * </factory> 
 * </pre>
 * </p>
 *
 * @author Hanspeter D&uuml;nnenberger
 */
public class CustomPartialViewContextFactory extends PartialViewContextFactory {

	/**
	 * The wrapped standard PartialViewContextFactory.
	 */
	private PartialViewContextFactory wrapped;

	/**
	 * Create an instance with wrapped delegate.
	 *
	 * @param delegate the wrapped delegate.
	 */
	public CustomPartialViewContextFactory(PartialViewContextFactory delegate) {
		this.wrapped = delegate;
	}

	/**
	 * Return a new instance of {@link CustomPartialViewContextWrapper} wrapping the
	 * {@link PartialViewContext} obtained from the wrapped {@link PartialViewContextFactory}.
	 */
	@Override
	public PartialViewContext getPartialViewContext(FacesContext context) {
		PartialViewContext partialViewContext = getWrapped().getPartialViewContext(context);
		return new CustomPartialViewContext(partialViewContext);
	}

	@Override
	public PartialViewContextFactory getWrapped() {
		return wrapped;
	}

}
