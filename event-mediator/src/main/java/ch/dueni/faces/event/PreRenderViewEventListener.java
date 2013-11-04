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

import javax.faces.context.FacesContext;
import javax.faces.event.PreRenderViewEvent;

/**
 * Any class (including UIComponent subclasses) implementing
 * {@link PreRenderViewEventListener} may subscribe on
 * {@link PreRenderViewEventMediator#subscribe(PreRenderViewEventListener)} to
 * be notified before view rendering begins (PreRenderViewEvent on UIViewRoot).
 * Since {@link PreRenderViewEvent} is application scoped
 * {@link PreRenderViewEventMediator} is a single listener to mediate the
 * event to subscribed listeners on request scope.
 * 
 * @author hampidu@gmail.com
 */
public interface PreRenderViewEventListener {

	/**
	 * Process PreRenderViewEvent on component/bean/any-class level.
	 * 
	 * @param jsf
	 *          current FacesContext.
	 */
	public void processPreRenderViewEvent(FacesContext jsf);

}
