/*
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

import ch.dueni.insight2jsf.bean.Bindings;
import java.util.Collection;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextWrapper;
import javax.faces.event.PhaseId;

/**
 * <code>CustomPartialViewContextWrapper</code> wraps the standard {@link PartialViewContext} to add
 * some clientId's of components that must always be rendered.
 *
 * @author Hanspeter D&uuml;nnenberger
 */
public class CustomPartialViewContext extends PartialViewContextWrapper {

  /**
   * The wrapped standard PartialViewContext.
   */
  private PartialViewContext wrapped;
  /**
   * Flag to add the id's only once.
   */
  private boolean added = false;

  /**
   * Create an instance with wrapped delegate.
   *
   * @param delegate the wrapped delegate.
   */
  public CustomPartialViewContext(PartialViewContext delegate) {
	wrapped = delegate;
  }

  @Override
  public PartialViewContext getWrapped() {
	return wrapped;
  }

  @Override
  public void setPartialRequest(boolean isPartialRequest) {
	getWrapped().setPartialRequest(isPartialRequest);
  }

  /**
   * Override getRenderIds() to add the clientId of components to always render. Wrapping of
   * renderIds list will happen
   *
   * @see PartialViewContext#getRenderIds()
   */
  @Override
  public Collection<String> getRenderIds() {
	if (!added) {
	  FacesContext jsf = FacesContext.getCurrentInstance();
	  if (PhaseId.RENDER_RESPONSE == jsf.getCurrentPhaseId()) {
		Bindings bindings = Bindings.getInstance(jsf);
		// h:messiges may be bound using binding="#{bindings.messages}" - get bounded component
		UIComponent component = bindings.get("messages");
		if (component != null && component.isRendered()) {
		  String clientId = component.getClientId(jsf);
		  Collection<String> renderIds = getWrapped().getRenderIds();
		  if (!renderIds.contains(clientId)) {
			renderIds.add(clientId);
		  }
		}
	  }
	}
	return getWrapped().getRenderIds();
  }
}
