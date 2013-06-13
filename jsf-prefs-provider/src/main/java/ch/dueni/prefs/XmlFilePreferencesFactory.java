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
package ch.dueni.prefs;

import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

/**
 * <code>JsfXmlPreferencesFactory</code> provides a {@link Preferences} implementation using XML
 * files as persistent store. It makes use of one XML file for the systemRoot tree and one XML file
 * per remote user userRoot preferences tree.
 * <p>
 * 
 * </p>
 * 
 * @author hampidu@gmail.com
 * 
 */
public class XmlFilePreferencesFactory implements PreferencesFactory {

	/**
	 * Access systemRoot Preferences tree cached on application scope (ServletContext).
	 */
	@Override
	public Preferences systemRoot() {
		return XmlFilePreferences.getSystemRoot();
	}

	/**
	 * Access userRoot Preferences tree cached on user session.
	 */
	@Override
	public Preferences userRoot() {
		return XmlFilePreferences.getUserRoot();
	}

}
