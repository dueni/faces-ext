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
package ch.dueni.faces.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

/**
 * <code>PreRenderViewEventListener</code> acts as a mediator for components
 * that need to be notified before view rendering starts.
 * <p>
 * Since {@link PreRenderViewEvent} is application scoped, UIComponents should
 * not act as listener for such event directly. This mediator manages the
 * listeners on ViewRequestMap - therefore the listeners are registered only
 * during a request and as long as the ViewRoot does not change.
 * </p>
 * 
 * @author hampidu@gmail.com
 * 
 */
// TODO: system event listener registration does not work (verify if JSF 2.2 fixes this)
// - use configuration to register this listener
// @ListenerFor(systemEventClass = PreRenderViewEvent.class)
public class PreRenderViewEventMediator implements SystemEventListener {

	public static final String SUBSCRIBED_LIST_KEY = PreRenderViewEventMediator.class.getName();

	public static final String SAVED_VIEW_ROOT_KEY = SUBSCRIBED_LIST_KEY + ".savedViewRoot";

	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		FacesContext jsf = FacesContext.getCurrentInstance();
		List<PreRenderViewEventListener> listeners = getListeners(jsf, false);
		for (PreRenderViewEventListener listener : listeners) {
			listener.processPreRenderViewEvent(jsf);
		}
	}

	@Override
	public boolean isListenerForSource(Object source) {
		return (source instanceof UIViewRoot);
	}

	/**
	 * Subscribe to be notified on PreRenderViewEvent.
	 * 
	 * @param listener
	 *          the PreRenderViewEventListener instance to notify.
	 */
	public static void subscribe(PreRenderViewEventListener listener) {
		List<PreRenderViewEventListener> listeners = getListeners(null, true);
		listeners.add(listener);
	}

	/**
	 * Unsubscribe from being notified on PreRenderViewEvent - this is only needed
	 * if a component wants to unsubscribe during a request. The listeners are
	 * lost after the request anyway.
	 * 
	 * @param listener
	 *          the PreRenderViewEventListener instance to notify.
	 */
	public static void unsubscribe(PreRenderViewEventListener listener) {
		List<PreRenderViewEventListener> listeners = getListeners(null, false);
		listeners.remove(listener);
	}

	/**
	 * Handle the subscriber list on FacesContext attributes Map - the list is
	 * dropped when UIViewRoot instance changes.
	 * 
	 * @param jsf
	 *          current FacesContext
	 * @param create
	 *          true to create the listeners List
	 * @return List<PreRenderViewEventListener> or Collections.emptyList().
	 */
	@SuppressWarnings("unchecked")
	private static List<PreRenderViewEventListener> getListeners(FacesContext jsf, boolean create) {
		Map<Object, Object> attMap = jsf.getAttributes();
		// savedRoot is needed to detect changed view
		UIViewRoot savedRoot = (UIViewRoot) attMap.get(SAVED_VIEW_ROOT_KEY);
		List<PreRenderViewEventListener> listeners = null;
		if (savedRoot == jsf.getViewRoot()) {
			// we have unchanged view root, retrieve the listeners List
			listeners = (List<PreRenderViewEventListener>) attMap.get(SUBSCRIBED_LIST_KEY);
		}
		if (listeners == null) {
			// create new listeners List or return Collections.emptyList()
			if (create) {
				listeners = new ArrayList<PreRenderViewEventListener>();
				attMap.put(SUBSCRIBED_LIST_KEY, listeners);
				attMap.put(SAVED_VIEW_ROOT_KEY, jsf.getViewRoot());
			} else {
				listeners = Collections.emptyList();
			}
		}
		return listeners;
	}

}
