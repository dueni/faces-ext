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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 * <code>JsfXmlPreferences</code> implements {@link Preferences} handling systemRoot on application
 * scope and userRoot on session scope persisting to one XML file per preferences tree (1 systemRoot
 * tree and 1 userRoot tree per user).
 * 
 * @author hampidu@gmail.com
 */
public class JsfXmlPreferences extends AbstractPreferences {

	public static final String SCOPE_KEY = JsfXmlPreferences.class.getName();

	private Map<String, String> valueMap;

	private Map<String, JsfXmlPreferences> childPrefs;

	private Root root;

	private static final String FILE_NAME_PREFIX = "JsfXmlPreferences-";
	private static final String FILE_NAME_SUFFIX = ".xml";

	private static final String SERVLET_CONTEXT_TEMPDIR = "javax.servlet.context.tempdir";

	private enum Root {
		system, user
	}

	protected JsfXmlPreferences(JsfXmlPreferences parent, String name) {
		super(parent, name);
	}

	void setRoot(Root root) {
		this.root = root;
	}

	Root getRoot() {
		return root;
	}

	@Override
	protected void putSpi(String key, String value) {
		if (valueMap == null) {
			valueMap = new HashMap<String, String>();
		}
		valueMap.put(key, value);
	}

	@Override
	protected String getSpi(String key) {
		return (valueMap == null) ? null : valueMap.get(key);
	}

	@Override
	protected void removeSpi(String key) {
		valueMap.remove(key);
	}

	@Override
	protected void removeNodeSpi() throws BackingStoreException {
		((JsfXmlPreferences) parent()).childPrefs.remove(this.name());
	}

	@Override
	protected String[] keysSpi() throws BackingStoreException {
		if (valueMap == null) {
			return new String[0];
		}
		return valueMap.keySet().toArray(new String[0]);
	}

	@Override
	protected String[] childrenNamesSpi() throws BackingStoreException {
		if (childPrefs == null) {
			return new String[0];
		}
		return childPrefs.keySet().toArray(new String[0]);
	}

	@Override
	protected AbstractPreferences childSpi(String name) {
		if (childPrefs == null) {
			childPrefs = new HashMap<String, JsfXmlPreferences>();
		}
		JsfXmlPreferences child = childPrefs.get(name);
		if (child == null) {
			child = new JsfXmlPreferences(this, name);
			childPrefs.put(name, child);
		}
		return child;
	}

	@Override
	public void sync() throws BackingStoreException {
	}

	@Override
	protected void syncSpi() throws BackingStoreException {
		throw new UnsupportedOperationException(
				"Since this implementation manages full tree at once sync() method most be overridden!");
	}

	@Override
	public void flush() throws BackingStoreException {
		JsfXmlPreferences p = this;
		while (p.parent() != null) {
			p = (JsfXmlPreferences) p.parent();
		}
		Root type = p.getRoot();
		ExternalContext extCtx = getExternalContext();
		storePreferencesTree(extCtx, type);
	}

	synchronized void storePreferencesTree(ExternalContext extCtx, Root root) {
		try {
			File storeFile = ensureStoreFile(extCtx, root);
			JsfXmlPreferences prefs = Root.user == root ? JsfXmlPreferences.getUserRoot()
					: JsfXmlPreferences.getSystemRoot();
			if (!storeFile.canWrite()) {
				storeFile.setWritable(true);
				if (!storeFile.exists()) {
					storeFile.createNewFile();
				}
				FileOutputStream os = new FileOutputStream(storeFile);
				prefs.exportSubtree(os);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void flushSpi() throws BackingStoreException {
		throw new UnsupportedOperationException(
				"Since this implementation manages full tree at once flush() method most be overridden!");
	}

	public static JsfXmlPreferences getSystemRoot() {
		ExternalContext extCtx = getExternalContext();
		Map<String, Object> appScope = extCtx.getApplicationMap();
		JsfXmlPreferences systemRoot = (JsfXmlPreferences) appScope.get(SCOPE_KEY);
		if (systemRoot == null) {
			systemRoot = new JsfXmlPreferences(null, "");
			appScope.put(SCOPE_KEY, systemRoot);
			importPreferencesTree(extCtx, Root.system);
		}
		return systemRoot;
	}

	private static ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}

	public static JsfXmlPreferences getUserRoot() {
		ExternalContext extCtx = getExternalContext();
		Map<String, Object> sessionMap = extCtx.getSessionMap();
		JsfXmlPreferences userRoot = (JsfXmlPreferences) sessionMap.get(SCOPE_KEY);
		if (userRoot == null) {
			userRoot = new JsfXmlPreferences(null, "");
			sessionMap.put(SCOPE_KEY, userRoot);
			importPreferencesTree(extCtx, Root.user);
		}
		return userRoot;
	}

	static void importPreferencesTree(ExternalContext extCtx, Root root) {
		try {
			File storeFile = ensureStoreFile(extCtx, root);

			if (storeFile.canRead()) {
				InputStream in = new FileInputStream(storeFile);
				Preferences.importPreferences(in);
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	private static File ensureStoreFile(ExternalContext extCtx, Root root) {
		String prefsDir = WebXmlConfig.PersistenceDir.getValue();
		File storeDir = null;
		if (prefsDir == null) {
			ServletContext servletContext = (ServletContext) extCtx.getContext();
			File tempDir = (File) servletContext.getAttribute(SERVLET_CONTEXT_TEMPDIR);
			storeDir = new File(tempDir, "prefs-store");
		} else {
			storeDir = new File(prefsDir, "prefs-store");
		}
		if (!storeDir.exists()) {
			storeDir.mkdirs();
		}
		String name = root.toString();
		if (root == Root.user) {
			name = name + "-" + extCtx.getRemoteUser();
		}
		String fileName = FILE_NAME_PREFIX + name + FILE_NAME_SUFFIX;
		File xmlFile = new File(storeDir, fileName);
		if (!xmlFile.exists()) {
			// TODO: copy initial state file into store-dir			

		}
		return xmlFile;
	}
}
