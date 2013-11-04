/**
 * Copyright 2013 by dueni.ch
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
package ch.dueni.faces.view;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageWrapper;

/**
 * <code>SuppressRebuildViewViewDeclarationLanguageWrapper</code> implements
 * omit of second buildView() execution in render-response phase on post-back
 * request processing when hinted by {@link SuppressRebuildViewPhaseListener}.
 * For more information read javadoc on {@link SuppressRebuildViewPhaseListener}.
 * 
 * @author hampidu@gmail.com
 * 
 */
public class SuppressRebuildViewViewDeclarationLanguageWrapper extends ViewDeclarationLanguageWrapper {

	private static final Logger log = Logger.getLogger(SuppressRebuildViewViewDeclarationLanguageWrapper.class.getName());

	private ViewDeclarationLanguage wrapped;

	public SuppressRebuildViewViewDeclarationLanguageWrapper(ViewDeclarationLanguage wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ViewDeclarationLanguage getWrapped() {
		return wrapped;
	}

	/**
	 * Suppress buildView() call in render-response phase when hinted to do so and
	 * the ViewRoot did not change since restore-phase. Forward the to the wrapped
	 * method otherwise.
	 */
	@Override
	public void buildView(FacesContext context, UIViewRoot root) throws IOException {
		Map<Object, Object> attMap = context.getAttributes();
		if (PhaseId.RENDER_RESPONSE == context.getCurrentPhaseId()
				&& root == attMap.get(SuppressRebuildViewPhaseListener.RESTORED_VIEWROOT_KEY)
				&& Boolean.TRUE.equals(attMap.get(SuppressRebuildViewPhaseListener.SUPPRESS_REBUILD_VIEW_KEY))) {

			// suppress re-buildView() in render-response phase on post-back
			// when hinted.
			if (log.isLoggable(Level.FINE)) {
				log.fine("Suppressed re-execution of buildView() in render-response phase for this postback request.");
			}
			return;
		}
		getWrapped().buildView(context, root);
	}
}
