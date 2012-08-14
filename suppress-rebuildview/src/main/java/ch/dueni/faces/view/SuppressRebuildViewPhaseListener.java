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
package ch.dueni.faces.view;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * <code>SuppressRebuildViewPhaseListener</code> can be used on views using
 * <code>&lt;f:phaseListener type="com.csg.jsf.view.SuppressRebuildViewPhaseListener" /&gt;</code>
 * on the page definition to signal to omit the second execution of buildView() in the
 * render-response phase. The actual omit of the buildView() call is handled in
 * {@link SuppressRebuildViewViewDeclarationLanguageWrapper}.
 * <p>
 * During JSF post-back requests, buildView() is executed once in the restore-view phase to process
 * the request parameters (execution phases) and a second time in the render-response phase to make
 * sure that changed includes or templates are used for the resulting component tree. JSTL tags and
 * EL expressions like &lt;ui:composition
 * template="#{condition ? 'include1.xhtml' : 'include2.xhtml'}"&gt; allow to change the used
 * includes during request processing - that is why the second execution of buildView() in the
 * render-response phase is necessary. However, if no such dynamic includes are used or the page
 * author is sure they will not change on the current page, the second execution of buildView() can
 * be omitted to reduce request processing time.
 * </p>
 * <p>
 * Within restore-view phase after restoring the view, JSF RI put the ViewRoot as a key to
 * FacesContext attributes map and later uses it to verify that the ViewRoot did not change during
 * the request. To suppress the second execution of buildView() it is required to know that ViewRoot
 * did not change after restoring the view. However, to make sure this works with any JSF
 * implementation we cannot rely on JSF RI specifics. Therefore
 * {@link SuppressRebuildViewPhaseListener} also adds the restored ViewRoot to FacesContext
 * attributes map using the key {@link SuppressRebuildViewPhaseListener#RESTORED_VIEWROOT_KEY}.
 * <p>
 * When FacesContext attributes map key
 * {@link SuppressRebuildViewPhaseListener#RESTORED_VIEWROOT_KEY} contains Boolean.TRUE and the
 * current ViewRoot to be rendered is the same instance as stored in FacesContext attributes map on
 * key {@link #RESTORED_VIEWROOT_KEY}, {@link SuppressRebuildViewViewDeclarationLanguageWrapper}
 * will suppress the second execution of buildView() on that view.
 * </p>
 * 
 * @author hampidu@gmail.com
 */
public class SuppressRebuildViewPhaseListener implements PhaseListener {

	/**
	 * The key on FacesContext attributes map to signal omit of second buildView() execution using a
	 * Boolean.TRUE as value - any other value does not suppress buildView() execution.
	 */
	public static final String SUPPRESS_REBUILD_VIEW_KEY = "ch.dueni.faces.suppressRebuildView";

	/**
	 * The key on FacesContext attributes map to keep the viewRoot as it was built in restore-view
	 * phase to only suppress buildView() execution of viewRoot did not change during the request
	 * processing.
	 */
	public static final String RESTORED_VIEWROOT_KEY = "ch.dueni.faces.restoredViewRoot";

	private static final long serialVersionUID = 1L;

	/**
	 * Do nothing during afterPhase processing.
	 */
	public void afterPhase(PhaseEvent event) {
		// do nothing
	}

	/**
	 * Add two attributes to FacesContext attributes map to suppress a second execution of buildView()
	 * during render-response phase if the ViewRoot does not change and no includes will change during
	 * the request. The two attributes that are added:
	 * <ul>
	 * <li><b>Key: {@link #RESTORED_VIEWROOT_KEY}</b> - value: ViewRoot after restore-view phase</li>
	 * <li><b>Key: {@link #SUPPRESS_REBUILD_VIEW_KEY}</b> - value: {@link Boolean#TRUE}</li>
	 * </ul>
	 */
	public void beforePhase(PhaseEvent event) {
		FacesContext jsf = event.getFacesContext();
		Map<Object, Object> attMap = jsf.getAttributes();
		attMap.put(SUPPRESS_REBUILD_VIEW_KEY, Boolean.TRUE);
		attMap.put(RESTORED_VIEWROOT_KEY, jsf.getViewRoot());
	}

	/**
	 * This PhaseListener hooks into {@link PhaseId#APPLY_REQUEST_VALUES} Phase.
	 */
	public PhaseId getPhaseId() {
		return PhaseId.APPLY_REQUEST_VALUES;
	}

}
